package level_2;

public class _조이스틱 {

    public static void main(String[] args) {
        /*System.out.println(solution("JEROEN"));
        System.out.println(solution("JAN"));
        System.out.println(solution("BBBAAAB"));
        System.out.println(solution("ABABAAAAABA"));*/
        System.out.println(solution("CANAAAAANAN"));
    }

    private static int solution(String name) {
        int answer = 0;
        String startName = name.replaceAll(".", "A");

        int nameIndex = 0;
        while(true){
            char charSet = name.charAt(nameIndex);
            answer += Math.min(91-charSet, charSet-65);
            startName = startName.substring(0, nameIndex) + charSet + startName.substring(nameIndex+1);

            if(startName.equals(name)) break;

            answer++;
            int preCusorPoint = nameIndex+1;
            int postCusorPoint = nameIndex-1;
            while(true){
                if(preCusorPoint == name.length()) preCusorPoint = 0;
                if(name.charAt(preCusorPoint) != startName.charAt(preCusorPoint)){
                    nameIndex = preCusorPoint;
                    break;
                }
                if(postCusorPoint == -1) postCusorPoint = name.length()-1;
                if(name.charAt(postCusorPoint) != startName.charAt(postCusorPoint)){
                    nameIndex = postCusorPoint;
                    break;
                }
                answer++;
                preCusorPoint++;
                postCusorPoint--;
            }

        }

        return answer;
    }
}
