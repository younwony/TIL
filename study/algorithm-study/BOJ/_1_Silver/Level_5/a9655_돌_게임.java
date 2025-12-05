package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a9655_돌_게임 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int stone = Integer.parseInt(bufferedReader.readLine());

        System.out.println(stone % 2 == 1 ? "SK" : "CY");
    }
}
