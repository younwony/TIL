/**
 * @작성자 wony
 * @작성일 2019. 12. 31.
 * @사용처
 *
 */
package _2_Gold.Level_3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 31.
 * @사용처
 * 
 */
public class a2580_스도쿠 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 31.
	 * @사용처 스도쿠 (백트래킹)
	 * @Todo
	 * @param args
	 * 
	 */
	
	static int zeroCount = 0;
	static int[][] sudoku = new int[9][9];
	static ArrayList<int[]> arrayList = new ArrayList<int[]>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		/* 
		 * 1. inputData 초기화 
		 * 2. 0 개수(입력해야할 개수) 카운트
		 * */
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				sudoku[i][j] = scanner.nextInt();
				if(sudoku[i][j] == 0){
					arrayList.add(new int[]{i,j});
					zeroCount++;
				}
			}
		}
		scanner.close();
		
		solvedBackTraking(0);
		
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 31.
	 * @사용처 백트래킹으로 찾기
	 * @Todo
	 * @param index
	 * 
	 */
	public static void solvedBackTraking(int index){
		if(zeroCount == index){
			for(int i = 0; i < 9; i++){
				for(int j = 0; j < 9; j++){
					System.out.print(sudoku[i][j] + " ");
				}
				System.out.println();
			}
			/* 답이 여러개 일 경우가 있기에 하나의 답만 출력 후 종료 */
			System.exit(0);
		}else{
			int[] zeroFiled = arrayList.get(index); // 0의 위치 필드 가져오기
			
			/* 1~10 까지 대입하면서 찾기 */
			for(int i = 1; i <10; i++){
				sudoku[zeroFiled[0]][zeroFiled[1]] = i;
				
				/* 0위치의 9칸, 행, 열 체크 후 i가 중복값이 아닐 경우 진행 */
				if(LineCheck(zeroFiled[0],zeroFiled[1]) && NineCheck(zeroFiled[0],zeroFiled[1])){
					solvedBackTraking(index+1);
				}
				
				/* i값이 9까지 진행 후 값을 못찾을 경우 다시 0 으로 초기화 시켜줘야 LineCheck, NineCheck 에서 제대로 체크 가능(9값이 지워지지 않은상태로 체크하는 경우 발생하기 때문)*/
				sudoku[zeroFiled[0]][zeroFiled[1]] = 0; // 
			}
		}
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 31.
	 * @사용처 행,열 체크
	 * @Todo
	 * @param x
	 * @param y
	 * @return
	 * 
	 */
	public static boolean LineCheck(int x, int y){
		
		int checkNum = sudoku[x][y]; // 체크할 값 가져오기
		
		for(int i = 0; i < 9; i++){
			/* x행, y열  체크*/
			if((checkNum == sudoku[x][i] && i != y)|| (checkNum == sudoku[i][y] && i != x)){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 31.
	 * @사용처 9칸 체크
	 * @Todo
	 * @param x
	 * @param y
	 * @return
	 * 
	 */
	public static boolean NineCheck(int x, int y){
		
		int checkNum = sudoku[x][y]; // 체크할 값 가져오기
		
		/* 입력값이 존재하는 9칸의 첫지점 구하기 */
		int nX = x/3;
		int nY = y/3;
		
		for(int i = nX*3; i < nX*3 + 3; i++){
			for(int j = nY*3; j < nY*3 + 3; j++){
				/* 칸 체크 */
				if(checkNum == sudoku[i][j] && i != x && j != y){
					return false;
				}
			}
		}
		
		return true;
	}
}
