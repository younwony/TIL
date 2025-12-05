/**
 * @작성자 wony
 * @작성일 2019. 11. 8.
 * @사용처
 *
 */
package Sort;

/**
 * @작성자 wony
 * @작성일 2019. 11. 8.
 * @사용처
 * 
 */
public class Tree {
	
	int[] Array;
	int left;
	int right;
	int middle;
	
	boolean searchCK;
	
	public Tree(int[] Array){
		this.Array = Array;
		this.left = 0;
		this.right = Array.length;
		this.middle = (left+right)/2;
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 4.
	 * @사용처 이진탐색
	 * @Todo
	 * 
	 */
	public boolean BinarySearch(int data){
		
		if(right - left < 0){
			searchCK =  false;
			return searchCK;
		}
		
		middle = (left+right)/2;
		
		if(data == Array[middle])
			searchCK =  true;
		else if(data > Array[middle]){
			left = middle+1;
			BinarySearch(data);
		}
		else if(data < Array[middle]){
			right = middle-1;
			BinarySearch(data);
		}
		
		return searchCK;
	}
}
