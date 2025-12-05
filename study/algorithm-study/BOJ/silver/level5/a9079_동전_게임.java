package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class a9079_동전_게임 {
    static String H = "HHHHHHHHH";
    static String T = "TTTTTTTTT";

    static int[][] takes = new int[][]{{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(bufferedReader.readLine());
        StringTokenizer stringTokenizer;
        StringBuilder result = new StringBuilder();
        while(T-- > 0){
            String field = "";
            for (int i = 0; i < 3; i++) {
                stringTokenizer = new StringTokenizer(bufferedReader.readLine());
                for (int j = 0; j < 3; j++) {
                    field += stringTokenizer.nextToken();
                }
            }

            result
                    .append(solved(field))
                    .append("\n");
        }
        System.out.println(result.toString());
    }

    private static int solved(String field) {
        int cnt = 0;
        if(isCoin(field)){
            return cnt;
        }

        Queue<Coin> queue = new LinkedList<>();

        Set<String> coinSet = new HashSet<>();
        coinSet.add(field);
        queue.add(new Coin(field,cnt));
        while(!queue.isEmpty()){
            Coin coin = queue.poll();
            for (int[] take : takes) {
                String nextCoin = take(coin.coin, take);
                if(isCoin(nextCoin)){
                    return coin.cnt + 1;
                }
                if(!coinSet.contains(nextCoin)){
                    queue.add(new Coin(nextCoin,coin.cnt+1));
                    coinSet.add(nextCoin);
                }
            }
        }

        return -1;
    }

    private static String take(String coin, int[] index) {
        char[] coins = coin.toCharArray();

        for (int i : index) {
            coins[i] = reverseCoin(coins[i]);
        }

        return new String(coins);
    }

    private static char reverseCoin(char coin) {
        return coin == 'T' ? 'H' : 'T';
    }

    private static boolean isCoin(String field) {
        return field.equals(H) || field.equals(T);
    }

    static class Coin{
        String coin;
        int cnt;

        public Coin(String coin, int cnt) {
            this.coin = coin;
            this.cnt = cnt;
        }
    }
}
