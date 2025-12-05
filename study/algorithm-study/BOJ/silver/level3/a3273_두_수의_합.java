package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a3273_두_수의_합 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int x = Integer.parseInt(bufferedReader.readLine());

        int[] numbers = new int[2000001];

        while(stringTokenizer.hasMoreTokens()){
            numbers[Integer.parseInt(stringTokenizer.nextToken())]++;
        }


        int result = 0;

        for(int i = 1; i <= x/2; i++){
            if(numbers[i] > 0 && numbers[x - i] > 0){
                result++;
            }
        }

        if(x % 2 == 0 && numbers[x/2] > 0){
            result--;
        }

        System.out.println(result);
    }
}
