package com.ami.iusb;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.gui.JViewerApp;

public class vCDMonitorThread extends Thread
{
	private CDROMRedir cdromRedir = null;
	private String[] cdromList = null;
	private String cdromSource = null;
	private boolean cdromThreadStatus = false;
	private boolean isMediaMatched = false;
	private int cddevice_no = 0;
	
	public vCDMonitorThread(int cddevice_no) {
		this.cddevice_no = cddevice_no;
	}

	/**
	 * Function used to start monitoring thread
	 *
	 */
	public void startCDROMMonitor()
	{
		cdromThreadStatus = true;
		this.start();
	}

	/**
	 * Function used to stop monitoring thread
	 *
	 */
	public void stopCDROMMonitor()
	{
		cdromThreadStatus = false;
	}

	/** Main execution loop for monitoring removal of physical drive
     *  when the media redirection is active. This thread will detect
     *  removal of physical drive and stop the media redirection */
	public void run()
	{
		cdromRedir = new CDROMRedir(true);
		cdromSource = JViewerApp.getInstance().getM_IUSBSession().getCDROMSource(cddevice_no);
		cdromThreadStatus = true;

		while (cdromThreadStatus)
		{
			try
			{
				// If cdromResirection is not running, Come out this while loop.
				if(!JViewerApp.getInstance().IsCDROMRedirRunning(cddevice_no))
					break;
				
				cdromList = cdromRedir.getCDROMList();
				
				if (cdromList == null) {
					if(JViewerApp.getInstance().m_mediaDlg != null)
						JViewerApp.getInstance().m_mediaDlg.setVisible(false);
					JViewerApp.getInstance().getM_IUSBSession().StopCDROMRedir(cddevice_no, IUSBRedirSession.STOP_NORMAL);						
				}
				else {
					for(int i=0; i < cdromList.length; i++) {
						if(cdromSource.equals(cdromList[i])) {
							isMediaMatched = true;
							break;
						}
					}
					/* Incase if multiple CDROM media is connected, this
					 * condition will be used to track the removal of redirected media
					 */
					if(!isMediaMatched) {
						Debug.out.println("REDIRECTED CDROM MEDIUM REMOVAL HAS BEEN DETECTED, STOPPING REDIRECTION TO PREVENT FURTHER DAMAGE");
						if(JViewerApp.getInstance().m_mediaDlg != null)
							JViewerApp.getInstance().m_mediaDlg.setVisible(false);
						JViewerApp.getInstance().getM_IUSBSession().StopCDROMRedir(cddevice_no, IUSBRedirSession.STOP_NORMAL);
					}
					else {
						isMediaMatched = false;
					}
				}
				/* Thread sleep to make CPU happy */
				sleep(1000);
			}
			catch (RedirectionException e)
			{
				if(JViewerApp.getInstance().m_mediaDlg != null)
					JViewerApp.getInstance().m_mediaDlg.setVisible(false);
				JViewerApp.getInstance().getM_IUSBSession().stopCDROMAbnormal(cddevice_no);
				Debug.out.println(e);
			}
			catch (InterruptedException e)
			{
				if(JViewerApp.getInstance().m_mediaDlg != null)
					JViewerApp.getInstance().m_mediaDlg.setVisible(false);
				JViewerApp.getInstance().getM_IUSBSession().stopCDROMAbnormal(cddevice_no);
				Debug.out.println(e);
			}
		}
		return;
	}
}