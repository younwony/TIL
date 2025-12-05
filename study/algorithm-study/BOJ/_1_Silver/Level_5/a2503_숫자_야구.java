package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;

public class a2503_숫자_야구 {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(bufferedReader.readLine());

        StringTokenizer stringTokenizer;

        int[][] baseballs = new int[N][3];

        for (int i = 0; i < N; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            int number = Integer.parseInt(stringTokenizer.nextToken());
            int strike = Integer.parseInt(stringTokenizer.nextToken());
            int ball = Integer.parseInt(stringTokenizer.nextToken());

            baseballs[i][0] = number;
            baseballs[i][1] = strike;
            baseballs[i][2] = ball;
        }

        ArrayList<Integer> numberList = new ArrayList<>();
        for(int number = 111; number <= 999; number++){
            numberList.add(number);
        }

        for (int[] baseball : baseballs) {
            for (int i = 0; i < numberList.size(); i++) {
                if(!isGameCheck(baseball, numberList.get(i))){
                    numberList.remove(numberList.get(i));
                    i--;
                }
            }
        }

        System.out.println(numberList.size());

    }

    private static boolean isGameCheck(int[] baseball, int number) {
        int base = baseball[0];
        int strike = 0;
        int ball = 0;

        int[] bases = new int[3];
        int[] numbers = new int[3];

        bases[0] = base/100;
        bases[1] = base%100/10;
        bases[2] = base%10;


        numbers[0] = number/100;
        numbers[1] = number%100/10;
        numbers[2] = number%10;

        if(isEqualNumber(numbers) || isZero(numbers)){
            return false;
        }
        
        for (int i = 0; i < 3; i++) {
            int digit = bases[i];
            for (int j = 0; j < 3; j++) {
                if(digit == numbers[j]){
                    if(i == j){
                        strike++;
                    }else{
                        ball++;
                    }
                }
            }
        }

        return strike == baseball[1] && ball == baseball[2];
    }

    private static boolean isZero(int[] numbers) {
        return numbers[0] == 0 || numbers[1] == 0 || numbers[2] == 0;
    }

    private static boolean isEqualNumber(int[] numbers) {
        return numbers[0] == numbers[1] || numbers[1] == numbers[2] || numbers[0] == numbers[2];
    }

}
