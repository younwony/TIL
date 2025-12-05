/**
 * @작성자 wony
 * @작성일 2019. 12. 4.
 * @사용처
 *
 */
package _0_Bronze.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 4.
 * @사용처
 * 
 */
public class a1783_병든나이트 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 4.
	 * @사용처 그리디 알고리즘
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		scanner.close();
		int count = 0;
		if(n == 1)	// 1일경우  무조건 1
			count = 1;
		else if(n == 2){ //2일경우 최대 4 
			if(m <= 6){
				count = (m+1)/2;
			}else
				count = 4;
		}else{ // 그외
			if(m <= 4){ //m 이 4이하일경우 m
				count = m;
			}else if(m <= 6){ //m이 4~6일경우 4
				count = 4;
			}else //그외
				count = m-2;
		}
		System.out.println(count);
	}

}
