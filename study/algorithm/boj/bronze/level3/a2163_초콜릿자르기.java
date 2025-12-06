/**
 * @작성자 wony
 * @작성일 2020. 1. 13.
 * @사용처
 *
 */
package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 13.
 * @사용처
 * 
 */
public class a2163_초콜릿자르기 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 13.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	static int count = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		scanner.close();
		DP(n, m);
		
		System.out.println(count);
	}
	
	public static void DP(int n, int m){
		if(n == 1 && m == 1){
			return ;
		}else if(n == 1){
			count += m-1;
			return ;
		}else if(m == 1){
			count += n-1;
			return ;
		}
		if(n < m){
			count++;
			if(m%2 == 1){
				DP(n, m/2);
				DP(n, (m/2)+1);
			}else{
				DP(n, m/2);
				DP(n, m/2);
			}
		}else{
			count++;
			if(n%2 == 1){
				DP(n/2, m);
				DP(n/2+1, m);
			}else{
				DP(n/2, m);
				DP(n/2, m);
			}
		}
	}

}
