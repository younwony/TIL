package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.StringTokenizer;

public class a14425_문자열_집합 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int n = Integer.parseInt(stringTokenizer.nextToken());
        int m = Integer.parseInt(stringTokenizer.nextToken());

        HashSet<String> S = new HashSet<>();
        while(n-- > 0){
            S.add(bufferedReader.readLine());
        }

        int result = 0;
        while(m-- > 0){
            if(S.contains(bufferedReader.readLine())){
                result++;
            }
        }

        System.out.println(result);
    }
}
