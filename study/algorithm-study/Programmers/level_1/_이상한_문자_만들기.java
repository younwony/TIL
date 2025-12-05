package level_1;

import java.util.Locale;

public class _이상한_문자_만들기 {
    public static void main(String[] args) {
        System.out.println(solution("try hello world  "));
    }

    private static String solution(String s) {
        StringBuilder answer = new StringBuilder();
        /**
         * 문제 핵심인 히든케이스, 문자열 뒤에 공백이 있을경우 공백도 같이 처리
         *
         *  split() 을 썻을 경우 문자 뒤의 공백들은 제거가된다.
         *  따라서 spilt(, -1)로 limit 변수에 음수를 두개 되면 모든 공백들이 배열에 포함된다.
         *
         *  * limit 인자는 split 할 배열의 크기를 결정하는 파라미터로 음수일경우 공백 포함해서 모두 배열에 포함되게 한다.
         *  예로 "sss   " 일떄 split("",2) 할 경우 [s,ss   ] 로 된다.
         *
         * */
        String[] split = s.split(" ", -1);
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            for(int j = 0; j < str.length(); j++) {
                String sChar = str.charAt(j) + "";
                answer.append(j % 2 == 0 ? sChar.toUpperCase() : sChar.toLowerCase());
            }
            if(i < split.length -1)
                answer.append(" ");

        }
        return answer.toString();
    }
}
