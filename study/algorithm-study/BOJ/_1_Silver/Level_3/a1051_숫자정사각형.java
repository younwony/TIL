package _1_Silver.Level_3;

import java.util.Scanner;

public class a1051_숫자정사각형 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		int m = scanner.nextInt();
		int max = n < m ? n : m;
		
		scanner.nextLine();
		
		String[][] field = new String[n][m];
		
		String[] test;
		
		for(int i = 0 ; i < n; i++){
			field[i] = scanner.nextLine().split("");
		}
		
		int result = 1;
		
		for(int i = 1; i < max; i++){
			for(int j = 0; j < n - i; j++){
				for(int k = 0; k < m - i; k++){
					if(field[j][k].equals(field[j][k + i]) && field[j][k].equals(field[j + i][k]) && field[j][k].equals(field[j + i][k + i])){
						result = i + 1 > result ? i + 1 : result;
					}
				}
			}
		}
		
		System.out.println(result*result);
	}

}
