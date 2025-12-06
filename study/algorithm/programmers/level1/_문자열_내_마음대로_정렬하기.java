package level_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class _문자열_내_마음대로_정렬하기 {

    public static void main(String[] args) {
        solution(new String[]{"sun","bed","car"}, 1);
        solution(new String[]{"abce","abcd","cdx"}, 2);
    }

    private static String[] solution(String[] strings, int n) {
        String[] answer = {};

        /* 리스트 선언 후 입력값 저장*/
        ArrayList<String> stringArrayList = new ArrayList<String>();
        for (String s : strings) {
            stringArrayList.add(s);
        }

        /* 정렬 조건 생성 */
        Collections.sort(stringArrayList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                /* 체크하는 char가 같을 경우 문자열 사전순 출력 */
                if(o1.charAt(n) == o2.charAt(n)) return o1.compareTo(o2);
                return Character.compare(o1.charAt(n), o2.charAt(n));
            }
        });

        /* 다시 Array로 변환 */
        answer = stringArrayList.toArray(new String[strings.length]);

        return answer;
    }

    /**
     * String의  Comparator가 생각이 나지않아 List의 Comparator를 사용했다.
     */
    private static String[] solution2(String[] strings, int n) {
        Arrays.sort(strings, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if(o1.charAt(n) == o2.charAt(n)) return o1.compareTo(o2);
                return Character.compare(o1.charAt(n), o2.charAt(n));
            }
        });

        return strings;
    }

}
