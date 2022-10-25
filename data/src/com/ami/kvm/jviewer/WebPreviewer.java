package com.ami.kvm.jviewer;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

import javax.swing.JApplet;

import com.ami.kvm.jviewer.gui.JViewerApp;
import com.ami.kvm.jviewer.gui.JViewerView;
import com.ami.kvm.jviewer.jvvideo.JVVideo;
import com.ami.kvm.jviewer.kvmpkts.IVTPPktHdr;
import com.ami.kvm.jviewer.gui.SinglePortKVM;

public class WebPreviewer extends JApplet implements Runnable,ActionListener {
	private byte[]				m_frameBuf;
	private ByteBuffer			m_frameByteBuf;
	private static final String SERVER_IP = "serverip";
	private static final String KVM_PORT = "kvmport";
	private static final String KVM_SECURE = "kvmsecure";
	private static final String WEB_PORT = "webport";
	private static final String SINGLE_PORT = "singleport";
	private static final String WEB_COOKIE = "webcookie";	
	public static final byte BUTTON_WIDTH = 80;
	public static final byte BUTTON_HEIGHT = 20;
	private static final byte MSG_AREA_HEIGHT = 30;
	public static final byte RETRY_COUNT = 10;
	private static	JViewerView	m_view;
	private SinglePortKVM singlePortKvm;
	private static byte retryCount = 0;
	private int CatchSize = 0;
	private byte m_capStatus;
	private Thread paintTh;
	byte[] ip;
	Button Capture1 =  null;
	String serverip;
	int kvmport = 0 ;
	int kvmsecure = 0;
	int webport=0 ;
	boolean useSSL = false;

	private DataInputStream in = null;
	String webtok;	
	int singlePort = -1;

	public void init() {
		setLayout(null);
		setBackground(java.awt.Color.black);
		JViewer.setApptype(this.getParameter("apptype").trim());
		if(JViewer.isWebPreviewer()){
			Capture1 = new Button("Refresh");
		}
		else if(JViewer.isBSODViewer()){
			Capture1 = new Button("Download");
		}
		
		if(Capture1 != null){
			Capture1.setBounds(5,5,BUTTON_WIDTH,BUTTON_HEIGHT);
			Capture1.addActionListener(this);
			this.add(Capture1);
			Capture1.setEnabled(false);
		}
		m_view = JViewerApp.getInstance().getM_view();

	}

	public void destroy() {

	}

	/**
	 * Applet update override.
	 * This is overrode to optimize painting. update is allowed to do only painting.
	 *
	 * @param g Graphics context
	 */
	public void update(Graphics g)
	{
		paint(g);
	}

	/**
	 * Applet paint override
	 *
	 * @param g Graphics context
	 */
	public void paint(Graphics g)
	{
		try
		{
			Dimension appletSize = this.getSize();
			//the blank area on top of webpreviewer screen where the messages are displayed
			int displayAreaPadding = 0;
			BufferedImage m_image = JViewerApp.getInstance().getPrepare_buf().getM_image();
			Rectangle full_view = new Rectangle(0,0, appletSize.width, appletSize.height-displayAreaPadding);
			if(JViewer.isWebPreviewer()){
				displayAreaPadding = MSG_AREA_HEIGHT;
				g.setColor(Color.yellow);
				g.clearRect(0, 0, appletSize.width, appletSize.height);
				if(m_capStatus == JViewerApp.WEB_PREVIEWER_CAPTURE_FAILURE)
					g.drawString("Capture Failed",BUTTON_WIDTH+10, BUTTON_HEIGHT);
				else if (m_capStatus == JViewerApp.WEB_PREVIEWER_CAPTURE_IN_PROGRESS)
					g.drawString("Capturing....",BUTTON_WIDTH+10, BUTTON_HEIGHT);
				else if(m_capStatus == JViewerApp.WEB_PREVIEWER_CONNECT_FAILURE)
					g.drawString("Not able to connect to "+serverip,BUTTON_WIDTH+10, BUTTON_HEIGHT);
				else if (m_capStatus == JViewerApp.WEB_PREVIEWER_INVALID_SERVERIP)
					g.drawString("INVALID SERVER IP"+serverip,BUTTON_WIDTH+10, BUTTON_HEIGHT);
				else if (m_capStatus == JViewerApp.WEB_PREVIEWER_HOST_POWER_OFF){
					//increase the size of the blank area on top of webpreviewer screen
					//where the messages are displayed
					displayAreaPadding = MSG_AREA_HEIGHT + BUTTON_HEIGHT;
					g.drawString("Host is powered OFF or is in Sleep Mode",5, BUTTON_HEIGHT*2);
				}
				else
					g.drawString(m_view.viewWidth() + " x " + m_view.viewHeight(), BUTTON_WIDTH+10, BUTTON_HEIGHT);
				full_view = new Rectangle(0,0, appletSize.width, appletSize.height-(displayAreaPadding+1));
			}
			BufferedImage	pImage = reduce(m_image, appletSize.width, appletSize.height-displayAreaPadding);
			Rectangle r = full_view.intersection(g.getClipBounds());
			BufferedImage rgn = pImage.getSubimage(r.x, r.y, r.width, r.height);
			if(JViewer.isWebPreviewer())
				g.drawImage(rgn, r.x+5, r.y+displayAreaPadding, null);
			else if (JViewer.isBSODViewer()) {
				g.drawImage(rgn, r.x, r.y, null);
			}
		}
		catch (Exception e) 
		{
			Debug.out.println(e);
		}
	}

	public static BufferedImage reduce(BufferedImage srcImage, int dstWidth, int dstHeight)
	{
		Image rescaled = srcImage.getScaledInstance(dstWidth, dstHeight, Image.SCALE_AREA_AVERAGING);
		BufferedImage result = new BufferedImage(dstWidth, dstHeight, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = result.createGraphics();
		g.drawImage(rescaled, 0, 0, null);
		g.dispose();
		return result;
	}

	/* start */
	public void start()
	{
		if (paintTh == null) {
			paintTh = new Thread(this, "paint");
			paintTh.start();
		}
	}

	/* stop */
	public void stop()
	{
		if (paintTh != null) {
			paintTh.stop();
			paintTh = null;
		}
	}

	/* Thread run */
	public void run()
	{
		while (true)
		{
			if(JViewer.isWebPreviewer()){
				startCapture();
				Capture1.setEnabled(true);
				mywait();
			}
			else if(JViewer.isBSODViewer()){
				captureBSOD();
				Capture1.setEnabled(true);
				mywait();
			}
		}
	}

	public synchronized void mywait() {

		try {
			wait();
		}
		catch(InterruptedException e)
		{
			Debug.out.println(e);
		}
	}

	public synchronized void mynotifyAll() {
		notifyAll();
	}

	/**
	 * Start capturing frame data for web previewer applet
	 */
	public void startCapture()
	{
		serverip = this.getParameter(SERVER_IP).trim();
		kvmport = Integer.parseInt(this.getParameter(KVM_PORT).trim());
		kvmsecure =Integer.parseInt(this.getParameter(KVM_SECURE).trim());
		webport = Integer.parseInt(this.getParameter(WEB_PORT).trim());
		webtok = this.getParameter(WEB_COOKIE).trim();

		singlePort = Integer.parseInt(this.getParameter(SINGLE_PORT).trim());
		if(singlePort == 1)
			JViewer.setSinglePortEnabled(true);
		else
			JViewer.setSinglePortEnabled(false);
		if(webport ==0)
			webport=80;
		ip = JViewer.getServerIP(serverip);
		if(serverip.compareTo("") == 0)
			m_capStatus = JViewerApp.WEB_PREVIEWER_INVALID_SERVERIP;
		else {
			if(kvmsecure == 1)
				useSSL = true;
			JViewerApp.getInstance().OnConnectToServer(serverip, kvmport,webport, "asdfa", useSSL, webtok);
			JViewerApp.getInstance().getKVMClient().setM_redirection(true);

			while(true) 
			{
				m_capStatus = JViewerApp.getInstance().getWebPreviewerCaptureStatus();
				if(m_capStatus == JViewerApp.WEB_PREVIEWER_CAPTURE_SUCCESS ||
						retryCount == RETRY_COUNT ||
						m_capStatus == JViewerApp.WEB_PREVIEWER_CAPTURE_FAILURE ||
						m_capStatus == JViewerApp.WEB_PREVIEWER_HOST_POWER_OFF){
					JViewerApp.getInstance().OnVideoStopRedirection();
					break;
				}
				else if (m_capStatus == JViewerApp.WEB_PREVIEWER_CONNECT_FAILURE) {
					break;
				}
				// Sleep for 1000 ms
				try {
					retryCount++;
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					Debug.out.println(e);
				}
			}
		}
		if(m_capStatus == JViewerApp.WEB_PREVIEWER_CAPTURE_FAILURE ||
				m_capStatus == JViewerApp.WEB_PREVIEWER_CONNECT_FAILURE ||
				m_capStatus == JViewerApp.WEB_PREVIEWER_INVALID_SERVERIP ||
				m_capStatus == JViewerApp.WEB_PREVIEWER_HOST_POWER_OFF) {
			retryCount = 0;
			Capture1.setEnabled(true);
			repaint();
			return;
		}

		getFrameImage();
	}

	/**
	 * Start capturing frame data for BSOD applet
	 */
	private void captureBSOD(){
		serverip = this.getParameter(SERVER_IP).trim();
		webport = Integer.parseInt(this.getParameter(WEB_PORT).trim());
		if(webport ==0)
			webport=80;
		ip = JViewer.getServerIP(serverip);
		JViewerApp.getInstance().OnConnectToServer(ip);
		getFrameImage();
	}

	/**
	 * Reads the image buffer data from the captured file, for the WebPreviewer
	 * or the BSODViewer.
	 */
	private void getFrameImage(){
		// this method only use for HTTP
		try {
			URL srcUrl = null;
			if(JViewer.isWebPreviewer()){
				srcUrl = new URL(getCodeBase().getProtocol()+"://"+getCodeBase().getHost()+":"+webport+"/capture/webPreview.cap");
			}
			else if(JViewer.isBSODViewer()){
				srcUrl = new URL(getCodeBase().getProtocol()+"://"+getCodeBase().getHost()+":"+webport+"/bsod/crashscreen.cap");
			}

			HttpURLConnection connection = (HttpURLConnection) srcUrl.openConnection();
			connection.setUseCaches(false);
			in = new DataInputStream(connection.getInputStream());

			m_frameBuf = new byte[2 * JVVideo.MAX_X_RESOLUTION * JVVideo.MAX_Y_RESOLUTION];
			m_frameByteBuf = ByteBuffer.wrap(m_frameBuf);
			/*File f = new File ("Z:\\sengud\\megarac\\spx\\workspace\\RAINIER_WEBPREVIEW\\Build\\output\\ImageTree\\usr\\local\\www\\Java\\release\\webPreview.cap");
			DataInputStream in = new DataInputStream(new FileInputStream(f));*/
			if(JViewer.isBSODViewer()){
				JViewerApp.getSoc_manager().getSOCJVVideo().socreadframeattributes(in);//read color palette
				JViewerApp.getSoc_manager().getSOCJVVideo().socreadframeattributes(in);//read attribute
			}
			readFrameData();

			repaint();
		} catch(Exception e) {
			m_capStatus = JViewerApp.WEB_PREVIEWER_CONNECT_FAILURE;
			Debug.out.println(e);
			repaint();
		}
	}

	/**
	 * Reads the frame buffer data from the captured file and send it to
	 * JVVideo class for drawing on the viewer. 
	 */
	private void readFrameData(){
		retryCount = 0;
		byte[] buffer = new byte[4096];
		int count = 0;
		CatchSize = 0;
		m_frameByteBuf.position(0);

		count  = 0;
		try{
			while ((count = in.read(buffer)) > 0) {
				for (int i=0; i<count; i++)
					if((CatchSize+i) < m_frameBuf.length)
						m_frameBuf[CatchSize+i] = buffer[i];
				if((CatchSize+count < m_frameBuf.length))
					CatchSize += count;}
			in.close();
		}catch (IOException e) {
			Debug.out.println(e);
		}
		m_frameByteBuf.limit(CatchSize);
		m_frameByteBuf.position(CatchSize);
		JViewerApp.getInstance().getKVMClient().onNewFrame(m_frameByteBuf);
	}
	// Refresh Capture Screen
	public void actionPerformed (ActionEvent e)
	{ 
		if (e.getSource()== Capture1)
		{
			if(Capture1.getLabel().equalsIgnoreCase("Refresh")){
				Capture1.setEnabled(false);
				JViewerApp.getInstance().setWebPreviewerCaptureStatus(JViewerApp.WEB_PREVIEWER_CAPTURE_IN_PROGRESS);
				m_capStatus = JViewerApp.WEB_PREVIEWER_CAPTURE_IN_PROGRESS;
				repaint();
				mynotifyAll();
			}
			else if(Capture1.getLabel().equalsIgnoreCase("Download"))
			{
				Capture1.setEnabled(false);
				JViewerApp.getInstance().onVideoCaptureScreen();
				Capture1.setEnabled(true);
			}
		}
	}
}

