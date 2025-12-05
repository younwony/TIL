package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

public class a20551_Sort_마스터_배지훈의_후계자 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int N = Integer.parseInt(stringTokenizer.nextToken());
        int M = Integer.parseInt(stringTokenizer.nextToken());

        int[] aArrays = new int[N];
        for (int i = 0; i < aArrays.length; i++) {
            aArrays[i] = Integer.parseInt(bufferedReader.readLine());
        }

        Arrays.sort(aArrays);

        HashMap<Integer,Integer> map = new HashMap<>();
        for (int i = 0; i < aArrays.length; i++) {
            if(!map.containsKey(aArrays[i])){
                map.put(aArrays[i], i);
            }
        }

        StringBuilder result = new StringBuilder();
        while(M-- > 0){
            int input = Integer.parseInt(bufferedReader.readLine());
            result
                    .append(map.getOrDefault(input, -1))
                    .append("\n");
        }

        System.out.println(result.toString());

    }
}
