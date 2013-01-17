package ru.gelin.android.browser.open.intent;

import android.content.Intent;
import android.net.Uri;

/**
 *  Converts the original intent received from a file manager into
 *  the intent which opens the file in the browser.
 *  This converter handles intents with file: scheme.
 */
class FileIntentConverter extends IntentConverter {

    static final String FILE_SCHEME = "file";

    Uri data;

    /**                                                                                         .
     *  Returns the appropriate instance of the converter.
     *  Returns null if the intent is not applicable.
     */
    public static FileIntentConverter getInstance(Intent intent) {
        Uri data = intent.getData();
        if (data == null) {
            return null;
        }
        if (!FILE_SCHEME.equals(data.getScheme())) {
            return null;
        }
        return new FileIntentConverter(data);
    }

    FileIntentConverter(Uri data) {
        this.data = data;
    }

    @Override
    Uri extractUri(Intent intent) {
        return this.data;
    }

}
