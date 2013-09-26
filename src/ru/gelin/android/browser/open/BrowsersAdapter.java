package ru.gelin.android.browser.open;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  List adapter for the list of browsers
 */
public class BrowsersAdapter extends BaseAdapter {

    Context context;
    BrowsersListManager browsers;

    public BrowsersAdapter(Context context, Intent intent) {
        this.context = context;
        this.browsers = new BrowsersListManager(context, intent);
    }

    public BrowsersListManager getManager() {
        return this.browsers;
    }

    @Override
    public int getCount() {
        return this.browsers.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = createBrowserView(viewGroup);
        }
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        icon.setImageDrawable(this.browsers.getIcon(position));
        TextView text = (TextView)view.findViewById(R.id.title);
    	text.setText(this.browsers.getLabel(position));
        highlightItem(position, view);
        return view;
    }

    protected void highlightItem(int position, View view) {
        //no hightlight here
    }

    protected View createBrowserView(ViewGroup viewGroup) {
        return  LayoutInflater.from(this.context).inflate(
                R.layout.browser_list_item, viewGroup, false);
    }

}
