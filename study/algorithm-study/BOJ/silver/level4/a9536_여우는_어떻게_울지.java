package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a9536_여우는_어떻게_울지 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(bufferedReader.readLine());
        StringBuilder result = new StringBuilder();
        while(T-- > 0){
            String[] input = bufferedReader.readLine().split(" ");
            String crying = "";
            while(!"what does the fox say?".equals((crying = bufferedReader.readLine().trim()))){
                removeCry(input, crying.split(" ")[2]);
            }
            result
                    .append(concat(input).trim())
                    .append("\n");
        }
        System.out.println(result.toString());
    }

    private static String concat(String[] input) {
        StringBuilder concat = new StringBuilder();
        for (String s : input) {
            if(!"".equals(s)){
                concat
                        .append(s)
                        .append(" ");
            }
        }
        return concat.toString();
    }

    private static void removeCry(String[] input, String s) {
        for (int i = 0; i < input.length; i++) {
            if(s.equals(input[i])){
                input[i] = "";
            }
        }
    }
}
