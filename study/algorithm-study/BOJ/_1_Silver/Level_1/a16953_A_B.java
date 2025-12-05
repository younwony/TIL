package _1_Silver.Level_1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class a16953_A_B {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        Queue<Cnt> queue = new LinkedList<>();
        queue.add(new Cnt(a, 1));

        boolean isNonCnt = true;
        while (!queue.isEmpty()){
            Cnt cnt = queue.poll();
            if(cnt.a == b){
                isNonCnt = false;
                System.out.println(cnt.cnt);
                break;
            }

            if(cnt.a * 2 <= b){
                queue.add(new Cnt(cnt.a*2, cnt.cnt+1));
            }

            if(cnt.a * 10 + 1 <= b){
                queue.add(new Cnt(cnt.a * 10 + 1, cnt.cnt+1));
            }
        }
        if(isNonCnt) System.out.println(-1);
    }

    static class Cnt{
        long a;
        long cnt;

        public Cnt(long a, long cnt) {
            this.a = a;
            this.cnt = cnt;
        }
    }
}
