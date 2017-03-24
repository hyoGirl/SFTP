package cn.xulei.ssh.sftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHftp {
	
	public static void main(String[] args) {
		
	}
	
	
	public static void ssh(String ip,String user,String psw,int port) throws Exception{
		Session session=null;
		Channel channel=null;
		
		JSch jsch = new JSch(); // 创建JSch对象
		
		if(port <=0){
			//连接服务器，采用默认端口
			session=jsch.getSession(user, ip);
		}else{
			//采用指定端口连接服务器
			session=jsch.getSession(user, ip, port);
		}
		
		//如果服务器连接不上去，就抛出异常
		if(session == null){
			throw new Exception("session 会话连接异常！！！！");
		}
		
		//设置登陆主机的密码
		
		session.setPassword(psw);
		
		//设置第一次登陆时的提示
		session.setConfig("StrictHostKeyChecking", "no");
		
		//设置登陆超时时间
		
		session.connect(30000);
		
		try {
			//创建sftp通信通道
			channel = (Channel) session.openChannel("sftp");
			
			channel.connect(1000);
			
			ChannelSftp sftp=(ChannelSftp) channel;
			
			//遍历该目录
			Vector ls=sftp.ls("");
			
			//进入服务器指定的文件夹上下两行代码的顺序一定不能乱，否则就会被 2：No Such file的错误
			sftp.cd("");
			
			Iterator iterator = ls.iterator();
			while (iterator.hasNext()) {
				Object next = (Object) iterator.next();
				
				System.out.println(iterator.toString());
				
				String str=next.toString();
				
				if (str.endsWith("zip")) {
					//根据文件名切割字符串
					String subString =str.substring(str.lastIndexOf(""), str.length());
					System.out.println(subString.trim());
					
					File file=new File("D:/sftp"+subString.trim());
					
					sftp.get(subString.trim(), new FileOutputStream(file));
				}
				
				OutputStream outStream =sftp.put("1.txt");
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		} finally{
			
			session.disconnect();
			channel.disconnect();
		}
		
		
	}
}
