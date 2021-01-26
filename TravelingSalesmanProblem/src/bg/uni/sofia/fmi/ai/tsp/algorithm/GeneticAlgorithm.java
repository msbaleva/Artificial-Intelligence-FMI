package bg.uni.sofia.fmi.ai.tsp.algorithm;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import bg.uni.sofia.fmi.ai.tsp.city.City;
import bg.uni.sofia.fmi.ai.tsp.path.Path;

public class GeneticAlgorithm {
	
	private int N;
	private List<City> cities;
	private List<Path> currentPopulation;
	private int currentGeneration;
	private Random randomGenerator;
		
	public static final int GENERATION_THRESHOLD = 100;
	public static final int POPULATION_SIZE = 100;
	public static final int PERCENT_PARENTS = (int) Math.floor(POPULATION_SIZE * 0.2);
	public static final int PERCENT_OFFSPRING = (int) Math.floor(POPULATION_SIZE * 0.8);
	public static final int FIRST_GENERATION_PRINT = 10;
	public static final int SECOND_GENERATION_PRINT = 30;
	public static final int THIRD_GENERATION_PRINT = 50;
	public static final int FOURTH_GENERATION_PRINT = 70;
	
	private GeneticAlgorithm(int N, List<City> cities, List<Path> currentPopulation, Random randomGenerator) {
		this.N = N;
		this.cities = cities;
		this.currentPopulation = currentPopulation;
		this.randomGenerator = randomGenerator;
	}	
	
		
	public static GeneticAlgorithm getInstance(int N, List<City> cities) {
		return new GeneticAlgorithm(N, cities, new ArrayList<>(POPULATION_SIZE), new Random());
	}
	
	private boolean isToPrint(int currentGeneration) {
		return currentGeneration == FIRST_GENERATION_PRINT || currentGeneration == SECOND_GENERATION_PRINT 
				|| currentGeneration == THIRD_GENERATION_PRINT || currentGeneration == FOURTH_GENERATION_PRINT;
	}	

	private void generateInitialPopulation() {
		for (int i = 0; i < POPULATION_SIZE; i++) {			
			Path path = null;
			do {
				Collections.shuffle(cities);
				path = new Path(cities);
				
			} while (currentPopulation.contains(path));
			
			currentPopulation.add(new Path(path));
		}
	}
	
	private List<Path> generateOffspring() {
		List<Path> offspring = new ArrayList<>(POPULATION_SIZE);
		int pairs = POPULATION_SIZE / 2;
		for (int i = 0; i < pairs; i++) {
			List<Path> offspringPaths = reproductionFrom(currentPopulation.get(i), currentPopulation.get(i + pairs));
			offspring.addAll(offspringPaths);
		}
		
		return offspring;
	}
	
	private List<Path> reproductionFrom(Path firstParent, Path secondParent) {
		List<Path> offspring = new ArrayList<>(2);
		List<City> firstChild = new ArrayList<>(Collections.nCopies(N, null));
		List<City> secondChild = new ArrayList<>(Collections.nCopies(N, null));
		int cycleCnt = 0;
		for (int i = 0; i < N; i++) {
			if (firstChild.contains(firstParent.getCityAt(i))) {
				continue;
			}			
			
			int index = i;
			City cityFromFirst = null;
			City cityFromSecond = null;
			
			do {
				cityFromFirst = firstParent.getCityAt(index);
				cityFromSecond = secondParent.getCityAt(index);
				
				if (cycleCnt % 2 == 0) {
					firstChild.set(index, cityFromSecond);
					secondChild.set(index, cityFromFirst);
				} else {
					firstChild.set(index, cityFromFirst);
					secondChild.set(index, cityFromSecond);
				}
				
				int j = 0;
				while (firstParent.getCityAt(j) != cityFromSecond) {
					j++;
				}
				
				index = j;			
			} while (index != i);
			
			cycleCnt++;
		}
		
		mutate(firstChild);
		mutate(secondChild);
		offspring.add(new Path(firstChild));
		offspring.add(new Path(secondChild));
		return offspring;
	}
	
	private void mutate(List<City> child) {
		int firstIndex = 0;
		int secondIndex = 0;
		//do {
			firstIndex = randomGenerator.nextInt(N);
			secondIndex = randomGenerator.nextInt(N);
		//} while (firstIndex == secondIndex);
		
		Collections.swap(child, firstIndex, secondIndex);
	}
	
	private List<Path> buildNewPopulation(List<Path> offspring) {
		Collections.sort(offspring);
		Collections.sort(currentPopulation);
		List<Path> newPopulation = new ArrayList<>(POPULATION_SIZE);
		for (int i = 0; i < PERCENT_OFFSPRING; i++) {
			newPopulation.add(offspring.get(i));
		}

		for (int i = 0; i < PERCENT_PARENTS; i++) {
			newPopulation.add(currentPopulation.get(i));
		}
		
		return newPopulation;
	}
	
	public void printCurrentBest() {
		Collections.sort(currentPopulation);
		System.out.println("Generation " + currentGeneration);
		currentPopulation.get(0).print();
	}
	
	public void generateBestPath() {
		long start = System.currentTimeMillis();
		generateInitialPopulation();
		while (currentGeneration != GENERATION_THRESHOLD) {
			List<Path> offspring = generateOffspring();	
			currentPopulation = buildNewPopulation(offspring);
			if (isToPrint(currentGeneration)) {
				printCurrentBest();
			} 
			
			currentGeneration++;
		}
		
		printCurrentBest();
		long end = System.currentTimeMillis();
		//System.out.println("Elapsed time:" + (end - start) + "ms");
	}

}
