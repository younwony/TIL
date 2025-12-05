package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 나머지를 이용해 만든 수열은 주기가 나타날 수 있다.
 * k(m)을 반복하는 부분 수열의 길이라 했을 때, k(11) = 10 임을 알 수 있다.
 * n            1   2   3   4   5   6   7   8   9   10
 * F(n)         1   1   2   3   5   8   13  21  34  55
 * F(n) mod 11  1   1   2   3   5   8   2   10  1   0
 *
 * 그외 성질
 *
 *  1.  m > 2 인 경우 k(m)은 짝수
 *  2. 임의의 짝수 정수 n > 2 에 대해, k(m) = n 인 m이 항상 존재
 *  3. k(m) <= Math.pow(m,2) - 1
 *  4. k(Math.pow(2,n)) = 3 * Math.pow(2,n-1)
 *  5. k(Math.pow(5,n) = 4 * Math.pow(5,n)
 *  6. n > 2 라면, k(Math.pow(10,n) = 15*Math.pow(10,n-1)
 *  
 *  m 이 주어졌을 때 , k(m)을 구하시오
 *
 *
 */
public class a9471_피사노_주기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int testCase = Integer.parseInt(bufferedReader.readLine());
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < testCase; i++){
            String[] nm = bufferedReader.readLine().split(" ");
            int n = Integer.parseInt(nm[0]);
            int m = Integer.parseInt(nm[1]);

            result
                .append(n + " ")
                .append(pisano(m))
                .append("\n");
        }
        System.out.println(result.toString());
    }

    /**
     *  1.  m > 2 인 경우 k(m)은 짝수
     *  2. 임의의 짝수 정수 n > 2 에 대해, k(m) = n 인 m이 항상 존재
     *  3. k(m) <= Math.pow(m,2) - 1
     *  4. k(Math.pow(2,n)) = 3 * Math.pow(2,n-1)
     *  5. k(Math.pow(5,n) = 4 * Math.pow(5,n)
     *  6. n > 2 라면, k(Math.pow(10,n) = 15*Math.pow(10,n-1)
     */
    private static long pisano(int m) {
        int one = 0;
        int two = 1;
        int sum = (one + two)%m;
        int cnt = 1;
        while(true){
            one = two;
            two = sum;
            sum = (one + two)%m;
            if(one == 0 && two == 1) break;
            cnt++;
        }

        return cnt;
    }
}
