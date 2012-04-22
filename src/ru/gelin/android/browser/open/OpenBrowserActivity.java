package ru.gelin.android.browser.open;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  Converts input view intent into browsable intent.
 */
public class OpenBrowserActivity extends Activity {
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Log.d(Tag.TAG, "Intent: " + intent);
        Uri uri = intent.getData();
        if (uri == null) {
            finish();
            return;
        } else if ("content".equals(uri.getScheme())) {
            startActivityForContent(uri);
        }
        finish();
    }

    void startActivityForContent(Uri uri) {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        Log.d(Tag.TAG, "Columns: " + Arrays.asList(cursor.getColumnNames()));
        cursor.moveToFirst();
        Uri newUri = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)));
        Intent intent = new Intent(Intent.ACTION_VIEW, newUri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getString(R.string.open_in_browser)));
    }

    //TODO: process sifferent content types

    //dat=content://com.metago.astro.filesystem/mnt/sdcard/books/mongo/DOCS/1.0 Changelist.html typ=text/html
    //dat=content://media/external/file/15271 typ=text/html

    //TODO: open only in browsers

}