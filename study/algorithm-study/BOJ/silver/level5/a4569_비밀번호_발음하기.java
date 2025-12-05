package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a4569_비밀번호_발음하기 {
    static char[] collections = {'a', 'e', 'i', 'o', 'u'};

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringBuilder result = new StringBuilder();
        String line;
        while (!"end".equals(line = bufferedReader.readLine())) {
            result
                    .append("<")
                    .append(line)
                    .append("> ")
                    .append("is ");
            if (isNonPwd(line)) {
                result
                        .append("not ");
            }
            result
                    .append("acceptable.")
                    .append("\n");
        }
        System.out.println(result.toString());
    }

    private static boolean isNonPwd(String line) {
        return isNonConditionOne(line) || isNonConditionTwo(line) || isNonConditionThree(line);
    }

    private static boolean isNonConditionThree(String line) {
        for (char collection : collections) {
            if (line.indexOf(collection) > -1) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNonConditionTwo(String line) {

        for (int i = 0; i < line.length() - 2; i++) {
            char now = line.charAt(i);
            char next1 = line.charAt(i+1);
            char next2 = line.charAt(i+2);
            if(isCondition(now) == isCondition(next1) && isCondition(next1) == isCondition(next2)){
                return true;
            }
        }

        return false;
    }

    private static boolean isCondition(char now) {
        for (char collection : collections) {
            if(collection == now){
                return true;
            }
        }
        return false;
    }

    private static boolean isNonConditionOne(String line) {
        for (int i = 0; i < line.length() - 1; i++) {
            char now = line.charAt(i);
            char next = line.charAt(i+1);
            if(now == 'e' || now == 'o'){
                continue;
            }
            if(now == next){
                return true;
            }
        }

        return false;
    }
}
