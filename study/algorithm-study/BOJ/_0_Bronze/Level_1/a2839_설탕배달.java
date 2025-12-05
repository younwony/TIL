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
public class a2839_설탕배달 {

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
		
		int n = scanner.nextInt();
		
		scanner.close();
		
		int nMax = n/5;
		int minCount = 0;
		int count;
		int m;
		for(int i= 0; i <= nMax; i++){
			m = n - 5*i;
			if(m%3 == 0){
				count = i + m/3;
				minCount = minCount == 0 ? count :  (count < minCount ? count : minCount);
			}
		}
		
		System.out.println(minCount == 0 ? -1 : minCount);
		
	}

}
