package _0_Bronze.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a9094_수학적_호기심 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(bufferedReader.readLine());
        StringBuilder stringBuilder = new StringBuilder();
        StringTokenizer stringTokenizer;
        while(T-- > 0){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            int n = Integer.parseInt(stringTokenizer.nextToken());
            int m = Integer.parseInt(stringTokenizer.nextToken());
            stringBuilder
                    .append(solved(n,m))
                    .append("\n");
        }
        System.out.println(stringBuilder.toString());
    }

    private static int solved(int n, int m) {
        int cnt = 0;
        for(int i = 1; i < n; i++){
            for(int j = i + 1; j < n; j++){
                if((i*i + j*j + m)%(i*j) == 0){
                    cnt++;
                }
            }
        }
        return cnt;
    }
}
