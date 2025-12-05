/**
 * @작성자 wony
 * @작성일 2020. 2. 3.
 * @사용처
 *
 */
package _2_Gold.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 3.
 * @사용처
 * 
 */
public class a15711_환상의짝꿍 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 3.
	 * @사용처 골드바흐의 추측사용(2이상의 짝수는 소수의 합으로 표현이 가능하다. 단, 하나의 소수를 두번까지 사용이 가능 ex) 4 = 2 + 2
	 * @Todo *합성수 - 1보다 큰 모든 정수는 소수이거나 합성수 이다. 소수판별법 공부 필요!
	 * 미완
	 * @param args
	 * 
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int testCase = scanner.nextInt();
		
		for(int i = 0; i < testCase; i++){
			GoldBath(scanner.nextLong(),scanner.nextLong());
		}
		
		scanner.close();
	}
	
	public static void GoldBath(long a, long b){
		
		boolean result = false;
		
		long sum = a+b;
		
		if(sum%2 == 0 && sum > 2){
			result = true;
		}else {
			result = isDecimal(sum-2);
		}
		
		System.out.println(result ? "YES" : "NO");
		
	}		

	public static boolean isDecimal(long num){
		
		if(num == 2){return true;}
		
		for(long i = 2; i*i < num; i++){
			if(num % i == 0){
				return false;
			}
		}
		
		return true;
	}
}
