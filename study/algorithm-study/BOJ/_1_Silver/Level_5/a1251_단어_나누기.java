package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class a1251_단어_나누기 {

    static ArrayList<String> strings = new ArrayList<>();
    static String input;
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        input = bufferedReader.readLine();
        
        dfs(0, 0, "");

        Collections.sort(strings);

        System.out.println(strings.get(0));
    }

    private static void dfs(int index,int cnt, String s) {
        if(cnt == 2){
            String newStr = s + new StringBuilder(input.substring(index, input.length())).reverse().toString();
            strings.add(newStr);
            return;
        }

        for(int i = index + 1; i < input.length(); i++){
            String newStr = new StringBuilder(input.substring(index, i)).reverse().toString();
            dfs(i,cnt+1,s + newStr);
        }
    }
}
