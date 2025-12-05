/**
 * @작성자 wony
 * @작성일 2020. 1. 8.
 * @사용처
 *
 */
package _1_Silver.Level_3;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 8.
 * @사용처
 * 
 */
public class a14889_스타트와링크 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 8.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	static String[][] filed;
	static boolean[] visitied;
	static int min = 100;
	static int aSum = 0;
	static int bSum = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int testCount = scanner.nextInt();
		filed = new String[testCount][testCount];
		visitied = new boolean[testCount];
		scanner.nextLine();
		for(int i = 0; i < testCount; i++)
			filed[i] = scanner.nextLine().split(" ");
		
		scanner.close();
		dfs(0, 0);
		
		System.out.println(min);
		
		
	}
	
	public static void dfs(int depth, int index){
		if(depth == visitied.length/2){
			aSum = 0;
			bSum = 0;
			for(int i = 0; i < visitied.length; i++){
				for(int j = i+1; j < visitied.length; j++){
					if(visitied[i] && visitied[j]){
						aSum += Integer.parseInt(filed[i][j]) + Integer.parseInt(filed[j][i]);
					}else if(!visitied[i] && !visitied[j]){
						bSum += Integer.parseInt(filed[i][j]) + Integer.parseInt(filed[j][i]);
					}
				}
			}
			min = Math.min(min, (int)Math.abs(aSum-bSum));
		}else{
			for(int i = index; i<visitied.length; i++){
				if(visitied[i]){continue;}
				else{
					visitied[i] = true;
					dfs(depth+1, i+1);
					visitied[i] = false;
				}
				
			}
		}
	}

}
