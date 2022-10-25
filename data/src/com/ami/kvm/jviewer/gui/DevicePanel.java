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

package com.ami.kvm.jviewer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DevicePanel extends JPanel {

	/**
	 *SerialVersionUID added
	 */
	private String[] imagePathList;
	private static final long serialVersionUID = 1L;
	public static final int	DEVICE_TYPE_CDROM=0;
	public static final int	DEVICE_TYPE_FLOPPY=1;
	public static final int	DEVICE_TYPE_HD_USB=2;
	private static final int DEFAULT_NUM_DRIVES=1;
	public static final int IMAGE_TYPE_CDROM = 1;
	public static final int IMAGE_TYPE_FLOPPY = 2;
	public static final int IMAGE_TYPE_HARDDISK = 3;

	private	ButtonGroup	m_deviceButtonGroup;
	private JRadioButton[]	m_deviceButtons;
	public	JButton			m_browseButton;
	public JComboBox m_imagePath;
	public  String ImagepathSelect;

	/***
	 * Creating the Vmedia device display dialog
	 * @param driveList - list of the drives to be  displayed
	 * @param deviceType - floppy or CD
	 * @param deviceIndex - device index determines the instance device for
	 *						which the device panel is created.
	 */
	public DevicePanel(String driveList[], int deviceType, int deviceIndex) {

		super( new GridLayout((driveList!=null && driveList.length>DEFAULT_NUM_DRIVES)?driveList.length+1:DEFAULT_NUM_DRIVES+1,1,0,0) ); //including External Image
		int totalDrives = (driveList!=null)?driveList.length+1:1;
		// build the radio buttons for each drive.
		m_deviceButtons = new JRadioButton[totalDrives];
		m_deviceButtonGroup = new ButtonGroup();

		// first button will be "Floppy Image/ISO Image"
		switch(deviceType) {
			case DEVICE_TYPE_CDROM:
				m_deviceButtons[0] = new JRadioButton( "CD Image");
				break;
			case DEVICE_TYPE_FLOPPY:
				m_deviceButtons[0] = new JRadioButton( "Floppy Image");
				break;
			case DEVICE_TYPE_HD_USB:
				m_deviceButtons[0] = new JRadioButton( "HD/USB Image");
				break;
		}
		m_deviceButtons[0].setBackground(Color.WHITE);
		m_deviceButtons[0].addChangeListener(new radiobuttonlistener());
		m_deviceButtonGroup.add( m_deviceButtons[0] );
		m_deviceButtons[0].setSelected(true);

		// rest of the actual drives
		for(int r_ix=1,d_ix=0;r_ix<totalDrives;r_ix++,d_ix++)
		{
			m_deviceButtons[r_ix] = new JRadioButton(driveList[d_ix]);
			m_deviceButtons[r_ix].setBackground(Color.WHITE);
			m_deviceButtonGroup.add( m_deviceButtons[r_ix] );
		}

		// construct panel for reading iso image/floppy image which will have a radio button, text box, push button.
		JPanel panelImage = new JPanel(new BorderLayout(2,0));
		panelImage.setBackground(Color.WHITE);

		switch(deviceType) {
			case DEVICE_TYPE_CDROM:
				m_imagePath = new JComboBox(JViewerApp.Imagepath_CD[deviceIndex]);
				imagePathList = JViewerApp.Imagepath_CD[deviceIndex];
				break;
			case DEVICE_TYPE_FLOPPY:
				m_imagePath = new JComboBox(JViewerApp.Imagepath_Floppy[deviceIndex]);
				imagePathList = JViewerApp.Imagepath_Floppy[deviceIndex];
				break;
			case DEVICE_TYPE_HD_USB:
				m_imagePath = new JComboBox(JViewerApp.Imagepath_Harddsik[deviceIndex]);
				imagePathList = JViewerApp.Imagepath_Harddsik[deviceIndex];
				break;
		}
		m_imagePath.setEditable(false);
		m_imagePath.setBackground(Color.white);
		m_imagePath.setBorder(null);
		m_imagePath.setMaximumRowCount(JViewerApp.MAX_IMAGE_PATH_COUNT);

		m_imagePath.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        JComboBox cb = (JComboBox)e.getSource();
		        String Imagepath = (String)cb.getSelectedItem();
		        setImagepathSelect(Imagepath);
		      }
		});

		m_browseButton = new JButton(LocaleStrings.getString("A_1_DP"));
		panelImage.add(m_deviceButtons[0], BorderLayout.WEST);
		panelImage.add(m_imagePath, BorderLayout.CENTER);
		panelImage.add(m_browseButton, BorderLayout.EAST);
		add(panelImage);

		//add rest of the drives to the table
		for(int r_ix=1;r_ix<totalDrives;r_ix++)
			add(m_deviceButtons[r_ix]);

		setBackground(Color.WHITE);
	} // end constructor



	/**
	 *
	 * Change action listener for the ISO image button or Device button is selected
	 */
	class radiobuttonlistener implements ChangeListener {

		public void stateChanged(ChangeEvent e)
		{
			if (m_deviceButtons[0].isSelected()) {
				if (m_browseButton != null)
					m_browseButton.setEnabled(true);
				if (m_imagePath != null)
					m_imagePath.setEnabled(true);
			}
			else {
				m_browseButton.setEnabled(false);
				if (m_imagePath != null)
					m_imagePath.setEnabled(false);
			}
		}
	}
	/**
	 * Return the IsoImage button is slected or Unselected
	 * @return
	 */
	public boolean isImageSelected() {
		if (m_deviceButtons[0].getModel() == m_deviceButtonGroup.getSelection())
			return true;

		return false;
	}

	/***
	 * Set the image path specified by the user
	 * @param path - selected path
	 * @param devicetype - the redirected device type(CD/Floppy/HDD)
	 * @param deviceIndex - index of the device to determine for which instance of the
	 *					device, the path needs to be set.
	 */
	public void setImagePath(String path, int devicetype, int deviceIndex)
	{		
		int j = 0;
		//List of already savedpaths .
		String[] savedPathList = null;
		switch(devicetype) {
		case DEVICE_TYPE_CDROM:
			savedPathList = JViewerApp.Imagepath_CD[deviceIndex];
			break;
		case DEVICE_TYPE_FLOPPY:
			savedPathList = JViewerApp.Imagepath_Floppy[deviceIndex];
			break;
		case DEVICE_TYPE_HD_USB:
			savedPathList = JViewerApp.Imagepath_Harddsik[deviceIndex];
			break;
		}

		imagePathList = new String[JViewerApp.MAX_IMAGE_PATH_COUNT];
		if(m_imagePath.getItemAt(0) == null){
			m_imagePath.insertItemAt(path, 0);
			imagePathList[0] = path;
		}
		else{
			//if an image path, thats already in the combobox list, is browsed and selected,
			//remove the already existing entry.
			for(j=JViewerApp.MAX_IMAGE_PATH_COUNT-1; j>=0; j-- ) {
				if(m_imagePath.getItemAt(j)!= null && m_imagePath.getItemAt(j).equals(path)){
					m_imagePath.removeItemAt(j);
					break;
				}
			}
			//Ensure that the number of paths shown in image path combobox list, is equal to the limit +1
			//while browsing. One extra entry is allowed while browsing to accommodate the newly selected
			//image path.
			if(m_imagePath.getItemAt(JViewerApp.MAX_IMAGE_PATH_COUNT) != null && 
					!path.equals(m_imagePath.getItemAt(JViewerApp.MAX_IMAGE_PATH_COUNT))){
				m_imagePath.removeItemAt(JViewerApp.MAX_IMAGE_PATH_COUNT);
			}
			//Don't keep on adding new items browsing for a file.Add only the last item selected to the list.
			if(!path.equals(m_imagePath.getItemAt(0))){
				m_imagePath.insertItemAt(path, 0);
				for(j=0; j < JViewerApp.MAX_IMAGE_PATH_COUNT; j++){
					if(m_imagePath.getItemAt(1)!= null && m_imagePath.getItemAt(1).equals(savedPathList[j]))
						break;
				}
				if(j==JViewerApp.MAX_IMAGE_PATH_COUNT)
					m_imagePath.removeItemAt(1);
			}

			//Store the paths in the combobox list into imagePathList array.
			for(j=0;j<JViewerApp.MAX_IMAGE_PATH_COUNT;j++){
				if((String) m_imagePath.getItemAt(j) == null)
					break;
				imagePathList[j] = (String) m_imagePath.getItemAt(j);
			}

		}
		m_imagePath.setSelectedIndex(0);
	}

	/**
	 * Saves the path of the images redirected through VMedia redirection.
	 * @param pathList - list of redirected paths.
	 * @param devicetype - type of redirected device (CD/Floppy/HDD)
	 * @param deviceIndex - index of the device to determine for which instance of the
	 *						device, the path needs to be saved.
	 */
	public void saveImagePath(String[] pathList, int devicetype, int deviceIndex){
		//Ensure that the number of paths shown in image path combobox list, is equal to the limit.
		if(m_imagePath.getItemAt(JViewerApp.MAX_IMAGE_PATH_COUNT) != null){
			m_imagePath.removeItemAt(JViewerApp.MAX_IMAGE_PATH_COUNT);
		}
		switch(devicetype) {
		case DEVICE_TYPE_CDROM:
			JViewerApp.Imagepath_CD[deviceIndex]= pathList;
			break;
		case DEVICE_TYPE_FLOPPY:
			JViewerApp.Imagepath_Floppy[deviceIndex] = pathList;
			break;
		case DEVICE_TYPE_HD_USB:
			JViewerApp.Imagepath_Harddsik[deviceIndex] = pathList;
			break;
		}
	}

	/**
	 * Get the Image path name of the ISO redirection
	 * @return
	 */
	public String getImagePath()
	{
		String path = (String)m_imagePath.getSelectedItem();
		
		if(path == null)			
			path = "";

		return path ;
	}

	/**
	 * Return the User selected device string its a Drive or ISO name
	 * @return
	 */
	public String selectedDeviceString()
	{
		for (Enumeration e=m_deviceButtonGroup.getElements(); e.hasMoreElements(); )
		{
		    JRadioButton b = (JRadioButton)e.nextElement();
		    if (b.getModel() == m_deviceButtonGroup.getSelection())
		    	return b.getText();
		}
		return "";
	}

	/**
	 *Method disable all component in the VMedia dialog
	 *
	 */
	public void disableAll()
	{
		//disable radio buttons
		for(int i=0; i<m_deviceButtons.length;i++)
			m_deviceButtons[i].setEnabled(false);

		//disable browse button
		m_browseButton.setEnabled(false);
		//disable text field button
		m_imagePath.setEnabled(false);
	}

	/**
	 * Method Enable all component in the VMedia dialog
	 *
	 */
	public void enableAll()
	{
		//disable radio buttons
		for(int i=0; i<m_deviceButtons.length;i++)
			m_deviceButtons[i].setEnabled(true);

		if(m_deviceButtons[0].isSelected()) {				
			//disable browse button
			m_browseButton.setEnabled(true);	
			//disable text field button
			m_imagePath.setEnabled(true);
		}
	}
	public void SelectRadioButton_StateChange(String strRedirectedDevice,boolean state)
	{
		for (Enumeration e=m_deviceButtonGroup.getElements(); e.hasMoreElements(); )
	    {
			JRadioButton b = (JRadioButton)e.nextElement();
			if ( b.getText().equals(strRedirectedDevice) && b.isEnabled() != state) {
				m_deviceButtons[0].setSelected(true);
				b.setEnabled(state);
				return;
			}
	    }
	}
	
	/**
	 * select the User selected drive or ISo image path name once redirection started,
	 * if user invoke the Vmedia dialog
	 * @param strRedirectedDevice
	 */
	@SuppressWarnings("unchecked")
	public void SelectRadioButton(String strRedirectedDevice)
	{
		for (Enumeration e=m_deviceButtonGroup.getElements(); e.hasMoreElements(); )
	    {
			JRadioButton b = (JRadioButton)e.nextElement();
			if ( b.getText().equals(strRedirectedDevice) ) {
				b.setSelected(true);
				return;
			}
	    }

	    // If control came here, that means, there is no radio button matching the supplied string. So, select
	    // the first radio button by default which is a CDROM/Floppy/UsbKey Image.
	    Enumeration e=m_deviceButtonGroup.getElements();
	    if( e.hasMoreElements() ) {
	    	JRadioButton b = (JRadioButton)e.nextElement();
	    	b.setSelected(true); // this will be the first radio button in the button group.
	    	////m_imagePath.setText(strRedirectedDevice);
	    }
	}

	/**
	 * Gets the path list, after rearranging, if required.
	 * @return the pathList
	 */
	public String[] getImagePathList() {
		int index = m_imagePath.getSelectedIndex();
		String selectedItem = (String) m_imagePath.getSelectedItem();
		//if a path from the already existing path list is selected, bring it
		//to the first position.
		if(index != 0)
			for(int i = index; i > 0; i--)
			 imagePathList[i] = imagePathList[i-1];
		imagePathList[0] = selectedItem;
		return imagePathList;
	}

	/**
	 * @param pathList the pathList to set
	 */
	public void setImagePathList(String[] pathList) {
		this.imagePathList = pathList;
	}

	public String getImagepathSelect() {
		return ImagepathSelect;
	}

	public void setImagepathSelect(String imagepathSelect) {
		ImagepathSelect = imagepathSelect;
	}
}