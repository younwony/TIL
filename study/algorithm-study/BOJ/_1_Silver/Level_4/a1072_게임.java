package _1_Silver.Level_4;

import java.util.Scanner;

public class a1072_게임 {

	 public static void main(String[] args) {
	      Scanner scanner = new Scanner(System.in);
	      
	      long X=scanner.nextLong();
	      long Y=scanner.nextLong();
	      long Z=100*Y/X;
	      
	      if(Z>=99) {
	         System.out.println(-1);
	      }else {
	         
	    	 long low=0;
	         long mid=0;
	         long high=1000000000;
	         
	         while(low<=high) {
	            mid=(low+high)/2;
	            if(Z<100*(Y+mid)/(X+mid)){
	               high=mid-1;
	            }else {
	               low=mid+1;
	            }
	         }
	         
	         System.out.println(low);
	      }
	      
	      scanner.close();
	   }

}
