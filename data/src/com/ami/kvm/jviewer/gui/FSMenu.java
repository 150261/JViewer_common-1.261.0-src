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
// JViewer Full screen mode menu component.
//

package com.ami.kvm.jviewer.gui;

import javax.swing.JPopupMenu;

import com.ami.kvm.jviewer.common.ISOCMenu;

/**
 * Full screen Menu class.
 */
public class FSMenu extends JVMenu {

    private JPopupMenu m_popupMenu;
    private ISOCMenu m_soc_menu;

	/**
	 * The constructor.
	 */
	public FSMenu()
	{
		m_soc_menu = JViewerApp.getSoc_manager().getSOCmenu();
		m_soc_menu.SetSOCMenuItem(m_menuItems);
		m_soc_menu.SetSOCMenu(m_menu);
		constructUserIf();

	}

    /**
     * Get the popup menu.
     */
	public JPopupMenu getPopupMenu()
	{
		return m_popupMenu;
	}

	/*
	 * Construct user interface.
	 */
	private void constructUserIf()
	{
		m_popupMenu	= new JPopupMenu();
		m_popupMenu.add(constructVideoMenu());
		m_popupMenu.add(constructKeyboardMenu());
		m_popupMenu.add(constructMouseMenu());
		m_popupMenu.add(constructOptionsMenu());
		m_popupMenu.add(constructDeviceRedirMenu());
		m_popupMenu.add(constructKeyboardLayoutMenu());
		m_popupMenu.add(constructVideoRecordMenu());
		m_popupMenu.add(constructPowerMenu());
		m_popupMenu.add(constructUserMenu());
		m_popupMenu.add(constructHelpMenu());
		m_popupMenu.add(constructString());
		//Creating the SOC related menu specific to project related
		m_soc_menu.initKVMPartialExceptionSOCMenuItems();
		m_soc_menu.constructsocMenu(m_popupMenu);
	}

	/**
	 * @return the m_soc_menu
	 */
	public ISOCMenu getM_soc_menu() {
		return m_soc_menu;
	}
}