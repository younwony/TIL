/**
 * @작성자 wony
 * @작성일 2019. 9. 25.
 * @사용처
 * TODO
 */
package _2_Gold.Level_2;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

/**
 * @작성자 wony
 * @작성일 2019. 9. 25.
 * @사용처
 * TODO
 */
public class a1005_ACM_Craft {

	/**
	 * @작성자 wony
	 * @작성일 2019. 9. 25.
	 * @사용처
	 * @Todo 풀이 실패.(메모제이션 기법으로 각각 노드까지의 시간을 저장하는 로직을 시도하였으나 로직 구현 실패로 무산)
	 * 1. 위상 정렬 사용(위상정렬 = 그래프의 각 노드를 어떠한 순서로 방문시 노드를 방문할 때 선행노드가 모두 방문되어야 조건을 만족하는 노드 방문순서를 결정하는 정렬)
	 * @param args
	 * TODO
	 */
	static int[] bildMem = {};
	static int[] bildTime = {};
	static int[][] bildRoute = {};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner scanner = new Scanner(System.in);
		
		int testCaseCount = scanner.nextInt();
		
		int blidCount = scanner.nextInt();
		
		int bildRoll = scanner.nextInt();
		
		bildTime = new int[bildRoll];
		
		bildMem = new int[bildRoll];
		
		bildMem[0] = bildTime[0];
		
		for(int i = 0; i< bildRoll; i++)
			bildTime[i] = scanner.nextInt();
		
		bildRoute = new int[bildRoll][2];
		for(int i = 0; i < bildRoll; i++){
			bildRoute[i][0] = scanner.nextInt();
			bildRoute[i][1] = scanner.nextInt();
		}
		
		int destination = scanner.nextInt(); // 여기까지가 설정
		
		List<Integer> destinationList = new ArrayList<Integer>();
		
		for(int i = 0; i<bildRoll; i++){
			if(bildRoute[i][1] == destination)
				destinationList.add(i);
		}
	}
	
	public static void bildMem(){
	}
}
