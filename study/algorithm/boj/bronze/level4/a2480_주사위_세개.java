package _0_Bronze.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

public class a2480_주사위_세개 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] dices = bufferedReader.readLine().split(" ");
        HashMap<String, Integer> map = new HashMap<>();
        int money = 0;

        for (String dice : dices) {
            map.put(dice, map.getOrDefault(dice, 0) + 1);
        }


        if(map.size() == 1){
            money = 10_000 + Integer.parseInt(dices[0]) * 1_000;
        }else if(map.size() == 2){
            int number = 0;
            for (String dice : dices) {
                if(map.get(dice) == 2){
                    number = Integer.parseInt(dice);
                }
            }
            money = 1_000 + number * 100;
        }else{
            int number = Arrays.stream(dices)
                    .mapToInt(Integer::parseInt)
                    .max()
                    .getAsInt();
            money = number * 100;
        }

        System.out.println(money);
    }
}
