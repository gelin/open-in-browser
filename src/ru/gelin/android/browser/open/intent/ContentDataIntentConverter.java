package ru.gelin.android.browser.open.intent;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import ru.gelin.android.browser.open.Tag;

/**
 *  Converts the original intent received from the file manager into
 *  the intent which opens the file in the browser.
 *  The data of the intent must point to the cursor which must contain the "_data" column.
 *  The "_data" column is treated as a filename.
 */
class ContentDataIntentConverter extends IntentConverter {

    static final String CONTENT_SCHEME = "content";

    Uri uri;

    /**
     *  Returns the appropriate instance of the converter.
     *  Returns null if the converter cannot be created.
     */
    public static ContentDataIntentConverter getInstance(Context context, Intent intent) {
        Uri data = intent.getData();
        if (data == null) {
            return null;
        }
        if (!CONTENT_SCHEME.equals(data.getScheme())) {
            return null;
        }
        try {
            ContentResolver resolver = context.getContentResolver();
            Cursor cursor = resolver.query(data, null, null, null, null);
            cursor.moveToFirst();
            Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
            return new ContentDataIntentConverter(uri);
        } catch (Exception e) {
            Log.w(Tag.TAG, e);
            return null;
        }
    }

    ContentDataIntentConverter(Uri uri) {
        this.uri = uri;
    }

    @Override
    Uri extractUri(Intent intent) {
        return this.uri;
    }
}
