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

package com.ami.kvm.jviewer.gui;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import javax.swing.ImageIcon;
import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.videorecord.URLHandler;


public class OEMResourceURLProcessor extends URLHandler{
	
	private final String oemPath = "/res/oem";
	private final String copyrightFileName = "copyright.txt";
	private final String logoFileName = "jviewerlogo.jpg";
	
	private String serverIp;
	
	
	
	/**
	 * 	Set the session cookie for the HTTP request.
	 * 	@param sessionCookie
	 **/
	public OEMResourceURLProcessor(String sessionCookie, String serverIp){
		sesCookie = sessionCookie;
		this.serverIp = serverIp;
	}
	
	public String getOemCopyright(){
		String copyright = null;
		String url = "http://" + this.serverIp + (this.oemPath.startsWith("/")? "": "/")
								+ this.oemPath + (this.oemPath.endsWith("/")? "": "/")
								+ copyrightFileName;
		byte[] bytes = getOemResourceBytes(url);
		if (bytes != null){
			copyright = new String(bytes);
			Debug.out.println(url + " is available url");
		}
		return copyright;
	}
	
	public ImageIcon getOemLogo(){
		ImageIcon logo = null;
		String url = "http://" + this.serverIp + (this.oemPath.startsWith("/")? "": "/")
								+ this.oemPath + (this.oemPath.endsWith("/")? "": "/")
								+ logoFileName;
		byte[] bytes = getOemResourceBytes(url);
		if (bytes != null){
			logo = new ImageIcon(bytes);
			Debug.out.println(url + " is available url");
		}		
		return logo;
	}
	
	
	public byte[] getOemResourceBytes(String urlStr){
		URL resUrl = null;
		byte[] resBytes = null;
		try{
			resUrl = new URL(urlStr);
			conn = (HttpURLConnection) resUrl.openConnection();
			conn.setRequestProperty("Cookie", "SessionCookie=" + sesCookie);
			conn.setUseCaches(false);
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
				BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
				ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
				int c = 0;
				while ((c = in.read()) != -1){
						byteArrayOut.write(c);
				}
				resBytes = byteArrayOut.toByteArray();
				byteArrayOut.flush();
				byteArrayOut.close();
				in.close();
			}
		}catch (java.net.MalformedURLException ex){
			Debug.out.println(ex);
		}catch (IOException ex1){
			Debug.out.println(ex1);
			ex1.printStackTrace();
		}
		return resBytes;
	}
	
}
