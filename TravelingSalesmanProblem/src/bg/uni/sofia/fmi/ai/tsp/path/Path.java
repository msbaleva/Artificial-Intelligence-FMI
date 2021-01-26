package bg.uni.sofia.fmi.ai.tsp.path;

import java.util.ArrayList;
import java.util.List;

import bg.uni.sofia.fmi.ai.tsp.city.City;

public class Path implements Comparable<Path> {
	
	List<City> citiesPath;
	double fitness;
	
	public Path(List<City> path) {
		this.citiesPath = path;
		this.fitness = calculateFitness(path);
	}
	
	public Path(Path other) {
		this.fitness = other.fitness;
		this.citiesPath = new ArrayList<City>();
		citiesPath.addAll(other.citiesPath);
	}
	
	public double getLengthBetweenCities(City firstCity, City secondCity) {
		return Math.sqrt(Math.pow(firstCity.getX() - secondCity.getX(), 2) + Math.pow(firstCity.getY() - secondCity.getY(), 2));
	}
	
	private double calculateFitness(List<City> path) {
		double sum = 0.0;
		int bound = path.size() - 1;
		for (int i = 0; i < bound; i++) {
			sum += getLengthBetweenCities(path.get(i), path.get(i + 1));
		}
		
		return sum;
	}
	
	public void print() {
		System.out.println("Path length: " + fitness);
//		for(City city : citiesPath) {
//			System.out.println("(" + city.getX() + ", " + city.getY() + ")");
//		}
	}

	public City getCityAt(int index) {
		return citiesPath.get(index);
	}
	
	@Override
	public int compareTo(Path otherPath) {
		return Double.compare(fitness, otherPath.fitness);
	}

}
