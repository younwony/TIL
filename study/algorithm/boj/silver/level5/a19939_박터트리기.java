package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a19939_박터트리기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int n = Integer.parseInt(stringTokenizer.nextToken());
        int k = Integer.parseInt(stringTokenizer.nextToken());

        int minSum = (k*(k+1))/2;

        if(n < minSum){
            System.out.println(-1);
        }else if((n - minSum) % k == 0){
            System.out.println(k-1);
        }else{
            System.out.println(k);
        }
    }
}
