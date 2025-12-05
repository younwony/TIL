/**
 * @작성자 wony
 * @작성일 2019. 9. 18.
 * @사용처
 * TODO
 */
package _1_Silver.Level_3;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 9. 18.
 * @사용처
 * TODO
 */
public class a1003_피보나치함수 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 9. 18.
	 * @사용처 피보나치
	 * @Todo
	 * @param args
	 * TODO 기존의 일반적인 재귀방식으로 작동시 On2의 시간복잡도를 가져 시간초과를 이르킴. 따라서 메모제이션방식으로 해결
	 * 메모리를 먼저 선언해 해당 메모리에 적재하여 필요할때마다 부르는 방식으로한다. 해당 방법으로는 On의 시간복잡도를 가진다.
	 */
	static int[][] fivoMem = new int[40][2];
	 
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		fivoMem[0][0] = 1; 
		fivoMem[0][1] = 0; 
		fivoMem[1][0] = 0; 
		fivoMem[1][1] = 1; 
		
		fibonacci();
		
		Scanner scanner = new Scanner(System.in);
		
		int testCaseCount = scanner.nextInt();
		
		String[] answer = new String[testCaseCount];
		
		for(int i = 0; i < testCaseCount; i++){
			answer[i] = fibonacciAnswer(scanner.nextInt());
		}
		
		for (int i = 0; i < testCaseCount; i++) {
			System.out.println(answer[i].toString());
		}
		
	}
	
	public static void fibonacci() {
	    for(int i= 2; i<40; i++){
	    	fivoMem[i][0] = fivoMem[i-2][0] + fivoMem[i-1][0];
	    	fivoMem[i][1] = fivoMem[i-2][1] + fivoMem[i-1][1];
	    }
	}
	
	public static String fibonacciAnswer(int i){
		int a,b = 0;
		if(i == 0){
			return "1 0";
		}else if(i == 1){
			return "0 1";
		}else{
			a = fivoMem[i-2][0] + fivoMem[i-1][0];
			b = fivoMem[i-2][1] + fivoMem[i-1][1];
			return a + " " + b;
		}
	}
}
