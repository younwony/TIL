package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class a1969_DNA {
    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int n = Integer.parseInt(stringTokenizer.nextToken());
        int m = Integer.parseInt(stringTokenizer.nextToken());
        String[] dnas = new String[n];
        int[][] check = new int[m]['Z' - 'A' + 1];
        for(int i = 0; i < n; i++){
            dnas[i] = bufferedReader.readLine();
            for(int j = 0; j < m; j++){
                int cInt = dnas[i].charAt(j) - 'A';
                check[j][cInt]++;
            }
        }

        StringBuilder dna =  new StringBuilder();
        int sum = 0;
        for(int i = 0; i < m; i++){
            int maxCharIndex = 0;
            for(int j = 'A'; j <= 'Z'; j++){
                if(check[i][j - 'A'] > check[i][maxCharIndex]){
                    maxCharIndex = j - 'A';
                }
            }
            sum += n - check[i][maxCharIndex];
            dna.append((char)(maxCharIndex + 'A'));
        }
        dna
                .append("\n")
                .append(sum);

        System.out.println(dna.toString());

    }
}
