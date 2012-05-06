package ru.gelin.android.browser.open;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import ru.gelin.android.browser.open.intent.IntentConverter;

/**
 *  Converts input view intent into browsable intent.
 */
public class OpenBrowserActivity extends ListActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.open_in_browser);
        Log.d(Tag.TAG, "Intent: " + getIntent());
        try {
            IntentConverter converter = IntentConverter.getInstance(this, getIntent());
            Intent fileIntent = converter.convert();
            Log.d(Tag.TAG, "File: " + fileIntent);
            BrowsersAdapter adapter = new BrowsersAdapter(this, fileIntent);
            switch (adapter.getCount()) {
                case 0:
                    Toast.makeText(this, R.string.no_browsers, Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 1:
                    startActivity(adapter.getIntent(0));
                    finish();
                    break;
                default:
                    setListAdapter(adapter);
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
        startActivity(intent);
        finish();
    }
}