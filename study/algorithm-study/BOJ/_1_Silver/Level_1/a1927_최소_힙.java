package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;

public class a1927_최소_힙 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());
        StringBuilder stringBuilder = new StringBuilder();
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        for(int i = 0; i < n; i++){
            int input = Integer.parseInt(bufferedReader.readLine());
            if(input == 0){
                int rootValue = 0;
                if(!priorityQueue.isEmpty()) rootValue = priorityQueue.poll();
                stringBuilder
                        .append(rootValue)
                        .append("\n");
            }else{
                priorityQueue.add(input);
            }
        }
        System.out.println(stringBuilder.substring(0, stringBuilder.length()-1).toString());
    }
}
