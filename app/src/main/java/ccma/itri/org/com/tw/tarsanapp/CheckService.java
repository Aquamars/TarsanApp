package ccma.itri.org.com.tw.tarsanapp;

import android.app.Application;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by A40503 on 2016/10/14.
 */

public class CheckService extends Application {
    private static boolean mEnabled = false;
    private static CheckService Instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        mEnabled = isAccessibilityEnabled();
    }

    public synchronized static CheckService getInstance(){
        return Instance;
    }

    public boolean checkEnabled(){
        mEnabled = isAccessibilityEnabled();
        return mEnabled;
    }

    private boolean isAccessibilityEnabled(){
        int accessibilityEnabled = 0;
        String LOGTAG = "isAbEnabled";
//        final String LIGHTFLOW_ACCESSIBILITY_SERVICE = "com.example.test/com.example.text.ccessibilityService";
        boolean accessibilityFound = false;

        try {
            accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(),android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.d(LOGTAG, "ACCESSIBILITY: " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.d(LOGTAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }

        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled==1){
            Log.d(LOGTAG, "***ACCESSIBILIY IS ENABLED***: ");

            String settingValue = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            Log.d(LOGTAG, "Setting: " + settingValue);

            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();
                    Log.d(LOGTAG, "Setting: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase("ccma.itri.org.com.tw.tarsanapp/ccma.itri.org.com.tw.tarsanapp.AbService.MyService")){
                        Log.d(LOGTAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }

            Log.d(LOGTAG, "***END***");
        }
        else{
            Log.d(LOGTAG, "***ACCESSIBILIY IS DISABLED***");
        }

        return accessibilityFound;
    }
}
