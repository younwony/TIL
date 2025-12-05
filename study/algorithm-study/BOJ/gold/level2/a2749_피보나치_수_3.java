package _2_Gold.Level_2;

import java.util.Scanner;

public class a2749_피보나치_수_3 {
    /*
    * 나머지를 이용해 만든 수열은 주기가 나타난다는 피사노 주기 공식에 의해, 1,000,000 으로 나눈 나머지의 값을 구하는 문제에서
    * 입력값의 n%1500000 에 해당하는 피보나치 수를 구하면 된다.
    * #참고 a9471_피사노_주기
    * */
    static final int PisanoCnt = 1500000;
    static final int MOD = 1000000;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong()%PisanoCnt;
        int[] fivo = new int[PisanoCnt+1];
        fivo[1] = 1;
        for(int i = 2; i < fivo.length; i++){
            fivo[i] = (fivo[i-1] + fivo[i-2])%MOD;
        }
        System.out.println(fivo[(int) n]);
    }
}
