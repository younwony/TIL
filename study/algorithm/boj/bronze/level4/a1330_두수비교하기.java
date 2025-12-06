package _0_Bronze.Level_4;

import java.util.Scanner;

public class a1330_두수비교하기 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 9. 29.
	 * @사용처 크기 비교
	 * @param args
	 * @Todo
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int a,b = 0;
		a = scanner.nextInt();
		b = scanner.nextInt();
		
		if(a>b)
			System.out.println(">");
		else if(a<b)
			System.out.println("<");
		else
			System.out.println("==");
	}

}
