package me.wony.level_2;

import java.util.Arrays;
import java.util.Comparator;

public class _정렬 {
    public static void main(String[] args) {
        /*System.out.println(solution(new int[]{6,10,2}));
        System.out.println(solution(new int[]{3,30,34,5,9}));
        System.out.println(solution(new int[]{15,151}));
        System.out.println(solution(new int[]{0,0,0,0,10}));*/
        /*System.out.println(solution(new int[]{412,41}));*/
        System.out.println(solution(new int[]{10,10}));
        System.out.println(solution(new int[]{2,20,200}));
        System.out.println(solution(new int[]{0,0,70}));
        System.out.println(solution(new int[]{12,121}));
    }
    private static String solution(int[] numbers) {
        String answer = "";
        String[] numberStr = new String[numbers.length];
        for (int i = 0; i < numberStr.length; i++) {
            numberStr[i] = String.valueOf(numbers[i]);
        }

        Arrays.sort(numberStr, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return (o2+o1).compareTo(o1+o2);
            }
        });

        for (String s : numberStr) {
            answer += s;
        }

        return answer.substring(0, answer.length() - 1).replaceAll("^0*","") + answer.substring(answer.length() - 1);
    }

}
