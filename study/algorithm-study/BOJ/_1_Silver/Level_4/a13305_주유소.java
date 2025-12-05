package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a13305_주유소 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        int[] cities = new int[n];
        int[] distances = new int[n-1];

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for(int i =0 ; i < n-1; i++){
            distances[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for(int i =0 ; i < n; i++){
            cities[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        long sum = 0;
        long min = cities[0];

        for(int i = 0; i < n - 1; i++){
            if(cities[i] < min){
                min = cities[i];
            }
            sum += min * distances[i];
        }

        System.out.println(sum);
    }
}
