package _1_Silver.Level_5;

import java.util.Scanner;

public class a16171_나는_친구가_적다 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String S = scanner.nextLine();
        String K = scanner.nextLine();
        S = S.replaceAll("\\d","");
        boolean isExist = S.indexOf(K) > -1;
        System.out.println(isExist ? 1 : 0);
    }
}
