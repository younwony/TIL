/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 * 
 */
public class a2581_소수 {

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
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		scanner.close();
		
		int[] memDecimal = new int[10001];
		
		memDecimal[1] = 1;
		
		for(int i = 2; i < (int)Math.sqrt(memDecimal.length); i++){
			for(int j = i*i; j < memDecimal.length; j += i){
				memDecimal[j] = 1;
			}
		}
		
		int sum = 0;
		int minDecimal = 0;
		for(; n<=m; n++){
			if(memDecimal[n] == 0){
				if(sum == 0){minDecimal = n;}
				sum+=n;
			}
		}
		
		if(minDecimal == 0){System.out.println(-1);}
		else{
			System.out.println(sum);
			System.out.println(minDecimal);
		}
	}

}
