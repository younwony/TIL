package _0_easy;

import java.util.Arrays;

public class a26_Remove_Duplicates_from_Sorted_Array {

    public static void main(String[] args) {
        int[] nums = {1,1,2};
        System.out.println(removeDuplicates(nums));
    }

    public static int removeDuplicates(int[] nums) {
        int result = 0;

        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if(i == 0) {
                result++;
            } else {
                if(num != nums[i-1]) {
                    result++;
                    nums[result-1] = num;
                }
            }
        }
        return result;
    }
}
