package exam.tranbe;

public class Solve1 {
    public static void main(String[] args) {
        System.out.println(solution("aAb"));
        System.out.println(solution("BA"));
        System.out.println(solution("BbA"));
    }
    private static String solution(String s){
        StringBuilder answer = new StringBuilder();
        s = s.toLowerCase();

        int[] alphabet = new int['z' - 'a' + 1];

        int maxAlphabetCnt = 0;

        for(int i = 0; i < s.length(); i++){
            maxAlphabetCnt = Math.max(maxAlphabetCnt, ++alphabet[s.charAt(i) - 'a']);
        }

        for (int i = 0; i < alphabet.length; i++) {
            if(alphabet[i] == maxAlphabetCnt){
                answer.append((char)(i + 'a'));
            }
        }

        return answer.toString();
    }
}
