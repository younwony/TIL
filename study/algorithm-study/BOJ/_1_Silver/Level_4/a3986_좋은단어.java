package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Stack;

public class a3986_좋은단어 {

	static Stack<Character> stack = new Stack<>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			int n = Integer.valueOf(bufferedReader.readLine());
			
			String inputData;
			int count = 0;
			
			for(int i = 0; i < n ; i++){
				inputData = bufferedReader.readLine();
				for(int j = 0; j < inputData.length(); j++){
					Order(inputData.charAt(j));
				}
				
				if(stack.empty()){
					count++;
				}else{
					stack.clear();
				}
			}
			
			System.out.println(count);
			
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
	
	public static void Order(char ch){
		
		if(!stack.empty()){
			if(stack.peek() == ch){
				stack.pop();
			}else{
				stack.push(ch);
			}
		}else{
			stack.push(ch);
		}
	}
}
