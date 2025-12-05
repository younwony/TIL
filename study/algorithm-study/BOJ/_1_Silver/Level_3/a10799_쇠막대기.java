package _1_Silver.Level_3;

import java.util.Scanner;

public class a10799_쇠막대기 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String inputData = scanner.nextLine();

        inputData = inputData.replace("()", "x");

        String str;

        int left = 0;
        int sum = 0;

        for(int i = 0 ; i < inputData.length(); i++){
            str = String.valueOf(inputData.charAt(i));
            if("(".equals(str)){
                left++;
            }else if(")".equals(str)){
                sum++;
                left--;
            }else if("x".equals(str)){
                sum += left;
            }
        }

        System.out.println(sum);
    }
}
