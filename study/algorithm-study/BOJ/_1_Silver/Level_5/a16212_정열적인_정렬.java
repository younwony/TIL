package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a16212_정열적인_정렬 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int maxSize = 4_000_001;
        int[] numbers = new int[maxSize];
        bufferedReader.readLine();
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int absNumber = 2_000_000;
        while(stringTokenizer.hasMoreTokens()){
            int num = Integer.parseInt(stringTokenizer.nextToken());
            numbers[num + absNumber]++;
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < maxSize; i++) {
            for(int j = 0; j < numbers[i]; j++){
                result.append(i - absNumber)
                        .append(" ");
            }
        }

        System.out.println(result.toString());
    }
}
