import java.io.Serializable;
import java.util.Hashtable;

public class Message implements Serializable {
	public final Hashtable<String, Character> encTable;
	public final String data;
	
	public Message(Hashtable<String, Character> encTable, String data) {
		this.encTable = encTable;
		this.data = data;
	}

}
