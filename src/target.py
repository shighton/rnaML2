import cv2
import os
import numpy as np
from random import shuffle
from tqdm import tqdm
from PIL import Image
from numpy import asarray
import random

imgMiss = Image.open('/Users/shighton/eclipse-workspace/rnaML/src/rnaML/targetMiss.png')
imgHit = Image.open('/Users/shighton/eclipse-workspace/rnaML/src/rnaML/targetFind.png')

imgMiss = asarray(imgMiss)
imgHit = asarray(imgHit)
tilesMiss = [imgMiss[x:x + 25, y:y + 22] for x in range(0, imgMiss.shape[0], 25) for y in
             range(0, imgMiss.shape[1], 22)]
tilesHit = [imgHit[x:x + 25, y:y + 22] for x in range(0, imgHit.shape[0], 25) for y in range(0, imgHit.shape[1], 22)]

numMiss = 1
numHit = 1

for tile in tilesMiss:
    pilImg = Image.fromarray(tile)
    im1 = pilImg.save("/Users/shighton/eclipse-workspace/rnaML/src/rnaML/trainML/targetMiss%s.jpg" % (numMiss))
    numMiss += 1

for tile in tilesHit:
    pilImg = Image.fromarray(tile)
    im1 = pilImg.save("/Users/shighton/eclipse-workspace/rnaML/src/rnaML/trainML/targetHit%s.jpg" % (numHit))
    numHit += 1

TRAIN_DIR = '/Users/shighton/eclipse-workspace/rnaML/src/rnaML/trainML'
TEST_DIR = '/Users/shighton/eclipse-workspace/rnaML/src/rnaML/testML'
IMG_X = 22
IMG_Y = 25
LR = 1e-3
MODEL_NAME = 'rnaML-{}-{}.model'.format(LR, '6conv-basic')


def label_img(imag):
    word_label = imag.split('.')[0]
    word_label = word_label.rstrip('0123456789')
    if word_label == 'targetMiss':
        return [1, 0]
    elif word_label == 'targetHit':
        return [0, 1]


def create_train_data():
    training_data = []
    for img in tqdm(os.listdir(TRAIN_DIR)):
        label = label_img(img)
        path = os.path.join(TRAIN_DIR, img)
        img = cv2.imread(path, cv2.IMREAD_GRAYSCALE)
        training_data.append([np.array(img), np.array(label)])
    shuffle(training_data)
    np.save('train_data.npy', training_data)
    return training_data


def process_test_data():
    testing_data = []
    for img in tqdm(os.listdir(TEST_DIR)):
        path = os.path.join(TEST_DIR, img)
        img_num = [int(s) for s in img.split() if s.isdigit()]
        img = cv2.imread(path, cv2.IMREAD_GRAYSCALE)
        img = cv2.resize(img, (IMG_X, IMG_Y))
        testing_data.append([np.array(img), img_num])
    shuffle(testing_data)
    np.save('test_data.npy', testing_data)
    return testing_data


train_data = create_train_data()
test_data = process_test_data()

import tflearn
from tflearn.layers.conv import conv_2d, max_pool_2d
from tflearn.layers.core import input_data, dropout, fully_connected
from tflearn.layers.estimator import regression

import tensorflow as tf

convnet = input_data(shape=[None, IMG_X, IMG_Y, 1], name='input')

convnet = conv_2d(convnet, 32, 5, activation='relu')
convnet = max_pool_2d(convnet, 5)

convnet = conv_2d(convnet, 64, 5, activation='relu')
convnet = max_pool_2d(convnet, 5)

convnet = conv_2d(convnet, 128, 5, activation='relu')
convnet = max_pool_2d(convnet, 5)

convnet = conv_2d(convnet, 64, 5, activation='relu')
convnet = max_pool_2d(convnet, 5)

convnet = conv_2d(convnet, 32, 5, activation='relu')
convnet = max_pool_2d(convnet, 5)

convnet = fully_connected(convnet, 1024, activation='relu')
convnet = dropout(convnet, 0.8)

convnet = fully_connected(convnet, 2, activation='softmax')
convnet = regression(convnet, optimizer='adam', learning_rate=LR, loss='categorical_crossentropy', name='targets')

model = tflearn.DNN(convnet, tensorboard_dir='log')

train = train_data[:-150]
test = train_data[-150:]

X = np.array([i[0] for i in train]).reshape(-1, 1, 1, 1)
Y = [i[1] for i in train]
test_x = np.array([i[0] for i in test]).reshape(-1, 1, 1, 1)
test_y = [i[1] for i in test]

model.fit({'input': X}, {'targets': Y}, n_epoch=5, validation_set=({'input': test_x}, {'targets': test_y}),
          show_metric=True, run_id=MODEL_NAME)
model.save(MODEL_NAME)

import matplotlib.pyplot as plt

test_data = np.load('test_data.npy', allow_pickle=True)
fig = plt.figure()

for num, data in enumerate(test_data[:20]):
    img_num = data[1]
    img_data = data[0]

    y = fig.add_subplot(4, 5, num + 1)
    orig = img_data
    data = img_data.reshape(IMG_X, IMG_Y, 1)

    model_out = model.predict([data])[0]

    if np.argmax(model_out) == 1:
        str_label = 'targetHit'
    else:
        str_label = 'targetMiss'

    y.imshow(orig, cmap='gray')
    plt.title(str_label)
    y.axes.get_xaxis().set_visible(False)
    y.axes.get_yaxis().set_visible(False)

plt.show()
