package _0_easy;

public class a1480_Running_Sum_of_1d_Array {
    public static void main(String[] args) {

        a1480_Running_Sum_of_1d_Array solution = new a1480_Running_Sum_of_1d_Array();
        System.out.println(solution.runningSum(new int[]{1,2,3,4}));

    }

    public int[] runningSum(int[] nums) {

        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            sum += num;
            nums[i] = sum;
        }
        return nums;
    }
}
