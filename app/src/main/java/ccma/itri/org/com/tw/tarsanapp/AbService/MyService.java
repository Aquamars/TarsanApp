package ccma.itri.org.com.tw.tarsanapp.AbService;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.os.Vibrator;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import br.com.goncalves.pugnotification.notification.PugNotification;
import ccma.itri.org.com.tw.tarsanapp.MainActivity;

/**
 * Created by A40503 on 2016/10/12.
 */

public class MyService extends AccessibilityService {

    private AccessibilityServiceInfo info;
    private Vibrator myVibrator;
    //Respond to AccessibilityEvents
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String eventText = event.getText().toString();
        showToast("onAccessibilityEvent");
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            myVibrator.vibrate(1000);
        }
        switch(eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                showToast(eventText);
                myVibrator.vibrate(1000);
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                showToast(eventText);
                myVibrator.vibrate(1000);
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                showToast(eventText);
                myVibrator.vibrate(1000);
                break;
        }
        Log.d("service", eventText);
        // Do something nifty with this text, like speak the composed string
        // back to the user.

    }

    @Override
    public void onInterrupt() {

    }

    //Configure the Accessibility Service
    @Override
    protected void onServiceConnected() {
        Toast.makeText(getApplication(), "onServiceConnected", Toast.LENGTH_SHORT).show();
        myVibrator = (Vibrator) getApplication().getSystemService(VIBRATOR_SERVICE);
        myVibrator.vibrate(new long[]{10, 100, 10, 100, 10, 100, 10, 100}, -1);
        // Set the type of events that this service wants to listen to.  Others
        // won't be passed to this service.
//        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
//                AccessibilityEvent.TYPE_VIEW_FOCUSED;

        // If you only want this service to work with specific applications, set their
        // package names here.  Otherwise, when the service is activated, it will listen
        // to events from all applications.
//        info.packageNames = new String[]
//                {"ccma.itri.org.com.tw.tarsanapp"};

        // Set the type of feedback your service will provide.
//        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;

        // Default services are invoked only if no package-specific ones are present
        // for the type of AccessibilityEvent generated.  This service *is*
        // application-specific, so the flag isn't necessary.  If this was a
        // general-purpose service, it would be worth considering setting the
        // DEFAULT flag.

        // info.flags = AccessibilityServiceInfo.DEFAULT;

//        info.notificationTimeout = 100;
//
//        this.setServiceInfo(info);
    }

    private void showEvent(String event){
        PugNotification.with(this)
                .load()
                .message(event)
                .flags(Notification.DEFAULT_ALL)
                .click(MainActivity.class)
                .simple()
                .build();
    }

    private void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


}
