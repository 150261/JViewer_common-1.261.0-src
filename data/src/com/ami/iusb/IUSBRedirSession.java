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

package com.ami.iusb;

import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.gui.InfoDialog;
import com.ami.kvm.jviewer.gui.JVFrame;
import com.ami.kvm.jviewer.gui.JViewerApp;
import com.ami.kvm.jviewer.gui.LocaleStrings;
import com.ami.kvm.jviewer.gui.vMediaDialog;

public class IUSBRedirSession
{
	/** size of each authentication packets */
	public static final byte AUTH_USER_PKT_SIZE = 98; /** size of user credentials auth packet */
	public static final short WEB_AUTH_PKT_MAX_SIZE	= AUTH_USER_PKT_SIZE; /** Max of all above auth packet sizes */
	public static final short SSI_AUTH_PKT_MAX_SIZE	= AUTH_USER_PKT_SIZE+112;
	public static final int 	DEVICE_REDIR_STATUS_IDLE = 0;
	public static final int 	DEVICE_REDIR_STATUS_CONNECTED = 1;

	public static final byte STOP_NORMAL = 0x00;
	public static final byte STOP_ON_EJECT = 0x01;
	public static final byte STOP_ON_TERMINATE = 0x02;

	public static final byte DEVICE_USED = 0x00;
	public static final byte DEVICE_FREE = 0x01;

	public CDROMRedir cdromSession[] ;
	public FloppyRedir floppySession[];
	public HarddiskRedir harddiskSession[];
	private	int cdromRedirStatus[];
	private	int floppyRedirStatus[];
	private	int harddiskRedirStatus[];

	private	int cdROMDeviceStatus[];
	private	int floppyDeviceStatus[];
	private	int hardDiskDeviceStatus[];
	
	private boolean cdButtonRedirState = false;
	private boolean fdButtonRedirState = false;
	private boolean hdButtonRedirState = false;

    /** Creates a new instance of videoRedirSession
     *  @param host Hostname or IP address of the remote server */
    public IUSBRedirSession()
    {
    	cdromSession = new CDROMRedir[JViewerApp.getInstance().getM_cdNum()];
    	floppySession = new FloppyRedir[JViewerApp.getInstance().getM_fdNum()];
      	harddiskSession = new HarddiskRedir[JViewerApp.getInstance().getM_hdNum()];
    	cdromRedirStatus = new int[JViewerApp.getInstance().getM_cdNum()];
    	floppyRedirStatus = new int[JViewerApp.getInstance().getM_fdNum()];
    	harddiskRedirStatus = new int[JViewerApp.getInstance().getM_hdNum()];
    	cdROMDeviceStatus = new int[JViewerApp.getInstance().getM_cdNum()];
    	floppyDeviceStatus = new int[JViewerApp.getInstance().getM_fdNum()];
    	hardDiskDeviceStatus = new int[JViewerApp.getInstance().getM_hdNum()];
    	for(int j=0;j<cdromRedirStatus.length;j++) {
    		cdromRedirStatus[j] = DEVICE_REDIR_STATUS_IDLE;
    		cdROMDeviceStatus[j] = DEVICE_FREE;
    	}
    	for(int j=0;j<floppyRedirStatus.length;j++) {
    		floppyRedirStatus[j] = DEVICE_REDIR_STATUS_IDLE;
    		floppyDeviceStatus[j] = DEVICE_FREE;
    	}
    	for(int j=0;j<harddiskRedirStatus.length;j++) {
    		harddiskRedirStatus[j] = DEVICE_REDIR_STATUS_IDLE;
    		hardDiskDeviceStatus[j] = DEVICE_FREE;
    	}
    }
	/**
	* Start CDROM redirection
	* returns true if successful or false on failure.
	*/
	@SuppressWarnings("static-access")
	public boolean StartCDROMRedir(String token,int port,int cddevice_no, boolean bVMUseSSL,String cdromDrive)
	{
		String host = JViewerApp.getInstance().getMainWindow().getServerIP();
		if( host == null ) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_6_IUSBREDIR"),
					LocaleStrings.getString("6_1_IUSBREDIR")+LocaleStrings.getString("6_6_IUSBREDIR"));
			return false;
		}
		if( cdromDrive.length() == 0 ) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_32_IUSBREDIR"),
					LocaleStrings.getString("6_2_IUSBREDIR")+LocaleStrings.getString("6_6_IUSBREDIR"));
			return false;
		}
        try
        {
        	if( cdromSession[cddevice_no] != null ) {
				if( cdromSession[cddevice_no].isRedirActive() ) {
					JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_32_IUSBREDIR"),
							LocaleStrings.getString("6_3_IUSBREDIR"));
					return false;
				}
				cdromSession = null;
				System.gc();
			}

			cdromSession[cddevice_no] = new CDROMRedir( true );
            if( !cdromSession[cddevice_no].startRedirection( host, cdromDrive ,cddevice_no,token,port,bVMUseSSL ) )
            {
            	JViewerApp.getInstance().getMainWindow().generalErrorMessage(
            			LocaleStrings.getString("6_6_IUSBREDIR"), LocaleStrings.getString("6_4_IUSBREDIR"));
            	StopCDROMRedir(cddevice_no, STOP_NORMAL);
            	return false;
            }

        }
        catch( UnsatisfiedLinkError e )
        {
            /* We couldn't load the native CD-ROM library */
            JViewerApp.getInstance().getMainWindow().generalErrorMessage(
            		LocaleStrings.getString("6_6_IUSBREDIR"),LocaleStrings.getString("6_5_IUSBREDIR"));
            StopCDROMRedir(cddevice_no, STOP_NORMAL);
            return false;
        }
        catch( RedirectionException e )
        {
            /* Something funky happened... */
            JViewerApp.getInstance().getMainWindow().generalErrorMessage(
            		LocaleStrings.getString("6_6_IUSBREDIR"), e.getMessage() );
            StopCDROMRedir(cddevice_no, STOP_NORMAL);
            return false;
        }
        cdromRedirStatus[cddevice_no] = DEVICE_REDIR_STATUS_CONNECTED;
		return true;
	}

	/**
	* Stop CDROM redirection
	* returns true if successful or false on failure.
	*/
	public boolean StopCDROMRedir(int cddevice_no, int stopMode)
	{
        if( cdromSession[cddevice_no] != null ) {
            if ( cdromSession[cddevice_no].stopRedirection() == false ) {
	            JViewerApp.getInstance().getMainWindow().generalErrorMessage(
	            		LocaleStrings.getString("6_33_IUSBREDIR"), LocaleStrings.getString("6_7_IUSBREDIR") );
				return false;
			}

            cdromRedirStatus[cddevice_no] = DEVICE_REDIR_STATUS_IDLE;
        	if(stopMode == STOP_ON_EJECT){
            	InfoDialog.showDialog(JViewer.getMainFrame(), LocaleStrings.getString("6_41_IUSBREDIR")+
            			getCDInstanceNumber(cddevice_no)+ LocaleStrings.getString("6_47_IUSBREDIR"),
            			LocaleStrings.getString("6_38_IUSBREDIR"), InfoDialog.INFORMATION_DIALOG);
            }
            else if (stopMode == STOP_ON_TERMINATE) {
            	InfoDialog.showDialog(JViewer.getMainFrame(), LocaleStrings.getString("6_44_IUSBREDIR")+
            			getCDInstanceNumber(cddevice_no)+ LocaleStrings.getString("6_47_IUSBREDIR"),
            			LocaleStrings.getString("6_38_IUSBREDIR"), InfoDialog.INFORMATION_DIALOG);
    		}
        	if(stopMode != STOP_NORMAL){
        		if(JViewerApp.getInstance().getM_mediaDlg() != null && 
        				JViewerApp.getInstance().getM_mediaDlg().isShowing()){
        			vMediaDialog vmDialog = JViewerApp.getInstance().getM_mediaDlg();
        			vmDialog.disposeVMediaDialog();
        		}
        	}
        	cdromSession[cddevice_no] = null;

            /* Manually invoke garbage collector to reclaim memory from
             * the cdromSession.  In particular, we want back the directly
             * allocated memory, as there is an upper limit to how much
             * of that we can have allocated at any given time, and directly
             * allocated memory that hasn't yet been garbage collected counts
             * against that limit. */
            System.gc();
        }
		return true;
	}

	/**
	* Start ISO redirection
	* returns true if successful or false on failure.
	*/
	@SuppressWarnings("static-access")
	public boolean StartISORedir(String token, int port,int cddevice_no, boolean bVMUseSSL,String filename)
	{
		String host = JViewerApp.getInstance().getMainWindow().getServerIP();
		if( host == null ) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(
					LocaleStrings.getString("6_8_IUSBREDIR"), LocaleStrings.getString("6_1_IUSBREDIR")+
					LocaleStrings.getString("6_8_IUSBREDIR"));
			return false;
		}
		if( filename.length() == 0 ) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(
					LocaleStrings.getString("6_32_IUSBREDIR"),LocaleStrings.getString("6_9_IUSBREDIR")+
					LocaleStrings.getString("6_8_IUSBREDIR"));
			return false;
		}
		else if(!filename.toLowerCase().endsWith( ".iso" ) && !filename.toLowerCase().endsWith( ".nrg" )) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(
					LocaleStrings.getString("6_32_IUSBREDIR"),LocaleStrings.getString("6_9_IUSBREDIR"));
			return false;
		}

        try
        {
			if( cdromSession[cddevice_no] != null ) {
				if( cdromSession[cddevice_no].isRedirActive() ) {
					JViewerApp.getInstance().getMainWindow().generalErrorMessage(
							LocaleStrings.getString("6_32_IUSBREDIR"),LocaleStrings.getString("6_3_IUSBREDIR"));
					return false;
				}
				cdromSession[cddevice_no] = null;
				System.gc();
			}
			/* Create CDROM redirection object for ISO9660 image */
			cdromSession[cddevice_no] = new CDROMRedir( false );

			/* start redirection */
			if( !cdromSession[cddevice_no].startRedirection( host, filename ,cddevice_no,token,port,bVMUseSSL ) ) {
				/* Uh-oh, redirect failed */
				JViewerApp.getInstance().getMainWindow().generalErrorMessage( 
						LocaleStrings.getString("6_8_IUSBREDIR"),LocaleStrings.getString("6_11_IUSBREDIR"));
				StopISORedir(cddevice_no, STOP_NORMAL);
				return false;
            }
		}
        catch( UnsatisfiedLinkError e )
        {
            /* We couldn't load the native CD-ROM library */
            JViewerApp.getInstance().getMainWindow().generalErrorMessage( 
					LocaleStrings.getString("6_8_IUSBREDIR"),LocaleStrings.getString("6_12_IUSBREDIR"));
            StopISORedir(cddevice_no, STOP_NORMAL);
            return false;
        }
        catch( RedirectionException e )
        {
            JViewerApp.getInstance().getMainWindow().generalErrorMessage( 
					LocaleStrings.getString("6_8_IUSBREDIR"), e.getMessage() );
            StopISORedir(cddevice_no, STOP_NORMAL);
            return false;
        }
        cdromRedirStatus[cddevice_no] = DEVICE_REDIR_STATUS_CONNECTED;
		return true;
	}

	/**
	* Stop ISO redirection
	* returns true if successful or false on failure.
	*/
	public boolean StopISORedir(int cddevice_no, int stopMode)
	{
		return StopCDROMRedir(cddevice_no, stopMode);
	}

	/**
	* Start Harddisk redirection
	* returns true if successful or false on failure.
	*/
	@SuppressWarnings("static-access")
	public boolean StartHarddiskRedir(String token,int port,int device_no, boolean bVMUseSSL,String hardDrive,byte MediaType )
	{
		String host = JViewerApp.getInstance().getMainWindow().getServerIP();
		if( host == null ) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_13_IUSBREDIR"),
					LocaleStrings.getString("6_1_IUSBREDIR"));
			return false;
		}
		if( hardDrive.length() == 0 ) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_32_IUSBREDIR"),
					LocaleStrings.getString("6_14_IUSBREDIR")+LocaleStrings.getString("6_13_IUSBREDIR"));
			return false;
		}
        try
        {
        	if( harddiskSession[device_no] != null ) {
				if( harddiskSession[device_no].isRedirActive() ) {
					JViewerApp.getInstance().getMainWindow().generalErrorMessage(
							LocaleStrings.getString("6_32_IUSBREDIR"),LocaleStrings.getString("6_15_IUSBREDIR"));
					return false;
				}
				harddiskSession[device_no] = null;
				System.gc();
			}
        	harddiskSession[device_no] = new HarddiskRedir( true );
        	harddiskSession[device_no].setDrive_Type(MediaType);
			/* User selected a drive - let's redirect it */
			if( !harddiskSession[device_no].startRedirection( host, hardDrive ,device_no,token,port,bVMUseSSL ) ) {
				JViewerApp.getInstance().getMainWindow().generalErrorMessage( 
						LocaleStrings.getString("6_13_IUSBREDIR"),LocaleStrings.getString("6_16_IUSBREDIR") );
                StopHarddiskRedir(device_no, STOP_NORMAL);
                return false;
			}
        }
        catch( UnsatisfiedLinkError e )
        {
        	Debug.out.println(e);
            /* We couldn't load the native Floppy library */
            JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_13_IUSBREDIR"),
            		LocaleStrings.getString("6_17_IUSBREDIR") );
            StopHarddiskRedir(device_no, STOP_NORMAL);
            return false;
        }
        catch( RedirectionException e )
        {
            /* Something funky happened... */
            JViewerApp.getInstance().getMainWindow().generalErrorMessage( LocaleStrings.getString("6_13_IUSBREDIR"), e.getMessage() );
            StopHarddiskRedir(device_no, STOP_NORMAL);
            return false;
        }
        harddiskRedirStatus[device_no] = DEVICE_REDIR_STATUS_CONNECTED;
		return true;
	}

	/**
	* Start Floppy redirection
	* returns true if successful or false on failure.
	*/
	@SuppressWarnings("static-access")
	public boolean StartFloppyRedir(String token,int port,int device_no, boolean bVMUseSSL,String floppyDrive )
	{
		String host = JViewerApp.getInstance().getMainWindow().getServerIP();
		if( host == null ) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_18_IUSBREDIR"),
					LocaleStrings.getString("6_1_IUSBREDIR")+LocaleStrings.getString("6_18_IUSBREDIR"));
			return false;
		}
		if( floppyDrive.length() == 0 ) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_32_IUSBREDIR"),
					LocaleStrings.getString("6_2_IUSBREDIR")+LocaleStrings.getString("6_18_IUSBREDIR"));
			return false;
		}
        try
        {
        	if( floppySession[device_no] != null ) {
				if( floppySession[device_no].isRedirActive() ) {
					JViewerApp.getInstance().getMainWindow().generalErrorMessage(
							LocaleStrings.getString("6_32_IUSBREDIR"),LocaleStrings.getString("6_19_IUSBREDIR"));
					return false;
				}
				floppySession[device_no] = null;
				System.gc();
			}
			floppySession[device_no] = new FloppyRedir( true );

			/* User selected a drive - let's redirect it */
			if( !floppySession[device_no].startRedirection( host, floppyDrive ,device_no,token,port,bVMUseSSL ) ) {
				JViewerApp.getInstance().getMainWindow().generalErrorMessage( LocaleStrings.getString("6_18_IUSBREDIR"),
						LocaleStrings.getString("6_20_IUSBREDIR"));
                StopFloppyRedir(device_no, STOP_NORMAL);
                return false;
			}
        }
        catch( UnsatisfiedLinkError e )
        {
            /* We couldn't load the native Floppy library */
            JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_18_IUSBREDIR"),
            		LocaleStrings.getString("6_21_IUSBREDIR") );
            StopFloppyRedir(device_no, STOP_NORMAL);
            return false;
        }
        catch( RedirectionException e )
        {
            /* Something funky happened... */
            JViewerApp.getInstance().getMainWindow().generalErrorMessage( 
            		LocaleStrings.getString("6_18_IUSBREDIR"), e.getMessage() );
            StopFloppyRedir(device_no, STOP_NORMAL);
            return false;
        }
        floppyRedirStatus[device_no] = DEVICE_REDIR_STATUS_CONNECTED;
		return true;
	}


	/**
	* Stop Harddisk/USB redirection
	* returns true if successful or false on failure.
	*/
	public boolean StopHarddiskRedir(int device_no, int stopMode)
	{
        if( harddiskSession[device_no] != null ) {
            if ( harddiskSession[device_no].stopRedirection() == false ) {
	            JViewerApp.getInstance().getMainWindow().generalErrorMessage( LocaleStrings.getString("6_34_IUSBREDIR"),
	            		 LocaleStrings.getString("6_36_IUSBREDIR") );
				return false;
			}

            harddiskRedirStatus[device_no] = DEVICE_REDIR_STATUS_IDLE;
            if(stopMode == STOP_ON_EJECT){
            	InfoDialog.showDialog(JViewer.getMainFrame(), LocaleStrings.getString("6_43_IUSBREDIR")+
            			getHDInstanceNumber(device_no)+ LocaleStrings.getString("6_47_IUSBREDIR"),
            			LocaleStrings.getString("6_40_IUSBREDIR"), InfoDialog.INFORMATION_DIALOG);
            }
            else if (stopMode == STOP_ON_TERMINATE) {
            	InfoDialog.showDialog(JViewer.getMainFrame(), LocaleStrings.getString("6_46_IUSBREDIR")+
            			getHDInstanceNumber(device_no)+ LocaleStrings.getString("6_47_IUSBREDIR"),
            			LocaleStrings.getString("6_40_IUSBREDIR"), InfoDialog.INFORMATION_DIALOG);
    		}

            if(stopMode != STOP_NORMAL){
            	if(JViewerApp.getInstance().getM_mediaDlg() != null && 
            			JViewerApp.getInstance().getM_mediaDlg().isShowing()){
            		vMediaDialog vmDialog = JViewerApp.getInstance().getM_mediaDlg();
            		vmDialog.disposeVMediaDialog();
            	}
            }
            harddiskSession[device_no] = null;

            /* Manually invoke garbage collector to reclaim memory from
             * the cdromSession.  In particular, we want back the directly
             * allocated memory, as there is an upper limit to how much
             * of that we can have allocated at any given time, and directly
             * allocated memory that hasn't yet been garbage collected counts
             * against that limit. */
            System.gc();
        }
		return true;
	}

	/**
	* Stop Floppy redirection
	* returns true if successful or false on failure.
	*/
	public boolean StopFloppyRedir(int device_no, int stopMode)
	{
        if( floppySession[device_no] != null ) {
            if ( floppySession[device_no].stopRedirection() == false ) {
	            JViewerApp.getInstance().getMainWindow().generalErrorMessage(  LocaleStrings.getString("6_35_IUSBREDIR"),
	            		 LocaleStrings.getString("6_22_IUSBREDIR") );
				return false;
			}

            floppyRedirStatus[device_no] = DEVICE_REDIR_STATUS_IDLE;
            if(stopMode == STOP_ON_EJECT){
            	InfoDialog.showDialog(JViewer.getMainFrame(), LocaleStrings.getString("6_42_IUSBREDIR")+
            			getFDInstanceNumber(device_no)+ LocaleStrings.getString("6_47_IUSBREDIR"),
            			LocaleStrings.getString("6_39_IUSBREDIR"), InfoDialog.INFORMATION_DIALOG);
            }
            else if (stopMode == STOP_ON_TERMINATE) {
            	InfoDialog.showDialog(JViewer.getMainFrame(), LocaleStrings.getString("6_45_IUSBREDIR")+
            			getFDInstanceNumber(device_no)+ LocaleStrings.getString("6_47_IUSBREDIR"),
            			LocaleStrings.getString("6_39_IUSBREDIR"), InfoDialog.INFORMATION_DIALOG);
    		}

            if(stopMode != STOP_NORMAL){
            	if(JViewerApp.getInstance().getM_mediaDlg() != null && 
            			JViewerApp.getInstance().getM_mediaDlg().isShowing()){
            		vMediaDialog vmDialog = JViewerApp.getInstance().getM_mediaDlg();
            		vmDialog.disposeVMediaDialog();
            	}
            }
            floppySession[device_no] = null;

            /* Manually invoke garbage collector to reclaim memory from
             * the cdromSession.  In particular, we want back the directly
             * allocated memory, as there is an upper limit to how much
             * of that we can have allocated at any given time, and directly
             * allocated memory that hasn't yet been garbage collected counts
             * against that limit. */
            System.gc();
        }
		return true;
	}



	/**
	* Start Floppy image redirection
	* returns true if successful or false on failure.
	*/
	@SuppressWarnings("static-access")
	public boolean StartFloppyImageRedir(String token,int port,int device_no, boolean bVMUseSSL,String filename )
	{
		String host = JViewerApp.getInstance().getMainWindow().getServerIP();
		if( host == null ) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_23_IUSBREDIR"),
					LocaleStrings.getString("6_1_IUSBREDIR")+LocaleStrings.getString("6_23_IUSBREDIR"));
			return false;
		}

		if( filename.length() == 0 ) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_32_IUSBREDIR"),
					LocaleStrings.getString("6_9_IUSBREDIR")+LocaleStrings.getString("6_23_IUSBREDIR"));
			return false;
		}else if(!filename.toLowerCase().endsWith( ".img" ) && !filename.toLowerCase().endsWith( ".ima" )) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_32_IUSBREDIR"),
					LocaleStrings.getString("6_9_IUSBREDIR")+LocaleStrings.getString("6_23_IUSBREDIR"));
			return false;
		}
		
        try
        {
        	if( floppySession[device_no] != null ) {
				if( floppySession[device_no].isRedirActive() ) {
					JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_32_IUSBREDIR"),
							LocaleStrings.getString("6_24_IUSBREDIR"));
					return false;
				}
				floppySession[device_no] = null;
				System.gc();
			}
			/* Create Floppy redirection object for Floppy image */
			floppySession[device_no] = new FloppyRedir( false );

			/* start redirection */
			if( !floppySession[device_no].startRedirection( host, filename,device_no,token,port,bVMUseSSL  ) ) {
				/* Uh-oh, redirect failed */
				JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_23_IUSBREDIR"),
						LocaleStrings.getString("6_25_IUSBREDIR") );
				StopFloppyImageRedir(device_no, STOP_NORMAL);
				return false;
            }
		}
        catch( UnsatisfiedLinkError e )
        {
            /* We couldn't load the native Floppy library */
            JViewerApp.getInstance().getMainWindow().generalErrorMessage( LocaleStrings.getString("6_23_IUSBREDIR"),
            		LocaleStrings.getString("6_26_IUSBREDIR") );
            StopFloppyImageRedir(device_no, STOP_NORMAL);
            return false;
        }
        catch( RedirectionException e )
        {
            JViewerApp.getInstance().getMainWindow().generalErrorMessage( LocaleStrings.getString("6_23_IUSBREDIR"),
            		e.getMessage() );
            StopFloppyImageRedir(device_no, STOP_NORMAL);
            return false;
        }
        floppyRedirStatus[device_no] = DEVICE_REDIR_STATUS_CONNECTED;
		return true;
	}

	/**
	* Start Hardisk/Floppy image redirection
	* returns true if successful or false on failure.
	*/
	@SuppressWarnings("static-access")
	public boolean StartharddiskImageRedir(String token,int port,int device_no, boolean bVMUseSSL,String filename,byte mediatype)
	{
		String host = JViewerApp.getInstance().getMainWindow().getServerIP();
		if( host == null ) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_27_IUSBREDIR"),
					LocaleStrings.getString("6_1_IUSBREDIR"));
			return false;
		}

		if( filename.length() == 0 ) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_32_IUSBREDIR"),
					LocaleStrings.getString("6_9_IUSBREDIR")+LocaleStrings.getString("6_27_IUSBREDIR"));
			return false;
		}
		else if(!filename.endsWith( ".img" ) && !filename.endsWith( ".IMG" )) {
			JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_32_IUSBREDIR"),
					LocaleStrings.getString("6_9_IUSBREDIR")+LocaleStrings.getString("6_27_IUSBREDIR"));
			return false;
		}
        try
        {
        	if( harddiskSession[device_no] != null ) {
				if( harddiskSession[device_no].isRedirActive() ) {
					JViewerApp.getInstance().getMainWindow().generalErrorMessage(
							LocaleStrings.getString("6_32_IUSBREDIR"), LocaleStrings.getString("6_28_IUSBREDIR"));
					return false;
				}
				harddiskSession = null;
				System.gc();
			}
			/* Create Floppy redirection object for Floppy image */
        	harddiskSession[device_no] = new HarddiskRedir( false );
        	harddiskSession[device_no].setDrive_Type(mediatype);
			/* start redirection */
			if( !harddiskSession[device_no].startRedirection( host, filename,device_no,token,port,bVMUseSSL  ) ) {
				/* Uh-oh, redirect failed */
				JViewerApp.getInstance().getMainWindow().generalErrorMessage(
						LocaleStrings.getString("6_27_IUSBREDIR"),LocaleStrings.getString("6_29_IUSBREDIR"));
				StopHarddiskImageRedir(device_no, STOP_NORMAL);
				return false;
            }
		}
        catch( UnsatisfiedLinkError e )
        {
            /* We couldn't load the native Floppy library */
            JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_27_IUSBREDIR"),
            		LocaleStrings.getString("6_30_IUSBREDIR") );
            StopHarddiskImageRedir(device_no, STOP_NORMAL);
            return false;
        }
        catch( RedirectionException e )
        {
            JViewerApp.getInstance().getMainWindow().generalErrorMessage(LocaleStrings.getString("6_27_IUSBREDIR"), 
            		e.getMessage() );
            StopHarddiskImageRedir(device_no, STOP_NORMAL);
            return false;
        }
        harddiskRedirStatus[device_no] = DEVICE_REDIR_STATUS_CONNECTED;
		return true;
	}

	/**
	* Stop Floppy image redirection
	* returns true if successful or false on failure.
	*/
	public boolean StopFloppyImageRedir(int device_no, int stopMode)
	{
		return StopFloppyRedir(device_no, stopMode);
	}

	/**
	* Stop Floppy image redirection
	* returns true if successful or false on failure.
	*/
	public boolean StopHarddiskImageRedir(int device_no, int stopMode)
	{
		return StopHarddiskRedir(device_no, stopMode);
	}

    /** Query the native cdrom library and get its version
     *  @return The version number if the native library is present
     *  @return "Not present" if the native library is not present */
    public String getLIBCDROMVersion(int cddevice_no)
    {
        String version;

        try
        {
            if( cdromSession[cddevice_no] == null )
            {
                cdromSession[cddevice_no] = new CDROMRedir( false );
                version = cdromSession[cddevice_no].getLIBCDROMVersion();
                cdromSession[cddevice_no] = null;

                /* Manually invoke garbage collector to reclaim memory from
                * the cdromSession.  In particular, we want back the directly
                * allocated memory, as there is an upper limit to how much
                * of that we can have allocated at any given time, and directly
                * allocated memory that hasn't yet been garbage collected counts
                * against that limit. */
                System.gc();
            }
            else
                version = cdromSession[cddevice_no].getLIBCDROMVersion();
        }
        catch( UnsatisfiedLinkError e )
        {
            version = LocaleStrings.getString("6_31_IUSBREDIR");
        }

        return( version );
    }

    /**
     * File filter for ISO images and directories
     */
    class ISOImageFilter extends javax.swing.filechooser.FileFilter
    {
        public boolean accept(File file )
        {
            String filename = file.getName();
            return filename.toLowerCase().endsWith( ".iso" ) || file.isDirectory();
        }

        public String getDescription()
        {
            return "*.iso; *.ISO";
        }
    }
    
    /**
     * File filter for NRG images and directories
     */
    class NRGImageFilter extends javax.swing.filechooser.FileFilter
    {
        public boolean accept(File file )
        {
            String filename = file.getName();
            return filename.toLowerCase().endsWith( ".nrg" ) || file.isDirectory();
        }

        public String getDescription()
        {
            return "*.nrg; *.NRG";
        }
    }
    
    /**
     * File filter for ISO & NRG images and directories
     */
    class CDImageFilter extends FileFilter{

		public boolean accept(File file) {
			boolean accept = false;
			if(new ISOImageFilter().accept(file) || new NRGImageFilter().accept(file))
				accept = true;
			return accept;
		}

		public String getDescription() {
			return "ISO(*.iso, *.ISO), NRG(*.nrg, *.NRG)";
		}
    	
    }

    /** Prompt the user to select a CD image from their file system
     * @param string
     *  @return A <code>String</code> containing the complete file path
     *  of the selected CD image
     *  @return <code>null</code> if the user hit the cancel button */
    public String cdImageSelector(String dirpath)
    {
        JVFrame frame = JViewerApp.getInstance().getMainWindow();
        JFileChooser chooser = new JFileChooser(dirpath);
        chooser.setFileFilter( new CDImageFilter() );

        if( chooser.showOpenDialog( frame ) == JFileChooser.APPROVE_OPTION )
            return( chooser.getSelectedFile().getAbsolutePath() );
        else
            return( null );
    }

    /** Query the native floppy library and get its version
     *  @return The version number if the native library is present
     *  @return "Not present" if the native library is not present */
    public String getLIBFLOPPYVersion(int device_no)
    {
        String version;
        try
        {
            if( floppySession[device_no] == null )
            {
                floppySession[device_no] = new FloppyRedir( false );
                version = floppySession[device_no].getLIBFLOPPYVersion();
                floppySession = null;
                /* Manually invoke garbage collector to reclaim memory from
                * the floppySession.  In particular, we want back the directly
                * allocated memory, as there is an upper limit to how much
                * of that we can have allocated at any given time, and directly
                * allocated memory that hasn't yet been garbage collected counts
                * against that limit. */
                System.gc();
            }
            else
                version = floppySession[device_no].getLIBFLOPPYVersion();
        }
        catch( UnsatisfiedLinkError e )
        {
            version = LocaleStrings.getString("6_31_IUSBREDIR");
        }

        return( version );
    }


    public boolean isCDROMPhysicalDrive(int cddevice_no)
	{
		if(cdromSession[cddevice_no] != null)
		{
			if(cdromSession[cddevice_no].isPhysicalDevice() == true)
				return true;
		}
		return false;
	}

    public boolean isHarddiskPhysicalDrive(int device_no)
	{
		if(harddiskSession[device_no] != null)
		{
			if(harddiskSession[device_no].isPhysicalDevice() == true)
				return true;
		}
		return false;
	}

    public boolean isFloppyPhysicalDrive(int device_no)
	{
		if(floppySession[device_no] != null)
		{
			if(floppySession[device_no].isPhysicalDevice() == true)
				return true;
		}
		return false;
	}
  /***
   *  File filter for file images (stuff ending with .img) and directories
   *
   */
    class FloppyImageFilter extends javax.swing.filechooser.FileFilter
    {
        public boolean accept( java.io.File f )
        {
            String filename = f.getName();
            return (filename.toLowerCase().endsWith( ".img" ) ||
            		filename.toLowerCase().endsWith( ".ima" ) ||
            		f.isDirectory());
        }

        public String getDescription()
        {
            return "IMG(*.img, *.IMG), IMA(*.ima, *.IMA)";
        }
    }

    /** Prompt the user to select a floppy image from their filesystem
     * @param string
     *  @return A <code>String</code> containing the completely file path
     *  of the selected image
     *  @return <code>null</code> if the user hit the cancel button */
    public String floppyImageSelector(String dirpath)
    {
        JVFrame mainFrame = JViewerApp.getInstance().getMainWindow();
        JFileChooser chooser = new JFileChooser(dirpath);
        chooser.setFileFilter( new FloppyImageFilter() );
        if( chooser.showOpenDialog( mainFrame ) == JFileChooser.APPROVE_OPTION )
            return( chooser.getSelectedFile().getAbsolutePath() );
        else
            return( null );
    }

    /**
     * Stop the CDROMRedirection abnormally
     *
     */
    public void stopCDROMAbnormal(int device_no)
	{
		try{
			cdromSession[device_no] = null;

			/* Manually invoke garbage collector to reclaim memory from
			 * the cdromSession.  In particular, we want back the directly
			 * allocated memory, as there is an upper limit to how much
			 * of that we can have allocated at any given time, and directly
			 * allocated memory that hasn't yet been garbage collected counts
			 * against that limit. */
			System.gc();
			cdromRedirStatus[device_no] = DEVICE_REDIR_STATUS_IDLE;
		}catch(ArrayIndexOutOfBoundsException aie){
			System.gc();
			Debug.out.println(aie);
		}
	}

    /**
     * Stop the floppy Redirection abnormally
     *
     */
	public void stopFloppyAbnormal(int device_no)
	{
		try {
			floppySession[device_no] = null;
			/* Manually invoke garbage collector to reclaim memory from
			 * the cdromSession.  In particular, we want back the directly
			 * allocated memory, as there is an upper limit to how much
			 * of that we can have allocated at any given time, and directly
			 * allocated memory that hasn't yet been garbage collected counts
			 * against that limit. */
			System.gc();
			floppyRedirStatus[device_no] = DEVICE_REDIR_STATUS_IDLE;
		} catch (ArrayIndexOutOfBoundsException aie) {
			System.gc();
			Debug.out.println(aie);
		}
	}

	/**
     * Stop the floppy Redirection abnormally
     *
     */
	public void stopHarddiskAbnormal(int device_no)
	{
		try {
			harddiskSession[device_no] = null;

			/* Manually invoke garbage collector to reclaim memory from
			 * the cdromSession.  In particular, we want back the directly
			 * allocated memory, as there is an upper limit to how much
			 * of that we can have allocated at any given time, and directly
			 * allocated memory that hasn't yet been garbage collected counts
			 * against that limit. */
			System.gc();
			harddiskRedirStatus[device_no] = DEVICE_REDIR_STATUS_IDLE;
		} catch (ArrayIndexOutOfBoundsException aie) {
			System.gc();
			Debug.out.println(aie);
		}
	}

	/**
	 * Return the CDROM Redirection status
	 * @return
	 */
	public int getCDROMRedirStatus(int device_no)
	{
		int ret;
		try{
			ret =  cdromRedirStatus[device_no];
		}catch (ArrayIndexOutOfBoundsException aie) {
			ret = DEVICE_REDIR_STATUS_IDLE;
		}
		return ret;
	}

	/**
	 * Return the Harddisk Redirection status
	 * @return
	 */
	public int getHarddiskRedirStatus(int device_no)
	{
		int ret;
		try{
			ret =  harddiskRedirStatus[device_no];
		}catch (ArrayIndexOutOfBoundsException aie) {
			ret = DEVICE_REDIR_STATUS_IDLE;
		}
		return ret;
	}

	/**
	 * Return the Floppy Redirection status
	 * @return
	 */
	public int getFloppyRedirStatus(int device_no)
	{
		int ret;
		try{
			ret =  floppyRedirStatus[device_no];
		}catch (ArrayIndexOutOfBoundsException aie) {
			ret = DEVICE_REDIR_STATUS_IDLE;
		}
		return ret;
	}

	/**
	 * Return the CDROM source drive name
	 * @return
	 */
	public String getCDROMSource(int cddevice_no)
	{
		if ( cdromSession[cddevice_no] != null )
			return cdromSession[cddevice_no].getSourceDrive();

		return new String("");
	}

	/**
	 * Return the Floppy source drive name
	 * @return
	 */
	public String getFloppySource(int device_no)
	{
		if ( floppySession[device_no] != null )
			return floppySession[device_no].getSourceDrive();

		return new String("");
	}

	/**
	 * Return the Floppy source drive name
	 * @return
	 */
	public String getharddiskSource(int device_no)
	{
		if ( harddiskSession[device_no] != null )
			return harddiskSession[device_no].getSourceDrive();

		return new String("");
	}

	/**
	 * Return the data transfer rate of the CDROM Redirection
	 * @return
	 */
	public int getCDROMReadBytes(int cddevice_no)
	{
		if( cdromSession[cddevice_no] != null )
			return cdromSession[cddevice_no].getBytesRedirected();

		return 0;
	}

	/**
	 * Return the data transfer rate of the Floppy Redirection
	 * @return
	 */
	public int getFloppyReadBytes(int device_no)
	{
		if( floppySession[device_no] != null )
			return floppySession[device_no].getBytesRedirected();

		return 0;
	}

	/**
	 * Return the data transfer rate of the Floppy Redirection
	 * @return
	 */
	public int getHarddiskReadBytes(int device_no)
	{
		if( harddiskSession[device_no] != null )
			return harddiskSession[device_no].getBytesRedirected();

		return 0;
	}

	public CDROMRedir getCdromSession(int cddevice_no) {
		CDROMRedir cdROMSession = null;
		if(cddevice_no < cdromSession.length)
			cdROMSession = cdromSession[cddevice_no];
		return cdROMSession;
	}

	public void setCdromSession(CDROMRedir cdromSession,int cddevice_no) {
		this.cdromSession[cddevice_no] = cdromSession;
	}

	public FloppyRedir getFloppySession(int device_no) {
		FloppyRedir floppyRedir = null;
		if(device_no < floppySession.length)
			floppyRedir = floppySession[device_no];
		return floppyRedir;
	}

	public void setFloppySession(FloppyRedir floppySession,int device_no) {
		this.floppySession[device_no] = floppySession;
	}

	public HarddiskRedir getHarddiskSession(int device_no) {
		HarddiskRedir hardDiskRedir = null;
		if(device_no < harddiskSession.length)
			hardDiskRedir = harddiskSession[device_no];
		return hardDiskRedir;
	}

	public void setHarddiskSession(HarddiskRedir harddiskSession,int device_no) {
		this.harddiskSession[device_no] = harddiskSession;
	}
	
	public int getCDInstanceNumber(int devNum){
		return cdromSession[devNum].getCdInstanceNum();
	}
	public int getFDInstanceNumber(int devNum){
		return floppySession[devNum].getFdInstanceNum();
	}
	public int getHDInstanceNumber(int devNum){
		return harddiskSession[devNum].getHdInstanceNum();
	}
	public void updateCDToolbarButtonStatus(boolean status){
		if(status != cdButtonRedirState){
			if(status){
				URL imageCDR = com.ami.kvm.jviewer.JViewer.class.getResource("res/CDR.png");
    			JViewerApp.getInstance().getM_wndFrame().toolbar.cdBtn.setIcon(new ImageIcon(imageCDR));
    			JViewerApp.getInstance().getM_wndFrame().toolbar.cdBtn.setToolTipText(LocaleStrings.getString("G_21_VMD"));
    			cdButtonRedirState = true;
			}
			else{
				URL imageCD = com.ami.kvm.jviewer.JViewer.class.getResource("res/CD.png");
    			JViewerApp.getInstance().getM_wndFrame().toolbar.cdBtn.setIcon(new ImageIcon(imageCD));
    			JViewerApp.getInstance().getM_wndFrame().toolbar.cdBtn.setToolTipText(LocaleStrings.getString("G_20_VMD"));
    			cdButtonRedirState = false;
			}
		}
	}
	
	public void updateFDToolbarButtonStatus(boolean status){
		if(status != fdButtonRedirState){
			if(status){
				URL imageFDR = com.ami.kvm.jviewer.JViewer.class.getResource("res/FloppyR.png");
    			JViewerApp.getInstance().getM_wndFrame().toolbar.floppyBtn.setIcon(new ImageIcon(imageFDR));
    			JViewerApp.getInstance().getM_wndFrame().toolbar.floppyBtn.setToolTipText(LocaleStrings.getString("G_23_VMD"));
    			fdButtonRedirState = true;
			}
			else{
				URL imageFD = com.ami.kvm.jviewer.JViewer.class.getResource("res/Floppy.png");
    			JViewerApp.getInstance().getM_wndFrame().toolbar.floppyBtn.setIcon(new ImageIcon(imageFD));
    			JViewerApp.getInstance().getM_wndFrame().toolbar.floppyBtn.setToolTipText(LocaleStrings.getString("G_22_VMD"));
    			fdButtonRedirState = false;
			}
		}
	}
	
	public void updateHDToolbarButtonStatus(boolean status){
		if(status != hdButtonRedirState){
			if(status){
				URL imageHDR = com.ami.kvm.jviewer.JViewer.class.getResource("res/HDR.png");
    			JViewerApp.getInstance().getM_wndFrame().toolbar.hardddiskBtn.setIcon(new ImageIcon(imageHDR));
    			JViewerApp.getInstance().getM_wndFrame().toolbar.hardddiskBtn.setToolTipText(LocaleStrings.getString("G_25_VMD"));
    			hdButtonRedirState = true;
			}
			else{
				URL imageHD = com.ami.kvm.jviewer.JViewer.class.getResource("res/HD.png");
    			JViewerApp.getInstance().getM_wndFrame().toolbar.hardddiskBtn.setIcon(new ImageIcon(imageHD));
    			JViewerApp.getInstance().getM_wndFrame().toolbar.hardddiskBtn.setToolTipText(LocaleStrings.getString("G_24_VMD"));
    			hdButtonRedirState = false;
			}
		}
	}
	/**
	 * Returns the status whether the particular CD device is used or free.
	 * @param instanceNum - The device instance for which the status is to be checked. 
	 * @return IUSBRedirSession.DEVICE_USED - if the device is being used.
	 * 			IUSBRedirSession.DEVICE_FREE - if the device is free.
	 */
	public int getCDROMDeviceStatus(int instanceNum) {
		return cdROMDeviceStatus[instanceNum];
	}
	/**
	 * Sets the status whether the particular CD device is used or free.
	 * @param instanceNum - The device instance for which the status is to be set.
	 * @param deviceStatus - IUSBRedirSession.DEVICE_USED - if the device is being used.
	 * 						 IUSBRedirSession.DEVICE_FREE - if the device is free.
	 */
	public void setCDROMDeviceStatus(int instanceNum, int deviceStatus) {
		this.cdROMDeviceStatus[instanceNum] = deviceStatus;
	}
	/**
	 * Returns the status whether the particular floppy device is used or free.
	 * @param instanceNum - The device instance for which the status is to be checked. 
	 * @return IUSBRedirSession.DEVICE_USED - if the device is being used.
	 * 			IUSBRedirSession.DEVICE_FREE - if the device is free.
	 */
	public int getFloppyDeviceStatus(int instanceNum) {
		return floppyDeviceStatus[instanceNum];
	}
	/**
	 * Sets the status whether the particular floppy device is used or free.
	 * @param instanceNum - The device instance for which the status is to be set.
	 * @param deviceStatus - IUSBRedirSession.DEVICE_USED - if the device is being used.
	 * 						 IUSBRedirSession.DEVICE_FREE - if the device is free.
	 */
	public void setFloppyDeviceStatus(int instanceNum, int deviceStatus) {
		this.floppyDeviceStatus[instanceNum] = deviceStatus;
	}
	/**
	 * Returns the status whether the particular hard disk device is used or free.
	 * @param instanceNum - The device instance for which the status is to be checked. 
	 * @return IUSBRedirSession.DEVICE_USED - if the device is being used.
	 * 			IUSBRedirSession.DEVICE_FREE - if the device is free.
	 */
	public int getHardDiskDeviceStatus(int instanceNum) {
		return hardDiskDeviceStatus[instanceNum];
	}
	/**
	 * Sets the status whether the particular hard disk device is used or free.
	 * @param instanceNum - The device instance for which the status is to be set.
	 * @param deviceStatus - IUSBRedirSession.DEVICE_USED - if the device is being used.
	 * 						 IUSBRedirSession.DEVICE_FREE - if the device is free.
	 */
	public void setHardDiskDeviceStatus(int instanceNum, int deviceStatus) {
		this.hardDiskDeviceStatus[instanceNum] = deviceStatus;
	}
}
