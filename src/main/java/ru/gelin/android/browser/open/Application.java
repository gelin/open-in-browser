package ru.gelin.android.browser.open;

import android.os.StrictMode;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

}
