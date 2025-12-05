/**
 * @작성자 wony
 * @작성일 2020. 2. 25.
 * @사용처
 *
 */
package _0_easy;

/**
 * @작성자 wony
 * @작성일 2020. 2. 25.
 * @사용처
 * 
 */
public class a1342_Number_of_Steps_to_Reduce_a_Number_to_Zero {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 25.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(Solution(14));
	}
	
	public static int Solution(int num){
		
		int result = 0;
		
		while(num != 0){
			num = num % 2 == 0 ? num / 2 : num - 1;
			result++;
		}
		
		return result;
	}

}
