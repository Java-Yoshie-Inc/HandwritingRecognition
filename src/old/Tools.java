package old;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tools {
	
	public static BufferedImage scaleImage(BufferedImage image, int width, int height) {
		BufferedImage image2 = new BufferedImage(width, height, image.getType());
		image2.getGraphics().drawImage(image, 0, 0, image2.getWidth(), image2.getHeight(), null);
		return image2;
	}
	
	public static int[] convertImage(BufferedImage image) {
		int[][] colors = new int[image.getWidth()][image.getHeight()];
		for(int x=0; x<colors.length; x++) {
			for(int y=0; y<colors[x].length; y++) {
				Color color = new Color(image.getRGB(x, y));
				if(color.equals(Color.BLACK)) {
					colors[x][y] = 0;
				} else {
					colors[x][y] = 1;
				}
			}
		}
		return convertArrayDimension(colors);
	}

	public static int[] convertArrayDimension(int[][] array) {
		List<Integer> output = new ArrayList<>();
		for(int x=0; x<array.length; x++) {
			for(int y=0; y<array[x].length; y++) {
				output.add(array[x][y]);
			}
		}
		Integer[] output2 = output.toArray(new Integer[0]);
		int[] output3 = new int[output2.length];
		for(int i=0; i < output3.length; i++) {
			output3[i] = output2[i];
		}
		return output3;
	}

	public static double[] convertArray(int[] array) {
		double[] output = new double[array.length];
		for(int i=0; i<output.length; i++) {
			output[i] = array[i];
		}
		return output;
	}
	
	public static File[] shuffleFileArray(File[] array) {
		int index;
		File temp;
		File[] output = array.clone();
		
		Random random = new Random();
		for (int i = output.length - 1; i > 0; i--) {
			index = random.nextInt(i + 1);
			temp = output[index];
			output[index] = output[i];
			output[i] = temp;
		}
		return output;
	}
	
}
