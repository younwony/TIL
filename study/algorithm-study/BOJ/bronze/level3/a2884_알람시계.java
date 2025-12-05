package _0_Bronze.Level_3;

import java.util.Scanner;

public class a2884_알람시계 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 9. 29.
	 * @사용처 알람시계 설정(시간 빼기)
	 * @param args
	 * @Todo
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int hour,min;
		
		hour = scanner.nextInt();
		min = scanner.nextInt();
		
		int result = hour*60 + min - 45;
		
		if(result<0)
			result += 1440;
		
		System.out.println(result/60 + " " + result%60 );
		
	}

}
