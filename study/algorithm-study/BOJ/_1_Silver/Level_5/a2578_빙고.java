/**
 * @작성자 wony
 * @작성일 2020. 3. 19.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 3. 19.
 * @사용처
 * 
 */
public class a2578_빙고 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 19.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	
	public static int[][] bingo = new int[5][5];
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				bingo[i][j] = scanner.nextInt();
			}
		}
		
		int count = 0;
		
		while(!isBingo()){
			bingoCheck(scanner.nextInt());
			count++;
		}
		
		System.out.println(count);
	}
	
	public static void bingoCheck(int number){
		
		loop:
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
 				if(bingo[i][j] == number){
					bingo[i][j] = 0;
					break loop;
				}
			}
		}
	}
	
	public static boolean isBingo(){
		
		int bingoCount = 0;
		boolean isBingo = false;
		
		for(int i = 0; i < 5; i++){
			isBingo = false;
			for(int j = 0; j < 5; j++){
				if(bingo[i][j] == 0){
					isBingo = true;
				}else{
					isBingo = false;
					break;
				}
			}
			
			if(isBingo){
				bingoCount++;
			}
		}
		
		for(int i = 0; i < 5; i++){
			isBingo = false;
			for(int j = 0; j < 5; j++){
				if(bingo[j][i] == 0){
					isBingo = true;
				}else{
					isBingo = false;
					break;
				}
			}
			
			if(isBingo){
				bingoCount++;
			}
		}
		
		isBingo = false;
		for(int i = 0; i< 5; i++){
			if(bingo[i][i] == 0){
				isBingo = true;
			}else{
				isBingo = false;
				break;
			}
		}
		
		if(isBingo){bingoCount++;}
		
		isBingo = false;
		for(int i = 0; i< 5; i++){
			if(bingo[i][4-i] == 0){
				isBingo = true;
			}else{
				isBingo = false;
				break;
			}
		}
		if(isBingo){bingoCount++;}
		
		isBingo = bingoCount >= 3 ? true : false;
		
		return isBingo;
	}

}
