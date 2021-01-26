package bg.sofia.uni.fmi.ai.homework.id3;

import java.util.HashMap;
import java.util.Map;

public class TreeNode {
	
	private static final String ROOT = "root";
	private static final int NONE = -1;
	private static final int NO_CHILDREN = 0;
	Map<String, TreeNode> children;
	TreeNode parent;
	int partioningAttribute;
	String branch;
	boolean hasRecurring;
	
	TreeNode() {
		this(null, NONE, ROOT);
	}
	
	TreeNode(TreeNode parent, int partioningAttribute, String branch) {
		this.parent = parent;
		this.partioningAttribute = partioningAttribute;
		this.branch = branch;
		this.children = new HashMap<>();
	}
	
	public void addChild(TreeNode node) {
		children.put(node.branch, node);
	}
	
	public void setLeaf(boolean hasRecurring) {
		this.hasRecurring = hasRecurring;
	}
	
	public TreeNode getRoot() {
		return children.get(ROOT);
	}
	
	public Map<String, TreeNode> getChildren() {
		return children;
	}
	
	public int getPartioningAttribute() {
		return partioningAttribute;
	}
	
	public boolean isLeaf() {
		return children.size() == NO_CHILDREN;
	}
	
	public boolean hasRecurring() {
		return hasRecurring;
	}
	
	public boolean isRoot() {
		return branch == ROOT;
	}

}
