package ru.gelin.android.browser.open;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class OibPreferenceManager {

	String BROWSER_PREF = "browser";
	Context context;
	
	public OibPreferenceManager(Context context) {
		this.context = context;
	}
	
	void saveSelection(String savingText) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        Editor editor = prefs.edit();
		editor.putString(BROWSER_PREF, savingText);
		editor.commit();
	 }
	  
	 String loadSelection() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
		String savedText = prefs.getString(BROWSER_PREF, "");
		return savedText;
	 }
}
