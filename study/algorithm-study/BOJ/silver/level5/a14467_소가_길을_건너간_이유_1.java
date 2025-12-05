package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class a14467_소가_길을_건너간_이유_1 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bufferedReader.readLine());

        HashMap<String, String> cows = new HashMap<>();
        String cow;
        String number;
        int result = 0;

        StringTokenizer stringTokenizer;
        while(N-- > 0){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            cow = stringTokenizer.nextToken();
            number = stringTokenizer.nextToken();

            if(!number.equals(cows.getOrDefault(cow, number))){
                result++;
            }

            cows.put(cow,number);
        }

        System.out.println(result);
    }

}
