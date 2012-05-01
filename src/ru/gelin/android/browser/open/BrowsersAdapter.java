package ru.gelin.android.browser.open;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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

    List<ResolveInfoAndIntent> browsers;

    public BrowsersAdapter(Context context, Intent intent) {
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
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
