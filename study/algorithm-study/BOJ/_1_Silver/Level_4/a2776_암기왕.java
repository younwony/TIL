package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class a2776_암기왕 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(bufferedReader.readLine());
        StringTokenizer stringTokenizer;
        StringBuilder stringBuilder = new StringBuilder();
        while(T-- > 0){
            HashSet<String> set = new HashSet<>();
            bufferedReader.readLine();
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            while (stringTokenizer.hasMoreTokens()){
                set.add(stringTokenizer.nextToken());
            }
            bufferedReader.readLine();
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            while (stringTokenizer.hasMoreTokens()){
                stringBuilder
                        .append(set.contains(stringTokenizer.nextToken()) ? 1 : 0)
                        .append("\n");
            }
        }
        System.out.println(stringBuilder.toString());
    }
}
