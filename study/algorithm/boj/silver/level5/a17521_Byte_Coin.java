package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a17521_Byte_Coin {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int n = Integer.parseInt(stringTokenizer.nextToken());
        long w = Integer.parseInt(stringTokenizer.nextToken());

        long[] coins = new long[n];

        for(int i = 0; i < n; i++){
            coins[i] = Integer.parseInt(bufferedReader.readLine());
        }

        long coinCnt = 0;

        for(int i = 0; i < n - 1; i++){
            long coin = coins[i];
            long next = coins[i+1];

            if(coin < next){
                coinCnt += w/coin;
                w %= coin;
            }

            if(coin > next){
                w += coin * coinCnt;
                coinCnt = 0;
            }
        }

        if(coinCnt > 0){
            w += coins[n-1] * coinCnt;
        }

        System.out.println(w);
    }
}
