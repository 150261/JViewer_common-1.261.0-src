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
// Debug message module
//



package com.ami.kvm.jviewer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Debug message control module
 */
public class Debug {
	public static Debug out = new Debug();
	public static int MODE;

	public static final int CREATE_LOG = 0;
	public static final int DEBUG = 1;
	public static final int RELEASE = 2;
	private File logFile = null;
	private FileWriter fWriter = null;
	private PrintWriter pWriter = null;

	/**
	 * Constructor
	 */
	private Debug() {
 		MODE = RELEASE;
		if(MODE == CREATE_LOG){
			logFile = new File(System.getProperty("user.home")+
					File.separator+"JViewer_log_to-"+JViewer.getIp()+"-"+getTimeStamp()+".txt");
			try {
				fWriter = new FileWriter(logFile, true);
			} catch (IOException e) {
				System.err.println("ERROR WHILE CREATING LOG FILE!!!");
				e.printStackTrace();
				fWriter = null;
			}
		}
	}

	/**
	 * Print given object with.
	 */
	public void print(Object e) {
		if (MODE == DEBUG) {
			//print exception stack trace
			if(e.getClass().getName().contains("Exception")){
				Exception except = (Exception) e;
				System.err.println("\nEXCEPTION !!!");
				except.printStackTrace();
			}
			else
				System.out.print(e);
		}
		else if(MODE == CREATE_LOG){
			try {
				if(fWriter == null)
					fWriter = new FileWriter(logFile, true);
				//log exception stack trace
				if(e.getClass().getName().contains("Exception")){
					Exception except = (Exception) e;
					if(pWriter == null)
						pWriter = new PrintWriter(fWriter, true);
					pWriter.append("\nEXCEPTION !!!\n");
					except.printStackTrace(pWriter);
					pWriter.append('\n');
				}
				else
					fWriter.append((CharSequence) e);
			} catch (IOException ioe) {
				System.err.println("ERROR WHILE WRITTING TO LOG FILE!!!");
				ioe.printStackTrace();
				closeLog();
			}
			catch(ClassCastException cce){
				try {
					fWriter.append((CharSequence)e.toString());
				} catch (IOException ioe) {
					System.err.println("ERROR WHILE WRITTING TO LOG FILE!!!");
					ioe.printStackTrace();
					closeLog();
				}

			}
		}
	}

	/**
	 * Print given object with new line.
	 */
	public void println(Object e) {
		if (MODE == DEBUG) {
			//print exception stack trace
			if(e.getClass().getName().contains("Exception")){
				Exception except = (Exception) e;
				System.err.println("\nEXCEPTION !!!");
				except.printStackTrace();
			}
			else
				System.out.println(e);
		}
		else if(MODE == CREATE_LOG){
			try {
				if(fWriter == null)
					fWriter = new FileWriter(logFile, true);
				//log exception stack trace
				if(e.getClass().getName().contains("Exception")){
					Exception except = (Exception) e;
					if(pWriter == null)
						pWriter = new PrintWriter(fWriter, true);
					pWriter.append("\nEXCEPTION !!!\n");
					except.printStackTrace(pWriter);
					pWriter.append('\n');
				}
				else{
					fWriter.append((CharSequence) e);
					fWriter.append('\n');
				}
			} catch (IOException ioe) {
				System.err.println("ERROR WHILE WRITTING TO LOG FILE!!!");
				ioe.printStackTrace();
				closeLog();
			}
			catch(ClassCastException cce){
				try {
					fWriter.append((CharSequence)e.toString());
				} catch (IOException ioe) {
					System.err.println("ERROR WHILE WRITTING TO LOG FILE!!!");
					ioe.printStackTrace();
					closeLog();
				}

			}
		}
	}

	/**
	 * Print hex dump
	 *
	 * @param buffer byte array to be printed
	 */
	public void dump(byte[] buffer) {
		if (MODE == DEBUG || MODE == CREATE_LOG) {
			dump(buffer, buffer.length);
		}
	}

	/**
	 * Print hex dump
	 *
	 * @param buffer byte array to be printed
	 * @param length buffer length
	 */
	public void dump(byte[] buffer, int length) {
		if (MODE == DEBUG || MODE == CREATE_LOG) {
			dump(buffer, 0, length);
		}
	}

	/**
	 * Print hex dump
	 *
	 * @param buffer byte array to be printed
	 * @param offset offset in byte array
	 * @param length buffer length
	 */
	public void dump(byte[] buffer, int offset, int length) {
		if (MODE == DEBUG || MODE == CREATE_LOG) {
			if ((buffer.length - offset) < length) {
				println("Invalid buffer");
				return;
			}

			StringBuffer hexString = new StringBuffer(2 * length);

			// convert byte array to hex string
			for (int i = offset; i < (offset + length); i++) {

				appendHexPair(buffer[i], hexString);
			}

			// print hex string
			for (int j = 0; j < hexString.length(); j += 60) {

				int end = j + 60;
				if (end > hexString.length()) {
					end = hexString.length();
				}

				println(hexString.substring(j, end));
			}
		}
	}


	/**
	 * Get Hexadicimal string representation of the IPMI response
	 * @param buffer byte array to be printed
	 * @param offset offset in byte array
	 * @param length buffer length
	 * @return Hexadicimal string representation of the IPMI response
	 */
	public String dumpIPMI(byte[] buffer, int offset, int length) {
		String ipmiData = "";

		if ((buffer.length - offset) < length) {
			println("Invalid buffer");
			return null;
		}

		StringBuffer hexString = new StringBuffer(2 * length);

		// convert byte array to hex string
		for (int i = offset; i < (offset + length); i++) {

			appendHexPair(buffer[i], hexString);
		}
		String tempString = hexString.substring(3, hexString.length());
		for(int i = 0; i < tempString.length(); i+=48){
			int end = i + 48;
			if (end > tempString.length()){
				end = tempString.length();
			}
			ipmiData += tempString.substring(i, end) + "\n";
		}
		return ipmiData;
	}
	/**
	 * append given byte to hex string
	 *
	 * @param b byte
	 * @param hexString hex string
	 */
	public static void appendHexPair(byte b, StringBuffer hexString) {

		char highNibble = kHexChars[(b & 0xF0) >> 4];
		char lowNibble = kHexChars[b & 0x0F];

		hexString.append(highNibble);
		hexString.append(lowNibble);
		hexString.append(' ');
	}

	public String getTimeStamp(){
		String timeStamp = "";
		Date currDate = new Date();
		SimpleDateFormat dateFormater = new SimpleDateFormat("'on_'dd-MM-yyyy'_at_'HH-mm-ss-SSS");
		timeStamp =  dateFormater.format(currDate);
		return timeStamp;
	}
	public void closeLog(){
		try {
			if(pWriter != null )
				pWriter.close();
			pWriter = null;
			if(fWriter != null)
				fWriter.close();
			fWriter = null;
		} catch (IOException ioe) {
			System.err.println("ERROR WHILE CLOSING LOG FILE WRITTER!!!");
			pWriter = null;
			fWriter = null;
			ioe.printStackTrace();
		}
	}
	private static final char kHexChars[] =
	{ '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F' };

}