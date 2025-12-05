package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a1049_기타줄 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int n = Integer.parseInt(stringTokenizer.nextToken());
        int m = Integer.parseInt(stringTokenizer.nextToken());

        int packMin = Integer.MAX_VALUE;
        int oneMin = Integer.MAX_VALUE;

        for (int i = 0; i < m; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            packMin = Math.min(packMin,Integer.parseInt(stringTokenizer.nextToken()));
            oneMin = Math.min(oneMin,Integer.parseInt(stringTokenizer.nextToken()));
        }

        int onePack = oneMin * 6;

        int sum = 0;
        if(packMin < onePack){
            sum = n/6 * packMin;
            n %= 6;

            if(n*oneMin < packMin){
                sum += n * oneMin;
            }else{
                sum += packMin;
            }
        }else{
            sum = oneMin * n;
        }

        System.out.println(sum);
    }

}
