public class TreeNode implements Comparable<TreeNode> {
	public final TreeNode[] children; // [0] holds a reference to smaller sub-tree
	// [1] holds a reference to bigger sub-tree
	public final char data; // If tree is a leaf, this holds the char (data) of the leaf
	public final int freq; // Stores the frequency of a leaf, or combined frequencies of children
	public TreeNode parent;

	public TreeNode(int freq, char data, TreeNode[] children) {
		this.children = children;
		this.data = data;
		this.freq = freq;
		// System.out.println("new node " + data + " " + freq);
	}

	@Override
	public int compareTo(TreeNode otherNode) {
		return Integer.compare(freq, otherNode.freq);
	}

}