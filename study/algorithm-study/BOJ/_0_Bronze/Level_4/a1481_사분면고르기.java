package _0_Bronze.Level_4;

import java.util.Scanner;

public class a1481_사분면고르기 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner scanner = new Scanner(System.in);
		
		int x = scanner.nextInt();
		int y = scanner.nextInt();
		
		int result = 0;
		
		if(x > 0 && y > 0){
			result = 1;
		}else if( x < 0 && y > 0){
			result = 2;
		}else if( x < 0 && y < 0){
			result = 3;
		}else{
			result = 4;
		}
		
		System.out.println(result);

	}

}
