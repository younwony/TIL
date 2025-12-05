package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a20438_출석체크 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int N = Integer.parseInt(stringTokenizer.nextToken()); // 학생 수
        int K = Integer.parseInt(stringTokenizer.nextToken()); // 졸고 있는 학생 수
        int Q= Integer.parseInt(stringTokenizer.nextToken()); // 출석 코드를 보낼 학생 수
        int M= Integer.parseInt(stringTokenizer.nextToken()); // 주어질 구간

        int[] students = new int[N+3];
        stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for (int i = 0; i < K; i++) {
            int sleep = Integer.parseInt(stringTokenizer.nextToken());
            students[sleep] = -1;
        }

        stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for (int i = 0; i < Q; i++) {
            int sendNumber = Integer.parseInt(stringTokenizer.nextToken());
            if(students[sendNumber] == -1){
                continue;
            }
            for(int j = sendNumber; j < students.length; j += sendNumber){
                if(students[j] == 0){
                    students[j] = 1;
                }
            }
        }

        int[] dp = new int[N+3];
        for (int i = 3; i < dp.length; i++) {
            dp[i] = dp[i-1] + (students[i] == 1 ? 0 : 1);
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < M; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            int start = Integer.parseInt(stringTokenizer.nextToken());
            int end = Integer.parseInt(stringTokenizer.nextToken());
            result
                    .append(dp[end] - dp[start - 1])
                    .append("\n");
        }

        System.out.println(result.toString());

    }
}
