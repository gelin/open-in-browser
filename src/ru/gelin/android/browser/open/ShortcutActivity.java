package ru.gelin.android.browser.open;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 *  An activity to create a desktop shortcut for a file.
 */
public class ShortcutActivity extends Activity {

    static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shortcut_activity);
        String initialName = getShortcutName(getIntent());
        EditText text = (EditText) findViewById(R.id.name);
        text.setText(initialName);
    }

    public void onOkClick(View v) {
        EditText text = (EditText) findViewById(R.id.name);
        createShortcut(getIntent(), text.getText().toString());
        finish();
    }

    public void onCancelClick(View v) {
        finish();
    }

    void createShortcut(Intent origIntent, String shortcutName) {
        Intent shortcutIntent = new Intent(Intent.ACTION_VIEW);
        shortcutIntent.setDataAndType(origIntent.getData(), origIntent.getType());
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent addIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));
        sendBroadcast(addIntent);
    }

    String getShortcutName(Intent origIntent) {
        if (origIntent.hasExtra(Intent.EXTRA_SUBJECT)) {
            return origIntent.getStringExtra(Intent.EXTRA_SUBJECT);
        }
        if (origIntent.getData() != null) {
            return origIntent.getData().getLastPathSegment();
        }
        return "";
    }

}