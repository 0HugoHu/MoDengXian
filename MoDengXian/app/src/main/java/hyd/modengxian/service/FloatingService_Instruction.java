package hyd.modengxian.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Build;
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

import hyd.modengxian.R;

public class FloatingService_Instruction extends Service {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Button button;
    private String url;
    private WebView webView;
    private ImageView quit,move,small,hide,img,img2;
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
            layoutParams.y = 20*height/100;

            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mDesktopLayout = inflater.inflate(R.layout.floating_window_instruction, null);



            windowManager.addView(mDesktopLayout, layoutParams);


            quit = mDesktopLayout.findViewById(R.id.floating_window_quit);
            img = mDesktopLayout.findViewById(R.id.plain_img);
            img2 = mDesktopLayout.findViewById(R.id.plain_img2);
            content = mDesktopLayout.findViewById(R.id.plain_text1);


            final SharedPreferences sp=getSharedPreferences("Temp", MODE_PRIVATE);
            Resources res =getResources();
            String[] string=res.getStringArray(R.array.function_instruction);
            switch (sp.getInt("ImgSource",0)){
                case 0:
                    break;
                case 1:
                    img2.setImageDrawable(getDrawable(R.drawable.ti1_eg));
                    content.setText(string[0]);
                    break;
                case 2:
                    img2.setImageDrawable(getDrawable(R.drawable.ti2_eg));
                    content.setText(string[1]);
                    break;
                case 3:
                    img2.setImageDrawable(getDrawable(R.drawable.ti3_eg));
                    content.setText(string[2]);
                    break;
                case 4:
                    img2.setImageDrawable(getDrawable(R.drawable.ti4_eg));
                    content.setText(string[3]);
                    break;
                case 5:
                    img2.setImageDrawable(getDrawable(R.drawable.ti5_eg));
                    content.setText(string[4]);
                    break;
                case 6:
                    //img2.setImageDrawable(getDrawable(R.drawable.ti6_eg));
                    content.setText(string[5]);
                    break;
                case 7:
                    //img2.setImageDrawable(getDrawable(R.drawable.ti7_eg));
                    content.setText(string[6]);
                    break;
                case 8:
                    img2.setImageDrawable(getDrawable(R.drawable.ti8_eg));
                    content.setText(string[7]);
                    break;
                case 9:
                    //img2.setImageDrawable(getDrawable(R.drawable.ti9_eg));
                    content.setText(string[8]);
                    break;
                case 10:
                    //img2.setImageDrawable(getDrawable(R.drawable.ti10_eg));
                    content.setText(string[9]);
                    break;
                case 11:
                    //img2.setImageDrawable(getDrawable(R.drawable.ti0_eg));
                    content.setText(string[10]);
                    break;
                case 12:
                    //img2.setImageDrawable(getDrawable(R.drawable.ti0_eg));
                    content.setText(string[10]);
                    break;
            }

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
