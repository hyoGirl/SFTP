package cn.xulei.sftp002;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import cn.xulei.elastic.domain.Bean3;

public class MySSHTest {
	
	
	
	public static void main(String[] args) {
		
		int port = 22;
		String user = "root";
		String pwd = "594065";
		
		List<Bean3> list = new ArrayList<Bean3>();
		//Bean3 bean = new Bean3();
		list.add( new Bean3("/home/elk/logstash-2.4.0/logtest/ssh/ssh23.txt","192.168.10.23"));
		list.add( new Bean3("/home/elk/logstash-2.4.0/logtest/ssh/sshTest.txt","192.168.10.15"));
		list.add( new Bean3("/usr/local/src/elk/logstash-2.4.0/logtest/ssh.txt","192.168.10.17"));
		
		String paths1="";
		String paths2="";
		String paths3="";
		for (Bean3 bean : list) {
			if (bean.getHost().equals("192.168.10.23")) {
				paths1+=bean.getPath()+" ";
				
			}
			if (bean.getHost().equals("192.168.10.17")) {
				paths2+=bean.getPath()+" ";
			}
			if (bean.getHost().equals("192.168.10.15")) {
				paths3+=bean.getPath()+" ";
			}
		}
		System.out.println(paths1);
		long start=System.currentTimeMillis();
		String exec1 = exec("192.168.10.23", user,pwd, port, paths1);
		System.out.println("192.168.10.23 机器上执行结果是： "+exec1);
		
		String exec2 = exec("192.168.10.17", user,pwd, port, paths2);
		System.out.println("192.168.10.17 机器上执行结果是： "+exec2);
		
		String exec3 = exec("192.168.10.15", user,pwd, port, paths3);
		System.out.println("192.168.10.15 机器上执行结果是： "+exec3);
		
		long end=System.currentTimeMillis();
		System.out.println("执行的总时间是："+(end-start)+"毫秒");
	}

	public static String exec(String host, String user, String pwd, int port, String paths) {
		Session session =null;
		ChannelExec openChannel =null;
		InputStream ins=null;
		OutputStream ops=null;
		String result="";
		
		// 根据用户名，主机ip，端口获取一个Session对象
		try {
			long sshtime=System.currentTimeMillis();
			JSch jsch=new JSch();
			session = jsch.getSession(user, host, port);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPassword(pwd);
			//session.setTimeout(10000);
			session.connect();
			long ssh=System.currentTimeMillis();
			
			System.out.println("Session connected. session会话链接建立总共花费时间为："+(ssh-sshtime));
			
			
			long tarStart=System.currentTimeMillis();
			
			openChannel = (ChannelExec)session.openChannel("exec");
			
			openChannel.setErrStream(System.err);
			
			//String tar="tar -zcvf /home/sftp/ssh23.tar.gz ";
			String tar="tar -zcvf";
			
			String file = fileName(host);
			
			String command=tar+" "+file+" "+paths;
			
			System.out.println(command);
			
			//String command=tar;
			
            openChannel.setInputStream(null);
			
			openChannel.setCommand(command);
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
			
			long tarEnd=System.currentTimeMillis();
			
			System.out.println(host+" 打包文件所需要时间是： "+ (tarEnd-tarStart)+"毫秒");
			
			/**
			 * 开始进行下载测试
			 * 
			 */
			long downStart=System.currentTimeMillis();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			
			ChannelSftp	sftp = (ChannelSftp) channel;
			
			String directory = "/home/sftp/";
			
			//String downloadFile ="/home/sftp/ssh170.tar.gz";
			String downloadFile =file;
			
			String saveFile = "D://JavaTest//sftp//003//";
			
			sftp.cd(directory);
			
            sftp.get(downloadFile, saveFile);
			
			long downEnd=System.currentTimeMillis();
			
			System.out.println("下载压缩包所花时间是： "+ (downEnd-downStart)+"毫秒");
			
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			System.out.println("ssh客户端链接失败");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("文件打包异常");
			e.printStackTrace();
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			System.out.println("文件下载异常");
			e.printStackTrace();
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

	private static String fileName(String host) {
		String file="";
		
		if (host.equals("192.168.10.15")) {
			
		   file="/home/sftp/ssh1500.tar.gz";
		}
		if (host.equals("192.168.10.17")) {
			
			file="/home/sftp/ssh1700.tar.gz";
		}
		if (host.equals("192.168.10.23")) {
			
			file="/home/sftp/ssh2300.tar.gz";
		}
		return file;
	}
}
