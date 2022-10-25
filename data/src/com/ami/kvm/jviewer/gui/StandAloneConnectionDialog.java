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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.videorecord.URLProcessor;

/**
 * The StandAloneConnectionDialog creates and shows the StandAloneApp connection dialog through which
 * user can provide required information to establish a connection with the Host. 
 */
@SuppressWarnings("serial")
public class StandAloneConnectionDialog  extends JDialog implements ActionListener, ItemListener{

	private String hostIP = null;
	private String username = null;
	private String password = null;	
	private String webSessionToken = null;
	private static String selectedLocale = "EN";
	private int secWebPort = JViewer.HTTPS_PORT;// default https port number for secure web port.

	private JPanel textPanel;
	private JPanel btnPanel;
	private JPanel helpPanel;
	private JPanel progressPanel;
	private JPanel bottomPanel;
	private JTable videoTable;
	private JLabel ipLabel;
	private JLabel portLabel;
	private JLabel unameLabel;
	private JLabel passwdLabel;
	private JLabel localeLabel;
	private JTextField ipText;
	private JTextField portText;
	private JTextField unameText;
	private JPasswordField passwdText;
	private JButton standaloneBtn;
	private JButton videoBtn;
	private JButton cancelBtn;	
	private JComboBox localeList;
	private JLabel helpTextLabel;
	private DefaultTableModel model;

	private URLProcessor urlProcessor;
	private Validator validator;
	private DialogKeyListener keyListener;
	private DialogWindowListener windowListener;
	private TextFieldFocusListener textFieldFocusListener;
	private boolean validate = true;
	private final int WIDTH = 410;
	private final int HEIGHT = 280;
	private final int SECURE_CONNECT = 1;
	private final int HTTPS_CONNECTION_SUCCESS = 0;
	private final int ADMIN_USER = 4;
	private final int OEM_PROPRIETARY_USER = 5;

	private boolean firstCheck = true;
	private boolean webLogIn = false;
	private boolean buttonsEnabled = false;
	private String[][] videoFile;
	private int remotePathSupport = 0;
	private boolean launching = false;
	private ProgressThread progThread;
	/**
	 * The COnstructor.
	 * @param parent - The parent frame on which the dialog will be shown.
	 * @param hostIP - The IP address of the host.
	 * @param username - user name to log into the BMC.
	 * @param password - password to log into the BMC.
	 */
	public StandAloneConnectionDialog(JFrame parent, String hostIP, int webPort, String username,String password) {	

		super(parent, LocaleStrings.getString("S_1_SACD")+JViewer.getTitle(), false);

		this.username = username;
		this.password = password;
		this.hostIP = hostIP;
		this.secWebPort = webPort;
		selectedLocale = JViewer.getLanguage();
		windowListener = new DialogWindowListener();
		addWindowListener(windowListener);
		constructDialog();
		enableDialog();
	}	


	/**
	 * Constructs the StandAloneConnection dialog.
	 */

	private void constructDialog(){		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		keyListener = new DialogKeyListener();
		textFieldFocusListener = new TextFieldFocusListener();
		setBounds(new Rectangle((screenSize.width - WIDTH)/2, (screenSize.height - HEIGHT)/2, WIDTH, HEIGHT));
		setLayout(new BorderLayout());
		setResizable(false);
		textPanel = new JPanel();

		textPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridCons = new GridBagConstraints();

		gridCons.fill = GridBagConstraints.HORIZONTAL;
		gridCons.insets = new Insets(10, 10, 5, 10);		
		gridCons.gridx = 0;
		gridCons.gridy = 0;
		gridCons.gridwidth = 1;
		gridCons.gridheight = 1;
		gridCons.weightx = 0.0;

		ipLabel = new JLabel(LocaleStrings.getString("S_2_SACD")+" : "); 	
		textPanel.add(ipLabel, gridCons);

		gridCons.insets = new Insets(5, 10, 5, 10);
		gridCons.gridy = 1;

		portLabel = new JLabel(LocaleStrings.getString("S_3_SACD")+" : ");
		textPanel.add(portLabel, gridCons);

		gridCons.gridy = 2;

		unameLabel = new JLabel(LocaleStrings.getString("S_4_SACD")+" : ");
		textPanel.add(unameLabel, gridCons);

		gridCons.gridy = 3;

		passwdLabel = new JLabel(LocaleStrings.getString("S_5_SACD")+" : ");
		textPanel.add(passwdLabel, gridCons);	

		gridCons.gridy = 4;

		localeLabel = new JLabel(LocaleStrings.getString("S_21_SACD")+" : ");
		textPanel.add(localeLabel, gridCons);

		gridCons.insets = new Insets(10, 10, 5, 10);
		gridCons.gridx = 1;
		gridCons.gridy = 0;
		gridCons.gridwidth = 1;
		gridCons.gridheight = 1;
		gridCons.weightx = 1.0;

		ipText = new JTextField();
		if(hostIP == null)
			ipText.setText("");
		else
			ipText.setText(hostIP);
		ipText.addKeyListener(keyListener);
		ipText.addFocusListener(textFieldFocusListener);
		textPanel.add(ipText, gridCons);

		gridCons.insets = new Insets(5, 10, 5, 10);
		gridCons.gridy = 1;

		portText = new JTextField();
		if(secWebPort < 0)
			portText.setText("");
		else
			portText.setText(String.valueOf(secWebPort));
		portText.addKeyListener(keyListener);
		portText.addFocusListener(textFieldFocusListener);
		textPanel.add(portText, gridCons);

		gridCons.gridy = 2;

		unameText = new JTextField();
		unameText.setText(username);
		unameText.addKeyListener(keyListener);
		unameText.addFocusListener(textFieldFocusListener);
		textPanel.add(unameText, gridCons);	

		gridCons.gridy = 3;

		passwdText = new JPasswordField();
		passwdText.setText(password);
		passwdText.addKeyListener(keyListener);
		passwdText.addFocusListener(textFieldFocusListener);
		textPanel.add(passwdText, gridCons);

		gridCons.gridy = 4;
		localeList = new JComboBox(getSupportedLocales());
		Locale locale = new Locale(selectedLocale.toLowerCase());
		String language = locale.getDisplayLanguage(new Locale(selectedLocale));
		localeList.setSelectedItem(selectedLocale+" - "+language);
		localeList.setAutoscrolls(true);
		localeList.addItemListener(this);
		textPanel.add(localeList, gridCons);

		getContentPane().add(textPanel, BorderLayout.NORTH);

		helpPanel = new JPanel();
		helpTextLabel = new JLabel(LocaleStrings.getString("S_38_SACD"));
		helpPanel.add(helpTextLabel);

		progressPanel = new JPanel();
		btnPanel = new JPanel();
		btnPanel.setLayout(new GridBagLayout());

		gridCons = new GridBagConstraints();

		gridCons.insets = new Insets(2, 15, 5, 10);
		gridCons.weightx = 0.0;

		standaloneBtn = new JButton(LocaleStrings.getString("S_6_SACD"));
		btnPanel.add(standaloneBtn, gridCons);

		gridCons.insets = new Insets(2, 5, 5, 10);
		gridCons.gridx = GridBagConstraints.RELATIVE;

		videoBtn = new JButton(LocaleStrings.getString("S_27_SACD"));
		btnPanel.add(videoBtn, gridCons);

		gridCons.insets = new Insets(2, 5, 5, 15);
		gridCons.gridx = GridBagConstraints.RELATIVE;

		cancelBtn = new JButton(LocaleStrings.getString("S_7_SACD"));
		cancelBtn.addActionListener(this);
		cancelBtn.addKeyListener(keyListener);
		btnPanel.add(cancelBtn, gridCons);

		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridBagLayout());
		gridCons = new GridBagConstraints();
		gridCons.insets = new Insets(5, 15, 5, 10);
		gridCons.weightx = 1.0;
		gridCons.gridy = 0;
		bottomPanel.add(helpPanel, gridCons);
		gridCons.insets = new Insets(0, 15, 0, 10);
		gridCons.weightx = 1.0;
		gridCons.gridy = 1;
		bottomPanel.add(btnPanel, gridCons);
		getContentPane().add(bottomPanel);
		this.setAlwaysOnTop(true);
	}	

	private void clearDialog() {
		getContentPane().removeAll();
		textPanel.removeAll();
		textPanel.updateUI();
		getContentPane().add(textPanel,BorderLayout.NORTH);
		getContentPane().add(btnPanel);
	}

	public abstract class MyTableModel extends AbstractTableModel {

		public boolean isEditable() {
			return false;
		}
	}

	private void constructTableDialog() {
		clearDialog();

		Object columnNames[] = { LocaleStrings.getString("S_32_SACD"),
				LocaleStrings.getString("S_33_SACD") };

		model = new DefaultTableModel(null, columnNames);
		videoTable = new JTable(model) {

			public boolean isCellEditable(int rowIndex, int vColIndex) {
				return false;
			}
		};
		textPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridCons = new GridBagConstraints();

		gridCons.fill = GridBagConstraints.BOTH;
		gridCons.insets = new Insets(10, 10, 5, 10);
		gridCons.gridx = 0;
		gridCons.gridy = 0;
		gridCons.gridwidth = 1;
		gridCons.gridheight = 1;
		gridCons.weightx = 1.0;
		gridCons.weighty = 0.5;

		JTableHeader header = videoTable.getTableHeader();
		header.setFont(new Font("Tahoma", Font.BOLD, 12));

		//un comment the below line to restrice user to select single selection
		// videoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		videoTable.setCellEditor(null);

		videoTable.addKeyListener(keyListener);
		videoTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (!buttonsEnabled) {
					enableDialog();
					buttonsEnabled = true;
				}
			}
		});
		videoTable.setBackground(Color.white);
		JTableHeader tableHeader = videoTable.getTableHeader();

		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		Border style = BorderFactory.createTitledBorder(border, "",
				TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
		tableHeader.setBorder(style);
		videoTable.getTableHeader().setPreferredSize(new Dimension(50, 23));

		JScrollPane pane = new JScrollPane(videoTable);
		gridCons.gridy = 1;
		textPanel.add(pane, gridCons);
		gridCons.gridy = 2;
		textPanel.add(btnPanel, gridCons);

		standaloneBtn.setText(LocaleStrings.getString("S_30_SACD"));
		videoBtn.setText(LocaleStrings.getString("S_31_SACD"));

		textPanel.updateUI();
		getContentPane().add(textPanel);
		this.setTitle(LocaleStrings.getString("S_29_SACD"));

	}
	/**
	 * Shows the StandAloneConnection dialog box if any of the runtime arguments, host IP address, user name, 
	 * or password is invalid. This dialog will prompt the user to enter the correct values. 
	 */

	private void showDialog(){
		disableDialog();
		validate = true;
		launching = false;
		validator = new Validator();
		validator.start();
		this.setVisible(true);
	}
	/**
	 * Returns the StandAloneConnectionDialog object. 
	 * @return - StandAloneConnectionDialog object.
	 */
	private JDialog getDialog(){
		return this;
	}

	/**
	 * Enables all the controls in the Dialog.
	 */
	private void enableDialog(){
		if (JViewer.isStandAloneApp())
			this.setTitle(LocaleStrings.getString("S_1_SACD"));
		ipText.setEditable(true);
		portText.setEditable(true);
		unameText.setEditable(true);
		passwdText.setEditable(true);
		standaloneBtn.setEnabled(true);
		standaloneBtn.addActionListener(this);
		standaloneBtn.addKeyListener(keyListener);
		videoBtn.setEnabled(true);
		videoBtn.addActionListener(this);
		videoBtn.addKeyListener(keyListener);
		localeList.setEnabled(true);
		showProgress(false);
		this.setVisible(true);
	}

	/**
	 * Disables all the controls in the Dialog.
	 */
	private void disableDialog(){	
			setDialogTitle(LocaleStrings.getString("S_8_SACD"));
		standaloneBtn.requestFocus();
		ipText.setEditable(false);
		portText.setEditable(false);
		unameText.setEditable(false);
		passwdText.setEditable(false);
		standaloneBtn.setEnabled(false);
		standaloneBtn.removeActionListener(this);
		standaloneBtn.removeKeyListener(keyListener);
		videoBtn.setEnabled(false);
		videoBtn.removeActionListener(this);
		videoBtn.removeKeyListener(keyListener);
		localeList.setEnabled(false);
		showProgress(true);
	}

	/**
	 * Shows the progress bar on StanAloneConnection dialog, while connecting to the web server.
	 * @param state - if true the progress bar will be shown, and hides the progress bar if false.
	 * 
	 */
	private void showProgress(boolean state){
		GridBagConstraints gridCons = new GridBagConstraints();
		gridCons.fill = GridBagConstraints.HORIZONTAL;
		gridCons.insets = new Insets(5, 20, 5, 15);
		gridCons.weightx = 1.0;
		gridCons.gridy = 0;
		if(state){
			helpTextLabel.setText("");
			bottomPanel.remove(helpPanel);
			progressPanel.removeAll();
			progressPanel.setLayout(new GridBagLayout());
			gridCons.ipady = 5;
			JProgressBar launchProgressBar = new JProgressBar();
			progressPanel.add(launchProgressBar, gridCons);
			launchProgressBar.setString(LocaleStrings.getString("S_8_SACD"));
			launchProgressBar.setStringPainted(true);
			launchProgressBar.setIndeterminate(true);
			bottomPanel.add(progressPanel, gridCons);
		}
		else{
			bottomPanel.remove(progressPanel);
			if(JViewer.isStandAloneApp()){
				helpTextLabel.setText(LocaleStrings.getString("S_38_SACD"));
				bottomPanel.add(helpPanel, gridCons);
			}
			else{
				helpTextLabel.setText("");
			}
		}
	}

	/**
	 * Gets the web session token using https request.
	 * @return Web session token.
	 */
	private int getWebSessionToken() {
		urlProcessor = new URLProcessor(null, SECURE_CONNECT);
		int ret = urlProcessor.connect_url_lock("https://"+hostIP+":"+secWebPort+
				"/rpc/WEBSES/create.asp?WEBVAR_USERNAME="+username+"&WEBVAR_PASSWORD="+password);
		try{
			if(ret == URLProcessor.INVALID_CREDENTIALS){
				validate = false;
				launching = false;
				enableDialog();
				ipText.setText(JViewer.getIp());
				unameText.setText(null);
				passwdText.setText(null);
				JOptionPane.showMessageDialog(getDialog(), LocaleStrings.getString("S_10_SACD"),
						JViewer.getTitle()+LocaleStrings.getString("S_9_SACD"), JOptionPane.ERROR_MESSAGE);
				unameText.requestFocus();
			}
			else if(ret == HTTPS_CONNECTION_SUCCESS){	
				webSessionToken = urlProcessor.getValue("'SESSION_COOKIE' : '", ',');
				webSessionToken.trim();				
				webSessionToken = webSessionToken.substring(0, webSessionToken.lastIndexOf('\''));
				JViewer.setWebSessionToken(webSessionToken);
			}
			else if(ret == URLProcessor.HTTP_REQUEST_FAILURE){
				launching = false;
				JOptionPane.showMessageDialog(getDialog(), LocaleStrings.getString("S_11_SACD"), 
						LocaleStrings.getString("S_9_SACD"), JOptionPane.ERROR_MESSAGE);
				windowListener.windowClosing(null);
			}	
		}catch(Exception e){
			launching = false;
			Debug.out.println(e);
			JOptionPane.showMessageDialog(getDialog(), LocaleStrings.getString("S_11_SACD"), 
					LocaleStrings.getString("S_9_SACD"), JOptionPane.ERROR_MESSAGE);
			windowListener.windowClosing(null);
		}		
		return ret;
	}

	/**
	 * Gets the KVM privilege of the given user.
	 * @return the user privilege.
	 */
	private int getKVMPrivilege(){
		int kvmPrivilege = 0;
		urlProcessor = new URLProcessor(webSessionToken, SECURE_CONNECT);
		int ret = urlProcessor.connect_url_lock("https://"+this.hostIP+":"+secWebPort+"/rpc/getrole.asp");
		try{
			if(ret == HTTPS_CONNECTION_SUCCESS){
				kvmPrivilege = Integer.parseInt(urlProcessor.getValue("'EXTENDED_PRIV' : ", ' '));
			}
		}catch (Exception e) {
			Debug.out.println(e);
			kvmPrivilege = 3;
		}
		JViewer.setKVMPrivilege(kvmPrivilege);
		if(!JViewer.isKVMEnabled()){
			validate = false;
			launching = false;
			enableDialog();
			ipText.setText(JViewer.getIp());
			unameText.setText(null);
			passwdText.setText(null);
			JOptionPane.showMessageDialog(getDialog(), LocaleStrings.getString("S_12_SACD")+username+
					LocaleStrings.getString("S_13_SACD"), LocaleStrings.getString("S_9_SACD"),
					JOptionPane.ERROR_MESSAGE);
			unameText.requestFocus();
			ret = -1;
		}
		return ret;
	}

	/**
	 * Gets the adviser session token value using https request and
	 * using https request and. 
	 */
	private void getAdviserSessionToken() {
		String adviserSessionToken = null;
		urlProcessor = new URLProcessor(webSessionToken, SECURE_CONNECT);
		int ret = urlProcessor.connect_url_lock("https://"+this.hostIP+":"+secWebPort+"/rpc/getsessiontoken.asp");
		try{
			if(ret == HTTPS_CONNECTION_SUCCESS){
				adviserSessionToken = urlProcessor.getValue("'SESSION_TOKEN' : '",',');
				adviserSessionToken.trim();				
				adviserSessionToken = adviserSessionToken.substring(0, adviserSessionToken.lastIndexOf('\''));	
				JViewer.setSessionCookies(adviserSessionToken);// set adviser session cookie.
			}

			else if(ret == URLProcessor.HTTP_REQUEST_FAILURE){
				JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_11_SACD"), 
						LocaleStrings.getString("S_9_SACD"), JOptionPane.ERROR_MESSAGE);
				windowListener.windowClosing(null);
			}
		}catch(Exception e){
			Debug.out.println(e);
			JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_11_SACD"), 
					LocaleStrings.getString("S_9_SACD"), JOptionPane.ERROR_MESSAGE);
			windowListener.windowClosing(null);
		}

	}
	
	private int getAllConfigs(){	
		
		int kvmPrivilege = 0;
		String adviserSessionToken = null;
		int kvmServiceStatus;
		int kvmSecureChannel;
		int kvmPort;
		int webport;
		int singlePort;
		int value;
		int vMediaSSL;
		String uriString = "/rpc/getallconfigs.asp";
		int ret = -1;
		byte oemFetureStatus = 0;
		String keyboardLayout = JViewer.AUTO_DETECT_KEYBOARD;
		
		urlProcessor = new URLProcessor(null, 1);
		ret = urlProcessor.connectToWebPort(hostIP, uriString, secWebPort, username, password);
			
		if(ret == HTTPS_CONNECTION_SUCCESS){
			
			kvmPrivilege = Integer.parseInt(urlProcessor.getValue("'EXTENDED_PRIV' : ", ','));
			JViewer.setKVMPrivilege(kvmPrivilege);
			if(!JViewer.isKVMEnabled()){
				validate = false;
				launching = false;
				enableDialog();
				ipText.setText(JViewer.getIp());
				unameText.setText(null);
				passwdText.setText(null);
				JOptionPane.showMessageDialog(getDialog(), LocaleStrings.getString("S_12_SACD")+username+
						LocaleStrings.getString("S_13_SACD"), LocaleStrings.getString("S_9_SACD"),
						JOptionPane.ERROR_MESSAGE);
				unameText.requestFocus();
				ret = -1;
			}
			
			adviserSessionToken = urlProcessor.getValue("'SESSION_TOKEN' : '",',');
			adviserSessionToken.trim();				
			adviserSessionToken = adviserSessionToken.substring(0, adviserSessionToken.lastIndexOf('\''));	
			JViewer.setSessionCookies(adviserSessionToken);// set adviser session cookie
									
			//Checking KVM Service status, whether KVM service is enabled or not.
			kvmServiceStatus = Integer.parseInt(urlProcessor.getValue("'V_STR_KVM_STATUS' : ", ',')); 
						
			if(kvmServiceStatus == 0){
				JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_14_SACD"),
						LocaleStrings.getString("S_9_SACD"), JOptionPane.ERROR_MESSAGE);
				windowListener.windowClosing(null);
			}
			
			kvmSecureChannel = Integer.parseInt(urlProcessor.getValue("'V_STR_SECURE_CHANNEL' : ", ',')); 
			JViewer.setSecureChannel(kvmSecureChannel);// set kvm secure channel value.
						
			kvmPort = Integer.parseInt(urlProcessor.getValue("'V_STR_KVM_PORT' : ", ',')); 
			webport =  Integer.parseInt(urlProcessor.getValue("'V_STR_WEB_PORT' : ", ','));
						
			//JViewer.setSinglePortEnabled(true);
			
			singlePort = Integer.parseInt(urlProcessor.getValue("'V_SINGLE_PORT_ENABLED' : ", '}')); 
			if(singlePort == 1)
				JViewer.setSinglePortEnabled(true);
			else
				JViewer.setSinglePortEnabled(false);
			
			if (JViewer.isSinglePortEnabled() == true)
			{
				// set WEB port number as KVM Port .As SinglePort feature is Enabled.
				JViewer.setKVMPort(webport);
				// set SSL as true.As SinglePort feature is Enabled.
				JViewer.setUseSSL(true);
			}
			else
			{
				JViewer.setKVMPort(kvmPort);// set KVM port number.

				// Set KVM SSl status.
				if(kvmSecureChannel == 0)
					JViewer.setUseSSL(false);
				else if(kvmSecureChannel == 1)
					JViewer.setUseSSL(true);
			}

			//Set OEM Feature status value
			try{
				oemFetureStatus = Byte.parseByte(urlProcessor.getValue("'V_STR_OEM_FEATURE_STATUS' : ", ','));
				JViewer.setOEMFeatureStatus(oemFetureStatus);
			}catch (NumberFormatException nfe) {
				Debug.out.println(nfe);
				JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_36_SACD"),
						LocaleStrings.getString("S_9_SACD"),JOptionPane.ERROR_MESSAGE);
				oemFetureStatus = 0;
			}catch (Exception e) {
				Debug.out.println(e);
				JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_36_SACD"),
						LocaleStrings.getString("S_9_SACD"),JOptionPane.ERROR_MESSAGE);
				oemFetureStatus = 0;
			}

			// Set Host Physical Keyboard layout option
			try{
				keyboardLayout = urlProcessor.getValue("'V_STR_KEYBOARD_LAYOUT' : ", ',');
				if(keyboardLayout.startsWith("'") && keyboardLayout.endsWith("'")){
					try{
						int start = keyboardLayout.indexOf('\'') + 1;
						int end = keyboardLayout.lastIndexOf('\'');
						keyboardLayout = keyboardLayout.substring(start, end);
					}catch (IndexOutOfBoundsException iobe) {
						Debug.out.println(iobe);
						keyboardLayout = JViewer.AUTO_DETECT_KEYBOARD;
					}
					catch (Exception e) {
						Debug.out.println(e);
						keyboardLayout = JViewer.AUTO_DETECT_KEYBOARD;
					}
				}
			}catch (Exception e) {
				Debug.out.println(e);
				keyboardLayout = JViewer.AUTO_DETECT_KEYBOARD;
			}
			JViewer.setKeyboardLayout(keyboardLayout);

			//Media configurations
			try{
			if(!JViewer.isSinglePortEnabled())
			{
				value = Integer.parseInt(urlProcessor.getValue("'V_STR_SECURE_CHANNEL' : ", ','));
				vMediaSSL = value;
				JViewer.setVMSecureChannel(vMediaSSL);//set VMedia secure channel
				
				if(vMediaSSL == 0){
					JViewer.setVMUseSSL(false);
					value = Integer.parseInt(urlProcessor.getValue("'V_STR_CD_PORT' : ", ','));
					JViewer.setCdserver_port(value);//set VMedia Non-SSL cdserver port
					value = Integer.parseInt(urlProcessor.getValue("'V_STR_FD_PORT' : ", ','));
					JViewer.setFdserver_port(value);//set FDServer port
					value = Integer.parseInt(urlProcessor.getValue("'V_STR_HD_PORT' : ", ','));
					JViewer.setHdserver_port(value);//set HDServer port
				}
				else if(vMediaSSL == 1){
					JViewer.setVMUseSSL(true);
					value = Integer.parseInt(urlProcessor.getValue("'V_STR_CD_SECURE_PORT' : ", ','));
					JViewer.setCdserver_port(value);//set VMedia SSL cdserver port
					value = Integer.parseInt(urlProcessor.getValue("'V_STR_FD_SECURE_PORT' : ", ','));
					JViewer.setFdserver_port(value);//set VMedia SSL FDServer port
					value = Integer.parseInt(urlProcessor.getValue("'V_STR_HD_SECURE_PORT' : ", ','));
					JViewer.setHdserver_port(value);//set VMedia SSL HDServer port
				}
				
			}
			value = Integer.parseInt(urlProcessor.getValue("'V_NUM_FD' : ", ','));	
			JViewer.setNum_FD((byte) (value));// set number of FD

			value = Integer.parseInt(urlProcessor.getValue("'V_NUM_CD' : ", ','));	
			JViewer.setNum_CD((byte) (value));// set number of CD

			value = Integer.parseInt(urlProcessor.getValue("'V_NUM_HD' : ", ','));
			JViewer.setNum_HD((byte) (value));// set number of HD

			value = Integer.parseInt(urlProcessor.getValue("'V_CD_STATUS' : ", ',')); 
			JViewer.setCD_State(value);// set cd-media service status.
			value = Integer.parseInt(urlProcessor.getValue("'V_FD_STATUS' : ", ','));
			JViewer.setFD_State(value);// set fd-media service status.
			value = Integer.parseInt(urlProcessor.getValue("'V_HD_STATUS' : ", ','));
			JViewer.setHD_State(value);// set hd-media service status.
			
			}catch(NumberFormatException nfe){
				Debug.out.println(nfe);
			JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_16_SACD"),
					LocaleStrings.getString("S_9_SACD"), JOptionPane.ERROR_MESSAGE);
			windowListener.windowClosing(null);
		}
		catch (NullPointerException npe) {
			Debug.out.println(npe);
			JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_16_SACD"),
					LocaleStrings.getString("S_9_SACD"), JOptionPane.ERROR_MESSAGE);
			windowListener.windowClosing(null);
		}
			
		
		}
		else{
			return ret;
		}
		
		return ret;
	}

	/**
	 * Gets the adviser configuration values using https request and
	 * sets this value for JViewer parameter.
	 */
	private void getAdviserConfig(){
		int kvmServiceStatus;
		int kvmSecureChannel;
		int kvmPort;
		int webport;
		byte oemFetureStatus = 0;
		byte kvmLicenseStatus = 0;
		String keyboardLayout = JViewer.AUTO_DETECT_KEYBOARD;
		urlProcessor = new URLProcessor(webSessionToken, SECURE_CONNECT);
		try{
			int ret = urlProcessor.connect_url_lock("https://"+this.hostIP+":"+secWebPort+"/rpc/getadvisercfg.asp");
			if(ret == HTTPS_CONNECTION_SUCCESS){
				//Checking KVM Service status, whether KVM service is enabled or not.
				kvmServiceStatus = Integer.parseInt(urlProcessor.getValue("'V_STR_KVM_STATUS' : ", ',')); 
				if(kvmServiceStatus == 0){
					JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_14_SACD"),
							LocaleStrings.getString("S_9_SACD"), JOptionPane.ERROR_MESSAGE);
					windowListener.windowClosing(null);
				}
				kvmSecureChannel = Integer.parseInt(urlProcessor.getValue("'V_STR_SECURE_CHANNEL' : ", ','));
				JViewer.setSecureChannel(kvmSecureChannel);// set kvm secure channel value.

				kvmPort = Integer.parseInt(urlProcessor.getValue("'V_STR_KVM_PORT' : ", ','));
				webport = Integer.parseInt(urlProcessor.getValue("'V_STR_WEB_PORT' : ", ','));

				if (JViewer.isSinglePortEnabled() == true)
				{
					// set WEB port number as KVM Port .As SinglePort feature is Enabled.
					JViewer.setKVMPort(webport);
					// set SSL as true.As SinglePort feature is Enabled.
					JViewer.setUseSSL(true);
				}
				else
				{
					JViewer.setKVMPort(kvmPort);// set KVM port number.

					// Set KVM SSl status.
					if(kvmSecureChannel == 0)
						JViewer.setUseSSL(false);
					else if(kvmSecureChannel == 1)
						JViewer.setUseSSL(true);
				}
				//Set OEM Feature status value
				try{
					oemFetureStatus = Byte.parseByte(urlProcessor.getValue("'V_STR_OEM_FEATURE_STATUS' : ", ' '));
					JViewer.setOEMFeatureStatus(oemFetureStatus);
				}catch (NumberFormatException nfe) {
					Debug.out.println(nfe);
					JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_36_SACD"),
							LocaleStrings.getString("S_9_SACD"),JOptionPane.ERROR_MESSAGE);
					oemFetureStatus = 0;
				}catch (Exception e) {
					Debug.out.println(e);
					JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_36_SACD"),
							LocaleStrings.getString("S_9_SACD"),JOptionPane.ERROR_MESSAGE);
					oemFetureStatus = 0;
				}
				
				//Set KVM License status value
				try{
					kvmLicenseStatus = Byte.parseByte(urlProcessor.getValue("'V_STR_KVM_LICENSE_STATUS' : ", ','));
					JViewer.setKVMLicenseStatus(kvmLicenseStatus);
				}catch (NumberFormatException nfe) {
					Debug.out.print(nfe);
					JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_37_SACD"),
							LocaleStrings.getString("S_9_SACD"),JOptionPane.ERROR_MESSAGE);
					JViewer.setKVMLicenseStatus((byte)0);
				}catch (Exception e) {
					Debug.out.print(e);
					JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_37_SACD"),
							LocaleStrings.getString("S_9_SACD"),JOptionPane.ERROR_MESSAGE);
					JViewer.setKVMLicenseStatus((byte)0);
				}

				// Set Host Physical Keyboard layout option
				try{
					keyboardLayout = urlProcessor.getValue("'V_STR_KEYBOARD_LAYOUT' : ", ',');
					if(keyboardLayout.startsWith("'") && keyboardLayout.endsWith("'")){
						try{
							int start = keyboardLayout.indexOf('\'') + 1;
							int end = keyboardLayout.lastIndexOf('\'');
							keyboardLayout = keyboardLayout.substring(start, end);
						}catch (IndexOutOfBoundsException iobe) {
							Debug.out.println(iobe);
							keyboardLayout = JViewer.AUTO_DETECT_KEYBOARD;
						}
						catch (Exception e) {
							Debug.out.println(e);
							keyboardLayout = JViewer.AUTO_DETECT_KEYBOARD;
						}
					}
				}catch (Exception e) {
					Debug.out.println(e);
					keyboardLayout = JViewer.AUTO_DETECT_KEYBOARD;
				}
				JViewer.setKeyboardLayout(keyboardLayout);

			}
			else if(ret == URLProcessor.HTTP_REQUEST_FAILURE){
				JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_15_SACD"),
						LocaleStrings.getString("S_9_SACD"),JOptionPane.ERROR_MESSAGE);
				windowListener.windowClosing(null);
			}
		}catch(Exception e){
			Debug.out.println(e);
			JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_15_SACD"),
					LocaleStrings.getString("S_9_SACD"), JOptionPane.ERROR_MESSAGE);
			windowListener.windowClosing(null);
		}
	}

	/**
	 * Parses the video file information from web 
	 * Displays the video file information in table
	 */
	private String[][] findVideoData(String fileInfo) {

		String fileParam = "'FILE_NAME' : '";
		String infoParam = "'FILE_INFO' : '";
		String search;
		String line;
		int startIndex = 0;
		int endIndex = 0, end = 0;
		int rows = 0;
		int columns = 0;
		int MAX_COLUMN = 2;

		int availRows = getOccurances(fileInfo, fileParam);
		if (availRows > 0) {
			videoFile = new String[availRows][MAX_COLUMN];

			while (rows < availRows) {

				columns = 0;
				line = null;
				search = null;
				search = fileParam;

				line = fileInfo.substring(endIndex);
				startIndex = line.indexOf(fileParam);
				line = line.substring(startIndex);

				end = endIndex = line.indexOf("}");

				if (startIndex > 0 && endIndex > 0) {
					line = line.substring(0, endIndex);

					while (columns < MAX_COLUMN) {

						if (search == null) {
							search = infoParam;
							line = line.substring(line.indexOf(search));
						}

						endIndex = line.indexOf(",", search.length());
						if (endIndex < 0) {

							endIndex = line.indexOf('\\');
						}

						videoFile[rows][columns] = line.substring(
								fileParam.length(), endIndex).trim();
						videoFile[rows][columns].trim();
						if (videoFile[rows][columns].indexOf('\'') > 0)
							videoFile[rows][columns] = videoFile[rows][columns]
									.substring(0, videoFile[rows][columns]
											.indexOf('\''));

						if (search.equalsIgnoreCase(infoParam)) {
							model.addRow(new Object[] { videoFile[rows][0],
									videoFile[rows][1] });
						}

						columns++;
						search = null;
					}
				}
				endIndex = startIndex + end + 1;
				rows++;
			}
		}
		//If Video files not available show message and Quit
		else{
			launching = false;
			JOptionPane.showMessageDialog (
					this,
					LocaleStrings.getString("S_28_SACD"),
					LocaleStrings.getString("A_6_GLOBAL"),
					JOptionPane.INFORMATION_MESSAGE);
			windowListener.windowClosing(null);
		}

		return videoFile;

	}

	/**
	 * Gets the total number of occurances in the given sting
	*/
	public int getOccurances(String data, String search) {

		int count = 0;
		int idx = 0;
		String responseData = new String(data);

		while ((idx = responseData.indexOf(search, idx)) != -1) {
			count++;
			idx += search.length();
		}
		return count;

	}

	/**
	 * Gets the recorded video files information using https request and sets
	 * this value for video Files information.
	 */
	private void getVideoInfo() {

		String fileInfo = null;

		constructTableDialog();

		urlProcessor = new URLProcessor(webSessionToken, SECURE_CONNECT);
		try {
			int ret = urlProcessor.connect_url_lock("https://" + this.hostIP
					+ ":" + secWebPort + "/rpc/getvideoinfo.asp");
			if (ret == HTTPS_CONNECTION_SUCCESS) {

				fileInfo = new String(urlProcessor.getData());
				findVideoData(fileInfo);
			} else if (ret == URLProcessor.HTTP_REQUEST_FAILURE) {
				launching = false;
				JOptionPane.showMessageDialog(this,
						LocaleStrings.getString("S_25_SACD"),
						LocaleStrings.getString("S_9_SACD"),
						JOptionPane.ERROR_MESSAGE);
				windowListener.windowClosing(null);
			}
		} catch (Exception e) {
			Debug.out.println(e);
			launching = false;
			JOptionPane.showMessageDialog(this,
					LocaleStrings.getString("S_25_SACD"),
					LocaleStrings.getString("S_9_SACD"),
					JOptionPane.ERROR_MESSAGE);
			windowListener.windowClosing(null);
		}
	}

	/**
	 * Gets the VMedia configuration values using https request and
	 * sets this value for JViewer parameter.
	 */
	private void getVMediaConfig(){
		String value;
		int vMediaSSL;

		urlProcessor = new URLProcessor(webSessionToken, SECURE_CONNECT);
		int ret = urlProcessor.connect_url_lock("https://"+this.hostIP+":"+secWebPort+"/rpc/getvmediacfg.asp");

		if(ret == HTTPS_CONNECTION_SUCCESS){
			try{
				value = urlProcessor.getValue("'V_SINGLE_PORT_ENABLED' : ", ',');				
				// set Single Port Feature status.
				if(1 == Integer.parseInt(value))
				{
					JViewer.setSinglePortEnabled(true);
				}
				else
				{
					JViewer.setSinglePortEnabled(false);
				}
				value = urlProcessor.getValue("'V_MEDIA_LICENSE_STATUS' : ", ',');
				JViewer.setMediaLicenseStatus((byte)Integer.parseInt(value));

				if(!JViewer.isSinglePortEnabled())
				{
					value = urlProcessor.getValue("'V_STR_SECURE_CHANNEL' : ", ',');
					vMediaSSL = Integer.parseInt(value);
					JViewer.setVMSecureChannel(vMediaSSL);//set VMedia securechannel

					if(vMediaSSL == 0){
						JViewer.setVMUseSSL(false);
						value = urlProcessor.getValue("'V_STR_CD_PORT' : ", ',');
						JViewer.setCdserver_port(Integer.parseInt(value));//set VMedia Non-SSL cdserver port
						value = urlProcessor.getValue("'V_STR_FD_PORT' : ", ',');
						JViewer.setFdserver_port(Integer.parseInt(value));//set FDServer port
						value = urlProcessor.getValue("'V_STR_HD_PORT' : ", ',');
						JViewer.setHdserver_port(Integer.parseInt(value));//set HDServer port
					}
					else if(vMediaSSL == 1){
						JViewer.setVMUseSSL(true);
						value = urlProcessor.getValue("'V_STR_CD_SECURE_PORT' : ", ',');
						JViewer.setCdserver_port(Integer.parseInt(value));//set VMedia SSL cdserver port
						value = urlProcessor.getValue("'V_STR_FD_SECURE_PORT' : ", ',');
						JViewer.setFdserver_port(Integer.parseInt(value));//set VMedia SSL FDServer port
						value = urlProcessor.getValue("'V_STR_HD_SECURE_PORT' : ", ',');
						JViewer.setHdserver_port(Integer.parseInt(value));//set VMedia SSL HDServer port
					}

				}
				value = urlProcessor.getValue("'V_NUM_FD' : ", ',');			
				JViewer.setNum_FD((byte) Integer.parseInt(value));// set number of FD

				value = urlProcessor.getValue("'V_NUM_CD' : ", ',');			
				JViewer.setNum_CD((byte) Integer.parseInt(value));// set number of CD

				value = urlProcessor.getValue("'V_NUM_HD' : ", ',');
				JViewer.setNum_HD((byte) Integer.parseInt(value));// set number of HD

				value = urlProcessor.getValue("'V_CD_STATUS' : ", ','); 
				JViewer.setCD_State(Integer.parseInt(value));// set cd-media service status.
				value = urlProcessor.getValue("'V_FD_STATUS' : ", ',');
				JViewer.setFD_State(Integer.parseInt(value));// set fd-media service status.
				value = urlProcessor.getValue("'V_HD_STATUS' : ", ',');
				JViewer.setHD_State(Integer.parseInt(value));// set hd-media service status.
			}catch(NumberFormatException nfe){
				Debug.out.println(nfe);
				JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_16_SACD"),
						LocaleStrings.getString("S_9_SACD"), JOptionPane.ERROR_MESSAGE);
				windowListener.windowClosing(null);
			}
			catch (NullPointerException npe) {
				Debug.out.println(npe);
				JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_16_SACD"),
						LocaleStrings.getString("S_9_SACD"), JOptionPane.ERROR_MESSAGE);
				windowListener.windowClosing(null);
			}
		}
		else if(ret == URLProcessor.HTTP_REQUEST_FAILURE){
			JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_16_SACD"),
					LocaleStrings.getString("S_9_SACD"), JOptionPane.ERROR_MESSAGE);
			windowListener.windowClosing(null);
		}
	}

	/** 
	 * Logout the web session once all the required configuration data are obtained.
	 */

	public void logoutWebSession() {

		if (!JViewer.isStandAloneApp()) {
			if (JViewerApp.getInstance().getVideorecordapp() != null)
				JViewerApp.getInstance().getVideorecordapp()
						.lockVideoFile(false);
		}
		if(webLogIn)
		{
			urlProcessor = new URLProcessor(webSessionToken, SECURE_CONNECT);
			int ret = urlProcessor.connect_url_lock("https://"+this.hostIP+":"+secWebPort+"/rpc/WEBSES/logout.asp");
			if(ret != HTTPS_CONNECTION_SUCCESS){
				JOptionPane.showMessageDialog(this, LocaleStrings.getString("S_17_SACD"),
						LocaleStrings.getString("S_18_SACD"), JOptionPane.INFORMATION_MESSAGE);
			}
			else
				webLogIn = false;
		}
	}

	/**
	 * Display the available video files in the table for user selection
         */
	private void manageVideo() {
		String[] file;
		int rowCounts = videoTable.getSelectedRowCount();
		int rows[] = videoTable.getSelectedRows();
		int row = 0;
		file = new String[rowCounts];
		if (rowCounts > 0) {

			try {
				while (row < rowCounts) {
					file[row] = videoTable.getValueAt(rows[row], 0).toString();
					row++;
				}
			} catch (Exception e) {
				Debug.out.println(e);
			}

			if (file.length > 0) {
				JViewer.setVideoFile(file);
				this.setVisible(false);
				this.dispose();
				JViewerApp.getInstance().constructUI();
				JViewer.recording();
			} else {
				JOptionPane.showMessageDialog(this,
						LocaleStrings.getString("S_34_SACD"),
						LocaleStrings.getString("S_26_SACD"),
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this,
					LocaleStrings.getString("S_34_SACD"),
					LocaleStrings.getString("S_26_SACD"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/**
	 * Action handler for the connect and cancel buttons.
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(standaloneBtn)) {

			//this condition is for player button
			if (standaloneBtn.getText().equals(
					LocaleStrings.getString("S_30_SACD"))) {
				//for Player App single selection only is allowed
				if (videoTable.getSelectedRowCount() == 1) {
					JViewer.setApptype(JViewer.APP_TYPE_PLAYER);
					manageVideo();
				} else {
					JOptionPane.showMessageDialog(this,
							LocaleStrings.getString("S_35_SACD"),
							LocaleStrings.getString("S_26_SACD"),
							JOptionPane.INFORMATION_MESSAGE);
				}
			} else{
				JViewer.setApptype(JViewer.APP_TYPE_STAND_ALONE);
				onConnectBtn();
			}

		} else if (e.getSource().equals(videoBtn)) {
			JViewer.setApptype(JViewer.APP_TYPE_DOWNLOAD_SAVE);

			 //this condition is for download button
			if (videoBtn.getText().equals(LocaleStrings.getString("S_31_SACD"))) {
				manageVideo();
			} else
				onConnectBtn();
		} else if (e.getSource().equals(cancelBtn)) {
			onCancelBtn();
		}
	}

	/**
	 * Invoked when the Connect button in the StandAloneConnection dialog is pressed.
	 * It will get the input values, and launch the JViewer, if all the inputs are valid. 
	 */
	private void onConnectBtn(){
		hostIP = ipText.getText();
		secWebPort = JViewer.getWebPortNumber(portText.getText());
		username = unameText.getText();
		password = " ";
		char[] passwd = passwdText.getPassword();			
		for(int i =0; i < passwd.length; i++){	
			password +=passwd[i];
			passwd[i] = 0;
		}
		password  = password.trim();
		showDialog();
	}

	/**
	 * Invoked when the data required to launch the JViewer have been obtained.
	 * This will dispose the connection dialog, get the required library files
	 * and initiates redirection. 
	 */
	private void onConnectionSuccess(){
		launching = false;
		validate = false;
		showProgress(false);
		if (JViewer.isStandAloneApp()) {
			this.setVisible(false);
			this.dispose();
			JViewerApp.getInstance().constructUI();
			JViewer.redirect();
		}
	}

	/**
	 * Invoked when the Cancel button in the StandAloneConnection dialog is pressed.
	 * It will dispose the dialog and end the application. 
	 */	
	private void onCancelBtn(){			
		this.dispose();
		windowListener.windowClosing(null);
	}
	
	private String getDialogTitle(){
		return this.getTitle();
	}
	private void setDialogTitle(String title){
		this.setTitle(title);
	}

	/**
	 * @return the windowListener
	 */
	public DialogWindowListener getWindowListener() {
		return windowListener;
	}

	class DialogKeyListener extends KeyAdapter{
		public void keyPressed(KeyEvent ke){		
			if(ke.getKeyCode() == KeyEvent.VK_ENTER){
				if(ke.getSource().equals(cancelBtn)){
					onCancelBtn();
				}
				else
					onConnectBtn();
			}

		}
	}


	class DialogWindowListener extends WindowAdapter{
		public void windowClosing(WindowEvent e) {
			if(webLogIn)
				logoutWebSession();
			JViewer.exit(0);
		}
	}

	class TextFieldFocusListener extends FocusAdapter{
		public void focusGained(FocusEvent fe){
			if(fe.getSource().toString().contains("JTextField") ||
					fe.getSource().toString().contains("JPasswordField")){
				JTextField textField = (JTextField) fe.getSource();
				textField.selectAll();
			}
		}
	}
	/**
	 * The following class validates the input credentials and initiates the https connection to the BMC  
	 */
	class Validator extends Thread{		
		@Override
		public void run() {	
			while(validate){
				String errorString = "";
				JTextField component = null;
				if(hostIP == null || hostIP.length() == 0){
					errorString += "\n* "+LocaleStrings.getString("S_2_SACD");
					component = ipText;
				}
				else{
					if(JViewer.getServerIP(hostIP) == null){
						errorString += "\n* "+LocaleStrings.getString("S_2_SACD");
						ipText.selectAll();
						component = ipText;
					}
					else{
						JViewer.setServerIP(JViewer.getServerIP(hostIP));
						hostIP = JViewer.getIp();
					}
				}
				if(secWebPort < 0){
					errorString += "\n* "+LocaleStrings.getString("S_3_SACD");
					portText.setText("");
					if(component == null)
						component = portText;
				}
				if(username == null || username.length() == 0){
					errorString += "\n* "+LocaleStrings.getString("S_4_SACD");
					if(component == null)
						component = unameText;
				}
				if(password == null || password.length() == 0){
					errorString += "\n* "+LocaleStrings.getString("S_5_SACD");
					if(component == null)
						component = passwdText;
				}
				if(errorString.length()!=0){
					validate = false;
					launching = false;
					enableDialog();

					//While launching the StandAloneApp, show the error message dialog of missing credentials
					//only if some of the arguments are specified and some are not, or if its not being validated
					//for the first time. This will prevent the pop up being displayed while launching the 
					//StandAloneApp by double clicking.
					if(JViewer.getArgLength() > 0 || !firstCheck){
						JOptionPane.showMessageDialog(getDialog(), LocaleStrings.getString("S_19_SACD")+
								errorString, LocaleStrings.getString("S_9_SACD"),
								JOptionPane.INFORMATION_MESSAGE);
					}
					firstCheck = false;
					component.requestFocus();
					component.selectAll();
					return;
				}
				else{
					launching = true;
					progThread = new ProgressThread();
					progThread.start();
					JViewer.setWebSecure(SECURE_CONNECT);
					if(getAllConfigs() < 0){
						//Force standalone application to use SSLv3 to use for https RPC calls
						System.setProperty("https.protocols", "SSLv3");

						if(getWebSessionToken() < 0){
							validate = false;
							launching = false;
							return;
						}
						else
							webLogIn = true;
						if(getKVMPrivilege() < 0){
							validate = false;
							launching = false;
							logoutWebSession();
							return;
						}
						if (JViewer.isStandAloneApp()) {
							getVMediaConfig();
							getAdviserConfig();

							if(JViewer.getKVMLicenseStatus() != JViewer.LICENSED)
							{
								Debug.out.println("JVIEWER LICENSE STATUS : "+JViewer.getKVMLicenseStatus());
								JOptionPane.showMessageDialog(getDialog(), LocaleStrings.getString("1_6_JVIEWER"),
										LocaleStrings.getString("1_3_JVIEWER"),
										JOptionPane.ERROR_MESSAGE);
								windowListener.windowClosing(null);
							}
							getAdviserSessionToken();
						}
						//get video file information only for player/download App 
						else {
							launching = false;
							getVideoInfo();
						}
						if (!JViewer.isSinglePortEnabled()
								&& JViewer.isStandAloneApp())
							logoutWebSession();
						onConnectionSuccess();
					}
					else{
						onConnectionSuccess();
					}

				}
			}
		}
	}

	/**
	 * This thread modifies the StandAloneConnection dialog title to denote the connection progress.
	 */
	class ProgressThread extends Thread{
		int count = 0;
		public void run(){
			try {
				while(launching){
					count++;
					sleep(500);
					setDialogTitle(getDialogTitle()+".");
					if(count > 3){
						count = 0;
						setDialogTitle(LocaleStrings.getString("S_8_SACD"));
					}
				}
				if(JViewer.isStandAloneApp()){
					setDialogTitle(LocaleStrings.getString("S_1_SACD")+JViewer.getTitle());
				}
				else if(JViewer.isdownloadapp())
					setDialogTitle(LocaleStrings.getString("S_29_SACD"));
				showProgress(false);
			} catch (InterruptedException ie) {
				Debug.out.println(ie);
			}catch (Exception e) {
				Debug.out.println(e);
			}
		}
	}

	/**
	 * This method replaces all the occurrences of the given pattern using a replacement pattern.
	 * If the pattern appears continuously in the source string, the entire sequence will be replaced
	 * with a single occurrence of  the replacement pattern. The method will return the source string
	 * itself if the source string doesn't contain the pattern to be replaced.<br><br>
	 * <b>Example:</b><br>
	 * replaceAllPattern("a*b***c", "*", "-") returns a-b-c.	 
	 * @param source - Source string.
	 * @param pattern - Pattern to be replaced.
	 * @param replacement - Replacement pattern.
	 * @return Modified string.
	 */
	public static String replaceAllPattern( String source, String pattern, String replacement){
		int index = 0;
		int count = 1;
		String temp = "";
		for(int i = 0; i < count; i++){
			if((index = source.indexOf(pattern, index)) >= 0){
				index += pattern.length();
				if(source.indexOf(pattern, index) == index){
					count++;
				}
				temp+=pattern;
			}
		}
		try{
			if(temp.length() > 0){
				source = source.replaceFirst(temp, replacement);				
			}
			else
				return source;
		}catch (NullPointerException npe) {
			Debug.out.println("REPLACE ALL PATTERN : ");
			Debug.out.println(npe);
			return source;
		}		
		return replaceAllPattern(source, pattern, replacement);
	}

	/**
	 * Extracts the libraries from the corresponding jar files into the lib directory.
	 */
	public static boolean getWrapperLibrary(String libName){
		String libSource = null;
		if(System.getProperty("os.arch").contains("64")){//64 bit OS
			if(System.getProperty("os.name").startsWith("Windows"))// Windows
				libSource = "lib/win64/";
			else if(System.getProperty("os.name").startsWith("Linux"))// Linux
				libSource = "lib/linux64/";
			else if(System.getProperty("os.name").startsWith("Mac"))// Mac
				libSource = "lib/mac64/";
		}
		else if(System.getProperty("os.arch").contains("86")){// 32 bit OS
			if(System.getProperty("os.name").startsWith("Windows"))// Windows
				libSource = "lib/win32/";
			// Linux - 32 only
			else if(System.getProperty("os.name").startsWith("Linux")
					&& !System.getProperty("os.arch").contains("64"))
				libSource = "lib/linux32/";
			// Mac - 32 only
			else if(System.getProperty("os.name").startsWith("Mac")
					&& !System.getProperty("os.arch").contains("64"))
				libSource = "lib/mac32/";
		}
		
		if(libName != null){
			String currPath = null;
			// For Windows platform.
			if(System.getProperty("os.name").startsWith("Windows")){
				currPath = System.getProperty("user.dir");
			}
			//For Linux or Mac platforms.
			else if(System.getProperty("os.name").startsWith("Linux")|| 
					System.getProperty("os.name").startsWith("Mac")){
				currPath = JViewer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				//Remove an extra / that comes in the path
				currPath = currPath.substring(0, currPath.lastIndexOf('/'));
				//If there is any white space in a directory name, it will be represented 
				//as %20 in the currPath, in Linux and Mac file system. It should replaced with a '\'. 
				if(currPath.contains("%20")){
					currPath = currPath.replaceAll("%20", "\\ ");
				}
			}

			String destinationPath = "Jar"+File.separator+JViewer.getIp()+File.separator+"lib";

			//If there is any occurrence of ':' in a directory name, 
			//it should replaced with a '_'.
			if(System.getProperty("os.name").startsWith("Windows")){
				if(destinationPath.contains(":")){
					destinationPath = replaceAllPattern(destinationPath, ":", "_");
				}
			}
			destinationPath = currPath + File.separator +destinationPath;
			File destinationFile = new File(destinationPath);
			if(!destinationFile.exists())
				destinationFile.mkdirs();
			
			File libraryFile = new File(destinationFile+File.separator+libName);
			try {
				if (!libraryFile.exists()){
					InputStream inStream = com.ami.kvm.jviewer.JViewer.class.getResourceAsStream(libSource+libName); // get the input stream
					FileOutputStream fileOutStream = new FileOutputStream(libraryFile);
					// write contents of 'inStream' to 'fileOutStream'
					while (inStream.available() > 0) {
						fileOutStream.write(inStream.read());
					}
					fileOutStream.close();
					inStream.close();
				}
			} catch (FileNotFoundException e) {
				Debug.out.println(e);
				return false;
			} catch (IOException e) {
				Debug.out.println(e);
				return false;
			}
		}
		return true;
	}

	/**
	 * This method searches the com.ami.kvm.jviewer.lang package and gets
	 * all the class file names that starts with Resource_ to find out 
	 * all the resource files available for the various supported languages.
	 * The two letters available at the end of the resource file name will 
	 * give the locale code(Eg: EN for English). All the supported locales 
	 * will thus be identified and the supported locale codes will be returned
	 * as a String array.
	 *   
	 * @return - a String array of supported locale codes.
	 */
	public static String[] getSupportedLocales(){
		String[] supportedLocales = {"EN - English"};
		ArrayList<String> classNames = getClassesNames("com.ami.kvm.jviewer.lang");
		ArrayList<String> classNamesSOC = getClassesNames("com.ami.kvm.jviewer.soc.lang");
		if(!classNames.isEmpty()){
			Object[] classList = classNames.toArray();
			List localeList = new List();
			for(Object className : classList){
				
				try {
					//A language option will be added to the supported locale list only if, 
					//the resource bundles of the specific language are found in the common 
					//and SOC specific lang packages.
					String fileName = (String) className;
					String fileNameSOC = classNamesSOC.get(classNamesSOC.indexOf("com.ami.kvm.jviewer.soc.lang.SOC"
							+fileName.substring(fileName.lastIndexOf('.')+1, fileName.length())));
					if(fileName.startsWith("Resources_", (fileName.lastIndexOf('.')+1))){
						int beginIndex = fileName.lastIndexOf('_')+1;
						int endIndex = fileName.length();
						String localeCode = fileName.substring(beginIndex, endIndex);
						Locale locale = new Locale(localeCode.toLowerCase());
						String language = locale.getDisplayLanguage(new Locale(selectedLocale));
						localeList.add(localeCode+" - "+language);
					}
				} catch (ArrayIndexOutOfBoundsException aioobe) {
					//This exception will occur if the SOC specific resource bundle for a language is not found.
					//In that case, the language won't be added to the supported locale list and, 
					//the loop iteration is allowed to continue.
					Debug.out.println(aioobe);
					continue;
				}
			}
			supportedLocales = localeList.getItems();
		}
		return supportedLocales;
	}
	
	/**
	 * Gets the list of the names of all the class files available in a given package.
	 * 
	 * @param packageName - name of the package form which class names should be retrieved.
	 * @return - an array list of class names.
	 */
	public static ArrayList<String> getClassesNames(String packageName) {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			assert classLoader != null;
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			ArrayList<String> dirs = new ArrayList<String>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(resource.getFile());
			}
			TreeSet<String> classes = new TreeSet<String>();
			for (String directory : dirs) {
				classes.addAll(findClasses(directory, packageName));
			}
			ArrayList<String> classList = new ArrayList<String>();
			for (String clazz : classes) {
				classList.add(clazz);
			}
			return classList;
		}
		catch (IOException ie) {
			Debug.out.println(ie);
			return null;
		}
	}

	/**
	 * Recursive method used to find all classes in a given directory and sub directories.
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static TreeSet<String> findClasses(String directory, String packageName){
		TreeSet<String> classes = new TreeSet<String>();
		/*
		 * To find all the class names with in a given package, which is part of a jar file,
		 * the following code will be used. This code sequence will not work while debugging
		 * the code in Eclipse.
		 */
		if(Debug.MODE == Debug.RELEASE){
			//if (directory.startsWith("file:") && directory.contains("!")) {
			if (directory.contains("!")) {
				String [] split = directory.split("!");
				URL jar = null;
				try {
					jar = new URL(split[0]);
				} catch (MalformedURLException e) {
					Debug.out.println(e);
				}
				ZipInputStream zip = null;
				try {
					zip = new ZipInputStream(jar.openStream());
				} catch (IOException e) {
					Debug.out.println(e);
				}
				ZipEntry entry = null;
				try {
					while ((entry = zip.getNextEntry()) != null) {
						if (entry.getName().endsWith(".class")) {
							String className = entry.getName().replaceAll("[$].*", "").replaceAll("[.]class", "").replace('/', '.');
							classes.add(className);
						}
					}
				} catch (IOException e) {
					Debug.out.println(e);
				}
			}
		}
		/*
		 * While debugging in Eclipse the Debug.Mode must be set as DEBUG. This is because the above code won't
		 * work wile debugging in Eclipse. To find all the classes in a package while debugging from Eclipse,
		 * the following code should be used. 
		 */
		else if(Debug.MODE == Debug.DEBUG){
			if(System.getProperty("os.name").startsWith("Windows")){
				if(directory.startsWith("/"))
					directory = directory.substring(directory.indexOf('/')+1, directory.length());
			}
			//Replace %20 with space. 
			if(directory.contains("%20")){
				if(System.getProperty("os.name").startsWith("Windows"))
					directory = directory.replaceAll("%20", " ");
				else
					directory = directory.replaceAll("%20", "\\ ");
			}
		}

		File dir = new File(directory);
		if (!dir.exists()) {
			return classes;
		}
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file.getAbsolutePath(), packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
			}
		}
		return classes;
	}


	public void itemStateChanged(ItemEvent ie) {
		if(ie.getSource().equals(localeList) && ie.getStateChange() == ItemEvent.DESELECTED){
			selectedLocale = (String) localeList.getSelectedItem();
			int endIndex = selectedLocale.indexOf(" - ");
			selectedLocale = selectedLocale.substring(0, endIndex);
			JViewer.setLanguage(selectedLocale);
			//removing the itemListener from localeList to avoid the itemStateChange()
			// event being triggered when the item in the JComboBox list gets rearranged 
			//in the onLanguageChange() method.
			localeList.removeItemListener(this);
			onLanguageChange();
			//adding item listener back again.
			localeList.addItemListener(this);
		}
	}

	private void onLanguageChange(){
		this.setTitle(LocaleStrings.getString("S_1_SACD"));
		ipLabel.setText(LocaleStrings.getString("S_2_SACD")+" : ");
		portLabel.setText(LocaleStrings.getString("S_3_SACD")+" : ");
		unameLabel.setText(LocaleStrings.getString("S_4_SACD")+" : ");
		passwdLabel.setText(LocaleStrings.getString("S_5_SACD")+" : ");
		localeLabel.setText(LocaleStrings.getString("S_21_SACD")+" : ");
		// kbdLayoutLabel.setText(LocaleStrings.getString("S_21_SACD")+" : ");
		standaloneBtn.setText(LocaleStrings.getString("S_6_SACD"));
		videoBtn.setText(LocaleStrings.getString("S_27_SACD"));
		cancelBtn.setText(LocaleStrings.getString("S_7_SACD"));
		String[] newItems = getSupportedLocales();
		int selectedIndex = localeList.getSelectedIndex();
		localeList.removeAllItems();
		for(String newItem : newItems){
			localeList.addItem(newItem);
		}
		localeList.setSelectedIndex(selectedIndex);
	}
	
	public static String getSelectedLocale() {
		return selectedLocale;
	}
	public static void setSelectedLocale(String selectedLocale) {
		StandAloneConnectionDialog.selectedLocale = selectedLocale;
	}

	public int getRemotePathSupport() {
		return remotePathSupport;
	}

	public void setRemotePathSupport(int remotePathSupport) {
		this.remotePathSupport = remotePathSupport;
	}

	public boolean isWebLogIn() {
		return webLogIn;
	}

	public void setWebLogIn(boolean webLogIn) {
		this.webLogIn = webLogIn;
	}
}
