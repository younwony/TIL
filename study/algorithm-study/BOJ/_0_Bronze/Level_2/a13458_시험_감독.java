package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a13458_시험_감독 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(bufferedReader.readLine());
        int[] examRoom = new int[N];
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        for(int i = 0 ; i < examRoom.length; i++) {
            examRoom[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int B = Integer.parseInt(stringTokenizer.nextToken());
        int C = Integer.parseInt(stringTokenizer.nextToken());

        long cnt = 0;

        for (int i = 0; i < examRoom.length; i++) {
            examRoom[i] -= B;
            if(examRoom[i] < 0){
                examRoom[i] = 0;
            }
        }
        cnt += N;

        for (int people : examRoom) {
            cnt += people/C;
            if(people % C > 0){
                cnt++;
            }
        }

        System.out.println(cnt);
    }
}
