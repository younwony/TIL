package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a1057_토너먼트 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int N = Integer.parseInt(stringTokenizer.nextToken());
        int kim = Integer.parseInt(stringTokenizer.nextToken());
        int lim = Integer.parseInt(stringTokenizer.nextToken());

        /* 작은 순 으로 변경*/
        if(kim > lim){
            int temp = kim;
            kim = lim;
            lim = temp;
        }

        /* 최대 경기 수 구하기 */
        int max = 0;
        int temp = N;
        while(N > 1){
            N /= 2;
            max++;
        }
        if(temp > Math.pow(2,max)){
            max++;
        }



        /* solved */
        int sum = 0;
        while (true) {
            int now = (int) Math.pow(2,--max);
            /* 둘이 다를 경우 종료 */
            int nowSum = now + sum;
            if(kim <= nowSum && lim > nowSum){
                max++;
                break;
            }

            /* 현재 지점보다 클 경우 now 더하기 */
            if(kim > nowSum){
                sum += now;
            }
        }

        System.out.println(max);
    }
}
