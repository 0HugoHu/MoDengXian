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

public class FloatingService_Dialog extends Service {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Button button;
    private String url;
    private WebView webView;
    private ImageView quit,move,small,hide;
    private TextView sure,not,content;
    private EditText data;
    private View mDesktopLayout;
    private int height,width;
    private boolean decision;
    private String Shared_SelectedUrl = "/MoDengXianData/Selected.txt";
    private String Shared_OrderUrl = "/MoDengXianData/Order.txt";
    private File filePrefix = Environment.getExternalStorageDirectory();

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
            layoutParams.height = height*50/100;
            layoutParams.x = 0;
            layoutParams.y = height/3;

            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mDesktopLayout = inflater.inflate(R.layout.floating_window_decision, null);



            windowManager.addView(mDesktopLayout, layoutParams);


            sure = mDesktopLayout.findViewById(R.id.decision_sure);
            not = mDesktopLayout.findViewById(R.id.decision_not);
            content = mDesktopLayout.findViewById(R.id.decision_text);
            data = mDesktopLayout.findViewById(R.id.decision_data);

            final SharedPreferences sp=getSharedPreferences("Temp", MODE_PRIVATE);
            content.setText(sp.getString("Content",""));
            data.setText(sp.getString("Data",""));

            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Writer r1 = null;
                    try {
                        r1 = new FileWriter(filePrefix + Shared_SelectedUrl, true);
                        r1.write(data.getText().toString().replace("\n","@&hyd")+"？hyd？"+ sp.getString("Package","")+"？hyd？"+sp.getString("Time","")+ "\n");
                        r1.close();
                        r1 = new FileWriter(filePrefix + Shared_OrderUrl, true);
                        r1.write(2 + "\n");
                        r1.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                        windowManager.removeView(mDesktopLayout);
                        onDestroy();
                        stopSelf();
                }
            });

            not.setOnClickListener(new View.OnClickListener() {
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