/**
 * @작성자 wony
 * @작성일 2019. 12. 30.
 * @사용처
 *
 */
package _1_Silver.Level_1;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 30.
 * @사용처
 * 
 */
public class a9663_N_Queen {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 30.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	static int count = 0;
	static int n;
	static int[] col;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		n = scanner.nextInt();
		col = new int[n];
		NQueen(0, 0);
		scanner.close();
		System.out.println(count);
		
	}
	
	public static void NQueen(int rowNum, int queenCount){
		if(queenCount == n){
			count++;
		}else{
			boolean[] check = new boolean[n]; // 형재 row의 위치값들 체크할 Boolean
			for(int i = 0; i < rowNum; i++){ //이전  Queen들의 위치
				check[col[i]] = true; // 이전 Queen의 위치 col값 제외
				if(col[i] - (rowNum - i) >= 0){check[col[i]-(rowNum - i)] = true;} //이전 Queen과 현재 row의 row차이만큼 이전Queen 위치의 col에서 뺀값 제외 ,즉 거리의 차이만큼 뺀값 제외
				if(col[i] + (rowNum - i) < n){check[col[i]+(rowNum - i)] = true;} //이전 Queen과 현재 row의 row차이만큼 이전Queen 위치의 col에서 더한값 제외,즉 거리의 차이만큼 더한값 제외
			}
			
			for(int i = 0; i < n; i++){
				if(check[i] != true){ //제외되지 않은 값들이 현재 row에서 Queen을 놓을 수 있는 위치이다.
					col[rowNum] = i; //현재 row에  Queen 위치 저장
					NQueen(rowNum+1, queenCount+1);
				}
			}
		}
	}

}
