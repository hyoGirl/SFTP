package cn.xulei.JSch;

import java.util.Map;
import java.util.Properties;




import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SFTPChannel {
	Session session = null;
	Channel channel = null;

	//private static final Logger LOG = LoggerFactory.getLogger(SFTPChannel.class);
	
	public static final Logger LOG = Logger.getLogger(SFTPChannel.class.getName());

	public ChannelSftp getChannel(Map<String, String> sftpDetails, int timeout) throws JSchException {

		String ftpHost = sftpDetails.get(SFTPConstants.SFTP_REQ_HOST);
		String port = sftpDetails.get(SFTPConstants.SFTP_REQ_PORT);
		String ftpUserName = sftpDetails.get(SFTPConstants.SFTP_REQ_USERNAME);
		String ftpPassword = sftpDetails.get(SFTPConstants.SFTP_REQ_PASSWORD);

		int ftpPort = SFTPConstants.SFTP_DEFAULT_PORT;
		if (port != null && !port.equals("")) {
			ftpPort = Integer.valueOf(port);
		}

		JSch jsch = new JSch(); // 创建JSch对象
		session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
		LOG.debug("Session created. Session会话创建开始");
		if (ftpPassword != null) {
			session.setPassword(ftpPassword); // 设置密码
		}
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
		LOG.debug("Session connected. session会话链接建立");

		LOG.debug("Opening Channel.正在开通SFTP通道 ");
		channel = session.openChannel("sftp"); // 打开SFTP通道
		channel.connect(); // 建立SFTP通道的连接
		LOG.debug("Connected successfully to ftpHost = " + ftpHost + ",as ftpUserName = " + ftpUserName + ", returning: " + channel);
		LOG.debug("Connected successfully to ftpHost ！！！通道成功链接");
		return (ChannelSftp) channel;
	}

	public void closeChannel() throws Exception {
		if (channel != null) {
			channel.disconnect();
			LOG.debug("通道失去链接");
		}
		if (session != null) {
			session.disconnect();
			LOG.debug("通道失去链接");
		}
	}
}
