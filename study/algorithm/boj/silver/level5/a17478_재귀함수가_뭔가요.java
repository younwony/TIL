package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class a17478_재귀함수가_뭔가요 {

    private static final String[] LOOP_STR = new String[4];
    private static final String LOOP_TAB = "____";
    private static final String LOOP_LAST_STR = "라고 답변하였지.";
    private static final String ANSWER = "\"재귀함수는 자기 자신을 호출하는 함수라네\"";
    private static StringBuilder result = new StringBuilder();

    static
    {
        LOOP_STR[0] = "\"재귀함수가 뭔가요?\"";
        LOOP_STR[1] = "\"잘 들어보게. 옛날옛날 한 산 꼭대기에 이세상 모든 지식을 통달한 선인이 있었어.";
        LOOP_STR[2] = "마을 사람들은 모두 그 선인에게 수많은 질문을 했고, 모두 지혜롭게 대답해 주었지.";
        LOOP_STR[3] = "그의 답은 대부분 옳았다고 하네. 그런데 어느 날, 그 선인에게 한 선비가 찾아와서 물었어.\"";
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());

        result
                .append("어느 한 컴퓨터공학과 학생이 유명한 교수님을 찾아가 물었다.")
                .append("\n");

        recursive(n);

        System.out.println(result.toString());
    }

    private static void recursive(int n) {
        String tab = "";
        for(int i = 0; i < n; i++){
            tab = getTab(i);
            for (String str : LOOP_STR) {
                result
                        .append(tab)
                        .append(str)
                        .append("\n");
            }
        }

        tab = getTab(n);

        result
                .append(tab)
                .append(LOOP_STR[0])
                .append("\n")
                .append(tab)
                .append(ANSWER)
                .append("\n");

        for(int i = 0; i <= n; i++){
            tab = getTab(n - i);
            result
                    .append(tab)
                    .append(LOOP_LAST_STR)
                    .append("\n");
        }
    }

    private static String getTab(int n) {

        StringBuilder tab = new StringBuilder();

        for(int i = 0; i < n; i++){
            tab.append(LOOP_TAB);
        }

        return tab.toString();
    }
}
