package bg.sofia.uni.fmi.ai.homework.id3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class ID3App {
	
	private static final String DATAFILE = "D:\\Docs\\ai-files\\breast-cancer.data";
	private static final String NAMESFILE = "D:\\Docs\\ai-files\\breast-cancer.names";
	private static final int FOLD_CNT = 10;
	private static DecimalFormat format = new DecimalFormat("0.00");
	
	static List<Example> readDataFile(String input) {
		int cnt = 0;
		List<Example> examples = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
			String line;
			while ((line = reader.readLine()) != null) {
				examples.add(new Example(line));
				cnt++;
				//if (cnt >= 10) break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return examples;
	}
	
	static List<String> readNamesFile(String input) {
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return lines;
	}
	
	public static void crossValidateAlgorithm(List<Example> examples, String[] attributeNames, Map<Integer, String[]> attributeValues) {
		double sum = 0.0;
		for (int i = 0; i < FOLD_CNT; i++) {
			ID3Algorithm decisionTree = ID3Algorithm.getInstance(examples, attributeNames, attributeValues);
			decisionTree.run();
			double accuracy = decisionTree.computeAccuracy();
			sum += accuracy;
			System.out.println("Training №" + (i + 1) + ": " + format.format(accuracy) + "%");
		}
		
		System.out.println("Average accuracy: " + format.format(sum / FOLD_CNT) + "%");
	}
	
	public static void main(String[] args) {
		List<Example> examples = readDataFile(DATAFILE);
		List<String> lines = readNamesFile(NAMESFILE);
		final String[] attributeNames = new String[lines.size()];
		final Map<Integer, String[]> attributeValues = new HashMap<>();
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			String[] splitLine = line.split(": ");
			attributeNames[i] = splitLine[0];	
			splitLine = splitLine[1].split(", ");
			attributeValues.put(i, splitLine);
		}
		
		crossValidateAlgorithm(examples, attributeNames, attributeValues);
		
	}
}
