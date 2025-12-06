/**
 * @작성자 wony
 * @작성일 2020. 1. 30.
 * @사용처
 *
 */
package _1_Silver.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 30.
 * @사용처
 * 
 */
public class a1912_연속합 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 30.
	 * @사용처
	 * @Todo 
	 * * 재풀이 필요!
	 * DP 문제 
	 * 해설 참조하고 품
	 * 꼭 다시 풀기
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int[] inputArray = new int[scanner.nextInt()];
		
		for(int i = 0; i < inputArray.length; i++){
			inputArray[i] = scanner.nextInt();
		}
		
		scanner.close();
		
		System.out.println(dp(inputArray));
	}
	
	public static int dp(int[] inputArray){
		int[] dp = new int[inputArray.length];
		dp[0] = inputArray[0];
		for(int i = 1; i < inputArray.length; i++){
			dp[i] = Math.max(inputArray[i], dp[i-1] + inputArray[i]);
		}
		
		int max = dp[0];
		
		for(int num : dp){
			max = Math.max(num, max);
		}
		
		return max;
		
		
	}

}
