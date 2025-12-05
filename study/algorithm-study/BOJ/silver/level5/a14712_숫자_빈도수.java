package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a14712_숫자_빈도수 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int n = Integer.parseInt(stringTokenizer.nextToken());
        int d = Integer.parseInt(stringTokenizer.nextToken());

        int cnt = 0;
        for(int i = 1; i <= n; i++){
            cnt += isDigit(i,d);
        }
        System.out.println(cnt);
    }

    private static int isDigit(int i, int d) {
        int cnt = 0;
        while(i > 0){
            int mod = i % 10;
            if(mod == d){
                cnt++;
            }
            i /= 10;
        }
        return cnt;
    }
}
