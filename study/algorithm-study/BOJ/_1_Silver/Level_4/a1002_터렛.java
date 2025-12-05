/**
 * @작성자 wony
 * @작성일 2019. 9. 18.
 * @사용처
 * TODO
 */
package _1_Silver.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 9. 18.
 * @사용처
 * TODO
 */
public class a1002_터렛 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 9. 18.
	 * @사용처 터렛 위치 계산
	 * @Todo 원의 교차점을 찾는 문제
	 * 중심사이 거리계산 부호 실수 하나때문에 한시간가량 애먹음...
	 * @param args
	 * TODO
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
Scanner scanner = new Scanner(System.in);
		
		int testCaseCount = scanner.nextInt(); //testcase 개수
			
		double x1,y1,r1,x2,y2,r2,d = 0;
		
		int[] answer = new int[testCaseCount];
		
		for(int i=0; i< testCaseCount; i++){
			x1 = scanner.nextInt();
			y1 = scanner.nextInt();
			r1 = scanner.nextInt();
			x2 = scanner.nextInt();
			y2 = scanner.nextInt();
			r2 = scanner.nextInt();
			
			d = Math.sqrt(Math.abs(Math.pow(x1-x2,2) + Math.pow(y1-y2,2)));
			if( d == 0 ){ // 중심이 같을 경우
				if(r1 == r2)
					answer[i] = -1;
				else
					answer[i] = 0;
			}else{
				if(r1+r2 > d && d > Math.abs(r1-r2)) // 거리의 합이 중심의 거리보다 크면서 거리의 차가 중심의 거리보다 작을 경우
					answer[i] = 2;
				else if(r1+r2 == d || Math.abs(r1-r2) == d) 
					answer[i] = 1;
				else
					answer[i] = 0;
			}
		}
		
		for(int i = 0; i<answer.length; i++)
			System.out.println(answer[i]);
	}
}
