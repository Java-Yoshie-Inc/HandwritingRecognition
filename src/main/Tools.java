package main;

import java.awt.image.BufferedImage;

public class Tools {
	
	public static BufferedImage scaleImage(BufferedImage image, int width, int height) {
		BufferedImage image2 = new BufferedImage(width, height, image.getType());
		image2.getGraphics().drawImage(image, 0, 0, image2.getWidth(), image2.getHeight(), null);
		return image2;
	}
	
}
