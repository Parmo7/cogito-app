package uk.ac.aston.cogito;

import android.app.Application;
import android.content.Context;

public class CogitoApp extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        CogitoApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return CogitoApp.context;
    }
}
