package _0_easy;

public class a7_reverse_integer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(Integer.valueOf("9646324351"));

	}
	
	public static int reverse(int x) {
		int result = 0;

		try {
			String[] reverse = String.valueOf(Math.abs(x)).split("");
			StringBuilder reverseStr = new StringBuilder();

			if (x < 0)
				reverseStr.append("-");

			for (int i = reverse.length - 1; i >= 0; i--)
				reverseStr.append(reverse[i]);

			result = Integer.parseInt(reverseStr.toString());

		} catch (NumberFormatException e) {
			result = 0;
		}

		return result;
	}
}
