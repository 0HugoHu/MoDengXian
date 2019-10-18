package hyd.modengxian.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import hyd.modengxian.MainActivity;


public class MyAccessibilityService extends AccessibilityService {

    public String a="";
    Handler handler;
    public AccessibilityEvent event2;
    public boolean isOnFocusView=false;
    private SharedPreferences sp ;
    private String Shared_SelectedUrl = "/MoDengXianData/Selected.txt";

    private File filePrefix = Environment.getExternalStorageDirectory();
    static Map<Integer, String> packagename;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        sp = getSharedPreferences("Setting", Context.MODE_PRIVATE);
        boolean onAccessibilityService = sp.getBoolean("onAccessibilityService",true);
        packagename = Txt("packagename");
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
                    isOnFocusView=true;
                    break;
                case AccessibilityEvent.TYPE_VIEW_CONTEXT_CLICKED:
                    break;
                case AccessibilityEvent.TYPE_VIEW_FOCUSED:
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
        a="";
        AccessibilityNodeInfo source = event2.getSource();
        if (source == null) {
            return;
        }

        for (int i = 0; i < source.getParent().getChildCount(); i++) {
            final AccessibilityNodeInfo node = source.getParent().getChild(i);
            if (("android.widget.TextView".contentEquals(node.getClassName())||"android.view.View".contentEquals(node.getClassName()))&& !TextUtils.isEmpty(node.getText())) {
                a = a+node.getText().toString();
            }
        }

        String packages=event2.getPackageName().toString();
        System.out.println(packages);
        int temp_i=-1;
        for(int i=0;i<packagename.size();i++){
            if(Objects.requireNonNull(packagename.get(i)).contains(packages)){
                temp_i=i;
                break;
            }
        }
        if(temp_i==-1)
            packages="未知应用";
        else{
            packages=packagename.get(temp_i).split(" ")[0];
        }

        if(!TextUtils.isEmpty(a)) {
            Calendar cal= Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            int day = cal.get(Calendar.DATE);

            sp = getSharedPreferences("Temp", MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("Content", "将下列内容添加至收藏？（点击修改）");
            edit.putString("Decision_type", "Selected");
            edit.putString("Data", a);
            edit.putString("Time", year+"-"+month+"-"+day);
            edit.putString("Package", packages);
            edit.commit();

            Intent intent = new Intent(getApplicationContext(), FloatingService_Dialog.class);
            startService(intent);

            isOnFocusView=false;

        }
    }

    public Map<Integer, String> Txt(String filePath) {
        //将读出来的一行行数据使用Map存储
        Map<Integer, String> map = new HashMap<Integer, String>();
        try {
            int count = 0;//初始化 key值
            InputStreamReader isr = new InputStreamReader(this.getAssets().open(filePath+".txt"));
            BufferedReader br = new BufferedReader(isr);
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                if (!"".equals(lineTxt)&&!(lineTxt.equals("\\r\\n")) ){
                    map.put(count, EncodingUtils.getString(lineTxt.getBytes("utf-8"),"utf-8"));//依次放到map 0，value0;1,value2
                }
                count++;
            }
            isr.close();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
