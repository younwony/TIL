package _0_Bronze.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a15721_번데기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int A = Integer.parseInt(bufferedReader.readLine());
        int T = Integer.parseInt(bufferedReader.readLine());
        int aid = Integer.parseInt(bufferedReader.readLine());

        int[] start = {0,1,0,1};
        int[] addAid = {0,1};

        int person = 0;
        int index = 2;
        loop:
        while (T > 0) {

            // Start 4 번
            for (int i = 0; i < start.length; i++) {
                person++;
                if(start[i] == aid){
                    T--;
                    if(T == 0){
                        break loop;
                    }
                }
            }

            // 이후 회차
            for (int i = 0; i < addAid.length; i++) {
                for(int j = 0; j < index; j++){
                    person++;
                    if(addAid[i] == aid){
                        T--;
                        if(T == 0){
                            break loop;
                        }
                    }
                }
            }
            index++;
        }

        System.out.println((person - 1) % A);
    }
}

