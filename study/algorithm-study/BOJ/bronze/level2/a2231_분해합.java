/**
 * @작성자 wony
 * @작성일 2019. 12. 19.
 * @사용처
 *
 */
package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 19.
 * @사용처
 * 
 */
public class a2231_분해합 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 19.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		scanner.close();
		int x = 0;
		while(x < n){
			if(n == ConsturctNum(x)){System.out.println(x); break;}
			else x++;
		}
		if(x == n) System.out.println(0);
	}
	
	public static int ConsturctNum(int n){
		int x = n;
		while(n != 0){
			x += n%10;
			n /= 10;
		}
		return x;
	}

}
