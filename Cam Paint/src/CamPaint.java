import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Webcam-based drawing 
 * Sample solution to PS-1, Dartmouth CS 10, Spring 2015
 * 
 * @author Chris Bailey-Kellogg, Spring 2015 (based on a different webcam app from previous terms)
 */
public class CamPaint extends Webcam {
	private char display = 'w';				// what to display: 'p': painting, 'r': regions, 'w': webcam
	private RegionFinder finder;				// handles the finding
	private Color targetColor;          		// color of regions of interest (set by mouse press)
	private Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
	private BufferedImage painting;			// the resulting masterpiece

	/**
	 * Initializes the region finder and the drawing
	 */
	public CamPaint() {
		targetColor=null;
		finder = new RegionFinder();
		clearPainting();
	}

	/**
	 * Resets the painting to a blank image
	 */
	protected void clearPainting() {
		painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * Overrides the DrawingGUI method to draw one of webcam, recolored image, or painting, 
	 * depending on display variable ('w', 'r', or 'p')
	 */
	public void draw(Graphics g) {
		// TODO: YOUR CODE HERE
		//Code by Alex Wolf
		
		//if webcam selected sow webcam
		if (display=='w'){
			g.drawImage(image, 0, 0, null);
		}
		
		//if r selcted show regions
		else if (display=='r'&& targetColor!=null) {
			g.drawImage(finder.getRecoloredImage(), 0, 0, null);       	
		}
		
		//if p selected show painting
		else if (display=='p'){
			g.drawImage(painting, 0, 0, null);
		}
	}

	/**
	 * Overrides Webcam method to do the region finding and update the painting.
	 */
	public void processImage() {
		// TODO: YOUR CODE HERE
		//Code by Alex Wolf
		
		//if ther is a target color
		if (targetColor != null) {
			
			//use regionFinder mthods
			finder.setImage(image);
			finder.findRegions(targetColor);
			finder.recolorRegions();
			
			//find largest region
			ArrayList<Point> largest = finder.findLargestRegion();
			
			//if a largest region was found
			if (largest != null) {
				
				//recolor the region
				for (Point p : largest) {
					painting.setRGB(p.x, p.y, paintColor.getRGB());
				}
			}            
		}
	}

	/**
	 * Overrides the DrawingGUI method to set the track color.
	 */
	public void handleMousePress(int x, int y) {
		// TODO: YOUR CODE HERE
		//code by Alex Wolf
		
		//if the webcam is initialized
		if (image != null) {
			
			//reset traget color with pouse press
			targetColor = new Color(image.getRGB(x, y));		
		}
	}

	/**
	 * DrawingGUI method, here doing various drawing commands
	 */
	public void handleKeyPress(char k) {
		if (k == 'p' || (k == 'r'&& targetColor!=null) || k == 'w') { // display: painting, regions, or webcam
			display = k;
		}
		else if (k == 'c') { // clear
			clearPainting();
		}
		else if (k == 'o') { // save the regions
			saveImage(finder.getRecoloredImage(), "pictures/regions.png", "png");
		}
		else if (k == 's') { // save the drawing
			saveImage(painting, "pictures/webdraw.png", "png");
		}
		else System.out.println("unhandled key "+k);
	}

	/**
	 * Main method for the application
	 * @param args      command-line arguments (ignored)
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CamPaint();
			}
		});
	}
}
