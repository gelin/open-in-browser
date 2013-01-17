package ru.gelin.android.browser.open;

import java.util.ArrayList;
import java.util.List;

import ru.gelin.android.browser.open.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 *  List adapter for the list of browsers
 */
public class BrowsersAdapter extends BaseAdapter {

    static final Intent BROWSER_INTENT = new Intent(Intent.ACTION_VIEW, Uri.parse("http://example.com"));

    static class ResolveInfoAndIntent {
        public ResolveInfo info;
        public Intent intent;
        public boolean def_checked;
        public boolean selected;
        ResolveInfoAndIntent(ResolveInfo info, Intent intent) {
            this.info = info;
            this.intent = intent;
            this.def_checked = false;
        }
    }

    Context context;
    List<ResolveInfoAndIntent> browsers;
    int view_id;
    boolean in_dialog;
	
    public BrowsersAdapter(Context context, Intent intent, boolean dialog) {
        this.context = context;
        this.browsers = new ArrayList<ResolveInfoAndIntent>();
        in_dialog = dialog;
        if (in_dialog) view_id = R.layout.activity_chooser_view_list_item;
        else view_id = R.layout.activity_chooser_list_in_settings;
        
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(BROWSER_INTENT, 0);
        for (ResolveInfo browser : info) {;
            ComponentName component = new ComponentName(browser.activityInfo.packageName, browser.activityInfo.name);
            //Log.d(Tag.TAG, "Found browser: " + browser.activityInfo.packageName);
            Intent browserIntent = new Intent(intent);
            browserIntent.setComponent(component);
            this.browsers.add(new ResolveInfoAndIntent(browser, browserIntent));
        }
    }

    @Override
    public int getCount() {
        return this.browsers.size();
    }

    @Override
    public Object getItem(int i) {
        return this.browsers.get(i);
    }

    public ResolveInfo getInfo(int i) {
        return this.browsers.get(i).info;
    }

    public Intent getIntent(int i) {
        return this.browsers.get(i).intent;
    }

    public String getBrowserPackage(int i) {
    	return this.browsers.get(i).info.activityInfo.packageName;
    }
    
    public void setChecked(int i) {
    	for (int j=0; j<browsers.size(); j++) {
    		if (i == j) {
    			browsers.get(j).def_checked = true;
    		} else {
    			browsers.get(j).def_checked = false;
    		}
    	}
    }
    
    public void uncheck() {
    	for (int j=0; j<browsers.size(); j++) {
    		browsers.get(j).def_checked = false;
    	}
    }
    
    public void setSelected(int i) {
    	for (int j=0; j<browsers.size(); j++) {
    		if (i == j) {
    			browsers.get(j).selected = true;
    		} else {
    			browsers.get(j).selected = false;
    		}
    	}
    }
      
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(view_id, viewGroup, false);
        }
        ResolveInfo browser = getInfo(i);
        PackageManager pm = this.context.getPackageManager();
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        icon.setImageDrawable(browser.loadIcon(pm));  //TODO cache icons
        LinearLayout ll = (LinearLayout)view.findViewById(R.id.list_item);
        
        if (in_dialog) {
        	TextView text = (TextView)view.findViewById(R.id.title);
        	text.setText(browser.loadLabel(pm));    //TODO cache labels
        } else {
        	CheckedTextView text = (CheckedTextView)view.findViewById(R.id.title);
        	text.setChecked(browsers.get(i).def_checked);
        	text.setText(browser.loadLabel(pm));    //TODO cache labels
        	
        	if (browsers.get(i).selected) ll.setBackgroundColor(0x22808080);
        	else ll.setBackgroundColor(0x00000000);
        }
        
        return view;
    }

}
