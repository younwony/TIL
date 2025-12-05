package _0_easy;

public class a1470_Shuffle_the_Array {
    public static void main(String[] args) {

    }
    public int[] shuffle(int[] nums, int n) {

        int[] result = new int[nums.length];
        for (int i = 0; i < n; i++) {
            result[2*i] = nums[i];      //xi
            result[2*i+1] = nums[i+n];  //yi
        }
        return result;
    }
}
