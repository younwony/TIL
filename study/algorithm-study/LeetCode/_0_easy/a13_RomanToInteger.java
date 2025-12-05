package _0_easy;

import java.util.HashMap;

public class a13_RomanToInteger {

    public static void main(String[] args) {
        System.out.println(romanToInt("MCMXCIV"));
    }

    public static int romanToInt(String s) {
        HashMap<Character, Integer> map = new HashMap<>();
        map.put('I',1);
        map.put('V',5);
        map.put('X',10);
        map.put('L',50);
        map.put('C',100);
        map.put('D',500);
        map.put('M',1000);
        map.put('a',4); // IV
        map.put('b',9); // IX
        map.put('c',40); // XL
        map.put('d',90); // XC
        map.put('e',400); // CD
        map.put('f',900); // CM

        s = s.replaceAll("IV", "a")
                .replaceAll("IX", "b")
                .replaceAll("XL", "c")
                .replaceAll("XC", "d")
                .replaceAll("CD", "e")
                .replaceAll("CM", "f");


        int sum = 0;

        for(int i = 0; i < s.length(); i++){
            sum += map.get(s.charAt(i));
        }

        return sum;
    }
}
