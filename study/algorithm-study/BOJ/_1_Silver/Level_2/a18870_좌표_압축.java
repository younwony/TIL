package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class a18870_좌표_압축 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());

        int[] xInput = new int[n];
        String[] input = bufferedReader.readLine().split(" ");
        for(int i = 0 ; i < n; i++){
            int x = Integer.parseInt(input[i]);
            xInput[i] = x;
        }

        Arrays.sort(xInput);

        HashMap<String,Integer> resultMap = new HashMap<>();
        int temp = xInput[0];
        int cnt = 0;
        resultMap.put(xInput[0] + "", cnt++);
        for (int x : xInput) {
            if(temp != x) {
                temp = x;
                resultMap.put(x + "", cnt++);
            }
        }


        StringBuilder result = new StringBuilder();

        for (String s : input) {
            result.append(resultMap.get(s) + " ");
        }

        System.out.println(result.toString());
    }
}
