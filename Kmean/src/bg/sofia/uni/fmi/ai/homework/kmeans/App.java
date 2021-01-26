package bg.sofia.uni.fmi.ai.homework.kmeans;

import java.util.Scanner;

public class App {
	
	private static final String PATH = "D:\\Docs\\ai-files\\Datasets\\";
	private static final String EXTENSION = ".txt";
	static String[] getInput() {
		Scanner scanner = new Scanner(System.in);
		String[] input = new String[2];
		System.out.print("Filename:");
		input[0] = scanner.next();
		input[0] = PATH + input[0] + "\\" + input[0] + EXTENSION;
		System.out.print("Clusters:");
		input[1] = scanner.next();
		scanner.close();
		return input;		
	}
	
	public static void main(String[] args) {
		String[] input = getInput();
		ClusteringAlgorithmFactory.getInstance(ClusteringAlgorithmType.K_MEANS, input).findClusters();
		ClusteringAlgorithmFactory.getInstance(ClusteringAlgorithmType.K_MEANS_PLUS_PLUS, input).findClusters();
	}

}
