package bg.sofia.uni.fmi.ai.homework.partyclassificator;

public enum Answer {
	
	YES("y"),
	NO("n"),
	UNKNOWN("?");
	
	private String type;
	
	private Answer(String type) {
		this.type = type;
	}

	public String toString() {
		return type;
	}
}
