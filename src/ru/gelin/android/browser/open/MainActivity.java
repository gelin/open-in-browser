package ru.gelin.android.browser.open;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListAdapter adapter = new BrowserOptionsAdapter(this);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        try {
            BrowsersAdapter adapter = (BrowsersAdapter)getListAdapter();
            BrowsersListManager manager = adapter.getManager();
            manager.setSelected(position);
            adapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Toast.makeText(this, getString(R.string.err_cant_apply), Toast.LENGTH_SHORT).show();
        }
    }

}
