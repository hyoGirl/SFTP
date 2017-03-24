package cn.xulei.sftp002;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class TestIp {

	public static void main(String[] args) {
		
		try {
			Enumeration<NetworkInterface> network = NetworkInterface.getNetworkInterfaces();
			InetAddress ip=null;
			
			while (network.hasMoreElements()) {
				NetworkInterface networkInterface = (NetworkInterface) network.nextElement();
				
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip!=null && ip instanceof Inet4Address) {
						System.out.println("本机器的ip=："+ip.getHostAddress());
					}
					
				}
				
			}
			
			
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
