package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class a12919_A와B_2 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String S = bufferedReader.readLine();
        String T = bufferedReader.readLine();

        int result = 0;
        Queue<String> queue = new LinkedList<>();
        queue.add(S);
        while(!queue.isEmpty()){
            String s = queue.poll();
            if(s.equals(T)){
                result = 1;
                break;
            }
            if(s.length() < T.length()){
                queue.add(s+"A");
                queue.add(new StringBuilder(s+"B").reverse().toString());
            }
        }

        System.out.println(result);
    }
}
