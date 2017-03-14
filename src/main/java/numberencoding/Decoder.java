package numberencoding;

import java.util.ArrayList;
/**
 * 
 * @author Matthias Hutter
 *
 */
public class Decoder {
	/**
	 * Constructor
	 * 
	 * @param dictionary the dictionary tree used to decode
	 * @param code the encoding used to decode
	 */
	public Decoder(DictTreeNode<Character> dictionary, char[][] code){
		encoding = code;
		dic = dictionary;
	}
	/**
	 * Specifies the phone number to decode
	 * 
	 * @param number the number including extra characters
	 */
	public void setNumber(String number){
		currentPhone = number;
		convertToDigits();
		end = currentDigits.size() - 1;
	}
	/**
	 * Starts decoder from the root of the phone number and root node of the dictionary tree
	 * does nothing if no number is set
	 */
	public void decodeCurrentNumber(){
		if (!currentDigits.isEmpty())
			decode(0, empty,empty,dic,true);
	}
	
	//PRIVATE
	private String currentPhone;
	private ArrayList<Integer> currentDigits = new ArrayList<Integer>();
	private char[][] encoding;
	private DictTreeNode<Character> dic;
	private int end = 0; 
	private static final StringBuilder empty = new StringBuilder("");
	/**
	 * Takes the currently set phone number digits and outputs all possible encodings
	 * 
	 * @param start the position of the decoder in the array of digits currentDigits
	 * @param prefix the encoded result before the start of the current word
	 * @param suffix the encoded result from the current word up to "start"
	 * @param node pointer to the current node in the dictionary tree dic
	 * @param digitPossible specifies whether a digit was encoded previously
	 * @return true if a digit was one of the possible encodings at this position
	 * 		   false if another encoding finished between start and end, or a digit was encoded previously
	 */
	
	private boolean decode(int start, StringBuilder prefix, StringBuilder suffix, DictTreeNode<Character> node, boolean digitPossible){
		int number = currentDigits.get(start); 
		DictTreeNode<Character> newNode;
		for (char letter : encoding[number]){ //iterates over all possible encodings
			if (node.hasChild(letter)){ //checks if dictionary tree has a encoded letter
				newNode = node.getChild(letter);
				// check if word ends here. Either output encoding or start new word
				// (transferring this block into a separate method or class significantly slows down execution speed)
				if (newNode.isWord()){ //word found
					digitPossible = false;
					if (start == end){ //output encoding
						StringBuilder output = new StringBuilder();
						output.append(currentPhone).append(": ").append(prefix).append(suffix).append(letter);
						log(output);
					}
					else{ //begin a new word
						//start recursion
						StringBuilder newPrefix = new StringBuilder();
						newPrefix.append(prefix).append(suffix).append(letter).append(" ");
						decode(start+1,newPrefix,empty, dic,true) ;
					}
				}//-------------------------------------------------------------------
				
				if (start < end){ //continue depth search with current word
					//start recursion
					StringBuilder newSuffix = new StringBuilder();
					newSuffix.append(suffix).append(letter);
					digitPossible = digitPossible & decode(start+1, prefix,newSuffix,newNode,digitPossible);
					
					//special handling for Umlauts
					if (isUmlaut(letter)){ 
						if (newNode.hasChild('\"')){ 
							newNode = newNode.getChild('\"');
							// check if word ends here. Either output encoding or start new word
							if (newNode.isWord()){ 
								digitPossible = false;
								if (start == end){ 
									StringBuilder output = new StringBuilder();
									output.append(currentPhone).append(": ").append(prefix).append(suffix).append(letter);
									log(output);
								}
								else{ ///start recursion with new word
									StringBuilder newPrefix = new StringBuilder();
									newPrefix.append(prefix).append(suffix).append(letter).append(" ");
									decode(start+1,newPrefix,empty, dic,true) ;
								}
							}
							//-------------------------------------------------------------------
							
							newSuffix.append('\"');
							digitPossible = digitPossible & decode(start+1, prefix,newSuffix,newNode,digitPossible);
						}
					}
				}
			}		
		}
		//special handling for digits
		if (digitPossible){ //at this position, a digit is encoded
			if (start == 0 & end != 0){ //no word at beginning
				//start recursion
				StringBuilder newPrefix = new StringBuilder();
				newPrefix.append(number).append(" ");
				decode(1,newPrefix,empty, dic,false);
			}
			else if (suffix.toString().isEmpty()){
				if (start == end ){ //no word finished at the end
					//start recursion
					StringBuilder output = new StringBuilder();
					output.append(currentPhone).append(": ").append(prefix).append(number);
					log(output);
				}
				else {//no word has begun
					//start recursion
					StringBuilder newPrefix = new StringBuilder();
					newPrefix.append(prefix).append(number).append(" ");
					decode(start+1,newPrefix,empty,dic,false);					
				}
			}
		}
		return digitPossible;
	}

	/**
	 * Converts the phone number string to a array of digits, extra characters removed
	 */
	private void convertToDigits(){
		currentDigits.clear();
		for (char c : currentPhone.toCharArray()){
    		if(c > 47 && c < 58)
    			currentDigits.add(Character.getNumericValue(c));
    	}
	}
	/**
	 * Checks if a letter is a Umlaut
	 * @param c the letter to be checked
	 * @return true if c is a Umlaut
	 */
	private  boolean isUmlaut(char c) {
		return "AEOUaeou".indexOf(c) != -1;
	}  
	/**
	 * sets "log" as shorthand for "System.out.println"
	 * @param aObject the argument passed to System.out.println
	 */
	private static void log(Object aObject){
		System.out.println(String.valueOf(aObject));
	}


}
