package ru.gelin.android.browser.open;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class BrowsersListManager {

    static final Intent BROWSER_INTENT = new Intent(Intent.ACTION_VIEW, Uri.parse("http://example.com"));
	static final String BROWSER_PREF = "browser";

	Context context;
    SharedPreferences prefs;
    PackageManager pm;
    List<ResolveInfoAndIntent> browsers;
    int selected = -1;

    static class ResolveInfoAndIntent {
        public ResolveInfo info;
        public Intent intent;

        ResolveInfoAndIntent(ResolveInfo info, Intent intent) {
            this.info = info;
            this.intent = intent;
        }

        public String getPackageName() {
            return this.info.activityInfo.packageName;
        }
    }
	
	public BrowsersListManager(Context context) {
		this(context, null);
	}

    public BrowsersListManager(Context context, Intent intent) {
        this.context = context;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
        this.pm = this.context.getPackageManager();
        this.browsers = new ArrayList<ResolveInfoAndIntent>();

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(BROWSER_INTENT, 0);
        for (int i = 0; i < infos.size(); i++) {;
            ResolveInfo browser = infos.get(i);
            ComponentName component = new ComponentName(browser.activityInfo.packageName, browser.activityInfo.name);
            //Log.d(Tag.TAG, "Found browser: " + browser.activityInfo.packageName);
            Intent browserIntent = null;
            if (intent != null) {
                browserIntent = new Intent(intent);
                browserIntent.setComponent(component);
            }
            ResolveInfoAndIntent infoAndIntent = new ResolveInfoAndIntent(browser, browserIntent);
            this.browsers.add(infoAndIntent);
            if (intent != null && infoAndIntent.getPackageName().equals(loadSelection())) {
                this.selected = i;
            }
        }

        if (this.browsers.size() == 1) {
            this.selected = 0;
        }
    }

    /**
     *  Returns the number of found browsers.
     */
    public int getCount() {
        return this.browsers.size();
    }

    /**
     *  Selects one browser from the list
     *  @param position browser position in the list, if out of the range (for example, -1) means clear the selection.
     */
    public void setSelected(int position) {
        if (position >= 0 && position < this.browsers.size()) {
            ResolveInfoAndIntent info = this.browsers.get(position);
            saveSelection(info.getPackageName());
        } else {
            saveSelection("");
        }
        this.selected = position;
    }

    /**
     *  Checks is the browser on the specified position selected.
     */
    public boolean isSelected(int position) {
        return this.selected == position;
    }

    /**
     *  Returns the Intent to start the selected browser.
     */
    public Intent getSelectedIntent() {
        //TODO
        return null;
    }

    /**
     *  Returns the Intent to start the browser on the position.
     */
    public Intent getIntent(int position) {
        //TODO
        //this.browsers.get(position).intent;
        return null;
    }

    /**
     *  Returns icon of the browser on the position.
     *  @return icon drawable or null
     */
    public Drawable getIcon(int position) {
        if (position < 0 || position >= this.browsers.size()) {
            return null;
        }
        ResolveInfoAndIntent info = this.browsers.get(position);
        return info.info.loadIcon(this.pm);  //TODO cache icons
    }

    /**
     *  Returns label of the browser on the position.
     *  @return label or null
     */
    public CharSequence getLabel(int position) {
        if (position < 0 || position >= this.browsers.size()) {
            return null;
        }
        ResolveInfoAndIntent info = this.browsers.get(position);
        return info.info.loadLabel(this.pm);    //TODO cache labels
    }
	
	private void saveSelection(String savingText) {
        Editor editor = this.prefs.edit();
		editor.putString(BROWSER_PREF, savingText);
		editor.commit();
	}
	  
	private String loadSelection() {
		String savedText = this.prefs.getString(BROWSER_PREF, "");
		return savedText;
	}

}
