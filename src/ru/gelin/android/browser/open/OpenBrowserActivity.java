package ru.gelin.android.browser.open;

import ru.gelin.android.browser.open.intent.IntentConverter;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import ru.gelin.android.browser.open.R;

/**
 *  Converts input view intent into browsable intent.
 */
public class OpenBrowserActivity extends ListActivity {

	CheckBox cb;
	BrowsersAdapter adapter;
	
	OibPreferenceManager man;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.open_in_browser);
        Log.d(Tag.TAG, "Intent: " + getIntent());
        man = new OibPreferenceManager(this);
        
        try {
            IntentConverter converter = IntentConverter.getInstance(this, getIntent());
            Intent fileIntent = converter.convert();
            Log.d(Tag.TAG, "File: " + fileIntent);
            adapter = new BrowsersAdapter(this, fileIntent);
            int items_count = adapter.getCount();
            int item_pos = 0;
            String def_package = "";
            switch (items_count) {
                case 0:
                    Toast.makeText(this, R.string.no_browsers, Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 1:
                    startActivity(adapter.getIntent(0));
                    finish();
                    break;
                default:
                	def_package = man.loadSelection();
                	Log.d(Tag.TAG,"default package = "+def_package);
                	if (def_package.length() > 10) for (item_pos=0; item_pos<items_count; item_pos++) {
                		if (def_package.equals(adapter.getBrowserPackage(item_pos))) {
                			startActivity(adapter.getIntent(item_pos));
                			finish();
                			break;
                		}
                	}
                	//not matched
                	setContentView(R.layout.selection_dialog_layout);
                	setListAdapter(adapter);
                	cb = (CheckBox) findViewById(R.id.select_def);
            }
        } catch (Exception e) {
            Log.e(Tag.TAG, "failed to open the file", e);
            Toast.makeText(this, R.string.cannot_open, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        BrowsersAdapter adapter = (BrowsersAdapter)l.getAdapter();
        Intent intent = adapter.getIntent(position);
        if (cb.isChecked()) man.saveSelection(adapter.getBrowserPackage(position));
        startActivity(intent);
        finish();
    }

}
