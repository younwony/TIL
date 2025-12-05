package _0_Bronze.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a10101_삼각형_외우기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int a = Integer.parseInt(bufferedReader.readLine());
        int b = Integer.parseInt(bufferedReader.readLine());
        int c = Integer.parseInt(bufferedReader.readLine());

        String result = "Scalene";

        if(a == b || b == c || a == c){
            result = "Isosceles";
        }

        if(a == b && b == c){
            result = "Equilateral";
        }

        if(a + b + c != 180){
            result = "Error";
        }

        System.out.println(result);
    }
}
