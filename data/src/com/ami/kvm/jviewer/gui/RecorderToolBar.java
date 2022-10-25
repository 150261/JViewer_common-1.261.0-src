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
////////////////////////////////////////////////////////////////////////////////////////
//This module implements the toolbar for the VideoPlayerApp 
//
package com.ami.kvm.jviewer.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class RecorderToolBar extends JVToolBar implements MouseListener{
	
	private JToolBar m_recToolbar;
	public JButton replayButton;	

	public RecorderToolBar(){
		m_recToolbar = new JToolBar();
		replayButton = createButton("res/play.png", LocaleStrings.getString("R_1_RT"), 33, 33,33,33);	
		
		m_recToolbar.add(replayButton);
		
		Border m_raised = new EtchedBorder(BevelBorder.LOWERED);		
        m_recToolbar.setBorder(m_raised);
        m_recToolbar.setFloatable(false);        
        m_recToolbar.repaint();
	}
	public JToolBar getToolBar() {
		return m_recToolbar;
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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub	
		if(e.getSource().equals(replayButton)){
			JViewerApp.getInstance().getVideorecordapp().OnVideorecordStartRedirection();			
		}		
	}
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Disables the given ToolBar button
	 * @param button - The button to be disabled
	 */
	public void disableButton(JButton button){
		button.setEnabled(false);
		button.removeMouseListener(this);
	}
	/**
	 * Enables the given ToolBar button
	 * @param button - The button to be enabled
	 */
	public void enableButton(JButton button){
		button.setEnabled(true);
		button.addMouseListener(this);
	}
}
