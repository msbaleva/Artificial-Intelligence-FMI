package bg.sofia.uni.fmi.ai.homework.partyclassificator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NaiveBayesianClassifier {
	
	
	private static final int CNT_ATTRIBUTES = 17;
	private static final int SMOOTHING_CONSTANT = 1;
	private static final int PERCENT = 100;
	private static final double PARTITION_COEFFICIENT = 0.9;
	private static final int YES = 0;
	private static final int NO = 1;
	private static final int UNKNOWN = 2;
	private int cntPeople;
	private List<Person> trainingSet;
	private List<Person> testSet;
	private Map<Integer, List<Integer>> attributeProbabilityIfDemocrat;
	private Map<Integer, List<Integer>> attributeProbabilityIfRepublican;
	private int cntRepublican;
	private int cntDemocrat;
	private int cntAccurate;
	
	private NaiveBayesianClassifier(List<Person> people) {			
		this.cntPeople = people.size();
		this.trainingSet = new ArrayList<>();
		this.testSet = new ArrayList<>();
		this.attributeProbabilityIfDemocrat = new HashMap<>();
		this.attributeProbabilityIfRepublican = new HashMap<>();
		initSets(people);
	}
	
	public static NaiveBayesianClassifier getClassifier(List<Person> people) {
		return new NaiveBayesianClassifier(people);
	}
	
	private void initSets(List<Person> people) {
		Collections.shuffle(people);
		int trainingSetSize = (int) (cntPeople * PARTITION_COEFFICIENT);
	    int testingSetSize = cntPeople - trainingSetSize;
	    this.cntDemocrat = 0;
	    Iterator<Person> iterator = people.iterator();
	    for (int i = 0; i < trainingSetSize; i++) {
	    	Person person = iterator.next();
    		if (person.isDemocrat()) {
    			this.cntDemocrat++;
    		} 
    		
    		trainingSet.add(person);
	    }
	    
	    this.cntRepublican = trainingSetSize - cntDemocrat;
	    for (int i = 0; i < testingSetSize; i++) {
	    	testSet.add(iterator.next());
	    }

		
	}
	
	private double computeProbability(int numerator, int denominator) {
		if (numerator == 0) {
			numerator += SMOOTHING_CONSTANT;
			denominator += SMOOTHING_CONSTANT;
		}
		
		return (double) numerator / (double) denominator;
	}
	
	public double computeAccuracy() {
		return computeProbability(cntAccurate, testSet.size()) * PERCENT;
	}
	
	public void classifyParty() {
		learn();
		classify();
	}
	
	private void learn() {
		for (int i = 1; i < CNT_ATTRIBUTES; i++) {			
			int sumYesRepublican = 0;
			int sumNoRepublican = 0;
			int sumUnknownRepublican = 0;
			int sumYesDemocrat = 0;
			int sumNoDemocrat = 0;
			int sumUnknownDemocrat = 0;
			for (Person person : trainingSet) {
				String[] attributes = person.getAttributes();
				if (person.isRepublican()) {
					if (attributes[i].equals(Answer.YES.toString())) {
						sumYesRepublican++;
					} else if (attributes[i].equals(Answer.NO.toString())) {
						sumNoRepublican++;
					} else {
						sumUnknownRepublican++;
					}
				} else {
					if (attributes[i].equals(Answer.YES.toString())) {
						sumYesDemocrat++;
					} else if (attributes[i].equals(Answer.NO.toString())) {
						sumNoDemocrat++;
					} else {
						sumUnknownDemocrat++;
					}
				}
			}
				List<Integer> republican = new ArrayList<>();
//				if (sumYesRepublican > sumNoRepublican) {
//					sumYesRepublican += sumUnknownRepublican;
//				} else {
//					sumNoRepublican += sumUnknownRepublican;
//				}
				republican.add(sumYesRepublican);
				republican.add(sumNoRepublican);
				republican.add(sumUnknownRepublican);
				attributeProbabilityIfRepublican.put(i, republican);
				
				List<Integer> democrat = new ArrayList<>();
//				if (sumYesDemocrat > sumNoDemocrat ) {
//					sumYesDemocrat += sumUnknownDemocrat;
//				} else {
//					sumNoDemocrat += sumUnknownDemocrat;
//				}
				democrat.add(sumYesDemocrat);
				democrat.add(sumNoDemocrat);
				democrat.add(sumUnknownDemocrat);
				attributeProbabilityIfDemocrat.put(i, democrat);
				
			
		}
	}

	private double computeRepublicanProbability(String[] attributes) {
		double probabilityRepublican = computeProbability(cntRepublican, trainingSet.size());
		double probability = Math.log(probabilityRepublican);
		for (int i = 1; i < CNT_ATTRIBUTES; i++) {
			List<Integer> probabilities = attributeProbabilityIfRepublican.get(i);
			int value = 0;
			if (attributes[i].equals(Answer.YES.toString())) {
				value = probabilities.get(YES);
			} else if (attributes[i].equals(Answer.NO.toString())) {
				value = probabilities.get(NO);
			} else {
				value = probabilities.get(UNKNOWN);
			}
			
			probability += Math.log(computeProbability(value, cntRepublican));
			
		}
		
		return probability;
	}
	
	private double computeDemocratProbability(String[] attributes) {
		double probabilityDemocrat = computeProbability(cntDemocrat, trainingSet.size());
		double probability = Math.log(probabilityDemocrat);
		for (int i = 1; i < CNT_ATTRIBUTES; i++) {
			List<Integer> probabilities = attributeProbabilityIfDemocrat.get(i);
			int value = 0;
			if (attributes[i].equals(Answer.YES.toString())) {
				value = probabilities.get(YES);
			} else if (attributes[i].equals(Answer.NO.toString())) {
				value = probabilities.get(NO);
			} else {
				value = probabilities.get(UNKNOWN);
			}
			
			probability += Math.log(computeProbability(value, cntDemocrat));
			
		}
		
		return probability;
	}

	private void classify() {
		for (Person person : testSet) {
			String[] attributes = person.getAttributes();
			double republicanProbablity = computeRepublicanProbability(attributes);
			double democratProbablity = computeDemocratProbability(attributes);
			if (person.isRepublican() && republicanProbablity > democratProbablity) {
				cntAccurate++;
			} else if (person.isDemocrat() && republicanProbablity < democratProbablity) {
				cntAccurate++;
			}
		}
		
	}
	

}
