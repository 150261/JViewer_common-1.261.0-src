package com.ami.iusb;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.gui.JViewerApp;

public class vFlpyMonitorThread extends Thread
{
	private FloppyRedir floppyRedir = null;
	private String[] floppyList = null;
	private String floppySource = null;
	private boolean floppyThreadStatus = false;
	private boolean isMediaMatched = false;
	private int fddevice_no = 0;
	
	public vFlpyMonitorThread(int device_no) {
		fddevice_no = device_no;
	}

	/**
	 * Function used to start monitoring thread
	 *
	 */
	public void startFloppyMonitor()
	{
		floppyThreadStatus = true;
		this.start();
	}

	/**
	 * Function used to stop monitoring thread
	 *
	 */
	public void startHarddiskMonitor()
	{
		floppyThreadStatus = false;
	}

	/** Main execution loop for monitoring removal of physical drive
     *  when the media redirection is active. This thread will detect
     *  removal of physical drive and stop the media redirection */
	public void run()
	{
		floppyRedir = new FloppyRedir(true);
		floppySource = JViewerApp.getInstance().getM_IUSBSession().getFloppySource(fddevice_no);

		while (floppyThreadStatus)
		{
			try
			{
				// If media redirection is not running, come out of this while loop
				if(!JViewerApp.getInstance().IsFloppyRedirRunning(fddevice_no))
					break;
				
				floppyList = floppyRedir.getFloppyList();
				
				if(floppyList == null) {
					if(JViewerApp.getInstance().m_mediaDlg != null)
						JViewerApp.getInstance().m_mediaDlg.setVisible(false);
					JViewerApp.getInstance().getM_IUSBSession().StopFloppyRedir(fddevice_no, IUSBRedirSession.STOP_NORMAL);
				}
				else {
					for(int i=0; i < floppyList.length; i++) 
					{
						if(floppySource.equals(floppyList[i])) {
							isMediaMatched = true;
							break;
						}
					}
					/* Incase if multiple Floppy/USB media is connected, this
					 * condition will be used to track the removal of redirected media
					 */
					if(!isMediaMatched)	{
						if(JViewerApp.getInstance().m_mediaDlg != null)
							JViewerApp.getInstance().m_mediaDlg.setVisible(false);
						JViewerApp.getInstance().getM_IUSBSession().StopFloppyRedir(fddevice_no, IUSBRedirSession.STOP_NORMAL);
					}
					else {
						isMediaMatched = false;
					}
				}
				/* Thread sleep to make CPU happy */
				sleep(1000);
			}
			catch (InterruptedException e)
			{
				if(JViewerApp.getInstance().m_mediaDlg != null)
					JViewerApp.getInstance().m_mediaDlg.setVisible(false);
				JViewerApp.getInstance().getM_IUSBSession().stopFloppyAbnormal(fddevice_no);
				Debug.out.println(e);
			}
		}
		return;
	}
}
