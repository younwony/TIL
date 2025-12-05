package _1_Silver.Level_2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class a1541_잃어버린_괄호 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inputLine = scanner.nextLine();
        String[] numsStr = inputLine.split("\\+|-");
        String[] signs = inputLine.replaceAll("\\d", "").split("");

        Deque<Integer> numQue = new ArrayDeque<>();
        Deque<String> signQue = new ArrayDeque<>();
        for (String s : numsStr) {
            numQue.add(Integer.valueOf(s));
        }
        for (String sign : signs) {
            signQue.add(sign);
        }

        for (int i = 0; i < signs.length; i++) {
            Integer num = numQue.pollFirst();
            String sign = signQue.pollFirst();
            if("+".equals(sign)){
                num += numQue.pollFirst();
                numQue.push(num);
            }else{
                signQue.add(sign);
                numQue.add(num);
            }
        }
        numQue.add(numQue.pollFirst());
        int answer = numQue.poll();
        int sum = 0;
        while(!numQue.isEmpty()){
            sum += numQue.poll();
        }
        System.out.println(answer - sum);

    }
}
