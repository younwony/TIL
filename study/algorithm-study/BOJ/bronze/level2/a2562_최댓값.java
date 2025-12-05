/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
package _0_Bronze.Level_2;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 10. 2.
 * @사용처
 * TODO
 */
public class a2562_최댓값 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 2.
	 * @사용처 최댓값, 최댓값 수의 인덱스
	 * @Todo
	 * @param args
	 * TODO
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int[] result = new int[9];
		int max = 0;
		int maxIndex = 0;
		
		for(int i = 0; i < 9; i ++){
			result[i] = scanner.nextInt();
			if(result[i] > max){
				max = result[i];
				maxIndex = i+1;
			}
		}
		
		System.out.println(max);
		System.out.println(maxIndex);
	}

}
