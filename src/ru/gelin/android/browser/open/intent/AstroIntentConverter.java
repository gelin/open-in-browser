package ru.gelin.android.browser.open.intent;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 *  Converts the original intent received from the ASTRO file manager into
 *  the intent which opens the file in the browser.
 *  Example of the intent:
 *  dat=content://com.metago.astro.filesystem/mnt/sdcard/books/mongo/DOCS/1.0 Changelist.html typ=text/html
 */
class AstroIntentConverter extends IntentConverter {

    static final String CONTENT_SCHEME = "content";
    static final String ASTRO_PATH = "com.metago.astro.filesystem";

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
        String path = data.getPath();
        if (!path.startsWith(ASTRO_PATH)) {
            return null;
        }
        return new AstroIntentConverter(path);
    }

    AstroIntentConverter(String path) {
        this.path = path;
    }

    @Override
    Uri extractUri(Intent intent) {
        String fileName = this.path.substring(ASTRO_PATH.length());
        return Uri.fromFile(new File(fileName));
    }

}
