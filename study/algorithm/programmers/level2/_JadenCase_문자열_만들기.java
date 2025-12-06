package level_2;

public class _JadenCase_문자열_만들기 {

    public static void main(String[] args) {
        System.out.println(solution("       "));
        System.out.println(solution("3people unFollowed me   "));
        System.out.println(solution("for the last week"));
        System.out.println(solution("a"));
    }

    private static String solution(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        s = s.toLowerCase();
        String[] splitStr = s.split(" ", -1);
        for (int i = 0; i < splitStr.length; i++) {
            String str = splitStr[i];
            if(!"".equals(str)) {
                char firstChar = Character.toUpperCase(str.charAt(0));
                String substring = str.substring(1);
                stringBuilder
                        .append(firstChar)
                        .append(substring);
            }
            if(i != splitStr.length-1)
                stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
}
