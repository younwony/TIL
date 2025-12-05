package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class a1946_신입사원 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			
			int testCase = Integer.valueOf(bufferedReader.readLine());
			StringBuilder result = new StringBuilder();
			for(int i = 0; i < testCase; i++){
				int perSonCount = Integer.valueOf(bufferedReader.readLine());
				int[] perSonArray = new int[perSonCount + 1];
				
				for(int j = 0 ; j < perSonCount; j++){
					String[] personRank = bufferedReader.readLine().split(" ");
					perSonArray[Integer.valueOf(personRank[0])] = Integer.valueOf(personRank[1]);
				}
				
				int nowTopRank = perSonArray[1];
				
				int count = 1;
				
				for(int j = 2; j < perSonArray.length; j++){
					if(perSonArray[j] < nowTopRank){
						count++;
						nowTopRank = perSonArray[j];
					}
				}
				
				result.append(count + "\n");
				
			}
			
			System.out.println(result.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(bufferedReader != null){bufferedReader.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
}
