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

package com.ami.kvm.jviewer.videorecord;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Date;
import java.util.TimerTask;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.common.ISOCFrameHdr;
import com.ami.kvm.jviewer.gui.JViewerApp;
import com.ami.kvm.jviewer.gui.LocaleStrings;
import com.ami.kvm.jviewer.gui.RecorderToolBar;
import com.ami.kvm.jviewer.kvmpkts.KVMClient;

public class DisplayVideoDataTask extends TimerTask{

	public static boolean run = true;
	private ISOCFrameHdr m_frameHdr;
	private byte[] videobuffer;
	private Date startTime;



	public DisplayVideoDataTask(byte[] buffer){

		videobuffer = buffer;
		m_frameHdr = JViewerApp.getSoc_manager().getSOCFramehdr();

		if(JViewer.isdownloadapp()){
			JViewerApp.getInstance().getVideorecordapp().showInformationDialog(LocaleStrings.getString("Y_1_DVDT"));
		}

	}

/**
 * thread proces the video buffer and
 */

	public void run() {

		byte[] timestamp = new byte[4];
		long Prevtimestamp = 0;
		long Currtimestamp = 0;
		byte[] code = new byte[1];
		byte[] header = new byte[m_frameHdr.getFrameHeadersize()];
		byte[] pad = new byte[3];
		int retval =0;
		run = true;
		long diff = 0;
		int findIndex = JViewerApp.getInstance().getVideorecordapp().getFileIndex();

		//ByteBuffer fullvideo_data = ByteBuffer.wrap(videobuffer); //converting the byte array buffer to Byte buffer

		/*String path = System.getProperty("java.io.tmpdir");
		FileOutputStream fc = null;
		System.out.println("PATH"+path);
		try {
			fc = new FileOutputStream(path+"video.dat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
		    fc.write(videobuffer);
		} catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}

		try {
		    fc.close();
		} catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}*/
		startTime = new Date(System.currentTimeMillis()/1000);


		FileInputStream fc_read = null;

		try {
		    fc_read = new FileInputStream(JViewerApp.getInstance().getVideorecordapp().getVideotmpstorepath()[findIndex]);
		} catch (FileNotFoundException e) {
			Debug.out.println(e);
		}

		while(run)
		{

		    ByteBuffer header_bb = ByteBuffer.wrap(header);
			header_bb.clear();
			try{
				//Checking the buffer limit,if limit exceed the processed the entire data so breaking the thread
			    	retval =fc_read.read(timestamp);
				if (retval <= 0)
				{
					run = false;
					onenablemenu();
					this.cancel();
					continue;
				}

				ByteBuffer timestampbuffer = ByteBuffer.wrap(timestamp);
				timestampbuffer.order(ByteOrder.LITTLE_ENDIAN);
				Currtimestamp = timestampbuffer.getInt();

				if(Prevtimestamp == 0)
					Prevtimestamp = Currtimestamp;


				retval = fc_read.read(code);
				if (retval <= 0)
				{
					run = false;
					onenablemenu();
					this.cancel();
					continue;
				}

				// Padding of remaining 0 which came in for byte alignment
				retval = fc_read.read(pad);

				/*Value 0x55 = video data
				 * Value 0Xaa = nochange*/
				if ((code[0] == 85) ) //0x55 video screen
				{
					diff = (Currtimestamp - Prevtimestamp);
					if (diff > 0)
					{
						try {
							Thread.sleep(diff*1000);
						} catch (InterruptedException e) {
							Debug.out.println(e);
						}
					}
				}
				else if ((code[0] == 170))//0xaa no change
				{
					continue;
				}
				else if ((code[0] == -86))//0xff?
				{
					continue;
				}
				else if(code[0] == 102) //0x66 blank screen
				{
					JViewerApp.getInstance().getVidClnt().onBlankScreen();
					continue;
				}

				Prevtimestamp = Currtimestamp;
				//fullvideo_data.get(header);
				retval = fc_read.read(header);
				if (retval <= 0)
				{
					run = false;
					onenablemenu();
					this.cancel();
					continue;
				}
				header_bb.position(m_frameHdr.getFrameHeadersize());
				m_frameHdr.setHeader(header_bb);
				header_bb.position(0);
				
				//handle large frame sizes to avoid OutOfMemoryError
				if(m_frameHdr.getFrameSize() > KVMClient.MAX_FRAGMENT_SIZE){
					run = false;
					onenablemenu();
					this.cancel();
					break;
				}
				byte [] framedata = new byte[m_frameHdr.getFrameSize()];
				//fullvideo_data.get(framedata);
				retval = fc_read.read(framedata);

				if (retval <= 0 || (retval < m_frameHdr.getFrameSize()))
				{
				    run = false;
				    onenablemenu();
				    this.cancel();
				    continue;
				}
				ByteBuffer buffer = ByteBuffer.allocate(m_frameHdr.getFrameSize()+m_frameHdr.getFrameHeadersize());

				buffer.order(ByteOrder.LITTLE_ENDIAN);
				buffer.put(header, 0, m_frameHdr.getFrameHeadersize());
				buffer.put(framedata);

				buffer.position(m_frameHdr.getFrameSize()+m_frameHdr.getFrameHeadersize());

				JViewerApp.getInstance().getVidClnt().onNewFrame(buffer);
			}
			catch(Exception e){
				Debug.out.println(e);
			}

			if(JViewer.isdownloadapp())
				calculateDuration();
		}

		try {
		    fc_read.close();

		} catch (IOException e) {
		    Debug.out.println(e);
		}

	}

	private void onenablemenu()
	{
		RecorderToolBar toolBar = (RecorderToolBar)JViewerApp.getInstance().getM_wndFrame().toolbar;
		toolBar.enableButton(toolBar.replayButton);
	}

	/**
	 * Method calculates the duration of the Video to be recorded and set this duration in video record class
	 *
	 */
	public void calculateDuration(){

		 long duration =((new Date(System.currentTimeMillis()/1000)).getTime() - startTime.getTime());
		 JViewerApp.getInstance().getVideorecordapp().setDuration(duration);
	}
}
