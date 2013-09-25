package ru.gelin.android.browser.open;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button resetButton = (Button)findViewById(R.id.btnReset);
        resetButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ListAdapter adapter = new BrowsersAdapter(this);
        setListAdapter(adapter);

        OibPreferenceManager prefs = new OibPreferenceManager(this);
        String defaultPackage = prefs.loadSelection();
        Log.d(Tag.TAG, "default package = " + defaultPackage);
    }

    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnReset:
            BrowsersAdapter adapter = (BrowsersAdapter)getListAdapter();
            OibPreferenceManager prefs = new OibPreferenceManager(this);
			adapter.uncheck();
			prefs.saveSelection("");
			setListAdapter(adapter); //way to refresh view
			break;
		}
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        try {
            BrowsersAdapter adapter = (BrowsersAdapter)getListAdapter();
            OibPreferenceManager prefs = new OibPreferenceManager(this);
            adapter.setSelected(position);
            prefs.saveSelection(adapter.getBrowserPackage(position));
        } catch (Exception ex) {
            Toast.makeText(this, getString(R.string.err_cant_apply), Toast.LENGTH_SHORT).show();
        }
    }

}
