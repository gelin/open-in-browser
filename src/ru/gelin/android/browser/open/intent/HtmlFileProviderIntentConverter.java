package ru.gelin.android.browser.open.intent;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 *  Converts the original intent received from the com.android.htmlviewer.FileContentProvider into
 *  the intent which opens the file in the browser.
 *  Example of the intent:
 *  action=android.intent.action.VIEW
 *  data=content://com.android.htmlfileprovider/sdcard/tmp/index.html
 *  type=text/html
 */
class HtmlFileProviderIntentConverter extends IntentConverter {

    static final String CONTENT_SCHEME = "content";
    static final String HTMLFILEPROVIDER_HOST = "com.android.htmlfileprovider";

    String path;

    /**                                                                                         .
     *  Returns the appropriate instance of the converter.
     *  Returns null if the intent is not applicable.
     */
    public static HtmlFileProviderIntentConverter getInstance(Intent intent) {
        Uri data = intent.getData();
        if (data == null) {
            return null;
        }
        if (!CONTENT_SCHEME.equals(data.getScheme())) {
            return null;
        }
        String host = data.getHost();
        if (!HTMLFILEPROVIDER_HOST.equals(host)) {
            return null;
        }
        String path = data.getPath();
        return new HtmlFileProviderIntentConverter(path);
    }

    HtmlFileProviderIntentConverter(String path) {
        this.path = path;
    }

    @Override
    Uri extractUri(Intent intent) {
        return Uri.fromFile(new File(this.path));
    }

}
