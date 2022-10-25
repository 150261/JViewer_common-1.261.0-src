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
// JViewer frame base class.
//

package com.ami.kvm.jviewer.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.ami.iusb.IUSBRedirSession;
import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;


@SuppressWarnings("serial")
public abstract class JVFrame extends JInternalFrame {

	protected JScrollPane m_viewSP;
	protected static String m_serverIP;
	private FSMenuBar m_fsMenuBr = new FSMenuBar();
	public JDialog videoStopError = null;
	private JLabel confirmationLabel = null;
	public JVFrame() {
		super(LocaleStrings.getString("C_1_JVF"), 
		          true, //resizable
		          true, //closable
		          true, //maximizable
		          true);//iconifiable
	}
	
	/**
	 * Get menu
	 *
	 * @return menu
	 */
	public abstract JVMenu getMenu();

    /**
     * Set status.
     *
     * @param status message.
     */
    public abstract void setStatus(String msg);

    /**
     * Reset status.
     */
    public abstract void resetStatus();

    /**
     * Exit application.
     */
	public abstract void exitApp();

	/**
	 * Set window label
	 *
	 * @param label label string
	 */
	public abstract void setWndLabel(String label);

	/**
	 * Refresh window label
	 */
	public abstract void refreshTitle();

	/**
	 * Set the server IP
	 */
	public static void setServerIP(byte[] servIP, int m_RedirectionState) {
		try {
			if(servIP == null && m_RedirectionState == JViewerApp.REDIR_PAUSING)
				m_serverIP = LocaleStrings.getString("C_2_JVF");
			else if(servIP == null && m_RedirectionState == JViewerApp.REDIR_STOPPING)
				m_serverIP = LocaleStrings.getString("C_3_JVF");
			else if(servIP == null  && JViewer.isplayerapp())
				m_serverIP = LocaleStrings.getString("C_4_JVF");
			else if(servIP == null  && JViewer.isdownloadapp())
				m_serverIP = LocaleStrings.getString("C_5_JVF");
			else
				m_serverIP = InetAddress.getByAddress(servIP).getHostAddress();
		} catch (UnknownHostException e) {
			m_serverIP = LocaleStrings.getString("C_6_JVF");
			Debug.out.println(e);
		}
	}

	/**
	 * Set the server IP
	 */
	public static String getServerIP() {
		if ( m_serverIP != null )
		{
			if ( !m_serverIP.equalsIgnoreCase(new String(LocaleStrings.getString("C_6_JVF"))) )
				return m_serverIP;
		}

		return null;
	}

	/**
	 * Attach JViewer view.
	 */
	public void attachView() {
        m_viewSP = new JScrollPane(JViewerApp.getInstance().getRCView());
        Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
          
        if( JViewerApp.getInstance().isM_wndMode()) {
        	m_viewSP.getHorizontalScrollBar().setUnitIncrement(10);
        	m_viewSP.getVerticalScrollBar().setUnitIncrement(10);
        	JViewerApp.getInstance().getMainWindow().getContentPane().add(m_viewSP);
        	getContentPane().setMaximumSize(sd);
        	
        	if( JViewer.isStandalone())
        		JViewer.getMainFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
        	this.setVisible(true);
        }
        if( !JViewerApp.getInstance().isM_wndMode()) {
        	if(JViewerApp.getInstance().getSocframeHdr().getresX() == sd.width )
        	   	m_viewSP.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        	else
        		m_viewSP.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        	if(JViewerApp.getInstance().getSocframeHdr().getresY() == sd.height )
        		m_viewSP.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );
        	else
        		m_viewSP.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );

        	JViewerApp.getInstance().getMainWindow().getContentPane().add(m_viewSP);
        	
         	if( JViewer.isStandalone())
        		JViewer.getMainFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
        	this.setVisible(true);
        }
	}

	public static void setUndecorated(boolean undecorated,JInternalFrame frame)
    {
            BasicInternalFrameUI ui=(BasicInternalFrameUI)frame.getUI();
            if (undecorated) {
            	frame.putClientProperty("titlePane", ui.getNorthPane());
            	frame.putClientProperty("border", frame.getBorder());
                ui.setNorthPane(null);
                frame.setBorder(null);
            } 
    }
	
	/**
	 * Detach JViewer view.
	 */
	public void detachView() {
		//Cancelling Slider Timer Thread
		m_fsMenuBr.cancelTimer();
		/* Inserted delay to prevent the fullscreen
		 * frame from throwing a exception because of
		 * the use of timer thread on the slider
		 */
		try
   		{
   			Thread.sleep(1);
   		}
   		catch(InterruptedException ie)
   		{
   			Debug.out.println(ie);
   		}
		getContentPane().remove(m_viewSP);
		this.dispose();
	}

	/**
	 * Shutdown procedure when run as an application.
	 */
	public boolean windowClosed() {

		if(JViewer.isdownloadapp() || JViewer.isplayerapp())
		{
			boolean unlockfile = true;
			/*This condition will be satisfied only when launch from Stanalonde App*/
			if(JViewerApp.getInstance().getConnectionDialog() != null)
			{
				/*isWebLogIn() will return session login status*/
				unlockfile = JViewerApp.getInstance().getConnectionDialog().isWebLogIn();
			}
			if(unlockfile)
				JViewerApp.getInstance().getVideorecordapp().lockVideoFile(false);
			JViewerApp.getInstance().getVideorecordapp().Ondeletetmpfile();
		}

		if(JViewerApp.getInstance().GetRedirectionState() != JViewerApp.REDIR_STOPPED){
			//If any KVM Sharing Dialog is open, JViewer should not close without giving permission
			//to the requesting sessions.
			if(JViewer.isjviewerapp() || JViewer.isStandAloneApp()){
				if(KVMShareDialog.isMasterSession &&
						JViewerApp.getInstance().getKVMShareDialog() != null ){
					if(JViewerApp.getInstance().getResponseDialogTable() !=null &&
							!JViewerApp.getInstance().getResponseDialogTable().isEmpty()){
						if(JViewerApp.getInstance().getKVMShareDialog().getDialogType() !=
							KVMShareDialog.KVM_SELECT_MASTER){
							JOptionPane.showMessageDialog(JViewerApp.getInstance().getMainWindow(), 
									LocaleStrings.getString("C_14_JVF"), 
									LocaleStrings.getString("C_15_JVF"), JOptionPane.INFORMATION_MESSAGE);
							return false;
						}
					}
				}			
			}
		}

		if(!stopVMediaRedirection(LocaleStrings.getString("C_7_JVF")))
			return false;

		if(JViewerApp.getInstance().getM_videorecord() != null){
			if(VideoRecord.Recording_Started ) 
				JViewerApp.getInstance().getM_videorecord().OnVideoRecordStop();
			if(VideoRecord.Record_Processing){
				if(JViewer.isdownloadapp())
					JViewerApp .getInstance().getVideorecordapp().disposeInformationDialog();
				showVideoStopError();
				while(VideoRecord.Record_Processing)
					videoStopError.setVisible(true);

			}
		}

		if((JViewer.isjviewerapp() || JViewer.isStandAloneApp()) && 
				JViewerApp.getInstance().GetRedirectionState() != JViewerApp.REDIR_STOPPED){
			JViewerApp.getInstance().OnSelectKVMMaster();
		}

		try {
			JViewerApp.getInstance().OnVideoStopRedirection();
		} catch (Exception e) {
			Debug.out.println(e);
		}

		if(JViewer.isStandalone()){
			if((!JViewer.isWebPreviewer() && !JViewer.isBSODViewer()) &&
					!JViewerApp.getInstance().getKVMClient().isStopSignalRecieved()){
				JViewer.exit(0);
			}
		}
		else{
			if(Debug.MODE == Debug.CREATE_LOG)
				Debug.out.closeLog();
			JViewerApp.getInstance().getMainWindow().dispose();
		}

		return true;// I know I know. Control never reaches here. But if I dont put this here, compiler creeps
	}
    

    /** Display an error message with the specified title and text
     *  @param title The title of the error dialog
     *  @param message The main text of the error dialog */
    public void generalErrorMessage( String title, String message )
    {
        JOptionPane.showMessageDialog( this, message, title, JOptionPane.ERROR_MESSAGE );
    }

	public JScrollPane getM_viewSP() {
		return m_viewSP;
	}

	public void setM_viewSP(JScrollPane m_viewsp) {
		m_viewSP = m_viewsp;
	}
	private void showVideoStopError(){
		if(videoStopError == null){
			JFrame owner = JViewer.getMainFrame();
			videoStopError = new JDialog(owner,true);//Modal Dialog
			videoStopError.setUndecorated(true);	
			videoStopError.setSize(550,100);		
			videoStopError.setLocationRelativeTo(owner);
			videoStopError.setTitle(LocaleStrings.getString("C_11_JVF"));	
			videoStopError.add(getVideoStopError());			
			videoStopError.setVisible(true);
		}				
		
	}
	
	private JPanel getVideoStopError(){	
		JPanel msgPanel = new JPanel();
		msgPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 30));
		msgPanel.setSize(550, 100);
		JLabel errorMsg = new JLabel(LocaleStrings.getString("C_12_JVF")
				+ JViewer.getTitle() + LocaleStrings.getString("C_13_JVF"));
		msgPanel.add(errorMsg);
		return msgPanel;
	}

	/**
	 * @return the confirmationLabel
	 */
	public JLabel getConfirmationLabel() {
		return confirmationLabel;
	}

	public void paintComponent(Graphics g){
		if( JViewer.isStandalone())
			setUndecorated(true,this);
		super.paintComponent(g);
	}

/**
	Stop active VMedia redirections.
**/
	public boolean stopVMediaRedirection(String message){
		if(JViewerApp.getInstance().GetRedirectionState() != JViewerApp.REDIR_STOPPED){
			if(message != null){
				if( JViewerApp.getInstance().IsCDROMRedirRunning() ||
						JViewerApp.getInstance().IsFloppyRedirRunning() ||
						JViewerApp.getInstance().IsHarddiskRedirRunning() )
				{
					if(JViewer.getMediaLicenseStatus() != JViewer.LICENSED)
					{
						InfoDialog.showDialog(JViewer.getMainFrame(), message, LocaleStrings.getString("A_6_GLOBAL"), InfoDialog.INFORMATION_DIALOG);
					}
					else
					{	
						confirmationLabel = new JLabel(message);
						int ret = JOptionPane.showConfirmDialog(JViewer.getMainFrame(), confirmationLabel,
								LocaleStrings.getString("C_8_JVF"), JOptionPane.YES_NO_OPTION);

						if( ret == JOptionPane.CLOSED_OPTION )
						{
							//System.out.println("Returning CLOSED_OPTION\n");
							return false;
						}
						if( ret == JOptionPane.NO_OPTION )
						{
							Debug.out.println("Returning false\n");
							return false;
						}
					}
				}
			}
		}
		onStopVMediaRedirection();
		return true;
	}
	
	/**
	 * Perform VMedia Stop operation.
	 */
	public void onStopVMediaRedirection(){
		if(JViewerApp.getInstance().getM_mediaDlg() != null)
			JViewerApp.getInstance().getM_mediaDlg().disposeVMediaDialog();
		try {
			for(int h=0;h<JViewerApp.getInstance().getM_cdNum();h++){
				if(JViewerApp.getInstance().getUSBRedirSession().cdromSession[h] != null){
					JViewerApp.getInstance().getUSBRedirSession().StopCDROMRedir(h, IUSBRedirSession.STOP_NORMAL);
					JViewerApp.getInstance().getM_IUSBSession().updateCDToolbarButtonStatus(false);
				}
			}
			for(int h=0;h<JViewerApp.getInstance().getM_fdNum();h++){
				if(JViewerApp.getInstance().getUSBRedirSession().floppySession[h] != null){
					JViewerApp.getInstance().getUSBRedirSession().StopFloppyRedir(h, IUSBRedirSession.STOP_NORMAL);
					JViewerApp.getInstance().getM_IUSBSession().updateFDToolbarButtonStatus(false);
				}
			}
			for(int h=0;h<JViewerApp.getInstance().getM_hdNum();h++){
				if(JViewerApp.getInstance().getUSBRedirSession().harddiskSession[h] != null){
					JViewerApp.getInstance().getUSBRedirSession().StopHarddiskRedir(h, IUSBRedirSession.STOP_NORMAL);
					JViewerApp.getInstance().getM_IUSBSession().updateHDToolbarButtonStatus(false);
				}
			}

		} catch (Exception e) {
			Debug.out.println(e);
		}
	}
}
