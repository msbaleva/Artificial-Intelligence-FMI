package bg.sofia.uni.fmi.ai.homework.id3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FrequencyTable {
	
	private Map<Integer, Map<String, int[]>> table;
	private static Set<Integer> availableAttributes;
	
	static {
		FrequencyTable.availableAttributes = new HashSet<>();
		for (int i = 1; i < ID3Algorithm.CNT_ATTRIBUTES; i++) {
			availableAttributes.add(i);
		}
	}
	
	FrequencyTable(List<Example> examples) {
		this.table = new HashMap<>();
		initTable();
		fillTable(examples);		
	}
	
	private void initTable() {
		for (Integer i : availableAttributes) {
			Map<String, int[]> valuesByAttribute = new HashMap<>();
			String[] values = ID3Algorithm.attributeValues.get(i);
			for (String value : values) {
				valuesByAttribute.put(value, new int[] {0, 0});
			}
			
			table.put(i, valuesByAttribute);
		}
	}
	
	private void fillTable(List<Example> examples) {
		for (Integer attribute : availableAttributes) {			
			Map<String, int[]> valuesByAttribute = table.get(attribute);			
			for (Example example : examples) {
				String attributeValue = example.getAttributeByIndex(attribute);
				int[] cntByValue = valuesByAttribute.get(attributeValue);
				if (cntByValue != null) {
					if (example.hasRecurrence()) {
						cntByValue[0]++;
					} else {
						cntByValue[1]++;
					}
					
					valuesByAttribute.put(attributeValue, cntByValue);
				}
			}
		}
	}
	
	public Map<String, int[]> getByAttribute(int attribute) {
		return table.get(attribute);
	}
	
	public static void addAttributeToUsed(int attribute) {
		availableAttributes.add(attribute);
	}
	
	public static void removeAttributeFromUsed(int attribute) {
		availableAttributes.remove(attribute);
	}

}
