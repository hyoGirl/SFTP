package cn.xulei.sftp002;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class MySFTP {

	/**
	 * 说明：开通SFTP服务
	 * @param host
	 * @param port
	 * @param usernamec
	 * @param password
	 * @return
	 * @author 徐磊
	 * @time：2017年1月16日 下午5:11:47
	 */
	public ChannelSftp connect(String host, int port, String username, String password) {
		ChannelSftp sftp = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			Session sshSession = jsch.getSession(username, host, port);
			System.out.println("Session created. Session会话创建开始");
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			System.out.println("Session connected. session会话链接建立");
			System.out.println("Opening Channel.正在开通通道");
			//^_^ 这个里面字符串是有固定要求的，O_O>"< 源代码里面可以修改^_^
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			System.out.println("Connected to " + host + ".");
			System.out.println("Connected successfully to ftpHost ！！！通道成功链接");
		} catch (Exception e) {

		}
		return sftp;
	}

	/**
	 * 说明：上传文件
	 * @param directory 虚拟机文件目录
	 * @param uploadFile 本机需要上传的文件
	 * @param sftp
	 * @author 徐磊
	 * @time：2017年1月16日 上午10:19:17
	 */
	public void upload(String directory, String uploadFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			File file = new File(uploadFile);
			String name = file.getName();
			System.out.println(name);
			sftp.put(new FileInputStream(file), file.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 说明：下载文件到文件
	 * @param directory  虚拟机上目录
	 * @param downloadFile  虚拟机需要下载的文件
	 * @param saveFile 本机需要保存的文件
	 * @param sftp
	 * @author 徐磊
	 * @time：2017年1月16日 上午10:19:46
	 */
	@SuppressWarnings("resource")
	public void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			File file = new File(saveFile);
			FileOutputStream fos = new FileOutputStream(file);
			// FileOutputStream fos = new FileOutputStream(new
			// File("D://test//test002//1.log"));
			System.out.println(fos);
			sftp.get(downloadFile, fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 说明：下载文件到目录
	 * 
	 * @param directory
	 * @param downloadFile
	 * @param saveFile
	 * @param sftp
	 * @author 徐磊
	 * @time：2017年1月16日 下午5:04:42
	 */
	public void downloadFile(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			sftp.get(downloadFile, saveFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 删除文件
	 * 
	 * @param directory
	 *            虚拟机文件目录
	 * @param deleteFile
	 *            要删除的文件
	 * @param sftp
	 */
	public void delete(String directory, String deleteFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 说明：列出目录下的文件
	 * 
	 * @param directory
	 * @param sftp
	 * @return
	 * @throws SftpException
	 * @author 徐磊
	 * @time：2017年1月16日 上午10:20:57
	 */
	public Vector listFiles(String directory, ChannelSftp sftp) throws SftpException {
		return sftp.ls(directory);
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		MySFTP sf = new MySFTP();
		String host = "192.168.10.16";
		int port = 22;
		String username = "root";
		String password = "594065";
		// 虚拟机文件目录
		String directory = "/home/sftp/test/";

		// 本机需要上传的文件
		// String uploadFile = "‪D:\\JavaTest\\sftp\\sftptest.log";
		String uploadFile = "E://ELK系统学习治疗//copyFile//test002//1.log";

		// String uploadName=new String(uploadFile.getBytes(),"UTF-8");
		// 虚拟机下需要下载的文件
		String downloadFile = "/home/sftp/test/2.tar.gz";
		// 本机下载后保存的文件
		// String saveFile = "D://JavaTest//sftp//download//2017.txt";
		String saveFile = "D://JavaTest//sftp//download//";
		// String path = "E://ELK系统学习治疗//copyFile//test002//1.log";
		// String saveFile = "‪‪D://JavaTest//sftp//download//download.txt";

		// String saveFileName=new String(saveFile.getBytes(),"UTF-8");

		// 虚拟机上删除的文件
		String deleteFile = "delete.txt";
		ChannelSftp sftp = sf.connect(host, port, username, password);

		// sf.upload(directory, uploadFile, sftp);
		//sf.download(directory, downloadFile, saveFile, sftp);
		 sf.downloadFile(directory, downloadFile, saveFile, sftp);
		// sf.delete(directory, deleteFile, sftp);

		try {
			//sf.execShell();
			// sftp.cd(directory);
			// sftp.mkdir("ss");
			System.out.println("finished");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sftp.quit();
		}
	}
}
