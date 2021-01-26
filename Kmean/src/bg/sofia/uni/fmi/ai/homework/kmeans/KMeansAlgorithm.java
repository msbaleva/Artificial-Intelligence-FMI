package bg.sofia.uni.fmi.ai.homework.kmeans;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class KMeansAlgorithm extends ClusteringAlgorithm {
	

	public static final int NUMBER_ITERATIONS = 20;

	KMeansAlgorithm(String fileType, int cntClusters, int cntPoints, List<Point> points) {
		super(fileType, cntClusters, cntPoints, points);
		// TODO Auto-generated constructor stub
	}

	private void randomRestartClear() {
		clusters = new ArrayList<>();
		foundClusters = false;
	}

	@Override
	public void findClusters() {
		double minCost = Double.MAX_VALUE;
		List<Cluster> firstIterationResult = new ArrayList<>();
		for (int i = 0; i < NUMBER_ITERATIONS; i++) {
			initCentroids();
			initClusters();
			while (!foundClusters) {
				updateCentroids();
				updateClusters();
			}
			
			double currentCost = computeWCSS();
			if (currentCost < minCost) {				
				minCost = currentCost;
				generateScatterPlot(ClusteringAlgorithmType.K_MEANS);
			}
			
			if (i == 0) {
				for (Cluster cluster : clusters) {
					firstIterationResult.add(cluster);				
				}
				
				//showImage(FILE_DIRECTORY + ClusteringAlgorithmType.K_MEANS.toString() + fileType + FILE_EXTENSTION);
				System.out.println(ClusteringAlgorithmType.K_MEANS.toString() + COST + minCost);
			}
			
			randomRestartClear();
		}
		
		showImage(FILE_DIRECTORY + ClusteringAlgorithmType.K_MEANS.toString() + fileType + FILE_EXTENSTION);
		System.out.println(ClusteringAlgorithmType.K_MEANS.toString() + " with random restart" + COST + minCost);
	}

	@Override
	protected void initCentroids() {
		Random randomGenerator = new Random();
		Set<Integer> takenIndices = new HashSet<>();
		for (int i = 0; i < cntClusters; i++) {
			int index = -1;
			do  {
				index = randomGenerator.nextInt(cntPoints);
			} while (takenIndices.contains(index));
			takenIndices.add(index);
			Point currentCentroid = points.get(index);
			Cluster currentCluster = new Cluster(currentCentroid);
			clusters.add(currentCluster);
			currentCluster.addPoint(currentCentroid);
			currentCluster.setCentroid(currentCentroid);
		}
		
	}
	
}
