package level_1;

import java.util.regex.Pattern;

public class _문자열_다루기_기본 {
    public static void main(String[] args) {
        System.out.println(solution("a234"));
        System.out.println(solution("1234"));
    }
    private static boolean solution(String s) {
        /*정규식 숫자가 4개또는 6개*/
        return Pattern.matches("^[\\d]{4}|{6}$", s);
    }
}
