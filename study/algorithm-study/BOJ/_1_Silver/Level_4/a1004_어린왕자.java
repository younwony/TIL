/**
 * @작성자 wony
 * @작성일 2019. 9. 24.
 * @사용처
 * TODO
 */
package _1_Silver.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 9. 24.
 * @사용처
 * TODO
 */
public class a1004_어린왕자 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 9. 24.
	 * @사용처 원 내부에 속해있는지 확인
	 * @Todo 1. 두원내에 같은점이 있는경우를 생각못함. 
	 * 2. Math.pow(StartY - y,2)를 해야하는데 Math.pow(StartY,2)로 작성하여 계산실수가 남. 
	 * @param args
	 * TODO
	 */
	static int startX = 0;
	static int startY = 0;
	static int stopX = 0;
	static int stopY = 0;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner scanner = new Scanner(System.in);
		
		int testCaseCount = scanner.nextInt(); //test case 개수
		
		int passCount = 0; //지나는 지점 개수
		
		int[] answer = new int[testCaseCount]; //출력 개수
		
		int testCaseCirCle = 0; //테스트 원 개수
		
		int x,y,r; // 각각 x, y, r
		
		for(int i=0; i<testCaseCount; i++){
			startX = scanner.nextInt();
			startY = scanner.nextInt();
			stopX = scanner.nextInt();
			stopY = scanner.nextInt();
			testCaseCirCle = scanner.nextInt();
			for(int j=0; j<testCaseCirCle; j++){
				x = scanner.nextInt();
				y = scanner.nextInt();
				r = scanner.nextInt();
				if(circlePassCheck(x,y,r))
						passCount++;
			}
			answer[i] = passCount;
			passCount = 0;
		}
		
		for(int i =0; i < testCaseCount; i++){
			System.out.println(answer[i]);
		}
	}
	
	public static boolean circlePassCheck(int x, int y, int r){
		//1
		if((Math.pow(startX-x,2) + Math.pow(startY-y, 2)) < Math.pow(r, 2) && (Math.pow(stopX-x,2) + Math.pow(stopY-y, 2)) < Math.pow(r, 2))
			return false;
		else if((Math.pow(startX-x,2) + Math.pow(startY-y, 2)) < Math.pow(r, 2)) 
			return true;
		else if((Math.pow(stopX-x,2) + Math.pow(stopY-y, 2)) < Math.pow(r, 2)) 
			return true;
		return false;
	}

}
