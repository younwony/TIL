package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class a9242_폭탄_해체 {

    public static HashMap<String,Integer> numberMap = new HashMap<String, Integer>(){
        {
            put("**** ** ** ****", 0);
            put("  *  *  *  *  *", 1);
            put("***  *****  ***", 2);
            put("***  ****  ****", 3);
            put("* ** ****  *  *", 4);
            put("****  ***  ****", 5);
            put("****  **** ****", 6);
            put("***  *  *  *  *", 7);
            put("**** ***** ****", 8);
            put("**** ****  ****", 9);
        }
    };

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String[] codes = new String[0];
        int size = 0;
        for(int i = 0 ;i < 5; i++){
            String input = bufferedReader.readLine();
            if(i == 0){
                int length = input.length() + 1;
                size = length /4;
                codes = new String[size];
                Arrays.fill(codes,"");
            }

            for(int j = 0; j < size; j++){
                codes[j] += input.substring(j*4, (j+1)*4-1);
            }
        }

        boolean isNonCode = false;

        StringBuilder stringBuilder = new StringBuilder();
        for (String code : codes) {
            Integer intCode = numberMap.get(code);
            if(intCode == null){
                isNonCode = true;
                break;
            }
            stringBuilder
                    .append(intCode);
        }

        if(isNonCode){
            System.out.println("BOOM!!");
        }else{
            int number = Integer.parseInt(stringBuilder.toString());
            if(number % 6 == 0){
                System.out.println("BEER!!");
            }else{
                System.out.println("BOOM!!");
            }
        }

    }

}
