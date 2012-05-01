package ru.gelin.android.browser.open;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;
import ru.gelin.android.browser.open.intent.IntentConverter;

import java.util.ArrayList;
import java.util.List;

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
            Log.d(Tag.TAG, "File: " + fileIntent);
            Intent chooserIntent = Intent.createChooser(BROWSER_INTENT, getString(R.string.open_in_browser));
            //http://stackoverflow.com/questions/5734678/custom-filtering-of-intent-chooser-based-on-installed-android-package-name
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    resolveBrowserComponents(fileIntent).toArray(new Parcelable[]{}));
            startActivity(chooserIntent);
        } catch (Exception e) {
            Log.e(Tag.TAG, "failed to open the file", e);
            Toast.makeText(this, R.string.cannot_open, Toast.LENGTH_LONG).show();
        } finally {
            finish();
        }
    }

    /**
     *  Find the browser(s) to open the file.
     *  Return the list of intents which component name is set to the browser.
     */
    List<Intent> resolveBrowserComponents(Intent intent) {
        List<Intent> result = new ArrayList<Intent>();
        PackageManager pm = getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(BROWSER_INTENT, 0);
        for (ResolveInfo browser : info) {;
            ComponentName component = new ComponentName(browser.activityInfo.packageName, browser.activityInfo.name);
            Log.d(Tag.TAG, "Browser: " + component);
            Intent browserIntent = new Intent(intent);
            browserIntent.setComponent(component);
            result.add(browserIntent);
        }
        return result;
    }

}