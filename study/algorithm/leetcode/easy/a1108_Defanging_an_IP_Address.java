/**
 * @작성자 wony
 * @작성일 2020. 3. 23.
 * @사용처
 *
 */
package _0_easy;

/**
 * @작성자 wony
 * @작성일 2020. 3. 23.
 * @사용처
 * 
 */
public class a1108_Defanging_an_IP_Address {

	/**
	 * @작성자 wony
	 * @작성일 2020. 3. 23.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(defangIPaddr("1.1.1.1"));
		System.out.println(defangIPaddr("255.100.50.0"));
	}
	
	public static String defangIPaddr(String address) {
        
		return address.replace(".", "[.]");
    }

}
