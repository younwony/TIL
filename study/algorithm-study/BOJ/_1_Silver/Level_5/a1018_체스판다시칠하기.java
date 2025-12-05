/**
 * @작성자 wony
 * @작성일 2019. 12. 19.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 19.
 * @사용처
 * 
 */
public class a1018_체스판다시칠하기 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 19.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		String[][] filed = new String[scanner.nextInt()][scanner.nextInt()];
		scanner.nextLine();
		for(int i = 0; i < filed.length; i++)
			filed[i] = scanner.nextLine().split("");
		scanner.close();
		int countMin = 0;
		
		for(int i = 0; i < filed.length-7; i++){
			for(int j = 0; j < filed[0].length -7; j++){
				if(i ==0 && j ==0) countMin = filedCount(filed, i, j);
				else{
					countMin = countMin > filedCount(filed, i, j) ? filedCount(filed, i, j) : countMin;
				}
			}
		}
		
		System.out.println(countMin);
		
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 20.
	 * @사용처 Filed 내 고쳐야하는 최솟값 구하는 함수
	 * @Todo
	 * @param filed
	 * @param startX
	 * @param startY
	 * @return
	 * 
	 */
	public static int filedCount(String[][] filed, int startX, int startY){
		int countOne = 0;
		for(int i = startX; i < startX+8; i++){
			for(int j = startY; j < startY+8; j++){
				if((i+j) % 2 == 0){
					if("B".equals(filed[i][j])) countOne++;
				}else{
					if("W".equals(filed[i][j])) countOne++;
				}
			}
		}
		int countTwo = 0;
		for(int i = startX; i < startX+8; i++){
			for(int j = startY; j < startY+8; j++){
				if((i+j) % 2 != 0){
					if("B".equals(filed[i][j])) countTwo++;
				}else{
					if("W".equals(filed[i][j])) countTwo++;
				}
			}
		}
		
		return countOne > countTwo ? countTwo : countOne;
	}

}
