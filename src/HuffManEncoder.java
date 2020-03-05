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
		return createEncodedMessage(msg, leaves);
	}

	private Message createEncodedMessage(String msg, Hashtable<Character, TreeNode> leaves) {
		Hashtable<String, Character> decodingTable = new Hashtable<String, Character>();
		Hashtable<Character, String> encodingTable = new Hashtable<Character, String>();
		
		//Creates encoding table for all the leaves/ characters in the tree
		for (Iterator<Entry<Character, TreeNode>> it = leaves.entrySet().iterator(); it.hasNext();) {
			Entry<Character, TreeNode> ent = it.next();
			StringBuilder currentPath = new StringBuilder();
			for(TreeNode currNode = ent.getValue(), prevNode = currNode; currNode.parent != null;) {
				currNode = currNode.parent;
				currentPath.insert(0, (currNode.children[0] == prevNode ? 0 : 1));
				prevNode = currNode;
			}
			encodingTable.put(ent.getKey(), currentPath.toString());
			decodingTable.put(currentPath.toString(), ent.getKey());
		}

		StringBuilder encodedData = new StringBuilder(); // String builder to create a string with the encoded data
		//Creates the bit string of all paths of all the letters in message (Encodes message)
		for (int i = 0; i < msg.length(); i++) 
			encodedData.append(encodingTable.get(msg.charAt(i)));
		return new Message(decodingTable, encodedData.toString());
	}

	public String decodeMessage(Message msg) {
	
		StringBuilder decodedData = new StringBuilder();
		
		StringBuilder bits = new StringBuilder();
		for(int i = 0; i < msg.data.length(); i++){
			bits.append(msg.data.charAt(i));
			if(msg.encTable.containsKey(bits.toString())) {
				decodedData.append(msg.encTable.get(bits.toString()));
				bits = new StringBuilder();
			}
		}
		return decodedData.toString();
	}
	
}
