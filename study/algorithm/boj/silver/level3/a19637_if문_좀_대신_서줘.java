package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class a19637_if문_좀_대신_서줘 {
    static ArrayList<Style> list = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int n = Integer.parseInt(stringTokenizer.nextToken());
        int m = Integer.parseInt(stringTokenizer.nextToken());

        String name;
        int power = 0;
        for(int i =0 ; i < n ; i++){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            name = stringTokenizer.nextToken();
            power = Integer.parseInt(stringTokenizer.nextToken());
            list.add(new Style(name,power));
        }


        StringBuilder result = new StringBuilder();
        for(int i = 0; i < m; i++){
            int score = Integer.parseInt(bufferedReader.readLine());
            result
                    .append(binary(score))
                    .append("\n");
        }

        System.out.println(result.toString());


    }

    private static String binary(int score) {
        int left = 0;
        int right = list.size()-1;
        int middle;
        while(left <= right){
            middle = (left+right)/2;
            if(score > list.get(middle).power){
                left = middle + 1;
            }else{
                right = middle - 1;
            }
        }
        return list.get(right+1).name;
    }


    static class Style{
        String name;
        int power;

        public Style(String name, int power) {
            this.name = name;
            this.power = power;
        }
    }
}
