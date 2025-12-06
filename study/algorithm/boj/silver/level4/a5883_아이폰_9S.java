package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class a5883_아이폰_9S {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(bufferedReader.readLine());

        int[] sizes = new int[N];
        Set<Integer> sizeSet = new HashSet<Integer>();

        for (int i = 0; i < N; i++) {
            int size = Integer.parseInt(bufferedReader.readLine());
            sizes[i] = size;
            sizeSet.add(size);
        }
        
        int max = 0;

        for (Integer removeSize : sizeSet) {
            int len = 0;
            int pre = 0;
            for (int i = 0; i < sizes.length; i++) {
                int size = sizes[i];
                if(size == removeSize){
                    continue;
                }

                if(pre == 0){
                    pre = size;
                }

                if(size == pre){
                    len++;
                }else{
                    max = Math.max(max, len);
                    len = 1;
                    pre = size;
                }
            }

            max = Math.max(max, len);
        }


        System.out.println(max);
    }
}
