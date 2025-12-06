package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a2522_별_찍기_12 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder output = new StringBuilder();
        int n = Integer.parseInt(bufferedReader.readLine());
        for(int i = 1 ; i <= 2*n - 1; i++){
            for(int j = 0; j < Math.abs(n - i); j++){
                output.append(" ");
            }
            for(int j = Math.abs(n - i); j < n; j++){
                output.append("*");
            }
            output.append("\n");
        }

        System.out.println(output.toString());


    }
}
