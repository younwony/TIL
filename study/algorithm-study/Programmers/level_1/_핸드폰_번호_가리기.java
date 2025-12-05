package me.wony.level_1;

public class _핸드폰_번호_가리기 {
    public static void main(String[] args) {
        System.out.println(solution("01033334444"));
    }
    private static String solution(String phone_number) {

        StringBuilder stringBuilder = new StringBuilder();
        for(int i =0 ; i < phone_number.length()-4; i++)
            stringBuilder.append("*");

        stringBuilder.append(phone_number.substring(phone_number.length()-4));

        return stringBuilder.toString();

    }
}
