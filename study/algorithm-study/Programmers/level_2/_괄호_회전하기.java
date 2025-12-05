package level_2;

import java.util.Stack;

public class _괄호_회전하기 {

    public static void main(String[] args) {
        System.out.println(solution("[](){}"));
        System.out.println(solution("}]()[{"));
    }

    private static int solution(String s) {
        int answer = 0;
        for(int i = 0; i < s.length(); i++){
            if(isRight(s)) answer++;
            s = rocateStr(s);
        }
        return answer;
    }

    private static String rocateStr(String s) {
        return s.substring(1) + s.charAt(0);
    }

    private static boolean isRight(String s) {
        Stack<Character> stack = new Stack<>();
        for(int i = 0; i < s.length(); i++){
            char input = s.charAt(i);
            if(input == '[' || input == '{' || input == '(' || stack.isEmpty()){
                stack.push(input);
                continue;
            }
            Character peek = stack.peek();
            if(peek == '[' && input == ']'){
                stack.pop();
                continue;
            }
            if(peek == '{' && input == '}'){
                stack.pop();
                continue;
            }
            if(peek == '(' && input == ')'){
                stack.pop();
                continue;
            }
            stack.push(input);
        }
        return stack.isEmpty();
    }
}
