package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a2852_NBA_농구 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(bufferedReader.readLine());
        StringTokenizer stringTokenizer;

        int a = 0;
        int b = 0;
        int aMin = 0;
        int bMin = 0;
        int min = 0;

        int preMin = 0;

        while(N-- > 0){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            String team = stringTokenizer.nextToken();
            min = getMin(stringTokenizer.nextToken());

            if(a > b){
                aMin += min - preMin;
            }

            if(b > a){
                bMin += min - preMin;
            }

            preMin = min;

            if("1".equals(team)){
                a++;
            }else{
                b++;
            }
        }

        min = 2880;

        if(a > b){
            aMin += min - preMin;
        }

        if(b > a){
            bMin += min - preMin;
        }

        System.out.println(lpad(aMin/60) + ":" + lpad(aMin%60));
        System.out.println(lpad(bMin/60) + ":" + lpad(bMin%60));
    }

    private static String lpad(int min) {

        String time = String.valueOf(min);

        for(int i = time.length(); i < 2; i++){
            time = "0" + time;
        }

        return time;
    }

    private static int getMin(String nextToken) {

        String[] times = nextToken.split(":");

        int hour = Integer.parseInt(times[0]);
        int min = Integer.parseInt(times[1]);
        return hour * 60 + min;
    }
}
