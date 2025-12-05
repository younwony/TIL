package level_1;

public class _소수_만들기 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{1,2,3,4}));
        System.out.println(solution(new int[]{1,2,7,6,4}));
    }

    private static int[] decimal = new int[3000];

    private static int solution(int[] nums) {
        setDecimal();
        int answer = 0;

        int a,b,c;
        for(int i = 0; i < nums.length; i++){
            a = nums[i];
            for(int j = i+1; j < nums.length; j++){
                b = nums[j];
                for(int k = j+1; k < nums.length; k++){
                    c = nums[k];
                    if(decimal[a+b+c] == 0) answer++;
                }
            }
        }
        return answer;
    }

    private static void setDecimal() {
        for(int i = 2; i < Math.sqrt(3000); i++){
            for(int j = i+i; j < decimal.length; j += i){
                decimal[j] = 1;
            }
        }
    }
}
