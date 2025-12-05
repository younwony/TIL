package _0_easy;

import java.util.Stack;

public class a20_Valid_Parentheses {

    /**
     * , , , , , s문자만 포함된 문자열이 주어졌을 때 입력 문자열이 유효한지 확인합니다.'('')''{''}''['']'
     *
     * 입력 문자열은 다음과 같은 경우 유효합니다.
     *
     * 열린 괄호는 같은 유형의 괄호로 닫아야 합니다.
     * 열린 괄호는 올바른 순서로 닫아야 합니다.
     * 모든 닫힌 괄호에는 같은 유형의 열린 괄호가 대응됩니다.
     *
     *
     * 예시 1:
     * 입력: s = "()"
     * 출력 : true
     *
     * 예시 2:
     * 입력: s = "()[]{}"
     * 출력 : true
     *
     * 예시 3:
     * 입력: s = "(]"
     * 출력 : false
     *
     * 예시 4:
     * 입력: s = "([])"
     * 출력 : true
     *
     * 제약사항:
     *
     * 1 <= s.length <= 104
     * s괄호로만 구성됩니다 '()[]{}'.
     * @param args
     */

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String strs = "(]";
        System.out.println(isValid(strs));
    }

    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        char[] charArray = s.toCharArray();
        for (char c : charArray) {
            if(c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                switch (c) {
                    case ')' :
                        if(stack.isEmpty() || stack.peek() != '(') {
                            return false;
                        }
                        stack.pop();
                        break;
                    case '}' :
                        if(stack.isEmpty() || stack.peek() != '{') {
                            return false;
                        }
                        stack.pop();
                        break;
                    case ']' :
                        if(stack.isEmpty() || stack.peek() != '[') {
                            return false;
                        }
                        stack.pop();
                        break;
                }
            }
        }

        return stack.isEmpty();
    }
}
