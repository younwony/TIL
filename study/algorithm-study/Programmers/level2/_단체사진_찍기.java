package level_2;

import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;

public class _단체사진_찍기 {
    /**
     * 1. 네오 프로도 나란히 서기
     * 2. 라이언은 튜브에게서 적어도 세칸이상 떨어지기
     * 3. 원하는 조건을 만족하면서 직을 수 있는 모둔 경우의 수
     *
     * 1 <= n <= 100
     * data 의 원소는 다섯글자로 구성
     * 1) 첫번쨰 글자와 세번째 글자는 다음 8개중 하나 {A, C, F, J, M, N, R, T}
     * 2) 첫 번째 글자는 조건을 제시한 프렌즈, 세번째는 대상
     * 3) 첫 번쨰와 세번째는 항상 다르다
     * 4) 두 번쨰 글자는 항상 ~이다
     * 5) 네 번째 글자는 3개중 하나 {=, <, >} 각각 같은, 미만, 초과
     * 6) 다섯 번째 글자는 0이상 6이하의 정수의 문자형이며 조건에 제시되는 간격을 의미한다.
     */


    public static void main(String[] args) {
        System.out.println(solution(2, new String[]{"N~F=0", "R~T>2"}));
        System.out.println(solution(2, new String[]{"M~C<2", "C~M>1", "R~T>2"}));
    }

    static HashSet<String> allSet = new HashSet<>();
    static String[] friends = new String[]{"A","C","F","J","M","N","R","T"};
    static boolean[] visited = new boolean[8];
    private static int solution(int n, String[] data) {
        int anser = 0;
        for (int i = 0; i < friends.length; i++) {
            visited[i] = true;
            dfs(1, friends[i]);
            visited[i] = false;
        }

        for (String setFriend : allSet) {
            if(isOk(data, setFriend)) anser++;
        }

        return anser++;
    }

    private static boolean isOk(String[] data, String setFriend) {
        for (int i = 0; i < data.length; i++) {
            String datum = data[i];
            char friend = datum.charAt(0);
            char target = datum.charAt(2);
            char sign = datum.charAt(3);
            int interval = datum.charAt(4) - '0';
            int friendIndex = setFriend.indexOf(friend);
            int targetIndex = setFriend.indexOf(target);
            int intervalFriend = Math.abs(friendIndex - targetIndex)-1;
            if(sign == '='){
                if(intervalFriend != interval) return false;
            }
            if(sign == '<'){
                if(!(intervalFriend < interval)) return false;
            }
            if(sign == '>'){
                if(!(intervalFriend > interval)) return false;
            }
        }
        return true;
    }

    /* 시간 초과 */
    private static int solution2(int n, String[] data) {
        for (int i = 0; i < friends.length; i++) {
            visited[i] = true;
            dfs(1, friends[i]);
            visited[i] = false;
        }

        Pattern[] patterns = new Pattern[data.length];
        Pattern[] patternsReverse = new Pattern[data.length];
        for (int i = 0; i < data.length; i++) {
            String datum = data[i];
            char friend = datum.charAt(0);
            char target = datum.charAt(2);
            char sign = datum.charAt(3);
            int interval = datum.charAt(4) - '0';
            String intervalStr = "";
            if(sign == '=') intervalStr= "{" + interval + "}";
            if(sign == '<') intervalStr= "{0," + (interval-1) + "}";
            if(sign == '>') intervalStr= "{" + (interval+1) + ",}";
            String regex = "^.*("+friend+"."+intervalStr+target+").*$";
            String regexReverse = "^.*("+target+"."+intervalStr+friend+").*$";
            patterns[i] = Pattern.compile(regex);
            patternsReverse[i] = Pattern.compile(regexReverse);
        }

        for (int i = 0; i < patterns.length; i++) {
            Pattern pattern = patterns[i];
            Pattern patternReverse = patternsReverse[i];
            Iterator<String> iterator = allSet.iterator();
            while(iterator.hasNext()){
                String next = iterator.next();
                if (pattern.matcher(next).matches() || patternReverse.matcher(next).matches()) {
                    continue;
                }
                iterator.remove();
            }
        }

        return allSet.size();
    }

    private static void dfs(int depth, String friend) {
        if(depth == 8){
            allSet.add(friend);
            return;
        }
        for (int i = 0; i < friends.length; i++) {
            if(visited[i]) continue;
            visited[i] = true;
            dfs(depth+1, friend + friends[i]);
            visited[i] = false;
        }
    }
}
