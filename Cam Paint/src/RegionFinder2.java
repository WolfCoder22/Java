import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Sample solution for PS-1, Dartmouth CS 10, Spring 2015
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
public class RegionFinder2 {
	private static final int maxColorDiff = 20;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions;				// a region is a list of points
	// so the identified regions are in a list of lists of points

	public void RegionFinder() {
		this.image = null;
	}

	public void RegionFinder(BufferedImage image) {
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
	public void findRegions(Color targetColor) {
		// TODO: YOUR CODE HERE

		// Initialize the regions
		regions = new ArrayList<ArrayList<Point>>();

		// Keep track of which pixels have already been visited
		BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// Look at all the pixels.
		for (int y=0; y<image.getHeight(); y++) {
			for (int x=0; x<image.getWidth(); x++) {
				// Start a new region if it's an unvisited pixel of the correct color
				if (visited.getRGB(x, y)>0 || !colorMatch(targetColor, new Color(image.getRGB(x, y)))) 
					continue;
				ArrayList<Point> region = new ArrayList<Point>();

				// Start visiting from the point
				ArrayList<Point> toVisit = new ArrayList<Point>(); // using it like a Stack
				toVisit.add(new Point(x, y));				
				while (!toVisit.isEmpty()) {
					// Add unvisited points to the region, and visit their correctly-colored neighbors
					Point p = toVisit.remove(toVisit.size()-1);
					if (visited.getRGB(p.x, p.y)==0) {
						// Indicate that the point has been visited
						visited.setRGB(p.x, p.y, 1);

						// Add point to the current region
						region.add(p);

						// Find other points to visit of the current point's correctly-colored neighbors
						for (int ny = Math.max(0, p.y-1); ny <= Math.min(image.getHeight()-1, p.y+1); ny++) {
							for (int nx = Math.max(0, p.x-1); nx <= Math.min(image.getWidth()-1, p.x+1); nx++) {
								if (visited.getRGB(nx, ny)==0 && (ny!=p.y || nx!=p.x) && colorMatch(targetColor, new Color(image.getRGB(nx,ny)))) {
									toVisit.add(new Point(nx, ny));
								}	
							}	
						}		
					}	
				}	

				// Big enough to be worth keeping?
				if (region.size() >= minRegion) {
					regions.add(region);
				}
			}
		}
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the static threshold).
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		// TODO: YOUR CODE HERE
		return (Math.abs(c1.getRed() - c2.getRed()) <= maxColorDiff &&
				Math.abs(c1.getGreen() - c2.getGreen()) <= maxColorDiff &&
				Math.abs(c1.getBlue() - c2.getBlue()) <= maxColorDiff);
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> findLargestRegion() {
		// TODO: YOUR CODE HERE
		int sz = 0; 
		ArrayList<Point> largest = null;
		for (ArrayList<Point> region : regions) {
			if (region.size() > sz) {
				sz = region.size();
				largest = region;
			}
		}
		return largest;
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a random uniform color, 
	 * so we can see where they are
	 */
	public void recolorRegions() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		// Now recolor the regions
		// TODO: YOUR CODE HERE
		for (ArrayList<Point> region : regions) {
			Color c = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
			for (Point p : region) {
				recoloredImage.setRGB(p.x, p.y, c.getRGB());
			}
		}
	}
}
