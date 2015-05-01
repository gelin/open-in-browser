package ru.gelin.android.browser.open.intent;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import ru.gelin.android.browser.open.Tag;

import java.io.*;

/**
 *  Converts the original intent received from the file manager into
 *  the intent which opens the file in the browser.
 *  It accepts 'content:' uris,
 *  reads the content of the uri,
 *  stores it to the temporary file in the application folder
 *  and then returns the temp file uri.
 */
class SaveContentIntentConverter extends IntentConverter {

    static final String CONTENT_SCHEME = "content";
    static final int MAX_SIZE = 20 * 1024 * 1024;       // allow to save files no more than 20 MB
    static final int MAX_AGE = 24 * 60 * 60 * 1000;     // keep files in cache no more than a day

    static char FILE_SEP = '/'; // ... or do this portably.
    static char ESCAPE = '%';   // ... or some other legal char.

    String data;

    /**
     *  Returns the appropriate instance of the converter.
     *  Returns null if the converter cannot be created.
     */
    public static SaveContentIntentConverter getInstance(Context context, Intent intent) {
        Uri data = intent.getData();
        if (data == null) {
            return null;
        }
        if (!CONTENT_SCHEME.equals(data.getScheme())) {
            return null;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            cleanCacheDir(context);
            ContentResolver resolver = context.getContentResolver();
            in = resolver.openInputStream(data);
            File outFile = getOutFile(context, data);
            out = new FileOutputStream(outFile);
            byte[] buf = new byte[1024];
            int read;
            int size = 0;
            while ((read = in.read(buf)) > 0) {
                size += read;
                if (size > MAX_SIZE) {
                    Log.w(Tag.TAG, "Max temp file size exceeded");
                    outFile.delete();
                    return null;
                }
                out.write(buf, 0, read);
            }
            outFile.setReadable(true, false);
            return new SaveContentIntentConverter(outFile.getAbsolutePath());
        } catch (Exception e) {
            Log.w(Tag.TAG, e);
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Log.w(Tag.TAG, "Cannot close file", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.w(Tag.TAG, "Cannot close stream", e);
                }
            }
        }
    }

    /**
     *  Cleans old files from the cache.
     */
    static void cleanCacheDir(Context context) {
        long now = System.currentTimeMillis();
        File dir = context.getCacheDir();
        for (File file : dir.listFiles()) {
            long modified = file.lastModified();
            if ((now - modified) > MAX_AGE) {
                file.delete();
            }
        }
    }

    /**
     *  Returns the temporary filename in the cache dir.
     */
    static File getOutFile(Context context, Uri uri) {
        return new File(context.getCacheDir(), escapeFileName(uri));
    }

    /**
     *  Converts URI to a safe filename.
     *  @link http://stackoverflow.com/questions/1184176/how-can-i-safely-encode-a-string-in-java-to-use-as-a-filename
     */
    static String escapeFileName(Uri uri) {
        String s = String.valueOf(uri);
        int len = s.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            if (ch < ' ' || ch >= 0x7F || ch == FILE_SEP || ch == ':' || (ch == '.' && i == 0) // we don't want to collide with "." or ".."!
                    || ch == ESCAPE) {
                sb.append(ESCAPE);
                if (ch < 0x10) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    SaveContentIntentConverter(String data) {
        this.data = data;
    }

    @Override
    Uri extractUri(Intent intent) {
        return Uri.fromFile(new File(this.data));
    }
}
