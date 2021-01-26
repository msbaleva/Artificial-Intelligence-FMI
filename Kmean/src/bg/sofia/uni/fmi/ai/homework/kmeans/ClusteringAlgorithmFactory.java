package bg.sofia.uni.fmi.ai.homework.kmeans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ClusteringAlgorithmFactory {	

	public static final String COORDINATES_DELIMITER = "\\s+";
	public static final String EXTENSION_DELIMITER = "\\.";
	
	public static ClusteringAlgorithm getInstance(ClusteringAlgorithmType type, String[] input) {
		String filename = input[0];
		int cntClusters = Integer.parseInt(input[1]);
		String fileType = Paths.get(input[0]).getFileName().toString().split(EXTENSION_DELIMITER)[0];
		List<Point> points = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while((line = reader.readLine()) != null) {
				String[] coordinates = line.split(COORDINATES_DELIMITER);
				points.add(new Point(coordinates[0], coordinates[1]));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (type.equals(ClusteringAlgorithmType.K_MEANS)) {
			return new KMeansAlgorithm(fileType, cntClusters, points.size(), points);
		} else if (type.equals(ClusteringAlgorithmType.K_MEANS_PLUS_PLUS)) {
			return new KMeansPlusPlusAlgorithm(fileType, cntClusters, points.size(), points);
		} else {
			return new KMeansAlgorithm(fileType, cntClusters, points.size(), points);
		}
	}

}
