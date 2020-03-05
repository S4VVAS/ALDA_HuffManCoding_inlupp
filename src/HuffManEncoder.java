import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class HuffManEncoder {

	public Message encodeMessage(String msg) {
		Hashtable<Character, Integer> freq = new Hashtable<Character, Integer>();

		for (int i = 0; i < msg.length(); i++) { // Create frequency chart of message
			char currChar = msg.charAt(i);
			if (freq.containsKey(currChar))
				freq.replace(currChar, (freq.get(currChar) + 1));
			else
				freq.put(currChar, 1);
		}

		PriorityQueue<TreeNode> prio = new PriorityQueue<TreeNode>();
		// We save the leaves to have a quick refrence to them, when creating the
		// encoding table
		Hashtable<Character, TreeNode> leaves = new Hashtable<Character, TreeNode>();

		// Populate priorityQueue with TreeNode Leafs
		for (Iterator<Entry<Character, Integer>> it = freq.entrySet().iterator(); it.hasNext();) {
			Entry<Character, Integer> currEnt = it.next();
			TreeNode newNode = new TreeNode(currEnt.getValue(), currEnt.getKey(), null);
			leaves.put(currEnt.getKey(), newNode);
			prio.add(newNode);
		}

		// Populate tree nodes with queue and leave root of tree on queue
		for (TreeNode zero, one; prio.size() > 1;) {
			zero = prio.poll();
			one = prio.poll();
			TreeNode newNode = new TreeNode(zero.freq + one.freq, '\u0000', new TreeNode[] { zero, one });
			prio.add(newNode);
			zero.parent = newNode;
			one.parent = newNode;
		}

		// Send root of tree and message to be converted into a encoded message
		return createEncodedMessage(prio.poll(), msg, leaves);
	}

	private Message createEncodedMessage(TreeNode root, String msg, Hashtable<Character, TreeNode> leaves) {
		Hashtable<Byte, Character> decodingTable = new Hashtable<Byte, Character>();
		Hashtable<Character, Byte> encTable = new Hashtable<Character, Byte>();

		for (int i = 0; i < msg.length(); i++) { // Creates encoding table for the message // CHANGE THIS TO BE THE LEAVES NOT THE MESSAGE, AS IT CREATES MULTIPLE ENTRIES IN HASHTABLE / REPLACES A BUNCH PROBS!
			TreeNode currChar = leaves.get(msg.charAt(i));
			byte enc = 0;
			for (TreeNode currNode = currChar, prevNode = currNode; currNode != root;) {
				currNode = currNode.parent;
				enc = (byte) ((enc << 1) | (currNode.children[0] == prevNode ? 0 : 1));
				prevNode = currNode;
			}
			System.out.println(enc);
			encTable.put(currChar.data, enc);
			decodingTable.put(enc, currChar.data);
		}

		StringBuilder builder = new StringBuilder(); // String builder to create a string with the encoded data
		for (int i = 0; i < msg.length(); i++) {
			builder.append(Integer.toBinaryString(encTable.get(msg.charAt(i))  & 0xFF));
		}

		return new Message(decodingTable, builder.toString());

	}

	public String decodeMessage(Message msg) {

		return null;
	}

	private boolean isLeaf(TreeNode node) {
		return node.data != '\u0000' && node.children == null;
	}

}
