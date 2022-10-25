package com.ami.kvm.jviewer.hid;

import java.awt.event.KeyEvent;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.gui.AutoKeyboardLayout;
import com.ami.kvm.jviewer.gui.JViewerApp;
import com.ami.kvm.jviewer.gui.LocaleStrings;

public class USBKeyProcessorEnglishHost extends USBKeyProcessorEnglish{
	private AutoKeyboardLayout autoKeyboradLayout;
	private boolean shiftModified = false;
	private boolean shiftHeldDown = false;
	private boolean altGrModified = false;
	private boolean autoKeyBreakMode = false;
	private byte[] usbKeyRepPkt = null;
	
	public USBKeyProcessorEnglishHost(){
		super();
	}
	public byte[] convertKeyCode( int keyCode, int keyLocation, boolean keyPress )
	{
		Integer usbKeyCode = new Integer(0);
		byte[] outputKeycodes = new byte[ 6 ];
		boolean sendAutoKeybreak = false;
		autoKeyBreakMode = autoKeybreakModeOn;
		autoKeyboradLayout = JViewerApp.getInstance().getAutokeylayout();
		if(autoKeyboradLayout != null) {
			switch(autoKeyboradLayout.getKeyboardType()) {
			case AutoKeyboardLayout.KBD_TYPE_FRENCH:
				keyCode = convertFrenchToEnglish(keyCode, keyPress);
				break;
			case AutoKeyboardLayout.KBD_TYPE_GERMAN:
				keyCode = convertGermanToEnglish(keyCode, keyPress);
				break;
			case AutoKeyboardLayout.KBD_TYPE_SPANISH:
				keyCode = convertSpanishToEnglish(keyCode, keyPress);
				break;
			}
		}
		switch( keyLocation )
		{
		case KeyEvent.KEY_LOCATION_LEFT:
			usbKeyCode = leftMap.get( keyCode );
			break;

		case KeyEvent.KEY_LOCATION_NUMPAD:
			if(keyCode == KeyEvent.VK_COMMA)
				usbKeyCode = standardMap.get(keyCode);
			else
				usbKeyCode = keypadMap.get( keyCode );
			break;

		case KeyEvent.KEY_LOCATION_RIGHT:
			usbKeyCode = rightMap.get( keyCode );
			break;

		case KeyEvent.KEY_LOCATION_STANDARD:
			usbKeyCode = standardMap.get( keyCode );
			break;

		case KeyEvent.KEY_LOCATION_UNKNOWN:
			System.err.println(LocaleStrings.getString("AD_1_USBKP"));
			break;

		default:
			System.err.println(LocaleStrings.getString("AD_2_USBKP"));
			break;
		}

		if( usbKeyCode != null ) {

			switch( keyCode )
			{
			case KeyEvent.VK_CONTROL:
				if( keyLocation == KeyEvent.KEY_LOCATION_LEFT ) {
					if( keyPress )
						modifiers |= MOD_LEFT_CTRL;
					else
						modifiers &= ~MOD_LEFT_CTRL;
				} else {
					if( keyPress )
						modifiers |= MOD_RIGHT_CTRL;
					else
						modifiers &= ~MOD_RIGHT_CTRL;
				}
				break;

			case KeyEvent.VK_SHIFT:
				if( keyLocation == KeyEvent.KEY_LOCATION_LEFT ) {
					if( keyPress ){
						modifiers |= MOD_LEFT_SHIFT;
						shiftHeldDown = true;
					}
					else {
						modifiers &= ~MOD_LEFT_SHIFT;
						modifiers &= ~MOD_RIGHT_SHIFT;
						shiftHeldDown = false;
					}
				} else {
					if( keyPress ){
						modifiers |= MOD_RIGHT_SHIFT;
						shiftHeldDown = true;
					}
					else {
						modifiers &= ~MOD_RIGHT_SHIFT;
						modifiers &= ~MOD_LEFT_SHIFT;
						shiftHeldDown = false;
					}
				}
				break;

			case KeyEvent.VK_ALT:
				if( keyLocation == KeyEvent.KEY_LOCATION_LEFT ) {
					if( keyPress )
						modifiers |= MOD_LEFT_ALT;
					else
						modifiers &= ~MOD_LEFT_ALT;
				} else {
					if( keyPress )
						modifiers |= MOD_RIGHT_ALT;
					else
						modifiers &= ~MOD_RIGHT_ALT;
				}
				break;

			case KeyEvent.VK_WINDOWS:
				if( keyLocation == KeyEvent.KEY_LOCATION_LEFT ) {
					if( keyPress )
						modifiers |= MOD_LEFT_WIN;
					else
						modifiers &= ~MOD_LEFT_WIN;
				} else {
					if( keyPress )
						modifiers |= MOD_RIGHT_WIN;
					else
						modifiers &= ~MOD_RIGHT_WIN;
				}
				break;

			default:
				if( keyPress || (keyCode == KeyEvent.VK_PRINTSCREEN)) {
					outputKeycodes[ 0 ] = usbKeyCode.byteValue();
					if( autoKeybreakModeOn )
						sendAutoKeybreak = true;
				} else {
					if( !autoKeybreakModeOn ||
							( keyCode == KeyEvent.VK_NUM_LOCK ) ||
							( keyCode == KeyEvent.VK_CAPS_LOCK ) ||
							( keyCode == KeyEvent.VK_SCROLL_LOCK ) )
						outputKeycodes[ 0 ] = 0;
					else
						return( null );
				}
				break;
			}
			USBKeyProcessorEnglish.setModifiers(modifiers);
			usbKeyRepPkt = USBKeyboardRepPkt( outputKeycodes, modifiers, sendAutoKeybreak );
			autoKeybreakModeOn = autoKeyBreakMode;
			return( usbKeyRepPkt);
		} else {
			Debug.out.println(LocaleStrings.getString("AD_3_USBKP") + KeyEvent.getKeyText( keyCode ) );
			return( null );
		}
	}

	/**
	 * Converts the key codes send from a client with German keyboard layout to the corresponding key code in 
	 * English keyboard layout. 
	 * @param keyCode - the key code to be converted
	 * @param keyPress - true if key is pressed; false otherwise. 
	 * @return
	 */
	private int convertGermanToEnglish(int keyCode, boolean keyPress){
		int convertedKeyCode = keyCode;

		switch(keyCode){
		case KeyEvent.VK_DEAD_CIRCUMFLEX:
			convertedKeyCode = KeyEvent.VK_UNDEFINED;
			break;
		case KeyEvent.VK_1:
		case KeyEvent.VK_4:
		case KeyEvent.VK_5:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if (keyPress) {
					if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK) {
						enableShift(false);
						shiftModified = true;
					}
				}
				else{
					if(shiftModified)
						shiftModified = false;
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}

			}
			else{
				if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK)
					enableShift(true);
			}
			break;
		case KeyEvent.VK_2:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if (keyPress) {
					if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK) {
						convertedKeyCode = KeyEvent.VK_2;
						enableShift(false);
						shiftModified = true;
					}
					else {
						convertedKeyCode = KeyEvent.VK_QUOTE;
					}
				}
				else{
					if(shiftModified){
						convertedKeyCode = KeyEvent.VK_2;
						shiftModified = false;
					}
					else
						convertedKeyCode = KeyEvent.VK_QUOTE;
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}

			}
			else{
				if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
					convertedKeyCode = KeyEvent.VK_QUOTE;
					enableShift(true);
				}
			}
			break;
		case KeyEvent.VK_3:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if (keyPress) {
					if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK) {
						convertedKeyCode = keyCode;
						enableShift(false);
						shiftModified = true;
					}
					else {
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
					}
				}
				else{
					if(shiftModified){
						convertedKeyCode = keyCode;
						shiftModified = false;
					}
					else
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}

			}
			else{
				if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
					convertedKeyCode = KeyEvent.VK_UNDEFINED;
					enableShift(true);
				}
			}
			break;
		case KeyEvent.VK_6:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if (keyPress) {
					if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK) {
						convertedKeyCode = keyCode;
						enableShift(false);
						shiftModified = true;
					}
					else {
						convertedKeyCode = KeyEvent.VK_7;
					}
				}
				else{
					if(shiftModified){
						convertedKeyCode = keyCode;
						shiftModified = false;
					}
					else
						convertedKeyCode = KeyEvent.VK_7;
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}

			}
			else{
				if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
					convertedKeyCode = KeyEvent.VK_7;
					enableShift(true);
				}
			}
			break;
		case KeyEvent.VK_7:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if (keyPress) {
					if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK)
						convertedKeyCode = keyCode;
					else 
						convertedKeyCode = KeyEvent.VK_SLASH;
					enableShift(false);
					shiftModified = true;
				}
				else{
					convertedKeyCode = keyCode;
					if(shiftModified){
						shiftModified = false;
					}
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}

			}
			else{
				if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK)
					convertedKeyCode = KeyEvent.VK_SLASH;
			}
			break;
		case KeyEvent.VK_8:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if (keyPress) {
					if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
						convertedKeyCode = keyCode;
						enableShift(false);
						shiftModified = true;
					}
					else 
						convertedKeyCode = KeyEvent.VK_9;

				}
				else{
					if(shiftModified){
						convertedKeyCode = keyCode;
						shiftModified = false;
					}
					else
						convertedKeyCode = KeyEvent.VK_9;
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}

			}
			else{
				if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
					convertedKeyCode = KeyEvent.VK_9;
					enableShift(true);
				}
			}
			break;
		case KeyEvent.VK_9:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if (keyPress) {
					if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
						convertedKeyCode = keyCode;
						enableShift(false);
						shiftModified = true;
					}
					else 
						convertedKeyCode = KeyEvent.VK_0;

				}
				else{
					if(shiftModified){
						convertedKeyCode = keyCode;
						shiftModified = false;
					}
					else
						convertedKeyCode = KeyEvent.VK_0;
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}

			}
			else{
				if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
					convertedKeyCode = KeyEvent.VK_0;
					enableShift(true);
				}
			}
			break;
		case KeyEvent.VK_0:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if (keyPress) {
					if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_0;
					}
					else 
						convertedKeyCode = KeyEvent.VK_EQUALS;
					enableShift(false);
					shiftModified = true;

				}
				else{
					convertedKeyCode = KeyEvent.VK_0;
					if(shiftModified){
						convertedKeyCode = keyCode;
						shiftModified = false;
					}
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}

			}
			else{
				if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK)
					convertedKeyCode = KeyEvent.VK_EQUALS;
			}
			break;
		case KeyEvent.VK_SLASH:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK )
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
					else
						convertedKeyCode = KeyEvent.VK_SLASH;
				}
				else{
					convertedKeyCode = KeyEvent.VK_SLASH;
					if(shiftHeldDown){
						enableShift(true);
					}
					else{
						enableShift(false);
					}
					autoKeybreakModeOn = false;
				}
			}
			else{
				if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK ){
					convertedKeyCode = KeyEvent.VK_SLASH;
					enableShift(true);
				}
				else{
					convertedKeyCode = KeyEvent.VK_UNDEFINED;
				}
			}
			break;
		case KeyEvent.VK_DEAD_ACUTE:
			convertedKeyCode = KeyEvent.VK_UNDEFINED;
			break;				
		case KeyEvent.VK_OPEN_BRACKET:
			convertedKeyCode = KeyEvent.VK_UNDEFINED;
			break;
		case KeyEvent.VK_PLUS:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK)
						convertedKeyCode = KeyEvent.VK_EQUALS;
					else
						convertedKeyCode = KeyEvent.VK_8;
				}
				else{
					convertedKeyCode = KeyEvent.VK_EQUALS;
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}
			}
			else{
				if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK)
					convertedKeyCode = KeyEvent.VK_8;
				else
					convertedKeyCode = KeyEvent.VK_EQUALS;
				enableShift(true);
			}
			break;
		case KeyEvent.VK_NUMBER_SIGN:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK)
						convertedKeyCode = KeyEvent.VK_3;
					else{
						convertedKeyCode = KeyEvent.VK_QUOTE;
						enableShift(false);
						shiftModified = true;
					}
				}
				else{
					convertedKeyCode = KeyEvent.VK_3;
					if(shiftModified){
						convertedKeyCode = KeyEvent.VK_QUOTE;
						shiftModified = false;
					}
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}
			}
			else{
				if (keyPress) {
					if ((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK)
						convertedKeyCode = KeyEvent.VK_QUOTE;
					else {
						convertedKeyCode = KeyEvent.VK_3;
						enableShift(true);
					}
				}
				else
					convertedKeyCode = KeyEvent.VK_QUOTE;
			}
			break;
		case KeyEvent.VK_SEMICOLON:
		case KeyEvent.VK_QUOTE:
			convertedKeyCode = KeyEvent.VK_UNDEFINED;
			break;
		case KeyEvent.VK_COMMA:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK)
						convertedKeyCode = keyCode;
					else
						convertedKeyCode = KeyEvent.VK_SEMICOLON;
					enableShift(false);
					shiftModified = true;
				}
				else{
					if(shiftModified)
						shiftModified = false;
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = true;
				}
			}
			else{
				if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
					convertedKeyCode = KeyEvent.VK_SEMICOLON;
				}
			}
			break;
		case KeyEvent.VK_PERIOD:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
						enableShift(false);
						shiftModified = true;
					}
					else{
						convertedKeyCode = KeyEvent.VK_SEMICOLON;
					}
				}
				else{
					if(shiftModified)
						shiftModified = false;
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = true;
				}
			}
			else{
				if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
					convertedKeyCode = KeyEvent.VK_SEMICOLON;
					enableShift(true);
				}
			}
			break;
		case KeyEvent.VK_LESS:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					convertedKeyCode = KeyEvent.VK_PERIOD;
				}
				else{
					convertedKeyCode = KeyEvent.VK_COMMA;
					if (altGrModified) {
						convertedKeyCode = KeyEvent.VK_BACK_SLASH;
						modifiers |= MOD_RIGHT_ALT;
						altGrModified = false;
					}
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}
			}
			/*else if(((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT) && !altGrModified) {
					if(keyPress){
						convertedKeyCode = KeyEvent.VK_BACK_SLASH;
						enableShift(true);
						modifiers &= ~MOD_RIGHT_ALT;
						altGrModified = true;
					}
				}*/
			else{
				convertedKeyCode = KeyEvent.VK_COMMA;
				enableShift(true);
			}
			break;
		case KeyEvent.VK_DECIMAL:
			convertedKeyCode = KeyEvent.VK_COMMA;
		}

		return convertedKeyCode;
	}

	/**
	 * Converts the key codes send from a client with Spanish keyboard layout to the corresponding key code in 
	 * English keyboard layout. 
	 * @param keyCode - the key code to be converted
	 * @param keyPress - true if key is pressed; false otherwise. 
	 * @return
	 */
	private int convertSpanishToEnglish(int keyCode, boolean keyPress){
		int convertedKeyCode = keyCode;

		switch(keyCode){
		case KeyEvent.VK_BACK_QUOTE:
			convertedKeyCode = KeyEvent.VK_UNDEFINED;
			break;
		case KeyEvent.VK_2:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				convertedKeyCode = KeyEvent.VK_QUOTE;
			}
			break;
		case KeyEvent.VK_3:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				convertedKeyCode = KeyEvent.VK_UNDEFINED;
			}
			break;
		case KeyEvent.VK_6:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				convertedKeyCode = KeyEvent.VK_7;
			}
			break;
		case KeyEvent.VK_7:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if (keyPress) {
					convertedKeyCode = KeyEvent.VK_SLASH;
					enableShift(false);
					shiftModified = true;
				}
				else{
					convertedKeyCode = KeyEvent.VK_SLASH;
					if(shiftModified)
						shiftModified = false;
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}
			}
			break;
		case KeyEvent.VK_8:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				convertedKeyCode = KeyEvent.VK_9;
			}
			break;
		case KeyEvent.VK_9:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				convertedKeyCode = KeyEvent.VK_0;

			}
			break;
		case KeyEvent.VK_0:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if (keyPress) {
					convertedKeyCode = KeyEvent.VK_EQUALS;
					enableShift(false);
					shiftModified = true;

				}
				else{
					convertedKeyCode = KeyEvent.VK_EQUALS;
					if(shiftModified)
						shiftModified = false;
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}

			}
			break;
		case KeyEvent.VK_QUOTE:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				convertedKeyCode = KeyEvent.VK_SLASH;
			}
			break;
			
		case KeyEvent.VK_INVERTED_EXCLAMATION_MARK:
			convertedKeyCode = KeyEvent.VK_UNDEFINED;
			break;				
		case KeyEvent.VK_OPEN_BRACKET:
			convertedKeyCode = KeyEvent.VK_UNDEFINED;
			break;
		case KeyEvent.VK_PLUS:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					convertedKeyCode = KeyEvent.VK_8;
				}
				else{
					convertedKeyCode = KeyEvent.VK_EQUALS;
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}
			}
			else{
				convertedKeyCode = KeyEvent.VK_EQUALS;
				enableShift(true);
			}
			break;
		case KeyEvent.VK_BACK_SLASH:
		case KeyEvent.VK_SEMICOLON:
		case KeyEvent.VK_DEAD_ACUTE:
			convertedKeyCode = KeyEvent.VK_UNDEFINED;
			break;
		case KeyEvent.VK_COMMA:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					convertedKeyCode = KeyEvent.VK_SEMICOLON;
					enableShift(false);
					shiftModified = true;
				}
				else{
					if(shiftModified)
						shiftModified = false;
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = true;
				}
			}
			break;
		case KeyEvent.VK_PERIOD:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				convertedKeyCode = KeyEvent.VK_SEMICOLON;
			}
			break;
		case KeyEvent.VK_LESS:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					convertedKeyCode = KeyEvent.VK_PERIOD;
				}
				else{
					convertedKeyCode = KeyEvent.VK_COMMA;
					if (altGrModified) {
						convertedKeyCode = KeyEvent.VK_BACK_SLASH;
						modifiers |= MOD_RIGHT_ALT;
						altGrModified = false;
					}
					if(shiftHeldDown)
						enableShift(true);
					else
						enableShift(false);
					autoKeybreakModeOn = false;
				}
			}
			else{
				convertedKeyCode = KeyEvent.VK_COMMA;
				enableShift(true);
			}
			break;
		}

		return convertedKeyCode;
	}

	/**
	 * Converts the key codes send from a client with French keyboard layout to the corresponding key code in 
	 * English keyboard layout. 
	 * @param keyCode - the key code to be converted
	 * @param keyPress - true if key is pressed; false otherwise. 
	 * @return
	 */
	private int convertFrenchToEnglish(int keyCode, boolean keyPress){

		int convertedKeyCode =keyCode;
		switch(keyCode){
		//first line
		case KeyEvent.VK_BACK_QUOTE:
			convertedKeyCode =KeyEvent.VK_UNDEFINED;
			break;
		case KeyEvent.VK_1:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT ||
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_1;
						enableShift(false);
					}
					else
						convertedKeyCode = KeyEvent.VK_7;
						
					shiftModified = true;
				}
				else {
					if(shiftModified){
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_1;
						else
							convertedKeyCode = KeyEvent.VK_7;
						
						if(shiftHeldDown){
							enableShift(true);
						}
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						convertedKeyCode = KeyEvent.VK_7;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				if( keyPress ){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_7;
						enableShift(true);
					}
				}
			}
			break;
			
		case KeyEvent.VK_2:

			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_2;
						enableShift(false);
					}
					else
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						
					shiftModified = true;
				}
				else {
					if(shiftModified){
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_2;
						else
							convertedKeyCode = KeyEvent.VK_UNDEFINED;
						
						if(shiftHeldDown){
							enableShift(true);
						}
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				if( keyPress ){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
					}
				}
			}
			break;
			
		case KeyEvent.VK_3:	 // need to work on AltGr combination

			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_3;
						enableShift(false);
					}
					else
						convertedKeyCode = KeyEvent.VK_QUOTE;
						
					shiftModified = true;
				}
				else {
					if(shiftModified){
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_3;
						else
							convertedKeyCode = KeyEvent.VK_QUOTE;
						
						if(shiftHeldDown){
							enableShift(true);
						}
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						convertedKeyCode = KeyEvent.VK_QUOTE;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				if( keyPress ){
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						// modifiers &= ~MOD_RIGHT_ALT;
						convertedKeyCode = KeyEvent.VK_3;
						enableShift(true);
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						enableShift(true);
						convertedKeyCode = KeyEvent.VK_QUOTE;
					}
				}
			}
			break;
		case KeyEvent.VK_4: // need to work on AltGr combination

			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_4;
						enableShift(false);
					}
					else
					{
						convertedKeyCode = KeyEvent.VK_QUOTE;
						enableShift(false);
					}
					shiftModified = true;
				}
				else {
					if(shiftModified){
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_4;
						else
							convertedKeyCode = KeyEvent.VK_QUOTE;
						
						if(shiftHeldDown){
							enableShift(true);
						}
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						convertedKeyCode = KeyEvent.VK_QUOTE;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				if( keyPress ){
					
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						// modifiers &= ~MOD_RIGHT_ALT;
						convertedKeyCode = KeyEvent.VK_4;
						enableShift(true);
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_QUOTE;
					}
				}
			}
		
			break;
			
		case KeyEvent.VK_5:	  // need to work on AltGr combination

			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT ||
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_5;
						enableShift(false);
					}
					else
						convertedKeyCode = KeyEvent.VK_9;
					shiftModified = true;
				}
				else {
					if(shiftModified){
						
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_5;
						else
							convertedKeyCode = KeyEvent.VK_9;
						
						if(shiftHeldDown){
							enableShift(true);
						}
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						convertedKeyCode = KeyEvent.VK_9;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				if( keyPress ){
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						// modifiers &= ~MOD_RIGHT_ALT;
						convertedKeyCode = KeyEvent.VK_5;
						enableShift(true);
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						enableShift(true);
						convertedKeyCode = KeyEvent.VK_9;
					}
				}
			}
		
			break;
			
		case KeyEvent.VK_6:	  // need to work on AltGr combination

			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				
				if(keyPress){
					enableShift(false);
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_6;
					}
					else
					{
						convertedKeyCode = KeyEvent.VK_MINUS;
					}
						
					shiftModified = true;
				}
				else {
					if(shiftModified){
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_6;
						else
							convertedKeyCode = KeyEvent.VK_MINUS;

						if(shiftHeldDown){
							enableShift(true);
						}
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						convertedKeyCode = KeyEvent.VK_MINUS;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				if( keyPress ){
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						convertedKeyCode = KeyEvent.VK_6;
						enableShift(true);
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_MINUS;
					}
				}
			}
			break;
		case KeyEvent.VK_7:	 // need to work on AltGr combination

			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_7;
						enableShift(false);
					}
					else
					{
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
					}
						
					shiftModified = true;
				}
				else {
					if(shiftModified){
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_7;
						else
							convertedKeyCode = KeyEvent.VK_UNDEFINED;
						if(shiftHeldDown){
							enableShift(true);
						}
						
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				if( keyPress ){
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						convertedKeyCode = KeyEvent.VK_6;
						enableShift(true);
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK)
					{
						convertedKeyCode = KeyEvent.VK_7;
					}
					else 
					{
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
					}
				}
			}
		
			break;

		case KeyEvent.VK_8: // need to work on AltGr combination

			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_8;
						enableShift(false);
					}
					else
					{
						convertedKeyCode = KeyEvent.VK_MINUS;
						enableShift(true);
					}
					shiftModified = true;
				}
				else {
					if(shiftModified){
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_8;
						else
							convertedKeyCode = KeyEvent.VK_MINUS;
						if(shiftHeldDown){
							enableShift(true);
						}
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						convertedKeyCode = KeyEvent.VK_MINUS;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				if( keyPress ){
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						convertedKeyCode = KeyEvent.VK_8;
						enableShift(true);
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						enableShift(true);
						convertedKeyCode = KeyEvent.VK_MINUS;
					}
				}
			}
			break;
		case KeyEvent.VK_9:	 // need to work on AltGr combination

			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				
				if(keyPress){
					
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_9;
						enableShift(false);
					}
					else
					{
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
					}
					shiftModified = true;
				}
				else {
					if(shiftModified){
						
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_9;
						else
							convertedKeyCode = KeyEvent.VK_UNDEFINED;
						if(shiftHeldDown){
							enableShift(true);
						}
					
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				
				if( keyPress ){
					
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						convertedKeyCode = KeyEvent.VK_9;
						enableShift(true);
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK)
					{
						convertedKeyCode = KeyEvent.VK_9;
					}
					else 
					{
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
					}
				}
			}
		
			break;
			
		case KeyEvent.VK_0:	 // need to work on AltGr combination

			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_0;
						enableShift(false);
					}
					else
					{
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
					}
					shiftModified = true;
				}
				else {
					if(shiftModified){
						
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_0;
						else
							convertedKeyCode = KeyEvent.VK_UNDEFINED;
						if(shiftHeldDown){
							enableShift(true);
						}
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				
				if( keyPress ){
					
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						// modifiers &= ~MOD_RIGHT_ALT;
						convertedKeyCode = KeyEvent.VK_0;
						enableShift(true);
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK)
					{
						convertedKeyCode = KeyEvent.VK_0;
					}
					else 
					{
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
					}
				}
			}
			break;

		case KeyEvent.VK_RIGHT_PARENTHESIS:	 // need to work on AltGr combination

			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				
				if(keyPress){
					
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
					}
					else
					{
						convertedKeyCode = KeyEvent.VK_0;
						enableShift(true);
					}
						
					shiftModified = true;
				}
				else {
					if(shiftModified){
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_UNDEFINED;
						else
							convertedKeyCode = KeyEvent.VK_0;
						if(shiftHeldDown){
							enableShift(true);
						}
					
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						
						convertedKeyCode = KeyEvent.VK_0;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				
				if( keyPress ){
					
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						// modifiers &= ~MOD_RIGHT_ALT;
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(true);
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						enableShift(true);
						convertedKeyCode = KeyEvent.VK_0;
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
						enableShift(true);
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
					}
					
				}
			}
			break;
		case KeyEvent.VK_EQUALS:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK )
						enableShift(true);
					else
						enableShift(false);
					shiftModified = true;
				}
				else{

					if(shiftHeldDown){
						enableShift(true);
					}
					else{
						enableShift(false);
					}
					shiftModified = false;
					autoKeybreakModeOn = false;
				}
			}
			else{

				if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK ){
					if(keyPress)
						enableShift(true);
				}
			}
			break;
		case KeyEvent.VK_Q:
		
			break;
		case KeyEvent.VK_W:
		
			break;
			
		case KeyEvent.VK_A:
		
			break;
		case KeyEvent.VK_Z:
		
			break;
		case KeyEvent.VK_DEAD_CIRCUMFLEX :
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
					}
					else
					{
						convertedKeyCode = KeyEvent.VK_6;
						enableShift(true);
					}
					shiftModified = true;
				}
				else {
					if(shiftModified){
						
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_UNDEFINED;
						else
							convertedKeyCode = KeyEvent.VK_6;
						if(shiftHeldDown){
							
							enableShift(true);
						}
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						
						convertedKeyCode = KeyEvent.VK_6;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				if( keyPress ){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						
						convertedKeyCode = KeyEvent.VK_6;
						enableShift(true);
					}
					else
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
				}
			}
			break;
		case KeyEvent.VK_DOLLAR :
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
					}
					else
					{
						convertedKeyCode = KeyEvent.VK_4;
						enableShift(true);
					}
					shiftModified = true;
				}
				else {
					if(shiftModified){
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_UNDEFINED;
						else
							convertedKeyCode = KeyEvent.VK_4;
						if(shiftHeldDown){
							enableShift(true);
						}
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						convertedKeyCode = KeyEvent.VK_4;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				if( keyPress ){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						
						convertedKeyCode = KeyEvent.VK_4;
						enableShift(true);
					}
					else
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
				}
			}
			break;
			
		case KeyEvent.VK_ASTERISK :
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
					}
					else
					{
						convertedKeyCode = KeyEvent.VK_8;
						enableShift(true);
					}
					shiftModified = true;
				}
				else {
					if(shiftModified){
						
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_UNDEFINED;
						else
							convertedKeyCode = KeyEvent.VK_8;
						if(shiftHeldDown){
							enableShift(true);
						}
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						convertedKeyCode = KeyEvent.VK_8;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				
				if( keyPress ){
					
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						
						convertedKeyCode = KeyEvent.VK_8;
						enableShift(true);
					}
					else
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
				}
			}
			
			break;
		case KeyEvent.VK_M:
			break;
		case KeyEvent.VK_QUOTE:
			
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_5;
						enableShift(true);
					}
					else
					{
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
					}
					shiftModified = true;
				}
				else {
					if(shiftModified){
						
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_5;
						else
							convertedKeyCode = KeyEvent.VK_UNDEFINED;
						if(shiftHeldDown){
							
							//enableShift(true);
						}
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				
				if( keyPress ){
					
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						// modifiers &= ~MOD_RIGHT_ALT;
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(true);
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
						enableShift(true);
						convertedKeyCode = KeyEvent.VK_5;
					}
					else 
					{
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
					}
				}
			}
			break;
		case KeyEvent.VK_COMMA:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_SLASH;
						enableShift(true);
					}
					else
					{
						enableShift(false);
					}
					shiftModified = true;
				}
				else {
					if(shiftModified){
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_SLASH;
						if(shiftHeldDown)
							enableShift(true);
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				
				if( keyPress ){
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
						enableShift(true);
						convertedKeyCode = KeyEvent.VK_SLASH;
					}
				}
			}
			break;
		case KeyEvent.VK_SEMICOLON:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){

				if(keyPress){

					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_PERIOD;

						enableShift(false);
					}
					else
					{
						enableShift(false);
					}
					shiftModified = true;
				}
				else {
					if(shiftModified){
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_PERIOD;
						if(shiftHeldDown)
							enableShift(true);
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				if( keyPress ){

					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
						enableShift(false);
						convertedKeyCode = KeyEvent.VK_PERIOD;
					}
				}
			}
			break;
		case KeyEvent.VK_COLON:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				
				if(keyPress){
					
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_SLASH;
						enableShift(false);
					}
					else
					{
						convertedKeyCode = KeyEvent.VK_SEMICOLON;
						enableShift(true);
					}
						
					shiftModified = true;
				}
				else {
					if(shiftModified){
						
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_SLASH;
						else
							convertedKeyCode = KeyEvent.VK_SEMICOLON;
						
						if(shiftHeldDown)
							enableShift(true);						
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						
						convertedKeyCode = KeyEvent.VK_SEMICOLON;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				
				if( keyPress ){
					
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						// modifiers &= ~MOD_RIGHT_ALT;
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(true);
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
						enableShift(false);
						convertedKeyCode = KeyEvent.VK_SLASH;
					}
					else 
					{
						enableShift(true);
						convertedKeyCode = KeyEvent.VK_SEMICOLON;
					}
				}
			}
			break;
		case KeyEvent.VK_EXCLAMATION_MARK:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				if(keyPress){
					
					if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK){
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(false);
					}
					else
					{
						convertedKeyCode = KeyEvent.VK_1;
						enableShift(true);
					}

					shiftModified = true;
				}
				else {
					if(shiftModified){
						
						if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) != JViewerApp.CAPSLOCK)
							convertedKeyCode = KeyEvent.VK_UNDEFINED;
						else
							convertedKeyCode = KeyEvent.VK_1;
						if(shiftHeldDown){
							enableShift(true);
						}
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						
						convertedKeyCode = KeyEvent.VK_1;
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				if( keyPress ){
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						// modifiers &= ~MOD_RIGHT_ALT;
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(true);
					}
					else if((JViewerApp.getInstance().getLed_status() & JViewerApp.CAPSLOCK) == JViewerApp.CAPSLOCK){
						enableShift(false);
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
					}
					else 
					{
						enableShift(true);
						convertedKeyCode = KeyEvent.VK_1;
					}
				}
			}
			break;
		case KeyEvent.VK_LESS:
			if((modifiers & MOD_LEFT_SHIFT) == MOD_LEFT_SHIFT || 
					(modifiers & MOD_RIGHT_SHIFT) == MOD_RIGHT_SHIFT || shiftModified){
				convertedKeyCode = KeyEvent.VK_PERIOD;
				if(keyPress){
					enableShift(true);
					shiftModified = true;
				}
				else {
					if(shiftModified){
						shiftModified = false;
						autoKeybreakModeOn = false;
					}
					else{
						enableShift(false);
						autoKeybreakModeOn = false;
					}
				}
			}
			else{
				
				if( keyPress ){
					
					if((modifiers & MOD_RIGHT_ALT) == MOD_RIGHT_ALT){
						convertedKeyCode = KeyEvent.VK_UNDEFINED;
						enableShift(true);
					}
					else {
						enableShift(true);
						convertedKeyCode = KeyEvent.VK_COMMA;
					}
				}
			}
			break;
		default:
			break;
		}
		return convertedKeyCode;

	}
	/**
	 * Enable Shift key modifier
	 * @param state - true if the Shift modifier needs to be enabled; false otherwise.
	 */
	private void enableShift(boolean state){
		if(state){
			modifiers |= MOD_LEFT_SHIFT;
			modifiers |= MOD_RIGHT_SHIFT;
		}
		else{
			modifiers &= ~MOD_LEFT_SHIFT;
			modifiers &= ~MOD_RIGHT_SHIFT;
		}
	}
}
