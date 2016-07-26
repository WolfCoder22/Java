import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import javax.swing.JFileChooser;
/**
 * This class compresses and decompresses a selected file and saves those outputs
 * @author Alexander Wolf, Dartmouth '18
 */
public class HuffmanEncoder {
	
	//set instance variables
	Map<Character,Integer> frequencyMap; //A map with character's as key and frequencies has values
	static Map<Character, String>	bitCodeMap; //A Map with Charcter's as key's and their corresponding bit-code as values 
	static BinaryTree<BinaryTreeData> finalTree; //Tree will all characters
	static String startingFile;	//location of file to compress 
	
	//constructor for intstance variables
	HuffmanEncoder(){
		frequencyMap= new TreeMap<Character, Integer>();
		bitCodeMap= new TreeMap<Character, String>();
	}
	
	/**
	 * method that returns a map with character's and frequencies
	 * 
	 * @param location	location of file in which to get character frequencies
	 */
	public Map<Character, Integer> getFrequencyMap() throws Exception{
		
		//reads over a file
		BufferedReader input= new BufferedReader(new FileReader(startingFile));
		
		//initializes character variable
		int character;
		
		//loops through all characters in the file
		while((character=input.read())!=-1){
			
			//casts character from int to char
			char current= (char) character;//creates current variable to be a char
			
			//checks to see if character is not in map
			if(!frequencyMap.containsKey(current)){
				
				frequencyMap.put(current,1);//add key(current) with value of 1(int)
			}
			
			//if key(character is already in Map
			else{
				
				//update value by +1
				int value=frequencyMap.get(current);
				value+=1;
				frequencyMap.put(current, value);
			}
		}
		
		//closes file-reader
		input.close();
		
		//returns the completed map
		return frequencyMap;
	}
	
	/**
	 * This method forms a binary Tree with leaf's with data of a character and frequency
	 * with inner nodes having data of just frequencies
	 
	 */
	public void formTree() throws Exception{
		
		//initializes the comparator
		Comparator<BinaryTree<BinaryTreeData>> comparator= new TreeComparator();
		
		//initializes the priority queue
		PriorityQueue<BinaryTree<BinaryTreeData>> priorityQueue= new PriorityQueue<BinaryTree<BinaryTreeData>>(getFrequencyMap().size(), comparator);
		
		// loop through every character
		for(char character: getFrequencyMap().keySet()){
			
			//creates a binary tree for each character
			BinaryTree<BinaryTreeData> tree= new BinaryTree<BinaryTreeData> (new BinaryTreeData(character,getFrequencyMap().get(character)));
			
			//add single binary tree for each character to the priority queue
			priorityQueue.add(tree);
			
		}
		
		while(priorityQueue.size()>1){
			
			//creates variables for first two items in the queue
			BinaryTree<BinaryTreeData> tree1= priorityQueue.remove();
			BinaryTree<BinaryTreeData> tree2= priorityQueue.remove();
			
			//create new binary tree, T, and children tree1 and tree2
			BinaryTree<BinaryTreeData> T= new BinaryTree<BinaryTreeData> (new BinaryTreeData(tree1.data.frequency + tree2.data.frequency), tree1, tree2);
			
			//adds new Tree T to priority Queue
			priorityQueue.add(T);
			finalTree=T;
			}
		}
	
	
		/**
		 * Creates a Map with Characters as Keys and their individual bit code as string values
		 * @param tree	tree for which code is created
		 * @return		Map<Character, String>
		 */
		public void getBitCodes(BinaryTree<BinaryTreeData> tree, String bitcode) throws Exception{
			
			//if the node is a leaf
			if(!tree.hasLeft() && !tree.hasRight()){
				
				//add it to bitCodeMap
				bitCodeMap.put(tree.data.character, bitcode);
			}
			
			//the tree is not a leaf
			else{
				
				if(tree.left!=null){
					//recursively call getBitCodes on left child
					getBitCodes(tree.left, bitcode+"0");
				}
				
				if(tree.right!=null){
					//recursively call getBitCodes on right child
					getBitCodes(tree.right, bitcode+"1");
				}
			}
		}
		
		
		public void getBitCodes(BinaryTree<BinaryTreeData> tree) throws Exception{
			
			//initializes the bit-code string
			String bitcode= "";
			System.out.println(bitcode);
			
			//searches through tree to get create a fully updated frequencyMap
			getBitCodes(tree, bitcode);
			
		}
		
		/**
		 * method that compress a file by writing it to a bit
		 * @return 
		 */
		public String compress() throws Exception{
			
			//read original file 
			BufferedReader input= new BufferedReader(new FileReader(startingFile));
			
			//creates filname from output of compress file
			String outputLocation= startingFile.substring(0, startingFile.length()-4);
			outputLocation= outputLocation+ "_compress.txt";
			
			//Initializes bitWriter
			BufferedBitWriter bitOutput= new BufferedBitWriter(outputLocation);
			
			
			//Initializes chracter variable
			int character;
			
			//loop over every character in a file
			while((character=input.read())!=-1){
				
				//casts character to a char
				char ccharacter= (char) character;
				
				//gets bit-code variable
				String bitcode= bitCodeMap.get(ccharacter);
				
				//initializes n for for loop
				int n= bitcode.length();
				
				//add bits to Bit-code ArrayList
				for (int i = 0; i < n; i++) {
					
					//turns bit to an integer 0 or 1
				    char bit = bitcode.charAt(i);
				    if(bit=='0'){
				    	bit=0;
				    }
				    else{
				    	bit=1;
				    }
				    
				    //writes bit to outputFile
				    bitOutput.writeBit(bit);
				}		
			}	
			
			//closes bit-writer and file-reader
			bitOutput.close();
			input.close();
			
			return outputLocation;
			
		}
		
		
		/**
		 * method that decompresses a bit-file and turns into a normal one
		 */
		public void uncompress() throws Exception{
			
			//Initializes bitReader
			BufferedBitReader bitInput= new BufferedBitReader(compress());
			
			//creates filename for decompress location
			String decompressLocation= startingFile.substring(0, startingFile.length()-4);
			decompressLocation= decompressLocation+ "_decompress.txt";
			
			//Initializes bitWriter
			BufferedWriter output= new BufferedWriter(new FileWriter(decompressLocation));
			
			//creates tracker variable to follow path on tree
			BinaryTree<BinaryTreeData> tracker= finalTree;
			
			//loop through all bits in file
			int bit;
			while((bit = bitInput.readBit()) !=-1){
				
				//if bit is a 0 go left on tree
				if(bit==0){
					tracker=tracker.left;
				}
				
				//if bit is 1 go right on tree
				else{
					tracker=tracker.right;
				}
				
				//if we hit a leaf write it to output file and reset tracker
				if(tracker.isLeaf()){
					output.write(tracker.data.character);
					tracker=finalTree;
				}
			}
			
			//close output and bitInput
			bitInput.close();
			output.close();
		}
		
		
		/**
		 * Puts up a fileChooser and gets path name for file to be opened.
		 * Returns an empty string if the user clicks "cancel".
		 * @return path name of the file chosen	
		 */
		public static String getFilePath() {
		  JFileChooser fc = new JFileChooser("."); // start at current directory
		  
		  int returnVal = fc.showOpenDialog(null);
		  if(returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();
		    String pathName = file.getAbsolutePath();
		    return pathName;
		  }
		  else
		    return "";
		}
		
		
		/**
		 * Main method
		 * @param args
		 */
		public static void main(String[] args) throws Exception{
				
			//lets user click on starting file
			startingFile= getFilePath();
			HuffmanEncoder main = new HuffmanEncoder();
			main.getFrequencyMap();
			//form final tree
			main.formTree();
			//get bitCOdeMap
			main.getBitCodes(finalTree);
			//uncompress and decompress file (compress called in decompress)
			main.uncompress();
			}
		
}
	
