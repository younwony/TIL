package level_2;

import java.util.ArrayList;
import java.util.List;

public class _모음_사전 {
    public static void main(String[] args) {
        System.out.println(solution("AAAAE"));
        System.out.println(solution("AAAE"));
        System.out.println(solution("I"));
        System.out.println(solution("EIO"));
    }

    public static List<String> strs = new ArrayList<>();
    public static Character[] chars = {'A', 'E', 'I', 'O', 'U'};

    public static int solution(String word) {
        int answer = 0;

        dfs("", 0);

        return strs.indexOf(word) + 1;
    }

    public static void dfs(String str, int index){
        if(index >= chars.length){
            return;
        }

        for(int i = 0; i < chars.length; i++){
            String nowStr  = str + chars[i];
            strs.add(nowStr);
            dfs(nowStr, index + 1);
        }
    }
}