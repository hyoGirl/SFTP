package cn.xulei.sftp002;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import cn.xulei.elastic.domain.Bean3;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SingleMySSH {
	
	static long tarStart;
	static long tarEnd;
	
	static long downStart;
	static long downEnd;
	
	public static String exec(String host,String user,String psw,int port,String[] paths){
		//String[] result=null;
		String result="";
		String command="";
		Session session =null;
		ChannelExec openChannel =null;
		InputStream ins=null;
		OutputStream ops=null;
		try {
			// 创建JSch对象
			
			long sshtime=System.currentTimeMillis();
			
			JSch jsch=new JSch();
			// 根据用户名，主机ip，端口获取一个Session对象
			session = jsch.getSession(user, host, port);
			Properties config = new Properties();
			
			//设置为no 就是让客户端自动接受新主机的密钥。密钥修改后也能生效
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPassword(psw);
			//session.setTimeout(10000);
			session.connect();
			
			long ssh=System.currentTimeMillis();
			
			System.out.println("Session connected. session会话链接建立总共花费时间为："+(ssh-sshtime));
			
			tarStart=System.currentTimeMillis();
			
			//^_^ 这个里面字符串是有固定要求的，O_O>"< 源代码里面可以修改^_^
			openChannel = (ChannelExec)session.openChannel("exec");
			
			openChannel.setErrStream(System.err);
			
			int length=paths.length;
			
			String tar="tar -zcvf /root/ss2.tar.gz ";
			
			//"tar -zcvf /root/ss2.tar.gz /home/sftp/test/ss;sleep 20;";
			
			
//			for (int i = 0; i < length; i++) {
//				
//				command+="/home/sftp/ssh/path"+i+".log"+" ";
//				 
//			}
			command="/home/sftp/test/ss";
			String str=tar+" "+command;
			
			System.out.println(command);
			
			openChannel.setInputStream(null);
			
			openChannel.setCommand(str);
			openChannel.connect(); 
			BufferedReader input = new BufferedReader(new InputStreamReader (openChannel.getInputStream()));
            InputStream in = openChannel.getInputStream();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
            String buf = null;
            while ((buf = reader.readLine()) != null) {
            	result+= new String(buf.getBytes("gbk"),"UTF-8")+"    \r\n";  
            }  
            openChannel.disconnect();
			while (!openChannel.isClosed()) {

			}
			result = openChannel.getExitStatus() + "";
			
			
			tarEnd=System.currentTimeMillis();
			
			/**
			 * 开始进行下载测试
			 * 
			 */
			downStart=System.currentTimeMillis();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			
			System.out.println("-------------------");
			ChannelSftp	sftp = (ChannelSftp) channel;
			String directory = "/root/";
			String downloadFile = "ss2.tar.gz";
			String saveFile = "D://JavaTest//sftp//001//";
			//String saveFile = "/home/sftp/001/";
			
			sftp.cd(directory);
			
			sftp.get(downloadFile, saveFile);
			
			downEnd=System.currentTimeMillis();
			
			System.out.println("下载压缩包所花时间是： "+ (downEnd-downStart)+"毫秒");
			
			
		} catch (Exception e) {
			
		}finally{
			
			if(openChannel!=null&&!openChannel.isClosed()){
				openChannel.disconnect();
			}
			if(session!=null&&session.isConnected()){
				session.disconnect();
			}
		}
		return result;
		
		
}
	public static void main(String args[]) {
		
		String host = "192.168.10.16";
		int port = 22;
		String user = "root";
		String pwd = "594065";
		
		
		String path1="/home/sftp/test/001.log";
		String path2="/home/sftp/path2.log";
		String path3="/home/sftp/path3.log";
		/**
		 * 仿照list传值进行测试
		 */
		List<Bean3> list = new ArrayList<Bean3>();
		Bean3 bean = new Bean3();
		list.add( new Bean3("/home/elk/logstash-2.4.0/logtest/sshTest.txt","192.168.10.23"));
		list.add( new Bean3("/home/elk/logstash-2.4.0/logtest/ssh/sshTest.txt","192.168.10.15"));
		list.add( new Bean3("/usr/local/src/elk/logstash-2.4.0/logtest/ssh.txt","192.168.10.17"));
		
		//String[] paths={path1,path2,path3}; 
		String[] paths=new String[100]; 
		//String command="/bin/sh <br/>\r"+shell+" "+param1+" "+param2;
		
		//String command="tar -zcvf /home/sftp/test/ss2.tar.gz /home/sftp/test/ss;sleep 20;";
		//String command="tar -zcvf /home/sftp/test/ss2.tar.gz"+"/home/sftp/test/ss;sleep 20;";
		
	    long start=System.currentTimeMillis();
		String exec = exec(host, user,pwd, port, paths);
		long end=System.currentTimeMillis();
		
		System.out.println("执行的总时间是："+(end-start)+"毫秒");
		System.out.println(exec);
	}
}