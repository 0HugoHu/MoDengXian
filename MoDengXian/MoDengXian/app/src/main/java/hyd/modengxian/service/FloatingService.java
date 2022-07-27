package hyd.modengxian.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import hyd.modengxian.R;
import hyd.modengxian.utils.Database;

public class FloatingService extends Service {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Button button;
    private String url;
    private WebView webView;
    private ImageView quit,move,small,hide;
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
        SharedPreferences sp=getSharedPreferences("Temp", MODE_PRIVATE);
        url=sp.getString("HotSearch_Url","");

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
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            layoutParams.width = width;
            layoutParams.height = height*71/100;
            layoutParams.x = 0;
            layoutParams.y = height/6;

            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mDesktopLayout = inflater.inflate(R.layout.floating_window, null);

            webView = mDesktopLayout.findViewById(R.id.floating_window_webview);
            quit = mDesktopLayout.findViewById(R.id.floating_window_quit);
            move = mDesktopLayout.findViewById(R.id.floating_window_move);
            small = mDesktopLayout.findViewById(R.id.floating_window_small);

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
            webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
            webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
            webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            webSettings.setDomStorageEnabled(true);
            webSettings.setTextZoom(100);
            webView.setWebChromeClient(new WebChromeClient());
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            webSettings.setAllowFileAccess(true); //设置可以访问文件
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
            webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
            webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
            webSettings.setUserAgentString(webSettings.getUserAgentString());


            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webView.getSettings().setBlockNetworkImage(false);

            webView.loadUrl(url);
            windowManager.addView(mDesktopLayout, layoutParams);

            move.setOnTouchListener(new FloatingOnTouchListener());
            quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    windowManager.removeView(mDesktopLayout);	// 移除悬浮窗
                    onDestroy();
                    stopSelf();
                }
            });

            small.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    windowManager.removeView(mDesktopLayout);
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    mDesktopLayout = inflater.inflate(R.layout.floating_window_hide, null);
                    layoutParams.format = PixelFormat.RGBA_8888;
                    layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                    layoutParams.width = 120;
                    layoutParams.height = 120;
                    layoutParams.x = width-120;
                    layoutParams.y = height*2/5;
                    windowManager.addView(mDesktopLayout, layoutParams);

                    hide=mDesktopLayout.findViewById(R.id.floating_window_hiding_icon);
                    hide.setOnClickListener(new HideOnClickListener());
                }
            });
        }
    }


    public class HideOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            windowManager.removeView(mDesktopLayout);
            showFloatingWindow();
        }
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(mDesktopLayout, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }



}