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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.kvmpkts.Mousecaliberation;

import com.ami.kvm.jviewer.hid.USBMouseRep;
import com.ami.kvm.jviewer.kvmpkts.KVMClient;
public class JVToolBar extends JToolBar implements MouseListener {

	//	Local variables
	
	 /**
	 * 
	 */
	 private JButton   altTab;
	 private JToolBar  m_toolbar;
	 public  JButton   mouseBtn;
	 public  JButton   cdBtn;
	 public  JButton   floppyBtn;
	 public  JButton   hardddiskBtn;
	 public  JButton   kbdBtn;
	 public  JButton   playBtn;
	 public  JButton   pauseBtn;
	 public JButton   fullScreenBtn;
	 public  JButton   videoRecordBtn;
	 public  JButton   powerBtn;
	 public  JButton   hostDisplayBtn;
	 public  JSlider   slider_zoom;
	 
	 private JPopupMenu m_popupMenu;
	 public  JButton 	m_hotKeyBtn;
	 private JMenuItem   altCtrlDelBtn;
	 protected static Hashtable<JButton ,String> m_menutoolbar_text = new Hashtable<JButton,String>();
	 
	 public JButton usersBtn;
	 private JPopupMenu userPopupMenu;

	 protected JButton createButton(String iconPath, String tooltip, int prefX, int prefY, int maxX, int maxY)
	 {
		 URL imageURL;
		 if (iconPath == null)
			 imageURL = null;
		 else
			 imageURL = com.ami.kvm.jviewer.JViewer.class.getResource(iconPath);
		 JButton button = new JButton() { public Point getToolTipLocation(MouseEvent event) { return new Point(getWidth(), getHeight()-40); } };
	     if (imageURL != null) {
	    	 button.setIcon(new ImageIcon(imageURL));
	     }
	     button.setToolTipText(tooltip);
	     //button.setPreferredSize(new Dimension(prefX, prefY));
	     button.setPreferredSize(new Dimension(maxX, maxY));
	     button.setMaximumSize(new Dimension(maxX, maxY));
	     button.setMinimumSize(new Dimension(maxX, maxY));
	     button.setVisible(true);
	     button.setRequestFocusEnabled(false);
	     button.addMouseListener(this);
	     return button;
	 }
	 
    @SuppressWarnings("serial")
	public JVToolBar ()  {

		m_toolbar = new JToolBar();
		ToolTipManager.sharedInstance().setInitialDelay(0);

		mouseBtn  	   = createButton ("res/Mouse2Btn.png", LocaleStrings.getString("Q_1_JVT"), 23, 23, 33, 33); // number are preferred X, Y and Max x and Y
		m_menutoolbar_text.put(mouseBtn,"Q_1_JVT");

		cdBtn 	  	   = createButton ("res/CD.png", LocaleStrings.getString("Q_2_JVT"), 23, 23, 33, 33);
		m_menutoolbar_text.put(cdBtn,"Q_2_JVT" );

		floppyBtn 	   = createButton ("res/Floppy.png", LocaleStrings.getString("Q_3_JVT"), 23, 23, 33, 33);
		m_menutoolbar_text.put(floppyBtn,"Q_3_JVT");

		hardddiskBtn   = createButton ("res/HD.png", LocaleStrings.getString("Q_4_JVT"), 23, 23, 33, 33);
		m_menutoolbar_text.put(hardddiskBtn , "Q_4_JVT");

		kbdBtn		   = createButton ("res/keyboard.png", LocaleStrings.getString("Q_5_JVT"), 23, 23, 33, 33);
		m_menutoolbar_text.put(kbdBtn,"Q_5_JVT");

		fullScreenBtn  = createButton ("res/Maxi.png", LocaleStrings.getString("Q_6_JVT"), 23, 23, 33, 33);
		m_menutoolbar_text.put(fullScreenBtn,"Q_6_JVT");

		//altTab		   = createButton (null, "Send Alt+Tab", 100, 20, 120, 20);
		videoRecordBtn = createButton ("res/videoRecord.png", LocaleStrings.getString("Q_7_JVT"), 23, 23, 33, 33);
		m_menutoolbar_text.put(videoRecordBtn,"Q_7_JVT");

		pauseBtn	= createButton ("res/pause.png", LocaleStrings.getString("Q_8_JVT"), 23, 23, 33, 33);
		m_menutoolbar_text.put(pauseBtn,"Q_8_JVT");

		playBtn		= createButton ("res/play.png", LocaleStrings.getString("Q_9_JVT"), 23, 23, 33, 33);
		m_menutoolbar_text.put(playBtn,"Q_9_JVT");

		m_hotKeyBtn	= createButton ("res/Hot-keys.png", LocaleStrings.getString("Q_10_JVT"), 23, 23, 33, 33);
		m_menutoolbar_text.put(m_hotKeyBtn,"Q_10_JVT");

		usersBtn	= createButton ("res/user.png", LocaleStrings.getString("Q_11_JVT"), 23, 23, 33, 33);
		m_menutoolbar_text.put(usersBtn,"Q_11_JVT");

		powerBtn	= createButton("res/poweroff.png", LocaleStrings.getString("Q_12_JVT"), 23, 23, 33, 33);
		m_menutoolbar_text.put(powerBtn,"Q_12_JVT");

		hostDisplayBtn	= createButton("res/monitorOn.png", LocaleStrings.getString("Q_21_JVT"), 23, 23, 33, 33);
		m_menutoolbar_text.put(hostDisplayBtn,"Q_21_JVT");

		altCtrlDelBtn = new JMenuItem("Ctrl + Alt + Del");
		altCtrlDelBtn.setToolTipText(LocaleStrings.getString("Q_13_JVT")+" Ctrl+Alt+Del "+LocaleStrings.getString("Q_14_JVT"));
		altCtrlDelBtn.setVisible(true);
		altCtrlDelBtn.setRequestFocusEnabled(false);
		altCtrlDelBtn.addMouseListener(this);
        
	        m_popupMenu = new JPopupMenu();
	        m_popupMenu.add(altCtrlDelBtn);
	        m_toolbar.add(playBtn);
	        m_toolbar.add(pauseBtn);
	        m_toolbar.addSeparator();
	        m_toolbar.add(fullScreenBtn);
	        m_toolbar.addSeparator();
	        m_toolbar.add(hardddiskBtn);
	        m_toolbar.add(floppyBtn);
	        m_toolbar.add(cdBtn);
	        m_toolbar.addSeparator();
	        m_toolbar.add(mouseBtn);
	        m_toolbar.add(kbdBtn);
	        m_toolbar.addSeparator();
	        m_toolbar.add(videoRecordBtn);
	        m_toolbar.addSeparator();
	        m_toolbar.add(m_hotKeyBtn);
	        m_toolbar.addSeparator();
	        m_toolbar.add(constructZoomMenu());
	        m_toolbar.addSeparator();
	       // m_toolbar.add(textlabel);
	        m_toolbar.add(Box.createHorizontalGlue());
	        m_toolbar.add(usersBtn);
	        m_toolbar.addSeparator();
	        m_toolbar.add(hostDisplayBtn);
	        m_toolbar.add(Box.createHorizontalStrut(1));
	        m_toolbar.add(powerBtn);
	        Border m_raised = new EtchedBorder(BevelBorder.LOWERED);  
	        m_toolbar.setBorder(m_raised);
	        m_toolbar.setFloatable(false);
	        m_toolbar.repaint();
	       
	
	 }

    protected JSlider constructZoomMenu() {
    	if(slider_zoom == null)
    	{
	    	slider_zoom = new JSlider(50,150);
	    	slider_zoom.setValue(100);
    		slider_zoom.setMaximumSize(new Dimension(200, Short.MAX_VALUE));
    		slider_zoom.setMinimumSize(new Dimension(200, Short.MAX_VALUE));
    	
    		slider_zoom.setBorder(null);
    		slider_zoom.setPaintLabels(true);
    		slider_zoom.setMajorTickSpacing(50);
    		slider_zoom.setMaximum(150);
    		slider_zoom.setMinimum(50);
    		slider_zoom.setMinorTickSpacing(0);
    		slider_zoom.setToolTipText("100%");

	        slider_zoom.setFocusable(false);
	        slider_zoom.addChangeListener(new MyChangeAction());
	        slider_zoom.setRequestFocusEnabled(false);

		//solved changing Zoom Size will make mouse cursor inconsist
		slider_zoom.addMouseListener(this);
        }
        return slider_zoom;
    }

    public void setZoomLabel(int value){
    	String str = Integer.toString(value);
        JViewerApp.getInstance().getM_wndFrame().getMenu().setZoomLabelText(str+"%");
        slider_zoom.setToolTipText(str+"%");
    }

    private  class MyChangeAction implements ChangeListener {
    	public void stateChanged(ChangeEvent ce)
    	{
    		if(!JViewerApp.getInstance().getKVMClient().redirection())
    			return;
    		int value = slider_zoom.getValue();
    		setZoomLabel(value);
    		BigDecimal b = BigDecimal.valueOf((value),1);
    		JVMenu.m_scale = b.doubleValue()/10;
    		JViewerApp.getInstance().getRCView().setScaleFactor(JVMenu.m_scale, JVMenu.m_scale);

    		if(value >= 50 && value <= 150)
    		{
    			JViewerApp.getInstance().getJVMenu().SetMenuEnable(JVMenu.ZOOM_IN, true);
    			JViewerApp.getInstance().getJVMenu().getMenuItem(JVMenu.ZOOM_IN).setEnabled(true);
    			JViewerApp.getInstance().getJVMenu().SetMenuEnable(JVMenu.ZOOM_OUT, true);
    			JViewerApp.getInstance().getJVMenu().getMenuItem(JVMenu.ZOOM_OUT).setEnabled(true);

    		}
    		if(value >= 150){
    			JViewerApp.getInstance().getJVMenu().SetMenuEnable(JVMenu.ZOOM_IN, false);
    			JViewerApp.getInstance().getJVMenu().getMenuItem(JVMenu.ZOOM_IN).setEnabled(false);
    		}
    		if(value <= 50){
    			JViewerApp.getInstance().getJVMenu().SetMenuEnable(JVMenu.ZOOM_OUT, false);
    			JViewerApp.getInstance().getJVMenu().getMenuItem(JVMenu.ZOOM_OUT).setEnabled(false);
    		}
    		if(JViewerApp.getInstance().getRCView().GetUSBMouseMode() == USBMouseRep.RELATIVE_MOUSE_MODE){
    			changeShowCursorOnZoom();
    		}
			//No zoom options will be selected if zoom is not 100%
    		if(JVMenu.m_scale != 1.0){
    			JViewerApp.getInstance().getJVMenu().getMenuItem(JVMenu.ZOOM_OPTION_NONE).setSelected(true);
    			JViewerApp.getInstance().setZoomOption(JVMenu.ZOOM_OPTION_NONE);
    		}
			//Actual size zoom option is selected when the zoom value is 100%
    		else{
    			JViewerApp.getInstance().getJVMenu().getMenuItem(JVMenu.ACTUAL_SIZE).setSelected(true);
    			JViewerApp.getInstance().setZoomOption(JVMenu.ACTUAL_SIZE);
    		}
    		JViewerApp.getInstance().getRCView().revalidate();
    		JViewerApp.getInstance().getRCView().repaint();
    	}
    }
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("static-access")
	public void mousePressed(MouseEvent e) {

		if(e.getSource() == mouseBtn)
        	{
	   		if(!JViewerApp.getInstance().getKVMClient().redirection())
        			return;
           		if(JViewerApp.getInstance().getMainWindow().getMenu().getMenuItem(JVMenu.MOUSE_CLIENTCURSOR_CONTROL).isSelected())
			{
               			if(JViewerApp.getInstance().getMainWindow().getMenu().getMenuItem(JVMenu.MOUSE_CLIENTCURSOR_CONTROL).isEnabled())
				{
					JViewerApp.getInstance().OnShowCursor(false);
				}
			}
            		else
            		{
                		JViewerApp.getInstance().OnShowCursor(true);
            		}
        	}
		else if(e.getSource()== kbdBtn)
	   	{
			if(!JViewerApp.getInstance().getKVMClient().redirection())
        			return;
			
			JVMenu menu = JViewerApp.getInstance().getJVMenu();
			AutoKeyboardLayout autokeylayout;
			if(JViewerApp.getInstance().getAutokeylayout() != null )
				autokeylayout = JViewerApp.getInstance().getAutokeylayout();
			else
			{
				autokeylayout = new AutoKeyboardLayout();
				JViewerApp.getInstance().setAutokeylayout(autokeylayout);
			}
				   
			if(menu.getMenuSelected(menu.AUTOMAIC_LANGUAGE) == false && menu.keyBoardLayout >= menu.LANGUAGE_ENGLISH_US)
			{
				JViewerApp.getInstance().OnSkbrdDisplay(menu.keyBoardLayout);
			}
			else 
			{   
			   	menu.notifyMenuStateSelected(JVMenu.AUTOMAIC_LANGUAGE, true);
			   	menu.getMenuItem(JVMenu.PKBRD_NONE).setSelected(true);
			   	if(autokeylayout.getKeyboardType() == autokeylayout.KBD_TYPE_FRENCH)
				{
				   	JViewerApp.getInstance().OnSkbrdDisplay(menu.LANGUAGE_FRENCH);
			   	}
			   	else if(autokeylayout.getKeyboardType() == autokeylayout.KBD_TYPE_GERMAN)
				{
				   	JViewerApp.getInstance().OnSkbrdDisplay(menu.LANGUAGE_GERMAN_GER);
			   	}
			   	else if(autokeylayout.getKeyboardType() == 1033)
				{
				   	JViewerApp.getInstance().OnSkbrdDisplay(menu.LANGUAGE_ENGLISH_US);
			   	}
			   	else if(autokeylayout.getKeyboardType() == autokeylayout.KBD_TYPE_JAPANESE)
				{
				   	JViewerApp.getInstance().OnSkbrdDisplay(menu.LANGUAGE_JAPANESE_Q);
			   	}
			   	else if(autokeylayout.getKeyboardType() == autokeylayout.KBD_TYPE_SPANISH)
				{
				   	JViewerApp.getInstance().OnSkbrdDisplay(menu.LANGUAGE_SPANISH);
			   	}
			   	else
				{
				   	JViewerApp.getInstance().OnSkbrdDisplay(menu.LANGUAGE_ENGLISH_US);
			   	}
		   	}
	   	}
		else if(e.getSource()== altCtrlDelBtn)
       		{
		   	if(!JViewerApp.getInstance().getKVMClient().redirection())
        			return;
		   	if((JViewerApp.getInstance().getJVMenu().getMenuSelected(JVMenu.KEYBOARD_LEFT_CTRL_KEY) || JViewerApp.getInstance().getJVMenu().getMenuSelected(JVMenu.KEYBOARD_RIGHT_CTRL_KEY ))
					&& (JViewerApp.getInstance().getJVMenu().getMenuSelected(JVMenu.KEYBOARD_LEFT_ALT_KEY) || JViewerApp.getInstance().getJVMenu().getMenuSelected(JVMenu.KEYBOARD_RIGHT_ALT_KEY)))
				return ;
			else
			{
				JViewerApp.getInstance().OnKeyboardAltCtrlDel();
			}
       		}
		else if(e.getSource()== m_hotKeyBtn)
	   	{
			m_popupMenu.show(e.getComponent(), 0, e.getComponent().getHeight());  // this x and y valuse is hardcoded to display this popup menu as drop down menu
       		}
		else if(e.getSource()== playBtn)
       		{
       			if(JViewerApp.getInstance().getJVMenu().getMenuItem(JVMenu.VIDEO_RESUME_REDIRECTION).isEnabled())
       			{
       				JViewerApp.getInstance().OnVideoResumeRedirection();       			
	 		}
       		}
		else if(e.getSource()== pauseBtn)
       		{
		   	if(JViewerApp.getInstance().getJVMenu().getMenuItem(JVMenu.VIDEO_PAUSE_REDIRECTION).isEnabled())
       			{
			   	JViewerApp.getInstance().setM_userPause(true);
			   	JViewerApp.getInstance().OnVideoPauseRedirection();       			
       			}
       		}
	   	else if(e.getSource()== usersBtn)
	   	{
		   	userPopupMenu = new JPopupMenu();
		   	int numUsers = KVMClient.getNumUsers();
			String[] userData = KVMClient.getUserData();
			if(userData != null && numUsers != 0)
			{			
				ImageIcon menuIcon;
				for (int i = 0;i <  numUsers;i++)
				{
					menuIcon = null;
					String display = (userData[i].substring(userData[i].indexOf(":")+1, userData[i].length())).trim();
					String index = (userData[i].substring(0,userData[i].indexOf(":")-1)).trim();
					if(Integer.parseInt(index) == JViewerApp.getInstance().getCurrentSessionId()){
						URL imageURL = com.ami.kvm.jviewer.JViewer.class.getResource("res/green.png");
						if(imageURL != null)
							menuIcon = new ImageIcon(imageURL);
					}
					userPopupMenu.add(new JMenuItem(display,menuIcon));
				}
			}
			userPopupMenu.show(e.getComponent(), 0, e.getComponent().getHeight());  // this x and y valuse is hardcoded to display this popup menu as drop down menu			
       		}	
	   	else if(e.getSource().equals(powerBtn))
		{
			//jviewer session doesn't have privilege to execute power commands
			if(!JViewer.isPowerPrivEnabled())
			{
				InfoDialog.showDialog(JViewerApp.getInstance().getMainWindow(), LocaleStrings.getString("D_62_JVAPP"),
						LocaleStrings.getString("A_6_GLOBAL"),InfoDialog.INFORMATION_DIALOG);
				return ;
			}

		   	if(JViewerApp.getInstance().getM_frame().getMenu().getMenuEnable(JVMenu.POWER_ON_SERVER))
			{
			   	JViewerApp.getInstance().onSendPowerControlCommand(JVMenu.POWER_ON_SERVER);
		   	}
		   	else if(JViewerApp.getInstance().getM_frame().getMenu().getMenuEnable(JVMenu.POWER_OFF_IMMEDIATE))
			{
			   	JViewerApp.getInstance().onSendPowerControlCommand(JVMenu.POWER_OFF_IMMEDIATE);
		   	}
	   	}
		else if (e.getSource().equals(hostDisplayBtn)) {
			if(JViewerApp.getInstance().getM_frame().getMenu().getMenuItem(JVMenu.VIDEO_HOST_DISPLAY_UNLOCK).isEnabled())
				JViewerApp.getInstance().onSendHostLock(JViewerApp.HOST_DISPLAY_UNLOCK);
			else if(JViewerApp.getInstance().getM_frame().getMenu().getMenuItem(JVMenu.VIDEO_HOST_DISPLAY_LOCK).isEnabled())
				JViewerApp.getInstance().onSendHostLock(JViewerApp.HOST_DISPLAY_LOCK);
		}
		return ;
	}

	public void mouseReleased(MouseEvent e) {
/*		if(e.getSource()== m_hotKeyBtn)
			m_popupMenu.show(false);*/
		//solved changing Zoom Size will make mouse cursor inconsist
		if(e.getSource() == slider_zoom) {			
			boolean cursurMenuState = JViewerApp.getInstance().getJVMenu().getMenuSelected(JVMenu.MOUSE_CLIENTCURSOR_CONTROL);
			if (slider_zoom.getValue() == 100 && cursurMenuState) {
				Mousecaliberation.resetCursor();
			}
		}
                else if(e.getSource()== fullScreenBtn)
       		{
           		if(JViewerApp.getInstance().getMainWindow().getMenu().getMenuItem(JVMenu.VIDEO_FULL_SCREEN).isEnabled())
           		{
                   		if(JViewerApp.getInstance().getMainWindow().getMenu().getMenuItem(JVMenu.VIDEO_FULL_SCREEN).isSelected())
                           	{
                                   	JViewerApp.getInstance().OnVideoFullScreen(false);
                       			JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.VIDEO_FULL_SCREEN,false);
                           	}
               			else
               			{
                   			JViewerApp.getInstance().OnVideoFullScreen(true);
                   			JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.VIDEO_FULL_SCREEN,true);
               			}
           		}
			else
			{
                   		fullScreenBtn.setToolTipText(LocaleStrings.getString("Q_16_JVT"));
           		}
		}
		else if((e.getSource() == cdBtn) || (e.getSource() == floppyBtn) || (e.getSource() == hardddiskBtn))
		{
			Debug.out.println("MEDIA LICENSE STATUS : "+JViewer.getMediaLicenseStatus());
			if(JViewerApp.getInstance().getMainWindow().getMenu().getMenuItem(JVMenu.DEVICE_MEDIA_DIALOG).isEnabled())
			{
				if(JViewer.getMediaLicenseStatus() == JViewer.LICENSED)
					JViewerApp.getInstance().OnvMedia();
				else{
					InfoDialog.showDialog(JViewer.getMainFrame(), LocaleStrings.getString("F_136_JVM"),
							LocaleStrings.getString("2_4_KVMCLIENT"), InfoDialog.INFORMATION_DIALOG);
				}
			}
		}

		else if(e.getSource() == videoRecordBtn)
		{

                    	if(JViewerApp.getInstance().getMainWindow().getMenu().getMenuItem(JVMenu.VIDEO_RECORD_START).isEnabled())
                    	{
                           	JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.VIDEO_RECORD_START, false);
                           	JViewerApp.getInstance().getM_videorecord().OnVideoRecordStart();
                   	}
                   	else if(JViewerApp.getInstance().getMainWindow().getMenu().getMenuItem(JVMenu.VIDEO_RECORD_STOP).isEnabled())
                   	{
                           	JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.VIDEO_RECORD_STOP, false);
                           	JViewerApp.getInstance().getM_videorecord().OnVideoRecordStop();
                   	}
                   	else if(JViewerApp.getInstance().getMainWindow().getMenu().getMenuItem(JVMenu.VIDEO_RECORD_SETTINGS).isEnabled())
                   	{
                           	//Don't show video record settings dialog in video recording is in progress.
                           	if(!VideoRecord.Recording_Started || !VideoRecord.Record_Processing)
				{
                                   	JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.VIDEO_RECORD_SETTINGS, false);
                                   	JViewerApp.getInstance().OnVideoRecordSettings();
                           	}
                   	}
           	}
	}
	
	public void changeMacrowsStatusOnPauseResume(boolean status){
		// Remove the mouse listener from toolbar buttons while pausing 
		// and add the mouse listener while resuming.
		setButtonEnabled(mouseBtn, status);
		setButtonEnabled(kbdBtn, status);
		setButtonEnabled(cdBtn, status);
		setButtonEnabled(floppyBtn, status);
		setButtonEnabled(hardddiskBtn, status);
		setButtonEnabled(m_hotKeyBtn, status);
		setButtonEnabled(videoRecordBtn, status);
		setButtonEnabled(powerBtn, status);

		/*
		 * Following method call is executed only for a second client session with, partial kvm privilage.		 
		 */
		if(KVMSharing.KVM_REQ_GIVEN == KVMSharing.KVM_REQ_PARTIAL )
			OnChangeToolbarIconState_KVMPartial();
		if(JViewerApp.getInstance().getZoomOption() == JVMenu.ACTUAL_SIZE)
			enableZoomSlider(status);
	}
	
	/**
	 * Enable or disable the Keyboard, Mouse, and Hotkey button on the toolbar,while partial access is given to the concurrent session in KVM sharing.
	 */
	public void OnChangeToolbarIconState_KVMPartial()
	{
		boolean powerBtnStatus = false;
		JVMenu menu = JViewerApp.getInstance().getM_wndFrame().getMenu();
		setButtonEnabled(kbdBtn, menu.getMenuItem(JVMenu.AUTOMAIC_LANGUAGE).isEnabled());
		setButtonEnabled(mouseBtn, menu.getMenuItem(JVMenu.MOUSE_CLIENTCURSOR_CONTROL).isEnabled());		
		setButtonEnabled(m_hotKeyBtn, menu.getMenuItem(JVMenu.KEYBOARD_CTRL_ALT_DEL).isEnabled());
		setButtonEnabled(floppyBtn, menu.getMenuItem(JVMenu.DEVICE_MEDIA_DIALOG).isEnabled());
		setButtonEnabled(cdBtn, menu.getMenuItem(JVMenu.DEVICE_MEDIA_DIALOG).isEnabled());
		setButtonEnabled(hardddiskBtn, menu.getMenuItem(JVMenu.DEVICE_MEDIA_DIALOG).isEnabled());
		// Enable power button mouse event on either power off or power on is enabled. if KVM_REQ_GIVEN is KVM_REQ_PARTIAL then disable power button.
		if(KVMSharing.KVM_REQ_GIVEN != KVMSharing.KVM_REQ_PARTIAL)
			powerBtnStatus = (menu.getMenuItem(JVMenu.POWER_OFF_IMMEDIATE).isEnabled() ||
								menu.getMenuItem(JVMenu.POWER_ON_SERVER).isEnabled());
		setButtonEnabled(powerBtn, powerBtnStatus);
	}
	
		/**
	 * Enable/Disable the tool bar buttons when power status changes.
	 * @param status boolean (true/false).
	 */
	public void changeToolbarButtonStateOnPowerStatus(boolean status){
		JVMenu menu = JViewerApp.getInstance().getM_wndFrame().getMenu();
		setButtonEnabled(playBtn, menu.getMenuItem(JVMenu.VIDEO_RESUME_REDIRECTION).isEnabled());
		setButtonEnabled(pauseBtn,  menu.getMenuItem(JVMenu.VIDEO_PAUSE_REDIRECTION).isEnabled());
		setButtonEnabled(fullScreenBtn,  status);
		setButtonEnabled(kbdBtn,  menu.getMenuItem(JVMenu.AUTOMAIC_LANGUAGE).isEnabled());
		setButtonEnabled(floppyBtn,  menu.getMenuItem(JVMenu.DEVICE_MEDIA_DIALOG).isEnabled());
		setButtonEnabled(cdBtn,  menu.getMenuItem(JVMenu.DEVICE_MEDIA_DIALOG).isEnabled());
		setButtonEnabled(hardddiskBtn,  menu.getMenuItem(JVMenu.DEVICE_MEDIA_DIALOG).isEnabled());
		setButtonEnabled(mouseBtn,  menu.getMenuItem(JVMenu.MOUSE_CLIENTCURSOR_CONTROL).isEnabled());
		//No need to check the menu status for the here, applying teh value of status itself
		//is enough, because the up on teh button click event video record settings dialog 
		//will be shown only if the video record settings menu is enabled.
		setButtonEnabled(videoRecordBtn,  status);
		setButtonEnabled(m_hotKeyBtn,  menu.getMenuItem(JVMenu.KEYBOARD_CTRL_ALT_DEL).isEnabled());
		if(JViewerApp.getInstance().getZoomOption() == JVMenu.ACTUAL_SIZE)
			enableZoomSlider(status);
	}
	/**
	 * Enable or disable a toolbar button and add or remove its MouseListener.  
	 * @param button - Toolbar button to be enabled or disabled.
	 * @param state - Enable(true) or disable(false).
	 */
	public void setButtonEnabled(JButton button, boolean state){
		MouseListener[] mListeners = button.getMouseListeners();
		if(!button.equals(powerBtn))
			button.setEnabled(state);
		if(button.equals(playBtn) || button.equals(pauseBtn))
			return;
		if(state){
			for(MouseListener ml : mListeners){
				if(ml.equals(this))
					return;
			}
			button.addMouseListener(this);
		}
		else{
			for(MouseListener ml : mListeners){
				if(ml.equals(this))
					button.removeMouseListener(this);
			}
		}
	}
	
	public void turnOnPowerButton(boolean state){
		if(state){
			URL imageURL = com.ami.kvm.jviewer.JViewer.class.getResource("res/poweron.png");
			powerBtn.setIcon(new ImageIcon(imageURL));
			powerBtn.setToolTipText(LocaleStrings.getString("Q_17_JVT"));
		}
		else{
			URL imageURL = com.ami.kvm.jviewer.JViewer.class.getResource("res/poweroff.png");
			powerBtn.setIcon(new ImageIcon(imageURL));
			powerBtn.setToolTipText(LocaleStrings.getString("Q_18_JVT"));
		}
	}
	
	public void turnOnHostDisplayButton(boolean status){
		if(status){
			URL imageURL = com.ami.kvm.jviewer.JViewer.class.getResource("res/monitorOn.png");
			hostDisplayBtn.setIcon(new ImageIcon(imageURL));
			hostDisplayBtn.setToolTipText(LocaleStrings.getString("Q_22_JVT"));
		}
		else{
			URL imageURL = com.ami.kvm.jviewer.JViewer.class.getResource("res/monitorOff.png");
			hostDisplayBtn.setIcon(new ImageIcon(imageURL));
			hostDisplayBtn.setToolTipText(LocaleStrings.getString("Q_23_JVT"));
		}
	}
	public JToolBar getToolBar() {
		return m_toolbar;
	}


	public JButton getCdBtn() {
		return cdBtn;
	}

	public void setCdBtn(JButton cdBtn) {
		this.cdBtn = cdBtn;
	}

	public JButton getFloppyBtn() {
		return floppyBtn;
	}

	public void setFloppyBtn(JButton floppyBtn) {
		this.floppyBtn = floppyBtn;
	}

	public JButton getHardddiskBtn() {
		return hardddiskBtn;
	}

	public void setHardddiskBtn(JButton hardddiskBtn) {
		this.hardddiskBtn = hardddiskBtn;
	}
	/**
	 * @return the m_popupMenu
	 */
	public JPopupMenu getM_popupMenu() {
		return m_popupMenu;
	}

	public void changeMacrowsStatus(boolean status){
		mouseBtn.setEnabled(status);
		cdBtn.setEnabled(status);
		floppyBtn.setEnabled(status);
		hardddiskBtn.setEnabled(status);
		kbdBtn.setEnabled(status);
		playBtn.setEnabled(status);
		pauseBtn.setEnabled(status);
		fullScreenBtn.setEnabled(status);
		altCtrlDelBtn.setEnabled(status);
		//altTab.setEnabled(status);
		videoRecordBtn.setEnabled(status);
		m_hotKeyBtn.setEnabled(status);
		powerBtn.setEnabled(status);
		hostDisplayBtn.setEnabled(status);
		
		if(slider_zoom != null)
			slider_zoom.setValue(100);
		
	}
	
	/**
	 * Add the menu item for each user defined hot key macro defined, on to the 
	 * pop up menu on the hotkey button.
	 * @param menuString
	 */
	public void addHotkeyPoupMenuItem(String menuString){
		JMenuItem popupMenuItem = new JMenuItem(menuString);
		popupMenuItem.addActionListener(JViewerApp.getInstance().getJVMenu().m_menuListener);
		popupMenuItem.setActionCommand("HK_"+menuString);
		m_popupMenu.add(popupMenuItem,2);
	}

	/**
	 * Remove all the user defined hot key macro menuitems from teh popup menu on the
	 * hot key button, except the alt+ctrl+del menuitem.   
	 */
	public void removeHotkeyPoupMenuItem(){
		m_popupMenu.removeAll();
		m_popupMenu.add(altCtrlDelBtn);
	}
	
	/*
	* Changes toolbar items text language
	*/
	public void changeToolBarItemLanguage() {
		Set st = m_menutoolbar_text.entrySet();
		Iterator itr = st.iterator();
		Object value = null ;
		JButton btn ;

		while (itr.hasNext())
		{
			try{
				Map.Entry me = (Map.Entry)itr.next();
				value = me.getValue();
				btn = (JButton) me.getKey();
				btn.setToolTipText(LocaleStrings.getString(value.toString()));
			}catch(Exception e){
				Debug.out.println(e);
			}
		}
	}

	/**
	 * Enable/Disable zoom slider
	 * @param status - true to enable zoom slider; false otherwise.
	 */
	public void enableZoomSlider(boolean status){
		if(status){
			if(slider_zoom.getMouseListeners().equals(null))
				slider_zoom.addMouseListener(this);
			setZoomLabel(slider_zoom.getValue());
		}
		else{
			slider_zoom.removeMouseListener(this);
			slider_zoom.setToolTipText(LocaleStrings.getString("Q_19_JVT"));
			JViewerApp.getInstance().getM_wndFrame().getMenu().setZoomLabelText(LocaleStrings.getString("Q_20_JVT"));
		}
		slider_zoom.setEnabled(status);
		JViewerApp.getInstance().getJVMenu().notifyMenuStateEnable(JVMenu.ZOOM_IN, status);
		JViewerApp.getInstance().getJVMenu().notifyMenuStateEnable(JVMenu.ZOOM_OUT, status);
	}
	
	/**
	 * Reset the video zoom to 100%
	 */
	public void resetZoom(){
		slider_zoom.setValue(100);
		JViewerApp.getInstance().getRCView().revalidate();
		JViewerApp.getInstance().getRCView().repaint();
		JVMenu.m_scale = 1.0;
		JViewerApp.getInstance().getRCView().setScaleFactor(JVMenu.m_scale, JVMenu.m_scale);
	}
	
	/**
	 * Change the state of show cursor menu and toolbar button when zoom is not 100 %
	 */
	public void changeShowCursorOnZoom(){
		if(JVMenu.m_scale != 1.0){
			URL imageURLMouse = com.ami.kvm.jviewer.JViewer.class.getResource("res/Mouse2Btn-gray.png");
			JViewerApp.getInstance().getM_wndFrame().toolbar.mouseBtn.setIcon(new ImageIcon(imageURLMouse));
			JViewerApp.getInstance().getM_wndFrame().toolbar.mouseBtn.setToolTipText(LocaleStrings.getString("D_48_JVAPP"));
			JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.MOUSE_CLIENTCURSOR_CONTROL, false);
			JViewerApp.getInstance().getJVMenu().notifyMenuStateEnable(JVMenu.MOUSE_CLIENTCURSOR_CONTROL, false);
			JViewerApp.getInstance().showCursor = false;
		}
		else if(JVMenu.m_scale == 1.0){
			JViewerApp.getInstance().getJVMenu().notifyMenuStateEnable(JVMenu.MOUSE_CLIENTCURSOR_CONTROL, true);
			JViewerApp.getInstance().getM_wndFrame().toolbar.mouseBtn.setToolTipText(LocaleStrings.getString("D_15_JVAPP"));
		}
	}

	/**
	 * Remove VMedia buttons from toolbar.
	 */
	public void removeVMediaButtons(){
		m_toolbar.remove(cdBtn);
		m_toolbar.remove(floppyBtn);
		m_toolbar.remove(hardddiskBtn);
	}

	/**
	 * Removes the host dispalay status button from toolbar.
	 */
	public void removeHostDisplayStatusButtons(){
		m_toolbar.remove(hostDisplayBtn);
	}
}

