package _0_easy;

import java.util.Arrays;

public class a14_LongestCommonPrefix {

	/**
	 * Example 1:
	 *
	 * Input: strs = ["flower","flow","flight"]
	 * Output: "fl"
	 * Example 2:
	 *
	 * Input: strs = ["dog","racecar","car"]
	 * Output: ""
	 * Explanation: There is no common prefix among the input strings.
	 *
	 * 공통 접두사 찾기
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String[] strs = {"flower","flow","flight"};
		System.out.println(longestCommonPrefix(strs));
	}

	public static String longestCommonPrefix(String[] strs) {
		String result = "";

		if(strs == null || strs.length == 0) {
			return result;
		}

		Arrays.sort(strs);

		String first = strs[0];

		for(int i = 0; i < first.length(); i++) {
			char c = first.charAt(i);
			boolean isSame = true;

			for(int j = 1; j < strs.length; j++) {
				char compareC = strs[j].charAt(i);
				if(c != compareC) {
					isSame = false;
					break;
				}
			}

			if(isSame) {
				result += c;
			} else {
				break;
			}
		}

		return result;
	}
}
