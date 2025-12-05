package _0_Bronze.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a1547_공 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());

        boolean[] balls = new boolean[4];
        balls[1] = true;

        for (int i = 0; i < n ; i++){
            StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

            int a = Integer.parseInt(stringTokenizer.nextToken());
            int b = Integer.parseInt(stringTokenizer.nextToken());

            boolean temp = balls[a];
            balls[a] = balls[b];
            balls[b] = temp;
        }

        for (int i = 0; i < balls.length; i++) {
            if(balls[i]){
                System.out.println(i);
            }
        }
    }
}
