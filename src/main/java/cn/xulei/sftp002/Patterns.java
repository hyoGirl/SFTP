package cn.xulei.sftp002;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patterns {
	
	public static void main(String[] args) {
		
		//String str="abddsadsabcdsadsa";
		String str="a*b";
		String regex="^a*.*b$";
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		
		System.out.println(m.find());
//		while(m.find()){
//			System.out.println(m.group());
//		}
		
//		Boolean b=str.matches(regex);
//		System.out.println(b);
	}
}
