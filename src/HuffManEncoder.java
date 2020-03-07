
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;

public class HuffManEncoder {

	private Message fileReader(String file) {
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(Main.filePath + "\\" + file));
			Message obj = (Message) is.readObject();
			is.close();
			System.out.println("Encrypted file read.");
			return obj;

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String textFileReader(String fileName) {
		try {
			Scanner file = new Scanner(new File(Main.filePath + "\\" + fileName));
			StringBuilder builder = new StringBuilder();
			while (file.hasNext()) {
				builder.append(file.nextLine());
			}
			System.out.println("Message file read.");
			return builder.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void textFileCreator(String file, String fileName) {
		try {
			PrintWriter writer = new PrintWriter(Main.filePath + "\\" + fileName, "UTF-8");
			writer.println(file);
			writer.close();
			System.out.println("Decrypted message file created.");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void objFileCreator(Message msg, String fileName) {
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
			os.writeObject(msg);
			os.close();
			System.out.println("Encrypted file created.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	

	public void encodeMessage(String fileName) {
		String msg = textFileReader(fileName);
		Hashtable<Character, TreeNode> leaves = createHuffTree(msg);
		objFileCreator(createEncodedMessage(msg, leaves), Main.filePath + "\\EncodedMessageFile.bin");
	}

	private Hashtable<Character, TreeNode> createHuffTree(String msg) {
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

		return leaves;
	}

	private Message createEncodedMessage(String msg, Hashtable<Character, TreeNode> leaves) {
		Hashtable<String, Character> decodingTable = new Hashtable<String, Character>();
		Hashtable<Character, String> encodingTable = new Hashtable<Character, String>();

		// Creates encoding table for all the leaves/ characters in the tree
		for (Iterator<Entry<Character, TreeNode>> it = leaves.entrySet().iterator(); it.hasNext();) {
			Entry<Character, TreeNode> ent = it.next();
			StringBuilder currentPath = new StringBuilder();
			for (TreeNode currNode = ent.getValue(), prevNode = currNode; currNode.parent != null;) {
				currNode = currNode.parent;
				currentPath.insert(0, (currNode.children[0] == prevNode ? 0 : 1));
				prevNode = currNode;
			}
			encodingTable.put(ent.getKey(), currentPath.toString());
			decodingTable.put(currentPath.toString(), ent.getKey());
		}

		StringBuilder encodedData = new StringBuilder(); // String builder to create a string with the encoded data
		// Creates the bit string of all paths of all the letters in message (Encodes
		// message)
		for (int i = 0; i < msg.length(); i++)
			encodedData.append(encodingTable.get(msg.charAt(i)));
		return new Message(decodingTable, encodedData.toString());
	}

	public void decodeMessage(String encFile) {
		Message msg = fileReader(encFile);

		StringBuilder decodedData = new StringBuilder();

		StringBuilder bits = new StringBuilder();
		for (int i = 0; i < msg.data.length(); i++) {
			bits.append(msg.data.charAt(i));
			if (msg.encTable.containsKey(bits.toString())) {
				decodedData.append(msg.encTable.get(bits.toString()));
				bits = new StringBuilder();
			}
		}
		textFileCreator(decodedData.toString(), "DecodedMessage.txt");
	}

}
