package ru.gelin.android.browser.open.intent;

import android.content.Intent;
import android.net.Uri;

/**
 *  Transparent converter.
 *  Just returns the Intent data Uri as is.
 */
class TransparentIntentConverter extends IntentConverter {

    Uri data;

    /**                                                                                         .
     *  Returns null if the intent has no data uri.
     */
    public static TransparentIntentConverter getInstance(Intent intent) {
        Uri data = intent.getData();
        if (data == null) {
            return null;
        }
        return new TransparentIntentConverter(data);
    }

    TransparentIntentConverter(Uri data) {
        this.data = data;
    }

    @Override
    Uri extractUri(Intent intent) {
        return this.data;
    }

}
