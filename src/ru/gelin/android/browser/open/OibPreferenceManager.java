package ru.gelin.android.browser.open;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class OibPreferenceManager {

	String ID_SELECTION;
	SharedPreferences sPref;
	Context caller_context;
	
	public OibPreferenceManager(Context con, String sel) {
		ID_SELECTION = sel;
		//ID_SELECTION = con.getString(R.string.oib_id_selection);
		caller_context = con;
	}
	
	void saveSelection(String savingText) {
		sPref = caller_context.getSharedPreferences(ID_SELECTION,Context.MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putString(ID_SELECTION, savingText);
		ed.commit();
	 }
	  
	 String loadSelection() {
		sPref = caller_context.getSharedPreferences(ID_SELECTION,Context.MODE_PRIVATE);
		String savedText = sPref.getString(ID_SELECTION, "");
		return savedText;
	 }
}
