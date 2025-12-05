package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a2947_나무_조각 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        int[] woods = new int[5];

        for (int i = 0; i < 5; i++) {
            woods[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        StringBuilder result = new StringBuilder();

        while(isRequireSort(woods)){

            execSort(woods, result);

        }

        System.out.println(result.toString());

    }

    private static void execSort(int[] woods, StringBuilder result) {
        for (int i = 0; i < woods.length - 1; i++) {
            if(woods[i] > woods[i + 1]){
                swap(woods, i);
                result.append(writeArray(woods))
                        .append("\n");
            }
        }
    }

    private static String writeArray(int[] woods) {
        StringBuilder returnStr = new StringBuilder();

        for (int wood : woods) {
            returnStr.append(wood)
                    .append(" ");
        }

        return returnStr.toString();
    }

    private static boolean isRequireSort(int[] woods) {

        for (int i = 0; i < woods.length - 1; i++) {
            if(woods[i] > woods[i + 1]){
                return true;
            }
        }

        return false;
    }

    private static void swap(int[] woods, int i) {
        int temp = woods[i];
        woods[i] = woods[i + 1];
        woods[i + 1] = temp;
    }
}
