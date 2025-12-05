package _0_Bronze.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a2953_나는_요리사다 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int person = 0;
        int max = 0;
        StringTokenizer stringTokenizer;
        for(int i = 1; i <= 5; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            int sum = 0;
            while(stringTokenizer.hasMoreTokens()){
                sum += Integer.parseInt(stringTokenizer.nextToken());
            }

            if(sum > max){
                max = sum;
                person = i;
            }
        }

        System.out.println(person + " " + max);
    }

}
