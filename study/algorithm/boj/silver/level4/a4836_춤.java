package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class a4836_춤 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null && !"".equals(line)){
            stringBuilder
                    .append("form ")
                    .append(isForm(line))
                    .append("\n");
        }
        System.out.println(stringBuilder.toString());
    }

    private static String isForm(String line) {

        StringBuilder stringBuilder = new StringBuilder();

        String[] dance = line.split(" ");
        boolean[] isCondition = new boolean[5];
        int cnt = 0;
        ArrayList<Integer> conditionOneList = new ArrayList<>();
        if(isConditionTwo(line)){
            isCondition[1] = true;
            cnt++;
        }
        if(isConditionThree(line)){
            isCondition[2] = true;
            cnt++;
        }
        if(isConditionFour(dance)){
            isCondition[3] = true;
            cnt++;
        }
        if(isConditionFive(line)){
            isCondition[4] = true;
            cnt++;
        }
        if(isConditionOne(dance, conditionOneList)){
            isCondition[0] = true;
            for (Integer errorIndex : conditionOneList) {
                dance[errorIndex] = dance[errorIndex].toUpperCase(Locale.ROOT);
            }
            StringBuilder reLine = new StringBuilder();
            for (String s : dance) {
                reLine.append(s)
                        .append(" ");
            }
            reLine.delete(reLine.length()-1,reLine.length());
            line = reLine.toString();
            cnt++;
        }

        if(cnt == 0){
            stringBuilder.append("ok");
        }

        if(cnt == 1){
            stringBuilder.append("error ");
            for (int i = 0; i < isCondition.length; i++) {
                if(isCondition[i]){
                    stringBuilder
                            .append(i+1);
                }
            }
        }

        if(cnt > 1){
            stringBuilder.append("errors ");
            for (int i = 0; i < isCondition.length; i++) {
                if(isCondition[i]){
                    stringBuilder
                            .append(i+1);
                    cnt--;
                    if(cnt == 1){
                        stringBuilder
                                .append(" and ");
                    }
                    if(cnt > 1){
                        stringBuilder
                                .append(", ");
                    }
                }
            }
        }

        return stringBuilder
                .append(": ")
                .append(line)
                .toString();
    }

    private static boolean isConditionOne(String[] dance, ArrayList<Integer> conditionOneList) {
        for (int i = 0; i < dance.length; i++) {
            String s = dance[i];
            if("dip".equals(s)){
                boolean isDip = false;
                for (int j = i+1; j < dance.length; j++) {
                    if("twirl".equals(dance[j])){
                        isDip = true;
                    }
                }

                if(!isDip){
                    if(i > 0 && "jiggle".equals(dance[i-1])){
                        isDip = true;
                    }
                    if(i > 1 && "jiggle".equals(dance[i-2])){
                        isDip = true;
                    }
                }
                if(!isDip){
                    conditionOneList.add(i);
                }
            }
        }

        return !conditionOneList.isEmpty();
    }

    private static boolean isConditionTwo(String dance) {
        return !dance.endsWith("clap stomp clap");
    }

    private static boolean isConditionThree(String dance) {
        if(dance.indexOf("twirl") > -1){
            return dance.indexOf("hop") == -1;
        }
        return false;
    }

    private static boolean isConditionFour(String[] dance) {
        return "jiggle".equals(dance[0]);
    }

    private static boolean isConditionFive(String dance) {
        return dance.indexOf("dip") == -1;
    }
}
