package _0_Bronze.Level_3;

import java.util.Scanner;

public class a2455_지능형기차 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int input,output;
		int max = 0;
		int count = 0;
		
		for(int i = 0; i < 4; i++){
			output = scanner.nextInt();
			input = scanner.nextInt();
			count += input-output;
			if(count > max){max = count;}
		}
		System.out.println(max);
	}

}
