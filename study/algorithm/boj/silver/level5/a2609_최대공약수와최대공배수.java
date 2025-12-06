/**
 * @작성자 wony
 * @작성일 2020. 1. 6.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 6.
 * @사용처
 * 
 */
public class a2609_최대공약수와최대공배수 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 6.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	static int gCD = 1;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		GreatestCommonDivisor(n, m); // 일반 최대공약수 구하는 공식
		gCD = GCDEuclideanAlgorithm(n,m); //유클리드 호제법 공식
		System.out.println(gCD);
		System.out.println((n*m)/gCD); //두 수의 곱 = 최대공약수 * 최소공배수 공식 활용
		scanner.close();
		
	}

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 6.
	 * @사용처 일반 최대 공약수 구하는 공식
	 * @Todo
	 * @param n
	 * @param m
	 * 
	 */
	public static void GreatestCommonDivisor(int n, int m){
		for(int i = 2; i <= Math.max(n, m); i++){
			if(n%i == 0 && m%i == 0){
				gCD*=i;
				GreatestCommonDivisor(n/i, m/i);
				break;
			}
		}
	}
	
	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 6.
	 * @사용처 유클리드 호제법
	 * @Todo
	 * n % m 이 0이 될때까지 큰수에서 작은수를 나눈 나머지를 이용한 공식
	 * 
	 * EX)
	 * 1071과 1029의 최대공약수를 구하면,
	 * 1071은 1029로 나누어떨어지지 않기 때문에, 1071을 1029로 나눈 나머지를 구한다. ≫ 42
	 * 1029는 42로 나누어떨어지지 않기 때문에, 1029를 42로 나눈 나머지를 구한다. ≫ 21
	 * 42는 21로 나누어떨어진다.
	 * 따라서, 최대공약수는 21
	 * 
	 * @param n
	 * @param m
	 * @return
	 * 
	 */
	public static int GCDEuclideanAlgorithm(int n, int m){
		if(m == 0){return n;}
		else{
			return GCDEuclideanAlgorithm(m, n%m);
		}
	}
}
