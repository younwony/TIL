package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a1439_뒤집기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String s = bufferedReader.readLine();
        int one = 0;
        int zero = 0;

        char pre = s.charAt(0);

        if(pre == '0'){
            one++;
        }else{
            zero++;
        }

        for(int i = 1; i < s.length(); i++){
            char now = s.charAt(i);
            if(pre != now){
                pre = now;
                if(pre == '0'){
                    one++;
                }else{
                    zero++;
                }
            }
        }

        System.out.println(Math.min(one,zero));

    }
}
