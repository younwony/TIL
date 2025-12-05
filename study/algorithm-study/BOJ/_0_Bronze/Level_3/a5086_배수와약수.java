/**
 * @작성자 wony
 * @작성일 2019. 12. 31.
 * @사용처
 *
 */
package _0_Bronze.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 31.
 * @사용처
 * 
 */
public class a5086_배수와약수 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 31.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n,m;
		
		while((n=scanner.nextInt()) != (m=scanner.nextInt())){
			if(n%m == 0 ){System.out.println("multiple");}
			else if(n/m == 0){System.out.println("factor");}
			else{System.out.println("neither");}
		}
		scanner.close();
	}

}
