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
// This module implements the JViewer frame as a window.
//

package com.ami.kvm.jviewer.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.hid.USBMouseRep;
import com.ami.kvm.jviewer.kvmpkts.Mousecaliberation;

/**
 * Frame as a winodw class.
 */
@SuppressWarnings("serial")
public class WindowFrame extends JVFrame {

    private WindowMenu	m_menu;
    private JVStatusBar m_status;
    public Macrows macrows;
    public JVToolBar toolbar;
    private String m_label;
    private Dimension frameDimension = null;
    private boolean resizeFrame = false;

	/**
	 * The constructor.
	 */
	public WindowFrame() {

        setTitle(LocaleStrings.getString("C_1_JVF"));
		// construct user interface components
		constructUserIf();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		frameDimension = JViewer.getMainFrame().getSize();
		
		//Modified for JInternalFrame
		if( JViewer.isStandalone() ) {
			JViewer.getMainFrame().addWindowFocusListener
			(
				new WindowAdapter()
				{
					 public void windowGainedFocus(WindowEvent e)
					 {
						 JViewerView.syncLEDFlag = false;
						 JViewerView.Lost_focus_flag = false;
						 if(JViewerApp.getInstance().GetRedirectionState() != JViewerApp.REDIR_STOPPED &&
								 JViewerApp.getInstance().GetRedirectionState() != JViewerApp.REDIR_STOPPING){
							 if(JViewerApp.getInstance().getKVMClient() != null)
								 JViewerApp.getInstance().getKVMClient().sendLEDrequest();
							 if(JViewerApp.getInstance().getRCView().GetUSBMouseMode() != USBMouseRep.INVALID_MOUSE_MODE)
								 JViewerApp.getInstance().getRCView().USBsyncCursor(true);
							 JViewerApp.getInstance().resetModifiers();
						}
						 if(JViewer.isjviewerapp() || JViewer.isStandAloneApp()){
							 if(JViewerApp.getInstance().getVidClnt() != null)
								 JViewerApp.getInstance().getVidClnt().setZoomOptionStatus();
							 getM_status().resetStatus();
						 }
					 }
					 public void windowLostFocus(WindowEvent e)
					 {
						 JViewerView.syncLEDFlag = true;
					     JViewerView.Lost_focus_flag = true;
					     resizeFrame = true;
					     if(JViewerApp.getInstance().GetRedirectionState() == JViewerApp.REDIR_STARTED)
						 JViewerApp.getInstance().getRCView().USBsyncCursor(false);
					     frameDimension = JViewer.getMainFrame().getSize();
			         }
				}
			);
			
			// Add window listener.
			//JViewer.getMainFrame().addWindowListener
			JViewer.getMainFrame().addWindowListener
			(
				new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						WindowFrame.this.windowClosed();
					}
				}
			);
		} else {
			// Added for JInternalFrame        
	        this.addInternalFrameListener(
	        	new InternalFrameAdapter() {
	        		public void internalFrameActivated(InternalFrameEvent e) {
	        			if(!JViewer.isStandalone())
	        				JViewer.getMainFrame().setJMenuBar(m_menu.getMenuBar());
	        		}
	        		public void internalFrameClosing(InternalFrameEvent e) {
	        			WindowFrame.this.windowClosed();
		            }
	        	}
	        );	
		}
	}

	/**
	 * Get menu
	 *
	 * @return menu
	 */
	public JVMenu getMenu()
	{
		return m_menu;
	}

    /**
     * Set status.
     *
     * @param status message.
     */
    public void setStatus(String msg)
    {
    	m_status.setStatus(msg);
    }

    /**
     * Reset status.
     */
    public void resetStatus()
    {
    	m_status.resetStatus();
    }

    /**
     * Exit application.
     */
	public void exitApp()
	{
		windowClosed();
	}

	/**
	 * Refresh window label
	 */
	public void refreshTitle()
	{
		// Modified for JInternalFrame
		if( JViewer.isStandalone() )
			JViewer.getMainFrame().setTitle(JViewer.getTitle()+" [" + m_serverIP + "] - " + m_label);
		else
			setTitle(JViewer.getTitle()+" [" + m_serverIP + "] - " + m_label);
	}
	/**
	 * Set window label
	 * @param label label string
	 */
	public void setWndLabel(String label)
	{
		m_label = label;
		if( JViewer.isStandalone() )
			JViewer.getMainFrame().setTitle(JViewer.getTitle()+" [" + m_serverIP + "] - " + label);
		else
			setTitle(JViewer.getTitle()+" [" + m_serverIP + "] - " + label);
	}

	/**
	 *
	 * Construct user interface components.
	 *
	 */
	private void constructUserIf() {
		Container contentPane = getContentPane();
		contentPane.removeAll();
		contentPane.setLayout(new BorderLayout());
		 if(!JViewer.isdownloadapp()){
			 if(JViewer.isplayerapp())
					toolbar = new RecorderToolBar();
				else{
		// construct menu component
		m_menu = new WindowMenu();
		if(JViewer.isStandalone())
			setJMenuBar(m_menu.getMenuBar());
		else {
			JViewer.getMainFrame().setJMenuBar(m_menu.getMenuBar());
			JViewer.getMainFrame().setVisible(true);
		}		
		// construct status bar component
		m_status = new JVStatusBar();
		macrows = new Macrows(m_status);
		contentPane.add(m_status.getStatusBar(), BorderLayout.PAGE_END);
					m_status.setStatus(LocaleStrings.getString("W_1_WF"));
		toolbar = new JVToolBar();
				}
		contentPane.add(toolbar.getToolBar(), BorderLayout.PAGE_START);
		 }
		if( JViewer.isStandalone() ) {
			URL imageURL = com.ami.kvm.jviewer.JViewer.class.getResource("res/jviewer.jpg");
	        // Modified for JInternalFrmae
			JViewer.getMainFrame().setIconImage(Toolkit.getDefaultToolkit().getImage(imageURL));			
		}
	}

	public JVStatusBar getM_status() {
		return m_status;
	}

	public void setM_status(JVStatusBar m_status) {
		this.m_status = m_status;
	}

	public WindowMenu getWindowMenu() {
    	return m_menu;
	}

	/**
	 * @return the frameDimension
	 */
	public Dimension getFrameDimension() {
		return frameDimension;
	}

	/**
	 * @return the resizeFrame
	 */
	public boolean isResizeFrame() {
		return resizeFrame;
	}

	/**
	 * @param resizeFrame the value to set
	 */
	public void setResizeFrame(boolean resizeFrame) {
		this.resizeFrame = resizeFrame;
	}
}