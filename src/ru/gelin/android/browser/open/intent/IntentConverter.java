package ru.gelin.android.browser.open.intent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 *  Converts the original intent received from the file manager into
 *  the intent which opens the file in the browser.
 */
public abstract class IntentConverter {

    /** Intent for which the converter is created */
    Intent intent;

    /**
     *  Returns the appropriate instance of the converter.
     *  @throws IllegalArgumentException if the converter cannot be created
     */
    public static IntentConverter getInstance(Context context, Intent intent) throws IllegalArgumentException {
        IntentConverter result;
        result = FileIntentConverter.getInstance(intent);
        if (result != null) {
            return result;
        }
        result = HtmlFileProviderIntentConverter.getInstance(intent);
        if (result != null) {
            return result;
        }
        result = AstroIntentConverter.getInstance(intent);
        if (result != null) {
            return result;
        }
        result = ContentDataIntentConverter.getInstance(context, intent);
        if (result != null) {
            return result;
        }
        throw new IllegalArgumentException("intent is not convertable: " + intent);
    }

    /**
     *  Returns the converted intent.
     */
    public Intent convert() {
        Intent intent = new Intent(Intent.ACTION_VIEW, extractUri(this.intent));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     *  Extracts the Uri from the original intent.
     */
    abstract Uri extractUri(Intent intent);

}
