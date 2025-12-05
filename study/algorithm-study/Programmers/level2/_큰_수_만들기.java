package level_2;

public class _큰_수_만들기 {

    public static void main(String[] args) {
        System.out.println(solution("99991", 3));
        System.out.println(solution("111119", 3));
    }

    private static String solution(String number, int k) {

        /* 이후 길이 체크를 위한 변수*/
        int resultLen = number.length()-k;

        /* 현재 loop에서 큰 숫자의 인덱스 */
        int nowIndex = 0;
        /* 결과 값 */
        StringBuilder answer = new StringBuilder();
        while(true){
            /* loop에서 큰숫자 체크*/
            int max = 0;
            for(int i = 0; i <= k; i++){
                int num = number.charAt(i) - '0';
                if(num > max){
                    max = num;
                    nowIndex = i;
                }
            }
            /* 선택완료후 String 자르기*/
            number = number.substring(nowIndex+1);
            /* 선택한 Index 까지 수 K에서 제거*/
            k -= nowIndex;
            /*결과값 저장*/
            answer.append(max);
            /* 제거개수 k를 모두 사용했으면 남은 number를 붙인 후 종료*/
            if(k < 1){
                answer.append(number);
                break;
            }
            /* 결과값이 최종 길이수만큼 이루어졌을 떄에도 종료*/
            if(answer.length() == resultLen) break;
        }

        return answer.toString();
    }
}
