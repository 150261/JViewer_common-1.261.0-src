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

////////////////////////////////////////////////////////////////////////////////
//
// This module implements the Localization for the JViewer.
//
package com.ami.kvm.jviewer.gui;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.ami.kvm.jviewer.Debug;
import com.ami.kvm.jviewer.JViewer;


/**
 * This class loads the resource bundle which is used to 
 * localize JViewer GUI components to a particular language.
 */
public class LocaleStrings {

	private static LocaleStrings localeStrings = new LocaleStrings();
	private ResourceBundle resourceBundle = null;
	private ResourceBundle resourceBundleSOC = null;
	private ResourceBundle prevresourceBundle = null;
	private ResourceBundle prevresourceBundleSOC = null;
	private ResourceBundle parentResourceBundle = null;
	private ResourceBundle parentResourceBundleSOC = null;
	private String errorMsg = "";

	/**
	 * LocaleStrings constructor.
	 * Loads the default language resource bundle(English). 
	 */
	private LocaleStrings() {
		parentResourceBundle = ResourceBundle.getBundle("com.ami.kvm.jviewer.lang.Resources_EN");
		parentResourceBundleSOC = ResourceBundle.getBundle("com.ami.kvm.jviewer.soc.lang.SOCResources_EN");
		resourceBundle =parentResourceBundle;
		resourceBundleSOC = parentResourceBundleSOC;
		prevresourceBundle = resourceBundle;
		prevresourceBundleSOC = resourceBundleSOC;
	}

	/**
	 * Sets the language ID for JViewer.
	 * Loads the resource bundle corresponding to the specified language. 
	 * @param str - language ID
	 */
	public static void setLanguageID(String str)
	{
		if(str.length() == 0 || str.equals(null))
			return;
		try{
			localeStrings.prevresourceBundle = localeStrings.resourceBundle;
			localeStrings.resourceBundle = ResourceBundle.getBundle("com.ami.kvm.jviewer.lang.Resources_"+str);
		}catch(MissingResourceException mre){
			Debug.out.println(mre);
			localeStrings.resourceBundle = ResourceBundle.getBundle("com.ami.kvm.jviewer.lang.Resources_EN");
			localeStrings.resourceBundleSOC = ResourceBundle.getBundle("com.ami.kvm.jviewer.soc.lang.SOCResources_EN");
			if(Debug.MODE == Debug.DEBUG){
				localeStrings.errorMsg = getString("AC_2_LS");
			}
			else{
				localeStrings.errorMsg = getString("AC_4_LS");
			}
			JOptionPane.showMessageDialog(null, localeStrings.errorMsg, getString("AC_1_LS"), JOptionPane.ERROR_MESSAGE);
			JViewer.setDefaultLanguage();
			return;
		}
		try{
			localeStrings.prevresourceBundleSOC = localeStrings.resourceBundleSOC;
			localeStrings.resourceBundleSOC = ResourceBundle.getBundle("com.ami.kvm.jviewer.soc.lang.SOCResources_"+str);
		}catch(MissingResourceException mre){
			Debug.out.println(mre);
			localeStrings.resourceBundle = ResourceBundle.getBundle("com.ami.kvm.jviewer.lang.Resources_EN");
			localeStrings.resourceBundleSOC = ResourceBundle.getBundle("com.ami.kvm.jviewer.soc.lang.SOCResources_EN");
			if(Debug.MODE == Debug.DEBUG){
				localeStrings.errorMsg = getString("AC_3_LS");
			}
			else{
				localeStrings.errorMsg = getString("AC_4_LS");
			}
			JOptionPane.showMessageDialog(null, localeStrings.errorMsg, getString("AC_1_LS"), JOptionPane.ERROR_MESSAGE);
			JViewer.setDefaultLanguage();
		}
	}

	/**
	 * Gets the string corresponding to the given key from the string table of the
	 * resource bundle for a particular language.  
	 * @param key - the sting keys
	 * @return - the localized string.
	 */
	public static String getString(String key)
	{
		try{
			return localeStrings.resourceBundle.getString(key);
		}catch (MissingResourceException mre) {
			Debug.out.println(mre);
			return localeStrings.parentResourceBundle.getString(key);
		}
	}

	/**
	* Gets the key corresponding to the given text from the string table of the
	* resource bundle for a particular language.
	* @param ResourceBundle - ResourceBundle resource
	* @param text - the string text
	* @return key - the string keys.
	*/
	private static String convertResourceBundleToProperties(ResourceBundle resource ,String text) {

		Properties properties = new Properties();
		Enumeration<String> keys = resource.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if(text.equalsIgnoreCase(resource.getString(key)))
			{
				return key;
			}
		}
		return null;
	}

	/**
	* Gets the key corresponding to the given text from the string table of the
	* resource bundle for a particular language.
	* @param text - the string text
	* @return key - the string keys.
	*/
	public static String getStringKey(String text){

		String key = null;
		try{
			//Check in SOC bundle first if not avaible check in common bundle
			key = convertResourceBundleToProperties(localeStrings.prevresourceBundleSOC,text);
			if(key == null){
				key = convertResourceBundleToProperties(localeStrings.prevresourceBundle,text);
			}
		}catch (MissingResourceException mre) {
			Debug.out.println(mre);
			key = convertResourceBundleToProperties(localeStrings.parentResourceBundleSOC,text);
			if(key == null){
				key = convertResourceBundleToProperties(localeStrings.parentResourceBundle,text);
			}
		}
		return key;
	}
	
	/**
	 * Gets the string corresponding to the given key from the string table of the
	 * previously loaded resource bundle.  
	 * @param key - the sting keys
	 * @return - the localized string.
	 */
	public static String getPreviousLocaleString(String key){
		String prevLocaleString = null;
		prevLocaleString = localeStrings.prevresourceBundle.getString(key);
		return prevLocaleString;
	}
	/**
	 * Gets the string corresponding to the given key from the string table of the
	 * resource bundle for a particular language for the SOC package.  
	 * @param key - the sting keys
	 * @return - the localized string.
	 */
	public static String getSOCString(String key)
	{
		try{
			return localeStrings.resourceBundleSOC.getString(key);
		}catch (MissingResourceException mre) {
			Debug.out.println(mre);
			return localeStrings.parentResourceBundleSOC.getString(key);
		}
	}

}
