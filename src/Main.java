
public class Main {
	public static final String filePath = "C:\\Users\\savva\\Desktop";
	// LÄGG IN ER PATH TILL EN .txt FIL NI SKAPAT OCH VILL KRYPTERA

	public static void main(String[] args) {
		HuffManEncoder enc = new HuffManEncoder();
		enc.encodeMessage("Message.txt"); // Takes in a text file, spits out a bin with the message object
		enc.decodeMessage("EncodedMessageFile.bin"); // Takes in an encrypted object file, do not change this unless you
														// have renamed the original file
	}

}
