package hyd.modengxian;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.tencent.sonic.sdk.SonicSession;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hyd.modengxian.fragment.fragment_functions;
import hyd.modengxian.fragment.fragment_home;
import hyd.modengxian.fragment.fragment_settings;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NotificationManager manger;

    private static final int Big_Image = 1000;
    private static final int Image_with_Text = 1001;
    private static final int Favourite = 1002;
    private static final int Game = 1003;
    private static final int Relax = 1004;
    private static final int MyAccessibilityService = 1005;
    private static final int Initial_Notification = 1006;
    private static final int Only_Text = 1007;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager = getSupportFragmentManager();
            final Fragment[] fragment = {null};
            final String[] tag = new String[1];
            //新建一个fragment事务来进行处理
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    tag[0] = ONE;
                    if ((fragment[0] = findFragment(tag[0])) == null) {
                        fragment[0] = new fragment_home();
                        fragmentTransaction.add(R.id.content_frame, fragment[0], tag[0]);
                    }
                    break;
                case R.id.navigation_functions:
                    tag[0] = TWO;
                    if ((fragment[0] = findFragment(tag[0])) == null) {
                        fragment[0] =new fragment_functions();
                        fragmentTransaction.add(R.id.content_frame, fragment[0], tag[0]);
                    }
                    break;
                case R.id.navigation_settings:
                    tag[0] = THREE;
                    if ((fragment[0] = findFragment(tag[0])) == null) {
                        fragment[0] = new fragment_settings();
                        fragmentTransaction.add(R.id.content_frame, fragment[0], tag[0]);
                    }
                    break;
            }
            for (Fragment fragment1 : fragmentManager.getFragments()) {
                if (fragment[0] == fragment1) {
                    fragmentTransaction.show(fragment1);
                } else {
                    fragmentTransaction.hide(fragment1);
                }
            }

            //提交事务
            fragmentTransaction.commitAllowingStateLoss();
            return true;
        }
    };

    private String[] TxtTitleList;
    private String[] TxtSubtitleList;
    private String[] TxtContentList;
    private String[] ImgTitleList;
    private String[] ImgSubtitleList;
    private String[] VocTitleList;
    private String[] VocContentList;
    private String[] VocYinbiaoList;
    private String[] VocMeanList;


//i：大图  j：图文  k：
    private int i=0,j=0,k=0;
    private Boolean Tag_like = false;
    private Boolean Tag_dislike = false;
    private Boolean Tag_Isbigimg = false;
    private Boolean Tag_onAccessibilityService=true;
    private Boolean Tag_isTextwithImg = false;

    private SharedPreferences sp ;
    private String url;
    private WebView webView;
    private final static String PARAM_URL = "param_url";
    private final static String PARAM_MODE = "param_mode";

    private SonicSession sonicSession;

    private List<String> NewsTitle= new ArrayList<>();
    private List<String> NewsSubTitle=new ArrayList<>();
    private List<String> CutContent=new ArrayList<>();
    private List<String> BitmapBytes=new ArrayList<>();

    private int saveData_i=0;
    private int saveData_Progress=0;
    private boolean isFinish=false;
    private Thread mTimeRunnable = new TimeRunnable();
    private int updateText=0;
    private int now;
    private int NewsNum=10;

    private String NewsTitleUrl = "/MoDengXian/NewsTitle.txt";
    private String NewsContentUrl = "/MoDengXian/CutContent.txt";
    private String NewsSubTitleUrl = "/MoDengXian/NewsSubTiTle.txt";
    private String RootUrl = "/MoDengXian";
    private String PicUrl = "/MoDengXian/pic";
    private File filePrefix = Environment.getExternalStorageDirectory();

    private int NewsReadLocation=0;
    private boolean alwaysUpdateData=false;//每次启动始终更新内容


    private FragmentManager fragmentManager;
    private final String ONE = "one";
    private final String TWO = "two";
    private final String THREE = "three";

    private TextView UpdateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        broadcast_init();
        try {
            checkUpdateData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sendNotification(Initial_Notification);
        reloadData();
        test();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentManager = getSupportFragmentManager();
        final Fragment[] fragment = {null};
        final String[] tag = new String[1];
        //新建一个fragment事务来进行处理
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment[0] = new fragment_home();
        fragmentTransaction.add(R.id.content_frame, fragment[0], tag[0]);
        for (Fragment fragment1 : fragmentManager.getFragments()) {
            if (fragment[0] == fragment1) {
                fragmentTransaction.show(fragment1);
            } else {
                fragmentTransaction.hide(fragment1);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();


        manger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Resources resources =getResources();
        TxtTitleList=resources.getStringArray(R.array.newstitle);
        TxtSubtitleList=resources.getStringArray(R.array.newssubtitle);
        TxtContentList=resources.getStringArray(R.array.newscontent);

        ImgTitleList=resources.getStringArray(R.array.imgtitle);
        ImgSubtitleList=resources.getStringArray(R.array.imgsubtitle);

        VocTitleList=resources.getStringArray(R.array.voctitle);
        VocYinbiaoList=resources.getStringArray(R.array.vocyinbiao);
        VocContentList=resources.getStringArray(R.array.voccontent);
        VocMeanList=resources.getStringArray(R.array.vocmean);

        verifyStoragePermissions(MainActivity.this);
        AVOSCloud.initialize(this,"xHiee3l7EnDWTMU2nQXlPdoM-gzGzoHsz","TIYGEvp1RXu3H86O6j55ezx6");
        AVOSCloud.setDebugLogEnabled(true);

/*
        if (!OpenAccessibilitySettingHelper.isAccessibilitySettingsOn(this,
                MyAccessibilityService.class.getName())){// 判断服务是否开启
            Toast.makeText(this, "请找到“莫等闲”并开启辅助功能", Toast.LENGTH_SHORT).show();
            OpenAccessibilitySettingHelper.jumpToSettingPage(this);// 跳转到开启页面
        }else {
            //Toast.makeText(this, "服务已开启", Toast.LENGTH_SHORT).show();
        }
*/
//获取应用接收分享
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action)&&type!=null){
            if ("text/plain".equals(type)){
                dealTextMessage(intent);
            }else if(type.startsWith("image/")){
                dealPicStream(intent);
            }
        }else if (Intent.ACTION_SEND_MULTIPLE.equals(action)&&type!=null){
            if (type.startsWith("image/")){
                dealMultiplePicStream(intent);
            }
        }

        createFile();
    }

    public void broadcast_init(){
        
//大图显示按钮
        MyBroadCast Big_Image_Next = new MyBroadCast();
        IntentFilter filter=new IntentFilter("Big_Image_Next");
        registerReceiver(Big_Image_Next, filter);

        MyBroadCast2 big_Image_Previous = new MyBroadCast2();
        IntentFilter filter2=new IntentFilter("Big_Image_Previous");
        registerReceiver(big_Image_Previous, filter2);
        
//喜欢or不喜欢按钮
        MyBroadCast3 like = new MyBroadCast3();
        IntentFilter filter3=new IntentFilter("Like");
        registerReceiver(like, filter3);

        MyBroadCast4 dislike = new MyBroadCast4();
        IntentFilter filter4=new IntentFilter("Dislike");
        registerReceiver(dislike, filter4);
        
//图文显示按钮
        MyBroadCast5 image_with_Text_Next = new MyBroadCast5();
        IntentFilter filter5=new IntentFilter("Image_with_Text_Next");
        registerReceiver(image_with_Text_Next, filter5);

        MyBroadCast6 image_with_Text_Previous = new MyBroadCast6();
        IntentFilter filter6=new IntentFilter("Image_with_Text_Previous");
        registerReceiver(image_with_Text_Previous, filter6);
        
//初始界面按钮
        MyBroadCast_Main_Game main_Game = new MyBroadCast_Main_Game();
        IntentFilter filter7=new IntentFilter("Main_Game");
        registerReceiver(main_Game, filter7);

        MyBroadCast_Main_Reading main_Reading = new MyBroadCast_Main_Reading();
        IntentFilter filter8=new IntentFilter("Main_Reading");
        registerReceiver(main_Reading, filter8);

        MyBroadCast_Main_Favourite main_Favourite = new MyBroadCast_Main_Favourite();
        IntentFilter filter9=new IntentFilter("Main_Favourite");
        registerReceiver(main_Favourite, filter9);

        MyBroadCast_Main_Relax main_Relax = new MyBroadCast_Main_Relax();
        IntentFilter filter10=new IntentFilter("Main_Relax");
        registerReceiver(main_Relax, filter10);

        MyBroadCast_Main_AccessibilityService MyAccibilityService = new MyBroadCast_Main_AccessibilityService();
        IntentFilter filter11=new IntentFilter("Main_AccessibilityService");
        registerReceiver(MyAccibilityService, filter11);


//关闭按钮
        MyBroadCast7 Close = new MyBroadCast7();
        IntentFilter filter12=new IntentFilter("Close");
        registerReceiver(Close, filter12);


    }

    public void sendNotification(int id) {
        if(id == Big_Image)
            Tag_Isbigimg = true;
        else
            Tag_Isbigimg = false;

        switch (id) {
            case Initial_Notification:
                //常驻通知栏消息
                manger.cancelAll();
                NotificationChannel noti_id_0 = new NotificationChannel("0",
                        "常驻通知栏", NotificationManager.IMPORTANCE_HIGH);
                noti_id_0.enableLights(false);
                noti_id_0.enableVibration(false);
                noti_id_0.setSound(null,null);
                noti_id_0.setVibrationPattern(new long[]{0});
                manger.createNotificationChannel(noti_id_0);
                RemoteViews view_id_0 = new RemoteViews(getPackageName(), R.layout.normal);

                if(Tag_onAccessibilityService) {
                    view_id_0.setImageViewResource(R.id.Normal_Icon, R.drawable.icon_click);
                }else{
                    view_id_0.setImageViewResource(R.id.Normal_Icon, R.drawable.icon);
                }

                Notification notification_id_0 = new NotificationCompat.Builder(this,"0")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("常驻通知栏")
                        .setOngoing(true)
                        .setGroupSummary(false)
                        .setGroup("常驻")
                        .setContent(view_id_0)//设置普通notification_id_0视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action_id_0=new Intent("Main_Game");
                view_id_0.setOnClickPendingIntent(R.id.Normal_Game,PendingIntent.getBroadcast(MainActivity.this, 0, action_id_0,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action2_id_02=new Intent("Main_Reading");
                view_id_0.setOnClickPendingIntent(R.id.Normal_Reading,PendingIntent.getBroadcast(MainActivity.this, 1, action2_id_02,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action3_id_03=new Intent("Main_Favourite");
                view_id_0.setOnClickPendingIntent(R.id.Normal_Favourite,PendingIntent.getBroadcast(MainActivity.this, 2, action3_id_03,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action4_id_04=new Intent("Main_Relax");
                view_id_0.setOnClickPendingIntent(R.id.Normal_Relax,PendingIntent.getBroadcast(MainActivity.this, 3, action4_id_04,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action5_id_0=new Intent("Main_AccessibilityService");
                view_id_0.setOnClickPendingIntent(R.id.Normal_Icon,PendingIntent.getBroadcast(MainActivity.this, 5, action5_id_0,PendingIntent.FLAG_UPDATE_CURRENT));


                NotificationManager manager_id_0 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_0 != null) {
                    manager_id_0.notify(0, notification_id_0);
                }

                break;


            case Big_Image:

                NotificationChannel noti_id_1_Big_Image = new NotificationChannel("1",
                        "大图", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_1_Big_Image);
                RemoteViews bigView_id_1_Big_Image = new RemoteViews(getPackageName(), R.layout.activity_image);

                if(Tag_like && Tag_Isbigimg) {
                    bigView_id_1_Big_Image.setImageViewResource(R.id.btnLike, R.drawable.like_press);
                }else{
                    bigView_id_1_Big_Image.setImageViewResource(R.id.btnLike, R.drawable.like);
                }

                if(Tag_dislike && Tag_Isbigimg) {
                    bigView_id_1_Big_Image.setImageViewResource(R.id.btnDislike, R.drawable.dislike_press);
                }else{
                    bigView_id_1_Big_Image.setImageViewResource(R.id.btnDislike, R.drawable.dislike);
                }

                bigView_id_1_Big_Image.setTextViewText(R.id.Image_Title, ImgTitleList[i]);
                bigView_id_1_Big_Image.setTextViewText(R.id.Image_SubTitle, ImgSubtitleList[i]);
                //bigView_id_1_Big_Image.setImageViewResource(R.id.Image_Img,ImgImg[i]);
                RemoteViews view_id_1_Big_Image = new RemoteViews(getPackageName(), R.layout.normal_down);
                Notification notification_id_1_Big_Image = new NotificationCompat.Builder(this,"1")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("大图")
                        .setOngoing(true)
                        .setGroupSummary(false)
                        .setGroup("其它")
                        .setContent(view_id_1_Big_Image)//设置普通notification_id_1_Big_Image视图
                        .setCustomBigContentView(bigView_id_1_Big_Image)//设置显示bigView_id_1_Big_Image的notification_id_1_Big_Image视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action_id_1_Big_Image=new Intent("Big_Image_Next");
                bigView_id_1_Big_Image.setOnClickPendingIntent(R.id.btnNext,PendingIntent.getBroadcast(MainActivity.this, 11, action_id_1_Big_Image,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action2_id_1_Big_Image2=new Intent("Big_Image_Previous");
                bigView_id_1_Big_Image.setOnClickPendingIntent(R.id.btnPrevious,PendingIntent.getBroadcast(MainActivity.this, 12, action2_id_1_Big_Image2,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action_Like=new Intent("Like");
                bigView_id_1_Big_Image.setOnClickPendingIntent(R.id.btnLike,PendingIntent.getBroadcast(MainActivity.this, 13, action_Like,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action_Dislike=new Intent("Dislike");
                bigView_id_1_Big_Image.setOnClickPendingIntent(R.id.btnDislike,PendingIntent.getBroadcast(MainActivity.this, 14, action_Dislike,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action3_id_1_Big_Image2=new Intent("Close");
                bigView_id_1_Big_Image.setOnClickPendingIntent(R.id.btnClose,PendingIntent.getBroadcast(MainActivity.this, 15, action3_id_1_Big_Image2,PendingIntent.FLAG_UPDATE_CURRENT));

                NotificationManager manager_id_1_Big_Image = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_1_Big_Image != null) {
                    manager_id_1_Big_Image.notify(1, notification_id_1_Big_Image);
                }

                NotificationChannel noti_id_0_Big_Image = new NotificationChannel("0",
                        "大图", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_0_Big_Image);
                RemoteViews view_id_0_Big_Image = new RemoteViews(getPackageName(), R.layout.normal_reading);


                if(Tag_onAccessibilityService) {
                    view_id_0_Big_Image.setImageViewResource(R.id.Normal_Icon, R.drawable.icon_click);
                }else{
                    view_id_0_Big_Image.setImageViewResource(R.id.Normal_Icon, R.drawable.icon);
                }


                Notification notification_id_0_Big_Image = new NotificationCompat.Builder(this,"0")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("大图")
                        .setOngoing(true)
                        .setGroupSummary(false)
                        .setGroup("常驻")
                        .setContent(view_id_0_Big_Image)//设置普通notification_id_0_Big_Image视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action_id_0_Big_Image=new Intent("Main_Game");
                view_id_0_Big_Image.setOnClickPendingIntent(R.id.Normal_Game,PendingIntent.getBroadcast(MainActivity.this, 1, action_id_0_Big_Image,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action2_id_0_Big_Image=new Intent("Main_Reading");
                view_id_0_Big_Image.setOnClickPendingIntent(R.id.Normal_Reading,PendingIntent.getBroadcast(MainActivity.this, 2, action2_id_0_Big_Image,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action3_id_0_Big_Image=new Intent("Main_Favourite");
                view_id_0_Big_Image.setOnClickPendingIntent(R.id.Normal_Favourite,PendingIntent.getBroadcast(MainActivity.this, 3, action3_id_0_Big_Image,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action4_id_0_Big_Image=new Intent("Main_Relax");
                view_id_0_Big_Image.setOnClickPendingIntent(R.id.Normal_Relax,PendingIntent.getBroadcast(MainActivity.this, 4, action4_id_0_Big_Image,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action5_id_0_Big_Image=new Intent("Main_AccessibilityService");
                view_id_0_Big_Image.setOnClickPendingIntent(R.id.Normal_Icon,PendingIntent.getBroadcast(MainActivity.this, 5, action5_id_0_Big_Image,PendingIntent.FLAG_UPDATE_CURRENT));


                NotificationManager manager_id_0_Big_Image = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_0_Big_Image != null) {
                    manager_id_0_Big_Image.notify(0, notification_id_0_Big_Image);
                }

                break;

            case Image_with_Text:

                NotificationChannel noti_id_1_Image_with_Text = new NotificationChannel("1",
                        "图文", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_1_Image_with_Text);
                RemoteViews view_id_1_Image_with_Text = new RemoteViews(getPackageName(), R.layout.normal_down);
                RemoteViews bigView_id_1_Image_with_Text = new RemoteViews(getPackageName(), R.layout.activity_text);

                if(Tag_like && !Tag_Isbigimg) {
                    bigView_id_1_Image_with_Text.setImageViewResource(R.id.btnLike, R.drawable.like_press);
                }else{
                    bigView_id_1_Image_with_Text.setImageViewResource(R.id.btnLike, R.drawable.like);
                }

                if(Tag_dislike && !Tag_Isbigimg) {
                    bigView_id_1_Image_with_Text.setImageViewResource(R.id.btnDislike, R.drawable.dislike_press);
                }else{
                    bigView_id_1_Image_with_Text.setImageViewResource(R.id.btnDislike, R.drawable.dislike);
                }

                bigView_id_1_Image_with_Text.setTextViewText(R.id.Text_Title, NewsTitle.get(j));
                bigView_id_1_Image_with_Text.setTextViewText(R.id.Text_SubTitle, NewsSubTitle.get(j));
                String[] temp2=null;
                temp2 = CutContent.get(j).split("hyd");
                bigView_id_1_Image_with_Text.setTextViewText(R.id.Text_Content,temp2[0]);
                bigView_id_1_Image_with_Text.setTextViewText(R.id.Text_Content2,temp2[1]);
                Bitmap bitmap = getLoacalBitmap(filePrefix +PicUrl+"/a" + j + ".jpg");
                bigView_id_1_Image_with_Text.setImageViewBitmap(R.id.Text_Img,bitmap);
                Notification notification_id_1_Image_with_Text = new NotificationCompat.Builder(this,"1")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("图文")
                        .setOngoing(true)
                        .setGroupSummary(false)
                        .setGroup("其它")
                        .setContent(view_id_1_Image_with_Text)//设置普通notification视图
                        .setCustomBigContentView(bigView_id_1_Image_with_Text)//设置显示bigView的notification视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action_id_1_Image_with_Text=new Intent("Image_with_Text_Next");
                bigView_id_1_Image_with_Text.setOnClickPendingIntent(R.id.btnNext,PendingIntent.getBroadcast(MainActivity.this, 15, action_id_1_Image_with_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action2_id_1_Image_with_Text=new Intent("Image_with_Text_Previous");
                bigView_id_1_Image_with_Text.setOnClickPendingIntent(R.id.btnPrevious,PendingIntent.getBroadcast(MainActivity.this, 16, action2_id_1_Image_with_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action3_id_1_Image_with_Text=new Intent("Close");
                bigView_id_1_Image_with_Text.setOnClickPendingIntent(R.id.btnClose,PendingIntent.getBroadcast(MainActivity.this, 17, action3_id_1_Image_with_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                action_Like=new Intent("Like");
                action_Dislike=new Intent("Dislike");
                bigView_id_1_Image_with_Text.setOnClickPendingIntent(R.id.btnLike,PendingIntent.getBroadcast(MainActivity.this, 13, action_Like,PendingIntent.FLAG_UPDATE_CURRENT));
                bigView_id_1_Image_with_Text.setOnClickPendingIntent(R.id.btnDislike,PendingIntent.getBroadcast(MainActivity.this, 14, action_Dislike,PendingIntent.FLAG_UPDATE_CURRENT));

                NotificationManager manager_id_1_Image_with_Text = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_1_Image_with_Text != null) {
                    manager_id_1_Image_with_Text.notify(1, notification_id_1_Image_with_Text);
                }

                NotificationChannel noti_id_0_Image_with_Text = new NotificationChannel("0",
                        "图文", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_0_Image_with_Text);
                RemoteViews view_id_0_Image_with_Text = new RemoteViews(getPackageName(), R.layout.normal_reading);

                if(Tag_onAccessibilityService) {
                    view_id_0_Image_with_Text.setImageViewResource(R.id.Normal_Icon, R.drawable.icon_click);
                }else{
                    view_id_0_Image_with_Text.setImageViewResource(R.id.Normal_Icon, R.drawable.icon);
                }



                Notification notification_id_0_Image_with_Text = new NotificationCompat.Builder(this,"0")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("图文")
                        .setGroupSummary(false)
                        .setGroup("常驻")
                        .setOngoing(true)
                        .setContent(view_id_0_Image_with_Text)//设置普通notification_id_0_Image_with_Text视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action_id_0_Image_with_Text=new Intent("Main_Game");
                view_id_0_Image_with_Text.setOnClickPendingIntent(R.id.Normal_Game,PendingIntent.getBroadcast(MainActivity.this, 1, action_id_0_Image_with_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action2_id_0_Image_with_Text=new Intent("Main_Reading");
                view_id_0_Image_with_Text.setOnClickPendingIntent(R.id.Normal_Reading,PendingIntent.getBroadcast(MainActivity.this, 2, action2_id_0_Image_with_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action3_id_0_Image_with_Text=new Intent("Main_Favourite");
                view_id_0_Image_with_Text.setOnClickPendingIntent(R.id.Normal_Favourite,PendingIntent.getBroadcast(MainActivity.this, 3, action3_id_0_Image_with_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action4_id_0_Image_with_Text=new Intent("Main_Relax");
                view_id_0_Image_with_Text.setOnClickPendingIntent(R.id.Normal_Relax,PendingIntent.getBroadcast(MainActivity.this, 4, action4_id_0_Image_with_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action5_id_0_Image_with_Text=new Intent("Main_AccessibilityService");
                view_id_0_Image_with_Text.setOnClickPendingIntent(R.id.Normal_Icon,PendingIntent.getBroadcast(MainActivity.this, 5, action5_id_0_Image_with_Text,PendingIntent.FLAG_UPDATE_CURRENT));


                NotificationManager manager_id_0_Image_with_Text = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_0_Image_with_Text != null) {
                    manager_id_0_Image_with_Text.notify(0, notification_id_0_Image_with_Text);
                }

                break;

            case Only_Text:

                NotificationChannel noti_id_1_Only_Text = new NotificationChannel("1",
                        "文字", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_1_Only_Text);
                RemoteViews view_id_1_Only_Text = new RemoteViews(getPackageName(), R.layout.normal_down);
                RemoteViews bigView_id_1_Only_Text = new RemoteViews(getPackageName(), R.layout.activity_onlytext);

                if(Tag_like && !Tag_Isbigimg) {
                    bigView_id_1_Only_Text.setImageViewResource(R.id.btnLike, R.drawable.like_press);
                }else{
                    bigView_id_1_Only_Text.setImageViewResource(R.id.btnLike, R.drawable.like);
                }

                if(Tag_dislike && !Tag_Isbigimg) {
                    bigView_id_1_Only_Text.setImageViewResource(R.id.btnDislike, R.drawable.dislike_press);
                }else{
                    bigView_id_1_Only_Text.setImageViewResource(R.id.btnDislike, R.drawable.dislike);
                }

                bigView_id_1_Only_Text.setTextViewText(R.id.Text_Title, NewsTitle.get(j));
                bigView_id_1_Only_Text.setTextViewText(R.id.Text_SubTitle, NewsSubTitle.get(j));
                String[] temp3=null;
                temp3 = CutContent.get(j).split("hyd");
                bigView_id_1_Only_Text.setTextViewText(R.id.Text_Content,temp3[0]);
                bigView_id_1_Only_Text.setTextViewText(R.id.Text_Content2,temp3[1]);

                Notification notification_id_1_Only_Text = new NotificationCompat.Builder(this,"1")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("文字")
                        .setOngoing(true)
                        .setGroupSummary(false)
                        .setGroup("其它")
                        .setContent(view_id_1_Only_Text)//设置普通notification视图
                        .setCustomBigContentView(bigView_id_1_Only_Text)//设置显示bigView的notification视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action_id_1_Only_Text=new Intent("Image_with_Text_Next");
                bigView_id_1_Only_Text.setOnClickPendingIntent(R.id.btnNext,PendingIntent.getBroadcast(MainActivity.this, 15, action_id_1_Only_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action2_id_1_Only_Text=new Intent("Image_with_Text_Previous");
                bigView_id_1_Only_Text.setOnClickPendingIntent(R.id.btnPrevious,PendingIntent.getBroadcast(MainActivity.this, 16, action2_id_1_Only_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action3_id_1_Only_Text=new Intent("Close");
                bigView_id_1_Only_Text.setOnClickPendingIntent(R.id.btnClose,PendingIntent.getBroadcast(MainActivity.this, 17, action3_id_1_Only_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                action_Like=new Intent("Like");
                action_Dislike=new Intent("Dislike");
                bigView_id_1_Only_Text.setOnClickPendingIntent(R.id.btnLike,PendingIntent.getBroadcast(MainActivity.this, 13, action_Like,PendingIntent.FLAG_UPDATE_CURRENT));
                bigView_id_1_Only_Text.setOnClickPendingIntent(R.id.btnDislike,PendingIntent.getBroadcast(MainActivity.this, 14, action_Dislike,PendingIntent.FLAG_UPDATE_CURRENT));

                NotificationManager manager_id_1_Only_Text = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_1_Only_Text != null) {
                    manager_id_1_Only_Text.notify(1, notification_id_1_Only_Text);
                }

                NotificationChannel noti_id_0_Only_Text = new NotificationChannel("0",
                        "文字", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_0_Only_Text);
                RemoteViews view_id_0_Only_Text = new RemoteViews(getPackageName(), R.layout.normal_reading);

                if(Tag_onAccessibilityService) {
                    view_id_0_Only_Text.setImageViewResource(R.id.Normal_Icon, R.drawable.icon_click);
                }else{
                    view_id_0_Only_Text.setImageViewResource(R.id.Normal_Icon, R.drawable.icon);
                }


                Notification notification_id_0_Only_Text = new NotificationCompat.Builder(this,"0")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("文字")
                        .setGroupSummary(false)
                        .setGroup("常驻")
                        .setOngoing(true)
                        .setContent(view_id_0_Only_Text)//设置普通notification_id_0_Only_Text视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action_id_0_Only_Text=new Intent("Main_Game");
                view_id_0_Only_Text.setOnClickPendingIntent(R.id.Normal_Game,PendingIntent.getBroadcast(MainActivity.this, 1, action_id_0_Only_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action2_id_0_Only_Text=new Intent("Main_Reading");
                view_id_0_Only_Text.setOnClickPendingIntent(R.id.Normal_Reading,PendingIntent.getBroadcast(MainActivity.this, 2, action2_id_0_Only_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action3_id_0_Only_Text=new Intent("Main_Favourite");
                view_id_0_Only_Text.setOnClickPendingIntent(R.id.Normal_Favourite,PendingIntent.getBroadcast(MainActivity.this, 3, action3_id_0_Only_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action4_id_0_Only_Text=new Intent("Main_Relax");
                view_id_0_Only_Text.setOnClickPendingIntent(R.id.Normal_Relax,PendingIntent.getBroadcast(MainActivity.this, 4, action4_id_0_Only_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action5_id_0_Only_Text=new Intent("Main_AccessibilityService");
                view_id_0_Only_Text.setOnClickPendingIntent(R.id.Normal_Icon,PendingIntent.getBroadcast(MainActivity.this, 5, action5_id_0_Only_Text,PendingIntent.FLAG_UPDATE_CURRENT));


                NotificationManager manager_id_0_Only_Text = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_0_Only_Text != null) {
                    manager_id_0_Only_Text.notify(0, notification_id_0_Only_Text);
                }

                break;

            case Favourite:

                NotificationChannel noti_id_1_Favourite = new NotificationChannel("1",
                        "收藏", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_1_Favourite);
                RemoteViews view_id_1_Favourite = new RemoteViews(getPackageName(), R.layout.normal_down);
                RemoteViews bigView_id_1_Favourite = new RemoteViews(getPackageName(), R.layout.activity_favourite);

                bigView_id_1_Favourite.setImageViewResource(R.id.Favourite_ImageView, R.drawable.favourite_aqy);


                Notification notification_id_1_Favourite = new NotificationCompat.Builder(this,"1")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("收藏")
                        .setOngoing(true)
                        .setGroupSummary(false)
                        .setGroup("其它1")
                        .setContentTitle("爱奇艺")
                        .setContentText("你和我的倾城时光")
                  //      .setContent(view_id_1_Favourite)//设置普通notification视图
                        .setCustomBigContentView(bigView_id_1_Favourite)//设置显示bigView的notification视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action1_id_1_Favourite=new Intent("Close");
                bigView_id_1_Favourite.setOnClickPendingIntent(R.id.btnClose,PendingIntent.getBroadcast(MainActivity.this, 11, action1_id_1_Favourite,PendingIntent.FLAG_UPDATE_CURRENT));


                NotificationManager manager_id_1_Favourite = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_1_Favourite != null) {
                    manager_id_1_Favourite.notify(1, notification_id_1_Favourite);
                }

                NotificationChannel noti_id_0_Favourite = new NotificationChannel("0",
                        "收藏", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_0_Favourite);
                RemoteViews view_id_0_Favourite = new RemoteViews(getPackageName(), R.layout.normal_favourite);

                if(Tag_onAccessibilityService) {
                    view_id_0_Favourite.setImageViewResource(R.id.Normal_Icon, R.drawable.icon_click);
                }else{
                    view_id_0_Favourite.setImageViewResource(R.id.Normal_Icon, R.drawable.icon);
                }

// **************************************************************************************************************
// *********************************以下代码为展示用收藏界面的重复通知*********************************************
                NotificationChannel noti_id_1_Favourite2 = new NotificationChannel("2",
                        "收藏", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_1_Favourite2);
                RemoteViews view_id_1_Favourite2 = new RemoteViews(getPackageName(), R.layout.normal_down);
                RemoteViews bigView_id_1_Favourite2 = new RemoteViews(getPackageName(), R.layout.activity_favourite);

                bigView_id_1_Favourite2.setImageViewResource(R.id.Favourite_ImageView, R.drawable.favourite_wyy);

                Notification notification_id_1_Favourite2 = new NotificationCompat.Builder(this,"2")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("收藏")
                        .setOngoing(true)
                        .setGroupSummary(false)
                        .setGroup("其它2")
                        .setContentTitle("网易有道词典")
                        .setContentText("《雅思单词》")
                   //     .setContent(view_id_1_Favourite2)//设置普通notification视图
                        .setCustomBigContentView(bigView_id_1_Favourite2)//设置显示bigView的notification视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action1_id_1_Favourite2=new Intent("Close");
                bigView_id_1_Favourite2.setOnClickPendingIntent(R.id.btnClose,PendingIntent.getBroadcast(MainActivity.this, 12, action1_id_1_Favourite2,PendingIntent.FLAG_UPDATE_CURRENT));


                NotificationManager manager_id_1_Favourite2 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_1_Favourite2 != null) {
                    manager_id_1_Favourite2.notify(2, notification_id_1_Favourite2);
                }

                NotificationChannel noti_id_1_Favourite3 = new NotificationChannel("3",
                        "收藏", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_1_Favourite3);
                RemoteViews view_id_1_Favourite3 = new RemoteViews(getPackageName(), R.layout.normal_down);
                RemoteViews bigView_id_1_Favourite3 = new RemoteViews(getPackageName(), R.layout.activity_favourite);

                bigView_id_1_Favourite3.setImageViewResource(R.id.Favourite_ImageView, R.drawable.favourite);

                Notification notification_id_1_Favourite3 = new NotificationCompat.Builder(this,"3")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("收藏")
                        .setOngoing(true)
                        .setGroupSummary(false)
                        .setGroup("其它3")
                        .setContentTitle("知乎")
                        .setContentText("哪些气质猥琐的动物？")
                   //     .setContent(view_id_1_Favourite3)//设置普通notification视图
                        .setCustomBigContentView(bigView_id_1_Favourite3)//设置显示bigView的notification视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action1_id_1_Favourite3=new Intent("Close");
                bigView_id_1_Favourite3.setOnClickPendingIntent(R.id.btnClose,PendingIntent.getBroadcast(MainActivity.this, 13, action1_id_1_Favourite3,PendingIntent.FLAG_UPDATE_CURRENT));


                NotificationManager manager_id_1_Favourite3 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_1_Favourite3 != null) {
                    manager_id_1_Favourite3.notify(3, notification_id_1_Favourite3);
                }


                NotificationChannel noti_id_1_Favourite4 = new NotificationChannel("4",
                        "收藏", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_1_Favourite4);
                RemoteViews view_id_1_Favourite4 = new RemoteViews(getPackageName(), R.layout.activity_favourite_search);

                Notification notification_id_1_Favourite4 = new NotificationCompat.Builder(this,"4")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("收藏")
                        .setOngoing(true)
                        .setGroupSummary(false)
                        .setGroup("其它4")
                        .setContent(view_id_1_Favourite4)//设置普通notification视图
                       // .setCustomBigContentView(bigView_id_1_Favourite4)//设置显示bigView的notification视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                NotificationManager manager_id_1_Favourite4 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_1_Favourite4 != null) {
                    manager_id_1_Favourite4.notify(4, notification_id_1_Favourite4);
                }
// *********************************以上代码为展示用收藏界面的重复通知*********************************************
// **************************************************************************************************************

                Notification notification_id_0_Favourite = new NotificationCompat.Builder(this,"0")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("收藏")
                        .setOngoing(true)
                        .setGroupSummary(false)
                        .setGroup("常驻")
                        .setContent(view_id_0_Favourite)
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action_id_0_Favourite=new Intent("Main_Game");
                view_id_0_Favourite.setOnClickPendingIntent(R.id.Normal_Game,PendingIntent.getBroadcast(MainActivity.this, 1, action_id_0_Favourite,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action2_id_0_Favourite=new Intent("Main_Reading");
                view_id_0_Favourite.setOnClickPendingIntent(R.id.Normal_Reading,PendingIntent.getBroadcast(MainActivity.this, 2, action2_id_0_Favourite,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action3_id_0_Favourite=new Intent("Main_Favourite");
                view_id_0_Favourite.setOnClickPendingIntent(R.id.Normal_Favourite,PendingIntent.getBroadcast(MainActivity.this, 3, action3_id_0_Favourite,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action4_id_0_Favourite=new Intent("Main_Relax");
                view_id_0_Favourite.setOnClickPendingIntent(R.id.Normal_Relax,PendingIntent.getBroadcast(MainActivity.this, 4, action4_id_0_Favourite,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action5_id_0_Favourite=new Intent("Main_AccessibilityService");
                view_id_0_Favourite.setOnClickPendingIntent(R.id.Normal_Icon,PendingIntent.getBroadcast(MainActivity.this, 5, action5_id_0_Favourite,PendingIntent.FLAG_UPDATE_CURRENT));


                NotificationManager manager_id_0_Favourite = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_0_Favourite != null) {
                    manager_id_0_Favourite.notify(0, notification_id_0_Favourite);
                }

                break;

            case Game:

                NotificationChannel noti_id_1_Game = new NotificationChannel("1",
                        "游戏", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_1_Game);
                RemoteViews view_id_1_Game = new RemoteViews(getPackageName(), R.layout.normal_down);
                RemoteViews bigView_id_1_Game = new RemoteViews(getPackageName(), R.layout.activity_game);

                Notification notification_id_1_Game = new NotificationCompat.Builder(this,"1")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("游戏")
                        .setOngoing(true)
                        .setGroupSummary(false)
                        .setGroup("其它")
                        .setContent(view_id_1_Game)//设置普通notification视图
                        .setCustomBigContentView(bigView_id_1_Game)//设置显示bigView的notification视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action1_id_1_Game=new Intent("Close");
                bigView_id_1_Game.setOnClickPendingIntent(R.id.btnClose,PendingIntent.getBroadcast(MainActivity.this, 11, action1_id_1_Game,PendingIntent.FLAG_UPDATE_CURRENT));


                NotificationManager manager_id_1_Game = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_1_Game != null) {
                    manager_id_1_Game.notify(1, notification_id_1_Game);
                }

                NotificationChannel noti_id_0_Game = new NotificationChannel("0",
                        "游戏", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_0_Game);
                RemoteViews view_id_0_Game = new RemoteViews(getPackageName(), R.layout.normal_game);

                if(Tag_onAccessibilityService) {
                    view_id_0_Game.setImageViewResource(R.id.Normal_Icon, R.drawable.icon_click);
                }else{
                    view_id_0_Game.setImageViewResource(R.id.Normal_Icon, R.drawable.icon);
                }


                Notification notification_id_0_Game = new NotificationCompat.Builder(this,"0")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("游戏")
                        .setOngoing(true)
                        .setGroupSummary(false)
                        .setGroup("常驻")
                        .setContent(view_id_0_Game)//设置普通notification_id_0_Game视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action_id_0_Game=new Intent("Main_Game");
                view_id_0_Game.setOnClickPendingIntent(R.id.Normal_Game,PendingIntent.getBroadcast(MainActivity.this, 1, action_id_0_Game,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action2_id_0_Game=new Intent("Main_Reading");
                view_id_0_Game.setOnClickPendingIntent(R.id.Normal_Reading,PendingIntent.getBroadcast(MainActivity.this, 2, action2_id_0_Game,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action3_id_0_Game=new Intent("Main_Favourite");
                view_id_0_Game.setOnClickPendingIntent(R.id.Normal_Favourite,PendingIntent.getBroadcast(MainActivity.this, 3, action3_id_0_Game,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action4_id_0_Game=new Intent("Main_Relax");
                view_id_0_Game.setOnClickPendingIntent(R.id.Normal_Relax,PendingIntent.getBroadcast(MainActivity.this, 4, action4_id_0_Game,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action5_id_0_Game=new Intent("Main_AccessibilityService");
                view_id_0_Game.setOnClickPendingIntent(R.id.Normal_Icon,PendingIntent.getBroadcast(MainActivity.this, 5, action5_id_0_Game,PendingIntent.FLAG_UPDATE_CURRENT));


                NotificationManager manager_id_0_Game = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_0_Game != null) {
                    manager_id_0_Game.notify(0, notification_id_0_Game);
                }

                break;

            case Relax:

                NotificationChannel noti_id_1_Relax = new NotificationChannel("1",
                        "放松", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_1_Relax);
                RemoteViews view_id_1_Relax = new RemoteViews(getPackageName(), R.layout.normal_down);
                RemoteViews bigView_id_1_Relax = new RemoteViews(getPackageName(), R.layout.activity_relax);

                Notification notification_id_1_Relax = new NotificationCompat.Builder(this,"1")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("放松")
                        .setOngoing(false)
                        .setGroupSummary(false)
                        .setGroup("其它")
                        .setContent(view_id_1_Relax)//设置普通notification视图
                        .setCustomBigContentView(bigView_id_1_Relax)//设置显示bigView的notification视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action1_id_1_Relax=new Intent("Close");
                bigView_id_1_Relax.setOnClickPendingIntent(R.id.btnClose,PendingIntent.getBroadcast(MainActivity.this, 11, action1_id_1_Relax,PendingIntent.FLAG_UPDATE_CURRENT));


                NotificationManager manager_id_1_Relax = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_1_Relax != null) {
                    manager_id_1_Relax.notify(1, notification_id_1_Relax);
                }

                NotificationChannel noti_id_0_Relax = new NotificationChannel("0",
                        "游戏", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_0_Relax);
                RemoteViews view_id_0_Relax = new RemoteViews(getPackageName(), R.layout.normal_relax);

                if(Tag_onAccessibilityService) {
                    view_id_0_Relax.setImageViewResource(R.id.Normal_Icon, R.drawable.icon_click);
                }else{
                    view_id_0_Relax.setImageViewResource(R.id.Normal_Icon, R.drawable.icon);
                }

                Notification notification_id_0_Relax = new NotificationCompat.Builder(this,"0")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("游戏")
                        .setOngoing(true)
                        .setGroupSummary(false)
                        .setGroup("常驻")
                        .setContent(view_id_0_Relax)//设置普通notification_id_0_Relax视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action_id_0_Relax=new Intent("Main_Game");
                view_id_0_Relax.setOnClickPendingIntent(R.id.Normal_Game,PendingIntent.getBroadcast(MainActivity.this, 1, action_id_0_Relax,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action2_id_0_Relax=new Intent("Main_Reading");
                view_id_0_Relax.setOnClickPendingIntent(R.id.Normal_Reading,PendingIntent.getBroadcast(MainActivity.this, 2, action2_id_0_Relax,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action3_id_0_Relax=new Intent("Main_Favourite");
                view_id_0_Relax.setOnClickPendingIntent(R.id.Normal_Favourite,PendingIntent.getBroadcast(MainActivity.this, 3, action3_id_0_Relax,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action4_id_0_Relax=new Intent("Main_Relax");
                view_id_0_Relax.setOnClickPendingIntent(R.id.Normal_Relax,PendingIntent.getBroadcast(MainActivity.this, 4, action4_id_0_Relax,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action5_id_0_Relax=new Intent("Main_AccessibilityService");
                view_id_0_Relax.setOnClickPendingIntent(R.id.Normal_Icon,PendingIntent.getBroadcast(MainActivity.this, 5, action5_id_0_Relax,PendingIntent.FLAG_UPDATE_CURRENT));


                NotificationManager manager_id_0_Relax = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_0_Relax != null) {
                    manager_id_0_Relax.notify(0, notification_id_0_Relax);
                }
                break;

        }

    }

    public void like() {
        if (!Tag_like) {
            Tag_like = true;
            Toast.makeText(MainActivity.this, "你喜欢该内容", Toast.LENGTH_SHORT).show();
            if (!Tag_Isbigimg) {
                sendNotification(Image_with_Text);
            } else {
                Tag_Isbigimg = true;
                sendNotification(Big_Image);
            }
        } else {
            Tag_like = false;
            Toast.makeText(MainActivity.this, "你取消了喜欢该内容", Toast.LENGTH_SHORT).show();
            if (!Tag_Isbigimg) {
                sendNotification(Image_with_Text);
            } else {
                sendNotification(Big_Image);
            }
        }
    }
    
    public void Dislike(){
        if(!Tag_dislike) {
            Tag_dislike = true;
            Toast.makeText(MainActivity.this,"你不再想看到类似内容",Toast.LENGTH_SHORT).show();
            if (!Tag_Isbigimg) {
                sendNotification(Image_with_Text);
            }else{
                Tag_Isbigimg = true;
                sendNotification(Big_Image);
            }
        }else{
            Tag_dislike = false;
            Toast.makeText(MainActivity.this,"你想再看到类似内容",Toast.LENGTH_SHORT).show();
            if (!Tag_Isbigimg) {
                sendNotification(Image_with_Text);
            }else{
                sendNotification(Big_Image);
            }
        }

    }
//image next
    class MyBroadCast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            i++;
            if(i>5)
                i=0;
            sendNotification(Big_Image);
        }
    }
//image previous
    class MyBroadCast2 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            i--;
            if(i<0)
                i=5;
            sendNotification(Big_Image);
        }
    }
//like
    class MyBroadCast3 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
           like();
        }
    }
//dislike
    class MyBroadCast4 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            Dislike();
        }
    }
//text pic next
    class MyBroadCast5 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            j++;
            if(j==NewsNum)
                j=0;
            sp=getSharedPreferences("Setting", MODE_PRIVATE);
            SharedPreferences.Editor edit2 = sp.edit();
            edit2.putInt("NewsReadLocation",j);
            edit2.apply();
            NewsReadLocation=sp.getInt("NewsReadLocation",0);
            File file = new File(filePrefix+PicUrl+"/a"+NewsReadLocation+".jpg");
            if(file.exists()){
                sendNotification(Image_with_Text);
            }else{
                sendNotification(Only_Text);
            }
        }
    }
//text pic previous
    class MyBroadCast6 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            j--;
            if(j==-1)
                j=NewsNum-1;
            sp=getSharedPreferences("Setting", MODE_PRIVATE);
            SharedPreferences.Editor edit2 = sp.edit();
            edit2.putInt("NewsReadLocation",j);
            edit2.apply();
            NewsReadLocation=sp.getInt("NewsReadLocation",0);
            File file = new File(filePrefix+PicUrl+"/a"+NewsReadLocation+".jpg");
            if(file.exists()){
                sendNotification(Image_with_Text);
            }else{
                sendNotification(Only_Text);
            }
        }
    }

    class MyBroadCast_Main_Game extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            sendNotification(Game);
        }
    }

    class MyBroadCast_Main_Reading extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            sp=getSharedPreferences("Setting", MODE_PRIVATE);
            int temp=sp.getInt("NewsReadLocation",0);
            j=temp;
            File file = new File(filePrefix+PicUrl+"/a"+temp+".jpg");
            if(file.exists()){
                sendNotification(Image_with_Text);
            }else{
                sendNotification(Only_Text);
            }
        }
    }

    //广播-主界面
    class MyBroadCast_Main_Favourite extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            sendNotification(Favourite);
        }
    }
    class MyBroadCast_Main_Relax extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            sendNotification(Relax);
        }
    }
    class MyBroadCast_Main_AccessibilityService extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent)
        {
            sp = getSharedPreferences("Setting", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            if(Tag_onAccessibilityService) {
                Tag_onAccessibilityService = false;
                edit.putBoolean("onAccessibilityService", Tag_onAccessibilityService);
            }else {
                Tag_onAccessibilityService = true;
                edit.putBoolean("onAccessibilityService", Tag_onAccessibilityService);
            }
            edit.apply();
            sendNotification(Initial_Notification);
        }

    }
    class MyBroadCast7 extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent)
        {
            manger.cancelAll();
        }
    }

    //接收应用分享
    void dealTextMessage(Intent intent){
        String share = intent.getStringExtra(Intent.EXTRA_TEXT);
        String title = intent.getStringExtra(Intent.EXTRA_TITLE);
    }
    void dealPicStream(Intent intent){
        Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
    }
    void dealMultiplePicStream(Intent intent){
        ArrayList<Uri> arrayList = intent.getParcelableArrayListExtra(intent.EXTRA_STREAM);
    }

    //方法类
    public static void verifyStoragePermissions(Activity activity) {
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    public Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    Date getDateWithDateString(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateString);
        return date;
    }
    public List<String> ReadTxtFile(String strFilePath) {
        List<String> newList=new ArrayList<>();
        //打开文件
        File file = new File(strFilePath);
        //如果path是传递过来的参数，可以做一个非目录的判断
        try {
            InputStream instream = new FileInputStream(file);
            if (instream != null)
            {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while (( line = buffreader.readLine()) != null) {
                    newList.add(line+"\n");
                }
                instream.close();
            }
        }
        catch (java.io.FileNotFoundException e)
        {
            Log.d("TestFile", "The File doesn't not exist.");
        }
        catch (IOException e)
        {
            Log.d("TestFile", e.getMessage());
        }

        return newList;
    }
    private Fragment findFragment(String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        return fragment;
    }


    //工具类
    public void createFile() {
        File file = new File(filePrefix + RootUrl);
        if (!file.exists()) {
            file.mkdir();
            file = new File(filePrefix + NewsTitleUrl);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = new File(filePrefix + NewsSubTitleUrl);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = new File(filePrefix + NewsContentUrl);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = new File(filePrefix + PicUrl);
            file.mkdir();
        }
    }
    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
    class TimeRunnable extends Thread{
        @Override
        public void run() {
            super.run();
            while(!isFinish) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            }
        }
    };


    public void checkUpdateData() throws ParseException {
        sp=getSharedPreferences("Time", MODE_PRIVATE);
        int time=sp.getInt("UpdateTime",1);
        Calendar calendar = Calendar.getInstance();
        now = calendar.get(Calendar.DAY_OF_YEAR);
        if(time<now || alwaysUpdateData){
            UpdateText=findViewById(R.id.notification_progressbar_text);
            mTimeRunnable.start();
            manger.cancelAll();
            File dir = new File(filePrefix+RootUrl);
            deleteDirWihtFile(dir);
            createFile();
            saveData();
            updateNoti();
        }
    }

    public void updateNoti(){
        NotificationChannel noti_id_saveData = new NotificationChannel("0",
                "下载数据", NotificationManager.IMPORTANCE_LOW);
        manger.createNotificationChannel(noti_id_saveData);
        RemoteViews view_id_0_saveData = new RemoteViews(getPackageName(), R.layout.notification_progressbar);
        view_id_0_saveData.setProgressBar(R.id.notification_progressbar_progressbar,100,saveData_Progress,false);
        view_id_0_saveData.setTextViewText(R.id.notification_progressbar_text,"正在更新每日内容，请稍后...(" + updateText+"%）");
        Notification notification_id_0_saveData = new NotificationCompat.Builder(this,"0")
                .setSmallIcon(R.drawable.icon)
                .setTicker("下载数据")
                .setOngoing(true)
                .setGroupSummary(false)
                .setGroup("其它")
                .setContent(view_id_0_saveData)//设置普通notification视图
                .setPriority(NotificationCompat.PRIORITY_LOW)//设置最大优先级
                .build();

        NotificationManager manager_id_0_saveData = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager_id_0_saveData != null) {
            manager_id_0_saveData.notify(0, notification_id_0_saveData);
        }
    }

    public void saveData() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        String time=simpleDateFormat.format(date);

        AVQuery<AVObject> query = new AVQuery<>("PengpaiNews");
        query.whereGreaterThanOrEqualTo("createdAt", getDateWithDateString(time));
        query.addAscendingOrder("priority");
        query.selectKeys(Arrays.asList("title", "subtitle","cutcontent","bitmapbytes"));
        query.limit(10);
        query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    for (AVObject avObject : list) {

                        NewsTitle.add(saveData_i , avObject.getString("title"));
                        NewsSubTitle.add(saveData_i, avObject.getString("subtitle"));
                        CutContent.add(saveData_i, avObject.getString("cutcontent"));
                        BitmapBytes.add(saveData_i, avObject.getString("bitmapbytes"));

                        saveData_Progress+=10;
                        updateText+=10;

                        Bitmap bitmapByte=stringToBitmap(BitmapBytes.get(saveData_i));
                        File file = new File(filePrefix + "/MoDengXian/pic/a" + saveData_i + ".jpg");//将要保存图片的路径
                        if (bitmapByte != null) {
                            try {
                                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                                bitmapByte.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                bos.flush();
                                bos.close();
                            } catch (IOException es) {
                                es.printStackTrace();
                            }
                        }
                        saveData_i++;
                        if (saveData_i == 10) {
                            saveData2();
                            break;
                        }
                    }
                }
            });
    }

    public void saveData2(){
        Writer r1,r3,r4 = null;
        try {
            r1 = new FileWriter(filePrefix + NewsTitleUrl, true);
            r3 = new FileWriter(filePrefix + NewsSubTitleUrl, true);
            r4 = new FileWriter(filePrefix + NewsContentUrl, true);
            for (String aNewsTitle : NewsTitle) {
                r1.write(aNewsTitle + "\n");
            }
            r1.close();
            for (String aNewsSubTitle : NewsSubTitle) {
                r3.write(aNewsSubTitle + "\n");
            }
            r3.close();
            for (String aNewsContent : CutContent) {
                r4.write(aNewsContent + "\n");
            }
            r4.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        sp=getSharedPreferences("Time", MODE_PRIVATE);
        SharedPreferences.Editor edit3 = sp.edit();
        edit3.putInt("UpdateTime", now);
        edit3.apply();
        reloadData();
    }

    public void reloadData(){
        NewsTitle.clear();
        NewsSubTitle.clear();
        CutContent.clear();
        for(int temp=0; temp<NewsNum; temp++){
            NewsTitle.addAll(ReadTxtFile(filePrefix+NewsTitleUrl));
            NewsSubTitle.addAll(ReadTxtFile(filePrefix+NewsSubTitleUrl));
            CutContent.addAll(ReadTxtFile(filePrefix+NewsContentUrl));
        }

    }

    public void test(){
        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        /* 判断剪切版时候有内容 */
        if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
            ClipData clipData = clipboardManager.getPrimaryClip();
            url= clipData.getItemAt(0).getText().toString();
        }

    }

    @Override
    protected void onDestroy() {
        if (null != sonicSession) {
            sonicSession.destroy();
            sonicSession = null;
        }
        super.onDestroy();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(saveData_Progress==100){
                        manger.cancelAll();
                        mTimeRunnable.interrupt();
                        isFinish=true;
                        sendNotification(Initial_Notification);
                    }else {
                        updateNoti();
                    }
                    break;
            }
        }
    };
}



