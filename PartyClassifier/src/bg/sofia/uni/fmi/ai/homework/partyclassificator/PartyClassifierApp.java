package bg.sofia.uni.fmi.ai.homework.partyclassificator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PartyClassifierApp {
	
	
	private static final String FILENAME = "D:\\Docs\\ai-files\\house-votes-84.data";
	private static final int FOLD_CNT = 10;
	private static DecimalFormat format = new DecimalFormat("0.00");
	
	static List<Person> readFile(String input) {
		List<Person> people = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
			String line;
			while ((line = reader.readLine()) != null) {
				Person person = new Person(line);
				people.add(person);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return people;
	}
	
	
	public static void main(String[] args) {
		List<Person> people = readFile(FILENAME);
		double sum = 0.0;
		for (int i = 0; i < FOLD_CNT; i++) {
			NaiveBayesianClassifier classifier = NaiveBayesianClassifier.getClassifier(people);
			classifier.classifyParty();
			double accuracy = classifier.computeAccuracy();
			sum += accuracy;
			System.out.println("Training №" + (i + 1) + ": " + format.format(accuracy) + "%");
		}
		
		System.out.println("Average accuracy: " + format.format(sum / FOLD_CNT) + "%");
		
	}

}
