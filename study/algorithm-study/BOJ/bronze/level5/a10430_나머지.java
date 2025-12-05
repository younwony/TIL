package _0_Bronze.Level_5;

import java.util.Scanner;

public class a10430_나머지 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 9. 28.
	 * @사용처 (A+B)%C는 (A%C + B%C)%C 와 같을까?
			(A×B)%C는 (A%C × B%C)%C 와 같을까?
		-> 첫번째 식은 성립하지만 두번째 식은 성립하지 않은 결과를 얻엇다
	 * @param args
	 * @Todo (A+B)%C = (A%C + B%C)%C 공식의 이름이 있는지?
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int a,b,c = 0;
		a = scanner.nextInt();
		b = scanner.nextInt();
		c = scanner.nextInt();
		
		System.out.println((a+b)%c);
		System.out.println((a%c+b%c)%c);
		System.out.println((a*b)%c);
		System.out.println((a%c*b%c)%c);
	}

}
