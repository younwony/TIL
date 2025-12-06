package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class a2443_별_찍기_6 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(bufferedReader.readLine());
        int x = 2*n - 1;
        for(int i = 0; i < n; i++){

            for(int j = 0; j < i; j++){
                output.write(" ");
            }

            for(int j = i; j < x - i; j++){
                output.write("*");
            }

            output.newLine();
        }

        output.close();
    }
}
