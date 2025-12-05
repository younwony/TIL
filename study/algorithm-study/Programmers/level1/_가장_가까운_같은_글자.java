package level_1;

import java.util.HashMap;

public class _가장_가까운_같은_글자 {
    public static void main(String[] args) {
        int[] bananas = solution("foobar");
        for (int banana : bananas) {
            System.out.print(banana + " ");
        }
    }

    public static int[] solution(String s) {
        char[] charArray = s.toCharArray();
        int[] answer = new int[charArray.length];
        HashMap<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            answer[i] = map.containsKey(c) ? i - map.get(c) : -1;
            map.put(c, i);
        }
        return answer;
    }
}
