import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class rnaPicture {
	public static void main(String[] args){
		BufferedReader br;
		String everythingMiss = "";
		try {
			br = new BufferedReader(new FileReader("/Users/shighton/IdeaProjects/rnaML2/src/lib/dataForMissPic.txt"));
			StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        line = br.readLine();
		    }
		    everythingMiss = sb.toString();
		    br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String everythingHit = "";
		try {
			br = new BufferedReader(new FileReader("/Users/shighton/IdeaProjects/rnaML2/src/lib/dataForHitPic.txt"));
			StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        line = br.readLine();
		    }
		    everythingHit = sb.toString();
		    br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < everythingMiss.length(); i++) {
			if (!String.valueOf(everythingMiss.charAt(i)).equals("&") && !String.valueOf(everythingMiss.charAt(i)).equals("0")&& !String.valueOf(everythingMiss.charAt(i)).equalsIgnoreCase("A") && !String.valueOf(everythingMiss.charAt(i)).equalsIgnoreCase("U") && !String.valueOf(everythingMiss.charAt(i)).equalsIgnoreCase("C") && !String.valueOf(everythingMiss.charAt(i)).equalsIgnoreCase("G") && !String.valueOf(everythingMiss.charAt(i)).equalsIgnoreCase("Z") ){
				System.out.println("Unknown character at index " + i + ": " + String.valueOf(everythingMiss.charAt(i)));
			}
		}
		for(int i = 0; i < everythingHit.length(); i++) {
			if (!String.valueOf(everythingHit.charAt(i)).equals("&") && !String.valueOf(everythingHit.charAt(i)).equals("1")&& !String.valueOf(everythingHit.charAt(i)).equalsIgnoreCase("A") && !String.valueOf(everythingHit.charAt(i)).equalsIgnoreCase("U") && !String.valueOf(everythingHit.charAt(i)).equalsIgnoreCase("C") && !String.valueOf(everythingHit.charAt(i)).equalsIgnoreCase("G") && !String.valueOf(everythingHit.charAt(i)).equalsIgnoreCase("Z") ){
				System.out.println("Unknown character at index " + i + ": " + String.valueOf(everythingHit.charAt(i)));
			}
		}
		String[] seqMiss = everythingMiss.split("&");
		String[] seqHit = everythingHit.split("&");
		String seqEvenMiss = "";
        String seqOddMiss = "";
		for(int i = 0; i < seqMiss.length; i++) {
			if(i % 2 == 0) {
				seqEvenMiss += seqMiss[i] + "&";
			}
			else {
				seqOddMiss += seqMiss[i] + "&";
			}
		}
		String seqEvenHit = "";
        String seqOddHit = "";
		for(int i = 0; i < seqHit.length; i++) {
			if(i % 2 == 0) {
				seqEvenHit += seqHit[i] + "&";
			}
			else {
				seqOddHit += seqHit[i] + "&";
			}
		}
		String[] evensMiss = seqEvenMiss.split("&");
		String[] oddsMiss = seqOddMiss.split("&");
		String[] evensHit = seqEvenHit.split("&");
		String[] oddsHit = seqOddHit.split("&");
		int totalMiss = evensMiss.length;
		int totalHit = evensHit.length;
		try {
			for(int yCounter = 0; yCounter < oddsMiss.length; yCounter+=totalMiss) {
				for(int xCounter = 0; xCounter < evensMiss.length; xCounter++) {
					//System.out.println(xCounter + " " + yCounter);
					createMissPic(everythingMiss, xCounter, yCounter);
					//createHitPic(everything, xCounter, yCounter);
				}
			}
			System.out.println("Complete.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not create the image.");
		}
		try {
			for(int yCounter = 0; yCounter < oddsHit.length; yCounter+=totalHit) {
				for(int xCounter = 0; xCounter < evensHit.length; xCounter++) {
					//System.out.println(xCounter + " " + yCounter);
					createHitPic(everythingHit, xCounter, yCounter);
					//createHitPic(everything, xCounter, yCounter);
				}
			}
			System.out.println("Complete.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not create the image.");
		}
	}
	public static void createMissPic(String aSeq, int anXCounter, int aYCounter) throws IOException {
		int width = 22;
        int height = 25;
        int xCount = anXCounter;
        int yCount = aYCounter;
        String seqEven = "";
        String seqOdd = "";
        String[] seq = aSeq.split("&");
        
		for(int i = 0; i < seq.length; i++) {
			if(i % 2 == 0) {
				seqEven += seq[i];
			}
			else {
				seqOdd += seq[i];
			}
		}
 
        // Constructs a BufferedImage of one of the predefined image types.
        BufferedImage bufferedImage = new BufferedImage(width*(yCount+1), height*(xCount+1), BufferedImage.TYPE_INT_RGB);
 
        // Create a graphics which can be used to draw into the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();
 
        // fill all the image with white
        //g2d.setColor(Color.white);
        //g2d.fillRect(xCount*width, yCount*height, width, height);
        for(int i = 0; i < seqOdd.length(); i++) {
        		for(int j = 0; j < seqEven.length(); j++) {
        			if(seqOdd.charAt(i) == 'A' && seqEven.charAt(j) == 'U' || seqOdd.charAt(i) == 'U' && seqEven.charAt(j) == 'A' || seqOdd.charAt(i) == 'Z' && seqEven.charAt(j) == 'U' || seqOdd.charAt(i) == 'U' && seqEven.charAt(j) == 'Z') {
        				g2d.setColor(Color.red);
        		        //g2d.fillRect((xCount*width)+j, (yCount*height)+i, 1, 1);
        				g2d.fillRect(j, i, 1, 1);
        			}
        			else if(seqOdd.charAt(i) == 'C' && seqEven.charAt(j) == 'G' || seqOdd.charAt(i) == 'G' && seqEven.charAt(j) == 'C') {
        				g2d.setColor(Color.blue);
        		        g2d.fillRect(j, i, 1, 1);
        			}
        			else if(seqOdd.charAt(i) == 'G' && seqEven.charAt(j) == 'U' || seqOdd.charAt(i) == 'U' && seqEven.charAt(j) == 'G') {
        				g2d.setColor(Color.gray);
        		        g2d.fillRect(j, i, 1, 1);
        			}
        			else {
        				g2d.setColor(Color.black);
        				g2d.fillRect(j, i, 1, 1);
        			}
        		}
        }
 
        // Disposes of this graphics context and releases any system resources that it is using. 
        g2d.dispose();
 
        // Save as PNG
        File file = new File("/Users/shighton/IdeaProjects/rnaML2/src/lib/targetMiss.png");
        ImageIO.write(bufferedImage, "png", file);
	}
	public static void createHitPic(String aSeq, int anXCounter, int aYCounter) throws IOException {
		int width = 22;
        int height = 25;
        int xCount = anXCounter;
        int yCount = aYCounter;
        String seqEven = "";
        String seqOdd = "";
        String[] seq = aSeq.split("&");
        
		for(int i = 0; i < seq.length; i++) {
			if(i % 2 == 0) {
				seqEven += seq[i];
			}
			else {
				seqOdd += seq[i];
			}
		}
 
        // Constructs a BufferedImage of one of the predefined image types.
        BufferedImage bufferedImage = new BufferedImage(width*(yCount+1), height*(xCount+1), BufferedImage.TYPE_INT_RGB);
 
        // Create a graphics which can be used to draw into the buffered image
        Graphics2D g2d = bufferedImage.createGraphics();
 
        // fill all the image with white
        //g2d.setColor(Color.white);
        //g2d.fillRect(xCount*width, yCount*height, width, height);
        for(int i = 0; i < seqOdd.length(); i++) {
        		for(int j = 0; j < seqEven.length(); j++) {
        			if(seqOdd.charAt(i) == 'A' && seqEven.charAt(j) == 'U' || seqOdd.charAt(i) == 'U' && seqEven.charAt(j) == 'A' || seqOdd.charAt(i) == 'Z' && seqEven.charAt(j) == 'U' || seqOdd.charAt(i) == 'U' && seqEven.charAt(j) == 'Z') {
        				g2d.setColor(Color.red);
        		        //g2d.fillRect((xCount*width)+j, (yCount*height)+i, 1, 1);
        				g2d.fillRect(j, i, 1, 1);
        			}
        			else if(seqOdd.charAt(i) == 'C' && seqEven.charAt(j) == 'G' || seqOdd.charAt(i) == 'G' && seqEven.charAt(j) == 'C') {
        				g2d.setColor(Color.blue);
        		        g2d.fillRect(j, i, 1, 1);
        			}
        			else if(seqOdd.charAt(i) == 'G' && seqEven.charAt(j) == 'U' || seqOdd.charAt(i) == 'U' && seqEven.charAt(j) == 'G') {
        				g2d.setColor(Color.gray);
        		        g2d.fillRect(j, i, 1, 1);
        			}
        			else {
        				g2d.setColor(Color.black);
        				g2d.fillRect(j, i, 1, 1);
        			}
        		}
        }
 
        // Disposes of this graphics context and releases any system resources that it is using. 
        g2d.dispose();
 
        // Save as PNG
        File file = new File("/Users/shighton/IdeaProjects/rnaML2/src/lib/targetFind.png");
        ImageIO.write(bufferedImage, "png", file);
	}
}
