package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;

public class a5397_키로거 {

	static Deque<Character> leftDeque = new ArrayDeque<>();
	static Deque<Character> rightDeque = new ArrayDeque<>();
	static StringBuilder result = new StringBuilder(); 
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		
		try {
			
			int n = Integer.valueOf(bufferedReader.readLine());
			String inputData;
			for(int i = 0; i < n; i++){
				inputData = bufferedReader.readLine();
				for(int j = 0; j < inputData.length(); j++){
					Order(inputData.charAt(j));
				}
				insertData();
				result.append("\n");
			}
			
			System.out.println(result);
			
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
	
	public static void Order(char ch){
		if(ch == '<'){
			if(!leftDeque.isEmpty()){
				rightDeque.offerFirst(leftDeque.pollLast());
			}
		}else if(ch == '>'){
			if(!rightDeque.isEmpty()){
				leftDeque.offer(rightDeque.poll());
			}
		}else if(ch == '-'){
			if(!leftDeque.isEmpty()){
				leftDeque.pollLast();
			}
		}else{
			leftDeque.offer(ch);
		}
	}
	
	public static void insertData(){
		while(!leftDeque.isEmpty()){
			result.append(leftDeque.poll());
		}
		
		while(!rightDeque.isEmpty()){
			result.append(rightDeque.poll());
		}
	}

}
