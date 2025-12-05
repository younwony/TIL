/**
 * @작성자 wony
 * @작성일 2020. 2. 20.
 * @사용처
 *
 */
package _0_easy;

/**
 * @작성자 wony
 * @작성일 2020. 2. 20.
 * @사용처
 * 
 */
public class a1_TwoSum {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 20.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int[] nums = {3,2,4}; 
		int target = 6;
		Solution(nums, target);
	}
	
	public static int[] Solution(int[] nums, int target){
		int[] result = new int[2];
		
		for(int i = 0 ; i < nums.length; i++){
			result[0] = i;
			for(int j = i+1; j < nums.length; j++){
				if(nums[j] == target - nums[i]){
					result[1] =j;
					break;
				}
			}
			if(result[1] != 0){
				break;
			}
		}
        return result;
	}

}
