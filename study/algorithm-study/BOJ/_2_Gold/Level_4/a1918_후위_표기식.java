package _2_Gold.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Stack;

// A*B+C => AB*C+

// (B+C)*A => BC+A*

// A*(B+C) => ABC+*

// A+B+C*D => AB+CD*+

// A*D+B+C*E => AD*B+CE*+
public class a1918_후위_표기식 {
    public static void main(String[] args) throws IOException {
        HashMap<Character, Integer> oper = new HashMap<>();
        /* 연산자 우선순위 부여 */
        oper.put('*', 2);
        oper.put('/', 2);
        oper.put('+', 1);
        oper.put('-', 1);
        oper.put('(', 0);
        oper.put(')', 0);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input = bufferedReader.readLine();
        /* 연산자가 저장될 스택 */
        Stack<Character> stack = new Stack<>();

        StringBuilder result = new StringBuilder();
        for(int i = 0 ; i < input.length(); i++){
            char c = input.charAt(i);
            switch (c){
                case '*' :
                case '/' :
                case '+' :
                case '-' :
                    /* 연산자일 경우, 연산자 스택에 현재 우선순위보다 높은 값들을 결과값에 붙인다. */
                    while(!stack.isEmpty() && oper.get(stack.peek()) >= oper.get(c)){
                        result.append(stack.pop());
                    }
                    /* 연산자 스택에 추가 */
                    stack.push(c);
                    break;
                case '(' :
                    /* 여는 괄호 연산자 스택에 추가 */
                    stack.push(c);
                    break;
                case ')' :
                    /* 닫는 괄호 일 경우 여는 괄호가 있을 때까지 연산자 스택에서 결과값에 붙인다. */
                    while (!stack.isEmpty() && stack.peek() != '('){
                        result.append(stack.pop());
                    }
                    /* 여는 괄호 꺼내기 */
                    stack.pop();
                    break;
                default:
                    /* 그외의 모든 문자들은 결과값에 붙인다. */
                    result.append(c);
            }
        }

        while (!stack.isEmpty()){
            result.append(stack.pop());
        }

        System.out.println(result.toString());
    }

}
