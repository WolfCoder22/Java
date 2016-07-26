/**
 * Creates an object with two data points frequency and character
 * @author alexwolf
 *
 */
public class BinaryTreeData {
	char character;
	int frequency;
	
	//constructor for character and frequency
	public BinaryTreeData(char character, int frequency){
		this.character=character;
		this.frequency= frequency; 
		
	}
	
	//constructor for frequency
	public BinaryTreeData(int frequency){
		this.frequency=frequency;
	}
	
}
