package _0_Bronze.Level_1;

import java.util.ArrayList;
import java.util.Scanner;

public class a2684_동전게임 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		
		ArrayList<String> coinList = new ArrayList<>();
		coinList.add("TTT");
		coinList.add("TTH");
		coinList.add("THT");
		coinList.add("THH");
		coinList.add("HTT");
		coinList.add("HTH");
		coinList.add("HHT");
		coinList.add("HHH");
		
		scanner.nextLine();
		
		String inputData;
		
		String isCoin;
		
		int[] resultArray;
		for(int i = 0 ;i < n; i++) {
			inputData = scanner.nextLine();
			resultArray = new int[8];
			for(int j = 0 ; j<= 37; j++){
				isCoin = inputData.substring(j, j+3);
				resultArray[coinList.indexOf(isCoin)]++;
			}
			
			for(int k : resultArray){
				System.out.print(k + " ");
			}
			System.out.println();
		}
		
		scanner.close();
	}

}
