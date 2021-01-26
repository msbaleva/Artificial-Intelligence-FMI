package bg.sofia.uni.fmi.ai.homework.partyclassificator;

public class Person {
	
	private String[] attributes;
	
	private static final String SEPARATOR = ",";
	
	Person(String line) {
		this.attributes = line.split(SEPARATOR);		
	}
	
	public String[] getAttributes() {
		return attributes;
	}
	
	public boolean isRepublican() {
		return attributes[0].equals(Party.REPUBLICAN.toString());
	}
	
	public boolean isDemocrat() {
		return attributes[0].equals(Party.DEMOCRAT.toString());
	}
	
	public void print() {
		for (int i = 0; i < attributes.length; i++) {
			System.out.print(attributes[i] + " "  );
		}
		
		System.out.println();
		
	}
}
