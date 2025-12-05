package _0_easy;

import java.util.Arrays;

public class a27_Remove_Element {

    public static void main(String[] args) {
        int[] nums = {3,2,2,3};
        System.out.println(removeElement(nums, 3));
        int[] nums2 = {0,1,2,2,3,0,4,2};
        System.out.println(removeElement(nums2, 2));
    }

    public static int removeElement(int[] nums, int val) {
        int resultIndex = 0;

        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if(num != val) {
                nums[resultIndex++] = num;
            }
        }
        return resultIndex;
    }
}
