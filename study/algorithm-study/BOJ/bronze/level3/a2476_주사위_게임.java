package _0_Bronze.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a2476_주사위_게임 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());
        int max = 0;
        while(n-- > 0){
            max = Math.max(max, reward(bufferedReader.readLine()));
        }

        System.out.println(max);
    }

    private static int reward(String dice) {
        StringTokenizer stringTokenizer = new StringTokenizer(dice);
        int a = Integer.parseInt(stringTokenizer.nextToken());
        int b = Integer.parseInt(stringTokenizer.nextToken());
        int c = Integer.parseInt(stringTokenizer.nextToken());

        int reward = 0;

        if(a == b && b == c){
            reward = 10_000 + a * 1_000;
        }else if(a != b && a != c && b != c){
            reward = Math.max(a, Math.max(b, c)) * 100;
        }else{
            if(a == b){
                reward = 1_000 + a * 100;
            }else{
                reward = 1_000 + c * 100;
            }
        }

        return reward;
    }
}
