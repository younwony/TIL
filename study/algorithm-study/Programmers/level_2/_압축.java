package me.wony.level_2;

import java.util.ArrayList;

public class _압축 {

    public static void main(String[] args) {
//        System.out.println(solution("KAKAO"));
//        System.out.println(solution("TOBEORNOTTOBEORTOBEORNOT"));
        System.out.println(solution("ABABABABABABABAB"));
    }

    private static int[] solution(String msg) {
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<Integer> answers = new ArrayList<>();
        for(int i = 65; i < 91; i++){
            strings.add(String.valueOf((char) i));
        }
        String nowStr = "";
        for(int i = 0; i < msg.length(); i++){
            nowStr += msg.charAt(i);
            int index = strings.indexOf(nowStr);
            if(index > -1){
                if(nowStr.length() > 1) answers.remove(answers.size()-1);
                answers.add(index);
                continue;
            }
            strings.add(nowStr);
            nowStr = "";
            i--;
        }
        int[] answer = new int[answers.size()];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = answers.get(i)+1;
        }
        return answer;
    }
}
