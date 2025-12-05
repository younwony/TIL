package _0_Bronze.Level_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class a2358_평행선 {
    public static void main(String[] args) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        try {
            int n = Integer.parseInt(bufferedReader.readLine());

            Map<Integer, Integer> xMap = new HashMap<>();
            Map<Integer, Integer> yMap = new HashMap<>();

            String[] tempArray = new String[2];
            int result = 0;

            int x,y;

            for(int i = 0 ; i < n; i++){
                tempArray = bufferedReader.readLine().split(" ");
                x = Integer.parseInt(tempArray[0]);
                y = Integer.parseInt(tempArray[1]);

                if(xMap.keySet().contains(x)){
                    if(xMap.get(x) == 1){
                        result++;
                        xMap.remove(x);
                        xMap.put(x,2);
                    }
                }else{
                    xMap.put(x, 1);
                }

                if(yMap.keySet().contains(y)){
                    if(yMap.get(y) == 1){
                        yMap.remove(y);
                        yMap.put(y,2);
                        result++;
                    }
                }else{
                    yMap.put(y, 1);
                }
            }

            bufferedWriter.write(Integer.toString(result));

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(bufferedReader != null){bufferedReader.close();}
                if(bufferedWriter != null){bufferedWriter.flush(); bufferedWriter.close();}
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
