package _0_Bronze.Level_4;

import java.util.Scanner;

public class a2588_곱셈 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 9. 28.
	 * @사용처
	 * 곱셈의 과정
	 * @param args
	 * @Todo
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int a,b = 0;
		a = scanner.nextInt();
		b = scanner.nextInt();
		
		String[] c = String.valueOf(b).split(""); 
		
		for(int i = c.length-1; i >= 0; i--) {
			System.out.println(a*Integer.parseInt(c[i]));
		}
		
		System.out.println(a*b);
	}

}
