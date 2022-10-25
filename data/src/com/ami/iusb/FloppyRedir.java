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
import java.util.Arrays;

import com.ami.iusb.protocol.FloppyProtocol;
import com.ami.iusb.protocol.IUSBHeader;
import com.ami.iusb.protocol.IUSBSCSI;
import com.ami.iusb.protocol.PacketMaster;
import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.gui.InfoDialog;
import com.ami.kvm.jviewer.gui.JViewerApp;
import com.ami.kvm.jviewer.gui.LocaleStrings;
import com.ami.kvm.jviewer.gui.StandAloneConnectionDialog;
import com.ami.kvm.jviewer.kvmpkts.IVTPPktHdr;

public class FloppyRedir extends Thread {

	private PacketMaster packetMaster;
	private FloppyProtocol protocol;
	private ByteBuffer packetReadBuffer;
	private ByteBuffer packetWriteBuffer;
	private boolean physicalDevice;
	private int  FDDevice_no;
	private boolean running = false;
	private boolean stopRunning = false;
	private long nativeReaderPointer = -1;
	private Thread redirThread;
	private String sourceFloppy = null;
	private int nBytesRedirected = 0;
	private int fdInstanceNum;

	private static final int DEVICE_REDIRECTION_ACK = 0xf1;
	private static final int AUTH_CMD = 0xf2;
	private static final int CONNECTION_ACCEPTED = 1;
	private static final int CONNECTION_PERM_DENIED = 5;
	private static final int CONNECTION_MAX_USER = 8;
	private static final int MAX_READ_SECTORS = 256;
	private static final int MAX_READ_SIZE = 512 * MAX_READ_SECTORS;
	private static final String IOERRMSG = LocaleStrings.getString("5_1_FLOPPYREDIR");

	public static final int	READ_WRITE		=	1;
	public static final int	READ_ONLY		=	2;
	public static final int ALREADY_IN_USE	=	-3;

	private  boolean fdImageRedirected = false;
	private  boolean fdImageEjected = false;
	private vFlpyMonitorThread vMThread = null;
	
	// Native library call
	private native String[] listFloppyDrives();
	private native void newFloppyReader(boolean physicalDevice);
	private native void deleteFloppyReader();
	private native int openFloppy(byte[] bs);
	private native void closeFloppy();
	private native int executeFloppySCSICmd(ByteBuffer scsiRequest,	ByteBuffer scsiResponse);
	private native String GetKeyboardName();
	private native byte GetLEDStatus();
	private native String getVersion();
	private static Object syncObj = new Object();
	private  boolean confModified = false;
	private boolean fdRedirectionKilled = false;

	/***
	 * Loading the Library for Acccesing the Floppy
	 */
	static {
		try {
			if( !JViewer.isdownloadapp() && !JViewer.isplayerapp())
			{
				if(JViewer.isjviewerapp()){ // Normal JViwer
					System.loadLibrary("javafloppywrapper");
				}
				else { //For SPXMultiViewer and StandAloneApp
					loadWrapperLibrary();
				}
			}
		} catch (UnsatisfiedLinkError e) {
			System.err.println(LocaleStrings.getString("5_2_FLOPPYREDIR"));
		}

	}

	/**
	 * Loads the javawrapper library files. 
	 */
	private static void loadWrapperLibrary(){
		String libPath = null;
		String commonPath = File.separator+"Jar"+File.separator+
							JViewer.getIp()+File.separator+"lib"+File.separator;
		File libFile = null;
		//Replace all ':' characters from the common path with '_'. This is because in Windows file system,
		//file and directory names and are not allowed to contain ':'. In this case the getIP() method
		//might return IPV6 address which will contain : and will lead to error.
		if(System.getProperty("os.name").startsWith("Windows")){
			commonPath = StandAloneConnectionDialog.replaceAllPattern(commonPath, ":", "_");
			libPath = System.getProperty("user.dir")+commonPath+"javafloppywrapper.dll";
			libFile = new File(libPath);
			//extract the library files only if its not already existing
			if(!libFile.exists())
				StandAloneConnectionDialog.getWrapperLibrary("javafloppywrapper.dll");
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
				libPath = currPath+commonPath+"libjavafloppywrapper.so";
				libFile = new File(libPath);
				//extract the library files only if its not already existing
				if(!libFile.exists())
					StandAloneConnectionDialog.getWrapperLibrary("libjavafloppywrapper.so");
			}
			else{
				libPath = System.getProperty("user.dir")+commonPath+"libjavafloppywrapper.so";
			}
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
				libPath = currPath+commonPath+"libjavafloppywrapper.jnilib";
				libFile = new File(libPath);
				//extract the library files only if its not already existing
				if(!libFile.exists())
					StandAloneConnectionDialog.getWrapperLibrary("libjavafloppywrapper.jnilib");
			}
			else
				libPath = System.getProperty("user.dir")+commonPath+"libjavafloppywrapper.jnilib";
		}
		System.load(libPath);
	}
	
	/**
	 * Creates a new instance of FloppyRedir
	 * @param physicalDevice
	 */
	public FloppyRedir(boolean physicalDevice) {
		this.physicalDevice = physicalDevice;
		protocol = new FloppyProtocol();
		packetReadBuffer = ByteBuffer.allocateDirect(MAX_READ_SIZE
				+ IUSBHeader.HEADER_LEN
				+ IUSBSCSI.IUSB_SCSI_PKT_SIZE_WITHOUT_HEADER);
		packetWriteBuffer = ByteBuffer.allocateDirect(MAX_READ_SIZE
				+ IUSBHeader.HEADER_LEN
				+ IUSBSCSI.IUSB_SCSI_PKT_SIZE_WITHOUT_HEADER);
	}

	/**
	 * Creating the request buffer and response buffer for floppy data transfer
	 *
	 * @param host
	 * @param port
	 * @param bVMUseSSL
	 * @throws IOException
	 */
	private int floppyConnect(String host, int port, boolean bVMUseSSL)
			throws IOException {
		packetMaster = new PacketMaster(host, port, false, protocol, bVMUseSSL);
		packetMaster.setupBuffers(packetReadBuffer, packetWriteBuffer);
		packetMaster.setBufferEndianness(ByteOrder.LITTLE_ENDIAN, ByteOrder.LITTLE_ENDIAN);
		if(JViewer.isSinglePortEnabled()){

			if( JViewerApp.getInstance().getSinglePortKvm().setHTTPConnect("FDMEDIA") < 0){			
				JViewerApp.getInstance().getMainWindow().generalErrorMessage(
						LocaleStrings.getString("4_3_CDROMREDIR"),
						LocaleStrings.getString("5_3_FLOPPYREDIR")+port+LocaleStrings.getString("5_4_FLOPPYREDIR"));
				return -1;
			}
			packetMaster.setSock(JViewerApp.getInstance().getSinglePortKvm().getHttpsock());

		}
		else{
			if(packetMaster.connectVmedia(bVMUseSSL) < 0 ){			
				JViewerApp.getInstance().getMainWindow().generalErrorMessage(
						LocaleStrings.getString("4_3_CDROMREDIR"),
						LocaleStrings.getString("5_3_FLOPPYREDIR")+port+LocaleStrings.getString("5_4_FLOPPYREDIR"));
				return -1;
			}
		}
		return 0;
	}

	/**
	 * Closing the Vmedia socket close
	 *
	 */
	private void floppyDisconnect() {
		try {
			/* Close the connection */
			packetMaster.VmediaSockclose();
		} catch (IOException e) {
			System.err.println(LocaleStrings.getString("5_5_FLOPPYREDIR")+ e.getMessage());
		}
	}

	/**
	 * Starting the floppy redirection
	 *
	 * @param host -
	 *            Hostname to be connect
	 * @param floppyDrive -
	 *            client drive name or drive path
	 * @param token -
	 *            session token
	 * @param port -
	 *            port to be connected
	 * @param bVMuseSSL -
	 *            ssl or nonssl socket
	 * @return
	 * @throws RedirectionException
	 */
	public boolean startRedirection(String host, String floppyDrive,int device_no,
			String token, int port, boolean bVMuseSSL)
			throws RedirectionException {
		if (running)
			return (true);
		FDDevice_no = device_no;

		try {
			if(floppyConnect(host, port, bVMuseSSL) < 0){
				return false;
			}
			SendAuth_SessionToken(token);

			/* Get the first request from the card - it has a special value */
			IUSBSCSI request = recvRequest();
			fdInstanceNum = request.instanceNum;
			if (request.opcode == DEVICE_REDIRECTION_ACK) {
				/* Did we get the connection? */
				if(request.connectionStatus == CONNECTION_PERM_DENIED){
					floppyDisconnect();
					JViewerApp.getInstance().getMainWindow().generalErrorMessage(
							LocaleStrings.getString("4_3_CDROMREDIR"),
							LocaleStrings.getString("4_17_CDROMREDIR"));
					return (false);
				}
				else if (request.connectionStatus == CONNECTION_MAX_USER) {
					floppyDisconnect();
					JViewerApp.getInstance().getMainWindow().generalErrorMessage(
								LocaleStrings.getString("4_3_CDROMREDIR"),
								LocaleStrings.getString("5_10_FLOPPYREDIR"));
					return (false);
				}
				else if (request.connectionStatus != CONNECTION_ACCEPTED) {
					floppyDisconnect();

					if (request.m_otherIP != null) {
						JViewerApp.getInstance().getMainWindow().generalErrorMessage(
								LocaleStrings.getString("4_3_CDROMREDIR"),
								LocaleStrings.getString("4_7_CDROMREDIR")+ request.m_otherIP);
					}
					return (false);
				}
			} else {
				floppyDisconnect();
				throw new RedirectionException(LocaleStrings.getString("4_8_CDROMREDIR")
						+ request.opcode);
			 }
		} catch (IOException e) {
			throw new RedirectionException(e.getMessage());
		}

		/* Create the floppy reader */
		if (nativeReaderPointer == -1)
			newFloppyReader(physicalDevice);
		sourceFloppy = floppyDrive;
		/* Open the floppy device */
		try{
			int openRet = openFloppy(floppyDrive.getBytes("UTF-8"));
			if(openRet < 0) {
				if(openRet == ALREADY_IN_USE){
					if(JViewerApp.getInstance().getM_IUSBSession().isFloppyPhysicalDrive(FDDevice_no))
					{
						InfoDialog.showDialog(JViewerApp.getInstance().getM_mediaDlg(),
								LocaleStrings.getString("6_37_IUSBREDIR"),LocaleStrings.getString("A_6_GLOBAL"),
								InfoDialog.INFORMATION_DIALOG);
					}
					else
					{
						InfoDialog.showDialog(JViewerApp.getInstance().getM_mediaDlg(),
								LocaleStrings.getString("5_8_FLOPPYREDIR"),LocaleStrings.getString("A_6_GLOBAL"),
								InfoDialog.INFORMATION_DIALOG);
					}
				}
				System.err.println(LocaleStrings.getString("5_6_FLOPPYREDIR"));
				deleteFloppyReader();
				floppyDisconnect();
				return (false);
			}
			if(openRet == READ_ONLY) {
				if((JViewer.getOEMFeatureStatus() & JViewerApp.OEM_REDIR_RD_WR_MODE) == JViewerApp.OEM_REDIR_RD_WR_MODE){
					if(JViewerApp.getInstance().getM_IUSBSession().isFloppyPhysicalDrive(FDDevice_no))
					{
						InfoDialog.showDialog(JViewerApp.getInstance().getM_mediaDlg(),
								LocaleStrings.getString("6_37_IUSBREDIR"),LocaleStrings.getString("A_6_GLOBAL"),
								InfoDialog.INFORMATION_DIALOG);
					}
					else
					{
						InfoDialog.showDialog(JViewerApp.getInstance().getM_mediaDlg(),
								LocaleStrings.getString("5_11_FLOPPYREDIR"),LocaleStrings.getString("A_6_GLOBAL"),
								InfoDialog.INFORMATION_DIALOG);
					}
					deleteFloppyReader();
					floppyDisconnect();
					return (false);

				}				
				InfoDialog.showDialog(JViewerApp.getInstance().getM_mediaDlg(),
						LocaleStrings.getString("4_13_CDROMREDIR"),LocaleStrings.getString("A_6_GLOBAL"),
						InfoDialog.INFORMATION_DIALOG);
			}
		}
		catch(UnsupportedEncodingException e){
			System.out.println(LocaleStrings.getString("4_12_CDROMREDIR"));
		}
		JViewerApp.getInstance().getKVMClient().MediaRedirectionState((byte) 1);
		nBytesRedirected = 0;
		/* Start the floppy redirection thread */
		redirThread = new Thread(this);
		redirThread.start();
		if(JViewerApp.getInstance().getM_IUSBSession().isFloppyPhysicalDrive(FDDevice_no))
	    {
	      	vMThread = new vFlpyMonitorThread(FDDevice_no);
	        vMThread.startFloppyMonitor();
	    }
		running = true;
		return (true);
	}

	/**
	 * Stop the running floppy redirection
	 *
	 * @return
	 */

	public boolean stopRedirection() {
		if (running) {
			stopRunning = true;
			floppyDisconnect();
			try {
				redirThread.join();
			} catch (InterruptedException e) {
				System.err.println(LocaleStrings.getString("5_7_FLOPPYREDIR"));
			}
			JViewerApp.getInstance().getKVMClient().MediaRedirectionState( (byte) 0);
			floppyDisconnect();
			running = false;
			stopRunning = false;
			closeFloppy();
			deleteFloppyReader();
		}
		nBytesRedirected = 0;
		return true;
	}

	/***
	 *
	 * @return calling the receive method in the packet master
	 * @throws IOException
	 * @throws RedirectionException
	 */
	private IUSBSCSI recvRequest() throws IOException, RedirectionException {
		return ((IUSBSCSI) packetMaster.receivePacket());
	}

	/***
	 * return the floppy redirection is running or not
	 *
	 * @return
	 */
	public boolean isRedirActive() {
		return (running);
	}

	/***
	 * Return the Source drive name
	 * @return
	 */
	public String getSourceDrive() {
		return sourceFloppy;
	}


	/****
	 * Must be called to stop floppy redirection thread abnormally.
	 *
	 */
	public void stopRedirectionAbnormal() {
		if (running) {
			stopRunning = true;
			floppyDisconnect();
			running = false;
			stopRunning = false;
			closeFloppy();
			deleteFloppyReader();
			JViewerApp.getInstance().reportFloppyAbnormal(FDDevice_no);
		}
		else
			JViewerApp.getInstance().reportFloppyAbnormal(FDDevice_no);
	}

	/**
	 * Creating a thread to send/receive the packet from the FDserver
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

				/* Execute the floppy request */
				int dataLen = executeFloppySCSICmd(packetReadBuffer, packetWriteBuffer);
				packetWriteBuffer.limit(dataLen);
				if(request.opcode == IUSBSCSI.OPCODE_KILL_REDIR)
				{
					System.out.println("EXIT COMMAND RECEIVED IN FLOPPY : "+request.opcode );
					fdRedirectionKilled = true;
				}
				if (request.opcode == IUSBSCSI.OPCODE_EJECT)// eject command for floppy imgae or floppy drive
				{
					if (request.Lba == 2)
						fdImageEjected = true;
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
							if (!confModified)
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
	}

	/****
	 * Getting the floppy list from the Native call
	 * if any drive name drive name duplicated removing here and copying in another array
	 *
	 * @return
	 */
	public String[] getFloppyList() {
		if (!physicalDevice) {
			DisplayErrorMsg("Cannot get Floppy drive list during Floppy IMAGE redirection");
			return (null);
		}

		if (nativeReaderPointer == -1)
			newFloppyReader(true);

		String[] ListDrive_original = listFloppyDrives();
		String[] ListDrive_filter = null;

		if (ListDrive_original != null) {
			Arrays.sort(ListDrive_original);
			int k = 0;

			for (int i = 0; i < ListDrive_original.length; i++) {
				if (i > 0 && ListDrive_original[i].equals(ListDrive_original[i - 1]))
					continue;
				ListDrive_original[k++] = ListDrive_original[i];
			}
			ListDrive_filter = new String[k];
			System.arraycopy(ListDrive_original, 0, ListDrive_filter, 0, k);
		}
		return (ListDrive_filter);
	}

	/**
	 * Getting the lib floppy Version from the Native call
	 *
	 * @return
	 */

	public String getLIBFLOPPYVersion() {
		String version;

		if (nativeReaderPointer == -1) {
			newFloppyReader(false);
			version = getVersion();
			deleteFloppyReader();
		} else
			version = getVersion();

		return (version);
	}

	/**
	 * Display error messages
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

	/**
	 * Return the Floppy drive is physical or image redirection
	 * @return
	 */
	public boolean isPhysicalDevice() {
		return (physicalDevice);
	}

	/***
	 * Send the Authentication token packet to  the FDServer validate the client
	 * @param session_token - Session token received as argument
	 * @throws RedirectionException
	 * @throws IOException
	 */
	public void SendAuth_SessionToken(String session_token)
			throws RedirectionException, IOException {

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
		packetWriteBuffer.put((byte)FDDevice_no);
		packetWriteBuffer.position(0);
		IUSBSCSI pkt = new IUSBSCSI(packetWriteBuffer, true);
		packetMaster.sendPacket(pkt);
	}

	/**
	 * Read the LED status of the Client from the native library call
	 * @return LED state
	 */
	public byte ReadKeybdLEDStatus() {
		return GetLEDStatus();
	}

	/**
	 * Return the data transfer rate of the FD redirection
	 * @return
	 */
	public int getBytesRedirected() {
		return nBytesRedirected;
	}
	/**
	 * @return the syncObj
	 */
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
	 * Return the client system keyboard layout
	 * @return
	 */
	public String ReadKeybdType()
	{
	    return GetKeyboardName();
	}
	/**
	 * @return the fdInstanceNum
	 */
	public int getFdInstanceNum() {
		return fdInstanceNum;
	}
	/**
	 * @return the fdImageRedirected
	 */
	public boolean isFdImageRedirected() {
		return fdImageRedirected;
	}
	/**
	 * @param fdImageRedirected the fdImageRedirected to set
	 */
	public void setFdImageRedirected(boolean fdImageRedirected) {
		this.fdImageRedirected = fdImageRedirected;
	}
	/**
	 * @return the fdImageEjected
	 */
	public boolean isFdImageEjected() {
		return fdImageEjected;
	}
	/**
	 * @return the fdRedirectionKilled
	 */
	public boolean isFdRedirectionKilled() {
		return fdRedirectionKilled;
	}
}
