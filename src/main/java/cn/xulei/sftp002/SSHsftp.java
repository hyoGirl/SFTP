package cn.xulei.sftp002;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHsftp {
	
	
	private static Vector<String> stdout;  
	
	public static Session getSession(String host,String user,String psw,int port){
		
		Session session =null;
		try {
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
			System.out.println("Session connected. session会话链接建立");
			
		} catch (JSchException e) {
			e.printStackTrace();
		}finally{
			
		}
		return session;
	}
	
	public  static String exec(String directory, String downloadFile, String saveFile,String host,String user,String psw,int port,String command){
		ChannelExec ExecChannel =null;
		String result= "";
		ChannelSftp sftp=null;
		Session session =null;
		// int returnCode = 0;
		try {
			session = SSHsftp.getSession(host, user, psw, port);
			//^_^ 这个里面字符串是有固定要求的，O_O>"< 源代码里面可以修改^_^
			ExecChannel = (ChannelExec) session.openChannel("exec");
			ExecChannel.setErrStream(System.err);
			ExecChannel.setCommand(command);
			
			System.out.println("正在执行脚本命令");
			
			
			//怎么判断打包完成！！！！！！！！
			//接收远程服务器执行命令的结果
			
			
			//------------------------------------------
			ExecChannel.setInputStream(null);
			ExecChannel.connect();  
			BufferedReader input = new BufferedReader(new InputStreamReader (ExecChannel.getInputStream()));
			System.out.println("远程脚本命令是: " + command);  
			
//			String line;  
//            while ((line = input.readLine()) != null) {  
//                stdout.add(line);  
//            }  
//            input.close();  
            
            
//            if(ExecChannel.isClosed()){
//            	returnCode = ExecChannel.getExitStatus();
//            	System.out.println(returnCode);
//            }
            
//			if(openChannel!=null&&!openChannel.isClosed()){
//			    openChannel.disconnect();
//		    }
			//------------------------------------------
			 
			 
			
//			int exitStatus = ExecChannel.getExitStatus();
//			
//			System.out.println(exitStatus);
			
			//StringBuilder message = new StringBuilder();
			
			InputStream in = ExecChannel.getInputStream();  
			
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
            
            String buf = null;
            
            while ((buf = reader.readLine()) != null) {
            	result+= new String(buf.getBytes("gbk"),"UTF-8")+"    \r\n";  
            }  
			
            ExecChannel.disconnect();
            
            while(!ExecChannel.isClosed()){
            	
            }
            result=ExecChannel.getExitStatus()+"";
			//Thread.sleep(5000);
			//😀😪^_^^.^O_O^.^
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			sftp.cd(directory);
			Thread.sleep(1000);
			sftp.get(downloadFile, saveFile);
			
		}catch (JSchException e) {
			result+=e.getMessage();
			System.out.println("shell 脚本没有执行完毕");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(ExecChannel!=null&&!ExecChannel.isClosed()){
				ExecChannel.disconnect();
			}
			if(session!=null&&session.isConnected()){
				session.disconnect();
			}
		}
	    return result;
	}
	
//	public static void downloadFile(String directory, String downloadFile, String saveFile,String host,String user,String psw,int port){
//		
//		Session session =null;
//		ChannelSftp sftp=null;
//		try {
//			session = SSHsftp.getSession(host, user, psw, port);
//			Channel channel = session.openChannel("sftp");
//			channel.connect();
//			sftp = (ChannelSftp) channel;
//			sftp.cd(directory);
//			sftp.get(downloadFile, saveFile);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			if(session!=null&&session.isConnected()){
//				session.disconnect();
//				System.out.println("session 关闭");
//			}
//		}
//	}
	
	
	public static void main(String[] args) throws InterruptedException  {
		String host = "192.168.10.16";
		int port = 22;
		String user = "root";
		String pwd = "594065";
		//String command="tar -zcvf /home/sftp/test/ss2.tar.gz /home/sftp/test/ss;sleep 20;";
		String command="tar -zcvf /root/ss2.tar.gz /home/sftp/test/ss;sleep 20;";
		
		//Thread.sleep(10000);
		
		String directory = "/home/sftp/test/";
		String downloadFile = "/home/sftp/test/ss2.tar.gz";
		String saveFile = "D://JavaTest//sftp//download//";
		String exec = exec(directory, downloadFile, saveFile,host, user, pwd, port, command);
		System.out.println(exec);
		//SSHsftp.downloadFile(directory, downloadFile, saveFile, host, user, pwd, port);
		System.out.println("finished");
		
	}
}


