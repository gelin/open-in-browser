package ru.gelin.android.browser.open;

import ru.gelin.android.browser.open.intent.IntentConverter;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

/**
 *  Converts input view intent into browsable intent.
 */
public class OpenBrowserActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_browser_dialog);
        setTitle(R.string.open_in_browser);
        Log.d(Tag.TAG, "Intent: " + getIntent());

        try {
            IntentConverter converter = IntentConverter.getInstance(this, getIntent());
            Intent fileIntent = converter.convert();
            Log.d(Tag.TAG, "File: " + fileIntent);
            BrowsersAdapter adapter = new BrowsersAdapter(this, fileIntent);
            setListAdapter(adapter);
            BrowsersListManager manager = adapter.getManager();
            if (adapter.getCount() == 0) {
                Toast.makeText(this, R.string.no_browsers, Toast.LENGTH_LONG).show();
                finish();
            } else if (manager.hasSelection()) {
                startActivity(adapter.getManager().getSelectedIntent());
                finish();
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
        BrowsersListManager manager = adapter.getManager();
        CheckBox checkBox = (CheckBox)findViewById(R.id.select_default);
        if (checkBox.isChecked()) {
            manager.setSelected(position);
        }
        try {
            Intent intent = manager.getIntent(position);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(Tag.TAG, "failed to open the file", e);
            Toast.makeText(this, R.string.cannot_open, Toast.LENGTH_LONG).show();
        }
        finish();
    }

}
