package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;

public class a7662_이중_우선순위_큐 {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < n; i++) {
            int k = Integer.parseInt(bufferedReader.readLine());
            TreeMap<Integer, Integer> treeMap = new TreeMap<>();
            for (int j = 0; j < k; j++) {
                String[] input = bufferedReader.readLine().split(" ");
                String order = input[0];
                int value = Integer.parseInt(input[1]);

                if("I".equals(order)) {
                    treeMap.put(value, treeMap.getOrDefault(value, 0) + 1);
                }

                if("D".equals(order)){
                    if(treeMap.isEmpty()) continue;
                    if(value == 1) removeKey(treeMap, treeMap.lastKey());
                    if(value == -1) removeKey(treeMap, treeMap.firstKey());
                }
            }
            if(treeMap.isEmpty()) stringBuilder.append("EMPTY").append("\n");
            else stringBuilder.append(treeMap.lastKey() + " " + treeMap.firstKey()).append("\n");
        }

        System.out.println(stringBuilder.toString());
    }

    private static void removeKey(TreeMap<Integer, Integer> treeMap, Integer key) {
        if (treeMap.containsKey(key)) {
            Integer cnt = treeMap.get(key);
            if (cnt == 1) treeMap.remove(key);
            else treeMap.put(key, cnt - 1);
        }
    }
}
