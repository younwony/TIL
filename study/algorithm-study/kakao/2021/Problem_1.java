package Kakao.Problem_2021;

public class Problem_1 {
    public static void main(String[] args) {
        System.out.println(solution("...!@BaT#*..y.abcdefghijklm"));
    }


    // 3 <= new_id <= 15
    // new_id = lower, -, _, .
    // . is non first and last, .. not
    public static String solution(String new_id) {
        String answer = "";
        if(new_id.length() > 15) new_id = new_id.substring(0, 15);
        if(new_id.length() < 3 || new_id.length() > 0){
            StringBuilder new_str = new StringBuilder();
            new_str.append(new_id).append(new_id).append(new_id);
            answer = new_str.substring(0,3);
        }
        answer = new_id.toLowerCase();
        if("".equals(answer)) return "a";
        StringBuilder str = new StringBuilder();
        for(int i = 0 ; i < answer.length(); i++){
            char ch = answer.charAt(i);
            if((i == 0 || i == answer.length() -1) && ch == '.') continue;
            if(ch == '.' && i + 1 < answer.length()){
                if(answer.charAt(i+1) == '.') continue;
            }
            if(isChar(ch)) str.append(ch);
        }
        return str.toString();
    }

    public static boolean isChar(char ch){
        boolean result = false;
        if(ch >= 97 && ch <= 122)result = true;;
        if(ch - '0' >= 0 && ch - '0' <= 9) result = true;
        if(ch == '-' || ch == '_' || ch == '.') result = true;

        return result;
    }
}
