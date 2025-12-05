package _2_Gold.Level_4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Stack;

public class a9935_문자열폭발 {
	/**
	 *	@author : wony
	 *	@date : 2020-07-20
	 *  @Desc : 
	 *  
	 *   1. 입력받을 String 생성
	 *   2. char[] 에 String.charAt(index) 삽입하며 로직 수행
	 *   3. InputString의 index를 위치하는 i와 inputData[]의 입력 받을 index를 위치하는 index를 같이 사용
	 *   4. Boom String은 덮어 씌우면서 O(n)의 시간복잡도를 가진다.
	 *   5. String.valuof(char[], start, end) 사용
	 */

	static char[] inputData; 
	static char[] boomData; 
	static String inputString; 
	
	public static void main(String[] args) {
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			inputString = bufferedReader.readLine();
			inputData = new char[inputString.length()];
			boomData = bufferedReader.readLine().toCharArray();
			
			int index = 0;
			
			for(int i = 0; i < inputString.length(); i++){
				inputData[index] = inputString.charAt(i);
				if(inputData[index] == boomData[boomData.length - 1] && index >= boomData.length - 1){
					if(isBoom(index)){
						index -= boomData.length;
					}
				}
				index++;
			}
			
			String result = String.valueOf(inputData, 0, index);
			
			System.out.println(result.length() > 0 ? result : "FRULA");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if(bufferedReader != null){bufferedReader.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	
	public static boolean isBoom(int index){
		for(int i = 0; i < boomData.length; i++){
			if(boomData[i] != inputData[index - boomData.length + 1 + i]){
				return false;
			}
		}
		
		return true;
	}
}
