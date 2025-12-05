package _1_Silver.Level_2;

import java.util.Scanner;
import java.util.Stack;

public class a2504_괄호의값 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String str = scanner.nextLine();

        Stack<Character> stack = new Stack<>();

        char data;

        int sum = 0;
        int temp = 1;
        boolean isData = true;
        for(int i = 0; i < str.length(); i++) {
            data = str.charAt(i);
            if(data == '('){
                stack.push(data);
                temp *= 2;
            }else if(data == ')'){
                if(!stack.empty() && stack.peek() == '('){
                    if(str.charAt(i-1) == '('){
                        sum += temp;
                    }
                    temp /= 2;
                    stack.pop();
                }else if(stack.empty()){
                    isData = false;
                    break;
                }
            }else if(data == '['){
                stack.push(data);
                temp *= 3;
            }else if(data == ']'){
                if(!stack.empty() && stack.peek() == '[') {
                    if (str.charAt(i-1) == '[') {
                        sum += temp;
                    }
                    stack.pop();
                    temp /= 3;
                }else if(stack.empty()){
                    isData = false;
                    break;
                }
            }
        }

        if(stack.size() > 0){isData = false;}
        System.out.println(isData ? sum : 0);
    }
}
