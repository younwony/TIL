package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class a9375_패션왕_신해빈 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        for(int i =0 ; i < n; i++){
            int testCnt = Integer.parseInt(bufferedReader.readLine());
            HashMap<String, Integer> fashion = new HashMap<>();
            for(int j = 0; j < testCnt; j++){
                String[] clothesKind = bufferedReader.readLine().split(" ");
                String kind = clothesKind[1];
                fashion.put(kind, fashion.getOrDefault(kind, 0) + 1);
            }

            int answer = 1;
            for (String key : fashion.keySet()) {
                answer *= fashion.get(key)+1;
            }
            System.out.println(answer-1);
        }
    }
}
