package cn.xulei.JSch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

public class SFTPGetTest {

	public SFTPChannel getSFTPChannel() {
		return new SFTPChannel();
	}

	public static void main(String[] args) throws Exception {
		SFTPGetTest test = new SFTPGetTest();

		Map<String, String> sftpDetails = new HashMap<String, String>();

		// 设置主机ip，端口，用户名，密码
		sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, "192.168.10.16");
		sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, "root");
		sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, "594065");
		sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, "22");

		SFTPChannel channel = test.getSFTPChannel();
		ChannelSftp chSftp = channel.getChannel(sftpDetails, 60000);
		
		
		

		//String filename = "/home/omc/ylong/sftp/INTPahcfg.tar.gz";
		String filename = "/home/sftp/test/001.tar.gz";
		//String name=new String(filename.getBytes(),"UTF-8");
		SftpATTRS attr = chSftp.stat(filename);
		long fileSize = attr.getSize();

		String dst = "D://JavaTest//sftp//download//2000.tar.gz";
		//String dstname=new String(dst.getBytes(),"UTF-8");
		OutputStream out = new FileOutputStream(new File(dst));
		
		
		try {
			chSftp.get(filename, dst, new FileProgressMonitor(fileSize)); // 代码段1
			
				
//			InputStream is = chSftp.get(filename, new MyProgressMonitor());
//
//			byte[] buff = new byte[8192];
//            int read=0;
//            if (is != null) {
//                System.out.println("Start to read input stream");
//                do {
//                    read = is.read(buff, 0, buff.length);
//                    if (read > 0) {
//                        out.write(buff, 0, read);
//                    }
//                    out.flush();
//                } while (read >= 0);
                
//                while((read=is.read(buff))!=-1){
//                	out.write(buff, 0, read);
//                	out.flush();
//                }
                
                System.out.println("input stream read done.  输入流结束");
//            }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			chSftp.quit();
			channel.closeChannel();

		}

	}
}
