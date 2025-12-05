/**
 * @작성자 wony
 * @작성일 2019. 12. 5.
 * @사용처
 *
 */
package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 31.
 * @사용처
 * 
 */
public class a2816_디지털티비 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 31.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		scanner.nextLine();
		
		String[] broadCast = new String[n];
		
		int locationKBS1 = 0;
		int locationKBS2 = 0;
		for(int i = 0; i < n; i++){
			broadCast[i] = scanner.nextLine();
			if("KBS1".equals(broadCast[i])){locationKBS1 = i;};
			if("KBS2".equals(broadCast[i])){locationKBS2 = i;};
		}
		
		scanner.close();
		
		if(locationKBS1 > locationKBS2){locationKBS2++;}
		
		for(int i = 0; i < locationKBS1; i++){
			System.out.print(1);
		}
		for(int i = 0; i < locationKBS1; i++){
			System.out.print(4);
		}
		for(int i = 0; i < locationKBS2; i++){
			System.out.print(1);
		}
		for(int i = 1; i < locationKBS2; i++){
			System.out.print(4);
		}
	}
}
