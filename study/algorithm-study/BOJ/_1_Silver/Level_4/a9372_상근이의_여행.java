package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a9372_상근이의_여행 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int T = Integer.parseInt(bufferedReader.readLine());

        StringTokenizer stringTokenizer;

        int N;
        int M;

        StringBuilder stringBuilder = new StringBuilder();

        while(T-- > 0){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            N = Integer.parseInt(stringTokenizer.nextToken());
            M = Integer.parseInt(stringTokenizer.nextToken());
            while(M-- > 0){
                bufferedReader.readLine();
            }
            stringBuilder
                    .append(N-1)
                    .append("\n");
        }

        System.out.println(stringBuilder.toString());
    }
}
