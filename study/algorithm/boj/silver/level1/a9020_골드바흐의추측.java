/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 *
 */
package _1_Silver.Level_1;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 * 
 */
public class a9020_골드바흐의추측 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 26.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCount = scanner.nextInt();
		
		int[] memDecimal = new int[10001];
		
		/* 에라토스테네스의 체 알고리즘 */
		memDecimal[1] = 1;
		for(int i = 2; i < Math.sqrt(memDecimal.length); i++){
			for(int j = i*i; j < memDecimal.length; j += i){
				memDecimal[j] = 1;
			}
		}
		
		int n,x,y;
		
		ArrayList<Integer> goldBachPartition = new ArrayList<Integer>();
		
		for(int i = 0 ; i < testCount; i++){
			goldBachPartition.clear();
			n = scanner.nextInt();
			x = 0;
			y = 0;
			/*소수 입력*/
			for(int j = 1; j < n; j++){
				if(memDecimal[j] == 0){goldBachPartition.add(j);}
			}
			
			/*골드바흐의 파티션 체크*/
			for(int k = 0; k < goldBachPartition.size(); k++){
				//골드바흐의 추측 값의 차이가 적은 값을 찾고자 하기에 입력 값의 절반 이하의 값만 탐색한다.
				if(goldBachPartition.get(k) > (n/2)+1) break;
				//골드바흐의 추측값 탐색 알고리즘
				if(goldBachPartition.contains(n-goldBachPartition.get(k))){
					x = goldBachPartition.get(k);
					y = n-x;
				} 
			}
			
			/*작은 수 부터 출력*/
			if(x > y){System.out.println(y + " " + x);}
			else {System.out.println(x + " " + y);}
		}
		
		scanner.close();
		
	}

}
