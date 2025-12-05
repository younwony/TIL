package DataStructure;

public class DF_KMP {
    public static void main(String[] args) {
        patternTable("ababcaaba");
        patternTable("aaaab");
        patternTable("abcabd");
        patternTable("ababa");

        KMP("ababcaaba", "aba", patternTable("aba"));
    }

    private static void KMP(String text, String pattern, int[] patternTable) {

        int answer = 0;
        int size = text.length();
        int j = 0;
        for(int i = 0; i < size; i++){
            while(j > 0 && text.charAt(i) != pattern.charAt(j)){
                j = patternTable[j-1];
            }
            if(text.charAt(i) == pattern.charAt(j)){
                if(j == pattern.length() -1){
                    answer++;
                    break;
                }
                else ++j;
            }
        }

        System.out.println(answer);
    }

    private static int[] patternTable(String test) {
        int stringSize = test.length();
        int[] pi = new int[stringSize];
        /* 비교 대상이 되는 인덱스, 이어지는 값*/
        int i = 0;

        /**
         * abacaaba 예
         * a = 0, pi[0] = 0
         *
         * ab = 0
         *      iC = a, jC = b
         *      불일치로 p[1] = 0
         *
         * aba = 1
         *      iC = a, jC = a
         *      일치로 p[2] = 1, i 값 위치 증가 i++
         *
         * abac = 0
         *      iC = b, jC = c
         *      불일치 -> i가 0보다 크기 때문에 i를 0으로 초기화 후 다시 비교
         *
         *      iC = a, jC = c
         *      불일치로 p[3] = 0
         *
         * abaca = 1
         *      iC = a, jC = a
         *      일치로 p[4] = 1, i값 위치 증가 i++
         *
         * abacaa = 1,
         *      iC = b, jC = a
         *      불일치 -> i가 0보다 크기 때문에 i를 0으로 초기화 후 다시 비교
         *
         *      iC = a, jC = a
         *      일치로 p[5] = 1, i값 위치 증가 i++
         *
         * abacaab = 2
         *      iC = b, jC = b
         *      일치로 p[6] = 2, i값 위치 증가 i++
         *
         * abacaaba = 3
         *      iC = a, jC = a
         *      일치로 p[7] = 3, i값 위치증가 i++
         */
        for(int j = 1; j < stringSize; j++){
            /* i 번째 문자 */
            char iC = test.charAt(i);
            /* j 번째 문자 */
            char jC = test.charAt(j);

            /* 문자가 같을 경우 */
            if(iC == jC){
                /* 현재 pi[j] 위치에 i + 1 한 값을 삽입한다. */
                pi[j] = ++i;
            }

            /* 문자가 다를 경우 */
            if(iC != jC){
                /* i 가 0으로 첫번째 문자에 위치하였을 경우 비교대상이 더이상 없으므로 0*/
                if(i == 0) pi[j] = 0;
                /* 이전 i가 같은 패턴이여서 i가 0보다 큰값이 왔을경우*/
                if(i > 0){
                    /* 다시 처음부터 비교하도록 i를 0으로 초기화 */
                    i = 0;
                    /* J의 위치는 이동하지 않아야하므로 1을 빼준다. */
                    j--;
                }
            }
        }
        return pi;
    }
}
