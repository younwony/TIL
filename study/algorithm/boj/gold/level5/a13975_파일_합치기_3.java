package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class a13975_파일_합치기_3 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int testCase = Integer.parseInt(bufferedReader.readLine());
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < testCase; i++) {

            int K = Integer.parseInt(bufferedReader.readLine());
            StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            PriorityQueue<Long> programs = new PriorityQueue<>();
            for (int j = 0; j < K; j++) {
                programs.add(Long.parseLong(stringTokenizer.nextToken()));
            }

            result
                    .append(solved(programs))
                    .append("\n");
        }
        System.out.println(result.toString());
    }



    private static long solved(PriorityQueue<Long> programs) {
        long result = 0;

        while(programs.size() > 1){
            Long next = programs.poll() + programs.poll();
            programs.add(next);
            result += next;
        }

        return result;
    }
}
