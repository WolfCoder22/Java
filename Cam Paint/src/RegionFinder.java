import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Template for PS-1, Dartmouth CS 10, Spring 2015
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
public class RegionFinder {
	private static final int maxColorDiff = 20;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored
	

	private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
	// so the identified regions are in a list of lists of points

	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;		
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public ArrayList<ArrayList<Point>> getRegions() {
		return regions;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	//Code by Alex Wolf
	public void findRegions(Color targetColor) {
		
		regions= new ArrayList<ArrayList<Point>>();	
		
		 //create copy of image to keep track of pixel visit
		BufferedImage blackImage= new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		ArrayList<Point> toVisit= new ArrayList<Point>();//list that keeps track of unvisited points
		
		//makes copy image all black
		for (int y = 0; y < blackImage.getHeight(); y++) {
			for(int x=0; x<blackImage.getWidth(); x++){
				blackImage.setRGB(x, y, 0);
			}
		}
			
		//loop over all pixels
		for (int y = 0; y < image.getHeight(); y++) {
			for(int x=0; x<image.getWidth(); x++){
				Color pointColor= new Color(image.getRGB(x, y));//returns color at specific point
				
				//if a pixel is within color range and not visited
				if(blackImage.getRGB(x, y) == 0 && colorMatch(targetColor, pointColor)) {
					
			
					//create a new region
					ArrayList<Point> region= new ArrayList<Point>();//create list of points in a region
				
					//add initial point to pixels which need to be visited
					Point e= new Point (x,y);
					toVisit.add(e); 
				
				//while toVisit list is not empty
				while (toVisit.size() > 0) {
					
					
					//gets last index of toVisit List
					int lastIndex= toVisit.size()-1;
					
					//return last point of toVisit List 
					Point pixel=toVisit.get(lastIndex);
					
					//remove last point of toVisit list
					toVisit.remove(lastIndex);
					
					
					//adds pixel to region
					region.add(pixel);
					
					//Marks pixel as visited by changing color 
					blackImage.setRGB(pixel.x, pixel.y, 255);
					
					
					
					//loop over all of the pixel's neighbors and checks if they're inbounds
					for(int y3=Math.max(0, pixel.y-1); y3<Math.min(image.getHeight(), pixel.y+2); y3++){
						for(int x2=Math.max(0, pixel.x-1); x2<Math.min(image.getWidth(), pixel.x+2); x2++){
								
								//defines neighbor and neighbor color
								Point neighbor= new Point(x2, y3);
								Color neighborColor= new Color(image.getRGB(x2, y3));
								
								
								//if neighbor is within range of target color and unvisited, add it to region
								if (colorMatch(neighborColor, targetColor) && blackImage.getRGB(x2, y3)==0){
						
									//add neighbor to toVisited list
									toVisit.add(neighbor);
							}
						}
					}
					
				}
				//add region to regions list if it is big enough
				if(region.size()>=minRegion){
					regions.add(region);
				}
			}
		}
		}

	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the static threshold).
	 */
	//Code by Alex Wolf
	private static boolean colorMatch(Color c1, Color c2) {
		
		//get RGB values of color 1
		int c1Red= c1.getRed();
		int c1Blue= c1.getBlue();
		int c1Green= c1.getGreen();
		
		//get RGB values of Color 2
		int c2Red= c2.getRed();
		int c2Blue= c2.getBlue();
		int c2Green= c2.getGreen();
		
		return Math.abs((c1Red-c2Red))<maxColorDiff && Math.abs((c1Blue-c2Blue))<maxColorDiff && Math.abs((c1Green-c2Green))<maxColorDiff; 
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	
	//Code by Alex Wolf
	public ArrayList<Point> findLargestRegion() {
		
		//sets biggest Region Variable
		ArrayList<Point> biggestRegion= regions.get(0);
		
		//gets integer of the size of number of regions
		int numberOfRegions=regions.size();
		
		//Finds largest region 
		for (int i=0; i<numberOfRegions; i++){
			ArrayList<Point> currentRegion= regions.get(i);
			if(currentRegion.size()>biggestRegion.size()){
				biggestRegion= currentRegion;
			}
		}
		return biggestRegion;
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a random uniform color, 
	 * so we can see where they are
	 */
	
	//code by Alex WOlf
	public void recolorRegions() {
		
		; 
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		
		
		// Now recolor the regions
		// TODO: YOUR CODE HERE
		for (ArrayList<Point> region : regions) {
			
			//get new random color
			Color c = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
			
			//loop through points in region
			for (Point p : region) {
				
				//recolor image
				recoloredImage.setRGB(p.x, p.y, c.getRGB());
			}
		}

}
}