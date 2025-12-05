/**
 * @작성자 wony
 * @작성일 2019. 12. 13.
 * @사용처
 *
 */
package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 13.
 * @사용처
 * 
 */
public class a1924_2007년 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 13.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int month = scanner.nextInt();
		int day = scanner.nextInt();
		scanner.close();
		
		String[] dayArray = {"SUN","MON","TUE","WED","THU","FRI","SAT"};
		
		switch (month) {
		case 1:
			System.out.println(dayArray[day % 7]);
			break;
		case 2:
			System.out.println(dayArray[(day+3) % 7]);
			break;
		case 3:
			System.out.println(dayArray[(day+3) % 7]);
			break;
		case 4:
			System.out.println(dayArray[(day-1) % 7]);
			break;
		case 5:
			System.out.println(dayArray[(day+1) % 7]);
			break;
		case 6:
			System.out.println(dayArray[(day+4) % 7]);
			break;
		case 7:
			System.out.println(dayArray[(day-1) % 7]);
			break;
		case 8:
			System.out.println(dayArray[(day+2) % 7]);
			break;
		case 9:
			System.out.println(dayArray[(day+5) % 7]);
			break;
		case 10:
			System.out.println(dayArray[day % 7]);
			break;
		case 11:
			System.out.println(dayArray[(day+3) % 7]);
			break;
		case 12:
			System.out.println(dayArray[(day+5) % 7]);
			break;
		}
	}

}
