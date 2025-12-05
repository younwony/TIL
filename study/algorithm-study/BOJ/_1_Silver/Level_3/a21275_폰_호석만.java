package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a21275_폰_호석만 {
    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] AB = bufferedReader.readLine().split(" ");

        int cnt = 0;


        long x = 0;
        long a = 0;
        long b = 0;

        for(int i = 2; i <= 36; i++){
            try {
                long aConvertBase = Long.parseLong(AB[0], i);
                for(int j = 2; j <= 36; j++){
                    if(i == j ){
                        continue;
                    }
                    try {
                        long bConvertBase = Long.parseLong(AB[1], j);
                        if(aConvertBase == bConvertBase){
                            x = aConvertBase;
                            a = i;
                            b = j;
                            cnt++;
                            break;
                        }
                    }catch (NumberFormatException e){
                        continue;
                    }
                }
            }catch (NumberFormatException e){
                continue;
            }
        }

        if(cnt == 1){
            System.out.println(x + " " + a + " " + b);
        }else if(cnt > 1){
            System.out.println("Multiple");
        }else if(cnt == 0){
            System.out.println("Impossible");
        }
    }
}
