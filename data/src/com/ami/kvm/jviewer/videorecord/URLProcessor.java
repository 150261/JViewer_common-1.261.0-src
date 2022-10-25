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

package com.ami.kvm.jviewer.videorecord;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JOptionPane;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.gui.JViewerApp;
import com.ami.kvm.jviewer.gui.LocaleStrings;

public class URLProcessor extends URLHandler{

	private byte[] data;	
	
	private static final int FILE_LOCKED = -1;
	private static final int FILE_NOT_FOUND = -2;
	private static final int HTTP_REQUEST_SUCCESS = 200;
	public static final int HTTP_REQUEST_FAILURE = -1;
	public static final int INVALID_CREDENTIALS = -6;
	private static final int MAX_BUFFER_SIZE = 4096;
	private static final String DEF_DELIMITER = " ";
	
	private String hostIP = null;
	private String uriString = null;
	private Socket socket = null;
	
	

	/**
	 * Set the session cookie for HTTP request
	 * @param Sessioncookie
	 * @param secureconnect
	 */

	public URLProcessor(String Sessioncookie, int secureConnect) {
		sesCookie = Sessioncookie;
		this.secureConnect = secureConnect;
	}
	
	public int connect_http_lock(String urlPath){

		URL url=null;
		HttpURLConnection lockpath = null;		

		try {
			url = new URL(urlPath);
		} catch (MalformedURLException e1) {			
			Debug.out.println(e1);
		}
		try{			
			lockpath.connect();
		}catch(Exception e)
		{
			Debug.out.println(e);
		}
		if(url == null)
			Debug.out.println("url null");			

		return 0;

	}

	/**
	 * Connect to the URL path specified and return the error code
	 * @param urlPath
	 * @return
	 */
	public int connect_url_lock(String urlPath){

		URL url=null;
		HttpURLConnection lockpath = null;
		int hapistatus = 0;

		try {
			url = new URL(urlPath);
			if(secureConnect == 0)
				lockpath = (HttpURLConnection) url.openConnection();
			else if(secureConnect == 1)
				lockpath = (HttpsURLConnection) url.openConnection();


			lockpath.setDoOutput(true);
			lockpath.setInstanceFollowRedirects(false);
			if (sesCookie != null) {
				lockpath.setRequestProperty("Cookie", "SessionCookie="+sesCookie);
			}
		} catch (MalformedURLException e) {
			Debug.out.println(e);
			if(lockpath != null)
				lockpath.disconnect();
			return HTTP_REQUEST_FAILURE;
		} catch (IOException e) {
			Debug.out.println(e);
			if(lockpath != null)
				lockpath.disconnect();
			lockpath.disconnect();
			return HTTP_REQUEST_FAILURE;
		}
		int responsecode = 0;
		try {
			responsecode = lockpath.getResponseCode();
		}catch(Exception e){
			Debug.out.println(e);
			lockpath.disconnect();
			return HTTP_REQUEST_FAILURE;
		}
		if(responsecode != HTTP_REQUEST_SUCCESS)// if response code is not 200
		{
			JOptionPane.showMessageDialog(JViewerApp.getInstance().getMainWindow(),
					"HTTP "+LocaleStrings.getString("Z_1_URLP"), LocaleStrings.getString("A_5_GLOBAL"),
					JOptionPane.ERROR_MESSAGE);
			lockpath.disconnect();
			return HTTP_REQUEST_FAILURE;
		}

		if(readData( lockpath) == HTTP_REQUEST_FAILURE){
			lockpath.disconnect();
			return HTTP_REQUEST_FAILURE;
		}
		try{
			hapistatus = Integer.parseInt(getValue("HAPI_STATUS:", ' ').trim());
		}catch(NullPointerException ne){
			Debug.out.println(ne);
			lockpath.disconnect();
			return HTTP_REQUEST_FAILURE;
		}
		/**retno from libvideocfg
		 * Setting the status
		 * -1 = File Found already locked
		 * -2 - File not found
		 * Reseting the satus
		 */
		if( hapistatus < 0 )
		{
			if(JViewer.isdownloadapp() || JViewer.isplayerapp()){
				JViewerApp.getInstance().getVideorecordapp().disposeInformationDialog();
				if(hapistatus == FILE_LOCKED)
				{
					JOptionPane.showMessageDialog(JViewerApp.getInstance().getMainWindow(),
							LocaleStrings.getString("Z_2_URLP"), LocaleStrings.getString("A_5_GLOBAL"),
							JOptionPane.ERROR_MESSAGE);
					return FILE_LOCKED;
				}
				else if(hapistatus == FILE_NOT_FOUND)
				{
					JOptionPane.showMessageDialog(JViewerApp.getInstance().getMainWindow(),
							LocaleStrings.getString("Z_3_URLP"), LocaleStrings.getString("A_5_GLOBAL"),
							JOptionPane.ERROR_MESSAGE);
					return FILE_NOT_FOUND;
				}
			}

				if(hapistatus == INVALID_CREDENTIALS){ // invalid user name or password in StandAloneApp.
					lockpath.disconnect();
					return INVALID_CREDENTIALS;
				}

        }
        lockpath.disconnect();
        return 0;
}
                                                                                      
    /**
     * Connect to the webport specified and return all configuration values
     * This method is for Digest Authentication
     * @param uri string
     * @return
     */
	public int connectToWebPort(String hostIP, String uriString, int secureWebPort, String username, String password ){

		this.hostIP = hostIP;
		this.uriString = uriString;
        byte[] serIP = JViewer.getServerIP(hostIP);

		SSLContext context = null;
		try {
		// Create a trust manager that does not validate certificate 
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
       	} 
    	catch (KeyManagementException e) {
    		Debug.out.println(e);
    		return -1;
    	}
    	SSLSocketFactory sf = context.getSocketFactory();
    	try
    	{
    		SSLSocket sslSocket = (SSLSocket) sf.createSocket(InetAddress
    			.getByAddress(serIP), secureWebPort);
    		sslSocket.startHandshake();
    		socket = sslSocket;
    	}
    	catch (Exception exception)
    	{
    		Debug.out.println(exception);
    		return -1;
    	}
    	writeRequestToSocket(socket, null);
    	String data = getResponseHeaderField();
    	
    	 if(data != null){
    	 	String realm = getValueOf(data, "realm=\"", '\"');
			String qop = getValueOf(data, "qop=\"", '\"');
			String nonce = getValueOf(data, "nonce=\"", '\"');
			String opaque = getValueOf(data, "opaque=\"", '\"');
	
		   	String nc = "00000001";
			String cnonce = generateClientnonce();	
			URI uri = null;
			try {
				uri = new URI(uriString);
			} catch (URISyntaxException e2) {
				Debug.out.println(e2);
				return -1;
			}
			String inputHA1 = username+":"+realm+":"+password;
			String HA1 = md5Digest(inputHA1);
			
			String inputHA2 = "GET:"+uri;
			String HA2 = md5Digest(inputHA2);
		    	
			String responseString  = HA1+':'+nonce+':'+nc+':'+cnonce+':'+qop+':'+HA2;
			String authResponse = md5Digest(responseString);
			
			String requestContentHeader = "Authorization: Digest username=\""+username+"\", realm=\""+realm+"\", nonce=\""+nonce+
			"\", uri=\""+uri+"\", response=\""+authResponse+"\", opaque=\""+opaque+
			"\", qop=\""+qop+"\", nc=\""+nc+"\", cnonce=\""+cnonce+"\"";
			
			writeRequestToSocket(socket, requestContentHeader);
			getServerResponseData();
			return 0;
    	 }
    	 else{
    		 return -1;
    	 }
	}

	/**
	 * Reads data from the InputStream of the HTTP connection, into a byte array.  
	 * @param conn - The HTTPConnection object.
	 * @return Returns 0 if reading is successful; -1 otherwise.
	 */
	private int readData(HttpURLConnection conn){
		int bufferSize = MAX_BUFFER_SIZE;//maximum buffer allocated to receive the response;		
		InputStream inStream = null;
		try {
			inStream = new DataInputStream(conn.getInputStream());
		} catch (IOException e) {
			Debug.out.println(e);
			JOptionPane.showMessageDialog(JViewerApp.getInstance().getMainWindow(),
					LocaleStrings.getString("Z_4_URLP"),
					LocaleStrings.getString("A_5_GLOBAL"), JOptionPane.ERROR_MESSAGE);
			return HTTP_REQUEST_FAILURE;
		}

		try {
			data = new byte[bufferSize];
			int offset = 0;

			while (true) {

				int read = 0;
					read = inStream.read(data, offset, data.length - offset);
				if (read <= 0) {
					break;
				}
				offset += read;
			}
		}catch (IOException e) {
			Debug.out.println(e);
			JOptionPane.showMessageDialog(JViewerApp.getInstance().getMainWindow(),
					LocaleStrings.getString("Z_5_URLP"), LocaleStrings.getString("A_5_GLOBAL"), 
					JOptionPane.ERROR_MESSAGE);
			return HTTP_REQUEST_FAILURE;
		}
		finally {
			try {
				inStream.close();
			} catch (IOException e) {
				Debug.out.println(e);
			}
			conn.disconnect();
		}
		return 0;
	}
	/** Reads the connection's InputStream and finds the value of the searched header.
	 *  
	 * @param search - Header value to be searched for.
	 * @param search - delimiter character that comes after the searched value.
	 * @return Returns the value of the searched header.
	 */
	public String getValue(String search, char delimiter){
		String value = null;
		String responseData = new String(data);			
		int start = responseData.indexOf(search);		
		int end = responseData.indexOf(delimiter, start+search.length());
		if(end <0){
			end = responseData.indexOf(DEF_DELIMITER, start+search.length());
		}

		if(start > 0 && end > 0)			{

			value = responseData.substring(start+search.length(), end).trim();				

		} 		
		return value;
	}
	
	public String getValueOf(String source, String search, char endDelimiter){
		String value = null;	
		int start = source.indexOf(search) + search.length();
		int end = source.indexOf(endDelimiter, start);
		value = source.substring(start, end).trim();
		return value;
	}
	
	
	private String md5Digest(String input) {
		String md5 = null;
		if(null == input) return null;
		try {
			//Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");
			//Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());
			//Converts message digest value in base 16 (hex)
			md5 = new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			Debug.out.println(e);
		}
		return md5;
	}
	
	private String generateClientnonce(){
		String cnonce = null;
		long currTime = System.currentTimeMillis();
		Random random = new Random(Integer.MAX_VALUE);
		Random randInt = new Random(Integer.MAX_VALUE);
		Integer nextRand = random.nextInt(Math.abs(randInt.nextInt()));
		cnonce = Long.toHexString(currTime)+Integer.toHexString(nextRand);		
		return cnonce;
	}
	
	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Connect to the URL Path Specified and Downlaod the Video Stream data file
	 * @param urlPath
	 * @return
	 * @throws IOException
	 */

	public int  download(String urlPath) throws IOException {

		URL url=null;
		int responsecode=0;

		try {
			url = new URL(urlPath);
			if(secureConnect == 0)
				conn = (HttpURLConnection) url.openConnection();
			else if(secureConnect == 1)
				conn = (HttpsURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			if (sesCookie != null) {
				conn.setRequestProperty("Cookie", "SessionCookie="+sesCookie);
			}
		} catch (MalformedURLException e) {
			Debug.out.println(e);
			return HTTP_REQUEST_FAILURE;
		} catch (IOException e) {
			Debug.out.println(e);
			return HTTP_REQUEST_FAILURE;
		}


		try{
			responsecode =  conn.getResponseCode();
		}catch(IOException e){
			Debug.out.println(e);
			JOptionPane.showMessageDialog(JViewerApp.getInstance().getMainWindow(),
					"HTTP "+LocaleStrings.getString("Z_1_URLP")+LocaleStrings.getString("Z_6_URLP"),
					LocaleStrings.getString("A_5_GLOBAL"), JOptionPane.ERROR_MESSAGE);
			return HTTP_REQUEST_FAILURE;
		}
		if(responsecode != HTTP_REQUEST_SUCCESS)
		{
			JOptionPane.showMessageDialog(JViewerApp.getInstance().getMainWindow(),
					"HTTP "+LocaleStrings.getString("Z_1_URLP"), LocaleStrings.getString("A_5_GLOBAL"), 
					JOptionPane.ERROR_MESSAGE);
			return HTTP_REQUEST_FAILURE;
		}
		//Add cehck for the HTTP response length

		int len = conn.getContentLength();
		if(len < 0)
		{
			JOptionPane.showMessageDialog(JViewerApp.getInstance().getMainWindow(),
					"HTTP "+LocaleStrings.getString("Z_1_URLP")+LocaleStrings.getString("Z_7_URLP"),
					LocaleStrings.getString("A_5_GLOBAL"), JOptionPane.ERROR_MESSAGE);
			return HTTP_REQUEST_FAILURE;
		}

		InputStream inStream = new DataInputStream(conn.getInputStream());
		try {

			byte[] data = new byte[len];
			int offset = 0;

			while (true) {

				int read = inStream.read(data, offset, data.length - offset);

				if ((read < 0) || (data.length == offset)) {
					break;
				}
				offset += read;
			}

			if (offset < len) {
				throw new IOException("Read"+offset+" bytes : expected" + len+"bytes");
			}
			//write the downlaod video buffer data for processing the video
			JViewerApp.getInstance().getVideorecordapp().setWritedata(data);

			return 0;

		} finally {
			inStream.close();
			conn.disconnect();
		}
	}
	private void writeRequestToSocket(Socket socket, String requestHeader){
    	PrintWriter writer = null;
       	try {
		writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		writer.println("GET "+uriString+" HTTP/1.1");
	    	writer.println("Host: " + hostIP);
	    	writer.println("Accept: */*");
	    	writer.println("User-Agent: Java");
	    	if(requestHeader != null && requestHeader.length() > 0)
	    		writer.println(requestHeader);
	    	writer.println(""); // denotes end of the request.
	    	writer.flush();
		} catch (IOException e) {
			Debug.out.println(e);
			e.printStackTrace();
		}
    }
    
	private void getServerResponseData(){
		BufferedReader reader = null;
		String dataString = "";
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			for (String line = ""; (line = reader.readLine()) != null;) {
				if (line.startsWith("//Dynamic data end")){ // Stop when headers are completed. We're not interested in all the HTML.
					break;
				}
				dataString += line + "\n";
			}
		} catch (IOException e) {
			Debug.out.println(e);
		}
		data = dataString.getBytes();
	}
    
    private String getResponseHeaderField(){
    	String dataString = null;
    	BufferedReader reader = null;
    	String pattern = "WWW-Authenticate:";
    	try {
    		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    		for (dataString = ""; (dataString = reader.readLine()) != null;) {
    			if(dataString.contains("404 Not Found"))
    				return null;
    			if (dataString.startsWith(pattern)){ // Stop when headers are completed. We're not interested in all the HTML.
    				if(!dataString.contains("Digest")){
    					return null;
    				}
    				break;
    			}
    		}
    	} catch (IOException e) {
			Debug.out.println(e);
		}
    	int start = 0;		
    	int end = 0; 
    	if(dataString != null){
    		dataString += "\n";
    		start = dataString.indexOf(pattern) + pattern.length();		
    		end = dataString.indexOf("\n", start);    		
    		dataString = dataString.substring(start, end).trim();
    	}   
    	return dataString;
    }
	
}
