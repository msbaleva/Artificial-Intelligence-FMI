package bg.sofia.uni.fmi.ai.homework.kmeans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class KMeansPlusPlusAlgorithm extends ClusteringAlgorithm {
	
	KMeansPlusPlusAlgorithm(String fileType, int cntClusters, int cntPoints, List<Point> points) {
		super(fileType, cntClusters, cntPoints, points);
	}
	

	private double  computeDistanceFromNearestCentroid(int pointIndex, Set<Point> centroidPoints) {
		double minDistance = Double.MAX_VALUE;
		Point point = points.get(pointIndex);
		for (Point centroid : centroidPoints) {
			double distance = computeDistance(point, centroid);
			if (minDistance > distance) {
				minDistance = distance;
			}
		}
		
		return minDistance;

//		
//		double sum = 0.0;
//		for (int i = 0; i < cntPoints; i++) {
//			Point currentPoint = points.get(i);
//			double minSquareDistance = Double.MAX_VALUE;
//			for (Point centroid : centroidPoints) {
//				double distance = computeSquareDistance(currentPoint, centroid);
//				if (minSquareDistance > distance) {
//					minSquareDistance = distance;
//				}
//			}
//			
//			sum += minSquareDistance;
//		}
//		
//		return minDistance / sum;
			
	}
	
	@Override
	protected void initCentroids() {
		Random randomGenerator = new Random();
		Set<Point> centroidPoints = new HashSet<>();
		int index = randomGenerator.nextInt(cntPoints);
		Point firstCentroid = points.get(index);
		centroidPoints.add(firstCentroid);
		Cluster firstCluster = new Cluster(firstCentroid);
		clusters.add(firstCluster);
		firstCluster.addPoint(firstCentroid);
		firstCluster.setCentroid(firstCentroid);
		for (int i = 0; i < cntClusters; i++) {
			double maxDistance = 0.0;
			Point candidateCentroid = null;
			for (int j = 0; j < cntPoints; j++) {
				Point point = points.get(j);
				double distance = computeDistanceFromNearestCentroid(j, centroidPoints);
				if (maxDistance < distance) {
					maxDistance = distance;
					candidateCentroid = point;
				}
			}

			centroidPoints.add(candidateCentroid);
			Cluster cluster = new Cluster(candidateCentroid);
			clusters.add(cluster);
			firstCluster.addPoint(candidateCentroid);
			firstCluster.setCentroid(candidateCentroid);
		}
	}
	
	@Override
	public void findClusters() {
		
		initCentroids();
		initClusters();
		while (!foundClusters) {
			updateCentroids();
			updateClusters();
		}
		
		generateScatterPlot(ClusteringAlgorithmType.K_MEANS_PLUS_PLUS);
		
		showImage(FILE_DIRECTORY + ClusteringAlgorithmType.K_MEANS_PLUS_PLUS.toString() + fileType + FILE_EXTENSTION);
		double cost = computeWCSS();
		System.out.println(ClusteringAlgorithmType.K_MEANS_PLUS_PLUS.toString() + COST + cost);
	}

}
