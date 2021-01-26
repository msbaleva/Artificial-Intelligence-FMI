package bg.sofia.uni.fmi.ai.homework.kmeans;

public enum ClusteringAlgorithmType {
	
	K_MEANS("KMeans"),
	K_MEANS_PLUS_PLUS("KMeans++"),
	SOFT_K_MEANS("Soft KMeans");
	
	private final String type;
	
	private ClusteringAlgorithmType(String type) {
		this.type = type;
	}
	
	public String toString() {
		return type;
	}
	

	

}
