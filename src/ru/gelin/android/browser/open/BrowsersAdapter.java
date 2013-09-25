package ru.gelin.android.browser.open;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 *  List adapter for the list of browsers
 */
public class BrowsersAdapter extends BaseAdapter {

    Context context;
    BrowsersListManager browsers;
    int view_id;
    boolean in_dialog;

    public BrowsersAdapter(Context context) {
        this.context = context;
        this.browsers = new BrowsersListManager(context);
        this.view_id = R.layout.activity_chooser_list_in_settings;
        this.in_dialog = false;
    }

    public BrowsersAdapter(Context context, Intent intent) {
        this.context = context;
        this.browsers = new BrowsersListManager(context, intent);
        this.view_id = R.layout.activity_chooser_view_list_item;
        this.in_dialog = true;
    }

    public BrowsersListManager getManager() {
        return this.browsers;
    }

    @Override
    public int getCount() {
        return this.browsers.getCount();
    }

    @Override
    public Object getItem(int i) {
        return null;
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

        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        icon.setImageDrawable(this.browsers.getIcon(i));

        LinearLayout ll = (LinearLayout)view.findViewById(R.id.list_item);
        RadioButton rb = (RadioButton)view.findViewById(R.id.ch_radio);
             
        TextView text = (TextView)view.findViewById(R.id.title);
    	text.setText(this.browsers.getLabel(i));
        
        if (!in_dialog) {
        	if (this.browsers.isSelected(i)) {
        		rb.setChecked(true);
        		ll.setBackgroundColor(0x22808080);
        	} else {
        		rb.setChecked(false);
        		ll.setBackgroundColor(0x00000000);
        	}
        }
        
        return view;
    }

}
