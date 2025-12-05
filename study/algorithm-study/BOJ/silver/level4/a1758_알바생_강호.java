package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class a1758_알바생_강호 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(bufferedReader.readLine());

        int[] tips = new int[N];

        for (int i = 0; i < tips.length; i++) {
            tips[i] = Integer.parseInt(bufferedReader.readLine());
        }

        Arrays.sort(tips);

        int j = 1;
        long cnt = 0;
        for(int i = N - 1; i >= 0; i--){
            int tip = tips[i] - j++ + 1;
            if(tip <= 0){
                break;
            }
            cnt += tip;
        }

        System.out.println(cnt);

    }
}
