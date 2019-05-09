package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

public class AI {
	
	private static final File DATA_SET_PATH = new File("data");
	private static final String BACKUP_FILE = "neural_network.nnet";
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#####");
	
	private NeuralNetwork<BackPropagation> network;
	private final DataSet dataSet;
	
	private static final int NN_IMAGE_SIZE = 25;
	private static final int INPUT_SIZE = NN_IMAGE_SIZE * NN_IMAGE_SIZE;
	private static final int OUTPUT_SIZE = 10;
	
	public AI() {
		network = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, INPUT_SIZE, NN_IMAGE_SIZE * 4, NN_IMAGE_SIZE, OUTPUT_SIZE);
		//network = NeuralNetwork.createFromFile("neural_network.nnet");
		
		//BackPropagation backPropagation = network.getLearningRule();
		this.dataSet = loadDataSet();
		
		DataSet d = new DataSet(INPUT_SIZE, OUTPUT_SIZE);
		d.addRow(new double[INPUT_SIZE], new double[OUTPUT_SIZE]);
		network.learn(d);
	}
	
	private DataSet loadDataSet() {
		File[] files = Tools.shuffleFileArray(DATA_SET_PATH.listFiles());

		int max = files.length;
		int current = 0;
		long startTime = System.currentTimeMillis();
		
		DataSet dataSet = new DataSet(INPUT_SIZE, OUTPUT_SIZE);
		
		for (File file : files) {
			current++;

			int percentage = Math.round(100f / ((float) max / current));
			String fileName = file.getName();
			int number = Integer.parseInt(fileName.split("-")[0]);
			long duration = Math.round(((100d / percentage) * (System.currentTimeMillis() - startTime)) / 1000d);

			System.out.println(current + "/" + max + " - " + percentage + "% - training " + number + " with file " + fileName + " - " + duration + "s remaining");

			try {
				BufferedImage image = Tools.scaleImage(Tools.cropImage(ImageIO.read(file), HandwritingRecognition.BACKGROUND_COLOR), NN_IMAGE_SIZE, NN_IMAGE_SIZE);

				double[] input = Tools.convertArray(Tools.convertImage(image));
				double[] target = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
				target[number] = 1;
				
				dataSet.addRow(new DataSetRow(input, target));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Loading Data Set finished");
		System.out.println();
		
		return dataSet;
	}
	
	public void train(int epochs) {
		long startTime = System.currentTimeMillis();
		
		for (int i = 0; i < epochs; i++) {
			if(i % 100 == 0) {
				System.out.println("Epoch: " + i);
			}
			//network.getLearningRule().doLearningEpoch(network.getLearningRule().getTrainingSet());
			//network.getLearningRule().doOneLearningIteration(network.getLearningRule().getTrainingSet());
			network.learn(dataSet);
		}
		
		long endTime = System.currentTimeMillis();
		double duration = (endTime - startTime) / 1000d;
		
		System.out.println("Training finished - it took " + duration + "s");
		System.out.println();
	}
	
	public void save() {
		network.save(BACKUP_FILE);
	}
	
	public int calculate(BufferedImage image) {
		BufferedImage scaledImage = Tools.scaleImage(Tools.cropImage(image, HandwritingRecognition.BACKGROUND_COLOR), NN_IMAGE_SIZE, NN_IMAGE_SIZE);
		try {
			ImageIO.write(scaledImage, "png", new File("recognized.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		network.setInput(Tools.convertArray(Tools.convertImage(scaledImage)));
        network.calculate();
        double[] output = network.getOutput();

		double maxOutput = 0;
		int number = -1;

		for (int i = 0; i < output.length; i++) {
			if (output[i] > maxOutput) {
				maxOutput = output[i];
				number = i;
			}
		}
		
		print(number, output);
		
		return number;
	}
	
	private void print(double number, double... output) {
		for(int i=0; i < output.length; i++) {
			System.out.print(" | ");
			System.out.print(i + ": " + DECIMAL_FORMAT.format(output[i]));
			System.out.print(" | ");
		}
		System.out.println();
		System.out.println("Recognized Number: " + number);
		System.out.println();
	}
	
}
