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

import java.awt.event.KeyEvent;
import java.util.HashMap;

import com.ami.iusb.FloppyRedir;
import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;
import com.ami.kvm.jviewer.hid.KeyProcessor;
import com.ami.kvm.jviewer.hid.USBKeyProcessorEnglish;
import com.ami.kvm.jviewer.hid.USBKeyProcessorEnglishHost;
import com.ami.kvm.jviewer.hid.USBKeyProcessorFrench;
import com.ami.kvm.jviewer.hid.USBKeyProcessorFrenchHost;
import com.ami.kvm.jviewer.hid.USBKeyProcessorGerman;
import com.ami.kvm.jviewer.hid.USBKeyProcessorGermanHost;
import com.ami.kvm.jviewer.hid.USBKeyProcessorJapanese;
import com.ami.kvm.jviewer.hid.USBKeyProcessorSpanish;
import com.ami.kvm.jviewer.hid.USBKeyProcessorSpanishHost;
import com.ami.kvm.jviewer.hid.USBKeyboardRep;
import com.ami.kvm.jviewer.kvmpkts.KVMClient;


public class AutoKeyboardLayout {

	public static final int  KBD_TYPE_AUTO		= 0;
	public static final int  KBD_TYPE_FRENCH 	= 1036;
	public static final int  KBD_TYPE_SPANISH 	= 1034;//0x40a
	public static final int  KBD_TYPE_GERMAN	= 1031;//0x407
	public static final int  KBD_TYPE_ENGLISH	= 1033;//409
	public static final int  KBD_TYPE_JAPANESE 	= 1041;//411
	
	private KeyProcessor m_keyprocessor = null;
	private int keyboardType;
	public static  boolean DEAD_FLAG 		= false;
	public static  boolean SHIFT_FLAG 		= false;
	public static  int SHIFT_KEY_POSITION 	= 0;
	public static  boolean ALT_GR_FLAG 		= false;
	public static  boolean PLUS_FLAG 		= false;
	public static  boolean JAPANESE_FLAG 	= false;
	public HashMap<Integer, Integer> French_linuxMap;
	public HashMap<Integer, Integer> French_WinMap;
	public HashMap<Integer, Integer> French_Alt_gr_linuxMap;
	public HashMap<Integer, Integer> Spanish_Map;
	public HashMap<Integer, Integer> Spanish_Alt_gr_linuxMap;
	public HashMap<Integer, Integer> German_Map;
	public HashMap<Integer, Integer> German_Map_Alt_gr_linuxMap;
	private byte[] bdata;
	private int hostKeyboardType = KBD_TYPE_AUTO;

	public AutoKeyboardLayout() {
		get_keybd_type();
		getHostKeyboardType();
		ongetKeyprocessor();
		JViewerApp.getInstance().getM_USBKeyRep().setM_USBKeyProcessor(ongetKeyprocessor());
		French_linuxMap = new HashMap<Integer, Integer>();
		French_Alt_gr_linuxMap = new HashMap<Integer, Integer>();
		French_WinMap = new HashMap<Integer, Integer>();
		Spanish_Map = new HashMap<Integer, Integer>();
		Spanish_Alt_gr_linuxMap = new HashMap<Integer, Integer>();
		German_Map = new HashMap<Integer, Integer>();
		German_Map_Alt_gr_linuxMap = new HashMap<Integer, Integer>();
		French_WinMap.put(178,192);
		French_WinMap.put(249,222);
		French_WinMap.put(37,222);
		French_linuxMap.put(339,192);//first key
		French_linuxMap.put(233,50);//2
		French_linuxMap.put(50,50);//2
		French_linuxMap.put(34,51);//3
		French_linuxMap.put(51,51);//3
		French_linuxMap.put(232,55);//7
		French_linuxMap.put(55,55);//7
		French_linuxMap.put(231,57);//9
		French_linuxMap.put(57,57);//9
		French_linuxMap.put(224,48);//0
		French_linuxMap.put(48,48);//0
		French_linuxMap.put(249,52);
		French_linuxMap.put(37,52);
		French_Alt_gr_linuxMap.put(38,49);
		French_Alt_gr_linuxMap.put(126,50);
		French_Alt_gr_linuxMap.put(35,51);
		French_Alt_gr_linuxMap.put(123,KeyEvent.VK_4);//for alt + 4
		French_Alt_gr_linuxMap.put(91,53);
		French_Alt_gr_linuxMap.put(124,54);
		French_Alt_gr_linuxMap.put(96,55);
		French_Alt_gr_linuxMap.put(92,56);
		French_Alt_gr_linuxMap.put(94,57);
		French_Alt_gr_linuxMap.put(64,48);
		French_Alt_gr_linuxMap.put(93,522);
		French_Alt_gr_linuxMap.put(125,61);
		French_Alt_gr_linuxMap.put(164,515);
		French_Alt_gr_linuxMap.put(8364,KeyEvent.VK_E);
		Spanish_Map.put(170,192);
		Spanish_Map.put(186,192);
		Spanish_Map.put(92,192);
	    Spanish_Map.put(231,92);
		Spanish_Map.put(199,92);
		Spanish_Map.put(125,92);
		Spanish_Map.put(241,59);
		Spanish_Map.put(209,59);
		Spanish_Alt_gr_linuxMap.put(92,192);
		Spanish_Alt_gr_linuxMap.put(124,KeyEvent.VK_1);
		Spanish_Alt_gr_linuxMap.put(64,KeyEvent.VK_2);
		Spanish_Alt_gr_linuxMap.put(35,KeyEvent.VK_3);
		Spanish_Alt_gr_linuxMap.put(126,KeyEvent.VK_4);
		Spanish_Alt_gr_linuxMap.put(189,KeyEvent.VK_5);
		Spanish_Alt_gr_linuxMap.put(172,KeyEvent.VK_6);
		Spanish_Alt_gr_linuxMap.put(KeyEvent.VK_CLOSE_BRACKET,93);
		Spanish_Alt_gr_linuxMap.put(123,KeyEvent.VK_DEAD_ACUTE);
		Spanish_Alt_gr_linuxMap.put(91,KeyEvent.VK_OPEN_BRACKET);
		Spanish_Alt_gr_linuxMap.put(8364,KeyEvent.VK_5);
		Spanish_Alt_gr_linuxMap.put(125,92);//for alt + \
		German_Map.put(223,45);
		German_Map.put(63,45);
		German_Map.put(92,47);//VK_BACK_SLASH
		German_Map.put(252,91);
		German_Map.put(220,91);
		German_Map.put(246,59);
		German_Map.put(214,59);
		German_Map.put(228,222);
		German_Map.put(196,222);
		German_Map.put(KeyEvent.VK_DEAD_CIRCUMFLEX,192);
		German_Map_Alt_gr_linuxMap.put(178,50);
		German_Map_Alt_gr_linuxMap.put(179,51);
		German_Map_Alt_gr_linuxMap.put(123,55);
		German_Map_Alt_gr_linuxMap.put(91,56);
		German_Map_Alt_gr_linuxMap.put(93,57);
		German_Map_Alt_gr_linuxMap.put(125,48);
		German_Map_Alt_gr_linuxMap.put(126,KeyEvent.VK_PLUS);
		German_Map_Alt_gr_linuxMap.put(181,KeyEvent.VK_M);
		German_Map_Alt_gr_linuxMap.put(64,KeyEvent.VK_Q);
		German_Map_Alt_gr_linuxMap.put(8364,KeyEvent.VK_E);
		German_Map_Alt_gr_linuxMap.put(92,47);
		German_Map_Alt_gr_linuxMap.put(124,153);//102 key
	}

	private void get_keybd_type()
	{
		String keybdLayout = null;
		FloppyRedir floppyredir = new FloppyRedir(true);
		keybdLayout = floppyredir.ReadKeybdType();
		Debug.out.println("Keybdtype" + keybdLayout);
		String OS_name = System.getProperty("os.name");
		Debug.out.println("Os_name" + OS_name);
		if (OS_name.equalsIgnoreCase("Linux")) {
			if (keybdLayout.equalsIgnoreCase("us"))
				keyboardType = KBD_TYPE_ENGLISH;
			else if (keybdLayout.equalsIgnoreCase("fr"))
				keyboardType = KBD_TYPE_FRENCH;
			else if (keybdLayout.equalsIgnoreCase("de"))
				keyboardType = KBD_TYPE_GERMAN;
			else if (keybdLayout.equalsIgnoreCase("es"))
				keyboardType = KBD_TYPE_SPANISH;
			else if (keybdLayout.equalsIgnoreCase("jp"))
				keyboardType = KBD_TYPE_JAPANESE;
			else
				keyboardType = KBD_TYPE_ENGLISH;
		} else {
			try {
				keybdLayout = keybdLayout.substring(keybdLayout.length() - 3,
						keybdLayout.length());
				keyboardType = Integer.parseInt(keybdLayout, 16);
	    		Debug.out.println("JViewerView.KBD_TYPE"+keyboardType);
			} catch (Exception e) {
				Debug.out.println("Unknown Language");
				Debug.out.println(e);
			}
		}

	}
	
	private void getHostKeyboardType(){
		String keybdLayout = JViewer.getKeyboardLayout();
		if (keybdLayout.equalsIgnoreCase("us"))
			hostKeyboardType = KBD_TYPE_ENGLISH;
		else if (keybdLayout.equalsIgnoreCase("fr"))
			hostKeyboardType = KBD_TYPE_FRENCH;
		else if (keybdLayout.equalsIgnoreCase("de"))
			hostKeyboardType = KBD_TYPE_GERMAN;
		else if (keybdLayout.equalsIgnoreCase("es"))
			hostKeyboardType = KBD_TYPE_SPANISH;
		else if (keybdLayout.equalsIgnoreCase("jp"))
			hostKeyboardType = KBD_TYPE_JAPANESE;
		else
			hostKeyboardType = KBD_TYPE_AUTO;
	}

	public int getKeyboardType() 
	{
		get_keybd_type();
		return keyboardType;
	}

	public void setKeyboardType(int keyboardType) 
	{
		if(this.keyboardType != keyboardType){
			if(keyboardType == KBD_TYPE_AUTO)
				get_keybd_type();
			else
				this.keyboardType = keyboardType;
			m_keyprocessor = null;
		}
	}

	public void setHostKeyboardType(int keyboardType) 
	{
		if(hostKeyboardType != keyboardType){
			hostKeyboardType = keyboardType;
			m_keyprocessor = null;
		}
	}
	public boolean OnkeyTyped(KeyEvent e) 
	{
		int keycode = 0;
		int ascii_value = e.getKeyChar();		
		
		switch(keyboardType)
		{
			case KBD_TYPE_SPANISH:
				if(e.getKeyLocation() != KeyEvent.KEY_LOCATION_NUMPAD)
					 keycode = getKeyboardKeycode(Spanish_Map, ascii_value, KBD_TYPE_SPANISH);
				
				if(e.getModifiersEx()== KeyEvent.ALT_GRAPH_DOWN_MASK) {
					keycode = getKeyboardKeycode(Spanish_Alt_gr_linuxMap, ascii_value, KBD_TYPE_SPANISH);
					OnSendALTGR_Keyevent(keycode);
				}
				else {
					OnSend_Keyevent(keycode);
					return true;
				}
				break;
			
			case KBD_TYPE_FRENCH:
				if(e.getModifiers() == KeyEvent.SHIFT_MASK)
					SHIFT_FLAG = true;

				if(System.getProperty("os.name").equals("Linux")) {
					if(e.getKeyLocation() != KeyEvent.KEY_LOCATION_NUMPAD)
						keycode = getKeyboardKeycode(French_linuxMap, ascii_value, KBD_TYPE_FRENCH);
					if(DEAD_FLAG && keycode < 0)
						DEAD_FLAG = false;
				}

				if(e.getKeyLocation() != KeyEvent.KEY_LOCATION_NUMPAD && e.getKeyLocation() != KeyEvent.KEY_LOCATION_UNKNOWN)
					keycode = getKeyboardKeycode(French_WinMap, ascii_value, KBD_TYPE_FRENCH);

				if(e.getModifiersEx()== KeyEvent.ALT_GRAPH_DOWN_MASK) {
					keycode = getKeyboardKeycode(French_Alt_gr_linuxMap, ascii_value, KBD_TYPE_FRENCH);
					OnSendALTGR_Keyevent(keycode);
				}
				else {
					if(keycode > 0){
						OnSend_Keyevent(keycode);
						return true;
					}
				}
				break;
				
			case KBD_TYPE_GERMAN:
				if(ascii_value == 223 || ascii_value == 63) {
					 OnSend_Keyevent(47);
					 return true;
				}

				if(e.getModifiers() == KeyEvent.SHIFT_MASK)
					SHIFT_FLAG = true;

				if(e.getKeyLocation() != KeyEvent.KEY_LOCATION_NUMPAD){
					keycode = getKeyboardKeycode(German_Map, ascii_value, KBD_TYPE_GERMAN);
					if(DEAD_FLAG && keycode < 0)
						DEAD_FLAG = false;
				}
				
				if(e.getModifiersEx()== KeyEvent.ALT_GRAPH_DOWN_MASK) {
					keycode = getKeyboardKeycode(German_Map_Alt_gr_linuxMap, ascii_value, KBD_TYPE_GERMAN);
					OnSendALTGR_Keyevent(keycode);
				}
				else {
					if(keycode > 0){
						OnSend_Keyevent(keycode);
						return true;
					}
				}
				break;
		}

		return false;
	}


	private void OnSendALTGR_Keyevent(int keycode) {
		KVMClient kvmClnt = JViewerApp.getInstance().getKVMClient();
		USBKeyboardRep m_USBKeyRep =  JViewerApp.getInstance().getM_USBKeyRep();
		m_USBKeyRep.set(KeyEvent.VK_CONTROL,KeyEvent.KEY_LOCATION_LEFT, false );
		kvmClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(KeyEvent.VK_ALT, KeyEvent.KEY_LOCATION_RIGHT, false);
		kvmClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(KeyEvent.VK_ALT, KeyEvent.KEY_LOCATION_RIGHT, true );
		kvmClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(keycode, KeyEvent.KEY_LOCATION_STANDARD, true );
		kvmClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(keycode, KeyEvent.KEY_LOCATION_STANDARD, false );
		kvmClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(KeyEvent.VK_ALT, KeyEvent.KEY_LOCATION_RIGHT, false);
		kvmClnt.sendKMMessage(m_USBKeyRep);
		return;
	}


	private void OnSend_Keyevent(int keycode) {
		KVMClient kvmClnt = JViewerApp.getInstance().getKVMClient();
		USBKeyboardRep m_USBKeyRep =  JViewerApp.getInstance().getM_USBKeyRep();
		m_USBKeyRep.set(keycode, KeyEvent.KEY_LOCATION_STANDARD, true);
		kvmClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(keycode, KeyEvent.KEY_LOCATION_STANDARD, false );
		kvmClnt.sendKMMessage(m_USBKeyRep);
		return;
	}

	private void OnSendShiftGrave_Keyevent(int keycode,int keylocation) {
		KVMClient kvmClnt = JViewerApp.getInstance().getKVMClient();
		USBKeyboardRep m_USBKeyRep =  JViewerApp.getInstance().getM_USBKeyRep();
		m_USBKeyRep.set(16, keylocation, true );
		kvmClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(keycode, KeyEvent.KEY_LOCATION_STANDARD, true );
		kvmClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(16, keylocation, false );
		kvmClnt.sendKMMessage(m_USBKeyRep);
		m_USBKeyRep.set(keycode, KeyEvent.KEY_LOCATION_STANDARD, false );
		kvmClnt.sendKMMessage(m_USBKeyRep);
		return;
	}

	@SuppressWarnings("unchecked")
	public boolean OnkeyPressed(KeyEvent e) {

		KVMClient kvmClnt = JViewerApp.getInstance().getKVMClient();
		USBKeyboardRep m_USBKeyRep =  JViewerApp.getInstance().getM_USBKeyRep();
		switch(keyboardType)
		{
			case KBD_TYPE_FRENCH:
			case KBD_TYPE_SPANISH:
			case KBD_TYPE_GERMAN:
				int keyModifiers = e.getModifiersEx();
				
				if( keyModifiers == KeyEvent.ALT_GRAPH_DOWN_MASK )
					return true;
				
				if( keyModifiers == 640 )
					return true;
				
				if(keyboardType == KBD_TYPE_GERMAN) {
					if(e.getKeyChar()== '~' ) {
						OnSend_Keyevent(61);
						return true;
					}
					
					if(e.getKeyCode()== 521 )
						PLUS_FLAG=true;
				}
				break;
				
			default:
				return false;
		}
		return false; // To satisfy compiler. What to do :-(
	}

	@SuppressWarnings("unchecked")
	public boolean OnkeyReleased(KeyEvent e) {
		KVMClient kvmClnt = JViewerApp.getInstance().getKVMClient();
		USBKeyboardRep m_USBKeyRep =  JViewerApp.getInstance().getM_USBKeyRep();

		if( ( e.getModifiersEx() & KeyEvent.ALT_DOWN_MASK ) == KeyEvent.ALT_DOWN_MASK ) {
			if(keyboardType == KBD_TYPE_ENGLISH )
				return true;
		}
		int keyCode = e.getKeyCode();
		
		switch(keyboardType)
		{
			case KBD_TYPE_GERMAN:
				if(System.getProperty("os.name").equals("Linux")) {
					if(keyCode == KeyEvent.VK_DEAD_CIRCUMFLEX && !SHIFT_FLAG) {
						OnSend_Keyevent(192);
						DEAD_FLAG = true;
						return true;
					}
					
					if(keyCode == 521 && !PLUS_FLAG) {
						if(e.getModifiers() == KeyEvent.ALT_GRAPH_MASK){
							OnSendALTGR_Keyevent(KeyEvent.VK_PLUS);
							PLUS_FLAG = false;
							return true;
						}
					}
					
					if(keyCode == KeyEvent.VK_DEAD_ACUTE && !SHIFT_FLAG) {
						OnSend_Keyevent(61);
						DEAD_FLAG = true;
						return true;
					}
					
					if(keyCode == KeyEvent.VK_DEAD_ACUTE && SHIFT_FLAG) {
						OnSendShiftGrave_Keyevent(61,SHIFT_KEY_POSITION);
						DEAD_FLAG = true;
						return true;
					}
					
					if(DEAD_FLAG ) {
						OnSend_Keyevent(keyCode);
						DEAD_FLAG = false;
						SHIFT_FLAG = false;
						return true;
					}
					
					if(keyCode == KeyEvent.VK_SHIFT) {
						SHIFT_FLAG=true;
						SHIFT_KEY_POSITION=e.getKeyLocation();
						DEAD_FLAG = false;
					}
					else {
						SHIFT_FLAG=false;
						DEAD_FLAG = false;
						SHIFT_KEY_POSITION=0;
					}
					
					PLUS_FLAG = false;
				}
				break;
			case KBD_TYPE_FRENCH:
				if(System.getProperty("os.name").equals("Linux")) {
					if(keyCode== KeyEvent.VK_DEAD_CIRCUMFLEX && !SHIFT_FLAG) {
						OnSend_Keyevent(91);
						DEAD_FLAG = true;
						return true;
					}
					
					if(keyCode== KeyEvent.VK_DEAD_CIRCUMFLEX && SHIFT_FLAG) {
						OnSendShiftGrave_Keyevent(91,SHIFT_KEY_POSITION);
						DEAD_FLAG = true;
						return true;
					}
					
					if(DEAD_FLAG ) {
						OnSend_Keyevent(keyCode);
						DEAD_FLAG = false;
						SHIFT_FLAG = false;
						SHIFT_KEY_POSITION=0;
						return true;
					}
					
					if(keyCode== KeyEvent.VK_SHIFT) {
						SHIFT_FLAG=true;
						SHIFT_KEY_POSITION=e.getKeyLocation();
						DEAD_FLAG = false;
					}
					else {
						SHIFT_FLAG=false;
						SHIFT_KEY_POSITION=0;
						DEAD_FLAG = false;
					}
				}
				break;
			case KBD_TYPE_SPANISH:
				if(System.getProperty("os.name").equals("Linux")) {
					if(keyCode== KeyEvent.VK_DEAD_ACUTE && !SHIFT_FLAG  && !ALT_GR_FLAG) {
						OnSend_Keyevent(KeyEvent.VK_DEAD_ACUTE);
						if(!DEAD_FLAG)
							DEAD_FLAG = true;
							else
								DEAD_FLAG = false;
						return true;
					}
					
					if(keyCode== KeyEvent.VK_DEAD_GRAVE && !SHIFT_FLAG  && !ALT_GR_FLAG) {
						OnSend_Keyevent(128);
						if(!DEAD_FLAG)
						DEAD_FLAG = true;
						else
							DEAD_FLAG = false;
						return true;
					}
					
					if(keyCode== KeyEvent.VK_DEAD_ACUTE && SHIFT_FLAG  && !ALT_GR_FLAG) {
						OnSendShiftGrave_Keyevent(KeyEvent.VK_DEAD_ACUTE,SHIFT_KEY_POSITION);
						if(!DEAD_FLAG)
						DEAD_FLAG = true;
						else
							DEAD_FLAG = false;
						return true;
					}
					
					if(keyCode== KeyEvent.VK_DEAD_GRAVE && SHIFT_FLAG && !ALT_GR_FLAG) {
						OnSendShiftGrave_Keyevent(128,SHIFT_KEY_POSITION);
						if(!DEAD_FLAG)
							DEAD_FLAG = true;
							else
								DEAD_FLAG = false;
						return true;
					}
					
					if(DEAD_FLAG && !ALT_GR_FLAG) {
						OnSend_Keyevent(keyCode);
						DEAD_FLAG = false;
						SHIFT_FLAG = false;
						SHIFT_KEY_POSITION=0;
						return true;
					}
					
					if(keyCode== KeyEvent.VK_SHIFT) {
						SHIFT_FLAG=true;
						SHIFT_KEY_POSITION=e.getKeyLocation();
						DEAD_FLAG = false;
						ALT_GR_FLAG = false;
					}
					else {
						SHIFT_FLAG=false;
						SHIFT_KEY_POSITION=0;
						DEAD_FLAG = false;
						ALT_GR_FLAG = false;
					}
				}
				break;
		}
		return false;
	}

	public KeyProcessor ongetKeyprocessor() {
		if(m_keyprocessor == null){
			if(JViewerApp.getInstance().getJVMenu().getMenuSelected(JVMenu.AUTOMAIC_LANGUAGE)){
				switch(keyboardType)
				{
				case KBD_TYPE_JAPANESE:
					m_keyprocessor = new USBKeyProcessorJapanese();
					JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.PKBRD_LANGUAGE_JAPANESE, true);
					break;
				case  KBD_TYPE_GERMAN:
					m_keyprocessor = new USBKeyProcessorGerman();
					JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.PKBRD_LANGUAGE_GERMAN_GER, true);
					break;
				case KBD_TYPE_FRENCH:
					m_keyprocessor = new USBKeyProcessorFrench();
					JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.PKBRD_LANGUAGE_FRENCH, true);
					break;
				case KBD_TYPE_SPANISH:
					m_keyprocessor = new USBKeyProcessorSpanish();
					JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.PKBRD_LANGUAGE_SPANISH, true);
					break;
				default:
					m_keyprocessor = new USBKeyProcessorEnglish();
					JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.PKBRD_LANGUAGE_ENGLISH_US, true);
				}
			}
			else{
				switch(hostKeyboardType)
				{
				case KBD_TYPE_JAPANESE:
					m_keyprocessor = new USBKeyProcessorJapanese();
					JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.PKBRD_LANGUAGE_JAPANESE, true);
					break;
				case  KBD_TYPE_GERMAN:
					m_keyprocessor = new USBKeyProcessorGermanHost();
					JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.PKBRD_LANGUAGE_GERMAN_GER, true);
					break;
				case KBD_TYPE_FRENCH:
					m_keyprocessor = new USBKeyProcessorFrenchHost();
					JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.PKBRD_LANGUAGE_FRENCH, true);
					break;
				case KBD_TYPE_SPANISH:
					m_keyprocessor = new USBKeyProcessorSpanishHost();
					JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.PKBRD_LANGUAGE_SPANISH, true);
					break;
				default:
					m_keyprocessor = new USBKeyProcessorEnglishHost();
					JViewerApp.getInstance().getJVMenu().notifyMenuStateSelected(JVMenu.PKBRD_LANGUAGE_ENGLISH_US, true);

				}
			}
		}
		return m_keyprocessor;
		// TODO Auto-generated method stub

	}
	
	/**
	 * Initialize the key processor object to null
	 */
	public void initKeyProcessor(){
		m_keyprocessor = null;
	}
	
	private int getKeyboardKeycode(HashMap<Integer, Integer> Key_Map, int ascii_value, int KeyBoardType)
	{
		try
		{
			return Key_Map.get(ascii_value);
		}catch(Exception e)
		{
			switch(KeyBoardType)
			{
				case KBD_TYPE_FRENCH:
					Debug.out.println("Exception in KBD_TYPE_FRENCH"+e);
					break;
				case KBD_TYPE_SPANISH:
					Debug.out.println("Exception in KBD_TYPE_SPANISH"+e);
					break;
				case KBD_TYPE_GERMAN:
					Debug.out.println("Exception in KBD_TYPE_GERMAN"+e);
					break;
				case KBD_TYPE_ENGLISH:
					Debug.out.println("Exception in KBD_TYPE_ENGLISH"+e);
					break;
				case KBD_TYPE_JAPANESE:
					Debug.out.println("Exception in KBD_TYPE_JAPANESE"+e);
					break;
			}
			return -1;
		}		
	}

}
