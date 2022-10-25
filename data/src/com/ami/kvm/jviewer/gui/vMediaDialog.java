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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import com.ami.iusb.CDROMRedir;
import com.ami.iusb.FloppyRedir;
import com.ami.iusb.HarddiskRedir;
import com.ami.iusb.IUSBRedirSession;
import com.ami.iusb.RedirectionException;
import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;


public class vMediaDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	DevicePanel	cdromDevTable[];
	DevicePanel	floppyDevTable[];
	DevicePanel	HarddiskDevTable[];
	JTable		statusTable;
	JButton		floppyButton[];
	JButton		cdromButton[];
	JButton		harddiskButton[];
	JButton		helpButton;
	JButton		exitButton;
	private JPanel tablePanel;
	private JPanel dialogPanel;
	private Timer 			m_updateTimer;		// frame rate update timer

	private JLabel cdConnectLabel[];
	private JLabel fdConnectLabel[];
	private JLabel hdConnectLabel[];

	IUSBRedirSession	m_vMediaSession;
	String				m_hostIp;
	String				m_sessToken;
	int					m_cdPort;
	int					m_fdPort;
	int					m_hdPort;
	int 				m_numcd;
	int 				m_numfd;
	int 				m_numhd;
	int 				m_cdStatus;
	int 				m_fdStatus;
	int 				m_hdStatus;
	boolean				m_useSSL;

	int numFreeCD;
	int numFreeFD;
	int numFreeHD;

	public vMediaDialog(JFrame parent_frame, IUSBRedirSession vMediaSession,
							String hostIp, int cdPort, int fdPort, int hdPort, int cdnum, int fdnum, int hdnum,int cdStatus,int fdStatus,int hdStatus, boolean useSSL,
							String sessToken){
	super(parent_frame, LocaleStrings.getString("G_1_VMD"), false );

	m_vMediaSession = vMediaSession;
	m_hostIp = hostIp;
	m_cdPort = cdPort;
	m_fdPort = fdPort;
	m_hdPort = hdPort;
	m_numcd	 = cdnum;
	m_numfd  = fdnum;
	m_numhd  = hdnum;
	m_useSSL = useSSL;
	m_cdStatus = cdStatus;
	m_fdStatus = fdStatus;
	m_hdStatus = hdStatus;
	m_sessToken = sessToken;

	cdromDevTable = new DevicePanel[m_numcd];
	floppyDevTable = new DevicePanel[m_numfd];
	HarddiskDevTable = new DevicePanel[m_numhd];
	cdromButton = new JButton[m_numcd];
	floppyButton = new JButton[m_numfd];
	harddiskButton = new JButton[m_numhd];

	cdConnectLabel = new JLabel[m_numcd];
	fdConnectLabel = new JLabel[m_numfd];
	hdConnectLabel = new JLabel[m_numhd];

	ButtonListener	cmdListener = new ButtonListener();
	//Floppy Label
	JLabel[]	floppyLabel = new JLabel[m_numfd];
	//Harddisk Label
	JLabel[]	harddiskLabel = new JLabel[m_numhd];
	//CDROM Label
	JLabel[]	cdromLabel = new JLabel[m_numcd];
	//status Label
	JLabel  statusLabel = new JLabel(LocaleStrings.getString("G_2_VMD"));
	//Status table
	Object tableData[][] = {
								{LocaleStrings.getString("G_4_VMD"), LocaleStrings.getString("G_3_VMD"), "n/a"},
								{LocaleStrings.getString("G_5_VMD"), LocaleStrings.getString("G_3_VMD"), "n/a"},
								{LocaleStrings.getString("G_6_VMD"), LocaleStrings.getString("G_3_VMD"), "n/a"},

							};
	int totaldevice = m_numfd+m_numhd+m_numcd;
	String[][] NewtableData = new String[totaldevice][3];
	String	columnNames[] = {LocaleStrings.getString("G_7_VMD"), LocaleStrings.getString("G_8_VMD")
								,LocaleStrings.getString("G_9_VMD")};
	//Update the status table based on the no of FloppyDisk device
	for(int dev_fdno=0;dev_fdno<m_numfd;dev_fdno++) {		
		for(int table_row=0;table_row<3;table_row++){
			if(table_row == 0){
				String t_data = (String) tableData[0][0];
				NewtableData[dev_fdno][table_row] = t_data;
				NewtableData[dev_fdno][table_row] = NewtableData[dev_fdno][table_row].concat(" "+(dev_fdno+1));
			}
			else{
				NewtableData[dev_fdno][table_row] = (String) tableData[0][table_row];
			}
		}
	}

	//Update the status table based on the no of CD device
	for(int dev_cdno=0;dev_cdno<m_numcd;dev_cdno++) {
		for(int table_row=0;table_row<3;table_row++){
			if(table_row == 0){
				String t_data = (String) tableData[1][0];
				NewtableData[dev_cdno+m_numfd][table_row] = t_data;
				NewtableData[dev_cdno+m_numfd][table_row] = NewtableData[dev_cdno+m_numfd][table_row].concat(" "+(dev_cdno+1));
			}
			else{
				NewtableData[dev_cdno+m_numfd][table_row] = (String) tableData[1][table_row];
			}
		}
	}

	///Update the status table based on the no of Harddisk device
	for(int dev_hdno=0;dev_hdno<JViewerApp.getInstance().getM_hdNum();dev_hdno++)
	{
		for(int table_row=0;table_row<3;table_row++){
			if(table_row == 0){
			String t_data = (String) tableData[2][0];
			NewtableData[dev_hdno+m_numfd+m_numcd][table_row] = t_data;
			NewtableData[dev_hdno+m_numfd+m_numcd][table_row] = NewtableData[dev_hdno+m_numfd+m_numcd][table_row].concat(" "+(dev_hdno+1));
			}
			else{
				NewtableData[dev_hdno+m_numfd+m_numcd][table_row] = (String) tableData[2][table_row];
			}
		}

	}

	statusTable = new JTable(NewtableData,columnNames) {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int rowIndex, int vColIndex)
		{
			return false;
		}
	};

	statusTable.setShowGrid(false);
	statusTable.setCellSelectionEnabled(false);
	statusTable.setCellEditor(null);
	statusTable.setEnabled(false);
	JScrollPane statusScrollPane = new JScrollPane(statusTable);
	//Help Button
	helpButton = new JButton(LocaleStrings.getString("G_10_VMD"));
	exitButton = new JButton(LocaleStrings.getString("G_11_VMD"));
	exitButton.addActionListener(cmdListener);
	JScrollPane scrollCdromList[]= new JScrollPane[m_numcd];
	JScrollPane scrollFloppyList[] = new JScrollPane[m_numfd];
	JScrollPane scrollHarddiskList[] = new JScrollPane[m_numhd];
    int setboundy=10;
    tablePanel = new JPanel();
	tablePanel.setAutoscrolls(true);
	//tablePanel.setHorizontalScrollBar(JScrollPane.HORIZONTAL_SCROLLBAR,)
	tablePanel.setLayout(null);
    // Floppy/USB Key drive table
    String[] floppyList = getFloppyList(); //new String[]{"A", "B"};
    JPanel floppyPanel[] = new JPanel[m_numfd];

    for(int i=0;i<m_numfd;i++) {
		//Floppy button
	    floppyButton[i] = new JButton(LocaleStrings.getString("G_12_VMD")+(i+1));
	    floppyButton[i].addActionListener(cmdListener);
    	fdConnectLabel[i] = new JLabel();
	    floppyDevTable[i] = new DevicePanel(floppyList, DevicePanel.DEVICE_TYPE_FLOPPY, i);
	    floppyDevTable[i].m_browseButton.addActionListener(cmdListener);
	    scrollFloppyList[i] = new JScrollPane(floppyDevTable[i]);
	    //  floppy table panel
	    floppyPanel[i] = new JPanel();
		floppyPanel[i].setLayout(new BorderLayout(2,2));

		floppyLabel[i] =  new JLabel(LocaleStrings.getString("G_13_VMD")+getRomanNumber(i));
		floppyPanel[i].add(floppyLabel[i], BorderLayout.NORTH);
		floppyPanel[i].add(scrollFloppyList[i], BorderLayout.CENTER);
		floppyPanel[i].setBounds(10,setboundy,475,100);
		floppyButton[i].setBounds(500,setboundy+20,210,25);
    	fdConnectLabel[i].setBounds(500, setboundy+50, 300, 25);
		tablePanel.add(floppyPanel[i]);
		tablePanel.add(floppyButton[i]);
    	tablePanel.add(fdConnectLabel[i]);
		setboundy = setboundy + 120;
		updateFloppyRedirStatus(i);
		updateFloppyRedirStatus_another(i, false);
    }
    updateFreeFDStatus();

    //  CDROM drive table
    String[] cdromList = getCDROMList(); //new String[]{"D", "E"};
    JPanel cdromPanel[] = new JPanel[m_numcd];
    for(int i=0;i<m_numcd;i++) { 
	    //CDROM button
	    cdromButton[i] = new JButton(LocaleStrings.getString("G_14_VMD")+i);
	    cdromButton[i].addActionListener(cmdListener);
	    cdConnectLabel[i] = new JLabel();
	   // cdromButton[i].setBounds(500,140,210,25);
	    cdromDevTable[i] = new DevicePanel(cdromList, DevicePanel.DEVICE_TYPE_CDROM, i);
	    cdromDevTable[i].m_browseButton.addActionListener(cmdListener);
	    scrollCdromList[i] = new JScrollPane(cdromDevTable[i]);
	    //  cdrom table panel
	    cdromPanel[i] = new JPanel();
		cdromPanel[i].setLayout(new BorderLayout(2,2));
		cdromLabel[i] =  new JLabel(LocaleStrings.getString("G_15_VMD")+getRomanNumber(i));
		cdromPanel[i].add(cdromLabel[i], BorderLayout.NORTH);
		cdromPanel[i].add(scrollCdromList[i], BorderLayout.CENTER);
		cdromPanel[i].setBounds(10,setboundy,475,100);
		cdromButton[i].setBounds(500,setboundy+20,210,25);
		cdConnectLabel[i].setBounds(500, setboundy+50, 250, 25);
		tablePanel.add(cdromButton[i]);
		tablePanel.add(cdromPanel[i]);
		tablePanel.add(cdConnectLabel[i]);
		setboundy = setboundy + 120;
		updateCDROMRedirStatus(i);
    }
    updateFreeCDStatus();

    //  Harddisk/USB Key drive table
    String[] harddiskList_Removable = getUSBHDDList();
    String[] harddiskList_Fixed = getHarddiskFixedList();
    String[] harddiskList = null;
    int i=0;

    if(harddiskList_Removable != null || harddiskList_Fixed != null) {
    	int deviceLen = 0;
    	
    	if(harddiskList_Removable != null)
    		deviceLen =  deviceLen+harddiskList_Removable.length;
    	if(harddiskList_Fixed != null)
    		deviceLen =  deviceLen + harddiskList_Fixed.length;
    	
    	harddiskList = new String[deviceLen] ;

    	if(harddiskList_Removable != null) {
	    	for(int k=0;k<harddiskList_Removable.length;k++) {
	    		harddiskList[i] = harddiskList_Removable[k];
	    		harddiskList[i] = harddiskList[i].concat(" - USB");
	    		i++;
	    	}
    	}

    	if(harddiskList_Fixed != null) {
	    	for(int j=0;j<harddiskList_Fixed.length;j++,i++) {
	    		harddiskList[i] = harddiskList_Fixed[j];
	    		harddiskList[i]=harddiskList[i].concat(LocaleStrings.getString("G_16_VMD"));
	    	}
    	}
    }

    JPanel harddiskPanel[] = new JPanel[m_numhd];
    for(int k=0;k<hdnum;k++) {
    	//	Harddisk button
    	harddiskButton[k] = new JButton(LocaleStrings.getString("G_17_VMD")+k);
    	harddiskButton[k].addActionListener(cmdListener);
        HarddiskDevTable[k] = new DevicePanel(harddiskList, DevicePanel.DEVICE_TYPE_HD_USB, k);
        HarddiskDevTable[k].m_browseButton.addActionListener(cmdListener);
        scrollHarddiskList[k] = new JScrollPane(HarddiskDevTable[k]);

        hdConnectLabel[k] = new JLabel();

        // 	Harddisk/USB table panel
        harddiskPanel[k] = new JPanel();
        harddiskPanel[k].setLayout(new BorderLayout(2,2));
        harddiskLabel[k] =  new JLabel(LocaleStrings.getString("G_18_VMD")+getRomanNumber(k));
        harddiskPanel[k].add(harddiskLabel[k], BorderLayout.NORTH);
        harddiskPanel[k].add(scrollHarddiskList[k], BorderLayout.CENTER);
        harddiskPanel[k].setBounds(10,setboundy,475,100);
        harddiskButton[k].setBounds(500,setboundy+20,210,25);
        hdConnectLabel[k].setBounds(500, setboundy+50, 250, 25);
        tablePanel.add(harddiskPanel[k]);
        tablePanel.add(harddiskButton[k]);
        tablePanel.add(hdConnectLabel[k]);
        setboundy = setboundy + 120;
        updateharddiskRedirStatus(k);
    }
    updateFreeHDStatus();

	//status table Panel
	JPanel statusPanel = new JPanel();
	statusPanel.setLayout(new BorderLayout(2,2));
	statusPanel.add(statusLabel, BorderLayout.NORTH);
	statusPanel.add(statusScrollPane, BorderLayout.CENTER);
	setBackground(Color.WHITE);
	statusPanel.setBounds(10,setboundy,475,100);
	exitButton.setBounds(500,setboundy+75,130,25);
	tablePanel.setSize( 750, 520 );
	tablePanel.add(exitButton);
	tablePanel.add(statusPanel);
	dialogPanel = new JPanel();
	dialogPanel.setLayout(new BorderLayout());
	getContentPane().add(dialogPanel, BorderLayout.CENTER);
	this.setLocation(100,100);
	JScrollPane scrollPane = new JScrollPane (tablePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	dialogPanel.add(scrollPane);
	tablePanel.setPreferredSize(new Dimension(700,setboundy+125));
	scrollPane.setMinimumSize(new Dimension(700,550));
	setSize( 750, 550 );
	setLocationRelativeTo(null);
	m_updateTimer = new Timer();
	m_updateTimer.schedule(new UpdateReadBytesTask(), 0, 1000);
	// Add window listener.
	this.addWindowListener (
		new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				m_updateTimer.cancel();
			}
		}
	);
 }
  	/***
  	 * Display the Vmedia Dialog
  	 *
  	 */
 	public void DisplayDialog()
 	{
		setVisible(true);
	}

 	/**
 	 * Get the CDROM list from the native library
 	 * @return
 	 */
 	public String[] getCDROMList()
	{
		String cdromList[] = null;
		try {
			CDROMRedir cdromObj = new CDROMRedir(true);
			cdromList = cdromObj.getCDROMList();
			System.gc();
		} catch(RedirectionException e) {
			Debug.out.println("Exception occured while getCDROMList()");
			Debug.out.println(e);
		}
		return cdromList;
	}

 	/**
 	 * Get the Floppy list from the native library
 	 * @return
 	 */
	public String[] getFloppyList()
	{
		String floppyList[] = null;
		FloppyRedir floppyObj = new FloppyRedir(true);
		floppyList = floppyObj.getFloppyList();
		System.gc();
		return floppyList;
	}
	//getHarddiskFixedList

	/**
 	 * Get the Removable USB/HDD list from the native library
 	 * @return
 	 */
	public String[] getUSBHDDList()
	{
		String hardDiskList[] = null;
		HarddiskRedir harddiskObj = new HarddiskRedir(true);
		hardDiskList = harddiskObj.getUSBHDDList();
		System.gc();
		return hardDiskList;
	}

	/**
 	 * Get the Fixed HDD from the native library
 	 * @return
 	 */
	public String[] getHarddiskFixedList()
	{
		String hardDiskList[] = null;
		if((JViewer.getOEMFeatureStatus() & JViewerApp.OEM_REDIR_RD_WR_MODE) != JViewerApp.OEM_REDIR_RD_WR_MODE)
		{
			HarddiskRedir harddiskObj = new HarddiskRedir(true);
			hardDiskList = harddiskObj.getHarddiskFixedList();
			System.gc();
		}
		return hardDiskList;
	}


	/**
 	 * Get the Fixed HDD from the native library
 	 * @return
 	 */
	public String[] getEntireHarddiskList()
	{
		String hardDiskList[] = null;
		HarddiskRedir harddiskObj = new HarddiskRedir(true);
		hardDiskList = harddiskObj.getEntireHarddiskList();
		System.gc();
		return hardDiskList;
	}


	/**
	 *Disable All control in the VMedia Dialog
	 *
	 */
	public void disableAllControls()
	{		
		//cdrom
		for(int numcd=0;numcd<m_numcd;numcd++) {
			cdromDevTable[numcd].setEnabled(false);
			cdromButton[numcd].setEnabled(false);
		}
		//floppy
		for(int numfd=0;numfd<m_numfd;numfd++) {
			floppyDevTable[numfd].setEnabled(false);
			floppyButton[numfd].setEnabled(false);
		}
		//Hard disk
		for(int numhd=0;numhd<m_numhd;numhd++) {
			HarddiskDevTable[numhd].setEnabled(false);
			harddiskButton[numhd].setEnabled(false);
		}
		//exit button
		exitButton.setEnabled(false);
	}

	/***
	 * Enable All control in the VMedia Dialog
	 *
	 */
	public void enableAllControls()
	{
		//cdrom
		for(int numcd=0;numcd<m_numcd;numcd++) {
			if(m_vMediaSession.getCDROMDeviceStatus(numcd) == IUSBRedirSession.DEVICE_FREE){
				cdromDevTable[numcd].enableAll();
				cdromButton[numcd].setEnabled(true);
			}
		}
		//floppy
		for(int numfd=0;numfd<m_numfd;numfd++) {
			if(m_vMediaSession.getFloppyDeviceStatus(numfd) == IUSBRedirSession.DEVICE_FREE){
				floppyDevTable[numfd].enableAll();
				floppyButton[numfd].setEnabled(true);
			}
		}
		//harddisk
		for(int numhd=0;numhd<m_numhd;numhd++) {
			if(m_vMediaSession.getHardDiskDeviceStatus(numhd) == IUSBRedirSession.DEVICE_FREE){
				HarddiskDevTable[numhd].enableAll();
				harddiskButton[numhd].setEnabled(true);
			}
		}
		//exit button
		exitButton.setEnabled(true);
	}

	/**
	 * Enable/Disable the CD media device instances, when the number of free CD media instances get updated 
	 */
	public void updateFreeCDStatus(){
		numFreeCD = JViewerApp.getInstance().getFreeCDNum();
		int freeSlot=0;
		m_vMediaSession = JViewerApp.getInstance().getM_IUSBSession();
		for(int numcd=0;numcd<m_numcd;numcd++) {

			if( m_vMediaSession.getCDROMRedirStatus(numcd) != IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED ) {
				cdromDevTable[numcd].enableAll();
				updateCDROMRedirStatus_another(numcd, false);
				cdromButton[numcd].setEnabled(true);
				m_vMediaSession.setCDROMDeviceStatus(numcd, IUSBRedirSession.DEVICE_FREE);
				cdConnectLabel[numcd].setText("");
				if(freeSlot < numFreeCD)
				{
					freeSlot++;
				}
				else
				{
					cdromDevTable[numcd].disableAll();
					cdromButton[numcd].setEnabled(false);
					m_vMediaSession.setCDROMDeviceStatus(numcd, IUSBRedirSession.DEVICE_USED);
					cdConnectLabel[numcd].setText(LocaleStrings.getString("6_37_IUSBREDIR"));
				}
			}
		}
	}

	/**
	 * Enable/Disable the FD media device instances, when the number of free FD media instances get updated 
	 */
	public void updateFreeFDStatus(){
		numFreeFD = JViewerApp.getInstance().getFreeFDNum();
		int freeSlot=0;
		m_vMediaSession = JViewerApp.getInstance().getM_IUSBSession();
		for(int numfd=0;numfd<m_numfd;numfd++) {

			if( m_vMediaSession.getFloppyRedirStatus(numfd) != IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED ) {
				floppyDevTable[numfd].enableAll();
				updateFloppyRedirStatus_another(numfd, false);
				m_vMediaSession.setFloppyDeviceStatus(numfd, IUSBRedirSession.DEVICE_FREE);
				floppyButton[numfd].setEnabled(true);
				fdConnectLabel[numfd].setText("");
				if(freeSlot < numFreeFD)
				{
					freeSlot++;
				}
				else
				{
					floppyDevTable[numfd].disableAll();
					floppyButton[numfd].setEnabled(false);
					m_vMediaSession.setFloppyDeviceStatus(numfd, IUSBRedirSession.DEVICE_USED);
					fdConnectLabel[numfd].setText(LocaleStrings.getString("6_37_IUSBREDIR"));
				}
			}
		}
	}

	/**
	 * Enable/Disable the HD media device instances, when the number of free HD media instances get updated 
	 */
	public void updateFreeHDStatus(){
		numFreeHD = JViewerApp.getInstance().getFreeHDNum();
		int freeSlot=0;
		m_vMediaSession = JViewerApp.getInstance().getM_IUSBSession();
		for(int numhd=0;numhd<m_numhd;numhd++) {

			if( m_vMediaSession.getHarddiskRedirStatus(numhd) != IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED ) {
				HarddiskDevTable[numhd].enableAll();
				updateharddiskRedirStatus_another(numhd, false);
				m_vMediaSession.setHardDiskDeviceStatus(numhd, IUSBRedirSession.DEVICE_FREE);
				harddiskButton[numhd].setEnabled(true);
				hdConnectLabel[numhd].setText("");
				if(freeSlot < numFreeHD)
				{
					freeSlot++;
				}
				else
				{
					HarddiskDevTable[numhd].disableAll();
					harddiskButton[numhd].setEnabled(false);
					m_vMediaSession.setHardDiskDeviceStatus(numhd, IUSBRedirSession.DEVICE_USED);
					hdConnectLabel[numhd].setText(LocaleStrings.getString("6_37_IUSBREDIR"));
				}
			}
		}
	}
	/**
	 * Enabling and disabling the Device listed displayed in the VMedia GUI
	 *
	 */
	public void updateCDROMRedirStatus(int device_no)
	{
		// get the vMedia redirection status and update the UI accordingly.
		//int cddevice_no=0;
		// update cdrom redir table
		m_vMediaSession = JViewerApp.getInstance().getM_IUSBSession();
		if( m_vMediaSession.getCDROMRedirStatus(device_no) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED ) {
			// disable cdrom drive table
			cdromDevTable[device_no].disableAll();
			cdromButton[device_no].setText(LocaleStrings.getString("G_19_VMD"));
			cdConnectLabel[device_no].setText(LocaleStrings.getString("G_32_VMD")+
					m_vMediaSession.getCDInstanceNumber(device_no));
			cdromDevTable[device_no].SelectRadioButton(m_vMediaSession.getCDROMSource(device_no));
			statusTable.setValueAt(m_vMediaSession.getCDROMSource(device_no), (device_no+m_numfd), 1);
			//JViewerApp.getInstance().getM_wndFrame().toolbar.getCdBtn().setToolTipText(m_vMediaSession.getCDROMSource(device_no)+" Drive is Connected to the session");
			int count = JViewerApp.MAX_IMAGE_PATH_COUNT;
			int Ret=-1;

			for(int s=0;s<count;s++) {
					 String Item = (String) cdromDevTable[device_no].m_imagePath.getItemAt(s);
					 if(Item != null)
						Ret = Item.compareTo(m_vMediaSession.getCDROMSource(device_no));
					 if(Ret == 0)
						break;
			}

			if(Ret != 0) {
				if(!m_vMediaSession.getCdromSession(device_no).isPhysicalDevice()) {
					cdromDevTable[device_no].m_imagePath.insertItemAt(m_vMediaSession.getCDROMSource(device_no), 0);
					cdromDevTable[device_no].m_imagePath.setSelectedItem(m_vMediaSession.getCDROMSource(device_no));
				}
			}
		}
		else {
			// enable all the controls
			if(m_vMediaSession.getCDROMDeviceStatus(device_no) == IUSBRedirSession.DEVICE_FREE){
				cdromDevTable[device_no].enableAll();
				updateCDROMRedirStatus_another(device_no, false);
				cdromButton[device_no].setText(LocaleStrings.getString("G_14_VMD"));
				cdConnectLabel[device_no].setText("");
				statusTable.setValueAt(LocaleStrings.getString("G_3_VMD"), (device_no+m_numfd), 1);
				statusTable.setValueAt("n/a", (device_no+m_numfd), 2);
				//set the status of tool bar icon
				for(int i= 0; i<m_numcd;i++){
					if(m_vMediaSession.getCDROMRedirStatus(i) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED){
						m_vMediaSession.updateCDToolbarButtonStatus(true);
						break;
					}
					else{
						m_vMediaSession.updateCDToolbarButtonStatus(false);
					}
				}
			}
		}
	}

	/**
	 * Enabling and disabling the Device listed displayed in the VMedia GUI
	 *
	 */
	public void updateFloppyRedirStatus(int device_no)
	{
		// update floppy redir table
		m_vMediaSession = JViewerApp.getInstance().getM_IUSBSession();
		if( m_vMediaSession.getFloppyRedirStatus(device_no) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED ) {
			// disable all the controls
			floppyDevTable[device_no].disableAll();
			floppyButton[device_no].setText(LocaleStrings.getString("G_19_VMD"));
			fdConnectLabel[device_no].setText(LocaleStrings.getString("G_33_VMD")+
					m_vMediaSession.getFDInstanceNumber(device_no));
			floppyDevTable[device_no].SelectRadioButton(m_vMediaSession.getFloppySource(device_no));
			statusTable.setValueAt(m_vMediaSession.getFloppySource(device_no), device_no, 1);
			statusTable.repaint();
			//JViewerApp.getInstance().getM_wndFrame().toolbar.getFloppyBtn().setToolTipText(m_vMediaSession.getFloppySource(device_no)+" Drive is Connected to the session");
			int count = JViewerApp.MAX_IMAGE_PATH_COUNT;
			int Ret=-1;

			for(int s=0;s<count;s++) {
					 String Item = (String) floppyDevTable[device_no].m_imagePath.getItemAt(s);
					 if(Item != null)
						 Ret = Item.compareTo(m_vMediaSession.getFloppySource(device_no));
					 if(Ret == 0)
						 break;
			}

			if(Ret != 0) {
				if(!m_vMediaSession.getFloppySession(device_no).isPhysicalDevice()) {
					floppyDevTable[device_no].m_imagePath.insertItemAt(m_vMediaSession.getFloppySource(device_no), 0);
					floppyDevTable[device_no].m_imagePath.setSelectedItem(m_vMediaSession.getFloppySource(device_no));
				}
			}

		} else {
			// enable all the controls
			if(m_vMediaSession.getFloppyDeviceStatus(device_no) == IUSBRedirSession.DEVICE_FREE){
				floppyDevTable[device_no].enableAll();
				updateFloppyRedirStatus_another(device_no, false);
				floppyButton[device_no].setText(LocaleStrings.getString("G_12_VMD"));
				fdConnectLabel[device_no].setText("");
				//floppyDevTable.SelectRadioButton(""); // this selects the floppy image radio button with null string.
				statusTable.setValueAt(LocaleStrings.getString("G_3_VMD"),device_no, 1);
				statusTable.setValueAt("n/a", device_no, 2);
				//set the status of tool bar icon
				for(int i= 0; i<m_numfd;i++){
					if(m_vMediaSession.getFloppyRedirStatus(i) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED){
						m_vMediaSession.updateFDToolbarButtonStatus(true);
						break;
					}
					else{
						m_vMediaSession.updateFDToolbarButtonStatus(false);
					}
				}
			}
		}
	}
	public void updateFloppyRedirStatus_another(int device_no,boolean state)
	{
		String[] redirecteddrive = new String[m_numfd];
		for(int h=0;h<m_numfd;h++) {
			if( m_vMediaSession.getFloppyRedirStatus(h) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED ) {
				redirecteddrive[h] = m_vMediaSession.getFloppySource(h);
			}
		}
		for(int g=0;g<redirecteddrive.length;g++) {
			if(redirecteddrive[g] != null || !state) {
				floppyDevTable[device_no].SelectRadioButton_StateChange(redirecteddrive[g],state);
			}
		}
	}
	public void updateFloppyRedirStatus_Enable(String drive ,boolean state) {
	    for(int g=0;g<m_numfd;g++) {
	    	if( m_vMediaSession.getFloppyRedirStatus(g) == IUSBRedirSession.DEVICE_REDIR_STATUS_IDLE ) {
	    		floppyDevTable[g].SelectRadioButton_StateChange(drive,state);
	    	}
	    }
	}
	public void updateharddiskRedirStatus_another(int device_no,boolean state)
	{
		String[] redirecteddrive = new String[m_numhd];
		for(int h=0;h<m_numhd;h++) {
			if( m_vMediaSession.getHarddiskRedirStatus(h) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED ) {
				redirecteddrive[h] = m_vMediaSession.getharddiskSource(h);
			}
		}
		for(int g=0;g<redirecteddrive.length;g++) {
			if(redirecteddrive[g] != null || !state) {
				HarddiskDevTable[device_no].SelectRadioButton_StateChange(redirecteddrive[g],state);
			}
		}
	}
	public void updateharddiskRedirStatus_Enable(String drive ,boolean state){
	    for(int g=0;g<m_numhd;g++) {
	    	if( m_vMediaSession.getHarddiskRedirStatus(g) == IUSBRedirSession.DEVICE_REDIR_STATUS_IDLE ) {
	    		HarddiskDevTable[g].SelectRadioButton_StateChange(drive,state);
	    	}
	    }
	}
	public void updateCDROMRedirStatus_another(int device_no,boolean state)
	{
		String[] redirecteddrive = new String[m_numcd];
		for(int h=0;h<m_numcd;h++) {
			if( m_vMediaSession.getCDROMRedirStatus(h) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED) {
				redirecteddrive[h] = m_vMediaSession.getCDROMSource(h);
			}
		}
		for(int g=0;g<redirecteddrive.length;g++) {
			if(redirecteddrive[g] != null || !state) {
				cdromDevTable[device_no].SelectRadioButton_StateChange(redirecteddrive[g],state);
			}
		}
	}
	public void updateCDROMRedirStatus_Enable(String drive ,boolean state){
	    for(int g=0;g<m_numcd;g++) {
	    	if( m_vMediaSession.getCDROMRedirStatus(g) == IUSBRedirSession.DEVICE_REDIR_STATUS_IDLE &&
	    			m_vMediaSession.getCDROMDeviceStatus(g) == IUSBRedirSession.DEVICE_FREE) {
	    		cdromDevTable[g].SelectRadioButton_StateChange(drive,state);
	    	}
	    }
	}
	/**
	 * Enabling and disabling the Device listed displayed in the VMedia GUI
	 *
	 */
	public void updateharddiskRedirStatus(int device_no)
	{
		// update harddisk redir table
		m_vMediaSession = JViewerApp.getInstance().getM_IUSBSession();
		if( m_vMediaSession.getHarddiskRedirStatus(device_no) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED ) {
			// disable all the controls
			HarddiskDevTable[device_no].disableAll();
			harddiskButton[device_no].setText(LocaleStrings.getString("G_19_VMD"));
			hdConnectLabel[device_no].setText(LocaleStrings.getString("G_34_VMD")+
					m_vMediaSession.getHDInstanceNumber(device_no));
			HarddiskDevTable[device_no].SelectRadioButton(m_vMediaSession.getharddiskSource(device_no));
			statusTable.setValueAt(m_vMediaSession.getharddiskSource(device_no), (device_no+m_numfd+m_numcd), 1);
			//JViewerApp.getInstance().getM_wndFrame().toolbar.getHardddiskBtn().setToolTipText(m_vMediaSession.getharddiskSource(device_no)+" Drive is Connected to the session");			
			int count = JViewerApp.MAX_IMAGE_PATH_COUNT;
			int Ret=-1;

			for(int s=0;s<count;s++) {
					 String Item = (String) HarddiskDevTable[device_no].m_imagePath.getItemAt(s);
					 if(Item != null)
						 Ret = Item.compareTo(m_vMediaSession.getharddiskSource(device_no));
					 if(Ret == 0)
						 break;
			}
			
			if(Ret != 0) {
				if(!m_vMediaSession.getHarddiskSession(device_no).isPhysicalDevice()) {
					HarddiskDevTable[device_no].m_imagePath.insertItemAt(m_vMediaSession.getharddiskSource(device_no), 0);
					HarddiskDevTable[device_no].m_imagePath.setSelectedItem(m_vMediaSession.getharddiskSource(device_no));
				}
			}
		} else {
			// enable all the controls
			if(m_vMediaSession.getHardDiskDeviceStatus(device_no) == IUSBRedirSession.DEVICE_FREE){
				HarddiskDevTable[device_no].enableAll();
				updateharddiskRedirStatus_another(device_no, false);
				harddiskButton[device_no].setText(LocaleStrings.getString("G_17_VMD"));
				hdConnectLabel[device_no].setText("");
				//floppyDevTable.SelectRadioButton(""); // this selects the floppy image radio button with null string.
				statusTable.setValueAt(LocaleStrings.getString("G_3_VMD"), (device_no+m_numfd+m_numcd), 1);
				statusTable.setValueAt("n/a", (device_no+m_numfd+m_numcd), 2);
				//set the status of tool bar icon
				for(int i= 0; i<m_numhd;i++){
					if(m_vMediaSession.getHarddiskRedirStatus(i) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED){
						m_vMediaSession.updateHDToolbarButtonStatus(true);
						break;
					}
					else{
						m_vMediaSession.updateHDToolbarButtonStatus(false);
					}
				}
			}
		}
	}

	/**
	 * Update the data transfer raates in the Vmedia dialog
	 *
	 */
	public void updateReadBytes()
	{
		for(int l=0;l<m_numfd;l++) {
			if( m_vMediaSession.getFloppyRedirStatus(l) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED )
				statusTable.setValueAt(m_vMediaSession.getFloppyReadBytes(l)+" "+"KB", l, 2);
		}
		for(int l=0;l<m_numcd;l++) {
			if( m_vMediaSession.getCDROMRedirStatus(l) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED )
				statusTable.setValueAt(m_vMediaSession.getCDROMReadBytes(l)+" "+"KB", (l+m_numfd), 2);
		}
		for(int l=0;l<m_numhd;l++){
			if( m_vMediaSession.getHarddiskRedirStatus(l) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED )
				statusTable.setValueAt(m_vMediaSession.getHarddiskReadBytes(l)+" "+"KB", (l+m_numfd+m_numcd), 2);
		}
	}
	/**
	 * Gets the dialog panel.
	 *
	 * @return the dialog panel
	 */
	public JPanel getDialogPanel() {
		return dialogPanel;
	}
	
	/**
	 * Sets the dialog panel.
	 *
	 * @param dialogPanel the new dialog panel
	 */
	public void setDialogPanel(JPanel dialogPanel) {
		this.dialogPanel = dialogPanel;
	}
	public JPanel getTablePanel() {
		return tablePanel;
	}

	public void setTablePanel(JPanel tablePanel) {
		this.tablePanel = tablePanel;
	}

	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Object actionObj = e.getSource();
			if( actionObj == exitButton ) {
				m_updateTimer.cancel();
				if( JViewer.isStandalone() ) {
					setVisible(false);
				} else {
					JViewerApp.getInstance().mediaDlg.dispose();
				}
				return;
			}
			for(int k=0;k<m_numcd;k++){
				if( actionObj == cdromDevTable[k].m_browseButton ) {
					if(cdromDevTable[k].getImagepathSelect() == null)
						cdromDevTable[k].setImagepathSelect(JViewerApp.Imagepath_CD[k][0]);
					String imagePath = m_vMediaSession.cdImageSelector(cdromDevTable[k].getImagepathSelect());
					if( imagePath != null )
						cdromDevTable[k].setImagePath(imagePath,DevicePanel.DEVICE_TYPE_CDROM, k);
					return;
				}
			}
			for(int k=0;k<m_numfd;k++) {
				if( actionObj == floppyDevTable[k].m_browseButton )
				{
					if(floppyDevTable[k].getImagepathSelect() == null)
						floppyDevTable[k].setImagepathSelect(JViewerApp.Imagepath_Floppy[k][0]);
					String imagePath = m_vMediaSession.floppyImageSelector(floppyDevTable[k].getImagepathSelect());
		
					if( imagePath != null )
						floppyDevTable[k].setImagePath(imagePath,DevicePanel.DEVICE_TYPE_FLOPPY, k);
					return;
				}
			}
			for(int k=0;k<m_numhd;k++) {
				if( actionObj == HarddiskDevTable[k].m_browseButton ) {
					if(HarddiskDevTable[k].getImagepathSelect() == null)
						HarddiskDevTable[k].setImagepathSelect(JViewerApp.Imagepath_Harddsik[k][0]);
					String imagePath = m_vMediaSession.floppyImageSelector(HarddiskDevTable[k].getImagepathSelect());
		
					if( imagePath != null )
						HarddiskDevTable[k].setImagePath(imagePath,DevicePanel.DEVICE_TYPE_HD_USB, k);
					return;
				}
			}
			for(int k=0;k<m_numcd;k++) {
				 if( actionObj == cdromButton[k] ) {
					 if(JViewerApp.getInstance().getM_cdStatus() == 0)
					 {
						 Debug.out.println("cdrom service disabled");
						 JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("G_26_VMD"),
								 LocaleStrings.getString("G_27_VMD"));
						 return;
					 }
					if( m_vMediaSession.getCDROMRedirStatus(k) == IUSBRedirSession.DEVICE_REDIR_STATUS_IDLE ) {
						Debug.out.println("Starting cdrom redirection");
						// Disable all the controls to disable interrupting during operation
						disableAllControls();
						boolean bRet=false;
						if( cdromDevTable[k].isImageSelected() ) {

							Debug.out.println("Starting ISO redirection "+cdromDevTable[k].getImagePath());
							String[] pathList = cdromDevTable[k].getImagePathList();
							bRet = m_vMediaSession.StartISORedir(m_sessToken,m_cdPort,k,m_useSSL, cdromDevTable[k].getImagePath());
							if(bRet)
							{
								m_vMediaSession.getCdromSession(k).setCdImageRedirected(true);
								cdromDevTable[k].saveImagePath(pathList, DevicePanel.DEVICE_TYPE_CDROM, k);
							}
						} else {

							Debug.out.println("Starting cdrom redirection "+cdromDevTable[k].selectedDeviceString());
							bRet = m_vMediaSession.StartCDROMRedir(m_sessToken,m_cdPort,k,m_useSSL,  cdromDevTable[k].selectedDeviceString());
							if(bRet)
								m_vMediaSession.getCdromSession(k).setCdImageRedirected(false);
						}
						// enable all controls back.
						enableAllControls();		
						Debug.out.println("cdrom redirection should be running");
						// if success
						if( bRet ) {
							for(int j=0;j<m_numfd;j++)
								updateFloppyRedirStatus(j);
							for(int j=0;j<m_numcd;j++) {
								updateCDROMRedirStatus(j);
								updateCDROMRedirStatus_another(j, false);
							}
							for(int j=0;j<m_numhd;j++)
								updateharddiskRedirStatus(j);
							m_vMediaSession.updateCDToolbarButtonStatus(true);
						}		
						else{ //maintain status of other devices
							for(int j=0;j<m_numfd;j++)
								updateFloppyRedirStatus(j);
							for(int j=0;j<m_numcd;j++)
								updateCDROMRedirStatus(j);
							for(int j=0;j<m_numhd;j++)
								updateharddiskRedirStatus(j);
						}
						for(int j=0;j<m_numcd;j++)
							updateCDROMRedirStatus_another(j, false);
						Debug.out.println("after updating cdrom redirection status");
					} 
					else {
						m_vMediaSession.getCdromSession(k).setCdImageRedirected(false);
						String drive = cdromDevTable[k].selectedDeviceString();
						m_vMediaSession.StopCDROMRedir(k, IUSBRedirSession.STOP_NORMAL);
						updateCDROMRedirStatus(k);
						updateCDROMRedirStatus_Enable(drive, true);
					}
					return;
				 }
			}
			for(int k=0;k<m_numfd;k++){
				if( actionObj == floppyButton[k] ) {
					 if(JViewerApp.getInstance().getM_fdStatus() == 0)
					 {
						 Debug.out.println("Floppy service disabled");
						 JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("G_28_VMD"),
								 LocaleStrings.getString("G_29_VMD"));
						 return;
					 }
					if( m_vMediaSession.getFloppyRedirStatus(k) == IUSBRedirSession.DEVICE_REDIR_STATUS_IDLE ) {
						Debug.out.println("Starting floppy redirection");
						// Disable all the controls to disable inkterrupting during operation
						disableAllControls();	
						boolean bRet=false;
						if( floppyDevTable[k].isImageSelected() ) {

							Debug.out.println("Starting floppy image redirection "+floppyDevTable[k].getImagePath());
							String[] pathList = floppyDevTable[k].getImagePathList();
							bRet = m_vMediaSession.StartFloppyImageRedir(m_sessToken,m_fdPort,k,m_useSSL, floppyDevTable[k].getImagePath());
							if(bRet)
							{
								m_vMediaSession.floppySession[k].setFdImageRedirected(true);
								floppyDevTable[k].saveImagePath(pathList, DevicePanel.DEVICE_TYPE_FLOPPY, k);
							}
						}
						else {

							Debug.out.println("Starting floppy redirection "+floppyDevTable[k].selectedDeviceString());
							bRet = m_vMediaSession.StartFloppyRedir(m_sessToken,m_fdPort,k,m_useSSL, floppyDevTable[k].selectedDeviceString());
							if(bRet)
								m_vMediaSession.floppySession[k].setFdImageRedirected(false);
						}
						// enable all controls back.
						enableAllControls();
						Debug.out.println("floppy redirection should be running");
						// if success
						if( bRet ) {
							for(int j=0;j<m_numfd;j++){
								updateFloppyRedirStatus(j);
								updateFloppyRedirStatus_another(j,false);
							}
							for(int j=0;j<m_numcd;j++)
								updateCDROMRedirStatus(j);
							for(int j=0;j<m_numhd;j++)
								updateharddiskRedirStatus(j);
							m_vMediaSession.updateFDToolbarButtonStatus(true);
						}
						else{ //maintain status of other devices
							for(int j=0;j<m_numfd;j++)
								updateFloppyRedirStatus(j);
							for(int j=0;j<m_numcd;j++)
								updateCDROMRedirStatus(j);
							for(int j=0;j<m_numhd;j++)
								updateharddiskRedirStatus(j);
						}
						for(int j=0;j<m_numfd;j++)
							updateFloppyRedirStatus_another(j, false);
						Debug.out.println("after updating floppy redirection status");
					}
					else
					{
						m_vMediaSession.floppySession[k].setFdImageRedirected(false);
						String drive = floppyDevTable[k].selectedDeviceString();
						m_vMediaSession.StopFloppyRedir(k, IUSBRedirSession.STOP_NORMAL);
						updateFloppyRedirStatus(k);
						updateFloppyRedirStatus_Enable(drive,true);
					}
					return;
				}
			}
			for(int k=0;k<m_numhd;k++) {
				if( actionObj == harddiskButton[k] ) {
					if(JViewerApp.getInstance().getM_hdStatus() == 0)
					{
						Debug.out.println("Harddisk service disabled");
						JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("G_30_VMD"),
								LocaleStrings.getString("G_31_VMD"));
						return;
					}
					if( m_vMediaSession.getHarddiskRedirStatus(k) == IUSBRedirSession.DEVICE_REDIR_STATUS_IDLE ) {
						Debug.out.println("Starting floppy redirection");
						// Disable all the controls to disable interrupting during operation
						disableAllControls();

						boolean bRet=false;
						if( HarddiskDevTable[k].isImageSelected() ) {

							Debug.out.println("Starting floppy image redirection "+HarddiskDevTable[k].getImagePath());
							String[] pathList = HarddiskDevTable[k].getImagePathList();
							byte mediatype = (byte)0x80; //USB key emulation
							bRet = m_vMediaSession.StartharddiskImageRedir(m_sessToken,m_hdPort,k,m_useSSL, HarddiskDevTable[k].getImagePath(),mediatype);
							if(bRet)
							{
								m_vMediaSession.harddiskSession[k].setHdImageRedirected(true);
								HarddiskDevTable[k].saveImagePath(pathList, DevicePanel.DEVICE_TYPE_HD_USB, k);
							}

						} else {

							Debug.out.println("Starting floppy redirection "+HarddiskDevTable[k].selectedDeviceString());
							byte mediatype = OngetMediatype(HarddiskDevTable[k].selectedDeviceString());
							bRet = m_vMediaSession.StartHarddiskRedir(m_sessToken,m_hdPort,k,m_useSSL, HarddiskDevTable[k].selectedDeviceString(),mediatype);
							if(bRet)
								m_vMediaSession.harddiskSession[k].setHdImageRedirected(false);
						}
						// enable all controls back.
						enableAllControls();	
						Debug.out.println("floppy redirection should be running");

						// if success
						if( bRet ) {
							for(int j=0;j<m_numfd;j++)
								updateFloppyRedirStatus(j);
							for(int j=0;j<m_numcd;j++)
								updateCDROMRedirStatus(j);
							for(int j=0;j<m_numhd;j++) {
								updateharddiskRedirStatus(j);
							}
							m_vMediaSession.updateHDToolbarButtonStatus(true);
						}
						else{ //maintain status of other devices
							for(int j=0;j<m_numfd;j++)
								updateFloppyRedirStatus(j);
							for(int j=0;j<m_numcd;j++)
								updateCDROMRedirStatus(j);
							for(int j=0;j<m_numhd;j++)
								updateharddiskRedirStatus(j);
						}
						for(int j=0;j<m_numhd;j++)
							updateharddiskRedirStatus_another(j, false);
						Debug.out.println("after updating floppy redirection status");
					}
					else
					{
						m_vMediaSession.harddiskSession[k].setHdImageRedirected(false);
						String drive = HarddiskDevTable[k].selectedDeviceString();
						m_vMediaSession.StopHarddiskRedir(k, IUSBRedirSession.STOP_NORMAL);
						updateharddiskRedirStatus(k);
						updateharddiskRedirStatus_Enable(drive, true);
					}
					return;
				}
			}
		 }
	}


    /**
     * Frame rate task.
     */
    class UpdateReadBytesTask extends TimerTask {

		/**
		 * Task routine.
		 */
    	public void run() {
			updateReadBytes();
    	}
    } // UpdateReadBytesTask


	public byte OngetMediatype(String string) {
		byte Mediatype = 0;
		// TODO Auto-generated method stub
		if(string != null) {
			String[] List_drive = getEntireHarddiskList();
			if(System.getProperty("os.name").startsWith("Win"))
			{
				String Physicaldriveno = string.substring(13,14);
				int redirectedDrive = Integer.parseInt(Physicaldriveno);		
				for (int i = 0; i < List_drive.length; i++) {
					String Drive = List_drive[i];
					String Drive_no = Drive.substring(0,1);
					int driveno = Integer.parseInt(Drive_no);
					if(driveno == redirectedDrive) 	{
						String DriveType =  Drive.substring(1,2);
						int drivetype= Integer.parseInt(DriveType);
						if(drivetype == 3) {
							Mediatype = (byte)0x00;//FIXED HARDDISK
						} else if(drivetype == 2) {
							Mediatype = (byte)0x80;//USB KEY
						}
					}
				}
			} else if(System.getProperty("os.name").equals("Linux")) {
				for (int i = 0; i < List_drive.length; i++) {
					String Drive = List_drive[i];
					String Drive_name = Drive.substring(0,Drive.length()-1);
					String[] redirectedDrive = string.split("-");
					String drivename = redirectedDrive[0].trim();
					//int driveno = Integer.parseInt(Drive_no);
					Debug.out.println("string::"+string+"Drive_name::"+Drive_name);
					Debug.out.println("COMPARED::"+Drive_name.compareTo(drivename));
					if(Drive_name.compareTo(drivename) == 0) {
						String DriveType =  Drive.substring(Drive.length()-1,Drive.length());
						int drivetype= Integer.parseInt(DriveType);
						if(drivetype == 0) {
							Mediatype = (byte)0x00;//FIXED HARDDISK
						} else if(drivetype == 1) {
							Mediatype = (byte)0x80;//USB KEY
						}
					}
				}
			}
		}
		return Mediatype;
	}
	
	public void disposeVMediaDialog(){
		m_updateTimer.cancel();
		this.dispose();
	}

	/**
	 * Converts a given number to Roman numeral format. This uses the Unicode representation
	 * So it is possible to get Roman numeral equivalent up to integer 12.
	 * @param number - number to be converted to Roman numeral
	 * @return Roman numeral String
	 */
	private String getRomanNumber(int number){
		String romanNumber = null;
		int roman = 8544;
		if(number < 12){
			if (number > 0)
				roman += number;
			char ch = (char)roman;
			romanNumber = String.valueOf(ch);
		}
		else
			romanNumber = String.valueOf(number);
		return romanNumber;
	}

} //end vMediaDialog
