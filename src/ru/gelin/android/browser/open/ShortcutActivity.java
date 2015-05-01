package ru.gelin.android.browser.open;

import android.app.Activity;
import android.os.Bundle;

/**
 *  An activity to create a desktop shortcut for a file.
 */
public class ShortcutActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shortcut);
    }


}