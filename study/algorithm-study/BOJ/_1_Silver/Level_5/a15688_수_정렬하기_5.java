package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a15688_수_정렬하기_5 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());

        int[] arrays = new int[2_000_001];

        for(int i = 0; i < n; i++) {
            arrays[Integer.parseInt(bufferedReader.readLine()) + 1_000_000]++;
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 2_000_001; i++) {
            if(arrays[i] > 0){
                for(int j = 0; j < arrays[i]; j++){
                    result.append(i - 1_000_000)
                            .append("\n");
                }
            }
        }

        System.out.println(result.toString());
    }
}
