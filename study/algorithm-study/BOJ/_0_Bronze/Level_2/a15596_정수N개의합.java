/**
 * @작성자 wony
 * @작성일 2019. 10. 19.
 * @사용처
 * @Todo
 */

package _0_Bronze.Level_2;

/**
 * @작성자 wony
 * @작성일 2019. 10. 19.
 * @사용처
 * @Todo
 */

public class a15596_정수N개의합 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 10. 19.
	 * @사용처 정수 N개의 합
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test test = new Test();
		
		test.sum(new int[] {1,2});
	}
	
	public static class Test {
	    long sum(int[] a) {
	        long ans = 0;
	        for(int i : a)
	            ans += i;
	        return ans;
	    }
	}

	
}
