package me.wony.level_2;

import java.util.Stack;

public class _올바른_괄호 {
    public static void main(String[] args) {
        System.out.println(solution("()()"));
    }
    private static boolean solution(String s) {
        Stack<Character> stack = new Stack<>();
        for(int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if(c == '(')
                stack.push(c);
            else
                if(!stack.isEmpty() && stack.peek() == '(')
                    stack.pop();
                else
                    stack.push(c);
        }

        return stack.isEmpty();
    }
}
