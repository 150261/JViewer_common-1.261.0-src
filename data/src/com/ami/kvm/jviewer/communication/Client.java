/****************************************************************
 **                                                            **
 **    (C) Copyright 2006-2009, American Megatrends Inc.       **
 **                                                            **
 **            All Rights Reserved.                            **
 **                                                            **
 **        5555 Oakbrook Pkwy Suite 200, Norcross,             **
 **                                                            **
 **        Georgia - 30093, USA. Phone-(770)-246-8600          **
 **                                                            **
****************************************************************/

///////////////////////////////////////////////////////////////////////////////
//
// Client base class for Video, cd, ide clients.
//

package com.ami.kvm.jviewer.communication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.gui.JViewerApp;
import com.ami.kvm.jviewer.gui.KVMSharing;
import com.ami.kvm.jviewer.gui.SinglePortKVM;

/**
 * This is the interface used by NWListener.
 * This interface is implemented by Video
 * Read event notifications passed to respective clients from NWListener.
 */
public abstract class Client {

	protected Socket m_sock = null;
	protected Socket m_sock_nonssl = null;
	protected SSLSocket m_sock_ssl = null;
	protected byte[] 		m_ipAddr;
	protected int    		m_port;

	/**
	 * Constructor.
	 *
	 * @param ipAddr server ip address
	 * @param port server port number
	 */
	public Client(byte[] ipAddr, int port)
	{
		m_ipAddr = ipAddr;
		m_port = port;
	}

	/**
	 * Setmethod for socket decscriptor
	 * @param Sock
	 */
	public void SetSocket(Socket Sock)
	{
		this.m_sock = Sock;
	}

	/**
	 * Get socket channel.
	 *
	 * @return the socket channel associated with this client.
	 */
	public Socket getSocket()
	{
		return m_sock;
	}

	/**
	 *
	 * @param useSSL - if its true create a sslsocket otherwise plain socket
	 * @return
	 */
	protected int connectVideo(boolean useSSL) {

		if(useSSL) {
			try {
				if (-1 == connectVideoSSL()) {
					return -1;
	    		}
	    	} catch (IOException e) {
	    		Debug.out.println("Failed to establish connection");
	    		Debug.out.println(e);
	    		return -1;
	    	}
		}
		else{
	    	try {
	    		if (-1 == connectVideoNonSSL()) {
					return -1;
	    		}
	    	} catch (IOException e) {
	    		Debug.out.println("Failed to establish connection");
	    		Debug.out.println(e);
	    		return -1;
	    	}
		}
		return 0;
	}

	/**
	 * Create a plain Socket and Connect to the server.
	 * number of retries expired.
	 *
	 * @return 0 on success, -1 otherwise.
	 */
	public int connectVideoNonSSL() throws IOException {

		m_sock_nonssl = new Socket();
		try
		{
			m_sock_nonssl.connect(new InetSocketAddress(InetAddress
				.getByAddress(m_ipAddr), m_port));
		}
		catch(Exception e)
		{
			Debug.out.println(e);
			return -1;
		}
		SetSocket(m_sock_nonssl);
		String Kvm_own_ip = m_sock.getLocalAddress().toString();

		String[] split = Kvm_own_ip.split("/");
		KVMSharing.KVM_CLIENT_OWN_IP = split[1];
		if (KVMSharing.KVM_CLIENT_OWN_IP.compareToIgnoreCase("0.0.0.0") == 0) {
			KVMSharing.KVM_CLIENT_OWN_IP = getipv6IP();
		}
		Debug.out.println("m_sock.socket().getLocalAddress()"
				+ Kvm_own_ip.toString());
		Debug.out.println("m_sock.socket().getLocalAddress()"
				+ KVMSharing.KVM_CLIENT_OWN_IP);
		return 0;
	}

	/**
	 * Create a SSL Socket and Connect to the server.
	 * number of retries expired.
	 *
	 * @return 0 on success, -1 otherwise.
	 */
	public int connectVideoSSL() throws IOException {

		SSLContext context = null;

		try {
				// Create a trust manager that does not validate certificate chains
				TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs,
						String authType) {
				}
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs,
						String authType) {
				}
			} };

			SSLContext sslCtx = SSLContext.getInstance("SSL");
			sslCtx.init(null, trustAllCerts, new java.security.SecureRandom());
			context = sslCtx;
		}
		catch (NoSuchAlgorithmException e) {
			Debug.out.println(e);
			return -1;
		} catch (KeyManagementException e) {
			Debug.out.println(e);
			return -1;
		}

		SSLSocketFactory sf = context.getSocketFactory();
		try
		{
			m_sock_ssl = (SSLSocket) sf.createSocket(InetAddress
					.getByAddress(m_ipAddr), m_port);
			m_sock_ssl.startHandshake();
			SetSocket(m_sock_ssl);

			String Kvm_own_ip = m_sock_ssl.getLocalAddress().toString();

			String[] split = Kvm_own_ip.split("/");
			KVMSharing.KVM_CLIENT_OWN_IP = split[1];
			if (KVMSharing.KVM_CLIENT_OWN_IP.compareToIgnoreCase("0.0.0.0") == 0) {
				KVMSharing.KVM_CLIENT_OWN_IP = getipv6IP();
			}
			Debug.out.println("m_sock.socket().getLocalAddress()"
					+ Kvm_own_ip.toString());
			Debug.out.println("m_sock.socket().getLocalAddress()"
					+ KVMSharing.KVM_CLIENT_OWN_IP);
		}
		catch (Exception exception)
		{
			Debug.out.println(exception);
			return -1;
		}
		return 0;
	}

	/**
	 * Send given message to server.
	 *
	 * @param msg message to be sent.
	 * @param len message length
	 * @return number of bytes written.
	 */
	public int sendMessage(byte[] msg, int len)
	{
		if(!JViewer.isplayerapp() && !JViewer.isdownloadapp())
		{
		try
		{
			getSocket().getOutputStream().write(msg);
		}
		catch (IOException e)
		{
			Debug.out.println("Send message failed");
			Debug.out.println(e);
			return 0;
		}
		}
		return len;
	}

	/**
	 * Close the Video socket connection.
	 */
	public void VideoSocketclose()
	{
		try
		{
			if(JViewer.isSinglePortEnabled())
			{
				if(getSocket() != null)
					getSocket().close();
			}else{
				if(m_sock_nonssl != null)
					m_sock_nonssl.close();
				if (m_sock_ssl != null)
					m_sock_ssl.close();
			}
		} catch (IOException e)
		{
			Debug.out.println(e);
		}
	}

	public String getipv6IP() {

		Enumeration enumInterfaces = null;
		try {
			enumInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			Debug.out.println(e);
		}
		while (enumInterfaces.hasMoreElements()) {
			NetworkInterface net = (NetworkInterface) enumInterfaces
					.nextElement();

			Debug.out
					.println("NetworkInterface name: " + net.getDisplayName());
			Enumeration enumIP = net.getInetAddresses();
			while (enumIP.hasMoreElements()) {
				InetAddress ip = (InetAddress) enumIP.nextElement();
				InetAddress sample5 = null;
				try {
					sample5 = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					Debug.out.println(e);
				}
				String string6 = sample5.getHostAddress();

				String sample = ip.toString();
				String sample2[] = sample.split("/");
				// String sample3[] = sample2[1].split(".");
				// String hostip = ip.getHostAddress();
				if (string6.compareTo(sample2[1]) == 0) {
					Debug.out.println("CONTINUING LOOP");
					continue;
				}
				Debug.out.println("" + isProperIPv6Address(sample2[1]));
				if (isProperIPv6Address(sample2[1]) == true) {
					Debug.out.println("VALID IPV6IP::" + sample2[1]);
					return sample2[1];
					// System.exit(0);
				}

			}
		}
		return null;

		// return IPV6_IP;
	}

	public static boolean isProperIPv6Address(String address) {

		String sample[] = { "fc80", "fec0", "ff" };
		String ipv[] = address.split(":");

		try {
			InetAddress IP = java.net.Inet6Address.getByName(address);
			if (IP.isLinkLocalAddress() || IP.isLoopbackAddress()) {
				Debug.out.println("Returning false because LOOPBACK");
				return false;
			} else {
				for (int i = 0; i < sample.length; i++) {
					if (ipv[0].compareToIgnoreCase(sample[i]) == 0) {
						Debug.out
								.println("Returning false because RESERVED IP");
						return false;
					}
				}
			}

			return true;
		} catch (Exception e) {
			Debug.out.println(e);
			return false;
		}

	}


	/**
	 * Read notification event handler.
	 */
	public abstract void onReadEvent();
}
