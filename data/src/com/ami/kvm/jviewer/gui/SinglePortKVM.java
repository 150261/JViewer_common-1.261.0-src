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

////////////////////////////////////////////////////////////////////////////////
//
// StandAloneApp connection module
//

package com.ami.kvm.jviewer.gui;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JOptionPane;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.lang.*;

import com.ami.kvm.jviewer.videorecord.URLProcessor;


/**
 * The StandAloneConnectionDialog creates and shows the StandAloneApp connection dialog through which
 * user can provide required information to establish a connection with the Host. 
 */
@SuppressWarnings("serial")
public class SinglePortKVM {	


	private String username = null;
	private String password = null;	
	private String webSessionToken = null;
	private int secWebPort ;	
	private String tunnelHost = null;
	private int tunnelPort ;
	private boolean m_bUseSSL = false;
	private boolean m_bVMUseSSL = false;

	private URLProcessor urlProcessor;

	public OutputStream outStream ;
	public InputStream inStream ;
	public Socket httpsock ;

	private String Service = null;
	public static final String VIDEO = "VIDEO";
	public static final String HTTP = "http";
	public static final String HTTPS = "https";
	public SSLSocket sslsock = null;
	public SSLContext context = null;

	public Socket getHttpsock() {
		return httpsock;
	}

	public void setHttpsock(Socket httpsock) {
		this.httpsock = httpsock;
	}

	public byte reply[] = new byte[200];


	public OutputStream getOutstream() {
		return outStream;
	}

	public void setOutstream(OutputStream outStream) {
		this.outStream = outStream;
	}

	public InputStream getInstream() {
		return inStream;
	}

	public void setInstream(InputStream inStream) {
		this.inStream = inStream;
	}

	public SSLSocket getSSLsock() {
		return sslsock;
	}
	public void setSSLsock(SSLSocket sslsock) {
		this.sslsock = sslsock;
	}
	/**
	 * The COnstructor.
	 * @param parent - The parent frame on which the dialog will be shown.
	 * @param hostIP - The IP address of the host.
	 * @param username - user name to log into the BMC.
	 * @param password - password to log into the BMC.
	 */
	public SinglePortKVM(String tunnelHost, int tunnelPort, int secWebPort,boolean m_bUseSSL) {				

		this.tunnelHost = tunnelHost;
		this.tunnelPort	=	tunnelPort;
		this.secWebPort = secWebPort;	
		this.m_bUseSSL = m_bUseSSL;
	//	this.m_bVMUseSSL = m_bVMUseSSL;
	}	

	public int  startConnect(){	

		if ( 0 > setHTTPConnect(VIDEO)){
			Debug.out.println(LocaleStrings.getString("AE_10_SPKVM"));
			return -1;
		}
		return 0;
	}

	/**
	 * Sets the web session token.
	 */
	public int setHTTPConnect(String service) {

		int ret =0;

		Socket sock =null;
		SSLSocket sslsock = null;
		Service = service;
		setHttpsock(null);
		setOutstream(null);
		setInstream(null);
		try {

			if(false == m_bUseSSL)
			{
				sock = new Socket(tunnelHost, tunnelPort);
				setHttpsock(sock);
				setOutstream(sock.getOutputStream());
				setInstream(sock.getInputStream());
			}
			if(true == m_bUseSSL)
			{
				SSLContext context = null;
				try
				{
					// Create a trust manager that does not validate certificate chains
					TrustManager[] trustAllCerts = new TrustManager[]{
							new X509TrustManager() {
								public java.security.cert.X509Certificate[] getAcceptedIssuers() {
									return null;
								}
								public void checkClientTrusted(
										java.security.cert.X509Certificate[] certs, String authType) {
								}
								public void checkServerTrusted(
										java.security.cert.X509Certificate[] certs, String authType) {
								}
							}
					};

					SSLContext sslCtx = SSLContext.getInstance("SSL");
					sslCtx.init(null, trustAllCerts, new java.security.SecureRandom());

					//HttpsURLConnection.setDefaultSSLSocketFactory(sslCtx.getSocketFactory());
					context = sslCtx;
				}
				catch( NoSuchAlgorithmException e )
				{ 
					Debug.out.println("NoSuchAlgorithmException  exception is thrown");
					Debug.out.println(e);
				}
				catch( KeyManagementException e )
				{ 
					Debug.out.println("KeyManagementException  exception is thrown");
					Debug.out.println(e);
				}
				SSLSocketFactory sf = context.getSocketFactory();

				try
				{
					sslsock = (SSLSocket) sf.createSocket(tunnelHost, tunnelPort);

					sslsock.startHandshake();

					java.security.cert.Certificate[] serverCerts = sslsock.getSession().getPeerCertificates();					
				}catch(Exception e)
				{
					Debug.out.println(e);
				}

				setHttpsock(sslsock);
				setOutstream(sslsock.getOutputStream());
				setInstream(sslsock.getInputStream());
			}
			
			ret = doTunnelHandshake(tunnelHost, secWebPort);
			if(ret < 0 ){
				Debug.out.println(LocaleStrings.getString("AE_10_SPKVM"));
			}

		} catch (UnknownHostException e) {

			Debug.out.println(e);
			ret = -1;
		} catch (IOException e) {

			Debug.out.println(e);
			ret =-1;
		}		
		catch(Exception e)
		{
			Debug.out.println(e);
			ret =-1;
		}	
		return ret;
	}

	/**
	 * Disconnect connection
	 */
	public void DisconnectService(Socket sock) {

		String HttpReq = null;
		int data = 0;

		HttpReq = "JVIEWER DISCONNECT Cookie "
			+ JViewerApp.getInstance().getM_webSession_token()
			+ "\r\n\r\n";		

		if(WriteToConnectedSock(HttpReq) == 0){
			HttpReq = null;
		}
		else{
			Debug.out.println(LocaleStrings.getString("AE_11_SPKVM"));
		}

		try {
			data = ReadFromConnectedSock( );
		} catch (IOException e) {
			Debug.out.println(e);
		}
		if(data < 0 )
			return ;

		return ;

	}

	/**
	 * Makes tunnel through WebServert. 
	 */
	private int doTunnelHandshake( String host, int port) throws IOException
	{
		int data = 0;
		String HttpReq = null;

		HttpReq = FormHttpRequest(" ",host,port);	

		if(WriteToConnectedSock(HttpReq) == 0){
			HttpReq = null;
		}
		else{
			Debug.out.println(LocaleStrings.getString("AE_10_SPKVM"));
			return -1;
		}

		HttpReq = "JVIEWER "+Service+ " cookie "
		+ JViewerApp.getInstance().getM_webSession_token()
		+ "\r\n\r\n";


		if(WriteToConnectedSock(HttpReq) == 0){
			HttpReq = null;
		}
		else{
			Debug.out.println(LocaleStrings.getString("AE_10_SPKVM"));
			return -1;
		}

		data = ReadFromConnectedSock( );
		if(data < 0 )
			return -1;

		/* tunneling Handshake was successful! */
		return 0;
	}



	/**
	 * Forms Http Connect Request. 
	 */
	private String FormHttpRequest(String type,String host, int port) {

		String HttpReq = null; 
		String Protocol= null;
		
		if(true == m_bUseSSL)
			Protocol = " HTTPS/1.1\n";
		else
			Protocol = " HTTP/1.1\n";
		
		HttpReq = "CONNECT"+ type + host + ":" + port + Protocol
		//+ "Proxy-Authorization:Basic "
		//+ "authorization:basic "
		//+ username
		//+ ":"
		//+ password	
		+" cookie "
		+ JViewerApp.getInstance().getM_webSession_token()
		//+ " "
		+ "\r\n\r\n";
		//+ "\n";

		return HttpReq;
	}

	/*Parse error code from web respone
	 * NOTe :- ERROR String should be in the following format "ERROR:12\\r\\n"
	 */
	public String GetErrorCode(String ErrMsg) throws IOException{
		String Err = null;
		int loc = -1;

		if(null == ErrMsg)
			return Err;

		StringBuffer ErrStr   = new StringBuffer ((String) ErrMsg);

		for ( loc = 0; loc < ErrStr.length(); loc++){
			if (ErrStr.charAt (loc) == '\\')
				break;
		}

		try{
			if(loc > 0)
			{
				Err = "AE_"+ErrMsg.substring((ErrMsg.indexOf(":")+1),loc)+"_SPKVM";
			}
		}catch(Exception e){
			Debug.out.println(e);
		}

		return Err;
	}

	/**
	 * read data To Connected Sock.
	 * @throws IOException 
	 */
	public int ReadFromConnectedSock() throws IOException{

		/*
		 * We need to store the reply so we can create a detailed
		 * error message to the user.
		 */

		int            replyLen = 0;
		int            newlinesSeen = 0;
		boolean        headerDone = false;     /* Done on first newline */
		String ErrMsg = null;

		while (newlinesSeen < 1) {

			int i = getInstream().read();		
			if (i < 0) {
				return i;
				//throw new IOException("Unexpected EOF from proxy");
			}		
			if(0 == getInstream().available()){
				headerDone = true;
				++newlinesSeen;
			} else if (i != '\r') {
				newlinesSeen = 0;
				if (!headerDone && replyLen < reply.length) {
					reply[replyLen++] = (byte) i;
				}
			}
		}	
		/*
		 * Converting the byte array to a string is slightly wasteful
		 * in the case where the connection was successful, but it's
		 * insignificant compared to the network overhead.
		 */
		String replyStr = null;
		try {
			replyStr = new String(reply, 0, replyLen, "ASCII7");
		} catch (UnsupportedEncodingException ignored) {
			Debug.out.println(ignored);
			replyStr = new String(reply, 0, replyLen);
		}

		/* We check for Connection Established because our proxy returns 
		 * HTTP/1.1 instead of 1.0 */
		if(replyStr.contains("ERROR") == true){ 
			try{
				ErrMsg = LocaleStrings.getString(GetErrorCode(replyStr));
			}catch(Exception e){
				Debug.out.println(e);
				ErrMsg = LocaleStrings.getString("AE_4_SPKVM");
			}

			JOptionPane.showMessageDialog(JViewerApp.getInstance().getMainWindow(), 
					ErrMsg,LocaleStrings.getString("AE_10_SPKVM"),JOptionPane.INFORMATION_MESSAGE);
			JViewerApp.getInstance().getM_frame().windowClosed();
		}	
		Debug.out.dump(replyStr.getBytes());
		return replyLen;
	}

	/**
	 * Write data To Connected Sock.
	 */
	public int WriteToConnectedSock(String writedata){

		byte b[];

		try {
			/*
			 * We really do want ASCII7 -- the http protocol doesn't change
			 * with locale.
			 */
			b = writedata.getBytes("ASCII7");
		} catch (UnsupportedEncodingException ignored) {
			/*
			 * If ASCII7 isn't there, something serious is wrong, but
			 * Paranoia Is Good (tm)
			 */
			Debug.out.println(ignored);
			b = writedata.getBytes();
		}		

		try {
			getOutstream().write(b);
			getOutstream().flush();

		} catch (IOException e) {
			Debug.out.println(e);
			return -1;
		}
		return 0;
	}

	/** 
	 * Logout the web session once all the required configuration data are obtained.
	 */

	public void logoutWebSession(){	
		urlProcessor = new URLProcessor(webSessionToken, 1);
		int ret = urlProcessor.connect_url_lock("https://"+this.tunnelHost+":"+secWebPort+"/rpc/WEBSES/logout.asp");
		if(ret != 0){
			Debug.out.println(LocaleStrings.getString("AE_12_SPKVM"));
		}
	}

}

