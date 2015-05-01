package ru.gelin.android.browser.open.intent;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 *  Converts the original intent received from the ASTRO file manager into
 *  the intent which opens the file in the browser.
 *  Example of the intent:
 *  dat=content://com.metago.astro.filesystem/mnt/sdcard/books/mongo/DOCS/1.0 Changelist.html typ=text/html
 *  It removes the 'com.metago.astro.filesystem' prefix and adds 'file://' before it.
 */
class AstroIntentConverter extends IntentConverter {

    static final String CONTENT_SCHEME = "content";
    static final String ASTRO_HOST = "com.metago.astro.filesystem";

    String path;

    /**                                                                                         .
     *  Returns the appropriate instance of the converter.
     *  Returns null if the intent is not applicable.
     */
    public static AstroIntentConverter getInstance(Intent intent) {
        Uri data = intent.getData();
        if (data == null) {
            return null;
        }
        if (!CONTENT_SCHEME.equals(data.getScheme())) {
            return null;
        }
        String host = data.getHost();
        if (!ASTRO_HOST.equals(host)) {
            return null;
        }
        String path = data.getPath();
        return new AstroIntentConverter(path);
    }

    AstroIntentConverter(String path) {
        this.path = path;
    }

    @Override
    Uri extractUri(Intent intent) {
        return Uri.fromFile(new File(this.path));
    }

}
