package _1_Silver.Level_4;

import java.util.Scanner;
import java.util.Stack;

public class a4949_균형잡힌세상 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		String str;
		
		boolean isgun = true;
		Stack<String> stack = new Stack<>();
		while(!".".equals(str = scanner.nextLine())){
			isgun = true;
			stack.clear();
			
			for(int i = 0; i < str.length(); i++){
				if(str.charAt(i) == '('){
					stack.push("(");
				}
				if(str.charAt(i) == ')'){
					if(stack.isEmpty() || !"(".equals(stack.peek())){isgun = false; break;}
					stack.pop();
				}
				if(str.charAt(i) == '['){
					stack.push("[");
				}
				if(str.charAt(i) == ']'){
					if(stack.isEmpty() || !"[".equals(stack.peek())){isgun = false; break;}
					stack.pop();
				}
			}
			
			if(stack.isEmpty() && isgun){
				System.out.println("yes");
			}else{
				System.out.println("no");
			}
		}
		
		scanner.close();
	}	

}
