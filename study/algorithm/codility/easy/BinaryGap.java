package easy;

public class BinaryGap {
    public static void main(String[] args) {
        System.out.println(solution(1041));
        System.out.println(solution(32));
    }

    public static int solution(int N) {
        int result = 0;

        String binaryString = Integer.toBinaryString(N);
        char[] chars = binaryString.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if(chars[i] == '1'){
                i++;
                int cnt = 0;
                for (; i < chars.length; i++) {
                    if(chars[i] == '0'){
                        cnt++;
                    }

                    if(chars[i] == '1'){
                        result = Math.max(result, cnt);
                        i--;
                        break;
                    }
                }
            }
        }

        return result;
    }
}
