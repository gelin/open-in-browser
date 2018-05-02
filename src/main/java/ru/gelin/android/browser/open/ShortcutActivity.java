package ru.gelin.android.browser.open;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  An activity to create a desktop shortcut for a file.
 */
public class ShortcutActivity extends Activity {

    static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shortcut_activity);
        Intent intent = getIntent();
        Log.d(Tag.TAG, "Intent: " + intent);
        Uri uri = getStreamUri(intent);
        if (uri == null) {
            Toast.makeText(this, R.string.cannot_open, Toast.LENGTH_LONG).show();
            finish();
        }
        TextView data = (TextView) findViewById(R.id.data);
        data.setText(uri.toString());
        String initialName = getShortcutName(intent);
        EditText name = (EditText) findViewById(R.id.name);
        name.setText(initialName);
    }

    Uri getStreamUri(Intent intent) {
        return (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
    }

    String getShortcutName(Intent origIntent) {
        if (origIntent.hasExtra(Intent.EXTRA_SUBJECT)) {
            return origIntent.getStringExtra(Intent.EXTRA_SUBJECT);
        }
        Uri uri = getStreamUri(origIntent);
        if (uri != null) {
            return uri.getLastPathSegment();
        }
        return "";
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
        shortcutIntent.setDataAndType(getStreamUri(origIntent), origIntent.getType());
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent addIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(this, R.drawable.icon));
        sendBroadcast(addIntent);
    }

}