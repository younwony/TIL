/**
 * @작성자 wony
 * @작성일 2019. 12. 4.
 * @사용처
 *
 */
package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 4.
 * @사용처
 * 
 */
public class a5585_거스름돈 {

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
		int money = 1000 - scanner.nextInt();
		scanner.close();
		
		int[] moneyArray = {500,100,50,10,5,1};
		int i = 0;
		int count = 0;
		while(money != 0){
			if(money/moneyArray[i] > 0){
				count += money/moneyArray[i];
				money %= moneyArray[i];
			}else
				i++;
		}
		
		System.out.println(count);
	}
}
