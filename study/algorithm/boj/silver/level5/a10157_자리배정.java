/**
 * @작성자 wony
 * @작성일 2020. 2. 24.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 24.
 * @사용처
 * 
 */
public class a10157_자리배정 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 24.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		
		int person = scanner.nextInt();
		scanner.close();
		
		if(person > n*m){System.out.println(0);}
		else{
			int[][] filed = new int[m][n];
			
			int[] signX = {1,0,-1,0};
			int[] signY = {0,1,0,-1};
			
			int count = 1;
			
			int sign = 0;
			int startI = 0;
			int startJ = 0;
			
			boolean isXY = true;
			boolean result = true;
			
			while(result){
				if(isXY){
					for(int j = 0; j < m; j++){
						if(count != 1){
							startI += signX[sign];
							startJ += signY[sign];
						}
						filed[startI][startJ] = count++;
						if(count == person+1){result = !result; break;}
					}
					m--;
				}else{
					n--;
					for(int j = 0; j < n; j++){
						startI += signX[sign];
						startJ += signY[sign];
						filed[startI][startJ] = count++;
						if(count == person+1){result = !result; break;}
					}
				}
				
				isXY = !isXY;
				sign = (sign + 1)%4;
			}
			
		System.out.println((startJ+1) + " " + (startI+1));}
	}

}
