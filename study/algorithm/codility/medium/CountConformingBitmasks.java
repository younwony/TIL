package medium;

public class CountConformingBitmasks {

    public static void main(String[] args) {
        System.out.println(Math.pow(2, 30) > Integer.MAX_VALUE);
        System.out.println(solution(2, 2, 2));
    }

    public static int solution(int A, int B, int C) {
        String aBinary = getBinaryStr(A);
        String bBinary = getBinaryStr(B);
        String cBinary = getBinaryStr(C);

        int aCaseCnt = getZeroCase(aBinary);
        int bCaseCnt = getZeroCase(bBinary);
        int cCaseCnt = getZeroCase(cBinary);
        int abCaseCnt = getZeroCase(aBinary, bBinary);
        int acCaseCnt = getZeroCase(aBinary, cBinary);
        int bcCaseCnt = getZeroCase(bBinary, cBinary);
        int abcCaseCnt = getZeroCase(aBinary, bBinary, cBinary);

        return aCaseCnt + bCaseCnt + cCaseCnt - abCaseCnt - acCaseCnt - bcCaseCnt + abcCaseCnt;
    }

    private static int getZeroCase(String binaryStr) {
        int cnt = 0;
        char[] binaryChars = binaryStr.toCharArray();
        for (char binaryChar : binaryChars) {
            if(binaryChar == '0'){
                cnt++;
            }
        }
        return (int) Math.pow(2,cnt);
    }

    private static int getZeroCase(String aBinaryStr, String bBinaryStr) {
        int cnt = 0;
        char[] aBinaryChars = aBinaryStr.toCharArray();
        char[] bBinaryChars = bBinaryStr.toCharArray();
        for (int i = 0; i < bBinaryChars.length; i++) {
            char aBinaryChar = aBinaryChars[i];
            char bBinaryChar = bBinaryChars[i];

            if(aBinaryChar == '0' && bBinaryChar == '0'){
                cnt++;
            }
        }
        return (int) Math.pow(2,cnt);
    }

    private static int getZeroCase(String aBinaryStr, String bBinaryStr, String cBinaryStr) {
        int cnt = 0;
        char[] aBinaryChars = aBinaryStr.toCharArray();
        char[] bBinaryChars = bBinaryStr.toCharArray();
        char[] cBinaryChars = cBinaryStr.toCharArray();

        for (int i = 0; i < bBinaryChars.length; i++) {
            char aBinaryChar = aBinaryChars[i];
            char bBinaryChar = bBinaryChars[i];
            char cBinaryChar = cBinaryChars[i];

            if(aBinaryChar == '0' && bBinaryChar == '0' && cBinaryChar == '0'){
                cnt++;
            }
        }
        return (int) Math.pow(2,cnt);
    }

    private static String getBinaryStr(int num) {
        String str = Integer.toString(num, 2);
        return String.format("%30s", str).replace(' ', '0');
    }
}
