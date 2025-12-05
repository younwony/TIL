package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.PriorityQueue;

public class a11279_최대힙 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Comparator.reverseOrder());
        StringBuilder stringBuilder = new StringBuilder();

        for(int i =0 ; i < n; i++){
            int input = Integer.parseInt(bufferedReader.readLine());
            if(input == 0){
                int result = 0;
                if(!priorityQueue.isEmpty()) result = priorityQueue.poll();
                stringBuilder
                        .append(result)
                        .append("\n");
            }else{
                priorityQueue.add(input);
            }
        }
        System.out.println(stringBuilder.toString());
    }
}
