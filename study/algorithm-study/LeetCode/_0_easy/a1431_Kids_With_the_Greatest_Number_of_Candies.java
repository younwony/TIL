package _0_easy;

import java.util.ArrayList;
import java.util.List;

public class a1431_Kids_With_the_Greatest_Number_of_Candies {

    public static void main(String[] args) {

    }

    public List<Boolean> kidsWithCandies(int[] candies, int extraCandies) {
        List<Boolean> result = new ArrayList<Boolean>();

        int max = 0;
        for (int candy : candies) {
            max = Math.max(candy, max);
        }

        for (int candy : candies) {
            result.add((candy + extraCandies) >= max);
        }

        return result;
    }
}
