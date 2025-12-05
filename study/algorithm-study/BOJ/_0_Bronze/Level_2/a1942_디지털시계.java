package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a1942_디지털시계 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while((line = bufferedReader.readLine()) != null && !"".equals(line)){
            String[] times = line.split(" ");
            String start = times[0].replace(":","");
            String end = times[1].replace(":","");
            int cnt = 0;

            while(!end.equals(start)){
                int time = Integer.parseInt(start);

                if(time % 3 == 0) cnt++;
                
                start = convertTimeToInt(time+1);
            }
            if(Integer.parseInt(end) % 3 == 0) cnt++;
            stringBuilder.append(cnt).append("\n");
        }
        System.out.println(stringBuilder.toString());
    }

    private static String convertTimeToInt(int time) {
        String timeStr = String.valueOf(time);
        timeStr = timeFormat(timeStr,6);
        String second = timeStr.substring(4,6);
        String min = timeStr.substring(2,4);
        String hour = timeStr.substring(0,2);

        if ("60".equals(second)) {
            min = String.valueOf(Integer.parseInt(min) + 1);
            second = "00";
        }
        if ("60".equals(min)) {
            hour = String.valueOf(Integer.parseInt(hour) + 1);
            min = "00";
        }
        if("24".equals(hour)){
            hour = "00";
        }

        return timeFormat(hour,2)+timeFormat(min,2)+timeFormat(second,2);
    }

    private static String timeFormat(String timeStr, int num) {
        for(int i = timeStr.length(); i < num; i++){
            timeStr = "0" + timeStr;
        }
        return timeStr;
    }

    private static String upper(String lowTime, String highTime) {
        if (Integer.parseInt(lowTime) > 59) {
            highTime = String.valueOf(Integer.parseInt(highTime) + 1);
        }
        return highTime;
    }
}
