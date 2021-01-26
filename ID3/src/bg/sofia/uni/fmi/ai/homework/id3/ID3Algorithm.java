package bg.sofia.uni.fmi.ai.homework.id3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class ID3Algorithm {

	
	static final int CNT_ATTRIBUTES = 10;
	private static final double PARTITION_COEFFICIENT = 0.9;
	private static final int PERCENT = 100;
	private int cntExamples;
	private List<Example> trainingExamples;
	private List<Example> testingExamples;
	private String[] attributeNames;
	private TreeNode decisionTree;
	private int cntAccurate;
	static Map<Integer, String[]> attributeValues;
	
	private ID3Algorithm(List<Example> examples, String[] attributeNames, Map<Integer, String[]> attributeValues) {
		this.cntExamples = examples.size();
		this.trainingExamples = new ArrayList<>();
		this.testingExamples = new ArrayList<>();
		this.attributeNames = attributeNames;
		this.decisionTree = new TreeNode();
		ID3Algorithm.attributeValues = attributeValues;
		initSets(examples);		
		
	}
	
	private void initSets(List<Example> examples) {
		Collections.shuffle(examples);
		int trainingSetSize = (int) (cntExamples * PARTITION_COEFFICIENT);
	    int testingSetSize = cntExamples - trainingSetSize;
	    Iterator<Example> iterator = examples.iterator();
	    for (int i = 0; i < trainingSetSize; i++) {
    		trainingExamples.add(iterator.next());
	    }
	    
	    for (int i = 0; i < testingSetSize; i++) {
	    	testingExamples.add(iterator.next());
	    }
		
	}
	
	public void run() {
		train();
		classify();
	}
	
	private void classify() {
		for (Example example : testingExamples) {
			traverseTree(decisionTree, example);
		}
	}
	
	private void traverseTree(TreeNode currentNode, Example example) {
		if (currentNode.isLeaf()) {
			cntAccurate += example.hasRecurrence() == currentNode.hasRecurring() ? 1 : 0;
			return;
		}
		
		Map<String, TreeNode> children = currentNode.getChildren();
		String attribute = example.getAttributeByIndex(currentNode.getPartioningAttribute());
		traverseTree(children.get(attribute), example);
	}

	private void train() {
		createDecisionTree(trainingExamples, decisionTree);
	}
	
	private double computeTargetEntropy(int positive, int negative) {
		double positiveProbability = 0.0;
		double negativeProbability = 0.0;
		if (positive != 0) {
			positiveProbability = computeProbability(positive, positive + negative);;
		}
		
		if (negative != 0) {
			negativeProbability = computeProbability(negative, positive + negative);
		}		
		
		return - (positiveProbability * log2(positiveProbability)) - (negativeProbability * log2(negativeProbability));
	}
	
	private double computeEntropy(List<Example> set, TreeNode currentNode) {
		int sumHasRecurrence = 0;
		for (Example example : set) {
			if (example.hasRecurrence()) {
				sumHasRecurrence++;
			}
		}
		
		
		if (sumHasRecurrence == 0) {
			currentNode.setLeaf(false);
			return 0.0;
		} else if (sumHasRecurrence == set.size()) {
			currentNode.setLeaf(true);
			return 0.0;
		}
		
		return computeTargetEntropy(sumHasRecurrence, set.size() - sumHasRecurrence);
	}
	
	private double computeMutualEntropy(FrequencyTable table, int attribute, int total) {
		Map<String, int[]> values = table.getByAttribute(attribute);
		double entropy = 0.0;
		for (String value : values.keySet()) {
			int[] cntByValues = values.get(value);
			double targetEntropy = computeTargetEntropy(cntByValues[0], cntByValues[1]);
			double valueProbability = computeProbability(cntByValues[0] + cntByValues[1], total);
			entropy += valueProbability * targetEntropy;
		}
		
		return entropy;		
	}
	
	private double probabilityProduct(double first, double second) {
		double logProduct = 0.0;
		if (first == 0 && second == 0) {
			return logProduct;
		}
		if (first > 0) {
			logProduct += Math.log(first);
		} 
		
		if (second > 0) {
			logProduct += Math.log(second);
		}
		
		return Math.pow(Math.E, logProduct);
	}
	
	private double computeProbability(int numerator, int denominator) {
		return (numerator == 0 || denominator == 0) ? 0.0 : (double) numerator / (double) denominator;
	}
	
	private double log2(double number) {
		return number == 0 ? 0 : Math.log(number) / Math.log(2);
	}
	
	
	private void createDecisionTree(List<Example> set, TreeNode currentNode) {
		FrequencyTable freqTableAttributes = new FrequencyTable(set);
		double entropy = computeEntropy(set, currentNode);
		if (entropy == 0.0) {
			return;
		}
		
		double maxGain = 0.0;
		int maxGainAttribute = -1;
		for (int i = 1; i < CNT_ATTRIBUTES; i++) {
			double mutualEntropy = computeMutualEntropy(freqTableAttributes, i, set.size());
			double gain = entropy - mutualEntropy;
			if (gain >= maxGain) {
				maxGain = gain;
				maxGainAttribute = i;
			}
		}
		
		splitToSets(set, maxGainAttribute, currentNode);
		
	}
	
	private void splitToSets(List<Example> set, int attribute, TreeNode currentNode) {
		FrequencyTable.addAttributeToUsed(attribute);
		String[] values = attributeValues.get(attribute);
		Map<String, List<Example>> splitSets = new HashMap<>();
		for (String value : values) {
			splitSets.put(value, new ArrayList<>());
		}
		
		for (Example example : set) {			
			for (String value : values) {
				if (example.getAttributeByIndex(attribute).equals(value)) {
					splitSets.get(value).add(example);
				}
			}
		}
		
		for (String value : values) {			
			createDecisionTree(splitSets.get(value), createNewNode(attribute, value, currentNode));
		}
		
		FrequencyTable.removeAttributeFromUsed(attribute);
	}
	
	private TreeNode createNewNode(int attribute, String value, TreeNode currentNode) {
		if (currentNode.isRoot()) {
			currentNode = new TreeNode(null, attribute, value);
			return currentNode;
		}
		
		TreeNode newNode = new TreeNode(currentNode, attribute, value);
		currentNode.addChild(newNode);
		return newNode;
	}
	
	public double computeAccuracy() {
		return computeProbability(cntAccurate, testingExamples.size()) * PERCENT;
	}
	
	public static ID3Algorithm getInstance(List<Example> examples, String[] attributeNames, Map<Integer, String[]> attributeValues) {
		return new ID3Algorithm(examples, attributeNames, attributeValues);
	}
}
