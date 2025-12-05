package me.wony.level_1;

import java.util.Locale;

public class _문자열_내_p와_y의_개수 {

    public static void main(String[] args) {
        System.out.println(solution("pPoooyY"));
        System.out.println(solution("Pyy"));
    }

    private static boolean solution(String s) {
        /* 문자 low 캐이스로 일괄 변경 */
        s = s.toLowerCase();

        int pCnt = 0;
        int yCnt = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(c == 'p') pCnt++;
            if(c == 'y') yCnt++;
        }

        return pCnt == yCnt;
    }
}
