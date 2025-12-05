package level_2;

import java.util.HashMap;
import java.util.HashSet;

public class _뉴스_클러스터링 {

    public static void main(String[] args) {
        System.out.println(solution("FRANCE", "french"));
        System.out.println(solution("handshake", "shake hands"));
        System.out.println(solution("aa1+aa2", "AAAA12"));
        System.out.println(solution("E=M*C^2", "e=m*c^2"));
    }

    static HashSet<String> allSet = new HashSet<>();
    private static int solution(String str1, String str2) {
        str1 = str1.toUpperCase();
        str2 = str2.toUpperCase();

        HashMap<String,Integer> str1Map = setJArray(str1);
        HashMap<String,Integer> str2Map = setJArray(str2);
        double commonStr = 0;
        double allStr = 0;
        for (String s : allSet) {
            Integer orDefault1 = str1Map.getOrDefault(s, 0);
            Integer orDefault2 = str2Map.getOrDefault(s, 0);
            if(orDefault1 != 0 && orDefault2 != 0){
                commonStr += Math.min(str1Map.get(s), str2Map.get(s));
            }
            allStr += Math.max(orDefault1, orDefault2);
        }
        if(commonStr == 0 && allStr == 0) return 65536;
        return (int)((commonStr/allStr)*65536);
    }

    private static HashMap<String,Integer> setJArray(String str) {
        HashMap<String,Integer> strMap = new HashMap<>();
        for(int i = 0; i < str.length()-1; i++){
            String substring = str.substring(i, i + 2);
            boolean matches = substring.matches("[a-zA-Z]*");
            if(!matches) continue;
            strMap.put(substring,strMap.getOrDefault(substring, 0)+1);
            allSet.add(substring);
        }
        return strMap;
    }
}
