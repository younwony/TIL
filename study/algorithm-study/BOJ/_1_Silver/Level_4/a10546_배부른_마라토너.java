package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class a10546_배부른_마라토너 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());

        Map<String, Integer> map = new HashMap<>();
        for(int i = 0; i < n; i++){
            String person = bufferedReader.readLine();
            map.put(person, map.getOrDefault(person,0) + 1);
        }

        String answer = "";
        for(int i = 0; i < n-1; i++){
            String person = bufferedReader.readLine();
            int cnt = map.get(person);
            if(cnt == 1){
                map.remove(person);
            }
            if(cnt > 1){
                map.put(person, cnt-1);
            }
        }

        for (String s : map.keySet()) {
            answer = s;
        }

        System.out.println(answer);
    }
}
