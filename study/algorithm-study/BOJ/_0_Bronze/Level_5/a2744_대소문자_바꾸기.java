package _0_Bronze.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class a2744_대소문자_바꾸기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        HashMap<Character, Character> map = new HashMap<>();
        for(int i = 'A'; i <= 'Z'; i++){
            char low  = (char) (i + 32);
            char upper = (char) i;
            map.put(upper, low);
            map.put(low, upper);
        }

        char[] inputs = bufferedReader.readLine().toCharArray();

        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = map.get(inputs[i]);
        }

        System.out.println(new String(inputs));
    }
}
