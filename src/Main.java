public class Main {
	
	public static void main(String[] args){
		HuffManEncoder enc = new HuffManEncoder();
		Message msg;
		msg = enc.encodeMessage("Hello world!");
		System.out.println(("Encoded Message: "+ msg.data));
		System.out.println("Decoded Message: " + enc.decodeMessage(msg));
	}

}
