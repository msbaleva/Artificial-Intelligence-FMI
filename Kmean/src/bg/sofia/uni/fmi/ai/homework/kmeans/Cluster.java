package bg.sofia.uni.fmi.ai.homework.kmeans;

import java.util.HashSet;
import java.util.Set;

public class Cluster {

	private Set<Point> points;
	private Point centroid;
	
	Cluster(Point centroid) {
		this.centroid = centroid;
		this.points = new HashSet<>();
	}
	
	Cluster(Cluster other) {
		Set<Point> otherPoints = other.getPoints();
		for (Point point : otherPoints) {
			other.addPoint(point);
		}
	}
	
	public Point getCentroid() {
		return centroid;
	}
	
	public void setCentroid(Point point) {
		this.centroid = point;
	}
	
	public void addPoint(Point point) {
		points.add(point);
	}
	
	public boolean hasPoint(Point point) {
		return points.contains(point);
	}
	
	public boolean isEmpty() {
		return points.size() == 0;
	}
	
	public void updateCentroid() {
		double sumX = 0.0;
		double sumY = 0.0;
		double cntPoints = points.size();
		if (cntPoints == 0) {
			return;
		}
		
		for (Point currentPoint : points) {
			sumX += currentPoint.getX();
			sumY += currentPoint.getY();
		}
		
		setCentroid(new Point(sumX / cntPoints, sumY / cntPoints));
	}
	
	public Set<Point> getPoints() {
		return points;
	}
	
	public void print() {
		for (Point currentPoint : points) {
			System.out.println(currentPoint.getX() + " " + currentPoint.getY());
		}
	}
	
	public int getPointsCnt() {
		return points.size();
	}
}
