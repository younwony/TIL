package level_2;

import java.util.HashSet;
import java.util.Set;

public class _영어_끝말잇기 {

    public static void main(String[] args) {
        System.out.println(solution(3, new String[]{"tank", "kick", "know", "wheel", "land", "dream", "mother", "robot", "tank"}));
        System.out.println(solution(5, new String[]{"hello", "observe", "effect", "take", "either", "recognize", "encourage", "ensure", "establish", "hang", "gather", "refer", "reference", "estimate", "executive"}));
        System.out.println(solution(2, new String[]{"hello", "one", "even", "never", "now", "world", "draw"}));
    }

    private static int[] solution(int n, String[] words) {
        int[] answer = new int[2];
        Set<String> wordSet = new HashSet<>();
        char start = words[0].charAt(0);
        for (String word : words) {
            if(word.charAt(0) != start) break;
            if(wordSet.contains(word)) break;
            wordSet.add(word);
            start = word.charAt(word.length()-1);
            answer[0]++;
            answer[1]++;
        }
        answer[0] = answer[0]%n + 1;
        answer[1] = answer[1]/n + 1;
        if(words.length == wordSet.size()) {
            answer[0] = 0;
            answer[1] = 0;
        }

        return answer;
    }
}
