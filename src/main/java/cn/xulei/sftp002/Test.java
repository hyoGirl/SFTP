package cn.xulei.sftp002;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
	
	public static void main(String[] args) throws IOException {
		
//		String tar="sleep 20;tar -zcvf /home/sftp/test/paths.tar.gz ";
//		
//		
//		String path1="/home/sftp/test/01.log";
//		String path2="/home/sftp/path2.log";
//		String path3="/home/sftp/path3.log";
//		
//		String[] paths={path1,path2,path3}; 
//		
//		String command="";
//		
//		
//		for (int i = 0; i < paths.length; i++) {
//			command+=paths[i]+" ";
//			
//		}
//		
//		String str=tar+command;
//		
//		System.out.println(str);
		
		
		
		String pathname = "E://ELK系统学习治疗//copyFile//20161123.log";
		File file = new File(pathname);
		byte[] buf = new byte[8192];
		int len = 0;
		long start =System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			FileInputStream fis = new FileInputStream(file);
			String path = "E://ELK系统学习治疗//copyFile//test003//path" + i + ".log";
			
			FileOutputStream fos = new FileOutputStream(new File(path));
			while ((len = fis.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
			fis.close();
			fos.close();
		}
		long end=System.currentTimeMillis();
		System.out.println("时间差事："+(end-start));
	}
}
