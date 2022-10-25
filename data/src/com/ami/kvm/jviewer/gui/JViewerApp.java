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

/**
 2.0 - Initial version.

 2.0.1
 	#Updated the library from SP2 to package
 2.0.2
 	#setautokeybreak mode is true
 2.0.3
 	#Image display path is not editable in VMedia box
 	#Autodetect Menu item is selected disabled the Softkeyboard
 1.0.7
 	#Modidfied the vrsion no to package no
 1.0.7
 	#Added the ISOImage and Floppy Image path reminder in the Vmedia dilog
 	#Modified the Status bar alignment to Box layout,if the screen size changes the LED state should be displayed properly
 1.0.9
   # Bug Fix : ID's 12520,12514,12455,12454,11462,12454 ,12091 and Refresh option enabled in Fullscreen menu.
 1.0.10
   # Bug Fix :Id's 12470 and 12472
 1.0.11
   # Buf Fix : ID's 12661 ,12879 and  12844
 1.0.15
   # Bug fix : Added AMI logo in Help,Alt+Gr issue
 1.0.16
  # LIBHARDDISK Library  source files renamed
 1.0.17
   # Removed unwanted file in res folder
 1.0.18
   # Bug Fix for Auto detect (Bug ID :13162)
1.0.20
	#Moved the Compression menu to SOC 
1.0.23
	#Both Shift key pressed with some other key,release event not receiving in the keylistennner so modifier flag not reset
1.0.24
	#Added the setharddisk command no in macro as 0xf4 
	#while disconnecting harddisk set the state to notconnected in the VMedia dialogs
1.0.25
	# Num,Caps,Scroll options do not syn with host machine.
	# HardDisk/USB key emulation-Select USB or hard Disk-Virtual media window buttons gets grayed out  
	# The device name shown on the multiple Hard disk/USB Key Redirecton dialogue is not correct  
	# Add one space between Hard and disk  
	# Press the windows keys using client keyboard invoke windows menu popup in both host and client windows.  
	# Improper message displayed for redirection already in use  
	# Media encryption-enabled by one client but the other client dont know when the console is opened
1.0.26
	#ContextMenu added  
1.0.27
	# Zoom Feaatue added
	#Video Recording
	#Macros Added
1.0.28
    # Bug fixes in Mouse Acceleration option.
 1.0.29
 	#Bug fixes:
 	1. Ctrl+ESC issue
 	2. Code cleanup in GEtframe and setframe
 	3.IP is not displayed on the Console
 1.0.32
 	#Moved the SOC command to the SOC package
	#Mouse Drag issue in relative mode
 1.0.34
 	#Added Multiple instance CD/FD?HD support
 	#Zoom In and out name Modified
 1.0.35
 	#removed the thread  to find the removed physical drive for HDredirection 
 1.0.36
 	#Issue fixed detecting USB Harddisk
 1.0.37
 	#Isue fixed display the redirected Fixed harddisk
 1.0.41
 	#Bug fixes:
 	1.NUM,CAPS,SCROLL lock LED blink issue
 	2.Mouse Calibration menu disable when pause redirection	
 1.0.42
	# Bug Fixes:
	# Add one space between Hard and disk 
	# In Java console, when clicking the Ctrl+Alt+Del in the bottom righ tof console, keyboard gets hanged. 
	# When Java console is minimized, the menus shrink and are not readable and accessible. 
	# In Java console,when we click settings under Video recording, the popup title needs to be edited. 
	# Shotcut key Alt - k not working for turn off Host display
	# Modified the Gettable panel to getjpanel for VMedia dialog to be displayed in SPX MultiViewer
1.3.0
	#Bug Fixes:
	1.When "Pause Redirection" is selected in the Java console window, it can't mount cd/dvd media successfully.
	2.when redirection is paused, and Auto detect in bandwidth is given it doesnt work even after redirection is resumed.
	3.In Java console,when we copy and paste files the mouse doesn't sync and unable to access the console thereafter
1.4.0
	#Bug Fix
	Stress Keyboard /mouse encryption in console - multiple sesssion are running is displayed
1.5.0
	#Bug Fixes
	1.Mouse calibration is not stable after changing the resolution in linux
	2.ALT+F did not work for more than 2 times while pressing continously
1.6.0
	#Bug Fixes:
	1. If Media 1 is redirected - Media 2 should be disabled in the virtual media wizard
	2.When Java console is minimized, the menus shrink and are not readable and accessible.
	3.Short cut Zoom option not able to use when we launch java console
	4.Shortcut option-Display a softkeyboard option was not displaying properly
1.6.1
	#Bug Fixes: Updating the user messages appropriately based on firmware conditions
1.8.
	#Modified  the Harddisk reader Opendevice return value
1.11.0
	# KB/Mouse Encryption Fail
1.13.0
	#  Alt + slash option alone not working in Dutch(Belgium) keyboard 
	#  In Java console,the Redirection icons representation does not vary when they are enabled and disabled. 
1.14.0
	#IDE harddisk in linux not finding,hard coded /dev/sd devices added /dev/hd also
1.15.0
	#  In Jviewer the prompt message on clicking Connect CD/DVD needs to be edited for spell check. 
	#  When Java console is open and virtual media configurations are reset, the error message needs to be edited. 
1.16.0
	#unable to access virtual media wizard in java console after connect and Disconnect the devices
	#USB Key-Reading Bytes still shows even we unplug USB key
	#File path for Video Record can be accepted
	#cosmetic issue in video record settings
	#Icon text is not updated for mouse show cursor
1.20.0
	#Modified the mouse sent event to the host reducing the timer thread polling
1.21.0
	#Japanes keyboard Issue Fixed
1.23.0
	#Video rcording Issue   fixed
1.24.0
	#Video recording Issue for window close event
1.25.0
	#ALT+N is not working in full screen mode.
	#shift + key input sends key information twice	
1.26.0	
	#Pause and play redirection button is not working in Java console.
1.30.0
	#Added the Fullscreen concurrent session information
1.31.0
	#Multiple instance CD/FD/HD redirection is disconnect,not updating the VMEdia dialog
1.32.0
	#Jviewer will work as JViewer application,Video player and video downloader.
1.34.0
	#JViewer auto-keyboard layout issue in Mac-OS cilent fixed.
	#Hebrew softkeyboard layout modified.
	#Error message shown when two sessions launched from same client to same host corrected. 
1.35.0
	#Host mouse cursor, client mouse cursor and JViewer draw cursor sychronized for RHEL
	#Fullscreen menubar for Mac OS client modified.
	#JVToolbar Pause button gaining focus on clicking JViewerView issue solved. 
1.38.0
	#CD eject in the host and supported Image eject support
	#Mouse sync issue
1.39.0
	#Mouse caliberation issue fixed
1.40.0
	#Invalid mouse mode pop-up fixed
	#KVM-sharing timer counter reset problem
1.41.0
	#Send Mouse button status for drag
	#update the Toolbars CD/Floppy/HD Icon for Image redirection 
1.43.0
	# Fixed BufferOverFlowException issue for relative mouse mode.	   
1.47.0
	#disabling menu's till first user allows access to second user	
1.48.0
	#Server Power control Options added
1.49.0	
	#Video recording stop on host reboot issue fixed.
	#Recorded AVI file is palyable in VLC media player also.
	#Video Download app file chooser dialog can't be closed issue solved.
1.51.0
	#Media redirection and Power Control options for KVM session with "Video Only" privilege, disabled.
	#Mouse calibration operation is not allowed if zoom value is not equal to 100%.
	#Input to Video Length textfield of Video record settings dialog limited to 4 characters. 
1.52.0
	#Service configuration change in the web to reflect in jviewer 
	#Parameter parsing added for each argument need to specify key value
	#mouse mode change & cd,fd&hd count updated at runtime
1.53.0
	# Exit menu disables while pausing redirection issue fixed
	# Service configuration changes for vmedia handled using thread synchronization.
	# Moved the mouse mode change message dialog into JViewerApp.
1.56.0
	#CD/FD/HD/Adviserd stop and start is fixed if the service is active and inactive
	#setting the adviser KVM session count in the NCML
	#SYnc the error messages if the servie s co
1.57.0
	#Activ client menu added	
1.58.0
	#Active client Images added
	#if the user list display two entry the current user indication green dot shows for both the user in the toolbar
	#In MAC Client Fullscreen MEnubar properly arranged 
1.59.0
	#Notiy thread for recording video if the blank screen comes from the adviser
1.60.0
	#Added the interface method and member to disable the SOC menu if the User given partial permission
1.61.0
	#Added the Imagepath combo fox-replaced the existing imagepath is already found
	#MenuItem and Toolbar icons disabled if the Host is off
1.62.0
	#Service Config packet size change modification
1.66.0
	#Video Record Settings menu gets enabled when host resolution changes, while video recording is in progress
	 issue fixed.
	#Power On server menu gets enabled on video pause and resume issue fixed.
	#Mouse mode change information dialog not shown when the mode is changed for the first time issue fixed. 
1.67.0
	#Modifier key status in JViewer invalid aftert host reboot, issue fixed. 
1.68.0
    #Dynamic load OEM logo and copyright string from web. when About dialog is shown.
1.69.0
	#Video Recorder App recording blank screen during Auto Video Record, issue fixed.
	#Included softawre keyboard support for Turkish-F and Turkish-Q keyborad language layouts.
1.70.0
	#System Time Monitor thread added to varify whether client system time has been 
	cahnged, to avoid abnormal conditions in JViewer.
	#Video Recording:Length of recorded video differs when video record duration is 
	given under 1 minute(60 second), issue fixed.
1.72.0
	# Server Power control icon is functioning the in client2 during 2nd client 
	requesting permission from 1st client, issue fixed
	# Message dialog shown stating that Device redirected in read only mode, when
	 CD-media is redirected.
	# No popup message for changing the mouse mode from Relative to Absolute mode,
	when ther is no video redirection(blank screen), issue fixed.
	# Included needed changes for Service Configuration Pkt size change in libncml.
1.73.0
	# Power status in JViewer updated immediately after Server Power ON or
	Server Power OFF event.
	# Trying to launch a third KVM session, after launching 2 sessions in same client,
	leads to Maximum no: of session reached message to be displayed, issue fixed.
	# Host not waking up from sleep mode if mouse is moved in JViewer, issue fixed.
	# Handled Thread.sleep() time deviation problem by setting time thresholds.
1.74.0
	# Java preview option shows connection failed status when host video is in
	sleep mode, issue fixed
	# When we Pause Console 1 and launch Console 2, the permission given by
	console 1 doesnt reflect in console 2, issue fixed.
1.75.0
	# User defined macros can be added to Keyboard menu and toolbar hotkey button popup. 
1.76.0
	# Other mouse mode added to JViewer.
1.78.0
	# Other mouse mode mouse movement made smoother.
1.79.0
	# JViewer could be launched as a Stand Alone App. 
1.81.0
	# Language localization(Multi-langugae support) implemented in JViewer.
1.82.0
	# Added Language localization support (Multi-langugae support) for StandAloneApp.
1.83.0
	# Video Recorder App recording only a blank screen, when the video dump file on
	BMC has only a single frame (mostly No Signal frame); issue fixed.
1.84.0
	#MouseCalibration implementation breaks in fullscreen mode; issue fixed.
	# Web Privewer not updating properly while SLES 11.0 installation with host resolution
	1600 X 1200. Issue fixed.
1.85.0
	# Client machine IP is displayed as 0.0.0.0 when console session is launched with ipv6
	address, issue fixed.
1.86.0
	# Soft keyboard is not working in JViewer when keyboard/mouse encryption is enabled and
	KVM sharing is initiated.
1.88.0
	# Stop command send to adviser before pop-up message appears in JViewer when adviser 
	restarts to clear session properly.
1.89.0
	# Show cursor fuctionality in JViewer disabled when the zoom value varies form 100%
	in Relative mouse mode.
1.91.0
	# Stand Alone Application is not able to launch using valid IPV6 address; issue fixed.
1.92.0
	# The cache of the files browsed in Virtual Media diaolg doesn't store the latest one if more than 6 files were browsed; issue fixed.
1.93.0
	# User Deifned Macro : Mac Meta key issue and Alt+F4 issue fixed.
1.94.0
	# Mouse pointer is not visible in java console, when the mouse hovers over menu items, in Mac client with Relative mouse mode; issue fixed.
1.95.0
	# Video recorded using Video Recording feature in JViewer is not able to be played
	 using VLC Media player; isseu fixed. 
1.96.0
	#Single Port KVM support
1.97.0
	# VMedia menu and toolbar buttons enabled even if host is powered off.
1.98.0
	# Added apptype macro for WebPreviewer.
1.99.0
	# All the message dialogs that block JViewer control flow are replaced with InfoDialogs
1.100.0
	#BSODVIewer applet implemenattion.
1.101.0
	#modified session token length to 128 for MAXtoken len changed in server side
1.102.0
	French launguage localization support added.
1.105.0
	 Virtual media unable open when repeat connect/disconnect image, issue fixed.
1.106.0
	Directory name and file name used to store recorded video file modified.
1.107.0
	Changed power off command send from power status button as Immediate Power Off, instead of Orederly Power Off.
1.108.0
	Mouse cursor automatically moves to the upper left corner in relative mode issue fixed.
1.109.0
	LED status bar implemented for JViewer in full screen mode.
1.110.0
	Corrected the misspelled words in various user information messages.
1.111.0
	The mouse cursor won't resync at the initial position, after using the scroll bars to scroll the view; issue fixed
1.112.0
	#Single Port KVM support for HTTPS	
1.113.0
	The semaphor lock on video dump file in Auto video recording , is released while closing the video player or recorder application.
1.114.0
	#StandAlone changes for HTTPS Session when singleport Feature Enabled.
1.115.0
	Unable to launch jviewer using java version 7; issue fixed.
1.116.0
	Unable to view the virtual media wizard when the user changes the virtual media devices configuration; issue fixed
1.117.0
	# The supported localization languages are not listed in language selection combo box in Stand Alone application connection dialog; isseu fixed.
	# User is given provision to close Stand Alone application connection dialog while connecting.
1.119.0
	# websessin cookie parsing modified to avoid BMC_IP_ADDR data with session cookie
1.120.0
	#Client Physical Keyboard Language Layout Support
1.121.0
	Deployment of JViewer Stand Alone application as a single jar file.
1.22.0
	#GUI Language localization menu
1.23.0
	Stand Alone Application moidifed as a configurable feature in PRJ file.
1.124.0
	Unable to add Print Screen macro and, unable to prevent dupliacte entries, in User Defined Macro feature; issue fixed
1.125.0
	Capture JViewer screen feature implemented.
1.126.0
	JPEG file filter added for Save File dialog in Capture Screen feature.
1.130.0
	#Player and download application as standalone application	
	#Multiple video files download from BMC
1.131.0
	Added new zoom options to the Zoom menu.
1.132.0
	Added download button for BSOD screen download
1.134.0
	Added support for sending IPMI raw commands to BMC
1.135.0
	Moved BSOD related soc function into soc package
1.136.0
	Enable/Disble KVM and Vmedia redirection based on KVM privilege argument.
1.137.0
	Added condition to avoid Webpreviewer and BSODApp from recievinf power status nad active user count
1.139.0
	Semaphore lock on the video file removed once the video fiel is downloaded using Video Player or Video Download StandAloneApp. 
1.141.0
	Multi-language support: corrections made for some of the strings - incorrect, missing or misspelled translations.
1.142.0
	Webpreviewer screen doesn't display the message fully when the host is in sleep mode; issue solved.
1.143.0
	# In the java console all the 3 zoom options are enabled when the client and host are in same resolution; issue fixed
	# Localization language menu items in GUI Localization option gets disabled in some scenarios; issue fixed.
	# Setting zoom options to Actual Size while switching back to window mode from fullscreen mode.
1.144.0
	Allowing CTRL and ALT key combination to be send to host via Physical keboard itself.
1.145.0
	#After the web session logout the jviewer launches with the blank screen(singleport app enabled).
1.146.0
	Added completion code to the response when an Invalid IPMI command is send to the BMC.
1.147.0
	# Info dialog message for Service Configuration changes being blocked by VMedia and Video Record Settings dialog; issue fixed.
	# Proper message has been added when trying floppy redirection and floppy srevice has been disabled.
	# Keybord shortcut mnemonic added fro Active Users menu.
1.148.0
	Added Full Keyboard Support Option.
1.149.0
	BSOD: Download button is scrambled if user scrolls BSOD page; issue fixed
1.151.0
	#Client machine IP is displayed as 0.0.0.0 when console session is launched with ipv6
	address, issue fixed.when singleport feature is enabled.
1.152.0
	#Manage video in standalone java:Error message displays when the user closes the video player.
1.153.0
	In java console extra frame appears when we use remote desktop connection from another client; issue fixed
1.154.0
	Localization faliure causes JViewer to crash; issue fixed
1.155.0
	In English Client to German Host Physical keyboard layout translation, Numpad .(dot) triggers ,(comma) in the host; issue fixed
1.156.0
	#download and player app hangs some times when playing/downloading videofile
1.157.0
	#Message dialog is dispalyed when the cuncurrent session is closed and full permission is received by a session with partial permission.
	#Minimum size set for the Video record settings dialog
	#Handled MissingResourceException while localizing SOC menus 
1.161.0
	#JViewer hangs when trying to close teh application, after a video storage location out of space error occurs, while recording; issue fixed.
1.163.0
	# Title argument added to provide custom titles to JViewer application if required.
	# Replaced the use of word JViewer in the application GUI related strings with teh custom title, if specified. 
	# Added support to avoid unknown parameters and continue lunching teh application normally.
	# Unable to click the ok button of popup box in JViewer, while changing the service as enable or disable; issue fixed
1.165.0
	# Auto resizing KVM Client window feature modified to work based on OEM Specific Feature, byte argument value.
	# Feature can be configured in PRJ under KVM feature.
1.166.0
	# Physical keyboard feature not working properly with Auto Detect keyboard layout; issue fixed
1.167.0
	Sign script moved into Jviewer_signkey pkg
1.168.0
	# Increased JViewer maximum session count.
	# Included a menu item Request Full Permission to request full permission, at any point of time.
1.169.0
	# Added web support to get OEM specific feature status, as a JViewer argument in jnlp file.
	# Enabled OEM specific feature support in JViewer StandAlone application.
1.170.0
	# Stopping active VMedia redirections, while giving away master privilege to another session.
	# Handled Master previlege request while closing JViewer
1.171.0
	#Handled exception cases :Issue  After the web session logout the jviewer launches with the blank screen.
1.172.0
	# Missing resource exception thrown while web logout fixed.
1.173.0
	# Stand Allone Application showing VMedia configuration error; issue fixed.
	# Softkeyboard mehu not enab;ed while disabling Auto keyboard layout.
1.177.0
	# Display proper message when virtual media instance count change.
	# Fit to Host Resolution zoom option does not work during system reboot; issue fixed.

1.181.0
	# Added message dialogs to be shown when image files are redirected in Read-Only mode.
	# Added message dialogs to be shown when already opened iamge file with read-write access is being redirected via VMedia.
	# When Client and Host screen resolutions are same, and zoom option selected is "Fit to Host Resolution", JViewer is not showing scroll bars; issue fixed.
1.183.0
	Video Record Settings dialog modified to retain the input values (storage path & time duration), even if closed and reopened.
1.184.0
	Soft Power Off Power Control command number modified to make it compatible with IPMI spec.

1.186.0
	VMedia dialog modified as a modeless dialog.
1.187.0
	Modified Host Monitor lock feature and added it to JViewer common code.
1.188.0
	Information dialog shown in JViewer if VMedia configuration changed from Web UI.
1.189.0
	Fixed screen data missing issue in JViewer view while zoom option selected in Fit to Host Resolution
1.190.0
	Fix for Other Mouse Mode issue in the RAID controller configuration window.
1.191.0
	Reverted changes done to change minimum frame size as 300 X 200. (Commit rivision : 37635) 
1.192.0
	Fit to Host Resolution zoom option implementation modified.
1.193.0
	The Start Video Record menuitem will remain enabled , if the Video Record Settings are provided once.
1.194.0
	Removed references to SystemTimeMointor thread.
1.195.0
	# Added connection status label in VMedia dialog to show the host device instance to which the media is being redirected.
	# Avoid multiple VMedia dialogs being opened using VMedia toolbar buttons, or Virtual media menu.
	# VMedia dialog contents localized when the GUI language is changed, while the VMedia dialog is already open.

1.197.0
	Fixed the issues with Fit to Host resolution zoom option, when the Auto Resize JViewer window configuration is not selected in PRJ.   
1.199.0
	Full Access permission given to the requesting session, on KVM sharing request timeout.
1.200.0
	Power Option menus getting enabled when a power OFF event occurs, in a Partial privileged KVM session; issue fixed
1.201.0
	JViewer window restores from minimized state, when the zoom option is Fit to Host, and a resolution change occurs in host; issue fixed.
1.202.0
	Resetting zoom options on JViewer focus lost and focus gain.
1.204.0
	Autokeyboard layout selected by default.
1.205.0
	VMedia dialog Device label displays device ID as Roman numerals.
1.206.0
	Support to open NRG CD Image files added in VMedia dialog.
1.207.0
	# ALT Gr + 102 key in Software keyboard displays proper charecter
1.208.0
	#Added feature to open device always read,write only
1.209.0
	# CD Images (NRG & ISO) can be browsed for redirection in VMedia dialog using same file filter.
	# Roman numerals in VMedia dialog Device label generated using UTF characters.
	# Fixed isseus regarding Dead Circumflex key in French to German keyprocessor.
	# Strings related to CD modified ans CD/DVD Media.
1.210.0
	#Add/Remove power related menu depends on user previlige.
1.211.0
	# Information dialog displays user name and IP of previous master, when a full permission request is granted to the KVM session with partial permission,
	and no master session is present; issue fixed.
	# Added proper pop-up message for image redirection.
1.212.0
	# JViewer mouse will resync to initial position on resolution change, only if the mouse mode is Relative.
	# Fixed NullPoinertException when launching Video Download and Vidoe Player apps.
1.213.0
	IMA file support added for Floppy redirection using VMedia.
1.214.0
	The Keyboard/Mouse Encryption menu item will not be available in the Options menu in JViewer single port application launched to connect via SSL socket.
1.215.0
	useSSl variable getter method added in JViewer.
1.216.0
	Physical keyboard layout selected in remote session configuration page will be selected in JViewer Stand Alone application also.

1.217.0
	# Separate menu items added to lock and unlock Host Display.
	# Toolbar button added to dipslay teh Host Display lock status, and to lock or unlock Host Display.
	# Resuming video redirection after pausing, does not work correctly in certain phases of POST; issue fixed.
1.218.0
	# Handled two more cases in Host Display Control status.
	# OEM Specific feature configurations listed under separate menu in PRJ
1.219.0
	Removed duplicate entry of "Context Menu" menuitem from Keyboard menu. 
1.220.0
	Resizable property of JViewer Frame removed for "Fit to Host Resolution" & "Fit to Client Resolution" zoom options.
1.221.0
	# Stopping VMedia redirection when master previlege is lost due to KVM Previlege request time out.
	# Adding KeyListener to JViewerView only if it is not already added.
1.222.0
	Enabled Debug logging in KVM CLinet.
1.223.0
	Multiple KVM redirection sessions to same BMC denied only when launched from same client, with same user name, and same user domain.
1.224.0
	Autodetect keyboard layout issues with Linux client fixed.
1.225.0
	Closing JViewer on KVM session time out.
1.226.0
	Added support to terminate KVM and VMedia redirection sessions using IPMI command.
1.227.0
	# Added function to set physical keyboard layout language in configuration file.
1.228.0
	# Set physical keyboard layout language in configuration file only iffeature enabled in PRJ.	
1.230.0
	# Updating the Vmedia device free slot in JViewer dynamically based on the Server connection accepted.
1.231.0
	# Number of VMedia free slots updated in JViewer.
1.232.0
	# Disable all the menu itmes, except Exit and Help, in JViewer while waiting for KVM permission.
1.233.0
	# VMedia dialog (if open) will be closed while giving away KVM full access permission.
	# IOException while writting to JViewer Debug log fixed.
1.234.0
	Adding mouse listener to JViewer toolbar icons only if its not already added.
1.235.0
	# Changing Mouse mode makes the JViewer stuck; issue fixed.
	# KVM permission response dialog in master session will not close, if the requesting session gets closed, while KVM sharing; issue fixed.
1.237.0
	Added new IVTP command packet for getting the Vmedia device free slot count in JViewer.
1.238.0
	# Fix : Power button in tool bar works in Partial access client.
	# Fix : Invoking Full Permission after master dead is printing Full Virtual Console access granted by null user with IP address null.
	# Fix : Invoking Full Permission after master dead is not enabling power button if host is power off.
1.239.0
	Fixed build error.
1.240.0
	# Video Pause, Resume, and Refersh menu items disabled after Full Permission Request; issue fixed.
	# Exception in JViewer when changing VMedia instance count; issue fixed.
	# File handles on image files closed properly in VMedia close due to Vmedia configuration change.
	# Video Record Start and Video record settings menu items are enabled after video recording, only if video rediection is not paused.
1.241.0
	Japanese software keyboard layout modified.
1.242.0
	Japanese software keyboard layout, key face characters modified using Unicode Character Set.
1.243.0
	KVM Client frame size update behaviour on Fit to Host zoom option modified.
1.244.0
	Updated active user list display to show proper information.
1.245.0
	Fixed SSL exception in standalone app rpc calls by setting system propery for https.protocol as SSLv3.
1.246.0
	JViewer Active User list not updating when switching from full screen to window mode; issue fixed.
1.247.0
	StandAloneConnection dialog will wait for user to select between KVM or Manage Video.
1.248.0
	Added code to save Auto Detect as host physical keyboard in configuration file
1.249.0
	#StandAlone JViewer App hangs if you enter an invalid Host IP address; issue fixed
	#Stand Alone App Manage Video option not able to download the recorded video; issue fixed
1.250.0
	Mouse synchronisation fails in Relative mouse mode when Show Cursor is enabled for the very first time; issue fixed.
1.251.0
	In Relative mouse mode, mouse movement is not sent to host, when mose is moved over the view, with a menu expanded in JViewer; issue fixed.
1.252.0
	# Zoom In & Zoom Out menu items become disabled when the Fit to Host zoom option can not be rendered; issue fixed.
	# Keyboard and mouse packets are not sent if the KVM session is have partial permission or if the server is powered OFF.
	# Select Next Master dialog trying to send null data when being disposed; issue fixed.
1.253.0
	Host Monitor Lock menu items should be grayed out if feature is disabled; issue fixed.
1.254.0
	Video is not displayed fully in Web previewew screen as in java console; issue fixed.
1.255.0
	Capture screen implementation modified to capture the screen at the moment of triggering the event.
1.256.0
	# JViewer slave session displays two pop up dialogs when Partial access permission is received; issue fixed
	# Soft Keyboard - Finnish - Symbol Mismatch fixed.
1257.0
	JViewer Mouse movement is not working in high resolution mode 1920x1200; issue fixed.
1258.0
	GUI Localization : String changes.
1.259.0
	# Physical Hard Disk Redirection fails; issue fixed
	# Jviewer- Getting wrong response message in send IPMI command; issue fixed
1.260.0
	# Hotkeys:Not able to view all 20 short-cut key, issue fixed.
1.261.0
	Video Socket Error comes when the VMedia device instance count is modified from 0 to non-zero; issue fixed.




**/


package com.ami.kvm.jviewer.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.omg.CORBA.LocalObject;

import com.ami.iusb.CDROMRedir;
import com.ami.iusb.FloppyRedir;
import com.ami.iusb.HarddiskRedir;
import com.ami.iusb.IUSBRedirSession;
import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.common.ISOCApp;
import com.ami.kvm.jviewer.common.ISOCCreateBuffer;
import com.ami.kvm.jviewer.common.ISOCFrameHdr;
import com.ami.kvm.jviewer.common.ISOCKvmClient;
import com.ami.kvm.jviewer.common.ISOCManager;
import com.ami.kvm.jviewer.hid.KeyProcessor;
import com.ami.kvm.jviewer.hid.USBKeyProcessorEnglish;
import com.ami.kvm.jviewer.hid.USBKeyboardRep;
import com.ami.kvm.jviewer.hid.USBMouseRep;
import com.ami.kvm.jviewer.jvvideo.JVVideo;
import com.ami.kvm.jviewer.kvmpkts.CfgBandwidth;
import com.ami.kvm.jviewer.kvmpkts.IVTPPktHdr;
import com.ami.kvm.jviewer.kvmpkts.KMCrypt;
import com.ami.kvm.jviewer.kvmpkts.KVMClient;
import com.ami.kvm.jviewer.kvmpkts.Mousecaliberation;
import com.ami.kvm.jviewer.videorecord.VideoRecordApp;



/**
 * JViewer frame module class.
 */
public class JViewerApp {

	private static JViewerApp m_rcApp = new JViewerApp();
	private JVFrame m_frame;
	private KVMClient m_KVMClnt;
	private ISOCKvmClient sockvmclient;
	private VideoRecordApp videorecordapp;
	private JViewerView m_view;
	private String m_encToken;
	private boolean m_wndMode = true;
	private WindowFrame m_wndFrame;
	private FSFrame m_fsFrame;
	private byte Led_status = 0; // Keyboard LED state backup
	private String m_webSession_token;// Added for WebSession
	private String m_session_token;
	private int m_session_token_type = 0; // Web token as default
	private byte[] m_serverIP;
	private int m_serverPort;
	private int m_RedirectionState = REDIR_STOPPED;
	private boolean m_bUseSSL = false;
	private int m_cdPort;
	private int m_fdPort;
	private int m_hdPort;
	private int m_cdNum = 1;
	private int m_fdNum = 1;
	private int m_hdNum = 1;
	private int freeCDNum = 0;
	private int freeFDNum = 0;
	private int freeHDNum = 0;
	private int m_cdStatus;
	private int m_fdStatus;
	private int m_hdStatus;
	private boolean m_bVMUseSSL = false;
	private IUSBRedirSession m_IUSBSession;
	private USBKeyboardRep m_USBKeyRep;	

	private KeyProcessor keyprocessor = null;
	
	private ISOCApp soc_App;
	private ISOCFrameHdr socframeHdr;
	private static ISOCManager soc_manager;
	private JLabel label;
	private JDialog dialog;
	private VideoRecord m_videorecord;
	private Mousecaliberation Mousecaliberation;
	private String Message;
	private String serverIP;
	private SoftKeyboard softKeyboard;
	private AutoBWDlg m_autoBWDlg;
	public static byte WEB_PREVIEWER_CAPTURE_SUCCESS = 0;
	public static byte WEB_PREVIEWER_CAPTURE_FAILURE = -1;
	public static byte WEB_PREVIEWER_CAPTURE_IN_PROGRESS = -2;
	public static byte WEB_PREVIEWER_CONNECT_FAILURE = -3;
	public static byte WEB_PREVIEWER_INVALID_SERVERIP = -4;
	public static byte WEB_PREVIEWER_HOST_POWER_OFF = -5;
	public static byte m_webPreviewer_cap_status = WEB_PREVIEWER_CAPTURE_IN_PROGRESS;
	private KVMShareDialog kVMDialog =  null;
	private int m_zoomSliderValue;// Zoom slider value
	private boolean m_userPause = false;//Flag to check whether video redirection is paused by user.
	public static final int MAX_IMAGE_PATH_COUNT = 5;
	public static String Imagepath_CD[][];
	public static String Imagepath_Floppy[][];
	public static String Imagepath_Harddsik[][];
	public static boolean showCursor = false;
	public static int REDIR_STOPPED = 0x00;
	public static int REDIR_STARTING = 0x01;
	public static int REDIR_STARTED = 0x02;
	public static int REDIR_STOPPING = 0x03;
	public static int REDIR_PAUSING = 0x04;
	public static final int NUMLOCK = 0x01;
	public static final int CAPSLOCK = 0x02;
	public static final int SCROLLLOCK = 0x04;
	public boolean m_refresh = false;
	public vMediaDialog m_mediaDlg = null;
	public JDialog mediaDlg = null;
	public JVVideo vidClnt;
	public ISOCCreateBuffer prepare_buf;
	public static final byte OEM_FIT_TO_HOST_SCREEN = 0x01;
	public static final byte OEM_REDIR_RD_WR_MODE = 0x02;
	public static final byte OEM_SHOW_HOST_CURSOR_DEFAULT= 0x04;
	public static final byte OEM_SET_PHYSICAL_KBD_LANG= 0x08;
	
	public static final byte SERVER_POWER_ON = 1;
	public static final byte SERVER_POWER_OFF = 0;

	public static final byte HOST_DISPLAY_UNLOCK = 0x00;
	public static final byte HOST_DISPLAY_LOCK = 0x01;
	public static final byte HOST_DISPLAY_UNLOCKED_AND_DISABLED = 0x02;
	public static final byte HOST_DISPLAY_LOCKED_AND_DISABLED = 0x03;

	private byte powerStatus;	

	private StandAloneConnectionDialog connectionDialog;
	private SinglePortKVM singlePortKvm;
	private AutoKeyboardLayout  autokeylayout = null;

	private String currentVersion = "1.261.0";
	private UserDefMacro userDefMacro = null;
	private AddMacro addMacro = null;
	private IPMICommandDialog ipmiDialog = null;
	private String zoomOption;
	private boolean fullKeyboardEnabled = false;
	private boolean fullPermissionRequest = false;
	private boolean renderFitToHost = true;
	private Hashtable<String, JDialog> responseDialogTable = null;
	public int currentSessionId = -1;

	private Object createObject(String className) {
		Object object = null;
		try {
			Class classDefinition = Class.forName(className);
			object = classDefinition.newInstance();
		} catch (InstantiationException e) {
			Debug.out.println(e);
		} catch (IllegalAccessException e) {
			Debug.out.println(e);
		} catch (ClassNotFoundException e) {
			Debug.out.println(e);
		}
		return object;
	}

	/**
	 * Get JViewer frame singleton instance.
	 *
	 * @return singleton instance of JViewer frame.
	 */
	public static JViewerApp getInstance() {
		return m_rcApp;
	}

	/**
	 * The constructor.
	 */
	private JViewerApp() {
		try{	
			soc_manager = (ISOCManager) createObject("com.ami.kvm.jviewer.soc.SOCManager");
			prepare_buf = soc_manager.getSOCCreateBuffer();
			soc_App = soc_manager.getSOCApp();
			socframeHdr = soc_manager.getSOCFramehdr();
		}catch(NullPointerException ne){
			Debug.out.println(ne);
			if(JViewer.isStandAloneApp()){
				JOptionPane.showMessageDialog(m_frame, LocaleStrings.getString("D_1_JVAPP"), LocaleStrings.getString("D_2_JVAPP"), JOptionPane.ERROR_MESSAGE);
				JViewer.exit(0);
			}
		}
		// construct JViewer view.
        m_view = new JViewerView(JViewerView.DEFAULT_VIEW_WIDTH, JViewerView.DEFAULT_VIEW_HEIGHT);

		// Don't construct GUI components for WebPreviewer and until the StandAloneApp
		// connection is success.This is done to postpone the UI creation until the 
		//language settings is seected from the StandAloneConnection dialog.
		if(!JViewer.isStandAloneApp() && !JViewer.isWebPreviewer() && !JViewer.isBSODViewer())
			constructUI();

	}

	/**
	 * Construct the JViewer user interface.
	 */
	public void constructUI(){
		m_wndFrame = new WindowFrame();
		if(!JViewer.isplayerapp() && !JViewer.isdownloadapp()){
			m_fsFrame = new FSFrame();
			m_USBKeyRep = m_view.getM_USBKeyRep();
			m_zoomSliderValue = 100;
			if(!JViewer.isVMediaEnabled()){
				m_wndFrame.getWindowMenu().getMenuBar().remove(m_wndFrame.getWindowMenu().
						getMenu(JVMenu.MEDIA));
				m_fsFrame.getM_menuBar().remove(m_fsFrame.getM_menuBar().getFSMenu().
						getMenu(JVMenu.MEDIA));
				m_wndFrame.toolbar.removeVMediaButtons();
			}
			if(!JViewer.isPowerPrivEnabled()){
				m_wndFrame.getWindowMenu().getMenuBar().remove(m_wndFrame.getWindowMenu().
						getMenu(JVMenu.POWER_CONTROL));
				m_fsFrame.getM_menuBar().remove(m_fsFrame.getM_menuBar().getFSMenu().
						getMenu(JVMenu.POWER_CONTROL));
			}
		}
	}
	public void  create_IUSBSession() {
		m_IUSBSession = new IUSBRedirSession();
	}

	/**
	 * Get JViewer view.
	 *
	 * @return JViewer view.
	 */
	public JViewerView getRCView() {
		return m_view;
	}

	/**
	 * Get the USBsession Instance
	 * @return
	 */
	public IUSBRedirSession getUSBRedirSession() {
		return m_IUSBSession;
	}

	/**
	 * Get KVM client.
	 *
	 * @return KVMClient
	 */
	public KVMClient getKVMClient() {
		return m_KVMClnt;
	}

	/**
	 * Get Default Menu.
	 *
	 * @return menu handle
	 */
	public JVMenu getJVMenu() {
		if (m_wndMode)
			return (((JVFrame) m_wndFrame).getMenu());
		else
			return (((JVFrame) m_fsFrame).getMenu());
	}

	/**
	 * Get main window.
	 *
	 * @return main window
	 */
	public JVFrame getMainWindow() {
		if (m_wndMode)
			return ((JVFrame) m_wndFrame);
		else
			return ((JVFrame) m_fsFrame);
	}

	/**
	 * Get encryption token.
	 *
	 * @return encryption token.
	 */
	public String getToken() {
		return m_encToken;
	}

	/**
	 * Get session token.
	 *
	 * @return session token.
	 */
	public String getSessionToken() {
		return m_session_token;
	}

	/**
	 * Get session token type.
	 *
	 * @return session token type.
	 */
	public int getSessionTokenType() {
		return m_session_token_type;
	}

	/**
	 * Set status.
	 *
	 * @param status
	 *            message.
	 */
	public void setStatus(String msg) {
		m_frame.setStatus(msg);
	}

	/**
	 * Reset status.
	 */
	public void resetStatus() {
		m_frame.resetStatus();
	}

	/**
	 * Return the Windows is in fullscreen or not
	 * @return
	 */
	public boolean isFullScreenMode() {
		return !m_wndMode;
	}

	/**
	 * Refresh application window title with new values
	 *
	 * @param additional
	 *            label
	 */
	public void refreshAppWndLabel() {
		try {
			m_frame.refreshTitle();
		} catch (Exception e) {
			Debug.out.println("Not able to refresh the title");
			Debug.out.println(e);
		}
	}

	/**
	 * Set application window title
	 *
	 * @param additional
	 *            label
	 */
	public void setAppWndLabel(String label) {
		try {
			m_frame.setWndLabel(label);
		} catch (Exception e) {
			Debug.out.println("Not able to set the Window Label");
			Debug.out.println(e);
		}
	}

	/**
	 * Return the redirectio state
	 *
	 * @return
	 */
	public int GetRedirectionState() {
		return m_RedirectionState;
	}
	/**
	 * Set the kvm redirection status.
	 * @param status - any one among (REDIR_STOPPED, REDIR_STARTING, REDIR_STARTED, REDIR_STOPPING, REDIR_PAUSING). 
	 */
	public void setRedirectionStatus(int status){
		m_RedirectionState = status;
	}

	/**
	 * @return the powerStatus
	 */
	public byte getPowerStatus() {
		return powerStatus;
	}

	public void Ondisplayvideo(String ip,String webPort, String sessionCookies, int secureConnect) {

		//Assigning the window frame to the common frame object
		m_frame = m_wndFrame;
		//Setting the scroll paneview Enable/Disable the Viewer pane display
		m_wndFrame.attachView();
		//Attaching the created panel to the frame Basedd on the application standalone or Multiviewer
		attachFrame();
		OnVideoRecordStartRedirection( ip, webPort, sessionCookies, secureConnect);
	}

	public void initilizeJVVideo() {
		// Creating the JVVideo object for process the video data rendering
		vidClnt = new JVVideo();
		setVidClnt(vidClnt); // Setting the Video client object for accesing in
								// SOC package
		sockvmclient.SetVidoclnt(vidClnt);
	}
	/**
	 * Start video redirection request handler
	 * @param secureconnect
	 */
	public void OnVideoRecordStartRedirection(String ip, String webPort,String sessionCookies, int secureConnect) {
		Debug.out.println("OnVideoRecordStartRedirection");
		m_RedirectionState = REDIR_STARTING;

		m_KVMClnt = new KVMClient(m_serverIP, m_serverPort, vidClnt, m_bUseSSL);
		sockvmclient = JViewerApp.getSoc_manager().getSOCKvmClient();

		sockvmclient.SetKVMClient(m_KVMClnt);
		sockvmclient.SOCKVM_reader();
		initilizeJVVideo();

		if(JViewer.isdownloadapp())
			m_videorecord = new VideoRecord();

		//Invoke the Video Record Object to save the video in client
		videorecordapp = new VideoRecordApp();

		JVFrame.setServerIP(m_serverIP, m_RedirectionState);
		// start the video record
		if (-1 == videorecordapp.startVideorecordRedirection( ip, webPort, secureConnect, sessionCookies)) {
			JViewerApp.getInstance().getM_frame().windowClosed();

		}
	}

	/**
	 * Launch the JViewer in Stand Alone Mode
	 * @param hostIP - IP address of the host.
	 * @param username - log in id required for user authentication.
	 * @param password - password required for user authentication.	 * 
	 */
	public void onLaunchStandAloneApp(String hostIP, int webPort, String username, String password){
		connectionDialog = new StandAloneConnectionDialog(null, hostIP, webPort, username, password);		
	}

	/**
	 * @return the connectionDialog
	 */
	public StandAloneConnectionDialog getConnectionDialog() {
		return connectionDialog;
	}

	/**
	 * @return the singlePortKvm
	 */
	public SinglePortKVM getSinglePortKvm() {
		return singlePortKvm;
	}

	/**
	 * Connect to server request handler.
	 */
	public void OnConnectToServer(String serverIP, int serverPort,
			String token, int token_type, boolean bUseSSL, boolean bVMUseSSL,
			int cdserver_port, int fdserver_port,int hdserver_port,byte num_cd,byte num_fd,byte num_hd,
			int cdstatus,int fdstatus,int hdstatus, String webSessionTok,int webSecPort) {

		m_serverIP = JViewer.getServerIP(serverIP);
		m_serverPort = serverPort;
		m_encToken = token;
		m_session_token = token;
		m_session_token_type = token_type;
		m_webSession_token = webSessionTok;
		m_frame = m_wndFrame;
		m_wndMode = true;
		m_bUseSSL = bUseSSL;
		if(JViewer.isSinglePortEnabled())
		{
			singlePortKvm = new SinglePortKVM( serverIP, serverPort, webSecPort,m_bUseSSL);
			singlePortKvm.startConnect();

		}
		else
		{
			m_cdPort = cdserver_port;
			m_fdPort = fdserver_port;
			m_hdPort = hdserver_port;
		}
		setM_cdNum(num_cd);
		setM_fdNum(num_fd);
		setM_hdNum(num_hd);
		setFreeCDNum(num_cd);
		setFreeFDNum(num_fd);
		setFreeHDNum(num_hd);
		m_cdStatus = cdstatus;
		m_fdStatus = fdstatus;
		m_hdStatus = hdstatus;
		m_bVMUseSSL = bVMUseSSL;
		m_wndFrame.attachView();
		attachFrame();

		// update menu state
		JVMenu menu = m_frame.getMenu();
		menu.notifyMenuStateEnable(JVMenu.VIDEO_PAUSE_REDIRECTION, false);
		menu.notifyMenuStateEnable(JVMenu.VIDEO_RESUME_REDIRECTION, true);
		menu.notifyMenuStateSelected(JVMenu.VIDEO_FULL_SCREEN, false);
		menu.notifyMenuStateEnable(JVMenu.KEYBOARD_ADD_HOTKEYS,false);
		// start redirection
		OnVideoStartRedirection();
		if((JViewer.getOEMFeatureStatus() & OEM_FIT_TO_HOST_SCREEN) == OEM_FIT_TO_HOST_SCREEN){
			setZoomOption(JVMenu.FIT_TO_HOST_RES);
		}
	}
	/*
	 * Below function used by webpreviewer applet. 
	 * WebPreviewer need connection to start the full video capture.
	 */
	public void OnConnectToServer(String serverIP, int serverPort, int webSecPort, String token, boolean bUseSSL, 
			String webSessionTok) {
		m_serverIP = JViewer.getServerIP(serverIP);
		m_serverPort = serverPort;
		m_encToken = token;
		m_session_token = token;
		m_webSession_token = webSessionTok;
		m_bUseSSL = bUseSSL;
		if(JViewer.isSinglePortEnabled()){
			singlePortKvm = new SinglePortKVM( serverIP, m_serverPort,webSecPort,bUseSSL);
			singlePortKvm.startConnect();
		}
		// start redirection
		OnVideoStartRedirection();
	}

	/*
	 * Initiates redirection of BSODViewer applet.
	 */
	public void OnConnectToServer(byte[] serverIP) {
		m_serverIP = serverIP;
		// start redirection
		OnVideoStartRedirection();
	}

	/**
	 * 
	 */
	private void attachFrame() {
		if(JViewer.isStandalone()) {
			if( !isM_wndMode()) {
				JViewer.getMainFrame().dispose();
				if(!JViewer.getMainFrame().isDisplayable())
					JViewer.getMainFrame().setUndecorated(true);
			} else {
				if(!JViewer.getMainFrame().isDisplayable())
					JViewer.getMainFrame().setUndecorated(false);
			}	
			JViewer.getMainFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
			JViewer.getMainFrame().setContentPane(m_frame);
			JViewer.getMainFrame().setSize(JViewer.MIN_FRAME_WIDTH, JViewer.MIN_FRAME_HEIGHT);
			JViewer.getMainFrame().setVisible(true);
			JViewerApp.getInstance().getRCView().requestFocus(); 
		} else {
			JViewer.getMainPane().add(m_frame);
		}
	}

	/**
	 * 
	 */
	private void detachFrame() {
		if(JViewer.isStandalone()) {
			// Added for JInternalFrame
			JViewer.getMainFrame().getContentPane().remove(m_frame);
			JViewer.getMainFrame().dispose();
		}
	}
	
	/**
	 * Start video redirection request handler
	 */
	public void OnVideoStartRedirection() {
		Debug.out.println("OnVideoStartRedirection");
		m_RedirectionState = REDIR_STARTING;
		vidClnt = new JVVideo();
		setVidClnt(vidClnt);
		m_KVMClnt = new KVMClient(m_serverIP, m_serverPort, vidClnt, m_bUseSSL);
		sockvmclient = JViewerApp.getSoc_manager().getSOCKvmClient();
		sockvmclient.SetVidoclnt(vidClnt);
		sockvmclient.SetKVMClient(m_KVMClnt);
		sockvmclient.SOCKVM_reader();

		//solved Java console button disable when open Java console continuely
		if(!JViewer.isWebPreviewer() && !JViewer.isBSODViewer()) {
			if (!m_KVMClnt.redirection()){
				JViewerApp.getInstance().getM_wndFrame().toolbar.changeMacrowsStatus(false);
				JViewerApp.getInstance().getM_wndFrame().getM_status().enableStatusBar(false);
			}
		}

		// start redirection
		if(!JViewer.isBSODViewer()){
			if (-1 == m_KVMClnt.startRedirection()){
				m_RedirectionState = REDIR_STOPPED;
				if(!(JViewer.isWebPreviewer() || JViewer.isBSODViewer())) {
					JOptionPane.showMessageDialog(m_frame, LocaleStrings.getString("D_3_JVAPP"), 
							LocaleStrings.getString("D_4_JVAPP"), JOptionPane.ERROR_MESSAGE);
					JViewerApp.getInstance().getM_frame().windowClosed();
				} else{
					JViewerApp.getInstance().setWebPreviewerCaptureStatus(WEB_PREVIEWER_CONNECT_FAILURE);
				}
			}
		}
		if(!JViewer.isWebPreviewer() && !JViewer.isBSODViewer())
		{

		JVFrame.setServerIP(m_serverIP, m_RedirectionState);
		//enable auto detect keyboard layout by default. 
		if(JViewer.getKeyboardLayout().equalsIgnoreCase(JViewer.AUTO_DETECT_KEYBOARD))
			onAutoKeyboardLayout(true,false);
		m_view.addKeyListener();
		// update menu state
		JVMenu menu = m_frame.getMenu();
		menu.notifyMenuStateEnable(JVMenu.VIDEO_PAUSE_REDIRECTION, true);
		menu.notifyMenuStateEnable(JVMenu.VIDEO_RESUME_REDIRECTION, false);
		m_RedirectionState = REDIR_STARTED;
		if (!m_KVMClnt.redirection()){
			String commonExceptionList[] = {JVMenu.VIDEO_EXIT,JVMenu.HELP_ABOUT_RCONSOLE,JVMenu.VIDEO_FULL_SCREEN};
			String exceptionList[]= getExceptionMenuList(commonExceptionList);
			m_frame.getMenu().enableMenu(exceptionList,true, true);
			JViewerApp.getInstance().getM_wndFrame().toolbar.changeMacrowsStatus(false);
			JViewerApp.getInstance().getM_wndFrame().getM_status().enableStatusBar(false);
		}
		//start the system time monitor thread.
			m_frame.getMenu().SetMenuSelected(JVMenu.OPTIONS_GUI_LANGUAGE_LOCALE+JViewer.getLanguage(), true);
		}			
	}

	/**
	 * Stop video redirection request handler
	 */
	public void OnVideoStopRedirection() {
		Debug.out.println("OnVideoStopRedirection");
		if(m_RedirectionState == REDIR_STOPPED)
			return;
		m_RedirectionState = REDIR_STOPPING;
		m_KVMClnt.Stop_Cmd_Redirection();
		if(JViewer.isjviewerapp() || JViewer.isStandAloneApp())
		{
			JVFrame.setServerIP(null, m_RedirectionState);
			OnUSBMouseSyncCursor(false);
			// update menu state
			m_frame.getMenu().notifyMenuStateEnable(JVMenu.VIDEO_PAUSE_REDIRECTION,	false);
			m_frame.getMenu().notifyMenuStateEnable(JVMenu.VIDEO_RESUME_REDIRECTION, true);

			if (isFullScreenMode()) {
				OnVideoFullScreen(false);
			}
		}
		if(!JViewer.isdownloadapp() && !JViewer.isplayerapp()){
			if(JViewer.isSinglePortEnabled() && JViewer.isStandAloneApp())
				getConnectionDialog().logoutWebSession();
			m_KVMClnt.stopRedirection();
			m_view.removeKMListener();
			m_RedirectionState = REDIR_STOPPED;
		}
	}

	/**
	 * @return the m_userPause
	 */
	public boolean isM_userPause() {
		return m_userPause;
	}

	/**
	 * @param m_userPause the m_userPause to set
	 */
	public void setM_userPause(boolean m_userPause) {
		this.m_userPause = m_userPause;
	}

	private void changeMenuItemsStatusOnPauseResume(JVMenu menu, boolean status) {
		Set<String>  set =JVMenu.m_menuItems_setenabled.keySet();
		Iterator<String> itr = set.iterator();
		String str;
		while (itr.hasNext()) {
			str = itr.next();
			if(JVMenu.VIDEO_RESUME_REDIRECTION == str ) {
				menu.notifyMenuStateEnable(str,!status);
			}
			else if(JVMenu.VIDEO_CAPTURE_SCREEN == str || JVMenu.VIDEO_FULL_SCREEN == str ||
					JVMenu.VIDEO_EXIT == str ||  JVMenu.HELP_ABOUT_RCONSOLE == str ||
					str.startsWith(JVMenu.OPTIONS_GUI_LANGUAGE_LOCALE))
			{
				continue;
			}
			else if(JVMenu.VIDEO_RECORD_START == str
					|| JVMenu.VIDEO_RECORD_SETTINGS ==str
					|| JVMenu.VIDEO_RECORD_STOP == str)  {


				//Checks whether video redirection pause is user initiated.
				if(JViewerApp.getInstance().getMainWindow().getMenu().getMenuItem(JVMenu.VIDEO_RECORD_STOP).isEnabled()&& isM_userPause()) {

					JViewerApp.getInstance().getM_videorecord().OnVideoRecordStop();
					InfoDialog.showDialog(m_frame, LocaleStrings.getString("D_5_JVAPP"),
							LocaleStrings.getString("D_6_JVAPP"),
							InfoDialog.ERROR_DIALOG);
					menu.notifyMenuStateEnable(JVMenu.VIDEO_RECORD_STOP,false);
				}
				else {
					//if Video recording is in progress don't change menu status.
					if(VideoRecord.Recording_Started || VideoRecord.Record_Processing)
						continue;
					//Change the state of Video Record Start menu, only if it is enabled, setting menu is always updated in this case. 
					else if(JVMenu.VIDEO_RECORD_SETTINGS == str && 
							JViewerApp.getInstance().getMainWindow().getMenu().getMenuItem(JVMenu.VIDEO_RECORD_START).isEnabled()){
						menu.notifyMenuStateEnable(str,status);
					}
					else if (JVMenu.VIDEO_RECORD_SETTINGS == str) {
						menu.notifyMenuStateEnable(str,status);
					}
				}
				continue;
			}
			else if(JVMenu.CALIBRATEMOUSETHRESHOLD ==str) {

				if(JViewerApp.getInstance().getM_view().m_USBMouseMode == USBMouseRep.RELATIVE_MOUSE_MODE){
					menu.notifyMenuStateEnable(str,status);
				}
				else{
					menu.notifyMenuStateEnable(str,false);
				}
				continue;
			}
			else if(JVMenu.VIDEO_HOST_DISPLAY_UNLOCK == str || JVMenu.VIDEO_HOST_DISPLAY_LOCK == str){
				if(status){//On resume, set the current status of host lock
					changeHostDisplayLockStatus(getKVMClient().getHostLockStatus());
				}
				else{// On pause, disable the menu item.
					menu.getMenuItem(str).setEnabled(status);
				}
				continue;
			}

			// Maintane the power menu status on pause and resume.
			else if(JVMenu.POWER_ON_SERVER == str  && powerStatus == SERVER_POWER_ON){
				// If power status is ON then we don't need to enable the POWER ON SERVER menu item.
				// When power status is OFF, pause and resume operations won't happen. So there is no need
				//to handle that case.

				if(status)
					continue;
			}
			else {
				if(status){
					try{
						if(menu.getMenuItem(str) != null && menu.getMenuEnable(str) != null)
							menu.getMenuItem(str).setEnabled(menu.getMenuEnable(str));
					}catch(Exception e){
						Debug.out.println(e);
					}
				}
				else{
					try{
						if(menu.getMenuItem(str) != null)
							menu.getMenuItem(str).setEnabled(status);
					}catch(Exception e){
						Debug.out.println(e);
					}
				}
			}
		}
		if(status){
			if(KVMSharing.KVM_REQ_GIVEN == KVMSharing.KVM_REQ_PARTIAL)
				OnChangeMenuState_KVMPartial(menu, !status);
			//set zoom options
			getVidClnt().setZoomOptionStatus();
		}
	}

	/**
	 * Pause video redirection request handler
	 */
	public void OnVideoPauseRedirection() {
		if (m_RedirectionState != JViewerApp.REDIR_STARTED)
			return;
		
		Debug.out.println("OnVideoPauseRedirection");
		m_RedirectionState = REDIR_PAUSING;
		m_KVMClnt.pauseRedirection();
		if(!JViewer.isWebPreviewer())
		{
			JVFrame.setServerIP(null, m_RedirectionState);
			// Added for MultiViewer
			JViewerApp.getInstance().refreshAppWndLabel();
			
			// update menu state
			changeMenuItemsStatusOnPauseResume(m_frame.getMenu(), false);
			JViewerApp.getInstance().getM_wndFrame().toolbar.pauseBtn.setEnabled(false);
			JViewerApp.getInstance().getM_wndFrame().toolbar.pauseBtn.setToolTipText(LocaleStrings.getString("D_7_JVAPP"));
			JViewerApp.getInstance().getM_wndFrame().toolbar.playBtn.setEnabled(true);
			JViewerApp.getInstance().getM_wndFrame().toolbar.playBtn.setToolTipText(LocaleStrings.getString("D_8_JVAPP"));
			//To change status bar Macrows state
			JViewerApp.getInstance().getM_wndFrame().toolbar.changeMacrowsStatusOnPauseResume(false);
		}
		m_RedirectionState = REDIR_STOPPED;
	}

	/**
	 * Resume video redirection request handler
	 */
	public void OnVideoResumeRedirection() {
		Debug.out.println("OnVideoResume");

		m_RedirectionState = REDIR_STARTING;
		m_KVMClnt.resumeRedirection();
		if(!JViewer.isWebPreviewer())
		{
			JVFrame.setServerIP(m_serverIP, m_RedirectionState);// (Add Resume status)
			JViewerApp.getInstance().refreshAppWndLabel();
		
			setM_userPause(false);
			changeMenuItemsStatusOnPauseResume(m_frame.getMenu(), true);

			if (m_view.GetUSBMouseMode() == USBMouseRep.RELATIVE_MOUSE_MODE) {
				m_view.m_mouseListener.splitandsend(-socframeHdr.getwidth(),
						-socframeHdr.getheight(), true);
			}
			JViewerApp.getInstance().getM_wndFrame().toolbar.pauseBtn.setEnabled(true);
			JViewerApp.getInstance().getM_wndFrame().toolbar.pauseBtn.setToolTipText(LocaleStrings.getString("D_9_JVAPP"));
			JViewerApp.getInstance().getM_wndFrame().toolbar.playBtn.setEnabled(false);
			JViewerApp.getInstance().getM_wndFrame().toolbar.playBtn.setToolTipText(LocaleStrings.getString("D_10_JVAPP"));
		
			//To change status bar Macrows state
			JViewerApp.getInstance().getM_wndFrame().toolbar.changeMacrowsStatusOnPauseResume(true);
		}
		m_RedirectionState = REDIR_STARTED;
	}

	/**
	 * Refresh video request handler
	 */
	public void OnVideoRefreshRedirection() {
		Debug.out.println("OnVideoRefresh");
		if(JViewer.isjviewerapp() || JViewer.isStandAloneApp()){
		if (m_RedirectionState == REDIR_STOPPED) {
			if (m_KVMClnt.m_isBlank == true) {
				OnVideoResumeRedirection();
				OnVideoPauseRedirection();
			} else {
				m_refresh = true;
				OnVideoResumeRedirection();
			}
		} else {
			OnVideoPauseRedirection();
			OnVideoResumeRedirection();
		}
		}
	}

	/**
	 * Captures the current screen and saves it to the client system.
	 */
	public void onVideoCaptureScreen(){
		BufferedImage capturedFrame = null;
		//For jviewer and standalone app get image from view
		//write the currently redirected image into a file
		if(JViewer.isjviewerapp() || JViewer.isStandAloneApp()){
			capturedFrame = copyScreenBuffer(JViewerApp.getInstance().getRCView().getImage());
		}
		//For BSOD  app get image from buffer
		else{
			capturedFrame = copyScreenBuffer(JViewerApp.getInstance().getPrepare_buf().getM_image());
		}
		CaptureScreen captureScreen = new CaptureScreen(capturedFrame);
		captureScreen.start();
	}
	
	/**
	 * Create a copy of the content of the source BufferedImage.
	 * @param sourceImage - the source BufferedImage object to be copied.
	 * @return A copy of the source BufferedImage
	 */
	public BufferedImage copyScreenBuffer(BufferedImage sourceImage){
		ColorModel colorModel = sourceImage.getColorModel();
		boolean isAlphaPremultiplied = sourceImage.isAlphaPremultiplied();
		WritableRaster raster = sourceImage.copyData(null);
		return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
	}

	/**
	 * Full screen request handler
	 */
	public void OnVideoFullScreen(boolean state) {
		Debug.out.println("OnVideoFullScreen");

		if (m_wndMode) {
			m_wndFrame.detachView();
			detachFrame();
			m_frame = m_fsFrame;
			m_wndMode = false;
			m_fsFrame.attachView();
			attachFrame();
			m_fsFrame.showWindow();
			//Reload the user defined macro menus, when switching to full screen mode.
			HashMap<String, String> macroMap = null;
			if(addMacro != null)
				macroMap = getAddMacro().getMacroMap();
			if(macroMap != null){
				// Remove user defined macros.
				getAddMacro().removeMacroMenu(macroMap);
				// Add user defined macros.
				getAddMacro().addMacroMenu(macroMap);
			}

			/*Disable Keyboard, Mouse, and Keyboard Layout menus and the 
			 * Keyboard/Mouse encryption menu item while partial access 
			 * is given to the concurrent session in KVM sharing.
			 */
			if(KVMSharing.KVM_REQ_GIVEN == KVMSharing.KVM_REQ_PARTIAL )	
				OnChangeMenuState_KVMPartial(m_fsFrame.getMenu(), false);
			m_frame.getMenu().notifyMenuStateEnable(JVMenu.FIT_TO_CLIENT_RES, false);
			m_frame.getMenu().notifyMenuStateEnable(JVMenu.FIT_TO_HOST_RES, false);
		} else {
			m_fsFrame.detachView();
			detachFrame();
			m_fsFrame.hideWindow();
			m_frame = m_wndFrame;
			m_wndMode = true;
			m_wndFrame.attachView();
			attachFrame();
			//Reload the user defined macro menus, when switching to window mode.
			HashMap<String, String> macroMap = null;
			if(addMacro != null)
				macroMap = getAddMacro().getMacroMap();
			if(macroMap != null){
				// Remove user defined macros.
				getAddMacro().removeMacroMenu(macroMap);
				// Add user defined macros.
				getAddMacro().addMacroMenu(macroMap);
			}

			/*Disable Keyboard, Mouse, and Keyboard Layout menus and the 
			 * Keyboard/Mouse encryption menu item and disable the 
			 * Keyboard, Mouse, and Hotkey button on the toolbar,while partial 
			 * access is given to the concurrent session in KVM sharing.
			 */
			if(KVMSharing.KVM_REQ_GIVEN == KVMSharing.KVM_REQ_PARTIAL && !KVMShareDialog.isMasterSession){
				OnChangeMenuState_KVMPartial(m_wndFrame.getMenu(), false);
				getM_wndFrame().toolbar.OnChangeToolbarIconState_KVMPartial();
			}

			if(JViewerApp.getInstance().getRCView().GetUSBMouseMode() == USBMouseRep.OTHER_MOUSE_MODE){
				GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
				JViewer.getMainFrame().setMaximizedBounds(graphEnv.getMaximumWindowBounds());
				JViewer.getMainFrame().setExtendedState(Frame.MAXIMIZED_BOTH);
			}
			getVidClnt().setZoomOptionStatus();
			//set the zoom options to ACTUAL_SIZE when switching back to window mode
			onChangeZoomOptions(JVMenu.ACTUAL_SIZE);
			getJVMenu().notifyMenuStateSelected(JVMenu.ACTUAL_SIZE, true);
		}

		// For updating the Window label status in Pause state
		if (m_frame.getMenu().getMenuItem(JVMenu.VIDEO_PAUSE_REDIRECTION).isEnabled()) {
			JViewerApp.getInstance().setAppWndLabel("0 fps");
		}

		m_frame.getMenu().notifyMenuStateSelected(JVMenu.VIDEO_FULL_SCREEN, state);
		m_frame.getMenu().notifyMenuStateEnable(JVMenu.VIDEO_FULL_SCREEN, true);
		m_frame.getMenu().refreshMenu();
		//Dispose user macro dialog, if open, while switching between
		// full screen mode and window mode.  
		if(userDefMacro != null && userDefMacro.isAddMacro()){
			userDefMacro.onCloseDialog();
		}
		//Maintain menu mneumonics and accelerartor status when switching between fullscreen and normal mode.
		if(JViewerApp.getInstance().getJVMenu().getMenuItem(JVMenu.KEYBOARD_FULL_KEYBOARD) != null){
			JViewerApp.getInstance().getJVMenu().getMenuItem(JVMenu.KEYBOARD_FULL_KEYBOARD).setSelected(isFullKeyboardEnabled());
			JViewerApp.getInstance().getJVMenu().enableMenuAccelerator(isFullKeyboardEnabled());
			JViewerApp.getInstance().getJVMenu().enableMenuMnemonics(isFullKeyboardEnabled());
		}
		JViewerApp.getInstance().getJVMenu().updateUserMenu();
	}

	/**
	 * Hold right control key request handler
	 */
	public void OnKeyboardHoldRightCtrlKey(boolean state) {
		Debug.out.println("OnKeyboardHoldRightCtrlKey");

		if (!m_KVMClnt.redirection())
			return;

		m_USBKeyRep.set(KeyEvent.VK_CONTROL, KeyEvent.KEY_LOCATION_RIGHT, state);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		// update menu option
		m_frame.getMenu().notifyMenuStateSelected(JVMenu.KEYBOARD_RIGHT_CTRL_KEY, state);
		
		if(state)
			this.getM_wndFrame().getM_status().getRightCtrl().setForeground(Color.red);
		else
			this.getM_wndFrame().getM_status().getRightCtrl().setForeground(Color.gray);
		this.getM_wndFrame().getM_status().getRightCtrl().setSelected(state);

		if(getSoftKeyboard() != null) {
			getSoftKeyboard().syncHoldKey();
		}
	}

	/**
	 * Hold right alt key request handler
	 */
	public void OnKeyboardHoldRightAltKey(boolean state) {
		Debug.out.println("OnKeyboardHoldRightAltKey");

		if (!m_KVMClnt.redirection())
			return;

		m_USBKeyRep.set(KeyEvent.VK_ALT, KeyEvent.KEY_LOCATION_RIGHT, state);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		// update menu option
		m_frame.getMenu().notifyMenuStateSelected(
				JVMenu.KEYBOARD_RIGHT_ALT_KEY, state);
		//update Status bar
		if(state)
			this.getM_wndFrame().getM_status().getRightAlt().setForeground(Color.red);
		else
			this.getM_wndFrame().getM_status().getRightAlt().setForeground(Color.gray);
		this.getM_wndFrame().getM_status().getRightAlt().setSelected(state);

		if(getSoftKeyboard() != null) {
			getSoftKeyboard().syncHoldKey();
		}
	}

	/**
	 * Hold left shift key request handler
	 */
	public void OnKeyboardHoldLeftShiftKey(boolean state) {
		Debug.out.println("OnKeyboardHoldLeftShiftKey");

		if (!m_KVMClnt.redirection())
			return;

		m_USBKeyRep.set(KeyEvent.VK_SHIFT, KeyEvent.KEY_LOCATION_LEFT, state);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
	}

	/**
	 *  Hold right shift key request handler
	 */
	public void OnKeyboardHoldRightShiftKey(boolean state) {
		Debug.out.println("OnKeyboardHoldRightShiftKey");

		if (!m_KVMClnt.redirection())
			return;

		m_USBKeyRep.set(KeyEvent.VK_SHIFT, KeyEvent.KEY_LOCATION_RIGHT, state);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
	}

	/**
	 * Hold left control key request handler
	 */
	public void OnKeyboardHoldLeftCtrlKey(boolean state) {
		Debug.out.println("OnKeyboardHoldLeftCtrlKey");

		if (!m_KVMClnt.redirection())
			return;

		m_USBKeyRep.set(KeyEvent.VK_CONTROL, KeyEvent.KEY_LOCATION_LEFT, state);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		// update menu option
		m_frame.getMenu().notifyMenuStateSelected(
				JVMenu.KEYBOARD_LEFT_CTRL_KEY, state);		
		//update Status bar
		if(state)
			this.getM_wndFrame().getM_status().getLeftCtrl().setForeground(Color.red);
		else
			this.getM_wndFrame().getM_status().getLeftCtrl().setForeground(Color.gray);
		this.getM_wndFrame().getM_status().getLeftCtrl().setSelected(state);

		if(getSoftKeyboard() != null) {
			getSoftKeyboard().syncHoldKey();
		}		
	}

	/**
	 * Hold left alt key request handler
	 */
	public void OnKeyboardHoldLeftAltKey(boolean state) {
		Debug.out.println("OnKeyboardHoldLeftAltKey");

		if (!m_KVMClnt.redirection())
			return;

		m_USBKeyRep.set(KeyEvent.VK_ALT, KeyEvent.KEY_LOCATION_LEFT, state);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		// update menu option
		m_frame.getMenu().notifyMenuStateSelected(JVMenu.KEYBOARD_LEFT_ALT_KEY,
				state);		
		//update Status bar
		if(state)
			this.getM_wndFrame().getM_status().getLeftAlt().setForeground(Color.red);
		else
			this.getM_wndFrame().getM_status().getLeftAlt().setForeground(Color.gray);
		this.getM_wndFrame().getM_status().getLeftAlt().setSelected(state);
				
		if(getSoftKeyboard() != null) {
			getSoftKeyboard().syncHoldKey();
		}
	}

	/**
	 * Left windows key hold down request handler
	 */
	public void OnKeyboardLeftWindowsKeyHoldDown(boolean state) {
		Debug.out.println("OnKeyboardLeftWindowsKeyHoldDown");

		if (!m_KVMClnt.redirection())
			return;

		m_USBKeyRep.set(KeyEvent.VK_WINDOWS, KeyEvent.KEY_LOCATION_LEFT, state);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		// update menu option
		m_frame.getMenu().notifyMenuStateSelected(
				JVMenu.KEYBOARD_LEFT_WINKEY_PRESSHOLD, state);
		
		if(getSoftKeyboard() != null) {
			getSoftKeyboard().syncHoldKey();
		}	
	}

	/**
	 * Left windows key press and release request handler
	 */
	public void OnKeyboardLeftWindowsKeyPressRelease() {
		Debug.out.println("OnKeyboardLeftWindowsKeyPressRelease");

		if (!m_KVMClnt.redirection())
			return;

		m_USBKeyRep.set(KeyEvent.VK_WINDOWS, KeyEvent.KEY_LOCATION_LEFT, true);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(KeyEvent.VK_WINDOWS, KeyEvent.KEY_LOCATION_LEFT, false);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
	}

	/**
	 * Right windows key hold down request handler
	 */
	public void OnKeyboardRightWindowsKeyHoldDown(boolean state) {
		Debug.out.println("OnKeyboardRightWindowsKeyHoldDown");

		if (!m_KVMClnt.redirection())
			return;

		m_USBKeyRep.set(KeyEvent.VK_WINDOWS, KeyEvent.KEY_LOCATION_RIGHT, state);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		// update menu option
		m_frame.getMenu().notifyMenuStateSelected(JVMenu.KEYBOARD_RIGHT_WINKEY_PRESSHOLD, state);
		
		if(getSoftKeyboard() != null) {
			getSoftKeyboard().syncHoldKey();
		}
	}

	/**
	 * Right windows key press and release request handler
	 */
	public void OnKeyboardRightWindowsKeyPressRelease() {
		Debug.out.println("OnKeyboardRightWindowsKeyPressRelease");

		if (!m_KVMClnt.redirection())
			return;

		m_USBKeyRep.set(KeyEvent.VK_WINDOWS, KeyEvent.KEY_LOCATION_RIGHT, true);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(KeyEvent.VK_WINDOWS, KeyEvent.KEY_LOCATION_RIGHT, false);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
	}

	/**
	 * Alt ctrl del request handler
	 */
	public void OnKeyboardAltCtrlDel() {
		Debug.out.println("OnKeyboardAltCtrlDel");

		if (!m_KVMClnt.redirection())
			return;

		m_USBKeyRep.set(KeyEvent.VK_CONTROL, KeyEvent.KEY_LOCATION_LEFT, true);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(KeyEvent.VK_ALT, KeyEvent.KEY_LOCATION_LEFT, true);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(KeyEvent.VK_DELETE, KeyEvent.KEY_LOCATION_STANDARD,	true);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(KeyEvent.VK_DELETE, KeyEvent.KEY_LOCATION_STANDARD, false);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(KeyEvent.VK_ALT, KeyEvent.KEY_LOCATION_LEFT, false);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(KeyEvent.VK_CONTROL, KeyEvent.KEY_LOCATION_LEFT, false);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);

	}

	/**
	 * Method to send the Context Menu key Event
	 *
	 */
	public void OnKeyboardContextMenu() {
		// TODO Auto-generated method stub
		m_USBKeyRep.set(KeyEvent.VK_CONTEXT_MENU, KeyEvent.KEY_LOCATION_STANDARD, true);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(KeyEvent.VK_CONTEXT_MENU, KeyEvent.KEY_LOCATION_STANDARD, false);
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
	}

	/**
	 * Reset the Modifiers
	 */
	public void resetModifiers(){
		byte modifiers = USBKeyProcessorEnglish.getModifiers();
		if((modifiers & KeyProcessor.MOD_LEFT_CTRL) == KeyProcessor.MOD_LEFT_CTRL &&
		!JViewerApp.getInstance().getJVMenu().getMenuSelected(JVMenu.KEYBOARD_LEFT_CTRL_KEY)){
			JViewerApp.getInstance().OnKeyboardHoldLeftCtrlKey(false);
		}
		if((modifiers & KeyProcessor.MOD_RIGHT_CTRL) == KeyProcessor.MOD_RIGHT_CTRL &&
				!JViewerApp.getInstance().getJVMenu().getMenuSelected(JVMenu.KEYBOARD_RIGHT_CTRL_KEY)){
			JViewerApp.getInstance().OnKeyboardHoldRightCtrlKey(false);
		}
		if(((modifiers & KeyProcessor.MOD_LEFT_ALT) == KeyProcessor.MOD_LEFT_ALT)&&
				!JViewerApp.getInstance().getJVMenu().getMenuSelected(JVMenu.KEYBOARD_LEFT_ALT_KEY)){
			JViewerApp.getInstance().OnKeyboardHoldLeftAltKey(false);
		}
		if(((modifiers & KeyProcessor.MOD_RIGHT_ALT) == KeyProcessor.MOD_RIGHT_ALT)&&
				!JViewerApp.getInstance().getJVMenu().getMenuSelected(JVMenu.KEYBOARD_RIGHT_ALT_KEY)){
			JViewerApp.getInstance().OnKeyboardHoldRightAltKey(false);
		}
		if(((modifiers & KeyProcessor.MOD_LEFT_SHIFT) == KeyProcessor.MOD_LEFT_SHIFT)){
			JViewerApp.getInstance().OnKeyboardHoldLeftShiftKey(false);
		}
		if(((modifiers & KeyProcessor.MOD_RIGHT_SHIFT) == KeyProcessor.MOD_RIGHT_SHIFT)){
			JViewerApp.getInstance().OnKeyboardHoldRightShiftKey(false);
		}
		USBKeyProcessorEnglish.setModifiers(modifiers);
	}

	/**
	 * Sending Macro keyevent to the Host
	 * @param string
	 */
	public void OnsendMacrokeycode(String string) {
		// TODO Auto-generated method stub		
		String[] keycodesplit = string.split("[+]");
		for(int j=0;j<keycodesplit.length;)
		{
			int keycode = Integer.parseInt(keycodesplit[j++]);
			int keylocation = Integer.parseInt(keycodesplit[j++]);
			sendevent( keycode, keylocation, true);
		}
		for(int j=0;j<keycodesplit.length;)
		{
			int keycode = Integer.parseInt(keycodesplit[j++]);
			int keylocation = Integer.parseInt(keycodesplit[j++]);
			sendevent( keycode, keylocation, false);
		}
	}

	public void sendevent(int keycode,int keylocation,boolean status){
		if (!m_KVMClnt.redirection()) return;

		m_USBKeyRep.set(keycode, keylocation, status );
		m_KVMClnt.sendKMMessage(m_USBKeyRep);
	}

	/**
	 * soft keyboard request handler
	 */

	public void OnSkbrdDisplay(int langindex) {
		if (softKeyboard != null) {
			softKeyboard.m_skmouselistener.close();
			softKeyboard.dispose();
		}
		if(langindex == -1 ) {
			if(softKeyboard != null) {
				softKeyboard.setVisible(false);
				softKeyboard =null;
			}
		}
		else {
			softKeyboard = new SoftKeyboard(langindex);
		}
	}

	/**
	 * Encryption status message handler. Encryption status message is received
	 * from server when a client enables keyboard/mouse encryption.
	 */
	public void OnEncryptionStatus(){
		if( !m_frame.getMenu().getMenuItem(JVMenu.OPTIONS_KEYBOARD_MOUSE_ENCRYPTION).isEnabled()){
			m_frame.getMenu().notifyMenuStateEnable(JVMenu.OPTIONS_KEYBOARD_MOUSE_ENCRYPTION, true);
		}

		// ignore encryption notification if encryption is enabled by this  client.
		if (m_KVMClnt.isKMEncryptionEnabled())
			return;

		// notify KVM client
		m_KVMClnt.notifyEncryption(true);
		// update menu option
		m_frame.getMenu().notifyMenuStateSelected(JVMenu.OPTIONS_KEYBOARD_MOUSE_ENCRYPTION, true);
		if (KVMClient.DISABLE_ENCRPT_FLAG) {
			InfoDialog.showDialog(m_frame, LocaleStrings.getString("D_11_JVAPP"),
					LocaleStrings.getString("D_12_JVAPP"),
					InfoDialog.INFORMATION_DIALOG);
			KVMClient.DISABLE_ENCRPT_FLAG = false;
			//initilize encryption
			m_KVMClnt.getCrypt().initialize(m_encToken, KMCrypt.ENCRYPT);
		} else {
			// notify the user about change in encryption status
			InfoDialog.showDialog(m_frame, LocaleStrings.getString("D_13_JVAPP"),
					LocaleStrings.getString("D_12_JVAPP"),
					InfoDialog.INFORMATION_DIALOG);
		}
	}

	/**
	 * Initial encryption status message handler. This message is received
	 * during the session establishment if there are any other clients connected
	 * to server with keyboard/mouse encryption enabled. In such case we need to
	 * enable the same for our client also.
	 */
	public void OnInitialEncryptionStatus() {

		// We just notify about the encryption here. As soon as we
		// receive first video frame which carries place holder, we generate
		// encryption key.
		// notify KVM client
		m_KVMClnt.notifyEncryption(true);
		// update menu option
		m_frame.getMenu().notifyMenuStateSelected(JVMenu.OPTIONS_KEYBOARD_MOUSE_ENCRYPTION, true);		
		//initilize encryption
		m_KVMClnt.getCrypt().initialize(m_encToken, KMCrypt.ENCRYPT);
		// notify the user about change in encryption status
		InfoDialog.showDialog(m_frame, LocaleStrings.getString("D_13_JVAPP"),
				LocaleStrings.getString("D_12_JVAPP"),
				InfoDialog.INFORMATION_DIALOG);
	}

	/**
	 * Keyboard encryption request handler
	 */
	public void OnOptionsKeyboardMouseEncryption(boolean state) {
		Debug.out.println("OnOptionsKeyboardMouseEncryption");

		m_KVMClnt.setEncryption(state);
		// update menu option
		m_frame.getMenu().notifyMenuStateSelected(JVMenu.OPTIONS_KEYBOARD_MOUSE_ENCRYPTION, state);
		
		if(m_frame.getMenu().getMenuSelected(JVMenu.OPTIONS_KEYBOARD_MOUSE_ENCRYPTION)){
			m_frame.getMenu().notifyMenuStateEnable(JVMenu.OPTIONS_KEYBOARD_MOUSE_ENCRYPTION, !state);
		}
		
		if (true == state) {
			m_KVMClnt.getCrypt().initialize(m_encToken, KMCrypt.ENCRYPT);
		}
		else {
			m_KVMClnt.getCrypt().close();
		}
	}

	/**
	 * Sync cursor request handler
	 */
	public void OnUSBMouseSyncCursor(boolean state) {
		Debug.out.println("OnUSBMouseSyncCursor");
		JViewerApp.showCursor = true;
		m_view.USBsyncCursor(state);
		m_frame.getMenu().refreshMenu();
	}

	/**
	 * Hide cursor request handler
	 */
	public void OnShowCursor(boolean state) {
		Debug.out.println("OnShowCursor");	
			
		getJVMenu().notifyMenuStateEnable( JVMenu.MOUSE_CLIENTCURSOR_CONTROL, true);
		getJVMenu().notifyMenuStateSelected(JVMenu.MOUSE_CLIENTCURSOR_CONTROL, state);
		
		if (state){
			if(JVMenu.m_scale != 1.0 || zoomOption == JVMenu.FIT_TO_CLIENT_RES){
				if(m_view.GetUSBMouseMode() == USBMouseRep.RELATIVE_MOUSE_MODE){
					showCursor=false;
					getJVMenu().notifyMenuStateSelected(JVMenu.MOUSE_CLIENTCURSOR_CONTROL, false);
					getJVMenu().notifyMenuStateEnable(JVMenu.MOUSE_CLIENTCURSOR_CONTROL, false);
					URL imageURLMouse = com.ami.kvm.jviewer.JViewer.class.getResource("res/Mouse2Btn-gray.png");
					getM_wndFrame().toolbar.mouseBtn.setIcon(new ImageIcon(imageURLMouse));
					JViewerApp.getInstance().getM_wndFrame().toolbar.mouseBtn.setToolTipText(LocaleStrings.getString("D_48_JVAPP"));
					return;
				}
			}
			else{
				showCursor=true;
				m_view.ShowCursor(true);
				URL imageURLMouse = com.ami.kvm.jviewer.JViewer.class.getResource("res/Mouse2Btn.png");
				getM_wndFrame().toolbar.mouseBtn.setIcon(new ImageIcon(imageURLMouse));
				JViewerApp.getInstance().getM_wndFrame().toolbar.mouseBtn.setToolTipText(LocaleStrings.getString("D_14_JVAPP"));
			}
		}
		else{
			showCursor=false;
			m_view.ShowCursor(false);
			URL imageURLMouse = com.ami.kvm.jviewer.JViewer.class.getResource("res/Mouse2Btn-gray.png");
			getM_wndFrame().toolbar.mouseBtn.setIcon(new ImageIcon(imageURLMouse));
			if(JVMenu.m_scale != 1.0 || zoomOption == JVMenu.FIT_TO_CLIENT_RES){
				if(m_view.GetUSBMouseMode() == USBMouseRep.RELATIVE_MOUSE_MODE){
					getJVMenu().notifyMenuStateSelected(JVMenu.MOUSE_CLIENTCURSOR_CONTROL, false);
					getJVMenu().notifyMenuStateEnable(JVMenu.MOUSE_CLIENTCURSOR_CONTROL, false);
					JViewerApp.getInstance().getM_wndFrame().toolbar.mouseBtn.setToolTipText(LocaleStrings.getString("D_48_JVAPP"));
				}
			}
			else
				JViewerApp.getInstance().getM_wndFrame().toolbar.mouseBtn.setToolTipText(LocaleStrings.getString("D_15_JVAPP"));
		}
	}

	/**
	 * Bandwidth auto detect request handler
	 */
	public void OnOptionsBandwidthAutoDetect() {
		Debug.out.println("OnOptionsBandwidthAutoDetect");
		m_KVMClnt.autoDetect();
		
		if( JViewer.isStandalone() ) {
			m_autoBWDlg = new AutoBWDlg(JViewer.getMainFrame());
			m_autoBWDlg.setVisible(true);			
		} else {
			JPanel panel = new JPanel();
			label = new JLabel(LocaleStrings.getString("9_1_BW")+" ...");
			panel.add(label);
			JOptionPane optionPane = new JOptionPane(panel,JOptionPane.PLAIN_MESSAGE);
		    optionPane.setOptions(new Object[] {});
			dialog = optionPane.createDialog(JViewerApp.getInstance().getMainWindow(), LocaleStrings.getString("9_1_BW")+"...");
			dialog.setResizable(false);
		    dialog.setSize(350, 100);
		    dialog.setVisible(true);
		}
	}

	/**
	 * Update bandwidth detection dialog
	 *
	 * @param new
	 *            bandwidth
	 */
	public void updateBandwidthMsg(String newBW) {
		
		if( JViewer.isStandalone()) {
			if ((m_autoBWDlg != null) && (m_autoBWDlg.isVisible())) {
				m_autoBWDlg.setMessage(LocaleStrings.getString("9_1_BW") + " - "+ newBW);
			}

			m_autoBWDlg.done();
		} else {
			try
			 {
				 label.setText(LocaleStrings.getString("9_1_BW") + " - "+newBW);
				 Thread.sleep(1000);
				 label.setText(LocaleStrings.getString("9_3_BW")+"...");
				 Thread.sleep(1000);
				 label.setText(LocaleStrings.getString("D_16_JVAPP")+newBW+LocaleStrings.getString("D_17_JVAPP"));
				 Thread.sleep(1000);
				 dialog.dispose();
			 }
			 catch(Exception e) {
				 Debug.out.println(e);
			 }
		}
	}
	
	public void OnOptionsBandwidth(int bandWidth)
	{
		m_KVMClnt.setBandwidth(bandWidth);
		m_frame.getMenu().SetMenuSelected(JVMenu.previous_bandwidth, false);
		
		switch(bandWidth) {
			case CfgBandwidth.BANDWIDTH_256KBPS:
				Debug.out.println("OnOptionsBandwidth256Kbps");				
				JVMenu.previous_bandwidth = JVMenu.OPTIONS_BANDWIDTH_256KBPS;
				m_frame.getMenu().notifyMenuStateSelected(JVMenu.OPTIONS_BANDWIDTH_256KBPS, true);
				break;
			case CfgBandwidth.BANDWIDTH_512KBPS:
				Debug.out.println("OnOptionsBandwidth512Kbps");
				JVMenu.previous_bandwidth = JVMenu.OPTIONS_BANDWIDTH_512KBPS;
				m_frame.getMenu().notifyMenuStateSelected(JVMenu.OPTIONS_BANDWIDTH_512KBPS, true);
				break;
			case CfgBandwidth.BANDWIDTH_1MBPS:
				Debug.out.println("OnOptionsBandwidth1Mbps");
				JVMenu.previous_bandwidth = JVMenu.OPTIONS_BANDWIDTH_1MBPS;
				m_frame.getMenu().notifyMenuStateSelected(JVMenu.OPTIONS_BANDWIDTH_1MBPS, true);
				break;
			case CfgBandwidth.BANDWIDTH_10MBPS:
				Debug.out.println("OnOptionsBandwidth10Mbps");
				JVMenu.previous_bandwidth = JVMenu.OPTIONS_BANDWIDTH_10MBPS;
				m_frame.getMenu().notifyMenuStateSelected(JVMenu.OPTIONS_BANDWIDTH_10MBPS, true);
				break;
			case CfgBandwidth.BANDWIDTH_100MBPS:
				Debug.out.println("OnOptionsBandwidth100Mbps");	
				JVMenu.previous_bandwidth = JVMenu.OPTIONS_BANDWIDTH_100MBPS;
				m_frame.getMenu().notifyMenuStateSelected(JVMenu.OPTIONS_BANDWIDTH_100MBPS, true);
				break;
		}		
	}

	/**
	 * Invoke the IPMI Command Dialog 
	 */
	public void invokeIPMICommandDialog(){
		ipmiDialog = new IPMICommandDialog(JViewer.getMainFrame());
		ipmiDialog.showDialog();
	}

	/**
	 * @return the ipmiDialog
	 */
	public IPMICommandDialog getIPMIDialog() {
		return ipmiDialog;
	}

	/**
	 * @param ipmiDialog the ipmiDialog to set
	 */
	public void setIPMIDialog(IPMICommandDialog ipmiDialog) {
		this.ipmiDialog = ipmiDialog;
	}

	/**
	 * Send IPMI request command to BMC
	 * @param sequence - sequence number of the command
	 * @param commands - commands to be send
	 * @return 0 if success -1 otherwise
	 */
	public int onSendIPMICommand(byte sequence, byte[] commands){
		IVTPPktHdr IPMICommandHdr = new IVTPPktHdr(IVTPPktHdr.IVTP_IPMI_REQUEST_PKT, commands.length+1, (short)0);
		ByteBuffer IPMICommandBuffer = ByteBuffer.allocate(IPMICommandHdr.size() + commands.length+1);
		byte[] IPMICommandReport;
		IPMICommandBuffer.position(0);
		IPMICommandBuffer.put(IPMICommandHdr.array());
		IPMICommandBuffer.put(sequence);
		IPMICommandBuffer.put(commands);
		IPMICommandBuffer.position(0);
		IPMICommandReport = new byte[IPMICommandBuffer.limit()];
		IPMICommandBuffer.get(IPMICommandReport, 0, IPMICommandBuffer.limit());
		if (IPMICommandReport.length != getKVMClient().sendMessage(IPMICommandReport, IPMICommandReport.length)) {
			Debug.out.println("Failed to send IPMI command");
			return -1;
		}
		return 0;
	}

	/**
	 * Handles the IPMI response message received from BMC
	 * @param responseMessage - response message from BMC
	 * @param responseStatus - status code
	 */
	public void onGetIPMICommandResponse(ByteBuffer responseMessage, int responseStatus){
		String response = new String();
		byte sequence = 0;
		responseMessage.order(ByteOrder.LITTLE_ENDIAN);
		sequence = responseMessage.get();
		if(responseStatus == 0){
			byte[] responseBuffer = new byte[responseMessage.limit()-1];
			responseMessage.order(ByteOrder.LITTLE_ENDIAN);
			responseMessage.position(1);
			responseMessage.get(responseBuffer);

			response = Debug.out.dumpIPMI(responseBuffer, 0, responseBuffer.length);
			if(response.equals("") || response.length() == 0){
				String hexCompletionCode  = Integer.toHexString(responseStatus & 0xFF);
				response = LocaleStrings.getString("D_50_JVAPP")+" : 0x "+hexCompletionCode.toUpperCase();
			}
		}
		else{
			//Show the last byte of the responseStatus as the Completion Code
			String hexCompletionCode  = Integer.toHexString(responseStatus & 0xFF);
			response = LocaleStrings.getString("D_50_JVAPP")+" : 0x "+hexCompletionCode.toUpperCase();
		}
		
		ipmiDialog.onIPMICommandRespose(sequence, response);
	}

	/*
	 * OnGUILanguageChange : To change GUI Text and Menu text language
	 */
	public void OnGUILanguageChange(String lang){
			
		JViewer.setLanguage(lang);	
		
		getJVMenu().changeMenuLanguage();
		getJVMenu().changeMenuItemLanguage();
		getJVMenu().changeStatusBarLanguage();
		JViewerApp.getInstance().getM_wndFrame().toolbar.changeToolBarItemLanguage();
		JViewerApp.getInstance().getM_wndFrame().toolbar.setZoomLabel(getM_wndFrame().toolbar.slider_zoom.getValue());
		JViewerApp.getInstance().getRCView().repaint();
		if(ipmiDialog != null){
			ipmiDialog.closeIPMICommandDialog();
			ipmiDialog = null;
			invokeIPMICommandDialog();
		}
		if(m_mediaDlg != null && m_mediaDlg.isShowing()){
			//recreate VMedia dialog.
			m_mediaDlg.disposeVMediaDialog();
			OnvMedia();
		}
	}

	/**
	 * Send the full permission request
	 */
	public void onSendFullPermissionRequest(){
		KVMSharing.KVM_CLIENT_USERNAME = null;
		KVMSharing.KVM_CLIENT_IP = null;
		KVMSharing.KVM_CLIENT_SESSION_INDEX = null;
		if(getKVMClient().sendKVMFullPermissionRequest() < 0){
			InfoDialog.showDialog(m_frame, LocaleStrings.getString("D_53_JVAPP"),
					LocaleStrings.getString("D_54_JVAPP"), InfoDialog.ERROR_DIALOG);
		}
	}
	public void onGetFullPermissionRequest(short status){		
		fullPermissionRequest = true;
		OnKvmPrevilage(status);
	}

	/**
	 * About jviewer request handler
	 */
	public void OnHelpAboutJViewer() {
		Debug.out.println("OnHelpAboutJViewer");
		OEMResourceURLProcessor urlProcessor = new OEMResourceURLProcessor(m_webSession_token, JViewer.getIp());
		String copyright = urlProcessor.getOemCopyright();
		ImageIcon logo = urlProcessor.getOemLogo();
		if (copyright != null && copyright.length() > 0){
				String target = "<=socversion=>";
				if (copyright.contains(target)){
					copyright = copyright.replace(target, this.getSoc_manager().getSOCVersion());
				}
				target = "<=soc=>";
				if (copyright.contains(target)){
					copyright = copyright.replace(target, this.getSoc_manager().getSOC());
				}
				target = "<=jviewerversion=>";
				if (copyright.contains(target)){
					copyright = copyright.replace(target, currentVersion);
				}
		}else{
			copyright = LocaleStrings.getString("D_18_JVAPP") + currentVersion + "\n"
						+ LocaleStrings.getString("D_19_JVAPP") + getSoc_manager().getSOCVersion()
						+ LocaleStrings.getString("D_20_JVAPP")+getSoc_manager().getSOC() + "\n"
						+ LocaleStrings.getString("D_21_JVAPP");
		}
		if (logo == null){
			logo = new ImageIcon(com.ami.kvm.jviewer.JViewer.class.getResource("res/ami.jpg"));
		}
		JOptionPane.showMessageDialog(m_frame,
				copyright, LocaleStrings.getString("D_22_JVAPP")+JViewer.getTitle(), JOptionPane.INFORMATION_MESSAGE, logo);

	}

	/**
	 * Exit request handler
	 */
	public void OnVideoExit() {
		Debug.out.println("OnVideoExit");
		m_frame.exitApp();
	}

	/**
	 * Max Session status message handler. Max Session status message is
	 * received from server when a client count reaches the maximum session
	 * limit.
	 */
	public void onMaxSession() {
		// notify the user about maximum session reached
		JOptionPane.showMessageDialog(m_frame, LocaleStrings.getString("D_23_JVAPP"),
				LocaleStrings.getString("D_24_JVAPP"),
				JOptionPane.INFORMATION_MESSAGE);
		m_frame.exitApp();
	}

	public void OnGetMouseMode(byte mouse_mode) {
		Debug.out.println("Mouse mode response packet received. Mouse Mode:" + mouse_mode);
		m_view.SetUSBMouseMode(mouse_mode);
		/*
		 * If Absolute mode Show Cursor is disabled If Relative mode Show Cursor
		 * Menu Option by default is "Not Selected"
		 */
		OnUSBMouseSyncCursor(true);
		if (mouse_mode == USBMouseRep.RELATIVE_MOUSE_MODE) {
			JViewerApp.getInstance().getJVMenu().notifyMenuStateEnable( JVMenu.CALIBRATEMOUSETHRESHOLD, true);
			JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected( JVMenu.MOUSE_RELATIVE_MODE, true);
			/*Allow window to be resizable*/
			if(!JViewer.getMainFrame().isResizable() && !isFullScreenMode() && zoomOption == JVMenu.ACTUAL_SIZE ){
				JViewer.getMainFrame().setResizable(true);
				JViewerApp.getInstance().getMainWindow().m_viewSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				JViewerApp.getInstance().getMainWindow().m_viewSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			}
		}
		else {
			if( mouse_mode == USBMouseRep.OTHER_MOUSE_MODE){
				GraphicsConfiguration gc = JViewerApp.getInstance().getM_wndFrame().getGraphicsConfiguration();
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
				int screenHeight = screenSize.height;
				int screenWidth = screenSize.width;
				int systemComponentsHeight = screenInsets.top + screenInsets.bottom;
				int systemComponentsWidth = screenInsets.left+screenInsets.right;
				int frameHeight = screenHeight - systemComponentsHeight;
				int frameWidth = screenWidth - systemComponentsWidth;
				JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.MOUSE_OTHER_MODE, true);
				//Set the scroll pane view position ot initial position.
				JViewerApp.getInstance().getMainWindow().m_viewSP.getViewport().setViewPosition(new Point(0, 0));
				/*Code to set window as not resizable*/
				JViewerApp.getInstance().getMainWindow().m_viewSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				JViewerApp.getInstance().getMainWindow().m_viewSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
				GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
				JViewer.getMainFrame().setMaximizedBounds(graphEnv.getMaximumWindowBounds());
				JViewer.getMainFrame().setSize(frameWidth, frameHeight);
				JViewer.getMainFrame().setLocationRelativeTo(null);
				JViewer.getMainFrame().setResizable(false);
			}
			else if(mouse_mode == USBMouseRep.ABSOLUTE_MOUSE_MODE){
				/*Allow window to be resizable*/
				if(!JViewer.getMainFrame().isResizable() && !isFullScreenMode() && zoomOption == JVMenu.ACTUAL_SIZE){
					JViewer.getMainFrame().setResizable(true);
					JViewerApp.getInstance().getMainWindow().m_viewSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					JViewerApp.getInstance().getMainWindow().m_viewSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				}
				JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected( JVMenu.MOUSE_ABSOLUTE_MODE, true);
			}
			JViewerApp.getInstance().OnShowCursor(true);
			JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected( JVMenu.MOUSE_CLIENTCURSOR_CONTROL, true);
		}
		getVidClnt().setZoomOptionStatus();
		getM_wndFrame().getM_status().resetStatus();
		JViewerApp.getInstance().getJVMenu().notifyMouseMode(mouse_mode);
	}

	/**
	 *
	 * Method is used to check the Response of the session valid/Invalid
	 *
	 * @param status
	 */

	public void OnValidateVideoSessionResp(byte status,byte sindex) {
		if (status == KVMClient.INVALID_SESSION) {
			m_frame.generalErrorMessage(LocaleStrings.getString("D_25_JVAPP"),
										LocaleStrings.getString("D_26_JVAPP"));
			JViewer.exit(0);
		}
		else if(status == KVMClient.KVM_DISABLED){
			m_frame.generalErrorMessage(LocaleStrings.getString("D_25_JVAPP"),
					LocaleStrings.getString("1_5_JVIEWER"));
			JViewer.exit(0);
		}

		//session id of JViewer
		currentSessionId = sindex;

		m_KVMClnt.OnValidVideoSession();
		if(!JViewer.isWebPreviewer() && m_KVMClnt.redirection()){

			String commonExceptionList[] = {JVMenu.VIDEO_EXIT,JVMenu.HELP_ABOUT_RCONSOLE,JVMenu.VIDEO_FULL_SCREEN};
			String exceptionList[]= getExceptionMenuList(commonExceptionList);
			m_frame.getMenu().enableMenu(exceptionList,true, true);
			if(KVMSharing.KVM_REQ_GIVEN == KVMSharing.KVM_REQ_PARTIAL){
				JViewerApp.getInstance().getJVMenu().notifyMenuStateEnable(JVMenu.OPTIONS_KEYBOARD_MOUSE_ENCRYPTION, false);
			}
			else{
				//Incase of partial KVMSharing no need to enable status bar
				JViewerApp.getInstance().getM_wndFrame().getM_status().enableStatusBar(true);
			}

			JViewerApp.getInstance().getM_wndFrame().toolbar.changeMacrowsStatus(true);
		}
	}

	/**
	 * Method used to check and update LED status on host
	 *
	 * @param status -
	 *            Host keyboard LED status
	 */
	public void onKeybdLED(byte Led_status) {
		if (!JViewerView.syncLEDFlag) {
			this.Led_status = Led_status;
			FloppyRedir flp = new FloppyRedir(true);
			byte clientKybdLED = flp.ReadKeybdLEDStatus();

			// Received LED status from host and comparing with the status from
			// client. Setting appropriate keyevent to client keyboard LED.
			try {
				Robot r = new Robot();
				//Don't add or remove KeyListener from JViewer while Mousealibration is in progress.
				if (!(Mousecaliberation.THRESHOLDFLAG || Mousecaliberation.ACCELERATION_FLAG)) {
					JViewerApp.getInstance().m_view.removeKeyListener();
				}
				if ((clientKybdLED & NUMLOCK) != (Led_status & NUMLOCK)) {
					r.keyPress(KeyEvent.VK_NUM_LOCK);
					r.keyRelease(KeyEvent.VK_NUM_LOCK);
					Thread.sleep(100);
				}
				if ((clientKybdLED & CAPSLOCK) != (Led_status & CAPSLOCK)) {
					r.keyPress(KeyEvent.VK_CAPS_LOCK);
					r.keyRelease(KeyEvent.VK_CAPS_LOCK);
					Thread.sleep(100);
				}
				if ((clientKybdLED & SCROLLLOCK) != (Led_status & SCROLLLOCK)) {
					r.keyPress(KeyEvent.VK_SCROLL_LOCK);
					r.keyRelease(KeyEvent.VK_SCROLL_LOCK);
					Thread.sleep(100);
				}
				if (!(Mousecaliberation.THRESHOLDFLAG || Mousecaliberation.ACCELERATION_FLAG)) {
					JViewerApp.getInstance().m_view.addKeyListener();
				}
			} catch (Exception e) {
				Debug.out.println(e);
			}
			flp = null;
		} else {
			this.Led_status = Led_status;
		}
		if(getSoftKeyboard() != null)
			getSoftKeyboard().syncKbdLED();//Synchronize Softkeyboard LED status with Host LED status;
	}

	/**
	 * Method stop the CD/FD Image, If eject cmd initiated by the host
	 */
	public void stopRedirection_ISoImage() {
		if (m_IUSBSession.cdromSession != null) {
			for(int k=0;k<JViewerApp.getInstance().getM_cdNum();k++){
				if (JViewerApp.getInstance().getM_IUSBSession().cdromSession[k].isCdImageRedirected() &&
						m_IUSBSession.cdromSession[k].isCdImageEjected())
					m_IUSBSession.StopISORedir(k, IUSBRedirSession.STOP_ON_EJECT);
			}
		}
		if (m_IUSBSession.floppySession != null) {
		    for(int k=0;k<JViewerApp.getInstance().getM_cdNum();k++){
			if (JViewerApp.getInstance().getM_IUSBSession().floppySession[k].isFdImageRedirected() &&
					JViewerApp.getInstance().getM_IUSBSession().floppySession[k].isFdImageEjected())
				m_IUSBSession.StopFloppyImageRedir(k, IUSBRedirSession.STOP_ON_EJECT);
		    }
		}
		if (m_IUSBSession.harddiskSession != null) {
		    for(int k=0;k<JViewerApp.getInstance().getM_cdNum();k++){
			if (JViewerApp.getInstance().getM_IUSBSession().harddiskSession[k].isHdImageRedirected() &&
					JViewerApp.getInstance().getM_IUSBSession().harddiskSession[k].isHdImageEjected())
				m_IUSBSession.StopHarddiskImageRedir(k, IUSBRedirSession.STOP_ON_EJECT);
		    }
		}

	}

	/**
	 * Method to caliberate the mouse if the mouse in relative mode
	 * @param state
	 */
	public void OnCalibareteMouse(boolean state) {
		if(JVMenu.m_scale != 1.0){//zoom not equal top 100%
			JOptionPane.showMessageDialog(getM_frame(), LocaleStrings.getString("D_27_JVAPP"),
					LocaleStrings.getString("D_28_JVAPP") , JOptionPane.ERROR_MESSAGE);
			getJVMenu().notifyMenuStateSelected(JVMenu.CALIBRATEMOUSETHRESHOLD, false);
			return;
		}
		Mousecaliberation.resetCursor();
		if (Mousecaliberation == null)
			Mousecaliberation = new Mousecaliberation();

		JViewerApp.getInstance().getJVMenu().notifyMenuStateEnable(JVMenu.VIDEO_FULL_SCREEN, false);
		JViewerApp.getInstance().getM_wndFrame().toolbar.fullScreenBtn.setToolTipText(
				LocaleStrings.getString("D_29_JVAPP"));
		//remove the default keylistener because ading new keylistener and mouse lisener for MOuse caliberation
		JViewerApp.getInstance().getRCView().removeKMListener();
		Mousecaliberation.OnCalibareteMouseThreshold(state);
	}

	public void OnSendKVMPrevilage(byte kvmPrivilege, String userDetails)
	{
		if(kvmPrivilege == KVMSharing.KVM_REQ_ALLOWED){
			if(JViewerApp.getInstance().IsCDROMRedirRunning() ||
					JViewerApp.getInstance().IsFloppyRedirRunning() ||
					JViewerApp.getInstance().IsHarddiskRedirRunning()){
				if(!getM_frame().stopVMediaRedirection(LocaleStrings.getString("D_58_JVAPP"))){
					kvmPrivilege = KVMSharing.KVM_REQ_PARTIAL;
				}
			}
		}
		m_KVMClnt.SendKVMPrevilage(kvmPrivilege, userDetails);
		if(isFullPermissionRequest())
			setFullPermissionRequest(false);
	}
	/**
	 * Updates the concurrent session status as one of the sessions close during KVM sharing. 
	 */
	public void onStopConcurrentSession(){
		//Show information dialog, when concurrent session is closed, and full permisssion is received.
		if( KVMSharing.KVM_REQ_GIVEN == KVMSharing.KVM_REQ_PARTIAL){
			InfoDialog.showDialog(JViewer.getMainFrame(), LocaleStrings.getString("D_51_JVAPP"),
					LocaleStrings.getString("D_52_JVAPP"), InfoDialog.INFORMATION_DIALOG);
		}
		KVMSharing.KVM_REQ_GIVEN = KVMSharing.KVM_REQ_DENIED;
		KVMShareDialog.isMasterSession = true;
		//if user has paused the session, then no need to update the controls.
		if(m_KVMClnt.redirection()){
			OnChangeMenuState_KVMPartial(getJVMenu(), true);
			JViewerApp.getInstance().getM_wndFrame().toolbar.OnChangeToolbarIconState_KVMPartial();
		}
	}
	/**
	 * Send the Websessiontoken to the server
	 * @return
	 */
	public int OnsendWebsessionToken() {
		int session_token_type = 0;
		int PktLen = 0, TokenLen = 0;
		//Sending the websession token only for JViewerApp
		if(JViewer.isjviewerapp()){				
			String web_session_token = JViewerApp.getInstance().getM_webSession_token();
			byte[] web_token = new byte[web_session_token.length()];
			web_token = web_session_token.getBytes();
			Debug.out.dump(web_token);
			IVTPPktHdr WebSessTokenPkt = new IVTPPktHdr(IVTPPktHdr.IVTP_GET_WEB_TOKEN, web_token.length, (short) 0);
			ByteBuffer wbf = ByteBuffer.allocate(WebSessTokenPkt.size()	+ web_token.length);
			wbf.position(0);
			wbf.put(WebSessTokenPkt.array());
			wbf.put(web_token);
			wbf.position(0);
			byte[] wreport = new byte[wbf.limit()];
			wbf.get(wreport);

			if (wreport.length != getKVMClient().sendMessage(wreport, wreport.length)) {
				Debug.out.println("Failed to web Session token to the card");
				return -1;
			}
		}

		session_token_type = JViewerApp.getInstance().getSessionTokenType();

		/*As we are using token length as 128. IVTPPktHdr.VIDEO_PACKET_SIZE  will be used for ssi and normal case.*/
		PktLen = IVTPPktHdr.HDR_SIZE + IVTPPktHdr.VIDEO_PACKET_SIZE;
		TokenLen = IVTPPktHdr.SSI_HASH_SIZE;


		/* Sending the session token */
		IVTPPktHdr VideoSessTokenPkt = new IVTPPktHdr(IVTPPktHdr.IVTP_VALIDATE_VIDEO_SESSION, PktLen, (short)0);
		String session_token = JViewerApp.getInstance().getSessionToken();
		ByteBuffer bf = ByteBuffer.allocate(PktLen);

		// Calculate digest
		byte[] hashed_token = new byte[TokenLen];
		hashed_token = session_token.getBytes();

		bf.position(0);
		bf.put(VideoSessTokenPkt.array());
		bf.put((byte)0);
		bf.put(session_token.getBytes());
		Debug.out.println("Hashed token");
		Debug.out.dump(hashed_token);

		for (int i=bf.position(); i < (IVTPPktHdr.HDR_SIZE+TokenLen); i++)
			bf.put((byte)0);


//		try {
//			InetAddress hostAddress = InetAddress.getByName(KVMSharing.KVM_CLIENT_OWN_IP);
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//
//		}

		bf.put(KVMSharing.KVM_CLIENT_OWN_IP.getBytes());
		for (int i=bf.position(); i < (IVTPPktHdr.HDR_SIZE+TokenLen+IVTPPktHdr.CLINET_OWN_IP_LENGTH); i++)
			bf.put((byte)0);
		KVMSharing.KVM_CLIENT_OWN_USERNAME = getClientUserName();
		bf.put(KVMSharing.KVM_CLIENT_OWN_USERNAME .getBytes());
		for (int i=bf.position(); i < bf.limit(); i++ )
			bf.put((byte) 0);

		bf.position(0);
		byte[] report = new byte[bf.limit()];
		bf.get(report);
		if (report.length != getKVMClient().sendMessage(report, report.length)) {
			Debug.out.println("Failed to send Session token to the card");
			return -1;
		}

		// send resume video redirection command
		if ((getKVMClient().OnFormIVTPHdr_Send(IVTPPktHdr.IVTP_RESUME_REDIRECTION, 0, (short) 0)) == -1)
			return -1;

		return 0;
	}
	public int onSendWebPreviewerSession() {
		IVTPPktHdr webPreviewSession = new IVTPPktHdr(IVTPPktHdr.IVTP_WEB_PREVIEWER_SESSION, 0, (short) 0);
		
		// Send webPreview session
		if (getKVMClient().sendMessage(webPreviewSession.array(), webPreviewSession.size()) != webPreviewSession.size())
			return -1;
		
		return 0;
	}
	
	public void setWebPreviewerCaptureStatus(byte status)
	{
		m_webPreviewer_cap_status = status;
	}
	
	public byte getWebPreviewerCaptureStatus()
	{
		return m_webPreviewer_cap_status;
	}

	/**
	 * USer KVM previlage for the user
	 */
	public void OnKvmPrevilage(short User_command) {

		byte User_command_low_byte = (byte) User_command;
		byte User_command_high_byte = (byte) (User_command>>8);


		//from Advsd to client1
		if(User_command_low_byte == KVMSharing.STATUS_KVM_PRIV_SWITCH_MASTER){
			if(kVMDialog !=null){
				if(kVMDialog.getKVMShareRequestDialog() != null)
					kVMDialog.disposeKVMShareReqestDialog();
			}
			if(User_command_high_byte == KVMSharing.KVM_REQ_ALLOWED){
				onKVMFullPermission();
			}
			else{
				onKVMPartialPermission(KVMSharing.KVM_REQ_PARTIAL);
			}
		}


		if(User_command_low_byte == KVMSharing.STATUS_KVM_PRIV_REQ_MASTER)
		{
			Thread thread=null;
			thread = new KVMResponseDialogThread();
			thread.start();
			KVMSharing.KVM_PRIV_RES_USER = KVMSharing.KVM_PRIV_MASTER_USER;
			return;
		}
		else
		{
			//from Advsd to client2 wait state
			if(User_command_low_byte == KVMSharing.STATUS_KVM_PRIV_WAIT_SLAVE)
			{
				String exceptionList[]= {JVMenu.VIDEO_EXIT, JVMenu.HELP_ABOUT_RCONSOLE};
				kVMDialog = new KVMShareDialog();
				kVMDialog.setUserStatus(KVMShareDialog.SECOND_USER);
				if(fullPermissionRequest)
					kVMDialog.constructDialog(KVMShareDialog.KVM_FULL_PERMISSION_REQUEST);
				else
					kVMDialog.constructDialog(KVMShareDialog.KVM_SHARING);
				kVMDialog.showDialog();
				KVMSharing.KVM_PRIV_RES_USER = KVMSharing.KVM_PRIV_SLAVE_USER;
				//disable menu items while waiting for initial permission alone.
				if(!fullPermissionRequest)
					getJVMenu().enableMenu(exceptionList, false, true);
				return;
			}
			else
			{
				//from Advsd to client1, if the client2 quit before the permission given
				if(User_command_low_byte == KVMSharing.STATUS_KVM_PRIV_REQ_CANCEL)
				{
					Debug.out.println("Cancel Packet received");
					kVMDialog.disposeKVMShareResponseDialog();
					if(fullPermissionRequest)
						setFullPermissionRequest(false);
					return;

				}
				else
				{
					//from Advsd to client1, if the client1 doesnt response
					if(User_command_low_byte == KVMSharing.STATUS_KVM_PRIV_REQ_TIMEOUT_TO_MASTER)
					{
						Thread thread=null;
						thread = new KVMRequestDialogThread();				
						this.setMessage(LocaleStrings.getString("D_33_JVAPP")+KVMSharing.KVM_CLIENT_USERNAME+
								LocaleStrings.getString("D_34_JVAPP")+KVMSharing.KVM_CLIENT_IP+
								LocaleStrings.getString("D_35_JVAPP"));
						thread.start();
						if(getM_frame().getConfirmationLabel() != null){
							Window confirmationDialog = SwingUtilities.getWindowAncestor(getM_frame().
									getConfirmationLabel());
							confirmationDialog.setVisible(false);
							confirmationDialog.dispose();
						}
						//setting partial access in case of reuest time out
						KVMSharing.KVM_REQ_GIVEN = KVMSharing.KVM_REQ_PARTIAL;
						KVMShareDialog.isMasterSession = false;
						//close if there is any kvm rsponse dialog is open
						if(kVMDialog !=null){
							if(kVMDialog.getKVMShareRequestDialog() != null)
								kVMDialog.disposeKVMShareReqestDialog();
						}
						getM_frame().onStopVMediaRedirection();
						if(m_wndMode){
							OnChangeMenuState_KVMPartial(getM_wndFrame().getMenu(), false);
							getM_wndFrame().toolbar.OnChangeToolbarIconState_KVMPartial();
						}
						else{
							OnChangeMenuState_KVMPartial(getM_fsFrame().getMenu(), false);
						}
						if(isFullPermissionRequest()){
							setFullPermissionRequest(false);
						}
						else{
							getJVMenu().addFullPermissionMenuItem();
						}
						return;
					}
					else
					{
						//from advsd to client2, packet with permission status
						if(User_command_low_byte == KVMSharing.STATUS_KVM_PRIV_RESPONSE_TO_SLAVE)
						{
							Debug.out.println("#########Got from user2########### and usercommand byte is:"+User_command_high_byte);
							if(kVMDialog != null)
								kVMDialog.disposeKVMShareReqestDialog();

							Thread thread=null;
							thread = new KVMRequestDialogThread();

							if(User_command_high_byte == KVMSharing.KVM_REQ_ALLOWED)
							{
								onKVMFullPermission();
							}
							else if(User_command_high_byte == KVMSharing.KVM_REQ_PARTIAL)
							{
								onKVMPartialPermission(KVMSharing.KVM_REQ_PARTIAL);
							}
							else if(User_command_high_byte == KVMSharing.KVM_REQ_PROGRESS)
							{
								JVFrame frame = JViewerApp.getInstance().getMainWindow();
								InfoDialog.showDialog(frame, LocaleStrings.getString("D_59_JVAPP"), LocaleStrings.getString("D_32_JVAPP"), InfoDialog.INFORMATION_DIALOG);
								onKVMPartialPermission(KVMSharing.KVM_REQ_PROGRESS);
							}
							else if(User_command_high_byte == KVMSharing.KVM_REQ_TIMEOUT)
							{
								Debug.out.println("IVTPPktHdr.KVM_REQ_TIMEOUT");
								kVMDialog.disposeKVMShareReqestDialog();
								this.setMessage(LocaleStrings.getString("D_41_JVAPP")+KVMSharing.KVM_CLIENT_USERNAME+
										LocaleStrings.getString("D_36_JVAPP")+KVMSharing.KVM_CLIENT_IP);
								thread.start();
								//setting full access in case of reuest time out
								KVMSharing.KVM_REQ_GIVEN = KVMSharing.KVM_REQ_ALLOWED;
								KVMShareDialog.isMasterSession = true;
								if(m_wndMode){
									OnChangeMenuState_KVMPartial(getM_wndFrame().getMenu(), true);
									getM_wndFrame().toolbar.OnChangeToolbarIconState_KVMPartial();
								}
								else{
									OnChangeMenuState_KVMPartial(getM_fsFrame().getMenu(), false);
								}
								if(isFullPermissionRequest()){
									setFullPermissionRequest(false);
									getJVMenu().removeFullPermissionMenuItem();
								}
							}
							else if(User_command_high_byte == KVMSharing.KVM_REQ_DENIED)
							{
								Debug.out.println("IVTPPktHdr.KVM_REQ_DENIED");
								kVMDialog.disposeKVMShareReqestDialog();
								KVMSharing.KVM_REQ_GIVEN = KVMSharing.KVM_REQ_DENIED;
								JViewerApp.getInstance().getKVMClient().setM_redirection(true);
								OnVideoStopRedirection();
								JOptionPane.showMessageDialog(JViewerApp.getInstance().getMainWindow(),
										LocaleStrings.getString("D_42_JVAPP"));
								JViewerApp.getInstance().getM_frame().windowClosed();
							}

							else if(User_command_high_byte == KVMSharing.KVM_NOT_MASTER){											
								if(KVMSharing.KVM_REQ_GIVEN == KVMSharing.KVM_REQ_DENIED){
									KVMSharing.KVM_REQ_GIVEN = KVMSharing.KVM_REQ_PARTIAL;
									KVMShareDialog.isMasterSession = false;
									if(m_wndMode){
										OnChangeMenuState_KVMPartial(getM_wndFrame().getMenu(), false);
										getM_wndFrame().toolbar.OnChangeToolbarIconState_KVMPartial();
									}
									else{
										OnChangeMenuState_KVMPartial(getM_fsFrame().getMenu(), false);
									}
									if(isFullPermissionRequest()){
										setFullPermissionRequest(false);
									}
									else{
										getJVMenu().addFullPermissionMenuItem();
									}
								}
								InfoDialog.showDialog(JViewerApp.getInstance().getMainWindow(),LocaleStrings.getString("D_55_JVAPP"),
										LocaleStrings.getString("D_56_JVAPP"),
										InfoDialog.INFORMATION_DIALOG);
							}
							return;
						}
					}
				}
			}
		}
	}


	public boolean OnCheckSameClient(String ip) {
		byte[] ipDgt = null;
		byte[] ipDgt_own= null;
		InetAddress hostAddress = null;
		InetAddress hostAddress_own = null;
		boolean SameIP = false;
		try {
			hostAddress = InetAddress.getByName(ip);
			hostAddress_own = InetAddress.getByName(KVMSharing.KVM_CLIENT_OWN_IP);

			String ipStr = hostAddress.getHostAddress();
			String ipStr_own = hostAddress_own.getHostAddress();
			ipDgt = InetAddress.getByName(ipStr).getAddress();
			ipDgt_own = InetAddress.getByName(ipStr_own).getAddress();
			
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			Debug.out.println(e1);
		}catch (Exception e) {
			// TODO: handle exception
			Debug.out.println(e);
		}

		try {
			if (java.net.InetAddress.getByAddress(ipDgt).equals(
				      java.net.InetAddress.getByAddress(ipDgt_own)))
			{
				Debug.out.println("Equals");
				SameIP =true;
				//JOptionPane.showMessageDialog(null,"ALready One Session is running",LocaleStrings.GetString("H_10_KVMS"),JOptionPane.INFORMATION_MESSAGE);
				//System.exit(0);
			}
			else
			{
				Debug.out.println("Not Equals");
				SameIP = false;
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SameIP;

	}
	/**
	 * Enable or disable Keyboard, Mouse, and Keyboard layout menus and the 
	 * Keyboard/Mouse encryption menu item while partial access is given to 
	 * the concurrent session in KVM sharing.
	 * @param menu - JVMenu
	 * @param state - boolean
	 */
	private void OnChangeMenuState_KVMPartial(JVMenu menu, boolean state)
	{
		if(powerStatus == SERVER_POWER_OFF){
			if(KVMSharing.KVM_REQ_GIVEN == KVMSharing.KVM_REQ_PARTIAL){
				m_frame.getMenu().notifyMenuStateEnable(JVMenu.POWER_ON_SERVER, false);
				m_frame.getMenu().notifyMenuStateEnable(JVMenu.DEVICE_MEDIA_DIALOG, false);
			}
			else if (state) {
				// if state is true and KVM_REG_GIVEN is no KVM_REQ_PARTIAL then KVM Full access is granted, so enable power and media dialogs. 
				m_frame.getMenu().notifyMenuStateEnable(JVMenu.POWER_ON_SERVER, true);
				m_frame.getMenu().notifyMenuStateEnable(JVMenu.DEVICE_MEDIA_DIALOG, true);
				enablePowerControls(state);// Preserve the Power menu items status.
			}
			return;
		}
		/*Exception list is the list of menu oitems that should be excluded while
		changing the state of menu items, when a user is given partial acess 
		permission in KVM sharing*/

		String []commonExceptionList = JVMenu.KVMPartialExceptionMenuItems;
		String exceptionList[]= getExceptionMenuList(commonExceptionList);
		menu.enableMenu(exceptionList, state, true);
		if(state)
			enablePowerControls(state);// Preserve the Power menu items status.
	}
	
	

	/**
	 * Invoke the dialog, Vmedia menuitem clieck from the Menu
	 *
	 */
	public void OnvMedia() {
		if(getM_cdNum() == 0 && getM_fdNum() == 0 && getM_hdNum() == 0){
			InfoDialog.showDialog(JViewer.getMainFrame(), LocaleStrings.getString("D_63_JVAPP"),
					LocaleStrings.getString("G_1_VMD"), InfoDialog.INFORMATION_DIALOG);
			return;
		}
	//if Vmedia dialog is already open, then return
		if(m_mediaDlg != null && m_mediaDlg.isShowing())
			return;
		String serverIP = null;
		// Debug.out.println(m_frame.getServerIP());
		try {
			serverIP = InetAddress.getByAddress(m_serverIP).getHostAddress();
		} catch (UnknownHostException e) {
			Debug.out.println("Invalid IP address");
			Debug.out.println(e);
		}

		m_mediaDlg = new vMediaDialog(JViewer.getMainFrame(),m_IUSBSession, serverIP,
					getCDPort(), getFDPort(),getHDPort(),getM_cdNum(),getM_fdNum(),getM_hdNum(),
					getM_cdStatus(),getM_fdStatus(),getM_hdStatus(),m_bVMUseSSL, getSessionToken());
		if( JViewer.isStandalone() ) {
			m_mediaDlg.DisplayDialog();
		} else {
			JOptionPane optionPane = new JOptionPane(m_mediaDlg.getDialogPanel(),JOptionPane.PLAIN_MESSAGE);
		    optionPane.setOptions(new Object[] {});
		    mediaDlg = optionPane.createDialog(JViewerApp.getInstance().getMainWindow(),
		    			LocaleStrings.getString("G_1_VMD"));
		    mediaDlg.setSize( 780, 650 );
		    mediaDlg.setLocationRelativeTo(null);
		    mediaDlg.setVisible(true);
		}

	}
	/**
	 * @return the m_mediaDlg
	 */
	public vMediaDialog getM_mediaDlg() {
		return m_mediaDlg;
	}

	/**
	 * Invoke the VideoRecording Settings dialog, Settings menu item click from the Menu
	 *
	 */
	public void OnVideoRecordSettings()
	{
		if(m_videorecord == null)
			m_videorecord = new VideoRecord();
		m_videorecord.VideoRecordsettings();
	}
	/**
	 * Abnormal CDROM Redirection failure occurs
	 *
	 */
	public void reportCDROMAbnormal(int device_no) {
		m_IUSBSession.stopCDROMAbnormal(device_no);

		if (m_mediaDlg != null) {
			m_mediaDlg.updateCDROMRedirStatus(device_no);
		}
		else{
			JViewerApp.getInstance().getM_IUSBSession().updateCDToolbarButtonStatus(false);
		}
	}

	/**
	 * Abnormal Floppy Redirection failure occurs
	 *
	 */
	public void reportFloppyAbnormal(int device_no) {
		m_IUSBSession.stopFloppyAbnormal(device_no);

		if (m_mediaDlg != null) {
			m_mediaDlg.updateFloppyRedirStatus(device_no);
		}
		else{
			JViewerApp.getInstance().getM_IUSBSession().updateFDToolbarButtonStatus(false);
		}
	}


	/**
	 * Abnormal Floppy Redirection failure occurs
	 *
	 */
	public void reportHarddiskAbnormal(int device_no) {
		m_IUSBSession.stopHarddiskAbnormal(device_no);

		if (m_mediaDlg != null) {
			m_mediaDlg.updateharddiskRedirStatus(device_no);
		}
		else{
			URL imageHDDR = com.ami.kvm.jviewer.JViewer.class.getResource("res/HD.png");
			JViewerApp.getInstance().getM_wndFrame().toolbar.hardddiskBtn.setIcon(new ImageIcon(imageHDDR));
			JViewerApp.getInstance().getM_wndFrame().toolbar.hardddiskBtn.setToolTipText(
					LocaleStrings.getString("G_24_VMD"));
		}
	}
	/**
	 *	Return the state of the CDRedirection
	 * @return
	 */
	public boolean IsCDROMRedirRunning(int device_no) {
		try{
		if (m_IUSBSession.getCDROMRedirStatus(device_no) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED)
			return true;
		}catch(Exception e){
			Debug.out.println(e);
		}
		return false;
	}

	public boolean IsCDROMRedirRunning() {
		for(int h=0;h<getM_cdNum();h++) {
			try{
			if (m_IUSBSession.getCDROMRedirStatus(h) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED)
				return true;
			}catch(Exception e){
				Debug.out.println(e);
				return false;
			}
		}
		return false;
	}

	/**
	 * Return hte state of the Floppy redirection
	 * @return
	 */
	public boolean IsFloppyRedirRunning(int device_no) {
		try{
		if (m_IUSBSession.getFloppyRedirStatus(device_no) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED)
			return true;
		}catch(Exception e){
			Debug.out.println(e);
		}
		return false;
	}

	public boolean IsFloppyRedirRunning() {

		for(int h=0;h<getM_fdNum();h++) {
			try{
			if (m_IUSBSession.getFloppyRedirStatus(h) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED)
				return true;
			}catch(Exception e){
				Debug.out.println(e);
				return false;
			}
		}
		return false;
	}

	/**
	 * Return hte state of the Harddisk/USB redirection
	 * @return
	 */
	public boolean IsHarddiskRedirRunning(int device_no) {
		try{
		if (m_IUSBSession.getHarddiskRedirStatus(device_no) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED)
			return true;
		}catch(Exception e){
			Debug.out.println(e);
		}
		return false;
	}

	public boolean IsHarddiskRedirRunning() {
		for(int h=0;h<JViewerApp.getInstance().getM_hdNum();h++) {
			try{
			if (m_IUSBSession.getHarddiskRedirStatus(h) == IUSBRedirSession.DEVICE_REDIR_STATUS_CONNECTED)
				return true;
			}catch(Exception e){
				return false;
			}
		}
		return false;
	}


	/***
	 * Return the CDRedirection connection port
	 * @return
	 */
	public int getCDPort() {
		return m_cdPort;
	}

	/**
	 * Return the FloppyRedirection connection port
	 * @return
	 */
	public int getFDPort() {
		return m_fdPort;
	}

	/**
	 * Return the HarddiskRedirection connection port
	 * @return
	 */
	public int getHDPort() {
		return m_hdPort;
	}
	/***
	 * Return the CDRedirection connection Status
	 * @return
	 */
	public int getCDStatus() {
		return m_cdStatus;
	}

	/**
	 * Return the FloppyRedirection connection Status
	 * @return
	 */
	public int getFDStatus() {
		return m_fdStatus;
	}

	/**
	 * Return the HarddiskRedirection connection Status
	 * @return
	 */
	public int getHDStatus() {
		return m_hdStatus;
	}

	/**
	 * Return the websession token string
	 * @return
	 */
	public String getM_webSession_token() {
		return m_webSession_token;
	}

	//Added for JInternalFrame
	/**
	 * 
	 * @return
	 */
	public JVFrame getM_frame() {
		return m_frame;
	}
	
	/**
	 * Return the Fullscreen frame instance
	 * @return
	 */
	public FSFrame getM_fsFrame() {
		return m_fsFrame;
	}

	/**
	 * Return the state of the JViewer Fullscreen / window mode
	 *
	 * @return
	 */
	public boolean isM_wndMode() {
		return m_wndMode;
	}

	/**
	 * Set the windowmode whetehr it is fullscreen or window mode
	 * @param mode
	 */
	public void setM_wndMode(boolean mode) {
		m_wndMode = mode;
	}

	/**
	 * Return the LED status of the Client
	 * @return
	 */
	public byte getLed_status() {
		return Led_status;
	}

	/**
	 * Set the LED status of the Client
	 * @param led_status
	 */
	public void setLed_status(byte led_status) {
		Led_status = led_status;
	}

	/**
	 * Return the SOC App instance
	 * @return
	 */
	public ISOCApp getSoc_App() {
		return soc_App;
	}

	/**
	 * Set the  SOC App instance
	 * @param soc_App
	 */
	public void setSoc_App(ISOCApp soc_App) {
		this.soc_App = soc_App;
	}

	/**
	 * Return the Viewpane instance
	 * @return
	 */
	public JViewerView getM_view() {
		return m_view;
	}

	/**
	 * Set the Viewpane instance
	 * @return
	 */
	public void setM_view(JViewerView m_view) {
		this.m_view = m_view;
	}

	/**
	 * Return the SOCmanager common interface instance
	 * @return
	 */
	public static ISOCManager getSoc_manager() {
		return soc_manager;
	}

	/**
	 * Return the SOCFrameHdr interface instance
	 * @return
	 */
	public ISOCFrameHdr getSocframeHdr() {
		return socframeHdr;
	}

	/**
	 * Set the SOCFrameHdr interface instance
	 * @param socframeHdr
	 */
	public void setSocframeHdr(ISOCFrameHdr socframeHdr) {
		this.socframeHdr = socframeHdr;
	}

	/**
	 * Get Window frame instance
	 * @return
	 */
	public WindowFrame getM_wndFrame() {
		return m_wndFrame;
	}

	/**
	 * Set Window frame instance
	 * @param frame
	 */
	public void setM_wndFrame(WindowFrame frame) {
		m_wndFrame = frame;
	}

	/**
	 * Return the JVVideo interface instance
	 * @return
	 */
	public JVVideo getVidClnt() {
		return vidClnt;
	}

	/**
	 * Set the JVVideo interface instance
	 * @return
	 */
	public void setVidClnt(JVVideo vidClnt) {
		this.vidClnt = vidClnt;
	}

	/**
	 * Return the Imagebuffer create instance
	 * @return
	 */
	public ISOCCreateBuffer getPrepare_buf() {
		return prepare_buf;
	}

	public ISOCKvmClient getSockvmclient() {
		return sockvmclient;
	}

	public void setSockvmclient(ISOCKvmClient sockvmclient) {
		this.sockvmclient = sockvmclient;
	}

	/**
	 * Enable /Disable Auto Detect keyboard layout
	 * @param state - true to enable and false to disable
	 */
	public void onAutoKeyboardLayout(boolean state,boolean isMenu){
		getJVMenu().getMenuItem(JVMenu.PKBRD_NONE).setSelected(true);
	
		if(state == true) {
			JVMenu.keyBoardLayout *= -1;
			getJVMenu().notifyMenuStateSelected(JVMenu.AUTOMAIC_LANGUAGE, true);

			JViewer.setKeyboardLayout(JViewer.AUTO_DETECT_KEYBOARD);
			if(isMenu == true){
				if((JViewer.getOEMFeatureStatus() & JViewerApp.OEM_SET_PHYSICAL_KBD_LANG) ==
					JViewerApp.OEM_SET_PHYSICAL_KBD_LANG){
					//Set keyboard layout language as default.
					String msg = LocaleStrings.getString("S_22_SACD")+" "+LocaleStrings.getString("S_21_SACD");
					InfoDialog.showDialog(m_frame, LocaleStrings.getString("M_1_ID")+msg+"?",
							msg,
							InfoDialog.CONFIRMATION_DIALOG,5000,InfoDialog.HOST_KBD_LANG);
				}
			}

			if(getAutokeylayout() == null){
				setAutokeylayout(new AutoKeyboardLayout());
				getAutokeylayout().setKeyboardType(AutoKeyboardLayout.KBD_TYPE_AUTO);
				getAutokeylayout().setHostKeyboardType(AutoKeyboardLayout.KBD_TYPE_AUTO);
			}
			else{
				getAutokeylayout().setKeyboardType(AutoKeyboardLayout.KBD_TYPE_AUTO);
				getAutokeylayout().setHostKeyboardType(AutoKeyboardLayout.KBD_TYPE_AUTO);
				getAutokeylayout().initKeyProcessor();
				getAutokeylayout().getKeyboardType();
				getAutokeylayout().ongetKeyprocessor();
			}
			OnSkbrdDisplay(-1);
		}
		else {
			if(JVMenu.keyBoardLayout != -1)
				JVMenu.keyBoardLayout *= -1;
			getJVMenu().getMenu(JVMenu.SOFTKEYBOARD).setEnabled(true);
			getJVMenu().notifyMenuStateSelected(JVMenu.AUTOMAIC_LANGUAGE, false);
			getJVMenu().SetMenuEnable(JVMenu.SOFTKEYBOARD, true);
			getJVMenu().m_menuItems_setenabled.put(JVMenu.SOFTKEYBOARD, true);
		}
		JViewerApp.getInstance().getM_USBKeyRep().setM_USBKeyProcessor(JViewerApp.getInstance().getKeyProcesssor());
	}

	public AutoKeyboardLayout getAutokeylayout() {
		return autokeylayout;
	}

	public void setAutokeylayout(AutoKeyboardLayout autokeylayout) {
		this.autokeylayout = autokeylayout;
	}

	public KeyProcessor getKeyProcesssor() {
		if(autokeylayout == null) {
			autokeylayout = new AutoKeyboardLayout();
			keyprocessor =  new USBKeyProcessorEnglish();
		}
		else {
			keyprocessor = autokeylayout.ongetKeyprocessor();
		}
		keyprocessor.setAutoKeybreakMode(true);
		return keyprocessor;
	}
	
	public void setKeyProcessor(String keyboardLayout){
		int keyBoardType = AutoKeyboardLayout.KBD_TYPE_ENGLISH;

		if(JViewer.getKeyboardLayout().equals(keyboardLayout))
			return;

		if(keyboardLayout.equalsIgnoreCase("en"))
			keyBoardType = AutoKeyboardLayout.KBD_TYPE_ENGLISH;
		else if(keyboardLayout.equalsIgnoreCase("fr"))
			keyBoardType = AutoKeyboardLayout.KBD_TYPE_FRENCH;
		else if(keyboardLayout.equalsIgnoreCase("de"))
			keyBoardType = AutoKeyboardLayout.KBD_TYPE_GERMAN;
		else if(keyboardLayout.equalsIgnoreCase("jp"))
			keyBoardType = AutoKeyboardLayout.KBD_TYPE_JAPANESE;
		else if(keyboardLayout.equalsIgnoreCase("es"))
			keyBoardType = AutoKeyboardLayout.KBD_TYPE_SPANISH;

		if(autokeylayout == null)
			autokeylayout = new AutoKeyboardLayout();

		getJVMenu().notifyMenuStateSelected(JVMenu.AUTOMAIC_LANGUAGE, false);
		//enable Softkeyboard menu
		if(getJVMenu().getMenuEnable(JVMenu.SOFTKEYBOARD) == false){
			getJVMenu().notifyMenuEnable(JVMenu.SOFTKEYBOARD, true);
			JVMenu.keyBoardLayout *= -1;
		}
		if (softKeyboard != null) {
			softKeyboard.m_skmouselistener.close();
			softKeyboard.dispose();
		}
		autokeylayout.setHostKeyboardType(keyBoardType);
		JViewer.setKeyboardLayout(keyboardLayout);
		getM_USBKeyRep().setM_USBKeyProcessor(getKeyProcesssor());

		if((JViewer.getOEMFeatureStatus() & JViewerApp.OEM_SET_PHYSICAL_KBD_LANG) ==
			JViewerApp.OEM_SET_PHYSICAL_KBD_LANG){
			//Set keyboard layout language as default.
			String msg = LocaleStrings.getString("S_22_SACD")+" "+LocaleStrings.getString("S_21_SACD");
			InfoDialog.showDialog(m_frame, LocaleStrings.getString("M_1_ID")+msg+"?",
					msg,
					InfoDialog.CONFIRMATION_DIALOG,5000,InfoDialog.HOST_KBD_LANG);
		}
	}

	public USBKeyboardRep getM_USBKeyRep() {
		return m_USBKeyRep;
	}

	public void setM_USBKeyRep(USBKeyboardRep keyRep) {
		m_USBKeyRep = keyRep;
	}

	public SoftKeyboard getSoftKeyboard() {
		return softKeyboard;
	}

	public void setSoftKeyboard(SoftKeyboard softKeyboard) {
		this.softKeyboard = softKeyboard;
	}

	public IUSBRedirSession getM_IUSBSession() {
		return m_IUSBSession;
	}
	public void setM_IUSBSession(IUSBRedirSession session) {
		m_IUSBSession = session;
	}
	public VideoRecord getM_videorecord() {
		return m_videorecord;
	}
	public void setM_videorecord(VideoRecord m_videorecord) {
		this.m_videorecord = m_videorecord;
	}

	public AddMacro getAddMacro() {
		return addMacro;
	}
	public void setAddMacro(AddMacro addMacro) {
		this.addMacro = addMacro;
	}
	
	/**
	 * @return the userDefMacro
	 */
	public UserDefMacro getUserDefMacro() {
		return userDefMacro;
	}

	public void OnAddMacro() {
		if(JViewerApp.getInstance().getAddMacro() == null)
			return;
		userDefMacro = new UserDefMacro(JViewer.getMainFrame());
	}

	public void OnVideoZoomIn() {

		m_zoomSliderValue = getM_wndFrame().toolbar.slider_zoom.getValue();
		if(JVMenu.m_scale <= 1.5F) {
			BigDecimal rate=new BigDecimal(JVMenu.m_scale);
			BigDecimal cost=new BigDecimal("0.1");
			BigDecimal stepped_value = rate.add(cost).setScale(2, BigDecimal.ROUND_HALF_UP);
			JVMenu.m_scale=stepped_value.floatValue();
			m_zoomSliderValue += 10;
			getM_wndFrame().toolbar.slider_zoom.setValue(m_zoomSliderValue);
			JViewerApp.getInstance().getRCView().revalidate();
	        JViewerApp.getInstance().getRCView().repaint();
		}

        if(JVMenu.m_scale >= 0.5F) {
			getJVMenu().SetMenuEnable(JVMenu.ZOOM_OUT, true);
			getJVMenu().getMenuItem(JVMenu.ZOOM_OUT).setEnabled(true);
		} 
        
        if(JVMenu.m_scale >= 1.5F) {
        	getJVMenu().SetMenuEnable(JVMenu.ZOOM_IN, false);
			getJVMenu().getMenuItem(JVMenu.ZOOM_IN).setEnabled(false);
		}
		//solved changing Zoom Size will make mouse cursor inconsist
		if(m_zoomSliderValue == 100){
			boolean cursurMenuState = JViewerApp.getInstance().getJVMenu().getMenuSelected(JVMenu.MOUSE_CLIENTCURSOR_CONTROL);
			if (cursurMenuState)
				Mousecaliberation.resetCursor();
		}
	}
	public void OnVideoZoomOut() {

		m_zoomSliderValue = getM_wndFrame().toolbar.slider_zoom.getValue();
		if(JVMenu.m_scale >= 0.5F)
		{
			BigDecimal rate=new BigDecimal(JVMenu.m_scale);
			BigDecimal cost=new BigDecimal("0.1");
			BigDecimal stepped_value = rate.subtract(cost).setScale(2, BigDecimal.ROUND_HALF_UP);
			JVMenu.m_scale=stepped_value.floatValue();
			m_zoomSliderValue -= 10;
			getM_wndFrame().toolbar.slider_zoom.setValue(m_zoomSliderValue);
			JViewerApp.getInstance().getRCView().revalidate();
	        JViewerApp.getInstance().getRCView().repaint();
		}

		if(JVMenu.m_scale <= 0.5F) {
			getJVMenu().SetMenuEnable(JVMenu.ZOOM_OUT, false);
			getJVMenu().getMenuItem(JVMenu.ZOOM_OUT).setEnabled(false);
		} 
		if(JVMenu.m_scale >= 0.5F) {
        	getJVMenu().SetMenuEnable(JVMenu.ZOOM_IN, true);
			getJVMenu().getMenuItem(JVMenu.ZOOM_IN).setEnabled(true);
		}

		//solved changing Zoom Size will make mouse cursor inconsist
		if(m_zoomSliderValue == 100){
			boolean cursurMenuState = JViewerApp.getInstance().getJVMenu().getMenuSelected(JVMenu.MOUSE_CLIENTCURSOR_CONTROL);			
			if (cursurMenuState)
				Mousecaliberation.resetCursor();
		}
	}

	/**
	 * Change the zoom option settings
	 * @param option - The option to be set.
	 */
	public void onChangeZoomOptions(String option){
		//return if JVIewer windows is out of focus
		if(JViewerView.syncLEDFlag == true)
			return;
		GraphicsConfiguration gc = JViewerApp.getInstance().getM_wndFrame().getGraphicsConfiguration();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets screenInsets = new Insets(0, 0, 0, 0);
		try{
			screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
		}catch (NullPointerException e) {
		}
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		int systemComponentsHeight = screenInsets.top + screenInsets.bottom;
		int systemComponentsWidth = screenInsets.left+screenInsets.right;
		int frameHeight = screenHeight - systemComponentsHeight;
		int frameWidth = screenWidth - systemComponentsWidth;

		if(option.equals(JVMenu.ACTUAL_SIZE)){
			if(zoomOption != JVMenu.ZOOM_OPTION_NONE){
				//set the JViewer frame size to the size prior to focus loss.
				if(!isRenderFitToHost() && m_wndFrame.isResizeFrame()){
					JViewer.getMainFrame().setSize(m_wndFrame.getFrameDimension());
				}
				else{//set the JViewer frame size to the size of the desktop.
					JViewer.getMainFrame().setSize(frameWidth, frameHeight);
					JViewer.getMainFrame().setLocationRelativeTo(null);
				}
			}
			setZoomOption(option);
			getM_wndFrame().toolbar.resetZoom();
			if(getRCView().GetUSBMouseMode() != USBMouseRep.OTHER_MOUSE_MODE){
				JViewer.getMainFrame().setResizable(true);
				JViewerApp.getInstance().getMainWindow().m_viewSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				JViewerApp.getInstance().getMainWindow().m_viewSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			}
			getM_wndFrame().toolbar.enableZoomSlider(true);
			getJVMenu().notifyMenuStateEnable(JVMenu.ZOOM_IN, true);
			getJVMenu().notifyMenuStateEnable(JVMenu.ZOOM_OUT, true);
			getJVMenu().notifyMenuStateSelected(JVMenu.ACTUAL_SIZE, true);
		}
		else if(option.equals(JVMenu.FIT_TO_CLIENT_RES)){
			getM_wndFrame().toolbar.resetZoom();

			JViewerApp.getInstance().getMainWindow().m_viewSP.getViewport().setViewPosition(new Point(0, 0));
			/*Code to set window as not resizable*/
			JViewerApp.getInstance().getMainWindow().m_viewSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			JViewerApp.getInstance().getMainWindow().m_viewSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			JViewer.getMainFrame().setSize(frameWidth, frameHeight);
			JViewerApp.getInstance().getRCView().revalidate();
			JViewerApp.getInstance().getRCView().repaint();
			JViewer.getMainFrame().setResizable(false);
			JViewer.getMainFrame().setLocationRelativeTo(null);
			setZoomOption(option);
			getM_wndFrame().toolbar.enableZoomSlider(false);
			if(getRCView().GetUSBMouseMode() == USBMouseRep.RELATIVE_MOUSE_MODE){
				getM_wndFrame().toolbar.changeShowCursorOnZoom();
			}
			getJVMenu().notifyMenuStateEnable(JVMenu.ZOOM_IN, false);
			getJVMenu().notifyMenuStateEnable(JVMenu.ZOOM_OUT, false);
			getJVMenu().notifyMenuStateSelected(JVMenu.FIT_TO_CLIENT_RES, true);
		}
		else if(option.equals(JVMenu.FIT_TO_HOST_RES)){
			int videoResX = getSocframeHdr().getresX();
			int videoResY = getSocframeHdr().getresY();
			//addedHeight is the height of the titlebar, menubar, toolbar and the statusbar, that should be taken into account
			//while calculating teh window height.
			int addedHeight = JViewer.getMainFrame().getInsets().top + JViewer.getMainFrame().getInsets().bottom+
			JViewerApp.getInstance().getM_wndFrame().getWindowMenu().getMenuBar().getHeight()+
			JViewerApp.getInstance().getM_wndFrame().toolbar.getToolBar().getHeight()+
			JViewerApp.getInstance().getM_wndFrame().toolbar.getToolBar().getInsets().top+
			JViewerApp.getInstance().getM_wndFrame().toolbar.getToolBar().getInsets().bottom+
			JViewerApp.getInstance().getM_wndFrame().getM_status().getStatusBar().getHeight()+
			JViewerApp.getInstance().getM_wndFrame().getM_status().getStatusBar().getInsets().top+
			JViewerApp.getInstance().getM_wndFrame().getM_status().getStatusBar().getInsets().bottom;

			int addedWidth = JViewer.getMainFrame().getInsets().left + JViewer.getMainFrame().getInsets().left+
								getRCView().getInsets().left + getRCView().getInsets().right;


			Dimension clientScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
			
			frameWidth = videoResX + addedWidth;
			frameHeight = videoResY + addedHeight;

			if((frameWidth + systemComponentsWidth) < clientScreenSize.width &&
					(frameHeight + systemComponentsHeight) < clientScreenSize.height){
				getM_wndFrame().toolbar.resetZoom();
				JViewerApp.getInstance().getMainWindow().m_viewSP.getViewport().setViewPosition(new Point(0, 0));
				JViewer.getMainFrame().setExtendedState(JFrame.NORMAL);
				if(videoResX >= JViewer.MIN_FRAME_WIDTH && videoResY >= JViewer.MIN_FRAME_HEIGHT)
					JViewer.getMainFrame().setSize(frameWidth, frameHeight);
				else
					JViewer.getMainFrame().setSize(JViewer.MIN_FRAME_WIDTH, JViewer.MIN_FRAME_HEIGHT);
				JViewerApp.getInstance().getMainWindow().m_viewSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				JViewerApp.getInstance().getMainWindow().m_viewSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
				JViewer.getMainFrame().setResizable(false);

				setZoomOption(option);
				if(getRCView().GetUSBMouseMode() == USBMouseRep.RELATIVE_MOUSE_MODE)
					getM_wndFrame().toolbar.changeShowCursorOnZoom();
				getM_wndFrame().toolbar.enableZoomSlider(false);
				getJVMenu().notifyMenuStateEnable(JVMenu.ZOOM_IN, false);
				getJVMenu().notifyMenuStateEnable(JVMenu.ZOOM_OUT, false);
				getJVMenu().notifyMenuStateSelected(JVMenu.FIT_TO_HOST_RES, true);
			}
		}
		getM_wndFrame().getM_status().resetStatus();
		m_wndFrame.setResizeFrame(false);
	}
	/**
	 * @return the zoomOption
	 */
	public String getZoomOption() {
		return zoomOption;
	}

	/**
	 * @param zoomOption the zoomOption to set
	 */
	public void setZoomOption(String zoomOption) {
		this.zoomOption = zoomOption;
	}

	/**
	 * Invoked once the power status is recieved from the server.
	 * @param pwrStatus - SERVER_POWER_ON or SERVER_POWER_OFF 
	 */
	public void onGetPowerControlStatus(byte pwrStatus){
		if(pwrStatus == SERVER_POWER_ON){// server power on
			powerStatus = SERVER_POWER_ON;
			try{
				if(getVidClnt().getPsMonitor() != null){
					getVidClnt().getPsMonitor().cancel();
					getVidClnt().setPsMonitor(null);
				}
				if(getVidClnt().getPsMonitorTimer() != null){
					getVidClnt().getPsMonitorTimer().cancel();
					getVidClnt().setPsMonitorTimer(null);
				}
			}catch(Exception e){
				Debug.out.println(e);
				e.printStackTrace();
			}
		}
		else if(pwrStatus == SERVER_POWER_OFF){// server power off
			powerStatus = SERVER_POWER_OFF;
		}
		enablePowerControls(true);		
	}
	/**
	 * Sends the power control command to the KVMClient.
	 * @param command command to be executed.
	 */
	public void onSendPowerControlCommand(String command){

		//jviewer session doesn't have privilege to execute power commands
		if(!JViewer.isPowerPrivEnabled())
			return ;

		JInternalFrame frame = JViewerApp.getInstance().getMainWindow();
		int ret = JOptionPane.showConfirmDialog(frame,LocaleStrings.getString("D_43_JVAPP")+command+
					LocaleStrings.getString("D_44_JVAPP"), LocaleStrings.getString("D_45_JVAPP"), 
					JOptionPane.YES_NO_OPTION);
		if(ret == JOptionPane.YES_OPTION){
			enablePowerControls(false);
			if(command.equals(JVMenu.POWER_RESET_SERVER))
				 m_KVMClnt.sendPowerControlCommand(IVTPPktHdr.IVTP_POWER_CONTROL_HARD_RESET);
			else if(command.equals(JVMenu.POWER_OFF_IMMEDIATE))
				 m_KVMClnt.sendPowerControlCommand(IVTPPktHdr.IVTP_POWER_CONTROL_OFF_IMMEDIATE);
			else if(command.equals(JVMenu.POWER_OFF_ORDERLY))
				m_KVMClnt.sendPowerControlCommand(IVTPPktHdr.IVTP_POWER_CONTROL_SOFT_RESET);
			else if(command.equals(JVMenu.POWER_ON_SERVER))
				m_KVMClnt.sendPowerControlCommand(IVTPPktHdr.IVTP_POWER_CONTROL_ON);
			else if(command.equals(JVMenu.POWER_CYCLE_SERVER))
				m_KVMClnt.sendPowerControlCommand(IVTPPktHdr.IVTP_POWER_CONTROL_CYCLE);			
		}
		
	}
	/**
	 * Enables or disables power control menu items and power status toolbar button. 
	 * It also updates the state of these controls according to the power status. 
	 * @param state - true - to enable the controls; false to disable them.
	 */
	private void enablePowerControls(boolean state){
		m_wndFrame.toolbar.powerBtn.setEnabled(state);
		if(state){
			if(powerStatus == SERVER_POWER_OFF){
				String exceptmenu[]={JVMenu.VIDEO_CAPTURE_SCREEN, JVMenu.VIDEO_EXIT, JVMenu.HELP_ABOUT_RCONSOLE,
										JVMenu.POWER_ON_SERVER, JVMenu.DEVICE_MEDIA_DIALOG};
				m_frame.getMenu().enableMenu(exceptmenu, false, false);// disable menus when server power off
				m_wndFrame.toolbar.turnOnPowerButton(false);
				if(KVMSharing.KVM_REQ_GIVEN == KVMSharing.KVM_REQ_PARTIAL){
					m_frame.getMenu().notifyMenuStateEnable(JVMenu.POWER_ON_SERVER, false);
					m_frame.getMenu().notifyMenuStateEnable(JVMenu.DEVICE_MEDIA_DIALOG, false);
				}
				else{
					m_frame.getMenu().notifyMenuStateEnable(JVMenu.POWER_ON_SERVER, true);
					m_frame.getMenu().notifyMenuStateEnable(JVMenu.DEVICE_MEDIA_DIALOG, true);
					m_wndFrame.toolbar.setButtonEnabled(m_wndFrame.toolbar.powerBtn, true);
				}
				m_wndFrame.toolbar.changeToolbarButtonStateOnPowerStatus(false);
			}
			else{
				//Enable gui controls if redirection is not paused. 
				if(m_RedirectionState == REDIR_STARTED){
					changeMenuItemsStatusOnPauseResume(m_frame.getMenu(), true);// change menu staes to default state.
					m_wndFrame.toolbar.changeToolbarButtonStateOnPowerStatus(true);
					m_frame.getMenu().notifyMenuStateEnable(JVMenu.POWER_ON_SERVER, false);				
					m_wndFrame.toolbar.turnOnPowerButton(true);
					getVidClnt().setZoomOptionStatus();
				}
			}
		}
	}
	/**
	 * Displays success or failure message after recieving the power control response from the server.
	 * @param response - response from the server.
	 */
	public void onPowerControlResponse(byte response){
		if(response != 0)
			InfoDialog.showDialog(JViewerApp.getInstance().getMainWindow(),
					LocaleStrings.getString("D_46_JVAPP"), LocaleStrings.getString("D_45_JVAPP"),
					InfoDialog.ERROR_DIALOG);
		else
			InfoDialog.showDialog(JViewerApp.getInstance().getMainWindow(), 
					LocaleStrings.getString("D_47_JVAPP"), LocaleStrings.getString("D_45_JVAPP"),
					InfoDialog.INFORMATION_DIALOG);
		enablePowerControls(true);
	}
	public Mousecaliberation getMousecaliberation() {
		return Mousecaliberation;
	}

	public void setMousecaliberation(Mousecaliberation mousecaliberation) {
		Mousecaliberation = mousecaliberation;
	}

	/**
	 * Get the number of CD media instances allowed
	 */
	public int getM_cdNum() {
		return m_cdNum;
	}

	/**
	 * Set the number of CD media instances allowed
	 * @param num - number of instances
	 */
	public void setM_cdNum(int num) {
		String[][] tempPath = null;
		int lastCDNum = m_cdNum;
		tempPath = Imagepath_CD;
		m_cdNum = num;
		JViewerApp.Imagepath_CD = new String[m_cdNum][JViewerApp.MAX_IMAGE_PATH_COUNT];
		if(lastCDNum > num)
			lastCDNum = num;
		if(tempPath != null){
			for(int count = 0; count < lastCDNum; count++){
				Imagepath_CD[count] = tempPath[count];
			}
		}
	}

	/**
	 * Get the number of FD media instances allowed
	 */
	public int getM_fdNum() {
		return m_fdNum;
	}

	/**
	 * Set the number of FD media instances allowed
	 * @param num - number of instances
	 */
	public void setM_fdNum(int num) {
		String[][] tempPath = null;
		int lastFDNum = m_fdNum;
		tempPath = Imagepath_Floppy;
		m_fdNum = num;
		JViewerApp.Imagepath_Floppy = new String[m_fdNum][JViewerApp.MAX_IMAGE_PATH_COUNT];
		if(lastFDNum > num)
			lastFDNum = num;
		if(tempPath != null){
			for(int count = 0; count < lastFDNum; count++){
				Imagepath_Floppy[count] = tempPath[count];
			}
		}
	}

	/**
	 * Get the number of HD media instances allowed
	 */
	public int getM_hdNum() {
		return m_hdNum;
	}

	/**
	 * Set the number of HD media instances allowed
	 * @param num - number of instances
	 */
	public void setM_hdNum(int num) {
		String[][] tempPath = null;
		int lastHDNum = m_hdNum;
		tempPath = Imagepath_Harddsik;
		m_hdNum = num;
		if(lastHDNum > num)
			lastHDNum = num;
		JViewerApp.Imagepath_Harddsik = new String[m_hdNum][JViewerApp.MAX_IMAGE_PATH_COUNT];
		if(lastHDNum > 0 && tempPath != null){
			for(int count = 0; count < lastHDNum; count++){
				Imagepath_Harddsik[count] = tempPath[count];
			}
		}
	}
	public int getM_cdStatus() {
		return m_cdStatus;
	}
	public int getM_fdStatus() {
		return m_fdStatus;
	}
	public int getM_hdStatus() {
		return m_hdStatus;
	}
	public void setM_cdStatus(int status) {
		m_cdStatus = status;		
	}
	public void setM_fdStatus(int status) {
		m_fdStatus = status;
	}
	public void setM_hdStatus(int status) {
		m_hdStatus = status;
	}
	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public VideoRecordApp getVideorecordapp() {
		return videorecordapp;
	}

	public void setVideorecordapp(VideoRecordApp videorecordapp) {
		this.videorecordapp = videorecordapp;
	}
	public int getM_hdPort() {
		return m_hdPort;
	}
	public void setM_hdPort(int port) {
		m_hdPort = port;
	}
	public int getM_fdPort() {
		return m_fdPort;
	}
	public void setM_fdPort(int port) {
		m_fdPort = port;
	}
	public int getM_cdPort() {
		return m_cdPort;
	}
	public void setM_cdPort(int port) {
		m_cdPort = port;
	}
	public boolean isM_bVMUseSSL() {
		return m_bVMUseSSL;
	}
	public void setM_bVMUseSSL(boolean useSSL) {
		m_bVMUseSSL = useSSL;
	}

	/**
	 * @return the fullKeyboardEnabled
	 */
	public boolean isFullKeyboardEnabled() {
		return fullKeyboardEnabled;
	}

	/**
	 * @param fullKeyboardEnabled the fullKeyboardEnabled to set
	 */
	public void setFullKeyboardEnabled(boolean fullKeyboardEnabled) {
		this.fullKeyboardEnabled = fullKeyboardEnabled;
	}

	/**
	 * @return the fullPermissionRequest
	 */
	public boolean isFullPermissionRequest() {
		return fullPermissionRequest;
	}

	/**
	 * @param fullPermissionRequest the fullPermissionRequest to set
	 */
	public void setFullPermissionRequest(boolean fullPermissionRequest) {
		this.fullPermissionRequest = fullPermissionRequest;
	}

	public boolean syncVMediaRedirection(){
		boolean isMediaRunning = false;
		synchronized (CDROMRedir.getSyncObj()) {
			for(int count = 0; count< JViewerApp.getInstance().getM_cdNum();count++)
				if(JViewerApp.getInstance().getUSBRedirSession().getCdromSession(count) != null)
				{
					JViewerApp.getInstance().getUSBRedirSession().getCdromSession(count).setConfModified(true);
					if(JViewerApp.getInstance().getUSBRedirSession().getCdromSession(count).isRedirActive())
						isMediaRunning = true;	
				}	
			CDROMRedir.getSyncObj().notifyAll();
		}
		synchronized (FloppyRedir.getSyncObj()) {
			for(int count = 0; count< JViewerApp.getInstance().getM_fdNum();count++)
				if(JViewerApp.getInstance().getUSBRedirSession().getFloppySession(count) != null)
				{
					JViewerApp.getInstance().getUSBRedirSession().getFloppySession(count).setConfModified(true);
					if(JViewerApp.getInstance().getUSBRedirSession().getFloppySession(count).isRedirActive())
						isMediaRunning = true;
				}
			FloppyRedir.getSyncObj().notifyAll();
		}
		synchronized (HarddiskRedir.getSyncObj()) {
			for(int count = 0; count< JViewerApp.getInstance().getM_hdNum();count++)
				if(JViewerApp.getInstance().getUSBRedirSession().getHarddiskSession(count) != null)
				{
					JViewerApp.getInstance().getUSBRedirSession().getHarddiskSession(count).setConfModified(true);
					if(JViewerApp.getInstance().getUSBRedirSession().getHarddiskSession(count).isRedirActive())
						isMediaRunning = true;
				}
			HarddiskRedir.getSyncObj().notifyAll();
		}
		return isMediaRunning;
	}
    // Used to send the mouse mode data to the adviserd 
	public void OnSendMouseMode(byte mouseMode)
	{
		// validation for the redirection flag. If redirection is not active, it return simply
		if (!m_KVMClnt.redirection()) return;
		// Sending mouse mode to the adviserd
		if (m_KVMClnt.SendMouseMode(mouseMode) == 1)
			Debug.out.println("Mouse mode send failured");
		Debug.out.println("Mouse mode send success");
	}

	public void OnSelectKVMMaster(){
		if((KVMClient.getNumUsers() > 1 )&& (KVMSharing.KVM_REQ_GIVEN == KVMSharing.KVM_REQ_ALLOWED)){
			if(kVMDialog == null)
				kVMDialog = new KVMShareDialog();
			kVMDialog.constructDialog(KVMShareDialog.KVM_SELECT_MASTER);
			kVMDialog.showDialog();
		}
	}
	/**
	 * @return the kVMDialog
	 */
	public KVMShareDialog getKVMShareDialog() {
		return kVMDialog;
	}

	/**
	 * @return the responseDialogTable
	 */
	public Hashtable<String, JDialog> getResponseDialogTable() {
		return responseDialogTable;
	}

	/**
	 * Initialize the KVM response Dialog HashTable
	 */
	public void initResponseDialogTable() {
		if(responseDialogTable == null)
			responseDialogTable = new Hashtable<String, JDialog>();
	}

	/**
	 * @param kVMDialog the kVMDialog to set
	 */
	public void setKVMDialog(KVMShareDialog kVMDialog) {
		this.kVMDialog = kVMDialog;
	}

	public void sendSelectedMasterInfo(String masterInfo){
		ByteBuffer masterDataBuffer = getKVMClient().getUserDataPacket().createUserDataBuffer(masterInfo);
		getKVMClient().sendNextMasterInfo(masterDataBuffer);
	}
	
	/**
	 * Handle the changes to be done when Full KVM Privilege is receved.
	 */
	private void onKVMFullPermission(){
		Thread thread=null;
		thread = new KVMRequestDialogThread();
		if ((KVMSharing.KVM_CLIENT_USERNAME != null) && (KVMSharing.KVM_CLIENT_IP != null))
			this.setMessage(LocaleStrings.getString("D_38_JVAPP")+KVMSharing.KVM_CLIENT_USERNAME+
					LocaleStrings.getString("D_39_JVAPP")+KVMSharing.KVM_CLIENT_IP);
		else	// No Client and Username, so there is no other concurrent master user to provide the access.
			this.setMessage(LocaleStrings.getString("D_51_JVAPP"));
		thread.start();
		KVMSharing.KVM_REQ_GIVEN = KVMSharing.KVM_REQ_ALLOWED;
		KVMShareDialog.isMasterSession = true;
		if(isFullPermissionRequest()){
			JViewerApp.getInstance().getJVMenu().removeFullPermissionMenuItem();
			if(m_wndMode){
				OnChangeMenuState_KVMPartial(getM_wndFrame().getMenu(), true);
				getM_wndFrame().toolbar.OnChangeToolbarIconState_KVMPartial();
			}
			else{
				OnChangeMenuState_KVMPartial(getM_fsFrame().getMenu(), true);
			}
		}
	
	}
	
	/**
	 * Handle the changes to be done when Partial KVM Privilege is receved.
	 */
	private void onKVMPartialPermission(byte permission){


		Debug.out.println("IVTPPktHdr.KVM_REQ_PARTIAL");
		if(permission == KVMSharing.KVM_REQ_PARTIAL)
		{
			Thread thread=null;
			thread = new KVMRequestDialogThread();
			if(KVMSharing.KVM_REQ_GIVEN == KVMSharing.KVM_REQ_ALLOWED){
				this.setMessage(LocaleStrings.getString("D_57_JVAPP")+
						KVMSharing.KVM_CLIENT_USERNAME+LocaleStrings.getString("D_39_JVAPP")
						+KVMSharing.KVM_CLIENT_IP);
			}
			else{
				this.setMessage(LocaleStrings.getString("D_40_JVAPP")+
						KVMSharing.KVM_CLIENT_USERNAME+LocaleStrings.getString("D_39_JVAPP")
						+KVMSharing.KVM_CLIENT_IP);
			}
			thread.start();
		}
		KVMSharing.KVM_REQ_GIVEN = KVMSharing.KVM_REQ_PARTIAL;
		KVMShareDialog.isMasterSession = false;
		if(getM_mediaDlg() != null && getM_mediaDlg().isShowing()){
			getM_mediaDlg().disposeVMediaDialog();
		}
		if(m_wndMode){
			OnChangeMenuState_KVMPartial(getM_wndFrame().getMenu(), false);
			getM_wndFrame().toolbar.OnChangeToolbarIconState_KVMPartial();
		}
		else{
			OnChangeMenuState_KVMPartial(getM_fsFrame().getMenu(), false);
		}
		if(isFullPermissionRequest()){
			setFullPermissionRequest(false);
		}
		else{
			getJVMenu().addFullPermissionMenuItem();
		}
	}
	public void onSendHostLock(byte state) {
		m_KVMClnt.onSendLockScreen(state);
		changeHostDisplayLockStatus(state);
	}

	public void changeHostDisplayLockStatus(short status){
		getJVMenu().notifyMenuStateEnable(JVMenu.VIDEO_HOST_DISPLAY_LOCK, false);
		getJVMenu().notifyMenuStateEnable(JVMenu.VIDEO_HOST_DISPLAY_UNLOCK, false);
		if(status == HOST_DISPLAY_UNLOCK || status == HOST_DISPLAY_UNLOCKED_AND_DISABLED){
			if(status == HOST_DISPLAY_UNLOCK)
				getJVMenu().notifyMenuStateEnable(JVMenu.VIDEO_HOST_DISPLAY_LOCK, true);
			getM_wndFrame().toolbar.turnOnHostDisplayButton(true);

			getJVMenu().getMenuItem(JVMenu.VIDEO_HOST_DISPLAY_LOCK).setMnemonic('n');
			getJVMenu().getMenuItem(JVMenu.VIDEO_HOST_DISPLAY_LOCK).setAccelerator(
					KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.ALT_MASK));
			getJVMenu().getMenuMnemonics().put(JVMenu.VIDEO_HOST_DISPLAY_LOCK, 'n');
			getJVMenu().getMenuAccelerator().put(JVMenu.VIDEO_HOST_DISPLAY_LOCK, 
					KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.ALT_MASK));
			
			getJVMenu().getMenuItem(JVMenu.VIDEO_HOST_DISPLAY_UNLOCK).setMnemonic('\0');
			getJVMenu().getMenuItem(JVMenu.VIDEO_HOST_DISPLAY_UNLOCK).setAccelerator(null);	
			getJVMenu().getMenuMnemonics().remove(JVMenu.VIDEO_HOST_DISPLAY_UNLOCK);
			getJVMenu().getMenuAccelerator().remove(JVMenu.VIDEO_HOST_DISPLAY_UNLOCK);
		}
		else if(status == HOST_DISPLAY_LOCK || status == HOST_DISPLAY_LOCKED_AND_DISABLED){
			if(status == HOST_DISPLAY_LOCK)
				getJVMenu().notifyMenuStateEnable(JVMenu.VIDEO_HOST_DISPLAY_UNLOCK, true);
			getM_wndFrame().toolbar.turnOnHostDisplayButton(false);

			getJVMenu().getMenuItem(JVMenu.VIDEO_HOST_DISPLAY_UNLOCK).setMnemonic('n');
			getJVMenu().getMenuItem(JVMenu.VIDEO_HOST_DISPLAY_UNLOCK).setAccelerator(
					KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.ALT_MASK));
			getJVMenu().getMenuMnemonics().put(JVMenu.VIDEO_HOST_DISPLAY_UNLOCK, 'n');
			getJVMenu().getMenuAccelerator().put(JVMenu.VIDEO_HOST_DISPLAY_UNLOCK, 
					KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.ALT_MASK));
			
			getJVMenu().getMenuItem(JVMenu.VIDEO_HOST_DISPLAY_LOCK).setMnemonic('\0');
			getJVMenu().getMenuItem(JVMenu.VIDEO_HOST_DISPLAY_LOCK).setAccelerator(null);
			getJVMenu().getMenuMnemonics().remove(JVMenu.VIDEO_HOST_DISPLAY_LOCK);
			getJVMenu().getMenuAccelerator().remove(JVMenu.VIDEO_HOST_DISPLAY_LOCK);
		}
		//Disable menu items in case of power OFF status, and partial access previlege.
		if(KVMSharing.KVM_REQ_GIVEN == KVMSharing.KVM_REQ_PARTIAL ||
				powerStatus == SERVER_POWER_OFF){
			getJVMenu().notifyMenuStateEnable(JVMenu.VIDEO_HOST_DISPLAY_UNLOCK, false);
			getJVMenu().notifyMenuStateEnable(JVMenu.VIDEO_HOST_DISPLAY_LOCK, false);
		}
	}

	/**
	 * Conacatinate the common menu item exception list and SOC menu item exception list.
	 * @param commonExceptionList - list of exception menus.
	 * @return
	 */
	public String[] getExceptionMenuList(String[] commonExceptionList){
		String SOCExceptionList[] = JVMenu.KVMPartialExceptionSOCMenuItems;
		String exceptionList[]= new String[commonExceptionList.length + SOCExceptionList.length];
		System.arraycopy(commonExceptionList, 0, exceptionList, 0, commonExceptionList.length);
		System.arraycopy(SOCExceptionList, 0, exceptionList, commonExceptionList.length, SOCExceptionList.length);
		return exceptionList;
	}

	public void onMediaLicenseStatus(byte state){
		Debug.out.println("ON MEDIA LICENSE STATE : "+state);
		Debug.out.println("CURRENT MEDIA LICENSE STATUS : "+JViewer.getMediaLicenseStatus());
		if(JViewer.getMediaLicenseStatus() != state)
		{
			JViewer.setMediaLicenseStatus(state);
			if(JViewerApp.getInstance().IsCDROMRedirRunning() ||
					JViewerApp.getInstance().IsFloppyRedirRunning() ||
					JViewerApp.getInstance().IsHarddiskRedirRunning()){
				getM_frame().stopVMediaRedirection(LocaleStrings.getString("D_60_JVAPP"));
			}
		}
	}

/**
	Returns the client user name with user domain if available.
*/
	public String getClientUserName(){
		String clientUserName = "";
		String userName = "";
		String userDomain = null;
		userName = System.getProperty("user.name");
		if(System.getProperty("os.name").startsWith("Windows")){
			userDomain = System.getenv("USERDOMAIN");
			userDomain = userDomain.trim();
		}
		if(userDomain != null && userDomain.length() > 0)
			clientUserName = userDomain + "\\" + userName;
		else
			clientUserName = userName;
		return clientUserName;
	}

	public void confirmationDialogResponse(int type){
		if (type == InfoDialog.HOST_KBD_LANG)
			m_KVMClnt.sendKeyBoardLang();
	}

	public int getFreeCDNum() {
		return freeCDNum;
	}

	public void setFreeCDNum(int num) {
		freeCDNum = num;
	}

	public int getFreeFDNum() {
		return freeFDNum;
	}

	public void setFreeFDNum(int num) {
		freeFDNum = num;
	}

	public int getFreeHDNum() {
		return freeHDNum;
	}

	public void setFreeHDNum(int num) {
		freeHDNum = num;
	}

	/**
	 * Upadte the free devices status in VMedia dialog.
	 */
	public void updateFreeDeviceStatus(){
		if(m_mediaDlg != null && m_mediaDlg.isShowing()){
			m_mediaDlg.updateFreeCDStatus();
			m_mediaDlg.updateFreeFDStatus();
			m_mediaDlg.updateFreeHDStatus();
		}
	}

	/**
	 * @return Gives the status if the Fit to Host zoom option can be rendered or not.
	 */
	public boolean isRenderFitToHost() {
		return renderFitToHost;
	}

	/**
	 * Set the flag to true if the Fit to Host zoom option can be rendered. Set teh flag as false otherwise.
	 * @param renderFitToHost the renderFitToHost to set
	 */
	public void setRenderFitToHost(boolean renderFitToHost) {
		this.renderFitToHost = renderFitToHost;
	}
	public int getCurrentSessionId() {
		return currentSessionId;
	}

	public void setCurrentSessionId(int currentSessionId) {
		this.currentSessionId = currentSessionId;
	}

}
