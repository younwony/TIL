package _1_Silver.Level_4;

import javax.swing.text.html.parser.Entity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class a1302_베스트셀러 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bufferedReader.readLine());
        HashMap<String, Integer> map = new HashMap<>();

        while(N-- > 0){
            String key = bufferedReader.readLine();
            map.put(key, map.getOrDefault(key, 0) + 1);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                int i = o2.getValue().compareTo(o1.getValue());
                if(i == 0){
                    i = o1.getKey().compareTo(o2.getKey());
                }
                return i;
            }
        });

        System.out.println(list.get(0).getKey());
    }
}
