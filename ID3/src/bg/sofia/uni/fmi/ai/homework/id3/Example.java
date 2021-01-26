package bg.sofia.uni.fmi.ai.homework.id3;


public class Example {
	private String[] attributes;
	private static final String SEPARATOR = ",";
	
	Example(String line) {
		this.attributes = line.split(SEPARATOR);
	}	
	
	public String[] getAttributes() {
		return attributes;
	}
	
	public String getAttributeByIndex(int index) {
		return attributes[index];
	}
	
	public boolean hasRecurrence() {
		return attributes[0].equals("recurrence-events");
	}
	
	public void print() {
		for (int i = 0; i < attributes.length; i++) {
			System.out.print(attributes[i] + " "  );
		}
		
		System.out.println();
		
	}

}
