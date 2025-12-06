package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class a16435_스네이크버드 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int N = Integer.parseInt(stringTokenizer.nextToken());
        int L = Integer.parseInt(stringTokenizer.nextToken());
        int[] fruits = new int[N];
        stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for (int i = 0; i < fruits.length; i++) {
            fruits[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        Arrays.sort(fruits);

        for (int fruit : fruits) {
            if(fruit <= L){
                L++;
            }else{
                break;
            }
        }

        System.out.println(L);
    }
}
