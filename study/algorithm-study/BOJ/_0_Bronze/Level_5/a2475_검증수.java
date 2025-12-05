package _0_Bronze.Level_5;

import java.util.Scanner;

public class a2475_검증수 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int sum = 0;
		
		for(int i = 0; i < 5; i++){
			sum += Math.pow(scanner.nextInt(), 2);
		}
		
		System.out.println(sum % 10);
	}

}
