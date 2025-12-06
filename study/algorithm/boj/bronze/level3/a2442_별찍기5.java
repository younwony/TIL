/**
 * @작성자 wony
 * @작성일 2020. 1. 16.
 * @사용처
 *
 */
package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 16.
 * @사용처
 * 
 */
public class a2442_별찍기5 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 16.
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
		for(int i = 1; i <= n; i++){
			for(int j = 1; j <= 2*n - 1; j++){
				if(j <= n-i){System.out.print(" ");}
				else if(j <= n-i+(2*i -1)){System.out.print("*");}
			}
			System.out.println();
		}
	}

}
