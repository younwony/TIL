/**
 * @작성자 wony
 * @작성일 2020. 3. 20.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 3. 20.
 * @사용처
 * 
 */
public class a10994_별찍기_19 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 20.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	
	public static boolean[][] filed; 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n = new Scanner(System.in).nextInt();
		
		int oneSideStarCount = 4*n - 3;
		
		filed = new boolean[2*oneSideStarCount - 1][oneSideStarCount];
		
		int startNum = 0;
		
		for(int i = n; i > 0; i--){
			oneSideStarCount = 4*i -3;
			drawFiled(startNum, oneSideStarCount);
			startNum += 2;
		}
		
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < filed.length; i++){
			if(i % 2 == 0){
				for(int j = 0; j< filed[0].length; j++){
					if(filed[i][j]){
						builder.append("*");
					}else{
						builder.append(" ");
					}
				}
			}else{
				builder.append("\n");
			}
		}
		
		
		System.out.println(builder.toString());
		
	}
	
	public static void drawFiled(int startNum, int oneSideStarCount){
		
//		가로
		for(int i = startNum; i < startNum + oneSideStarCount; i++){
			filed[2*startNum][i] = true;
			filed[2*startNum + 2*(oneSideStarCount-1)][i] = true;
		}
		
//		세로
		for(int i = startNum; i < startNum + oneSideStarCount; i++){
			filed[2*i][startNum] = true;
			filed[2*i][startNum + oneSideStarCount - 1] = true; 
		}
	}

}
