package me.wony.level_2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;

public class _튜플 {
    /**
     * 1. 원소 중복 가능
     * 2. 원소에 정해진 순서가있다 순서가 서로다르면 튜플
     * 3. 튜플의 원소 개수는 유한하다.
     * 4. 5 <= s <= 1_000_000
     * 5. s는 숫자 + '{' + '}' +',' 로만 구성
     * 6. 0으로 시작하지 않는다.
     * 7. s가 표현하는 튜플의 원소는 1이상 100_000 이하 자연수
     * 8. return 배열의 길이가 1이상 500이하이다.
     */
    public static void main(String[] args) {
        System.out.println(solution("{{2},{2,1},{2,1,3},{2,1,3,4}}"));
        System.out.println(solution("{{1,2,3},{2,1},{1,2,4,3},{2}}"));
        System.out.println(solution("{{20,111},{111}}"));
        System.out.println(solution("{{123}}"));
        System.out.println(solution("{{4,2,3},{3},{2,3,4,1},{2,3}}"));
    }

    private static int[] solution(String s) {
        int[] answer = {};
        s = s.substring(2, s.length()-2);
        String[] split = s.split("},\\{");
        Arrays.sort(split, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });
        answer = new int[split.length];
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (int i = 0; i < split.length; i++) {
            String[] str = split[i].split(",");
            for (String s1 : str) {
                linkedHashSet.add(Integer.valueOf(s1));
            }
        }
        int index = 0;
        for (Object o : linkedHashSet) {
            answer[index++] = (int)o;
        }
        return answer;
    }
}
