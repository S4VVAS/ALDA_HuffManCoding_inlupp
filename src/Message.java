import java.util.Hashtable;

public class Message {
	
	public final Hashtable<Byte, Character> encTable;
	public final String data;
	
	public Message(Hashtable<Byte, Character> encTable, String data) {
		this.encTable = encTable;
		this.data = data;
	}
	

}
