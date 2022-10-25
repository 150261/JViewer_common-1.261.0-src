package com.ami.iusb;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.gui.JViewerApp;

public class vHarddiskMonitorThread extends Thread
{
	private HarddiskRedir harddiskRedir = null;
	private String[] harddiskList = null;
	private String[] harddiskFixedList = null;
	private String[] harddiskUSBList = null;
	private String harddiskSource = null;
	private boolean harddiskThreadStatus = false;
	private boolean isMediaMatched = false;
	private int hddevice_no = 0;

	public vHarddiskMonitorThread(int device_no) {
		hddevice_no = device_no;
	}

	/**
	 * Function used to start monitoring thread
	 *
	 */
	public void startharddiskMonitor()
	{
		harddiskThreadStatus = true;
		this.start();
	}

	/**
	 * Function used to stop monitoring thread
	 *
	 */
	public void stopFloppyMonitor()
	{
		harddiskThreadStatus = false;
	}

	/** Main execution loop for monitoring removal of physical drive
     *  when the media redirection is active. This thread will detect
     *  removal of physical drive and stop the media redirection */
	public void run()
	{
		harddiskRedir = new HarddiskRedir(true);
		harddiskSource = JViewerApp.getInstance().getM_IUSBSession().getharddiskSource(hddevice_no);
		String[] harddiskSourceSplit  = harddiskSource.split("-");
		harddiskSource = harddiskSourceSplit[0].trim();

		while (harddiskThreadStatus)
		{
			try
			{
				harddiskUSBList = harddiskRedir.getUSBHDDList();
				harddiskFixedList = harddiskRedir.getHarddiskFixedList();
				int devicelength = 0;
				int usbdevicelength=0;
				int fixeddevicelength = 0;
				
				if(harddiskUSBList != null)
					usbdevicelength = harddiskUSBList.length;
				
				if(harddiskFixedList != null)
					fixeddevicelength  = harddiskFixedList.length;
				
				devicelength = usbdevicelength + fixeddevicelength;
				harddiskList = new String[devicelength];

				for(int k=0;k<usbdevicelength;k++)
				{
					String[] harddisksplit = harddiskUSBList[k].split("-");
					if(harddisksplit != null)
						harddiskList[k] = harddisksplit[0];
				}
				for(int k=0;k<fixeddevicelength;k++)
				{
					String[] harddisksplit = harddiskFixedList[k].split("-");
					if(harddisksplit != null)
						harddiskList[k+usbdevicelength] = harddisksplit[0];
				}
				
				if (!JViewerApp.getInstance().IsHarddiskRedirRunning(hddevice_no))
					break;

				if(harddiskList == null) {
					if(JViewerApp.getInstance().m_mediaDlg != null)
						JViewerApp.getInstance().m_mediaDlg.setVisible(false);
					JViewerApp.getInstance().getM_IUSBSession().StopHarddiskRedir(hddevice_no, IUSBRedirSession.STOP_NORMAL);
				}
				else {
					for(int i=0; i < harddiskList.length; i++)
					{
						if(harddiskSource.equals(harddiskList[i]))	{
							isMediaMatched = true;
							break;
						}
					}
					/* Incase if multiple Floppy/USB media is connected, this
					 * condition will be used to track the removal of redirected media
					 */
					if(!isMediaMatched) {
						if(JViewerApp.getInstance().m_mediaDlg != null)
							JViewerApp.getInstance().m_mediaDlg.setVisible(false);
						JViewerApp.getInstance().getM_IUSBSession().StopHarddiskRedir(hddevice_no, IUSBRedirSession.STOP_NORMAL);
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
				JViewerApp.getInstance().getM_IUSBSession().stopHarddiskAbnormal(hddevice_no);
				Debug.out.println(e);
			}
		}
		return;
	}
}
