package cn.xulei.test;

public class StringTest {
	
	public static void main(String[] args) {
		
		String time="10.129.41.82";
		
		int lastIndexOf = time.lastIndexOf(".");
		
		System.out.println(lastIndexOf);
		
		String ss = time.substring(lastIndexOf+1);
		
		System.out.println(ss);
		
	}
}
