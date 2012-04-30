package ru.gelin.android.browser.open;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import ru.gelin.android.browser.open.intent.IntentConverter;

/**
 *  Converts input view intent into browsable intent.
 */
public class OpenBrowserActivity extends Activity {
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Tag.TAG, "Intent: " + getIntent());
        try {
            IntentConverter converter = IntentConverter.getInstance(this, getIntent());
            startActivity(Intent.createChooser(converter.convert(), getString(R.string.open_in_browser)));
        } catch (Exception e) {
            Toast.makeText(this, R.string.cannot_open, Toast.LENGTH_LONG).show();
        } finally {
            finish();
        }
    }

    //TODO: open only in browsers

}