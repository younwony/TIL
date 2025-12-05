/**
 * @작성자 wony
 * @작성일 2020. 1. 17.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 17.
 * @사용처
 * 
 */
public class a1934_최소공배수 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 17.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int testCount = scanner.nextInt();
		int a, b;
		for(int i = 0; i < testCount; i++){
			a = scanner.nextInt();
			b = scanner.nextInt();
			System.out.println(a*b/gcd(a, b));
		}
		scanner.close();
	}
	
	public static int gcd(int a, int b){
		if(b == 0){
			return a;
		}else{
			return gcd(b, a%b);
		}
	}

}
