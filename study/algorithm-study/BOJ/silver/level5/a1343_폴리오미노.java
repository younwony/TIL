package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a1343_폴리오미노 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input = bufferedReader.readLine();

        String replace = input.replace("XX", "BB");

        System.out.println(replace.indexOf("X") > -1 ? -1 : replace.replace("BBBB","AAAA"));

    }
}
