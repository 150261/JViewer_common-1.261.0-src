package com.ami.kvm.jviewer.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.util.TimerTask;
import javax.swing.ImageIcon;

import com.ami.iusb.IUSBRedirSession;
import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.common.ISOCFrameHdr;

public class FrameRateTask extends TimerTask {
	Dimension Client_System_prev;
	private boolean cdRedirected = false;
	private boolean fdRedirected = false;
	private boolean hdRedirected = false;

	@Override
	public void run() {
		try{
			JViewerApp.getInstance().setAppWndLabel(JViewerApp.getInstance().getVidClnt().getM_frameRate() + " fps");
			JViewerApp.getInstance().getVidClnt().setM_frameRate(0);
		} catch(Exception e)
		{
			Debug.out.println(e);
		}
		
		ISOCFrameHdr m_frameHdr = JViewerApp.getInstance().getSocframeHdr();
		Dimension Client_System = Toolkit.getDefaultToolkit().getScreenSize();

		if(Client_System_prev == null)
			Client_System_prev = Client_System;

		if(Client_System.width != Client_System_prev.width || Client_System.height != Client_System_prev.height) {
			JViewerApp.getInstance().getVidClnt().setFullScreenMode();// Set the fullscreen menu and toolbar icon status.
			//Set zoom options when clint resolution is changed.
			JViewerApp.getInstance().getVidClnt().setZoomOptionStatus();
		}
		Client_System_prev = Client_System;
		if(JViewerApp.getInstance().getM_IUSBSession() != null)
		{
			try{
				int numCD = JViewerApp.getInstance().getM_cdNum();
				int numFD = JViewerApp.getInstance().getM_fdNum();
				int numHD = JViewerApp.getInstance().getM_hdNum();
				int cdItr = 0;
				int fdItr = 0;
				int hdItr = 0;
				cdRedirected = false;
				fdRedirected = false;
				hdRedirected = false;
				for(cdItr = 0;cdItr < numCD;cdItr++)
				{
					if(JViewerApp.getInstance().getM_IUSBSession().getCdromSession(cdItr) != null)
					{

						if(JViewerApp.getInstance().getM_IUSBSession().getCdromSession(cdItr).isCdRedirectionKilled()){
							JViewerApp.getInstance().getM_IUSBSession().StopCDROMRedir(cdItr, IUSBRedirSession.STOP_ON_TERMINATE);
						}
						else if(JViewerApp.getInstance().getM_IUSBSession().getCdromSession(cdItr).isCdImageEjected()){
							if(JViewerApp.getInstance().getM_IUSBSession().getCdromSession(cdItr).isCdImageRedirected()){
								JViewerApp.getInstance().getM_IUSBSession().StopISORedir(cdItr, IUSBRedirSession.STOP_ON_EJECT);
							}
						}
						else{
							cdRedirected = true;
						}
					}
				}
				if(!cdRedirected)
					JViewerApp.getInstance().getM_IUSBSession().updateCDToolbarButtonStatus(cdRedirected);

				for(fdItr = 0;fdItr < numFD;fdItr++)
				{
					if(JViewerApp.getInstance().getM_IUSBSession().getFloppySession(fdItr) != null){
						if(JViewerApp.getInstance().getM_IUSBSession().getFloppySession(fdItr).isFdRedirectionKilled()){
							JViewerApp.getInstance().getM_IUSBSession().StopFloppyRedir(fdItr, IUSBRedirSession.STOP_ON_TERMINATE);
						}
						else if(JViewerApp.getInstance().getM_IUSBSession().getFloppySession(fdItr).isFdImageEjected()){
							if(JViewerApp.getInstance().getM_IUSBSession().getFloppySession(fdItr).isFdImageRedirected()){
								JViewerApp.getInstance().getM_IUSBSession().StopFloppyImageRedir(fdItr, IUSBRedirSession.STOP_ON_EJECT);
							}
						}
						else{
							fdRedirected = true;
						}
					}
				}
				if(!fdRedirected)
					JViewerApp.getInstance().getM_IUSBSession().updateFDToolbarButtonStatus(fdRedirected);

				for(hdItr = 0;hdItr < numHD;hdItr++)
				{
					if(JViewerApp.getInstance().getM_IUSBSession().getHarddiskSession(hdItr) != null){
						if(JViewerApp.getInstance().getM_IUSBSession().getHarddiskSession(hdItr).isHdRedirectionKilled()){
							JViewerApp.getInstance().getM_IUSBSession().StopHarddiskImageRedir(hdItr, IUSBRedirSession.STOP_ON_TERMINATE);
						}
						else if(JViewerApp.getInstance().getM_IUSBSession().getHarddiskSession(hdItr).isHdImageEjected()){
							if(JViewerApp.getInstance().getM_IUSBSession().getHarddiskSession(hdItr).isHdImageRedirected()){
								JViewerApp.getInstance().getM_IUSBSession().StopHarddiskImageRedir(hdItr, IUSBRedirSession.STOP_ON_EJECT);
							}
						}
						else{
							hdRedirected = true;
						}
					}
				}
				if(!hdRedirected)
					JViewerApp.getInstance().getM_IUSBSession().updateHDToolbarButtonStatus(hdRedirected);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
