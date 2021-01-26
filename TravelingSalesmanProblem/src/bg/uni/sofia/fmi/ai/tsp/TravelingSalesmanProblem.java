package bg.uni.sofia.fmi.ai.tsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import bg.uni.sofia.fmi.ai.tsp.algorithm.GeneticAlgorithm;
import bg.uni.sofia.fmi.ai.tsp.city.City;

public class TravelingSalesmanProblem {
		
	public static final int MAX_COORDINATES = 10000;
	
	private static int input() {
		Scanner scanner = new Scanner(System.in);
		int N = scanner.nextInt();
		scanner.close();
		return N;
	}
	
	private static List<City> generateCities(int N) {
		Random randomGenerator = new Random();
		List<City> cities = new ArrayList<>(N);
		for (int i = 0; i < N; i++) {
			City city = null;
			do {
				int x = randomGenerator.nextInt(MAX_COORDINATES);
				int y = randomGenerator.nextInt(MAX_COORDINATES);
				city = new City(x, y);
			} while (cities.contains(city));
			
			cities.add(city);
		}
		
		return cities;
	}
	
	public static void main(String[] args) {
		int N = input();
		List<City> cities = generateCities(N);
		GeneticAlgorithm.getInstance(N, cities).generateBestPath();
	}

}
