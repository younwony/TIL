package _1_Silver.Level_4;

import java.util.Scanner;

public class a14501_퇴사 {
    /**
     * DP 사용
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        int[] field = new int[21];
        int a,b;

        for(int i = 0 ; i < n; i++){
            a = scanner.nextInt();
            b = scanner.nextInt();

            // field[i+a] 의 값이 현재 값 + b 의 값보다 작으면 입력된 값으로 교체
            if(field[i+a] < field[i] + b){
                field[i + a] = field[i] + b;
            }

            // a는 최소 1이기에 23라인의 연산이 끝난 후 field[i] 값과 field[i+1]의 값의 비교가 가능하다.
            // field[i+1] 의 값이 현재 값보다 작으면 field[i+1]의 값에 field[i]를 넣어준다.
            if(field[i] > field[i+1]){
                field[i+1] = field[i];
            }
        }
        System.out.println(field[n]);
    }
}
