package ru.gelin.android.browser.open;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 *  List adapter for the list of browsers
 */
public class BrowsersAdapter extends BaseAdapter {

    static final Intent BROWSER_INTENT = new Intent(Intent.ACTION_VIEW, Uri.parse("http://example.com"));

    static class ResolveInfoAndIntent {
        public ResolveInfo info;
        public Intent intent;
        ResolveInfoAndIntent(ResolveInfo info, Intent intent) {
            this.info = info;
            this.intent = intent;
        }
    }

    Context context;
    List<ResolveInfoAndIntent> browsers;

    public BrowsersAdapter(Context context, Intent intent) {
        this.context = context;
        this.browsers = new ArrayList<ResolveInfoAndIntent>();
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(BROWSER_INTENT, 0);
        for (ResolveInfo browser : info) {;
            ComponentName component = new ComponentName(browser.activityInfo.packageName, browser.activityInfo.name);
            Log.d(Tag.TAG, "Browser: " + component);
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

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(
                    R.layout.activity_list_item, viewGroup, false);
        }
        ResolveInfo browser = getInfo(i);
        if (browser.icon != 0) {
            ImageView icon = (ImageView)view.findViewById(R.id.icon);
            icon.setImageResource(browser.icon);  //TODO cache icons?
        }
        if (browser.labelRes != 0) {
            TextView text = (TextView)view.findViewById(R.id.text1);
            text.setText(browser.labelRes);
        }
        return view;
    }



}
