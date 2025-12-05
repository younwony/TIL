package level_2;

public class _이진_변환_반복하기 {

    public static void main(String[] args) {
        System.out.println(solution("110010101001"));
        System.out.println(solution("01110"));
        System.out.println(solution("1111111"));
    }

    private static int[] solution(String s) {
        int[] answer = new int[2];

        while(!"1".equals(s)){
            int[] zeroOne = new int[2];
            setZeroOne(zeroOne,s);
            answer[1] += zeroOne[0];
            s = Integer.toBinaryString(zeroOne[1]);
            answer[0]++;
        }
        return answer;
    }

    private static void setZeroOne(int[] zeroOne, String s) {
        for(int i = 0 ;i < s.length(); i++){
            char c = s.charAt(i);
            if(c == '0') zeroOne[0]++;
            if(c == '1') zeroOne[1]++;
        }
    }
}
