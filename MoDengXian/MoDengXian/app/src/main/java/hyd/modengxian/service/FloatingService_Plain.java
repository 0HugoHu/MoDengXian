package hyd.modengxian.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import hyd.modengxian.R;

public class FloatingService_Plain extends Service {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Button button;
    private String url;
    private WebView webView;
    private ImageView quit,move,small,hide,img;
    private TextView sure,not,content;
    private EditText data;
    private View mDesktopLayout;
    private int height,width;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;         // 屏幕宽度（像素）
        height = dm.heightPixels;

        isStarted = true;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {

            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            layoutParams.width = width;
            layoutParams.height = height*70/100;
            layoutParams.x = 0;
            layoutParams.y = 10*height/100;

            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mDesktopLayout = inflater.inflate(R.layout.floating_window_plain, null);



            windowManager.addView(mDesktopLayout, layoutParams);


            quit = mDesktopLayout.findViewById(R.id.floating_window_quit);
            img = mDesktopLayout.findViewById(R.id.plain_img);
            content = mDesktopLayout.findViewById(R.id.plain_text1);


            final SharedPreferences sp=getSharedPreferences("Temp", MODE_PRIVATE);
            content.setText(sp.getString("Content",""));

            quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    windowManager.removeView(mDesktopLayout);
                    onDestroy();
                    stopSelf();
                }
            });



        }
    }






}