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

/*
 * KeyProcessor.java
 *
 * Created on January 12, 2005, 6:04 PM
 * Modified to G4 on Feb 17, 2006 11:55 AM
 */

package com.ami.kvm.jviewer.hid;

public interface KeyProcessor
{
	public static final int MOD_LEFT_CTRL = 0x01;
	public static final int MOD_RIGHT_CTRL = 0x10;
	public static final int MOD_LEFT_SHIFT = 0x02;
	public static final int MOD_RIGHT_SHIFT = 0x20;
	public static final int MOD_LEFT_ALT = 0x04;
	public static final int MOD_RIGHT_ALT = 0x40;
	public static final int MOD_LEFT_WIN = 0x08;
	public static final int MOD_RIGHT_WIN = 0x80;
	
	public byte[] convertKeyCode( int keyCode, int keyLocation, boolean keyPressed );
	public void setAutoKeybreakMode( boolean state );
	public boolean getAutoKeybreakMode();
}
