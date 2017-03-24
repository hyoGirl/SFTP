package cn.xulei.elastic.domain;

public class Bean2 implements java.io.Serializable{
	private String message;
	private String path;
	private String host;
	private String type;
	private String logdate;
	private String tradeNo;
	private String level;
	private String logs;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLogdate() {
		return logdate;
	}
	public void setLogdate(String logdate) {
		this.logdate = logdate;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getLogs() {
		return logs;
	}
	public void setLogs(String logs) {
		this.logs = logs;
	}
	@Override
	public String toString() {
		return "Bean2 [message=" + message + ", host="
				+ host + ", tradeNo=" + tradeNo + ", logs="
				+ logs + "]";
	}
	
	
	
	
}
