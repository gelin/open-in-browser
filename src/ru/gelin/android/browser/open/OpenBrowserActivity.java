package ru.gelin.android.browser.open;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import ru.gelin.android.browser.open.intent.IntentConverter;

/**
 *  Converts input view intent into browsable intent.
 */
public class OpenBrowserActivity extends Activity {

    static final Intent BROWSER_INTENT = new Intent(Intent.ACTION_VIEW, Uri.parse("http://example.com"));

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Tag.TAG, "Intent: " + getIntent());
        try {
            IntentConverter converter = IntentConverter.getInstance(this, getIntent());
            Intent fileIntent = converter.convert();
            //getString(R.string.open_in_browser)
            //TODO: multiple browsers case
            fileIntent.setComponent(resolveBrowserComponent());
            startActivity(fileIntent);
        } catch (Exception e) {
            Toast.makeText(this, R.string.cannot_open, Toast.LENGTH_LONG).show();
        } finally {
            finish();
        }
    }

    /**
     *  Find the browser to open the file.
     */
    ComponentName resolveBrowserComponent() {
        PackageManager pm = getPackageManager();
        ResolveInfo info = pm.resolveActivity(BROWSER_INTENT, 0);
        ComponentName component = new ComponentName(info.activityInfo.packageName, info.activityInfo.targetActivity);
        return component;
    }

}