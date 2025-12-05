package _0_Bronze.Level_3;

import java.util.Scanner;

public class a2523_별찍기 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		
		int tempn = n;
		
		for(int i = 1; i <= 2*n - 1; i++){
			if(i <= n){
				for(int j = 0; j < i; j++){
					System.out.print("*");
				}
			}else{
				tempn--;
				for(int j = 0; j < tempn; j++){
					System.out.print("*");
				}
			}
			System.out.println();
		}
	}

}
