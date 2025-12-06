package _0_Bronze.Level_4;

import java.util.Scanner;

public class a2753_윤년 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 9. 29.
	 * @사용처 윤년 확인 
	 * 4의 배수이면서, 100의 배수가 아닐때 또는 400의 배 수일때가 윤년
	 * @param args
	 * @Todo
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int a = scanner.nextInt();
		
		if(a%4 == 0 && a%100 != 0 || a%400 == 0)
			System.out.println("1");
		else
			System.out.println("0");
	}

}
