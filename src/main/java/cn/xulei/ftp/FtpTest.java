package cn.xulei.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPClient;

public class FtpTest {

	public FtpTest(String userName, String password, String ip) {
		this.userName = userName;
		this.password = password;
		this.ip = ip;
	}

	/**
	 * 上传文件
	 * 
	 * @param File
	 *            f 要上传的文件
	 * @param String
	 *            uploadDir 上传文件的路径
	 * 
	 * @return boolean b 上传结果
	 * */
	public boolean putFile(File f, String uploadDir) {
		InputStream instream = null;
		boolean result = false;
		try {
			try {
				ftpClient.changeWorkingDirectory(uploadDir);
				instream = new BufferedInputStream(new FileInputStream(f));
				result = ftpClient.storeFile(f.getName(), instream);
			} finally {
				if (instream != null) {
					instream.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 从ftp服务器下载文件
	 * 
	 * @param File
	 *            f 要获取的ftp服务器上的文件
	 * @param String
	 *            localPath 本地存放的路径
	 * 
	 * @return boolean 文件下载是否成功
	 * */
	public boolean getFile(File f, String localPath) {
		OutputStream outStream = null;
		boolean result = false;
		try {
			try {
				outStream = new BufferedOutputStream(new FileOutputStream(new File(localPath)));
				String path = f.getPath();
				path = path.replaceAll("\\\\", "/");
				String filepath = path.substring(0, path.lastIndexOf("/") + 1) + "";
				String fileName = path.substring(path.lastIndexOf("/") + 1) + "";
				boolean b = ftpClient.changeWorkingDirectory(filepath);
				if (b) {
					result = ftpClient.retrieveFile(fileName, outStream);
				}
			} finally {
				if (outStream != null) {
					outStream.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取ftp链接
	 * 
	 * @return ftpClient
	 * */
	public FTPClient getFTPClient() {

		try {
			ftpClient = new FTPClient();

			ftpClient.connect(ip);
			ftpClient.login(userName, password);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ftpClient;
	}

	/**
	 * 关闭ftpClient链接
	 * 
	 * @param FTPClient
	 *            要关闭的ftpClient对象
	 * 
	 * */
	public void closeFTPClient(FTPClient ftpClient) {
		try {
			try {
				ftpClient.logout();
			} finally {
				ftpClient.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* ftp用户名 */
	private String userName;
	/* ftp密码 */
	private String password;
	/* ftp服务器ip */
	private String ip;
	private FTPClient ftpClient;
}
