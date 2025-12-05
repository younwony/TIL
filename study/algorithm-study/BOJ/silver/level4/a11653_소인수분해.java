package _1_Silver.Level_4;

import java.util.Scanner;

public class a11653_소인수분해 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		
		StringBuilder result = new StringBuilder();
		
		for(int i = 2 ; i * i <= n; i++){
			if(n % i == 0){
				result.append(i).append("\n");
				n /= i;
				i--;
			}
		}
		
		if(n > 1) result.append(n);
		
		System.out.println(result.toString());
	}
}
