package numberencoding;
/**
 * Solves the number encoding challenge.
 * 
 * @author Matthias Hutter
 */
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Reader {
	static DictTreeNode<Character> dic = new DictTreeNode<Character>();
	/**
	 * Reads from a dictionary and phone book file, and outputs all possible encodings according to the provided encoding rules
	 * 
	 * @throws IOException if an error occurred while trying to read from the files listed above.
	 */
	public static void main(String[] args) throws IOException {
		Reader text = new Reader();
	    text.readDic();
	    text.readPhones(); 
	}
	/**
	 * Converts path strings into Java.paths
	 */
	public Reader(){
		dicPath = Paths.get(DICTIONARY_PATH);
		phonePath = Paths.get(PHONEBOOK_PATH);
	}
	/**
	 * Reads line by line from the dictionary file and adds words to a tree
	 * 
	 * @throws IOException if an error occurred while trying to read from the dictionary file.
	 */
	public final void readDic() throws IOException {
		try (Scanner scanner =  new Scanner(dicPath, ENCODING.name())){
			while (scanner.hasNextLine()){
				appendTree(scanner.nextLine());
			}      
	    }
	}
	/**
	 * Adds a word to this dictionary tree
	 * @param word the word to be added 
	 */
	protected static void appendTree(String word){
		DictTreeNode<Character> pointer = dic;
		int iterator;
		for (iterator = 0; iterator < word.length(); ++iterator)
			 pointer = pointer.addChild(word.charAt(iterator));
		pointer.setWord();
	}
	/**
	 * Reads line by line from the phone book file and outputs possible encodings via the console
	 * 
	 * @throws IOException if an error occurred while trying to read from the phone book file
	 */
	public final void readPhones() throws IOException {
		try (Scanner scanner =  new Scanner(phonePath, ENCODING.name())){
			while (scanner.hasNextLine()){
				Decoder dec = new Decoder(dic,encoding);
				String Line = scanner.nextLine();
				dec.setNumber(Line);
				dec.decodeCurrentNumber();
			}      
	    }	
		
	}	

	
	// PRIVATE 
	private final static String DICTIONARY_PATH = "classes/dictionary.txt";
	private final static String PHONEBOOK_PATH = "classes/input.txt";
	private final static Charset ENCODING = StandardCharsets.UTF_8;  
	private final  Path dicPath,phonePath;
	private final static char[][] encoding = new char[][]{
			  { 'E', 'e'},
			  { 'J', 'N', 'Q', 'j', 'n', 'q'},
			  { 'R', 'W', 'X', 'r', 'w', 'x'},
			  { 'D', 'S', 'Y', 'd', 's', 'y'},
			  { 'F', 'T', 'f', 't'},
			  { 'A', 'M', 'a', 'm'},
			  { 'C', 'I', 'V', 'c', 'i', 'v'},
			  { 'B', 'K', 'U', 'b', 'k', 'u'},
			  { 'L', 'O', 'P', 'l', 'o', 'p'},
			  { 'G', 'H', 'Z', 'g', 'h', 'z'},
			};
}
