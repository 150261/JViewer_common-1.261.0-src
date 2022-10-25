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

import com.ami.iusb.protocol.HarddiskProtocol;
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

public class HarddiskRedir extends Thread {

	private PacketMaster packetMaster;
	private HarddiskProtocol protocol;
	private ByteBuffer packetReadBuffer;
	private ByteBuffer packetWriteBuffer;
	private boolean physicalDevice;
	private int  HDDevice_no;
	private boolean running = false;
	private boolean stopRunning = false;
	private long nativeReaderPointer = -1;
	private Thread redirThread;
	private String sourceHarddisk = null;
	private int nBytesRedirected = 0;
	private int hdInstanceNum;

	private static final int DEVICE_REDIRECTION_ACK = 0xf1;
	private static final int AUTH_CMD = 0xf2;
	private static final int SET_HARDDISK_TYPE = 0xf4;
	private static final int CONNECTION_ACCEPTED = 1;
	private static final int CONNECTION_PERM_DENIED = 5;
	private static final int CONNECTION_MAX_USER = 8;
	private static final int MAX_READ_SECTORS = 256;
	private static final int MAX_READ_SIZE = 512 * MAX_READ_SECTORS;
	private static final String IOERRMSG = LocaleStrings.getString("AB_1_HDREDIR");
	private boolean hdImageEjected = false;
	private  boolean hdImageRedirected = false;
	public static final int	READ_WRITE		=	1;
	public static final int	READ_ONLY		=	2;
	public static final int ALREADY_IN_USE	=	-3;

	int  Drive_Type;
	String[] ListDrive_USB = null;
	String[] ListDrive_Fixed = null;
	private vHarddiskMonitorThread vMThread = null;
	
	
	// Native library call
	private native String[] listHardDrivesFixed();
	private native String[] listHardDrives();
	private native void newHarddiskReader(boolean physicalDevice);
	private native void deleteHarddiskReader();
	private native int openHarddisk(byte[] bs,boolean physicalDevice);
	private native void closeHarddisk();
	private native int executeHarddiskSCSICmd(ByteBuffer scsiRequest, ByteBuffer scsiResponse);
	private native String getVersion();
	private static Object syncObj = new Object();
	private  boolean confModified = false;
	private boolean hdRedirectionKilled = false;

	/***
	 * Loading the Library for Acccesing the hard disk
	 */
	static {
		try {
			if( !JViewer.isdownloadapp() && !JViewer.isplayerapp())
			{
				if(JViewer.isjviewerapp()){// Normal JViewer
					System.loadLibrary("javaharddiskwrapper");
				}
				else { //For SPXMultiViewer and StandAloneApp
					loadWrapperLibrary();
				}
			}
		} catch (UnsatisfiedLinkError e) {
			System.err.println(LocaleStrings.getString("AB_2_HDREDIR"));
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
			libPath = System.getProperty("user.dir")+commonPath+"javaharddiskwrapper.dll";
			libFile = new File(libPath);
			//extract the library files only if its not already existing
			if(!libFile.exists())
				StandAloneConnectionDialog.getWrapperLibrary("javaharddiskwrapper.dll");
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
				libPath = currPath+commonPath+"libjavaharddiskwrapper.so";
				libFile = new File(libPath);
				//extract the library files only if its not already existing
				if(!libFile.exists())
					StandAloneConnectionDialog.getWrapperLibrary("libjavaharddiskwrapper.so");
			}
			else
				libPath = System.getProperty("user.dir")+commonPath+"libjavaharddiskwrapper.so";
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
				libPath = currPath+commonPath+"libjavaharddiskwrapper.jnilib";
				libFile = new File(libPath);
				//extract the library files only if its not already existing
				if(!libFile.exists())
					StandAloneConnectionDialog.getWrapperLibrary("libjavaharddiskwrapper.jnilib");
			}
			else
			libPath = System.getProperty("user.dir")+commonPath+"libjavaharddiskwrapper.jnilib";
		}
		System.load(libPath);
	}

	/**
	 * Creates a new instance of HarddiskRedir
	 * @param physicalDevice
	 */
	public HarddiskRedir(boolean physicalDevice) {
		this.physicalDevice = physicalDevice;
		protocol = new HarddiskProtocol();
		packetReadBuffer = ByteBuffer.allocateDirect(MAX_READ_SIZE
				+ IUSBHeader.HEADER_LEN
				+ IUSBSCSI.IUSB_SCSI_PKT_SIZE_WITHOUT_HEADER);
		packetWriteBuffer = ByteBuffer.allocateDirect(MAX_READ_SIZE
				+ IUSBHeader.HEADER_LEN
				+ IUSBSCSI.IUSB_SCSI_PKT_SIZE_WITHOUT_HEADER);
	}

	/**
	 * Creating the request buffer and response buffer for hard disk data transfer
	 *
	 * @param host
	 * @param port
	 * @param bVMUseSSL
	 * @throws IOException
	 */
	private int harddiskConnect(String host, int port, boolean bVMUseSSL)
			throws IOException {
		packetMaster = new PacketMaster(host, port, false, protocol, bVMUseSSL);
		packetMaster.setupBuffers(packetReadBuffer, packetWriteBuffer);
		packetMaster.setBufferEndianness(ByteOrder.LITTLE_ENDIAN, ByteOrder.LITTLE_ENDIAN);

		if(JViewer.isSinglePortEnabled()){

			if( JViewerApp.getInstance().getSinglePortKvm().setHTTPConnect("HDMEDIA") < 0){			
				JViewerApp.getInstance().getMainWindow().generalErrorMessage(
						LocaleStrings.getString("4_3_CDROMREDIR"),
						LocaleStrings.getString("AB_3_HDREDIR")+port+
						LocaleStrings.getString("AB_4_HDREDIR"));
				return -1;
			}
			packetMaster.setSock(JViewerApp.getInstance().getSinglePortKvm().getHttpsock());

		}
		else{
			if(packetMaster.connectVmedia(bVMUseSSL) < 0 ){			
				JViewerApp.getInstance().getMainWindow().generalErrorMessage(
						LocaleStrings.getString("4_3_CDROMREDIR"),
						LocaleStrings.getString("AB_3_HDREDIR")+port+
						LocaleStrings.getString("AB_4_HDREDIR"));
				return -1;
			}
		}
		return 0;
	}

	/**
	 * Closing the Vmedia socket close
	 *
	 */
	private void harddiskDisconnect() {
		try {
			/* Close the connection */
			packetMaster.VmediaSockclose();
		} catch (IOException e) {
			System.err.println(LocaleStrings.getString("AB_5_HDREDIR")+ e.getMessage());
		}
	}

	/**
	 * Starting the hard disk redirection
	 *
	 * @param host -
	 *            Hostname to be connect
	 * @param hard diskDrive -
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
	public boolean startRedirection(String host, String hardDrive,int hddevice_no,
			String token, int port, boolean bVMuseSSL)
			throws RedirectionException {
		if (running)
			return (true);
		HDDevice_no = hddevice_no;
		try {
			if(harddiskConnect(host, port, bVMuseSSL) < 0)
				return false;
			SendAuth_SessionToken(token);
			/* Get the first request from the card - it has a special value */
			IUSBSCSI request = recvRequest();
			hdInstanceNum = request.instanceNum;
			
			if (request.opcode == DEVICE_REDIRECTION_ACK) {
				/* Did we get the connection? */
				if(request.connectionStatus == CONNECTION_PERM_DENIED){
					harddiskDisconnect();
					JViewerApp.getInstance().getMainWindow().generalErrorMessage(
							LocaleStrings.getString("4_3_CDROMREDIR"),
							LocaleStrings.getString("4_17_CDROMREDIR"));
					return (false);
				}
				else if (request.connectionStatus == CONNECTION_MAX_USER) {
					harddiskDisconnect();
					JViewerApp.getInstance().getMainWindow().generalErrorMessage(
								LocaleStrings.getString("4_3_CDROMREDIR"),
								LocaleStrings.getString("AB_8_HDREDIR"));
					return (false);
				}
				else if (request.connectionStatus != CONNECTION_ACCEPTED) {
					harddiskDisconnect();

					if (request.m_otherIP != null) {
						JViewerApp.getInstance().getMainWindow().generalErrorMessage(
								LocaleStrings.getString("4_3_CDROMREDIR"),
								LocaleStrings.getString("4_7_CDROMREDIR")+ request.m_otherIP);
					}
					return (false);
				}
			} else {
				harddiskDisconnect();
				throw new RedirectionException(LocaleStrings.getString("4_8_CDROMREDIR")+ request.opcode);
			}
		} catch (IOException e) {
			throw new RedirectionException(e.getMessage());
		}

		/* Create the hard disk reader */
		if (nativeReaderPointer == -1)
			newHarddiskReader(physicalDevice);
		  
		if(System.getProperty("os.name").startsWith("Windows"))
		{
			sourceHarddisk = hardDrive =hardDrive.trim();
			if(isPhysicalDevice()){
				String remove = hardDrive.substring(hardDrive.indexOf('['));
				hardDrive = hardDrive.substring(0,hardDrive.indexOf('['));
				int j=0;
				while(j <remove.indexOf(']')){
					char ch = remove.charAt(j);
					if(ch != '[' && ch != ']' &&ch != '-')
					hardDrive+=ch;
					j++;
				}
				hardDrive += remove.substring(j+1);
			}				
		}
		else
		{
			if(isPhysicalDevice())
			{
				String[] sourceHarddisk_temp = hardDrive.split("-");
				sourceHarddisk = hardDrive;
				hardDrive = sourceHarddisk_temp[0].trim();
			}
			else
			{
				sourceHarddisk = hardDrive =hardDrive.trim();
			}
		}
		/* Open the Harddrive */
		try{
			int Open_ret = openHarddisk(hardDrive.getBytes("UTF-8"),isPhysicalDevice());
			if (Open_ret < 0) {
				if(Open_ret == ALREADY_IN_USE){
					if(JViewerApp.getInstance().getM_IUSBSession().isHarddiskPhysicalDrive(HDDevice_no))
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
				System.err.println(LocaleStrings.getString("AB_6_HDREDIR"));
				deleteHarddiskReader();
				harddiskDisconnect();
				return (false);
			}
			if(Open_ret == READ_ONLY) {

				if((JViewer.getOEMFeatureStatus() & JViewerApp.OEM_REDIR_RD_WR_MODE) == JViewerApp.OEM_REDIR_RD_WR_MODE){
					if(JViewerApp.getInstance().getM_IUSBSession().isHarddiskPhysicalDrive(HDDevice_no))
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
					System.err.println(LocaleStrings.getString("AB_6_HDREDIR"));
					deleteHarddiskReader();
					harddiskDisconnect();
					return (false);

				}
				InfoDialog.showDialog(JViewerApp.getInstance().getM_mediaDlg(),
						LocaleStrings.getString("4_13_CDROMREDIR"),LocaleStrings.getString("A_6_GLOBAL"),
						InfoDialog.INFORMATION_DIALOG);
			}
		}catch(UnsupportedEncodingException e){System.out.println(LocaleStrings.getString("4_12_CDROMREDIR"));}
		JViewerApp.getInstance().getKVMClient().MediaRedirectionState((byte) 1);
		nBytesRedirected = 0;
		/* Start the hard disk redirection thread */
		redirThread = new Thread(this);
		redirThread.start();
		if(JViewerApp.getInstance().getM_IUSBSession().isHarddiskPhysicalDrive(HDDevice_no))
	    {
	       	vMThread = new vHarddiskMonitorThread(HDDevice_no);
	       	vMThread.startharddiskMonitor();
	    }
		running = true;
		return (true);
	}

	/**
	 * Stop the running hard disk redirection
	 *
	 * @return
	 */

	public boolean stopRedirection() {
		if (running) {
			stopRunning = true;
			harddiskDisconnect();
			try {
				redirThread.join();
			} catch (InterruptedException e) {
				System.err.println(LocaleStrings.getString("AB_7_HDREDIR"));
			}
			JViewerApp.getInstance().getKVMClient().MediaRedirectionState( (byte) 0);
			harddiskDisconnect();
			running = false;
			stopRunning = false;
			closeHarddisk();
			deleteHarddiskReader();
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
	 * return the hard disk redirection is running or not
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
		return sourceHarddisk;
	}


	/****
	 * Must be called to stop hard disk redirection thread abnormally.
	 *
	 */

	public void stopRedirectionAbnormal() {
		if (running) {
			stopRunning = true;
			harddiskDisconnect();
			running = false;
			stopRunning = false;
			closeHarddisk();
			deleteHarddiskReader();
			JViewerApp.getInstance().reportHarddiskAbnormal(HDDevice_no);
		}
	}

	/**
	 * Creating a thread to send/receive the packet from the HDserver
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

				/* Execute the hard disk request */
				int dataLen = executeHarddiskSCSICmd(packetReadBuffer,	packetWriteBuffer);
				packetWriteBuffer.limit(dataLen);
				if (request.opcode == SET_HARDDISK_TYPE)
				{
					packetWriteBuffer.position(11);
					packetWriteBuffer.putInt(31);
					dataLen = dataLen+1;
					packetWriteBuffer.limit(dataLen);
					packetWriteBuffer.position(packetWriteBuffer.limit()-1);
					packetWriteBuffer.put((byte)getDrive_Type());
				}
				packetWriteBuffer.position(0);

				if(request.opcode == IUSBSCSI.OPCODE_KILL_REDIR)
				{
					System.out.println("EXIT COMMAND RECEIVED IN FLOPPY : "+request.opcode );
					hdRedirectionKilled = true;
				}
				if (request.opcode == IUSBSCSI.OPCODE_EJECT) { // eject command for hard disk imgae or hard disk drive
					if (request.Lba == 2)
						hdImageEjected = true;
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
	}

	/****
	 * Getting the hard disk list from the Native call
	 * if any drive name drive name duplicated removing here and copying in another array
	 *
	 * @return
	 */
	public String[] getEntireHarddiskList() {
		if (!physicalDevice) {
			DisplayErrorMsg("Cannot get Hard disk drive list during Hard disk IMAGE redirection");
			return (null);
		}

		if (nativeReaderPointer == -1)
			newHarddiskReader(true);

		String[] ListDrive_original = listHardDrives();
		String[] ListDriveFixed_original = listHardDrivesFixed();
		String[] ListDrive_filter = null;

		if( ListDrive_original != null && ListDriveFixed_original != null)
			ListDrive_filter = new String[(ListDrive_original.length + ListDriveFixed_original.length)];
		else if(ListDriveFixed_original == null)
			ListDrive_filter = new String[ListDrive_original.length];
		else if(ListDrive_original == null)
			ListDrive_filter = new String[ListDriveFixed_original.length];

		int counter=0;
		if(ListDrive_original != null)
		{
			for(int j=0;j<ListDrive_original.length ;j++)
			{
				ListDrive_filter[counter]= ListDrive_original[j];
				counter++;
			}
		}
		if(ListDriveFixed_original != null)
		{
			for(int j=0;j<ListDriveFixed_original.length ;j++)
			{
				ListDrive_filter[counter]= ListDriveFixed_original[j];
				counter++;
			}
		}
		return (ListDrive_filter);
	}

	/****
	 * Getting the USB/HDD removalble drive  from the Native call
	 * if any drive name drive name duplicated removing here and copying in another array
	 *
	 * @return
	 */
	public String[] getUSBHDDList() {
		if (!physicalDevice) {
			DisplayErrorMsg("Cannot get Hard Disk drive list during hard disk IMAGE redirection");
			return (null);
		}

		if (nativeReaderPointer == -1)
			newHarddiskReader(true);

		String[] ListDrive_original = listHardDrives();

		if(ListDrive_original == null)
			return ListDrive_original;

		if(System.getProperty("os.name").startsWith("Win"))
		{
			for (int i = 0; i < ListDrive_original.length; i++) {
				String Drive = ListDrive_original[i];
				String Drive_no = Drive.substring(0,1);
				String Drive_name = Drive.substring(2);
				String Drive_name_append = "[";
				int j=0;
				while(j < Drive_name.length()-1){
					Drive_name_append += (Drive_name.charAt(j)+"-");
					j++;
				}
				Drive_name_append += (Drive_name.charAt(j)+"]");
				String Physicaldevice = "PhysicalDrive";
				String Wholedrive = Physicaldevice.concat(Drive_no).concat("-").concat(Drive_name_append);
				ListDrive_original[i] = Wholedrive;
			}
		}
		else if(System.getProperty("os.name").equals("Linux")){
			for (int i = 0; i < ListDrive_original.length; i++) {
				String Drive = ListDrive_original[i];
				String Drive_name = Drive.substring(0,Drive.length()-1);
				ListDrive_original[i] = Drive_name;
			}
		}
		
		return (ListDrive_original);
	}


	/****
	 * Getting the HD list from the Native call
	 * if any drive name drive name duplicated removing here and copying in another array
	 *
	 * @return
	 */
	public String[] getHarddiskFixedList() {
		if (!physicalDevice) {
			DisplayErrorMsg("Cannot get hard disk drive list during Hard disk IMAGE redirection");
			return (null);
		}

		if (nativeReaderPointer == -1)
			newHarddiskReader(true);

		String[] ListDrive_original =listHardDrivesFixed();

		if(ListDrive_original == null)
			return null;

		if(System.getProperty("os.name").startsWith("Win"))
		{
			for (int i = 0; i < ListDrive_original.length; i++) {
				String Drive = ListDrive_original[i];
				String Drive_no = Drive.substring(0,1);
				String Drive_name = Drive.substring(2);
				String Drive_name_append = "[";
				int j=0;
				while(j < Drive_name.length()-1){
					Drive_name_append += (Drive_name.charAt(j)+"-");
					j++;
				}
				Drive_name_append += Drive_name.charAt(j)+"]";
				String Physicaldevice = "PhysicalDrive";
				String Wholedrive = Physicaldevice.concat(Drive_no).concat("-").concat(Drive_name_append);
				ListDrive_original[i] = Wholedrive;
			}
		}
		else if(System.getProperty("os.name").equals("Linux")){
			for (int i = 0; i < ListDrive_original.length; i++) {
				String Drive = ListDrive_original[i];
				String Drive_name = Drive.substring(0,Drive.length()-1);
				ListDrive_original[i] = Drive_name;
			}
		}
		return (ListDrive_original);
	}

	/**
	 * Getting the libhd Version from the Native call
	 *
	 * @return
	 */
	public String getLIBHARDDISKVersion() {
		String version;

		if (nativeReaderPointer == -1) {
			newHarddiskReader(false);
			version = getVersion();
			deleteHarddiskReader();
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
	 * Return the hard disk drive is physical or image redirection
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
		packetWriteBuffer.position(22);
		packetWriteBuffer.put((byte)getDrive_Type());
		packetWriteBuffer.put((byte)HDDevice_no);
		packetWriteBuffer.position(0);
		IUSBSCSI pkt = new IUSBSCSI(packetWriteBuffer, true);
		packetMaster.sendPacket(pkt);
	}

	/**
	 * Return the data transfer rate of the HD redirection
	 * @return
	 */
	public int getBytesRedirected() {
		return nBytesRedirected;
	}

	public int getDrive_Type() {
		Debug.out.println("GEt DRIVE TYPE:"+Drive_Type);
		return Drive_Type;
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
	public void setDrive_Type(int drive_Type) {
		Drive_Type = drive_Type;
	}
	/**
	 * @return the hdInstanceNum
	 */
	public int getHdInstanceNum() {
		return hdInstanceNum;
	}
	/**
	 * @return the hdImageRedirected
	 */
	public boolean isHdImageRedirected() {
		return hdImageRedirected;
	}
	/**
	 * @param hdImageRedirected the hdImageRedirected to set
	 */
	public void setHdImageRedirected(boolean hdImageRedirected) {
		this.hdImageRedirected = hdImageRedirected;
	}
	/**
	 * @return the hdImageEjected
	 */
	public boolean isHdImageEjected() {
		return hdImageEjected;
	}
	/**
	 * @return the hdRedirectionKilled
	 */
	public boolean isHdRedirectionKilled() {
		return hdRedirectionKilled;
	}
}
