package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class a14699_관악산_등산 {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int N = Integer.parseInt(stringTokenizer.nextToken());
        int M = Integer.parseInt(stringTokenizer.nextToken());

        HashMap<Integer, Integer> points = new HashMap<>();

        stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        for (int i = 0; i < N; i++) {
            points.put(i,Integer.parseInt(stringTokenizer.nextToken()));
        }

        ArrayList<Map.Entry<Integer, Integer>> entries = new ArrayList<>(points.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });

        int[][] field = new int[N][N];

        for (int i = 0; i < M; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            int v = Integer.parseInt(stringTokenizer.nextToken()) - 1;
            int nextV = Integer.parseInt(stringTokenizer.nextToken()) - 1;
            field[v][nextV] = 1;
            field[nextV][v] = 1;
        }
        int[] dp = new int[N];
        Arrays.fill(dp,1);

        for (Map.Entry<Integer, Integer> entry : entries) {
            int point = entry.getKey();
            for(int i = 0; i < field[point].length; i++){
                if(field[point][i] == 1 && points.get(point) < points.get(i)){
                    dp[point] = Math.max(dp[point],dp[i]+1);
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i : dp) {
            result.append(i).append("\n");
        }

        System.out.println(result.toString());

    }
}
