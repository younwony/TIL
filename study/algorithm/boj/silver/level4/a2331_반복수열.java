package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

public class a2331_반복수열 {

    public static int P;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int a = Integer.parseInt(stringTokenizer.nextToken());
        P = Integer.parseInt(stringTokenizer.nextToken());

        ArrayList<Integer> list = new ArrayList<>();

        list.add(a);

        int next = a;

        while(true){
            next = fnc(next);

            if(list.contains(next)){
                break;
            }

            list.add(next);
        }

        System.out.println(list.indexOf(next));
    }

    private static int fnc(int next) {
        int sum = 0;
        while(next > 0){
            sum += Math.pow(next%10,P);
            next /= 10;
        }
        return sum;
    }
}
