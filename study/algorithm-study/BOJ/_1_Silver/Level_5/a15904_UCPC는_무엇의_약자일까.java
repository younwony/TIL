package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a15904_UCPC는_무엇의_약자일까 {

    private static final String UCPC = "I love UCPC";
    private static final String NON_UCPC = "I hate UCPC";
    private static final Character[] UCPCS = {'U', 'C', 'P', 'C'};

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String input = bufferedReader.readLine();

        for (Character ucpc : UCPCS) {
            int index = input.indexOf(ucpc);
            if(index == -1){
                System.out.println(NON_UCPC);
                return;
            }
            input = input.substring(index + 1);
        }
        System.out.println(UCPC);
    }
}
