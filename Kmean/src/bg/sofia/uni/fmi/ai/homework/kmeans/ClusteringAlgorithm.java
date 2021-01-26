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

public abstract class ClusteringAlgorithm {

	protected String fileType;
	protected int cntClusters;
	protected int cntPoints;
	protected List<Cluster> clusters;
	protected List<Point> points;
	protected boolean foundClusters;
	public static final double DISPLAY_FACTOR_UPPER = 1.05;
	public static final double DISPLAY_FACTOR_LOWER = 0.7;
	public static final int PLOT_WIDTH = 800;
	public static final int PLOT_HEIGHT = 600;
	public static final String CLUSTER_NUMBER = "Cluster #";
	public static final String PLOT_NAME = " Clustering: ";
	public static final String DOMAIN_AXIS_NAME = "X";
	public static final String RANGE_AXIS_NAME = "Y";
	public static final String CENTROIDS = "Centroids";
	public static final String FILE_DIRECTORY = "D:\\Docs\\ai-files\\ScatterPlots\\";
	public static final String FILE_EXTENSTION = ".jpeg";
	public static final String COST = " WCSS:";
	
	protected ClusteringAlgorithm(String fileType, int cntClusters, int cntPoints, List<Point> points) {
		this.fileType = fileType;
		this.cntClusters = cntClusters;
		this.cntPoints = cntPoints;
		this.clusters = new ArrayList<>(); 
		this.points = points;
	}
	
	
	
	protected double computeWCSS() {
		double sumOfSquares = 0.0;
		for (Cluster currentCluster : clusters) {
			if (currentCluster.isEmpty()) {
				continue;
			}
			
			Set<Point> clusterPoints = currentCluster.getPoints();
			Point currentCentroid = currentCluster.getCentroid();
			double sumOfDistances = 0.0;
			for (Point currentPoint : clusterPoints) {
				sumOfDistances += computeSquareDistance(currentPoint, currentCentroid);
			}
			
			sumOfSquares += sumOfDistances / currentCluster.getPointsCnt();
		}
		
		return sumOfSquares;
	}
	
		
	public abstract void findClusters();

	private void printClusters() {
		for (Cluster cluster : clusters) {
			cluster.print();
		}
	}
	
	private double[] getPointRange() {
		double[] range = new double[4];
		double minX = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
		for (Point currentPoint : points) {
			double x = currentPoint.getX();
			double y = currentPoint.getY();
			maxX = (x > maxX) ? x : maxX;
			minX = (x < minX) ? x : minX;
			minY = (y < minY) ? y : minY;
			maxY = (y > maxY) ? y : maxY;
		}
		
		for (Cluster cluster : clusters) {
			Point centroid = cluster.getCentroid();
			double x = centroid.getX();
			double y = centroid.getY();
			maxX = (x > maxX) ? x : maxX;
			minX = (x < minX) ? x : minX;
			minY = (y < minY) ? y : minY;
			maxY = (y > maxY) ? y : maxY;
		}
		
		range[0] = minX;
		range[1] = maxX;
		range[2] = minY;
		range[3] = maxY;
		return range;
	}
	
	protected void generateScatterPlot(ClusteringAlgorithmType type) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries centroidSeries = new XYSeries(CENTROIDS); 
		for (int i = 0; i < cntClusters; i++) {
			Cluster currentCluster = clusters.get(i);
			Point currentCentroid = currentCluster.getCentroid();
			XYSeries clusterSeries = new XYSeries(CLUSTER_NUMBER + (i + 1)); 
			
			
			Set<Point> clusterPoints = currentCluster.getPoints();
			for (Point currentPoint : clusterPoints) {
				clusterSeries.add(currentPoint.getX(), currentPoint.getY());
			}
			
			centroidSeries.add(currentCentroid.getX(), currentCentroid.getY());
			dataset.addSeries(clusterSeries);
		}
		
		dataset.addSeries(centroidSeries);
		
		JFreeChart scatterPlot = ChartFactory.createScatterPlot(type.toString() + PLOT_NAME + fileType, DOMAIN_AXIS_NAME, RANGE_AXIS_NAME, dataset);
		double[] range = getPointRange();		
        scatterPlot.getXYPlot().getDomainAxis().setRange(range[0] * DISPLAY_FACTOR_LOWER, range[1] * DISPLAY_FACTOR_UPPER);
        scatterPlot.getXYPlot().getRangeAxis().setRange(range[2] * DISPLAY_FACTOR_LOWER, range[3] * DISPLAY_FACTOR_UPPER);
        String fileName = FILE_DIRECTORY + type.toString() + fileType + FILE_EXTENSTION;
        try {
			ChartUtilities.saveChartAsPNG(new File(fileName), scatterPlot, PLOT_WIDTH, PLOT_HEIGHT);
			//showImage(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void initCentroids();
	
	protected double computeSquareDistance(Point point, Point centroid) {
		double xDifference = point.getX() - centroid.getX();
		double yDifference = point.getY() - centroid.getY();
		return (xDifference * xDifference) + (yDifference * yDifference);
	}
	
	protected double computeDistance(Point point, Point centroid) {
		return Math.sqrt(computeSquareDistance(point, centroid));
	}
	
	protected void updateCentroids() {
		for (Cluster currentCluster : clusters) {
			currentCluster.updateCentroid();
		}
	}
	
	protected void initClusters() {
		for (int i = 0; i < cntPoints; i++) {
			double minDistance = Double.MAX_VALUE;
			Point currentPoint = points.get(i);
			int clusterIndex = -1;
			for (int j = 0; j < cntClusters; j++) {
				Cluster currentCluster = clusters.get(j);
				double currentDistance = computeDistance(currentPoint, currentCluster.getCentroid());
				if (minDistance > currentDistance) {
					minDistance = currentDistance;
					clusterIndex = j;										
				}				
			}		
			
			clusters.get(clusterIndex).addPoint(currentPoint);			
		}
	}
	
	protected void updateClusters() {
		boolean hasChange = false;
		List<Cluster> newClusters = new ArrayList<>();
		for (Cluster cluster : clusters) {
			newClusters.add(new Cluster(cluster.getCentroid()));
		}
		
		for (int i = 0; i < cntPoints; i++) {
			double minDistance = Double.MAX_VALUE;
			Point currentPoint = points.get(i);
			int clusterIndex = -1;
			boolean pointChangesCluster = false;
			for (int j = 0; j < cntClusters; j++) {
				Cluster currentCluster = clusters.get(j);
				double currentDistance = computeDistance(currentPoint, currentCluster.getCentroid());
				if (minDistance > currentDistance) {
					minDistance = currentDistance;
					clusterIndex = j;
					pointChangesCluster = !currentCluster.hasPoint(currentPoint);
				}				
			}
			
			if (pointChangesCluster) {
				hasChange = true;
			} 
			
			newClusters.get(clusterIndex).addPoint(currentPoint);
		}
		
		clusters = newClusters;
		foundClusters = !hasChange;
		
	}
	
	protected static void showImage(final String fileName)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                JFrame f = new JFrame();
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.getContentPane().setLayout(new GridLayout(1,1));
                f.getContentPane().add(new JLabel(new ImageIcon(fileName)));
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }
	
}
