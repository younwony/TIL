package _1_Silver.Level_4;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class a2164_카드 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Queue<Integer> queue = new LinkedList<>();
        for(int i = 1; i <= n; i++){
            queue.add(i);
        }
        Integer poll = 0;
        while(!queue.isEmpty()){
            poll = queue.poll();
            if(queue.isEmpty()) break;
            queue.add(queue.poll());
        }
        System.out.println(poll);
    }
}
