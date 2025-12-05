package _0_Bronze.Level_2;

import java.util.Scanner;

public class a1159_농구경기 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int[] firstNameArray = new int[26];
		
		int caseCount = scanner.nextInt();
		
		scanner.nextLine();
		
		for(int i = 0; i< caseCount; i++) {
			firstNameArray[(int)scanner.nextLine().charAt(0) - 'a']++;
		}
		
		StringBuilder result = new StringBuilder();
		
		for(int i = 0; i< firstNameArray.length; i++) {
			if(firstNameArray[i] >= 5) {
				result.append((char)(i + 'a'));
			}
		}
		
		System.out.println(result == null ? "PREDAJA" : result.toString());
	}

}
