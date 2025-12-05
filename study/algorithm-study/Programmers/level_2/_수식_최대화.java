package me.wony.level_2;

import java.util.Deque;
import java.util.LinkedList;

public class _수식_최대화 {

    /**
     * 1. 모든 참가자들에게는 숫자들과 3가지의 연산문자(+, -, *)만으로 이루어진 연산 수식이 전달.
     * 2. 참가자의 미션은 전달받은 수식에 포함된 연산자의 우선순위를 자유롭게 재정의 하여 만들 수 있는 가장 큰 숫자 제출
     * 3. 연산자 우선순위는 동일한게 없어야한다 예로 +,- 일경우 + > - 로 표시
     * 4. 주어진 연산자가 n개라면 n!개의 연산자 조합이 발생
     * 5. 결과값이 음수일경우 절댓값으로 변환
     * 6. expression 길이는 3이상 100이하
     * 7. expression 은 공백, 괄호 없이 오로지 숫자와 연산자로 이루어진 중위표기법으로 구성
     * 8. 피연산자는 0 이상 999 이하의 숫자
     * 9 적어도 1개의 연산자 포함
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(solution("100-200*300-500+20"));
        System.out.println(solution("50*6-3*2"));
        System.out.println(solution("2-990-5+2"));
        System.out.println(solution("*900"));
    }

    private static long solution(String expression) {
        long answer = 0;
        String[][] signArray = new String[][]{{"*","+","-"}, {"*","-","+"}, {"+","*","-"}, {"+","-","*"}, {"-","+","*"},{"-","*","+"}};
        for (String[] signs : signArray) {
            Deque<String> deque = setDeque(expression);
            for (String sign : signs) {
                Deque<String> tempDeque = new LinkedList<>();
                while(!deque.isEmpty()){
                    String s = deque.pollFirst();
                    if(sign.equals(s)) {
                        long valueOne = Long.valueOf(tempDeque.pollLast());
                        long valueTwo = Long.valueOf(deque.pollFirst());
                        s = cal(sign, valueOne, valueTwo);
                    }
                    tempDeque.add(s);
                }
                deque.addAll(tempDeque);
                if(deque.size() == 1) break;
            }
            answer = Math.max(answer,Math.abs(Long.valueOf(deque.poll())));
        }
        return answer;
    }

    private static String cal(String sign, long valueOne, long valueTwo) {
        switch (sign){
            case "*": return String.valueOf(valueOne * valueTwo);
            case "+": return String.valueOf(valueOne + valueTwo);
            default: return String.valueOf(valueOne - valueTwo);
        }
    }

    private static Deque<String> setDeque(String expression) {
        Deque<String> deque = new LinkedList<>();
        int substr = 0;
        for(int i = 0; i < expression.length(); i++){
            char str = expression.charAt(i);
            if(str == '*' || str == '+' || str == '-'){
                deque.add(expression.substring(substr, i));
                deque.add(expression.substring(i, i+1));
                substr = i+1;
            }
        }
        deque.add(expression.substring(substr));
        return deque;
    }

}
