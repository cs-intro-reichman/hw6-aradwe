import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		System.out.println("=== Runigram Testing ===");

		// Tests the reading and printing of an image:	
		System.out.println("Tests the reading and printing of an image:");
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		System.out.println("------------------------\n");

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] image;

		// Tests the horizontal flipping of an image:
		System.out.println("Tests the horizontal flipping of an image:");
		image = flippedHorizontally(tinypic);
		System.out.println();
		print(image);
		
		System.out.println("------------------------\n");
		
		
		//// Write here whatever code you need in order to test your work.
		//// You can continue using the image array.
		 
		 // Tests the vertical flipping of an image:
		 System.out.println("Tests the vertical flipping of an image:");
		 image = flippedVertically(tinypic);
		 System.out.println();
		 print(image);
		 
		 System.out.println("------------------------\n");
		 

		  // Tests the grayscale conversion of an image:
		  System.out.println("Tests the grayscale conversion of an image:");
		  image = grayScaled(tinypic);
		  System.out.println();
		  print(image);
		  
		  System.out.println("------------------------\n");
		  
	  
		  // Tests the scaling of an image:
		  System.out.println("Tests the scaling of an image:");
		  int newWidth = 3;
		  int newHeight = 5;
		  image = scaled(tinypic, newWidth, newHeight);
		  System.out.println("Scaled Image (" + newWidth + "x" + newHeight + "):");
		  System.out.println();
		  print(image);
		  
		  System.out.println("------------------------\n");
		  

  		  // Tests the blending of two colors:
		  System.out.println("Tests the blending of two colors:");
		  Color c1 = new Color(100, 40, 100);
		  Color c2 = new Color(200, 20, 40);
		  System.out.print("color 1 + color 2: ");
		  print(c1);
		  System.out.print("+ ");
		  print(c2);
		  System.out.print("= ");
		  print(blend(c1, c2, 0.25)); 
		  System.out.println(); 
		  
		  System.out.println("------------------------\n");

		  // Tests Morphing
		  System.out.println("Tests Morphing:");
		  Color[][] dummyTarget = grayScaled(tinypic); // Example morph target
		  morph(tinypic, dummyTarget, 900000000);

		  System.out.println("=== Testing Complete ==="); 
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		// Reads the RGB values from the file into the image array. 
		// For each pixel (i,j), reads 3 values from the file,
		// creates from the 3 colors a new Color object, and 
		// makes pixel (i,j) refer to that object.
		for(int i = 0; i < numRows; i++){
			for(int j = 0; j < numCols; j++){
				int red = in.readInt();
				int green = in.readInt();
				int blue = in.readInt();
				
				image[i][j] = new Color(red, green, blue);				
			}
		}
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		for(int i = 0; i < image.length; i++){
			for(int j = 0; j < image[i].length; j++){
				print(image[i][j]);
			}
			System.out.println(); // Print a new line after each row is finished iterating
		}
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		// Create a new 2D array with the same dimensions as the input image
		Color[][] newImage = new Color[image.length][image[0].length];
		// Loop through each row
		for(int i = 0; i < image.length; i++){
			// Loop through each column
			for(int j = 0; j < image[i].length; j++){
				// Put the pixel that is in the end of the row in the original image
				// in the beginning of the new one
				newImage[i][j] = image[i][image[i].length - 1 - j];
			}
		}
		return newImage;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		// Create a new 2D array with the same dimensions as the input image
		Color[][] newImage = new Color[image.length][image[0].length];
		// Loop through each row
		for(int i = 0; i < image.length; i++){
			// Loop through each column
			for(int j = 0; j < image[i].length; j++){
				// Put the pixel that is in the end of the column in the original image
				// in the beginning of the new one
				newImage[i][j] = image[image.length - 1 - i][j];
			}
		}
		return newImage;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	private static Color luminance(Color pixel) {
		 // Compute the luminance
		int lum = (int)(pixel.getRed() * 0.299 + pixel.getGreen() * 0.587 + pixel.getBlue() * 0.114);
		// Ensures that the luminance value, lum, remains within the valid range of RGB values: [0, 255]
		lum = Math.max(0, Math.min(255, lum));
		// Return a new grayscale Color object
		return new Color(lum, lum, lum);
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		// Create a new 2D array with the same dimensions as the input image
		Color[][] newImage = new Color[image.length][image[0].length];
		// Loop through each row
		for(int i = 0; i < image.length; i++){
			// Loop through each column
			for(int j = 0; j < image[i].length; j++){
				// Turn every pixel in it's luminance form
				newImage[i][j] = luminance(image[i][j]);
			}
		}
		return newImage;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) {
		// Get the original image dimensions
		int originalHeight = image.length;
		int originalWidth = image[0].length;
		
		// Create a new 2D array with the same dimensions as the input image
		Color[][] newImage = new Color[height][width];
		
		// Compute scale factors
		double scaleX = (double)originalWidth / width;
		double scaleY = (double)originalHeight / height;

		// Loop through each pixel in target image
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				// Map target pixel to source pixel
				int sourceI = (int)(i * scaleY);
				int sourceJ = (int)(j * scaleX);

				// Ensure the image scale is within bounds
				sourceI = Math.min(sourceI, originalHeight - 1);
				sourceJ = Math.min(sourceJ, originalWidth - 1);
				
				// Set the pixel in target image to the correct pixel from the original image
				newImage[i][j] = image[sourceI][sourceJ];
			}
		}		
		return newImage;
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		int blendedRed = (int)(alpha * c1.getRed() + (1 - alpha) * c2.getRed());
		int blendedGreen = (int)(alpha * c1.getGreen() + (1 - alpha) * c2.getGreen());
		int blendedBlue = (int)(alpha * c1.getBlue() + (1 - alpha) * c2.getBlue());

		return new Color(blendedRed, blendedGreen, blendedBlue);
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		// Create a new 2D array with the same dimensions as the input images
		Color[][] newImage = new Color[image1.length][image1[0].length];

		for(int i = 0; i < image1.length; i++){
			for(int j = 0; j < image1[0].length; j++)
			{
				newImage[i][j] = blend(image1[i][j], image2[i][j], alpha);
			}
		}
		
		return newImage;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		Color[][] scaledTargetImage = scaled(target, source[0].length, source.length);
		// Create a new 2D array with the same dimensions as the input images
		Color[][] newImage = new Color[source.length][source[0].length];

		for(int i = 0; i < n; i++){
			double newAlpha = (n - i) / n;
			newImage = blend(source, scaledTargetImage, newAlpha);
			display(newImage);
			StdDraw.pause(500);
		}
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(height, width);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

