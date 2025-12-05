/**
 * @작성자 wony
 * @작성일 2020. 2. 22.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 22.
 * @사용처
 * @Todo
 */

public class a4796_캠핑 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 22.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int L,P,V;
		
		int a,b;
		
		int count = 1;
		while((L=scanner.nextInt()) != 0 && (P=scanner.nextInt()) != 0 && (V=scanner.nextInt()) != 0){
			a = V/P *L;
			b = L > V%P ? V%P : L;
			System.out.println("Case " + count++ + ": " + (a+b));
		}
		
		scanner.close();
	}

}
