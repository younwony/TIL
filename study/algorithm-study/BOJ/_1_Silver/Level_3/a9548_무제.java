package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a9548_무제 {

    private static StringBuilder s;
    private static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int T = Integer.parseInt(bufferedReader.readLine());
        String input;
        while(T-- > 0){
            s = new StringBuilder(bufferedReader.readLine());

            while(!"END".equals(input = bufferedReader.readLine())){
                solved(input);
            }
        }

        System.out.println(result.toString());
    }

    private static void solved(String input) {

        StringTokenizer inputs = new StringTokenizer(input);

        String method = inputs.nextToken();

        if("I".equals(method)){
            String insertStr = inputs.nextToken();
            int index = Integer.parseInt(inputs.nextToken());

            insert(insertStr, index);
        }

        if("P".equals(method)){
            int start = Integer.parseInt(inputs.nextToken());
            int end = Integer.parseInt(inputs.nextToken());
            result
                    .append(print(start,end))
                    .append("\n");
        }
    }

    private static String print(int start, int end) {
        return s.substring(start,end+1);
    }

    private static void insert(String insertStr, int index) {
        s.insert(index, insertStr);
    }
}
