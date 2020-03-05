
//implementera komprimering och dekomprimering av filer med hjälp av Huffmankodning. 
//(giriga algoritmer, uppgift 10.7 i andra upplagan) I upplaga 3 är denna uppgift borttagen,
//men uppgiftstexten var denna: "10.7 Write a program to implement file compression 
//(and uncompression) using Huffman’s algorithm"
public class Main {
	
	public static void main(String[] args){
		HuffManEncoder enc = new HuffManEncoder();
		Message msg;
		
		msg = enc.encodeMessage("Hello world!");
		System.out.println((msg.data));
		
		
		System.out.println("Decoded Message: " + enc.decodeMessage(msg));
	}

}
