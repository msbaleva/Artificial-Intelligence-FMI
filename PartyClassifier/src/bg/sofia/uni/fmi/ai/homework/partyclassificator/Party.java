package bg.sofia.uni.fmi.ai.homework.partyclassificator;

public enum Party {
	
	DEMOCRAT("democrat"),
	REPUBLICAN("republican");
	
	private final String type;
	
	private Party(String type) {
		this.type = type;
	}
	
	public String toString() {
		return type;
	}

}
