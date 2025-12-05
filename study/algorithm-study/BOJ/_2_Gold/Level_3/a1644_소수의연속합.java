/**
 * @작성자 wony
 * @작성일 2020. 2. 2.
 * @사용처
 * @Todo
 */

package _2_Gold.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 2.
 * @사용처
 * @Todo
 */

public class a1644_소수의연속합 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 2.
	 * @사용처
	 * @param args
	 * @Todo
	 */
	
	static int[] decimal;
	static int n;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		n= scanner.nextInt();
		
		scanner.close();
		
		decimal = new int[n+1];
		
		Eratosthenes();
		
		System.out.println(decimalIsCheck(decimalArrayCreate()));
		
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 2.
	 * @사용처 에라토스테네스의 체 알고리즘
	 * @Todo
	 */
	public static void Eratosthenes() {

		decimal[0] = 1;
		decimal[1] = 1;
		
		for(int i = 2; i < decimal.length; i++) {
			for(int j = 2; i*j < decimal.length; j++) {
				decimal[i*j] = 1;
			}
		}
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 2.
	 * @사용처 소수 Array 생성
	 * @return
	 * @Todo
	 */
	public static int[] decimalArrayCreate() {
		
		int decimalCount = 0;
		for(int i = 0 ; i < decimal.length; i++) {
			if(decimal[i] == 0) {
				decimalCount++;
			}
		}
		
		int[] dp = new int[decimalCount];
		
		int index = 0;
		for(int i = 0 ; i < decimal.length; i++) {
			if(decimal[i] == 0) {
				dp[index] = i;
				index++;
			}
		}
		
		return dp;
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 2.
	 * @사용처 연속합 Count 함수
	 * @Todo
	 */
	public static int decimalIsCheck(int[] decimalArray) {
		
		int count = 0;
		
		int sum;
		
		for(int i = 0; i < decimalArray.length; i++) {
			sum = 0;
			for(int j = i; j < decimalArray.length; j++) {
				sum += decimalArray[j];
				if(sum == n) {
					count++;
					break;
				}else if(sum > n) {
					break;
				}
			}
		}
		
		return count;
	}

}
