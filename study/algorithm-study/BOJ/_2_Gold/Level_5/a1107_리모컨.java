package _2_Gold.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a1107_리모컨 {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String input = bufferedReader.readLine();
        int n = Integer.parseInt(input);
        int m = Integer.parseInt(bufferedReader.readLine());
        String error = bufferedReader.readLine();
        int answer = Integer.MAX_VALUE;
        /* 최소값 첫 번째 그냥 - or + 버튼 사용*/
        int fistAnswer = Math.abs(100-n);
        answer = fistAnswer;

        int nMax = n + 1;
        boolean maxCheck = false;
        int nMin = n - 1;
        boolean minCheck = false;
        while(nMax <= 1000000){
            if(isNonError(nMax,error)) {
                maxCheck = true;
                break;
            }
            nMax++;
        }
        if(maxCheck){
            answer = Math.min(answer, nMax - n + String.valueOf(nMax).length());
        }
        while(nMin >= 0 || nMin == 0) {
            if (isNonError(nMin, error)) {
                minCheck = true;
                break;
            }
            nMin--;
        }
        if(minCheck){
            answer = Math.min(answer, n -nMin + String.valueOf(nMin).length());
        }

        if(isNonError(n,error)) answer = Math.min(answer, input.length());

        System.out.println(answer);

    }

    private static boolean isNonError(int num, String error) {

        if(error == null) return true;

        String numStr = String.valueOf(num);
        for(int i = 0; i < numStr.length(); i++){
            if(error.indexOf(numStr.charAt(i)) > -1) return false;
        }
        return true;
    }

}
