package _0_easy;

public class a1672_Richest_Customer_Wealth {

    public static void main(String[] args) {

    }

    public int maximumWealth(int[][] accounts) {

        int max = 0;

        for (int[] account : accounts) {
            int sum = 0;
            for (int i : account) {
                sum +=i;
            }
            max = Math.max(max,sum);
        }

        return max;

    }
}
