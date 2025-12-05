/**
 * @작성자 wony
 * @작성일 2020. 2. 14.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_2;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 14.
 * @사용처
 * @Todo
 */

public class a2309_일곱난쟁이 {
	
	public static int sum;
	public static int[] nan = new int[9];
	public static boolean[] visitied = new boolean[9];
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		for(int i = 0 ; i < 9; i++) {
			 nan[i] = scanner.nextInt(); 
		}
		Arrays.sort(nan);
		
		dfs(0, 0);
		
		
	}
	
	public static void dfs(int index, int count) {
		if(count == 7) {
			sum = 0;
			for(int i = 0 ; i< 9; i++) {
				if(visitied[i]) {
					sum+=nan[i];
				}
			}
			if(sum == 100) {
				for(int i = 0; i < 9; i++) {
					if(visitied[i]) {
						System.out.println(nan[i]);
					}
				}
				System.exit(0);
			}
		}else {
			for(int i = index ; i < 9; i ++) {
				
				visitied[i] = true;
				count++;
				dfs(i+1, count);
				count--;
				visitied[i] = false;
				
			}
		}
	}
}
