package level_2;

import java.util.Stack;

public class _짝지어_제거하기 {
    public static void main(String[] args) {
        System.out.println(solution("baabaa"));
        System.out.println(solution("cdcd"));
    }

    private static int solution(String s){
        Stack<Character> stack =new Stack<>();
        String temp = s;
        for(int i =0; i < s.length(); i++){
            char c = s.charAt(i);
            if(stack.isEmpty()) {
                stack.push(c);
                continue;
            }
            Character peek = stack.peek();
            if(peek == c){
                stack.pop();
                continue;
            }
            stack.push(c);
        }

        return stack.isEmpty() ? 1 : 0;
    }
}
