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

package com.ami.iusb;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.ami.iusb.protocol.CDROMProtocol;
import com.ami.iusb.protocol.IUSBHeader;
import com.ami.iusb.protocol.IUSBSCSI;
import com.ami.iusb.protocol.PacketMaster;
import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.gui.InfoDialog;
import com.ami.kvm.jviewer.gui.JViewerApp;
import com.ami.kvm.jviewer.gui.LocaleStrings;
import com.ami.kvm.jviewer.gui.StandAloneConnectionDialog;
import com.ami.kvm.jviewer.kvmpkts.IVTPPktHdr;

/**
 * A <code>CDROMRedir</code> object contains all the information and threads
 * to perform CDROM redirection. It also provides some informative accessor
 * methods.<BR>
 * <BR>
 *
 * Starting redirection via {@link #startRedirection} starts the cdrom
 * redirection thread, and handles all incoming requests for cdrom information
 * and sends the responses. This thread is stopped via {@link #stopRedirection}.
 * <BR>
 * <BR>
 *
 * The actual interaction with the CD-ROM drive takes place inside native
 * methods that we access via JNI. We do all the network I/O in java, and pass
 * the SCSI requests to the native code. We pass the response right back to the
 * remote cdserver.
 *
 */
public class CDROMRedir extends Thread {

	private PacketMaster 	packetMaster;
	private CDROMProtocol 	protocol;
	private ByteBuffer		packetReadBuffer;
	private ByteBuffer 		packetWriteBuffer;
	private boolean 		physicalDrive;
	private int  			CDDevice_no;
	private boolean 		running = false;
	private boolean 		stopRunning = false;
	private long 			nativeReaderPointer = -1;
	private Thread 			redirThread;
	private String 			sourceCDROM = null;
	private int 			nBytesRedirected = 0;

	private static final int DEVICE_REDIRECTION_ACK = 0xf1;
	private static final int AUTH_CMD = 0xf2;
	private static final int CONNECTION_ACCEPTED = 1;
	private static final int CONNECTION_PERM_DENIED = 5;
	private static final int CONNECTION_MAX_USER = 8;
	private static final int MAX_READ_SECTORS = 0x40;
	private static final int MAX_READ_SIZE = 2048 * MAX_READ_SECTORS;
	private static final int MAX_READ_DATA_SIZE = 1024;
	private static final String IOERRMSG = LocaleStrings.getString("4_1_CDROMREDIR");
	private  boolean cdImageEjected = false;
	private  boolean cdImageRedirected = false;
	private vCDMonitorThread vMThread = null;
	private int cdInstanceNum;
	
	// Native library call
	private native String[] listCDROMDrives();
	private native void newCDROMReader(boolean physicalCD);
	private native void deleteCDROMReader();
	private native boolean openCDROM(byte[] a);
	private native void closeCDROM();
	private native int executeCDROMSCSICmd(ByteBuffer scsiRequest, ByteBuffer scsiResponse);
	private native String getVersion();
	private static Object syncObj = new Object();
	private  boolean confModified = false;
	private  boolean cdRedirectionKilled = false;

	
	/***
	 *
	 * Loading the Library for Acccesing the CDROM
	 */
		
	static {
		try {
			if( !JViewer.isdownloadapp() && !JViewer.isplayerapp()){
				if(JViewer.isjviewerapp()){ // Normal JViwer
					System.loadLibrary("javacdromwrapper");
				}
				else { //For SPXMultiViewer and StandAloneApp
					loadWrapperLibrary();
				}
			}
		} catch (UnsatisfiedLinkError e) {
			System.err.println(LocaleStrings.getString("4_2_CDROMREDIR"));
		}
	}

	/**
	 * Loads the javawrapper library files. 
	 */
	private static void loadWrapperLibrary(){
		String libPath = null;
		File libFile = null;
		String commonPath = File.separator+"Jar"+File.separator+
							JViewer.getIp()+File.separator+"lib"+File.separator;
		//Replace all ':' characters from the common path with '_'. This is because in Windows file system,
		//file and directory names and are not allowed to contain ':'. In this case the getIP() method
		//might return IPV6 address which will contain : and will lead to error.
		if(System.getProperty("os.name").startsWith("Windows")){
			commonPath = StandAloneConnectionDialog.replaceAllPattern(commonPath, ":", "_");
			libPath = System.getProperty("user.dir")+commonPath+"javacdromwrapper.dll";
			libFile = new File(libPath);
			//extract the library files only if its not already existing
			if(!libFile.exists())
				StandAloneConnectionDialog.getWrapperLibrary("javacdromwrapper.dll");
		}
		else if(System.getProperty("os.name").startsWith("Linux")){
			if(JViewer.isStandAloneApp()){
				//Get the current working path of the JViewer, in the case of StandAloneApp to load the libraries
				String currPath = JViewer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				currPath = currPath.substring(0, currPath.lastIndexOf('/'));
				//If there is any white space in a directory name, it will be represented 
				//as %20 in the currPath, in Linux and Mac file system. It should replaced with a '\'. 
				if(currPath.contains("%20")){
					currPath = currPath.replaceAll("%20", "\\ ");
				}
				libPath = currPath+commonPath+"libjavacdromwrapper.so";
				libFile = new File(libPath);
				//extract the library files only if its not already existing
				if(!libFile.exists())
					StandAloneConnectionDialog.getWrapperLibrary("libjavacdromwrapper.so");
			}
			else
				libPath = System.getProperty("user.dir")+commonPath+"libjavacdromwrapper.so";
		}
		else if(System.getProperty("os.name").startsWith("Mac")){
			if(JViewer.isStandAloneApp()){
				//Get the current working path of the JViewer, in the case of StandAloneApp to load the libraries
				String currPath = JViewer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				currPath = currPath.substring(0, currPath.lastIndexOf('/'));
				//If there is any white space in a directory name, it will be represented 
				//as %20 in the currPath, in Linux and Mac file system. It should replaced with a '\'. 
				if(currPath.contains("%20")){
					currPath = currPath.replaceAll("%20", "\\ ");
				}
				libPath = currPath+commonPath+"libjavacdromwrapper.jnilib";
				libFile = new File(libPath);
				//extract the library files only if its not already existing
				if(!libFile.exists())
					StandAloneConnectionDialog.getWrapperLibrary("libjavacdromwrapper.jnilib");
			}
			else
				libPath = System.getProperty("user.dir")+commonPath+"libjavacdromwrapper.jnilib";
		}

		System.load(libPath);
	}

	/**
	 * Creates a new instance of CDROMRedir
	 *
	 * @param physicalDrive
	 *            True for actual cdrom drives, false for iso images
	 */
	public CDROMRedir(boolean physicalDrive) {
		this.physicalDrive = physicalDrive;
		protocol = new CDROMProtocol();
		packetReadBuffer = ByteBuffer.allocateDirect(MAX_READ_DATA_SIZE);
		packetWriteBuffer = ByteBuffer.allocateDirect(MAX_READ_SIZE	+ IUSBHeader.HEADER_LEN
				+ IUSBSCSI.IUSB_SCSI_PKT_SIZE_WITHOUT_HEADER);
	}

	/**
	 * Method Used to establish the socket connection
	 *
	 * @param host -
	 *            serverip
	 * @param port -
	 *            cdserver connceting port
	 * @param bVMUseSSL -
	 *            ssl/nonssl
	 * @throws IOException
	 */

	private int cdromConnect(String host, int port, boolean bVMUseSSL)throws IOException {
		packetMaster = new PacketMaster(host, port, false, protocol, bVMUseSSL);
		packetMaster.setupBuffers(packetReadBuffer, packetWriteBuffer);
		packetMaster.setBufferEndianness(ByteOrder.LITTLE_ENDIAN,ByteOrder.LITTLE_ENDIAN);
		if(JViewer.isSinglePortEnabled()){

			if( JViewerApp.getInstance().getSinglePortKvm().setHTTPConnect("CDMEDIA") < 0){			
				JViewerApp.getInstance().getMainWindow().generalErrorMessage(
						LocaleStrings.getString("4_3_CDROMREDIR"),
						LocaleStrings.getString("4_4_CDROMREDIR")+port+LocaleStrings.getString("4_5_CDROMREDIR"));

				return -1;
			}
			packetMaster.setSock(JViewerApp.getInstance().getSinglePortKvm().getHttpsock());
		}
		else{
			if(packetMaster.connectVmedia(bVMUseSSL) < 0 ){			
				JViewerApp.getInstance().getMainWindow().generalErrorMessage(
						LocaleStrings.getString("4_3_CDROMREDIR"),
						LocaleStrings.getString("4_4_CDROMREDIR")+port+LocaleStrings.getString("4_5_CDROMREDIR"));
				return -1;
			}
		}
		return 0;
	}

	/**
	 * Method is Used to close the established conection
	 *
	 */
	private void cdromDisconnect() {
		try {
			/* Close the connection */
			packetMaster.VmediaSockclose();
		} catch (IOException e) {
			System.err.println(LocaleStrings.getString("4_6_CDROMREDIR")+ e.getMessage());
		}
	}

	/**
	 * Start CD-ROM redirection and create the CD-ROM redirection thread
	 *
	 * @param host
	 *            Hostname or IP of the remote cdserver application
	 * @param cdromDrive
	 *            Complete path to the device we should treat as the CD-ROM
	 *            drive, or full path to the iso file for image redirection
	 * @return true if redirection starts correctly
	 * @return false if redirection is not started
	 * @throws RedirProtocolException
	 *             if unexpected packets are encountered
	 * @throws RedirectionException
	 *             on network errors
	 */
	public boolean startRedirection(String host, String cdromDrive,int cddevice_no,String token, int port, boolean bVMuseSSL)
			throws RedirectionException {

		if (running)
			return (true);
		CDDevice_no = cddevice_no;
		try {
			/* Connect the network socket */
			if(cdromConnect(host, port, bVMuseSSL) < 0)
				return false;
			SendAuth_SessionToken(token);
			/* Get the first request from the card - it has a special value */
			IUSBSCSI request = recvRequest();
			
			cdInstanceNum = request.instanceNum;
			if (request.opcode == DEVICE_REDIRECTION_ACK) {
				/* Did we get the connection? */
				if(request.connectionStatus == CONNECTION_PERM_DENIED){
					cdromDisconnect();
					JViewerApp.getInstance().getMainWindow().generalErrorMessage(
							LocaleStrings.getString("4_3_CDROMREDIR"),
							LocaleStrings.getString("4_17_CDROMREDIR"));
					return (false);
				}
				if(request.connectionStatus == CONNECTION_MAX_USER){
					cdromDisconnect();
					JViewerApp.getInstance().getMainWindow().generalErrorMessage(
							LocaleStrings.getString("4_3_CDROMREDIR"),
							LocaleStrings.getString("4_18_CDROMREDIR"));
					return (false);
				}
				else if (request.connectionStatus != CONNECTION_ACCEPTED) {
					cdromDisconnect();

					if (request.m_otherIP != null) {
						JViewerApp.getInstance().getMainWindow().generalErrorMessage(
								LocaleStrings.getString("4_3_CDROMREDIR"), LocaleStrings.getString("4_7_CDROMREDIR")
								+ request.m_otherIP);
					}
					return (false);
				}
			} else {
				cdromDisconnect();
				throw new RedirProtocolException(LocaleStrings.getString("4_8_CDROMREDIR")+ request.opcode);
			}
		} catch (IOException e) {
			Debug.out.println(e);
			throw new RedirectionException(e.getMessage());
		}

		/* Create the CD-ROM reader */
		if (nativeReaderPointer == -1)
			newCDROMReader(physicalDrive);

		sourceCDROM = cdromDrive;
		File Drive = new File(sourceCDROM);
	        if(!physicalDrive)
	        {
		        if(!Drive.exists())
		        {
		        	JViewerApp.getInstance().getMainWindow().generalErrorMessage(
		        			LocaleStrings.getString("4_9_CDROMREDIR"),
		        			LocaleStrings.getString("4_10_CDROMREDIR"));
		        	//System.err.println(LocaleStrings.GetString("Cannot open CD"));
		            deleteCDROMReader();
		            cdromDisconnect();
		            return( false );
		        }
	        }
	    try {
	    	/* Open the CD-ROM device */
	    	if (!openCDROM(cdromDrive.getBytes("UTF-8"))) {
	    		System.err.println(LocaleStrings.getString("4_11_CDROMREDIR"));
	    		deleteCDROMReader();
	    		cdromDisconnect();
	    		return (false);
	    	}
	    }
	    catch(UnsupportedEncodingException e) {
	    	System.out.println(LocaleStrings.getString("4_12_CDROMREDIR"));
	    }
		JViewerApp.getInstance().getKVMClient().MediaRedirectionState((byte) 1);
		nBytesRedirected = 0;
		/* Start the CD-ROM redirection thread */
		redirThread = new Thread(this);
		redirThread.start();
		if(JViewerApp.getInstance().getM_IUSBSession().isCDROMPhysicalDrive(cddevice_no))
        {
        	vMThread = new vCDMonitorThread(CDDevice_no);
        	vMThread.startCDROMMonitor();
        }
		running = true;
		//CD Redirection will always occur in read only mode, irrespective of whether its Physical device or ISO image
		//that is being redirected.
		InfoDialog.showDialog(JViewerApp.getInstance().getM_mediaDlg(),
				LocaleStrings.getString("4_13_CDROMREDIR"),LocaleStrings.getString("A_6_GLOBAL"),
				InfoDialog.INFORMATION_DIALOG);	
		return (true);
	}

	/**
	 * Stop CD-ROM redirection and join the CD-ROM redirection thread
	 * @return
	 */
	public boolean stopRedirection() {
		if (running) {
			stopRunning = true;
			cdromDisconnect();
			try {
				redirThread.join();
			} catch (InterruptedException e) {
				System.err.println(LocaleStrings.getString("4_14_CDROMREDIR"));
			}
			JViewerApp.getInstance().getKVMClient().MediaRedirectionState((byte) 0);
			running = false;
			stopRunning = false;
			closeCDROM();
			deleteCDROMReader();
		}
		nBytesRedirected = 0;
		return true;
	}

	/**
	 * Response packet reeceived from the CDServer
	 *
	 * @return
	 * @throws IOException
	 * @throws RedirectionException
	 */
	private IUSBSCSI recvRequest() throws IOException, RedirectionException {
		return ((IUSBSCSI) packetMaster.receivePacket());
	}

	/***
	 * Returns true if redirection is currently active
	 * @return
	 */
	public boolean isRedirActive() {
		return (running);
	}

	/***
	 *
	 * Must be called to stop CDROM redirection thread abnormally
	 *
	 */
	public void stopRedirectionAbnormal() {
		if (running) {
			stopRunning = true;
			cdromDisconnect();
			running = false;
			stopRunning = false;
			closeCDROM();
			deleteCDROMReader();
			JViewerApp.getInstance().reportCDROMAbnormal(CDDevice_no);
		}
	}

	/**
	 * Main execution loop for the CD-ROM redirection thread. Don't mess with
	 * this, use {@link #startRedirection} and {@link #stopRedirection} to start
	 * and stop this thread.
	 */
	public void run() {
		IUSBSCSI request;
		IUSBSCSI response;
		int nTempLen = 0;
		while (!stopRunning) {
			try {
				packetWriteBuffer.rewind();
				/* Get a request from the card */
				request = recvRequest();

				if (request == null)
					continue;

				/* Execute the CDROM request */
				int dataLen = executeCDROMSCSICmd(packetReadBuffer,	packetWriteBuffer);
				packetWriteBuffer.limit(dataLen);
				
				if(request.opcode == IUSBSCSI.OPCODE_KILL_REDIR)
				{
					System.out.println("EXIT COMMAND RECEIVED IN FLOPPY : "+request.opcode );
					cdRedirectionKilled = true;
				}
				
				if (request.opcode == IUSBSCSI.OPCODE_EJECT)// eject command for CD image/drive
				{
					if (request.Lba == 2)
						cdImageEjected = true;
				}

				/* Form the IUSB response packet */
				response = new IUSBSCSI(packetWriteBuffer, true);
				/* Send the IUSB response packet */
				packetMaster.sendPacket(response);
				nTempLen += dataLen;
				nBytesRedirected += (nTempLen / 1024);
				nTempLen = nTempLen % 1024;
			} catch (IOException e) {
				if (!stopRunning) {
					synchronized (getSyncObj()) {
						try {
							if(!confModified)
								getSyncObj().wait(10000);//wait for 10 seconds to check if service configuration change packet arrives from adviser 
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					if(!confModified){
						handleError(IOERRMSG);
					}
					else
						confModified = false;
					stopRedirectionAbnormal();
					return;
				}
			} catch (RedirectionException e) {
				if (!stopRunning) {
					synchronized (getSyncObj()) {
						try {
							if(!confModified)
								getSyncObj().wait(10000);//wait for 10 seconds to check if service configuration change packet arrives from adviser 
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					if(!confModified){
						handleError(IOERRMSG);
					}
					else
						confModified = false;
					stopRedirectionAbnormal();
					return;
				}
			}
		}
		Debug.out.println("Exiting the CDROM/ISO Redirection thread");
		return;
	}

	/**
	 * For redirection sessions with an actual cdrom drive, this returns an
	 * array of strings that represent cdrom device paths Not valid for iso
	 * image redirection sessions.
	 *
	 * @return An array of strings of cdrom drives on success
	 * @return null on error
	 * @throws RedirectionException
	 * if this is not a physical cdrom drive redirection session
	 */
	public String[] getCDROMList() throws RedirectionException {
		if (!physicalDrive) {
			DisplayErrorMsg(LocaleStrings.getString("4_15_CDROMREDIR"));
			return (null);
		}

		if (nativeReaderPointer == -1)
			newCDROMReader(true);

		String[] cdromlist = listCDROMDrives();

		if (nativeReaderPointer == -1)
			deleteCDROMReader();

		return (cdromlist);
	}

	/**
	 * Query the LIBCDROM native library and get its version
	 * @return
	 */
	public String getLIBCDROMVersion() {
		String version;

		if (nativeReaderPointer == -1) {
			newCDROMReader(false);
			version = getVersion();
			deleteCDROMReader();
		} else
			version = getVersion();

		return (version);
	}

	/**
	 * Handle errors during redirection
	 */
	public void DisplayErrorMsg(String str) {
		JViewerApp.getInstance().getMainWindow().generalErrorMessage(
				LocaleStrings.getString("4_16_CDROMREDIR"), str);
	}

	/**
	 * Handle errors during redirection
	 */
	public void handleError(String str) {
		DisplayErrorMsg(str);
	}

	/***
	 * return the SourceDrive name of the CDROM
	 * @return
	 */

	public String getSourceDrive() {
		return sourceCDROM;
	}

	/**
	 * Returns true if this session is for an actual cdrom drive, and false for
	 * ISO images
	 */
	public boolean isPhysicalDevice() {
		return (physicalDrive);
	}

	/***
	 * Send the Authentication token packet to CDserver for validate the client
	 * @param session_token - Session token received as argument
	 * @throws RedirectionException
	 * @throws IOException
	 */

	public void SendAuth_SessionToken(String session_token) throws RedirectionException, IOException {
		
		int dataLen = 0;
		int session_token_type = JViewerApp.getInstance().getSessionTokenType();

		if (session_token_type == IVTPPktHdr.WEB_SESSION_TOKEN)
		{
			dataLen = IUSBSCSI.IUSB_SCSI_PKT_SIZE - IUSBHeader.HEADER_LEN + IUSBRedirSession.WEB_AUTH_PKT_MAX_SIZE;
			packetWriteBuffer.clear();
			packetWriteBuffer.limit(IUSBSCSI.IUSB_SCSI_PKT_SIZE	+ IUSBRedirSession.WEB_AUTH_PKT_MAX_SIZE);
		}
		else if (session_token_type == IVTPPktHdr.SSI_SESSION_TOKEN)
		{
			dataLen = IUSBSCSI.IUSB_SCSI_PKT_SIZE - IUSBHeader.HEADER_LEN + IUSBRedirSession.SSI_AUTH_PKT_MAX_SIZE;
			packetWriteBuffer.clear();
			packetWriteBuffer.limit(IUSBSCSI.IUSB_SCSI_PKT_SIZE	+ IUSBRedirSession.SSI_AUTH_PKT_MAX_SIZE);	
		}

		IUSBHeader AuthPktIUSBHeader = IUSBHeader.createCDROMHeader(dataLen);
		AuthPktIUSBHeader.write(packetWriteBuffer);
		packetWriteBuffer.position(41); // Opcode for SCSI command packet;
		packetWriteBuffer.put((byte) (AUTH_CMD & 0xff));
		packetWriteBuffer.position(IUSBSCSI.IUSB_SCSI_PKT_SIZE);
		packetWriteBuffer.put((byte) 0); // authpacket flags;
		packetWriteBuffer.put(session_token.getBytes());
		packetWriteBuffer.position(23);
		packetWriteBuffer.put((byte)CDDevice_no);
		packetWriteBuffer.position(0);
		IUSBSCSI pkt = new IUSBSCSI(packetWriteBuffer, true);
		packetMaster.sendPacket(pkt);
	}

	/**
	 * Method returns the data transfer rate
	 * @return
	 */
	public int getBytesRedirected() {
		return nBytesRedirected;
	}
	public static Object getSyncObj() {
		return syncObj;
	}
	/**
	 * @return the confModified
	 */
	public boolean isConfModified() {
		return confModified;
	}
	/**
	 * @param confModified the confModified to set
	 */
	public void setConfModified(boolean confModified) {
		this.confModified = confModified;
	}
	/**
	 * @return the cdInstanceNum
	 */
	public int getCdInstanceNum() {
		return cdInstanceNum;
	}
	/**
	 * @return the cdImageRedirected
	 */
	public boolean isCdImageRedirected() {
		return cdImageRedirected;
	}
	/**
	 * @param cdImageRedirected the cdImageRedirected to set
	 */
	public void setCdImageRedirected(boolean cdImageRedirected) {
		this.cdImageRedirected = cdImageRedirected;
	}
	/**
	 * @return the cdImageEjected
	 */
	public boolean isCdImageEjected() {
		return cdImageEjected;
	}
	/**
	 * @return the cdRedirectionKilled
	 */
	public boolean isCdRedirectionKilled() {
		return cdRedirectionKilled;
	}
}
