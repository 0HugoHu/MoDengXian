package hyd.modengxian;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;



public class MyAccessibilityService extends AccessibilityService {

    public String a="";
    Handler handler;
    public AccessibilityEvent event2;
    public boolean isOnFocusView=false;
    private SharedPreferences sp ;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        sp = getSharedPreferences("Setting", Context.MODE_PRIVATE);
        Boolean onAccessibilityService = sp.getBoolean("onAccessibilityService",true);
        if(onAccessibilityService) {

            isOnFocusView = false;
            event2 = event;
            int eventType = event.getEventType();
            switch (eventType) {
                case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                    break;
                case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                    break;
                case AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT:
                    break;
                case AccessibilityEvent.TYPE_GESTURE_DETECTION_END:
                    break;
                case AccessibilityEvent.TYPE_GESTURE_DETECTION_START:
                    break;
                case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                    break;
                case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                    break;
                case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                    break;
                case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END:
                    break;
                case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START:
                    break;
                case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED:
                    break;
                case AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED:
                    break;
                case AccessibilityEvent.TYPE_VIEW_CLICKED:
                    if (!isOnFocusView)
                        onFocusView();
                    break;
                case AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED:
                    if (!isOnFocusView)
                        onFocusView();
                    break;
                case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                    if (!isOnFocusView)
                        onFocusView();
                    break;
                case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                    break;
                case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                    break;
                case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                    break;
                case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                    break;
                case AccessibilityEvent.TYPE_VIEW_SELECTED:
                    break;
                case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                    break;
                case AccessibilityEvent.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY:
                    break;
                case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                    break;
                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                    break;
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                    break;
            }
        }
    }


    @Override
    public void onInterrupt() {

    }

    public void onFocusView(){
        AccessibilityNodeInfo node = event2.getSource();
        if (node ==null) {

        }else {
            a = node.getText() + "";
            if(!a.equals("null"))
            handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), a + "", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



}
