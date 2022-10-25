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
// JViewer KVM Sharing Dialog component module.
//

package com.ami.kvm.jviewer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.kvmpkts.KVMClient;

/**
 * KVMShareDialog component class.
 */
public class KVMShareDialog{

	public static final boolean FIRST_USER = true;
	public static final boolean SECOND_USER = false;
	public static final byte KVM_SHARING = 0;
	public static final byte KVM_SELECT_MASTER = 1;
	public static final byte KVM_FULL_PERMISSION_REQUEST = 2;

	public static boolean isMasterSession;
	private int kvmsDecisionDuration = 30;

	private final int NORTH_PANEL_STRUT = 150;
	private JButton kvmsSubmitButton = null;
	private JLabel kvmsRequestLabel = null;
	private JDialog kvmsResponseDialog = null;
	private JDialog kvmsRequestDialog = null;
	private Timer kvmsTimer = null;
	private JLabel kvmsTimerLabel = null;
	private JRadioButton fullAccessChoice = null;
	private JRadioButton partialAccessChoice = null;
	private JRadioButton denyAccessChoice = null;
	private JPanel kvmsNorthPanel = null;
	private JPanel kvmsCenterPanel = null;
	private JPanel kvmsSouthPanel = null;
	private JPanel kvmsResponsePanel = null;
	private JLabel kvmsResponseLabel = null;
	private JPanel kvmsRequestPanel = null;
	private ButtonGroup kvmsButtonGroup = null;
	private byte dialogType;	
	private String reqUserDetails = null;

	public KVMShareDialog(){
		kvmsRequestLabel = new JLabel();
		kvmsTimerLabel = new JLabel();
		fullAccessChoice = new JRadioButton(LocaleStrings.getString("H_1_KVMS")+"       ",false);
		partialAccessChoice = new JRadioButton(LocaleStrings.getString("H_2_KVMS"),false);
		denyAccessChoice = new JRadioButton(LocaleStrings.getString("H_3_KVMS")+"     ",false);
		kvmsResponsePanel = new JPanel();
		kvmsRequestPanel = new JPanel();		
	}
	/**
	 * Construct Dialog for first user
	 */
	private void constructResponseDialog(byte type)
	{
		dialogType = type;
		constructNorthPanel();
		constructCenterPanel();
		constructSouthPanel();
		kvmsResponsePanel.setLayout(new BorderLayout());
		kvmsResponsePanel.add(kvmsNorthPanel,BorderLayout.NORTH);
		kvmsResponsePanel.add(kvmsCenterPanel,BorderLayout.CENTER);
		kvmsResponsePanel.add(kvmsSouthPanel,BorderLayout.SOUTH);
		JFrame frame = JViewer.getMainFrame();
		kvmsResponseDialog = new JDialog(frame,LocaleStrings.getString("H_6_KVMS"),true);
		if(type != KVM_SELECT_MASTER)
			kvmsResponseDialog.setModal(false);
		kvmsResponseDialog.add(kvmsResponsePanel);
		kvmsResponseDialog.pack();
		kvmsResponseDialog.setResizable(false);
		kvmsResponseDialog.setLocation(300,300);
		kvmsResponseDialog.repaint();
		kvmsResponseDialog.addKeyListener(new KVMShareDialogKeyListener());
		kvmsResponseDialog.addWindowListener
		(
				new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						JViewerApp RCApp = JViewerApp.getInstance();
						Debug.out.println("Deny is Request");
						if(dialogType == KVM_FULL_PERMISSION_REQUEST)
							RCApp.OnSendKVMPrevilage(KVMSharing.KVM_REQ_PARTIAL, reqUserDetails);
						else if(dialogType == KVM_SHARING)
							RCApp.OnSendKVMPrevilage(KVMSharing.KVM_REQ_DENIED, reqUserDetails);
						getKVMShareResponseDialog().dispose();
						getTimer().stop();
						kvmsDecisionDuration = 30;
						JViewerApp.getInstance().getResponseDialogTable().remove(reqUserDetails);
						kvmsTimerLabel.setText("");
					}
				}
		);
		reqUserDetails = KVMSharing.KVM_CLIENT_USERNAME+" : "+
						KVMSharing.KVM_CLIENT_IP+" : "+KVMSharing.KVM_CLIENT_SESSION_INDEX;

		JViewerApp.getInstance().initResponseDialogTable();		
		JViewerApp.getInstance().getResponseDialogTable().put(reqUserDetails, kvmsResponseDialog);
	}

	/**
	 * Construct the North Panel of the KVM Share Dialog
	 */
	private void constructNorthPanel(){
		String panelLabel = "";
		if(kvmsNorthPanel == null)
			kvmsNorthPanel = new JPanel();
		else
			kvmsNorthPanel.removeAll();
		kvmsNorthPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		if(dialogType == KVM_SHARING || dialogType == KVM_FULL_PERMISSION_REQUEST){
			panelLabel = LocaleStrings.getString("H_4_KVMS")+" "+KVMSharing.KVM_CLIENT_USERNAME+
							" "+LocaleStrings.getString("H_5_KVMS")+" "+KVMSharing.KVM_CLIENT_IP;
		}
		else if(dialogType == KVM_SELECT_MASTER){
			panelLabel = LocaleStrings.getString("H_13_KVMS");
			kvmsNorthPanel.add(Box.createHorizontalStrut(NORTH_PANEL_STRUT));
		}
		if(kvmsResponseLabel == null)
			kvmsResponseLabel = new JLabel(panelLabel);
		else
			kvmsResponseLabel.setText(panelLabel);
		kvmsRequestLabel.setHorizontalTextPosition(JLabel.LEFT);
		kvmsNorthPanel.add(kvmsResponseLabel, FlowLayout.LEFT);
	}
	
	/**
	 * Construct the Center Panel of the Response Dialog 
	 */
	private void constructCenterPanel(){
		if(kvmsCenterPanel == null)
			kvmsCenterPanel = new JPanel();
		else
			kvmsCenterPanel.removeAll();
		if(kvmsButtonGroup == null)
			kvmsButtonGroup = new ButtonGroup();
		else{
			Enumeration<AbstractButton>	buttons = kvmsButtonGroup.getElements();
			while(buttons.hasMoreElements()){
				kvmsButtonGroup.remove(buttons.nextElement());
			}
		}
		if(dialogType == KVM_SHARING || dialogType == KVM_FULL_PERMISSION_REQUEST){
			if(fullAccessChoice == null){
				fullAccessChoice = new JRadioButton(LocaleStrings.getString("H_1_KVMS")+"       ",false);
				fullAccessChoice.setMnemonic(KeyEvent.VK_A);
			}
			if(partialAccessChoice == null){
				partialAccessChoice = new JRadioButton(LocaleStrings.getString("H_2_KVMS"),false);
				partialAccessChoice.setMnemonic(KeyEvent.VK_V);
			}

			if(dialogType == KVM_SHARING){
				if(denyAccessChoice == null){
					denyAccessChoice = new JRadioButton(LocaleStrings.getString("H_3_KVMS")+"     ",false);
					denyAccessChoice.setMnemonic(KeyEvent.VK_D);
				}

				kvmsCenterPanel.setLayout(new GridLayout(3,1));
				kvmsButtonGroup.add(fullAccessChoice);
				kvmsCenterPanel.add(fullAccessChoice);
				kvmsButtonGroup.add(partialAccessChoice);
				kvmsCenterPanel.add(partialAccessChoice);
				kvmsButtonGroup.add(denyAccessChoice);
				kvmsCenterPanel.add(denyAccessChoice);
			}
			else if(dialogType == KVM_FULL_PERMISSION_REQUEST){
				kvmsCenterPanel.setLayout(new GridLayout(2,1));
				kvmsButtonGroup.add(fullAccessChoice);
				kvmsCenterPanel.add(fullAccessChoice);
				kvmsButtonGroup.add(partialAccessChoice);
				kvmsCenterPanel.add(partialAccessChoice);
			}
			//Default choice is full permission
			fullAccessChoice.setSelected(true);
		}
		else if(dialogType == KVM_SELECT_MASTER){
			int userCount = KVMClient.getNumUsers();
			String []userData = KVMClient.getUserData();
			JRadioButton []otherSessions = new JRadioButton[userCount];
			kvmsCenterPanel.setLayout(new GridLayout((userCount - 1),1));

			for(int count = 0; count < userCount; count++){
				String display = (userData[count].substring(userData[count].indexOf(":")+1, userData[count].length())).trim();
				String index = (userData[count].substring(0,userData[count].indexOf(":")-1)).trim();

	    		if(Integer.parseInt(index) == JViewerApp.getInstance().getCurrentSessionId()){
					continue;
	    		}
				otherSessions[count] = new JRadioButton(display);
				otherSessions[count].setActionCommand(userData[count]);
				kvmsCenterPanel.add(otherSessions[count]);
				kvmsButtonGroup.add(otherSessions[count]);
			}
			kvmsCenterPanel.repaint();
		}
	}
	
	/**
	 * Construct the South Panel of the KVMSharing Response dialog.
	 */
	private void constructSouthPanel(){
		
		if(kvmsSouthPanel == null){
			kvmsSouthPanel = new JPanel();
			kvmsSouthPanel.setLayout(new FlowLayout());
			if(kvmsSubmitButton == null)
				initSubmitButton();
			if(kvmsTimerLabel == null)
				kvmsTimerLabel = new JLabel();
			if(dialogType == KVM_SELECT_MASTER)
				kvmsTimerLabel.setText("(10 "+LocaleStrings.getString("H_7_KVMS")+" )");
			else
				kvmsTimerLabel.setText("(30 "+LocaleStrings.getString("H_7_KVMS")+" )");
			kvmsSouthPanel.add(kvmsSubmitButton);
			kvmsSouthPanel.add(kvmsTimerLabel);
		}
	}
	
	/**
	 * Initialize the OK button on the KVMSaring Response Dialog
	 */
	private void initSubmitButton(){
		kvmsSubmitButton = new JButton(LocaleStrings.getString("A_3_GLOBAL"));
		KVMShareDialogButtonListener kvmsButtonListener = new KVMShareDialogButtonListener();
		kvmsSubmitButton.addActionListener(kvmsButtonListener);
	}
	/**
	 * Shows First user Request Dialog
	 */
	private void showResponseDialog()
	{
		kvmsResponseDialog.setVisible(true);
		kvmsResponseDialog.repaint();
		kvmsResponseDialog.requestFocus();
	}

	/**
	 * Construct Dialog for second user
	 */
	private void constructRequestDialog()
	{
		JFrame frame = JViewer.getMainFrame();
		kvmsRequestDialog = new JDialog(frame,"",false);
		kvmsRequestPanel.setLayout(new BorderLayout());
		kvmsRequestPanel.add(kvmsRequestLabel,BorderLayout.CENTER);
		kvmsRequestDialog.setSize(900,50);
		kvmsRequestDialog.add(kvmsRequestPanel);
		kvmsRequestDialog.setUndecorated(true);
		kvmsRequestDialog.setResizable(false);
		kvmsRequestDialog.setLocation(75,300);
		return;
	}

	/**
	 * Shows Second user Request Dialog
	 */
	private void showRequestDialog()
	{
		kvmsRequestLabel.setText("       "+LocaleStrings.getString("H_8_KVMS")+" "+KVMSharing.KVM_CLIENT_USERNAME+" "+LocaleStrings.getString("H_5_KVMS")+" "+KVMSharing.KVM_CLIENT_IP+"(30 "+LocaleStrings.getString("H_7_KVMS")+" )");
		kvmsRequestDialog.setVisible(true);
		kvmsRequestDialog.requestFocus();
	}

	/**
	 * Sets status bit to indicate client 1 or 2
	 * @param status true - First Client, false - Second Client
	 **/
	public void setUserStatus(boolean status)
	{
		isMasterSession = status;
	}

	/**
	 * Constructs KVMShareDialog componant
	 */
	public void constructDialog(byte type)
	{
		kvmsTimer = new Timer(1000,new KVMShareDialogListener());
		if(type == KVM_SELECT_MASTER)
			kvmsDecisionDuration = 10;
		else
			kvmsDecisionDuration = 30;
		if(isMasterSession)
		{
			this.constructResponseDialog(type);
		}
		else
		{
			this.constructRequestDialog();
		}
	}

	/**
	 * Shows KVMShareDialog
	 *
	 */
	public void showDialog()
	{
		kvmsTimer.start();
		if(isMasterSession)
		{
			this.showResponseDialog();
		}
		else
		{
			this.showRequestDialog();
		}
	}

	public void showInformationDialog(String message)
	{
		JVFrame frame = JViewerApp.getInstance().getMainWindow();
		JOptionPane.showMessageDialog(frame,message,LocaleStrings.getString("H_10_KVMS"),JOptionPane.INFORMATION_MESSAGE);
		return;
	}

	public JButton getOkButton()
	{
		return kvmsSubmitButton;
	}

	public JLabel getUserTwoTimerLabel()
	{
		return kvmsRequestLabel;
	}

	public JDialog getKVMShareResponseDialog()
	{
		return kvmsResponseDialog;
	}

	public JDialog getKVMShareRequestDialog()
	{
		return kvmsRequestDialog;
	}

	public Timer getTimer()
	{
		return kvmsTimer;
	}

	public JLabel getTimerLabel()
	{
		return kvmsTimerLabel;
	}

	public JRadioButton getRadioButtonOne()
	{
		return fullAccessChoice;
	}

	public JRadioButton getRadioButtonTwo()
	{
		return partialAccessChoice;
	}

	public JRadioButton getRadioButtonThree()
	{
		return denyAccessChoice;
	}

	/**
	 * @return the kvmsButtonGroup
	 */
	public ButtonGroup getKvmsButtonGroup() {
		return kvmsButtonGroup;
	}

	/**
	 * @return the Dialog type
	 */
	public byte getDialogType() {
		return dialogType;
	}

	/**
	 * Dispose the KVMSharing Response dialog
	 */
	public void disposeKVMShareResponseDialog()
	{
		if(kvmsTimer != null)
			kvmsTimer.stop();
		kvmsDecisionDuration = 30;
		kvmsResponseDialog.dispose();
		kvmsResponseDialog = null;
		JViewerApp.getInstance().getResponseDialogTable().remove(reqUserDetails);
	}

	public void disposeKVMShareReqestDialog()
	{
		if(kvmsTimer != null)
			kvmsTimer.stop();
		kvmsDecisionDuration = 30;
		if(kvmsRequestDialog != null){
			kvmsRequestDialog.dispose();
			kvmsRequestDialog = null;
		}
	}

	/**
	 * Just for testing purpose
	 * @param args
	 */
	/*public static void main(String[] args) {

		KVMShareDialog kd = KVMShareDialog.getInstance();

		// Here we are setting to be first or second user
		//  value          client
		//  =====          ======
		//  true           First Client
		//  false          Second Client
		kd.setUserStatus(false);

		kd.constructDialog();
		kd.showDialog();
	}*/

	/**
	 * DialogListener class to update timer value
	 */

	class KVMShareDialogListener implements ActionListener
	{
		/**
		 * Method invoked by the Timer class once its start method is called
		 */
		public void actionPerformed(ActionEvent e) {
			/*Decrement duration time*/
			kvmsDecisionDuration--;

			/*Once duration reaches 0,stop timer and dispatch dialog*/
			if(kvmsDecisionDuration <= 0)
			{
				getTimer().stop();
				kvmsDecisionDuration = 30;
				if(KVMShareDialog.isMasterSession)
				{
					Debug.out.println("User didn't gave any input");
					if(getKVMShareResponseDialog() != null)
						getKVMShareResponseDialog().dispose();
					JViewerApp.getInstance().getResponseDialogTable().remove(reqUserDetails);
				}
				else if(!KVMShareDialog.isMasterSession)
				{
					if(getKVMShareRequestDialog() != null)
						getKVMShareRequestDialog().dispose();
					Debug.out.println("Time out grant full access");
				}
			}

			/*Checking whether first or second client*/
			else if(KVMShareDialog.isMasterSession)
			{
				/*Update dialog*/
				getTimerLabel().setText("("+kvmsDecisionDuration+" "+LocaleStrings.getString("H_7_KVMS")+")");
				getTimerLabel().repaint();
			}
			else if(!KVMShareDialog.isMasterSession)
			{
				/*Update second user label status*/
				if(getKVMShareRequestDialog() != null){
					getUserTwoTimerLabel().setText("       "+LocaleStrings.getString("H_8_KVMS")+" "+KVMSharing.KVM_CLIENT_USERNAME+" "+LocaleStrings.getString("H_5_KVMS")+KVMSharing.KVM_CLIENT_IP+"  ("+kvmsDecisionDuration+" "+LocaleStrings.getString("H_7_KVMS")+")");
					getKVMShareRequestDialog().repaint();
				}
			}
		}
	}

	/**
	 * KeyListener used in the Dialog
	 */

	class KVMShareDialogKeyListener implements KeyListener
	{
		public void keyPressed(KeyEvent ke)
		{
			if(KVMShareDialog.isMasterSession)
			{
				if((ke.getModifiersEx() & KeyEvent.ALT_DOWN_MASK) == KeyEvent.ALT_DOWN_MASK)
				{
					if(ke.getKeyCode() == KeyEvent.VK_A)
					{

						getRadioButtonOne().setSelected(true);
						getRadioButtonOne().repaint();
					}
					else if(ke.getKeyCode() == KeyEvent.VK_V)
					{
						getRadioButtonTwo().setSelected(true);
						getRadioButtonTwo().repaint();
					}
					else if(ke.getKeyCode() == KeyEvent.VK_D)
					{
						getRadioButtonThree().setSelected(true);
						getRadioButtonThree().repaint();
					}
				}
				else if(ke.getKeyCode() == KeyEvent.VK_ENTER)
				{
					Debug.out.println ("***********ENTER KEY DETECTED********");
					getOkButton().doClick(); //Invoke OK Button
				}
				return;
			}
		}

		public void keyTyped(KeyEvent ke)
		{

		}

		public void keyReleased(KeyEvent ke)
		{

		}
	}

	/**
	 * ButtonListener for OK and Cancel Button in the Dialog
	 */
	class KVMShareDialogButtonListener implements ActionListener
	{
		JViewerApp RCApp = JViewerApp.getInstance();

		public void actionPerformed(ActionEvent ae)
		{
			if(ae.getSource().equals(getOkButton()))
			{
				if(getDialogType() == KVMShareDialog.KVM_SHARING ||
						getDialogType() == KVMShareDialog.KVM_FULL_PERMISSION_REQUEST){
					Debug.out.println("Submit Clicked");
					if(getRadioButtonOne().isSelected())
					{
						Debug.out.println("Allow KVM is Selected");
						RCApp.OnSendKVMPrevilage(KVMSharing.KVM_REQ_ALLOWED, reqUserDetails);
						//Close all the other KVM Privilege request dialogs, if any, since the Master privilege
						//has been given away.
						Set entrySet = JViewerApp.getInstance().getResponseDialogTable().entrySet();
						Iterator itr = entrySet.iterator();
						JDialog dialog = null;
						String keyString = null;
						int i = 1;
						while(itr.hasNext()){
							try{
								Map.Entry<String, JDialog> mapEntry = (Map.Entry<String, JDialog>) itr.next();
								dialog = mapEntry.getValue();
								keyString = mapEntry.getKey();
								if(dialog!= null)
									dialog.dispose();
							}catch (Exception ex) {
								Debug.out.println(ex);
							}
						}
						JViewerApp.getInstance().getResponseDialogTable().clear();
					}
					else if(getRadioButtonTwo().isSelected())
					{
						Debug.out.println("Allow only Video is Selected");
						RCApp.OnSendKVMPrevilage(KVMSharing.KVM_REQ_PARTIAL, reqUserDetails);
						getKVMShareResponseDialog().dispose();
						getTimer().stop();
						kvmsDecisionDuration = 30;
						JViewerApp.getInstance().getResponseDialogTable().remove(reqUserDetails);
					}
					else if(getRadioButtonThree().isSelected())
					{
						Debug.out.println("Deny is Selected");
						RCApp.OnSendKVMPrevilage(KVMSharing.KVM_REQ_DENIED, reqUserDetails);
						getKVMShareResponseDialog().dispose();
						getTimer().stop();
						kvmsDecisionDuration = 30;
						JViewerApp.getInstance().getResponseDialogTable().remove(reqUserDetails);
					}
					else
					{
						JOptionPane.showMessageDialog(getKVMShareResponseDialog(),LocaleStrings.getString("H_9_KVMS"),LocaleStrings.getString("A_5_GLOBAL"),JOptionPane.ERROR_MESSAGE);
					}
					kvmsTimerLabel.setText("");
				}
				else if(getDialogType() == KVMShareDialog.KVM_SELECT_MASTER){
					if(getKvmsButtonGroup().getSelection() != null){
						String selectedMaster = getKvmsButtonGroup().getSelection().getActionCommand();
						if(selectedMaster == null)
							return;

						RCApp.sendSelectedMasterInfo(selectedMaster);
					}
					getKVMShareResponseDialog().dispose();
					getTimer().stop();
					kvmsDecisionDuration = 30;
				}
				return;
			}
		}
	}
}


