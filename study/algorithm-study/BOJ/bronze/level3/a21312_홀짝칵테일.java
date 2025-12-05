package _0_Bronze.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class a21312_홀짝칵테일 {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int a = Integer.parseInt(stringTokenizer.nextToken());
        int b = Integer.parseInt(stringTokenizer.nextToken());
        int c = Integer.parseInt(stringTokenizer.nextToken());


        int result = 0;

        Set<Integer> set = new HashSet<>();

        set.add(a);
        set.add(b);
        set.add(c);
        set.add(a*b);
        set.add(a*c);
        set.add(b*c);
        set.add(a*b*c);

        for (int number : set) {
            if(number > result){
                if(number % 2 == 1){
                    result = number;
                }else{
                    if(result % 2 == 0){
                        result = number;
                    }
                }
            }else{
                if(number % 2 == 1 && result % 2 == 0){
                    result = number;
                }
            }
        }

        System.out.println(result);


    }
}
