package _0_Bronze.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a2845_파티가_끝나고_난_뒤 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int l = Integer.parseInt(st.nextToken());
        int p = Integer.parseInt(st.nextToken());
        int field = l * p;

        StringBuilder sb = new StringBuilder();
        st = new StringTokenizer(br.readLine());

        while(st.hasMoreTokens()){
            int input = Integer.parseInt(st.nextToken());
            sb.append(input - field)
                    .append(" ");
        }

        System.out.println(sb);
    }
}
