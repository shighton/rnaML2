# rnaML2
Research Project at UNG under Dr. Mohammad Mohebbi

This version creates images from nucleotide strand pairing information out of Java.

The images are split into two categories which are either "hit" or "miss".

These images will be passed into a Convolutional Neural Network (along with their labels) using Python to determine patterns and predict whether validation/test microRNA strands will bind to larger mRNA strands.

Goal for the future is to be set up a working system where a user may type or scan in the microRNA and mRNA nucleotide sequences and run the model on a browser that will then give a confidence interval for the binding probabilty.
