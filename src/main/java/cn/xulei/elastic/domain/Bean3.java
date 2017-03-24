package cn.xulei.elastic.domain;

public class Bean3 implements java.io.Serializable{
	
	private String path;
	private String host;
	
	

	public Bean3() {
		super();
	}

	public Bean3(String path, String host) {
		super();
		this.path = path;
		this.host = host;
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

	@Override
	public String toString() {
		return "Bean3 [path=" + path + ", host=" + host + "]";
	}
	
}
