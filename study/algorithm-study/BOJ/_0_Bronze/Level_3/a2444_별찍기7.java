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
public class a2444_별찍기7 {

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
		
		for(int i = 1; i <= 2*n -1; i++){
			if(i <= n){
				for(int j = 1; j <= n-i + (2*i-1); j++){
					if(j <= n-i){System.out.print(" ");}
					else{System.out.print("*");}
				}
			}else{
				for(int j = 1; j <= 2*n-1-(i-n); j++){
					if(j <= i-n){System.out.print(" ");}
					else{System.out.print("*");}
				}
			}
			System.out.println();
		}
	}

}
