package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a1173_운동 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int N = Integer.parseInt(stringTokenizer.nextToken());
        int m = Integer.parseInt(stringTokenizer.nextToken());
        int M = Integer.parseInt(stringTokenizer.nextToken());
        int T = Integer.parseInt(stringTokenizer.nextToken());
        int R = Integer.parseInt(stringTokenizer.nextToken());

        if(M - m < T) {
            System.out.println(-1);
        }else{
            int min = m;
            int time = 0;
            int cnt = 0;
            while(true){

                if(cnt == N){
                    break;
                }

                time++;

                if(m + T <= M){
                    cnt++;
                    m += T;
                    continue;
                }

                m -= R;
                m = Math.max(min,m);

            }

            System.out.println(time);
        }
    }
}
