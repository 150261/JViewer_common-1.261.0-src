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
// JViewer main module.
//

package com.ami.kvm.jviewer;

import java.awt.Dimension;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.ami.kvm.jviewer.gui.JViewerApp;
import com.ami.kvm.jviewer.gui.LocaleStrings;
import com.ami.kvm.jviewer.gui.StandAloneConnectionDialog;


/**
 * JViewer main module class.
 */
public class JViewer {
	public final static String APP_TYPE_JVIEWER = "JViewer";
	public final static String APP_TYPE_PLAYER = "PlayVideo";
	public final static String APP_TYPE_DOWNLOAD_SAVE = "SaveVideo";
	public final static String APP_TYPE_STAND_ALONE = "StandAlone";
	public final static String APP_TYPE_WEB_PREVIEW = "WebPreview";
	public final static String APP_TYPE_BSOD_VIEWER = "BSODScreen";
	public final static int HTTPS_PORT = 443;
	public final static int INVALID_PORT = -1;
	public static final int MIN_FRAME_WIDTH = 800;
	public static final int MIN_FRAME_HEIGHT = 600;
	public static final int KVM_VMEDIA_ENABLED = 3;
	
	//BIT manuplation for  privilege
	//Extended privilege 
	public static final byte KVM_ENABLED = 0;
	public static final byte VMEDIA_ENABLED = 1;	
	
	//User privilege 
	public static final byte POWER_OPTION_PRIV = 8;
	
	public static final int LICENSED = 1;
	public static final String AUTO_DETECT_KEYBOARD = "AD";

	private static String OPTION_USAGE = LocaleStrings.getString("1_2_JVIEWER")+"JViewer.jar" +
										" < -apptype StandAlone> < -hostname "+
										LocaleStrings.getString("S_2_SACD")+" > < -u "+
										LocaleStrings.getString("S_4_SACD")+" > < -p "+
										LocaleStrings.getString("S_5_SACD")+"> < -webport "+
										LocaleStrings.getString("S_3_SACD")+"> <-lang "+
										LocaleStrings.getString("S_21_SACD")+">";
	
	private static JFrame mainFrame;
	private static JDesktopPane mainPane;
	//Default apptype will be set to StandAlone.
	//This will avoid problems while launching the StandAlone app by double clicking
	//the JViewer.jar or form cmmand prompt or terminal.
	private static String apptype = null;

	private static boolean standalone;

	private static String title = "JViewer";
	private static String ip;
	private static byte[] ServerIp;
	private static int kvmPort = 7578;
	private static int SecureChannel = 0;
	private static int VMSecureChannel = 0;
	private static int cdserver_port = 0;
	private static int fdserver_port = 0;
	private static int hdserver_port = 0;
	private static byte Num_CD = 1;
	private static byte Num_FD = 1;
	private static byte Num_HD = 1;
	private static int CD_State = 0;
	private static int FD_State = 0;
	private static int HD_State = 0;
	private static boolean useSSL = false;
	private static boolean VMUseSSL = false;
	private static String webSessionToken = null;
	private static String kvmToken= null;
	private static int kvmTokenType = 0;
	private static String lang= null;
	private static int webPort = INVALID_PORT; // default value set to an invalid port number.
	private static int webSecPort = INVALID_PORT; // default value set to an invalid port number.
	private static int webSecure = 0;
	private static String keyboardLayout = AUTO_DETECT_KEYBOARD;// Auto-Detect Keyboard layout
	private static String[] videoFile = null;
	
	private static String username = null;
	private static String password = null;
	private static boolean isSinglePortEnabled = false;
	private static int argLength = 0;
	private static int kvmPrivilege = KVM_VMEDIA_ENABLED;
	private static boolean unknownParam = false;
	private static String unknownParams = "";
	private static byte OEMFeatureStatus = 0;
	private static byte KVMLicenseStatus = 0;
	private static byte MediaLicenseStatus = 0;

	//Supported Host Physical keyboard layouts.
	private static final Set<String> KEYBOARD_LAYOUTS = new HashSet<String>(Arrays.asList
			(new String[] {"AD", "EN", "FR", "DE", "ES"}));

	/*apptype - first argument
	 * apptpe = "JViewer" // Jviewer Application
	 * apptrype = "PlayVideo" //Vidoe play only
	 * apptype = "DownlaodVideo"//Video play and save
	 * apptype = "StandAlone"//JViewer StandAlone Application
	 */

	//Modified for JInternalFrame
	/**
	 * main method. JViewer starts execution from here.
	 *
	 * @param args command line arguments.
	 */
	public static void main(String[] args) {
		mainFrame = new JFrame("JViewer");
		mainFrame.setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		standalone = true;
		argLength = args.length;

		ParseCmd(args);
		if(lang == null)
			setLanguage("EN");
		if(apptype == null)
			apptype = APP_TYPE_STAND_ALONE;
		if(args.length >0){// Should not go in when StandAloneApp is launched by double clicking the jar.
			Debug.out.println("JViewer Arguments\n");
			for(int p=0;p<args.length-1;p+=2)
			{
				Debug.out.println(args[p]+" : "+args[p+1]);
			}
		}		

		if(isjviewerapp())
		{
			Debug.out.println("JViewer Application Initialised");
			redirect();
		}
		else if(isplayerapp())
		{
			Debug.out.println("Player Application  Initialised");
			recording();
		}
		else if(isdownloadapp())
		{
			Debug.out.println("Downlaod and save Application  Initialised");
			recording();
		}
		else if(isStandAloneApp()){
			Debug.out.println("Stand Alone Application  Initialised");
			launchStandAlone();
		}
		
		else
		{
			printUsage();
		}

	}

	/**
	 * Checks the appliaction type is Jviewer else retuirn false
	 * @return	true- JViewer APP
	 * 			false - If not JVIewer App
	 */
	public static boolean isjviewerapp() {
		if(apptype != null) {
			if(apptype.compareToIgnoreCase(JViewer.APP_TYPE_JVIEWER) == 0)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks the appliaction type is Player else retuirn false
	 * @return	true- JViewer APP
	 * 			false - If not JVIewer App
	 */
	public static boolean isplayerapp() {
		if(apptype != null) {
			if(apptype.compareToIgnoreCase(JViewer.APP_TYPE_PLAYER) == 0)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks the appliaction type is Download and save else retuirn false
	 * @return	true- JViewer APP
	 * 			false - If not JVIewer App
	 */
	public static boolean isdownloadapp() {
		if(apptype != null) {
			if(apptype.compareToIgnoreCase(JViewer.APP_TYPE_DOWNLOAD_SAVE) == 0)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean IsBitSet(int value, int pos)
	{
	   return (value & (1 << pos)) != 0;
	}

	/**
	 * Checks whether application type is Stand Alone App. 
	 * @return  true if application type is Stand Alone App; false otherwise.
	 */
	public static boolean isStandAloneApp(){
		if(apptype != null) {
			if(apptype.compareToIgnoreCase(JViewer.APP_TYPE_STAND_ALONE) == 0)
			{
				return true;
			}
		}
		return false;
	}
	
	public static String getIp() {
		return ip;
	}

	public static void launch(JFrame frame, JDesktopPane pane,String[] args) {
		standalone = false;
		mainFrame = frame;
		mainPane = pane;
		redirect();
	}

	public static void recording(){

		String port=Integer.toString(webPort);
		JViewerApp.getInstance().Ondisplayvideo(ip,port,webSessionToken,webSecure);
	}

	/**
	 * Launches the JVIewer Application in Stand Alone Mode 
	 */
	public static void launchStandAlone(){
		
		JViewerApp.getInstance().onLaunchStandAloneApp(ip, webPort, username, password);		
	}
	//Added for JInternalFrame
	/**
	 *
	 * @param args
	 */
	public static void redirect() {

		if(isKVMEnabled()){
			JViewerApp.getInstance().OnConnectToServer(ip, kvmPort, kvmToken, kvmTokenType, useSSL,
					VMUseSSL, cdserver_port, fdserver_port, hdserver_port,Num_CD,Num_FD,Num_HD,CD_State,FD_State,HD_State,
					webSessionToken,webSecPort);
			JViewerApp.getInstance().create_IUSBSession();
		}
		else{
			JOptionPane.showMessageDialog(null, LocaleStrings.getString("1_5_JVIEWER"),
					LocaleStrings.getString("1_3_JVIEWER"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	public static void ParseCmd(String[] args){

		int i=0;
		int enabled = -1;
		String arg = null;
		while (i < args.length && args[i].startsWith("-"))
 		{
			try{
				arg = args[i++];
				//apptype
				if (arg.equals("-apptype"))
				{
					if (i < args.length)
					{
						apptype = args[i++];
					}
					else
						System.err.println("-apptype"+LocaleStrings.getString("1_4_JVIEWER"));
				}
				//get JViewer title from JViewer argumment
				else if(arg.equals("-title")){
					if(args[i] != null)
						title = args[i];
					else
						title = "JViewer";
					i++;
				}
				//hostname
				else if(arg.equals("-hostname"))
				{
					ip =args[i];
					ServerIp = getServerIP(args[i++]);
					if (ServerIp == null) {
						if(!isStandAloneApp()){
							printUsage();
							return;
						}
					}
				}
				//kvmtoken
				else if(arg.equals("-kvmtoken"))
				{
					try {
						kvmToken = args[i++];
					} catch (NumberFormatException e) {
						Debug.out.println(e);
						printUsage();
						return;
					}
				}
				//kvmtokentype
			else if(arg.equals("-kvmtokentype"))
			{
				try {
					kvmTokenType = Integer.parseInt(args[i++]);
				} catch (NumberFormatException e) {
					Debug.out.println(e);
					printUsage();
					return;
				}
			}
				//kvmsecure
				else if(arg.equals("-kvmsecure"))
				{
					try {
						SecureChannel = Integer.parseInt(args[i++]);
						if (SecureChannel != 0 && SecureChannel != 1){
							printUsage();
							return;
						}
						useSSL = (SecureChannel == 1) ? true : false;
					}
					catch (NumberFormatException e) {
						Debug.out.println(e);
						printUsage();
						return;
					}
				}
				//kvmport
				else if(arg.equals("-kvmport"))
				{
					try {
						kvmPort = Integer.parseInt(args[i++]);
					} catch (NumberFormatException e) {
						Debug.out.println(e);
						printUsage();
						return;
					}
				}
				//vmsecure
				else if(arg.equals("-vmsecure"))
				{
					try {
						VMSecureChannel = Integer.parseInt(args[i++]);
						if (VMSecureChannel != 0 && VMSecureChannel != 1) {
							printUsage();
							return;
						}
						VMUseSSL = (VMSecureChannel == 1) ? true : false;
					}
					catch (NumberFormatException e) {
						Debug.out.println(e);
						printUsage();
						return;
					}
				}
				//cdstate
				else if(arg.equals("-cdstate"))
				{
					try {
						CD_State = Integer.parseInt(args[i++]);
					}
					catch (NumberFormatException e) {
						Debug.out.println(e);
						printUsage();
						return;
					}
				}
				//cdport
				else if(arg.equals("-cdport"))
				{
					try {
						cdserver_port = Integer.parseInt(args[i++]);
					} catch (NumberFormatException e) {
						Debug.out.println(e);
						printUsage();
						return;
					}
				}
				//cdnum
				else if(arg.equals("-cdnum"))
				{
					try {
						Num_CD = (byte) Integer.parseInt(args[i++]);
					} catch (NumberFormatException e) {
						printUsage();
						return;
					}
				}
				//fdstate
				else if(arg.equals("-fdstate"))
				{
					try {
						FD_State = Integer.parseInt(args[i++]);
					}
					catch (NumberFormatException e) {
						Debug.out.println(e);
						printUsage();
						return;
					}
				}
				//fdport
				else if(arg.equals("-fdport"))
				{
					try {
						fdserver_port = Integer.parseInt(args[i++]);
					} catch (NumberFormatException e) {
						Debug.out.println(e);
						printUsage();
						return;
					}
				}
				//fdnum
				else if(arg.equals("-fdnum"))
				{
					try {
						Num_FD = (byte) Integer.parseInt(args[i++]);
					} catch (NumberFormatException e) {
						printUsage();
						return;
					}
				}
				//hdstate
				else if(arg.equals("-hdstate"))
				{
					try {
						HD_State = Integer.parseInt(args[i++]);
					}
					catch (NumberFormatException e) {
						printUsage();
						return;
					}
				}
				//hdport
				else if(arg.equals("-hdport"))
				{
					try {
						hdserver_port = Integer.parseInt(args[i++]);
					} catch (NumberFormatException e) {
						printUsage();
						return;
					}
				}
				//hdnum
				else if(arg.equals("-hdnum"))
				{
					try {
						Num_HD = (byte) Integer.parseInt(args[i++]);
					} catch (NumberFormatException e) {
						Debug.out.println(e);
						printUsage();
						return;
					}
				}
				//lang
				else if(arg.equals("-localization"))
				{
					lang = args[i++];
					if(lang.equals(null) || lang.length() == 0){
						if(isStandAloneApp())
							JOptionPane.showMessageDialog(null, LocaleStrings.getString("1_1_JVIEWER")
									+arg+" !!!\n"+OPTION_USAGE,LocaleStrings.getString("1_3_JVIEWER"), 
									JOptionPane.ERROR_MESSAGE);
						printUsage();
						return;
					}
					JViewer.setLanguage(lang);
				}
				//webcookie
				else if(arg.equals("-webcookie"))
				{
					try {
						webSessionToken = args[i++];
					}
					catch (NumberFormatException e) {
						Debug.out.println(e);
						printUsage();
						return;
					}
				}
				//websecure
				else if(arg.equals("-websecure"))
				{
					try {
						webSecure = Integer.parseInt(args[i++]);
					}
					catch (NumberFormatException e) {
						Debug.out.println(e);
						printUsage();
						return;
					}
				}
				//webport
				else if(arg.equals("-webport"))
				{
					webPort = getWebPortNumber(args[i++]);
				}
				//websecureport
				else if(arg.equals("-websecureport"))
				{
						webSecPort = getWebPortNumber(args[i++]);				
				}

				else if(arg.equals("-keyboardlayout"))
				{
					keyboardLayout = args[i++];
				}
				
				//videofile
				else if(arg.equals("-videofile"))
				{
					try {
						if(videoFile == null)
							videoFile = new String[1];
						videoFile[0] = args[i++];
					}
					catch (NumberFormatException e) {
						Debug.out.println(e);
						printUsage();
						return;
					}
				}
				// username and password for StandAlone app
				else if(arg.equalsIgnoreCase("-u")){
					username = args[i++];
				}
				else if(arg.equalsIgnoreCase("-p")){
					password = args[i++];
				}
				else if(arg.equals("-singleportenabled"))
				{
					try {
						enabled = Integer.parseInt(args[i++]);
						if (enabled != 0 && enabled != 1){
							printUsage();
							return;
						}
						isSinglePortEnabled = (enabled == 1) ? true : false;
					}
					catch (NumberFormatException e) {
						Debug.out.println(e);
						printUsage();
						return;
					}
				}
				else if(arg.equals("-extendedpriv")){
					kvmPrivilege = Integer.parseInt(args[i++]);
				}
				else if(arg.equals("-oemfeatures")){
					try{
					OEMFeatureStatus = Byte.parseByte(args[i++]);
					}catch (NumberFormatException nfe) {
						Debug.out.println(nfe);
						OEMFeatureStatus = 0;
					}
				}
				else{
					unknownParam = true;
					if(unknownParams.length() <= 0)
						unknownParams += arg;
					else
						unknownParams += ", "+arg;
					printUsage();
					i++;
				}

			}catch(Exception e){
				Debug.out.println(e);
				if(isStandAloneApp())
					JOptionPane.showMessageDialog(null, LocaleStrings.getString("1_1_JVIEWER")
							+arg+" !!!\n"+OPTION_USAGE,LocaleStrings.getString("1_3_JVIEWER"), 
							JOptionPane.ERROR_MESSAGE);
				printUsage();
			}
		}

		if(isStandAloneApp() || apptype == null){
			if(unknownParams.length() > 0)
			JOptionPane.showMessageDialog(null, LocaleStrings.getString("1_1_JVIEWER")
					+unknownParams+" !!!\n"+OPTION_USAGE,LocaleStrings.getString("1_1_JVIEWER"), 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * print usage message.
	 */
	public static void printUsage() {
		if(isStandAloneApp() || apptype == null){
			Debug.out.println(OPTION_USAGE);
		}
		else{
			Debug.out.println("Invalid arguments, please try again");
			Debug.out
			.println("Usage: java -jar JViewer.jar <apptype> <ip address> <KVM port number> <token> <ssl for KVM> <ssl for vmedia> <Number of parallel Floppy Redirection> <Number of parallel CD/DVD Redirection>  <Number of parallel Hard disk Redirection> <cdserver port> <fdserver port> <hdserver port> <user privileges> <language> <token>");
			Debug.out
			.println("<apptype> JViewer for JViewer App,Player for playing the video,SAveVideo for download and save video in cleint system");
			Debug.out
			.println("<ssl for KVM> 1 for secure connection and 0 for non-secure ");
			Debug.out
			.println("<ssl for vmedia> 1 for secure connection and 0 for non-secure ");
			Debug.out.println("<user privileges> ");
			Debug.out
			.println("                  0x00000020 - VKVM permissions only");
			Debug.out
			.println("                  0x00000040 - VMedia permissions only");
			Debug.out
			.println("                  0x00000060 - VKVM & VMedia permissions");
			Debug.out.println("<language> ");
			Debug.out.println("                  EN - English");
		}
		if(JViewer.isStandalone()){
			if(!unknownParam)
				JViewer.exit(0);
			else
				unknownParam = false;
		}
		else
			JViewerApp.getInstance().getMainWindow().dispose();
	}

    /*
     * Get server ip address in byte array.
     *
     * @param ipStr ip address in string format.
     * @return server ip address.
     */
    public static byte[] getServerIP(String ipStr)
    {
    	byte[] ipDgt = null ;    	
    	try {
			ip = ipStr;
			InetAddress hostAddress = InetAddress.getByName(ipStr);
			ipStr = hostAddress.getHostAddress();
			ipDgt = InetAddress.getByName(ipStr).getAddress();
			Debug.out.println("Resolving to IPAddress " + ipStr);

			for(int i=0;i<ipDgt.length;i++)
				Debug.out.print(ipDgt[i]);
		} catch (UnknownHostException e) {
			ip = null;
			Debug.out.println("Error Resolving IP address");
			Debug.out.println(e);
		}

        return ipDgt;
    }

	/**
	 * @param hostName the hostName to set
	 */
	public static void setServerIP(byte[] hostName) {		
		JViewer.ServerIp = hostName;
	}

	public static JFrame getMainFrame() {
		return mainFrame;
	}

	public static void setMainFrame(JFrame mainFrame) {
		JViewer.mainFrame = mainFrame;
	}

	public static JDesktopPane getMainPane() {
		return mainPane;
	}

	public static void setMainPane(JDesktopPane mainPane) {
		JViewer.mainPane = mainPane;
	}

	public static boolean isStandalone() {
		return standalone;
	}

	public static void setStandalone(boolean standalone) {
		JViewer.standalone = standalone;
	}
	/**
	 * @param ip the ip to set
	 */
	public static void setIp(String ip) {
		JViewer.ip = ip;
	}

	/**
	 * @param kvmport the port to set
	 */
	public static void setKVMPort(int kvmport) {
		JViewer.kvmPort = kvmport;
	}

	/**
	 * @param sessionCookies the sessionCookies to set
	 */
	public static void setSessionCookies(String sessionCookies) {
		JViewer.kvmToken = sessionCookies;
	}

	/**
	 * @param webSessionToken the webSessionToken to set
	 */
	public static void setWebSessionToken(String webSessionToken) {
		JViewer.webSessionToken = webSessionToken;
	}
	/**
	 * @param secureChannel the secureChannel to set
	 */
	public static void setSecureChannel(int secureChannel) {
		SecureChannel = secureChannel;
	}

	/**
	 * @param vMSecureChannel the vMSecureChannel to set
	 */
	public static void setVMSecureChannel(int vMSecureChannel) {
		VMSecureChannel = vMSecureChannel;
	}

	/**
	 * @param cdserver_port the cdserver_port to set
	 */
	public static void setCdserver_port(int cdserver_port) {
		JViewer.cdserver_port = cdserver_port;
	}

	/**
	 * @param fdserver_port the fdserver_port to set
	 */
	public static void setFdserver_port(int fdserver_port) {
		JViewer.fdserver_port = fdserver_port;
	}

	/**
	 * @param hdserver_port the hdserver_port to set
	 */
	public static void setHdserver_port(int hdserver_port) {
		JViewer.hdserver_port = hdserver_port;
	}

	/**
	 * @param num_CD the num_CD to set
	 */
	public static void setNum_CD(byte num_CD) {
		Num_CD = num_CD;
	}

	/**
	 * @param num_FD the num_FD to set
	 */
	public static void setNum_FD(byte num_FD) {
		Num_FD = num_FD;
	}

	/**
	 * @param num_HD the num_HD to set
	 */
	public static void setNum_HD(byte num_HD) {
		Num_HD = num_HD;
	}

	/**
	 * @param cD_State the cD_State to set
	 */
	public static void setCD_State(int cD_State) {
		CD_State = cD_State;
	}

	/**
	 * @param fD_State the fD_State to set
	 */
	public static void setFD_State(int fD_State) {
		FD_State = fD_State;
	}

	/**
	 * @param hD_State the hD_State to set
	 */
	public static void setHD_State(int hD_State) {
		HD_State = hD_State;
	}

	/**
	 * @return the useSSL
	 */
	public static boolean isUseSSL() {
		return useSSL;
	}

	/**
	 * @param useSSL the useSSL to set
	 */
	public static void setUseSSL(boolean useSSL) {
		JViewer.useSSL = useSSL;
	}

	/**
	 * @param vMUseSSL the vMUseSSL to set
	 */
	public static void setVMUseSSL(boolean vMUseSSL) {
		VMUseSSL = vMUseSSL;
	}

	/**
	 * Gets the language code.
	 * @return - language code.
	 */
	public static String getLanguage() {
		return lang;
	}
	
	/**
	 * @param language the language to set
	 */
	public static void setLanguage(String language) {
		JViewer.lang = language;
		LocaleStrings.setLanguageID(lang);
		StandAloneConnectionDialog.setSelectedLocale(lang);
	}

	/**
	 * Sets the localization language as English - US (EN)
	 */
	public static void setDefaultLanguage() {
		JViewer.lang = "EN";
		StandAloneConnectionDialog.setSelectedLocale(lang);
	}
	
	/**
	 * @return the argLength
	 */
	public static int getArgLength() {
		return argLength;
	}

	/**
	 * @return the webPort
	 */
	public static int getWebPortNumber(String port) {
		int wPort;
		try {
			wPort = Integer.parseInt(port);
		}
		catch (NumberFormatException e) {
			wPort = INVALID_PORT;
			Debug.out.println("Invalid port number");
			Debug.out.println(e);
		}
		return wPort;
	}

	/**
	 * @param webPort the webPort to set
	 */
	public static void setWebPort(int webPort) {
		JViewer.webPort = webPort;
	}

	public static boolean isSinglePortEnabled() {
		return isSinglePortEnabled;
	}

	public static void setSinglePortEnabled(boolean isSinglePortEnabled) {
		JViewer.isSinglePortEnabled = isSinglePortEnabled;
	}
	public static void setApptype(String type)  {
		apptype = type;
	}
	public static boolean isWebPreviewer() {
		if(apptype != null) {
			if(apptype.compareToIgnoreCase(JViewer.APP_TYPE_WEB_PREVIEW) == 0)
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isBSODViewer() {
		if(apptype != null) {
			if(apptype.compareToIgnoreCase(JViewer.APP_TYPE_BSOD_VIEWER) == 0)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the keyboardLayout
	 */
	public static String getKeyboardLayout() {
		return keyboardLayout;
	}

	/**
	 * @param keyboardLayout the keyboardLayout to set
	 */
	public static void setKeyboardLayout(String keyboardLayout) {
		if(KEYBOARD_LAYOUTS.contains(keyboardLayout))
			JViewer.keyboardLayout = keyboardLayout;
		else
			JViewer.keyboardLayout = "AD";
	}

	public static String[] getVideoFile() {
		return videoFile;
	}

	public static void setVideoFile(String[] videoFile) {
		JViewer.videoFile = videoFile;
	}

	/**
	 * @return the kvmPrevilege
	 */
	public static int getKVMPrivilege() {
		return kvmPrivilege;
	}

	/**
	 * @param kvmPrivilege the kvmPrevilege to set
	 */
	public static void setKVMPrivilege(int kvmPrivilege) {
		JViewer.kvmPrivilege = kvmPrivilege;
	}

	/**
	 * Returns whether KVM privilege is enabled or not.
	 * @return true if KVM privilege is enabled, and false otherwise.
	 */
	public static boolean isKVMEnabled(){
		//if the 0th bit in kvm privilege is 1, then KVM is enabled 
		return IsBitSet(kvmPrivilege,KVM_ENABLED);
	}
	
	/**
	 * Returns whether VMedia privilege is enabled or not.
	 * @return true if VMedia privilege is enabled, and false otherwise.
	 */
	public static boolean isVMediaEnabled(){
		//if the 1st bit in kvm previlege 1, then VMedia is enabled 
		return IsBitSet(kvmPrivilege,VMEDIA_ENABLED);
	}
	
	/**
	 * Returns whether Power option privilege is enabled or not.
	 * @return true if Power option privilege is enabled, and false otherwise.
	 */
	public static boolean isPowerPrivEnabled(){
		//if the 8th bit in kvm previlege 1, then power privilege is enabled 
		return IsBitSet(kvmPrivilege,POWER_OPTION_PRIV);
	}

	/**
	 * Get the title given to the application 
	 */
	public static String getTitle(){
		return title;
	}

	/**
	 * Returns the current status denoting whether the OEM specifc features are enabled or disabled
	 */
	public static byte getOEMFeatureStatus(){
		return OEMFeatureStatus;
	}

	/**
	 * @param oEMFeatureStatus the oEMFeatureStatus to set
	 */
	public static void setOEMFeatureStatus(byte OEMFeatureStatus) {
		JViewer.OEMFeatureStatus = OEMFeatureStatus;
	}
	/**
	 * Returns the current status denoting whether the KVM has Valid License or not
	 */
	public static byte getKVMLicenseStatus(){
		return KVMLicenseStatus;
	}

	/**
	 * @param KVMLicenseStatus the KVMLicenseStatus to set
	 */
	public static void setKVMLicenseStatus(byte KVMLicenseStatus) {
		JViewer.KVMLicenseStatus = KVMLicenseStatus;
	}
	/**
	 * Returns the current status denoting whether the Media has Valid License or not
	 */
	public static byte getMediaLicenseStatus(){
		return MediaLicenseStatus;
	}

	/**
	 * @param MediaLicenseStatus the MediaLicenseStatus to set
	 */
	public static void setMediaLicenseStatus(byte MediaLicenseStatus) {
		JViewer.MediaLicenseStatus = MediaLicenseStatus;
	}
	
	/**
	 * @return the webSecure
	 */
	public static int getWebSecure() {
		return webSecure;
	}

	/**
	 * @param webSecure the webSecure to set
	 */
	public static void setWebSecure(int webSecure) {
		JViewer.webSecure = webSecure;
	}

	public static void exit(int status){
		if(Debug.MODE == Debug.CREATE_LOG)
			Debug.out.closeLog();
		System.exit(status);
	}
}
