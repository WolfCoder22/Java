import java.util.Comparator;

/**
 * Class that compares the root frequencies's of two binary trees 
 * @author Alex Wolf
 * @param <C>
 * @param <F>
 *
 */

public class TreeComparator implements Comparator<BinaryTree<BinaryTreeData>>{
	
	/* returns 1 if node1>node2
	 * returns -1 if node2>node1
	 * returns 0 if node's are equal
	 */
	
	public int compare(BinaryTree<BinaryTreeData> tree1, BinaryTree<BinaryTreeData> tree2){
		
		// on frequency is larger than node 2 frequency
		if(tree1.data.frequency > tree2.data.frequency){
			return 1;
		}
		
		//node one frequency is less than node2 frequency
		else if(tree1.data.frequency < tree2.data.frequency){
			return -1;
		}
		
		//two node frequencie's are equal
		else{
			return 0;
		}
		
	}
}
