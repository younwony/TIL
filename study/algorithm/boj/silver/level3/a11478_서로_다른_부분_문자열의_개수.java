package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class a11478_서로_다른_부분_문자열의_개수 {

    private static HashSet<String> set = new HashSet<>();
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input = bufferedReader.readLine();
        for(int i = 0; i <= input.length(); i++){
            for(int j = i + 1; j <= input.length(); j++){
                set.add(input.substring(i, j));
            }
        }
        System.out.println(set.size());
    }
}
