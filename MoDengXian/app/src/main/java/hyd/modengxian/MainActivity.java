package hyd.modengxian;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;




public class MainActivity extends Activity {

    private NotificationManager manger;

    private static final int Big_Image = 1000;
    private static final int Image_with_Text = 1001;
    private static final int Favourite = 1002;
    private static final int Game = 1003;
    private static final int Relax = 1004;
    private static final int MyAccessibilityService = 1005;



    private String[] TxtTitleList;
    private String[] TxtSubtitleList;
    private String[] TxtContentList;
    private String[] ImgTitleList;
    private String[] ImgSubtitleList;
    private String[] VocTitleList;
    private String[] VocContentList;
    private String[] VocYinbiaoList;
    private String[] VocMeanList;

    private int[] TxtImg={R.drawable.a0,R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,R.drawable.a5};
    private int[] ImgImg={R.drawable.b0,R.drawable.b1,R.drawable.b2,R.drawable.b3,R.drawable.b4,R.drawable.b5,R.drawable.b6};

//i：大图  j：图文  k：
    int i=0,j=0,k=0;
    Boolean Tag_like = false;
    Boolean Tag_dislike = false;
    Boolean Tag_Isbigimg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//id: 常驻通知栏（0）   一级通知（1）

        init();
        broadcast_init();


    }


    public void init(){

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

//常驻通知栏消息
        NotificationChannel noti_id_0 = new NotificationChannel("0",
                "常驻通知栏", NotificationManager.IMPORTANCE_HIGH);
        manger.createNotificationChannel(noti_id_0);
        RemoteViews view_id_0 = new RemoteViews(getPackageName(), R.layout.normal);
        Notification notification_id_0 = new NotificationCompat.Builder(this,"0")
                .setSmallIcon(R.drawable.icon)
                .setTicker("常驻通知栏")
                .setOngoing(true)
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

        NotificationManager manager_id_0 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager_id_0 != null) {
            manager_id_0.notify(0, notification_id_0);
        }


        if (!OpenAccessibilitySettingHelper.isAccessibilitySettingsOn(this,
                MyAccessibilityService.class.getName())){// 判断服务是否开启
            OpenAccessibilitySettingHelper.jumpToSettingPage(this);// 跳转到开启页面
        }else {
            Toast.makeText(this, "服务已开启", Toast.LENGTH_SHORT).show();
        }

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

    }

    public void sendNotification(int id) {
        if(id == Big_Image)
            Tag_Isbigimg = true;
        else
            Tag_Isbigimg = false;

        switch (id) {
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
                bigView_id_1_Big_Image.setImageViewResource(R.id.Image_Img,ImgImg[i]);
                RemoteViews view_id_1_Big_Image = new RemoteViews(getPackageName(), R.layout.normal_down);
                Notification notification_id_1_Big_Image = new NotificationCompat.Builder(this,"1")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("大图")
                        .setOngoing(false)
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

                NotificationManager manager_id_1_Big_Image = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_1_Big_Image != null) {
                    manager_id_1_Big_Image.notify(1, notification_id_1_Big_Image);
                }

                NotificationChannel noti_id_0_Big_Image = new NotificationChannel("0",
                        "大图", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_0_Big_Image);
                RemoteViews view_id_0_Big_Image = new RemoteViews(getPackageName(), R.layout.normal_reading);
                Notification notification_id_0_Big_Image = new NotificationCompat.Builder(this,"0")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("大图")
                        .setOngoing(true)
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

                bigView_id_1_Image_with_Text.setTextViewText(R.id.Text_Title, TxtTitleList[j]);
                bigView_id_1_Image_with_Text.setTextViewText(R.id.Text_SubTitle, TxtSubtitleList[j]);
                bigView_id_1_Image_with_Text.setTextViewText(R.id.Text_Content, TxtContentList[j]);
                bigView_id_1_Image_with_Text.setImageViewResource(R.id.Text_Img,TxtImg[j]);
                Notification notification_id_1_Image_with_Text = new NotificationCompat.Builder(this,"1")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("图文")
                        .setOngoing(false)
                        .setContent(view_id_1_Image_with_Text)//设置普通notification视图
                        .setCustomBigContentView(bigView_id_1_Image_with_Text)//设置显示bigView的notification视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                Intent action_id_1_Image_with_Text=new Intent("Image_with_Text_Next");
                bigView_id_1_Image_with_Text.setOnClickPendingIntent(R.id.btnNext,PendingIntent.getBroadcast(MainActivity.this, 15, action_id_1_Image_with_Text,PendingIntent.FLAG_UPDATE_CURRENT));

                Intent action2_id_1_Image_with_Text=new Intent("Image_with_Text_Previous");
                bigView_id_1_Image_with_Text.setOnClickPendingIntent(R.id.btnPrevious,PendingIntent.getBroadcast(MainActivity.this, 16, action2_id_1_Image_with_Text,PendingIntent.FLAG_UPDATE_CURRENT));
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
                Notification notification_id_0_Image_with_Text = new NotificationCompat.Builder(this,"0")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("图文")
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

            case Favourite:

                NotificationChannel noti_id_1_Favourite = new NotificationChannel("1",
                        "收藏", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_1_Favourite);
                RemoteViews view_id_1_Favourite = new RemoteViews(getPackageName(), R.layout.normal_down);
                RemoteViews bigView_id_1_Favourite = new RemoteViews(getPackageName(), R.layout.activity_favourite);

                Notification notification_id_1_Favourite = new NotificationCompat.Builder(this,"1")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("收藏")
                        .setOngoing(false)
                        .setContent(view_id_1_Favourite)//设置普通notification视图
                        .setCustomBigContentView(bigView_id_1_Favourite)//设置显示bigView的notification视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                NotificationManager manager_id_1_Favourite = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_1_Favourite != null) {
                    manager_id_1_Favourite.notify(1, notification_id_1_Favourite);
                }

                NotificationChannel noti_id_0_Favourite = new NotificationChannel("0",
                        "收藏", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_0_Favourite);
                RemoteViews view_id_0_Favourite = new RemoteViews(getPackageName(), R.layout.normal_favourite);
                Notification notification_id_0_Favourite = new NotificationCompat.Builder(this,"0")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("收藏")
                        .setOngoing(true)
                        .setContent(view_id_0_Favourite)//设置普通notification_id_0_Image_with_Text视图
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
                        .setOngoing(false)
                        .setContent(view_id_1_Game)//设置普通notification视图
                        .setCustomBigContentView(bigView_id_1_Game)//设置显示bigView的notification视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                NotificationManager manager_id_1_Game = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_1_Game != null) {
                    manager_id_1_Game.notify(1, notification_id_1_Game);
                }

                NotificationChannel noti_id_0_Game = new NotificationChannel("0",
                        "游戏", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_0_Game);
                RemoteViews view_id_0_Game = new RemoteViews(getPackageName(), R.layout.normal_game);
                Notification notification_id_0_Game = new NotificationCompat.Builder(this,"0")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("游戏")
                        .setOngoing(true)
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
                        .setContent(view_id_1_Relax)//设置普通notification视图
                        .setCustomBigContentView(bigView_id_1_Relax)//设置显示bigView的notification视图
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                        .build();

                NotificationManager manager_id_1_Relax = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_1_Relax != null) {
                    manager_id_1_Relax.notify(1, notification_id_1_Relax);
                }

                NotificationChannel noti_id_0_Relax = new NotificationChannel("0",
                        "游戏", NotificationManager.IMPORTANCE_HIGH);
                manger.createNotificationChannel(noti_id_0_Relax);
                RemoteViews view_id_0_Relax = new RemoteViews(getPackageName(), R.layout.normal_relax);
                Notification notification_id_0_Relax = new NotificationCompat.Builder(this,"0")
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("游戏")
                        .setOngoing(true)
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


                NotificationManager manager_id_0_Relax = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (manager_id_0_Relax != null) {
                    manager_id_0_Relax.notify(0, notification_id_0_Relax);
                }
                break;

            case MyAccessibilityService:
                Intent intent = new Intent(this, MyAccessibilityService.class);
                startService(intent);

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

    class MyBroadCast3 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
           like();
        }
    }

    class MyBroadCast4 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            Dislike();
        }
    }

    class MyBroadCast5 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            j++;
            if(j>5)
                j=0;
            sendNotification(Image_with_Text);
        }
    }

    class MyBroadCast6 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            j--;
            if(j<0)
                j=5;
            sendNotification(Image_with_Text);
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
            sendNotification(Image_with_Text);
        }
    }

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
            sendNotification(MyAccessibilityService);
        }
    }


}



