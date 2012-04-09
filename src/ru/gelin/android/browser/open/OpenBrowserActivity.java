package ru.gelin.android.browser.open;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 *  Converts input view intent into browsable intent.
 */
public class OpenBrowserActivity extends Activity {
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getIntent());
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(Intent.createChooser(intent, getString(R.string.open_in_browser)));
        finish();
    }
    
}