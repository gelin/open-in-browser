package ru.gelin.android.browser.open;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

/**
 *  Adds one more option: always choose browser.
 *  To be used in main activity.
 */
public class BrowserOptionsAdapter extends BrowsersAdapter {

    public BrowserOptionsAdapter(Context context) {
        super(context, null);
    }

    static enum Type {
        NO_BROWSERS, SINGLE_BROWSER, BROWSER, CHOOSE;
    }

    @Override
    public int getCount() {
        switch (this.browsers.getCount()) {
            case 0:
                return 1;
            case 1:
                return 1;
            default:
                return this.browsers.getCount() + 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return Type.values().length;
    }

    Type getItemType(int position) {
        switch (this.browsers.getCount()) {
            case 0:
                return Type.NO_BROWSERS;
            case 1:
                return Type.SINGLE_BROWSER;
            default:
                if (position < this.browsers.getCount()) {
                    return Type.BROWSER;
                } else {
                    return Type.CHOOSE;
                }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItemType(position).ordinal();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        switch (getItemType(position)) {
            case BROWSER:
                view = super.getView(position, convertView, viewGroup);
                break;
            case SINGLE_BROWSER:
                view = super.getView(position, convertView, viewGroup);
                view.setEnabled(false);
                break;
            case CHOOSE:
                view = getView(convertView, viewGroup, R.layout.choose_browser_list_item);
                break;
            case NO_BROWSERS:
                view = getView(convertView, viewGroup, R.layout.no_browser_list_item);
                break;
        }
        return view;
    }

    View getView(View convertView, ViewGroup viewGroup, int layout) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(layout, viewGroup, false);
        }
        return view;
    }

    @Override
    protected void highlightItem(int position, View view) {
        LinearLayout ll = (LinearLayout)view.findViewById(R.id.list_item);
        RadioButton rb = (RadioButton)view.findViewById(R.id.ch_radio);
        if (this.browsers.isSelected(position)) {
            rb.setChecked(true);
            ll.setBackgroundColor(0x22808080);
        } else {
            rb.setChecked(false);
            ll.setBackgroundColor(0x00000000);
        }
    }

    @Override
    protected View createBrowserView(ViewGroup viewGroup) {
        return LayoutInflater.from(this.context).inflate(
                R.layout.browser_option_list_item, viewGroup, false);
    }

}
