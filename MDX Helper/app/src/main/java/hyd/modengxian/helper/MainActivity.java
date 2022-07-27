package hyd.modengxian.helper;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.RelativeDateTimeFormatter;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.scrat.app.richtext.RichEditText;
import com.sun.tools.xjc.Main;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    public int CurrentVersionCode=3;

    private FloatingActionButton  fab, actionButton1, actionButton2, actionButton3;
    private boolean menuOpen = false;
    private int DISTANCE=180;
    private int DISTANCE2=125;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    List<String> NewsTitle=new ArrayList<>();
    List<String> NewsSubTitle=new ArrayList<>();
    List<String> NewsContent=new ArrayList<>();
    List<String> ConUrl=new ArrayList<>();
    static List<String> PicUrl=new ArrayList<>();
    List<String> CutContent=new ArrayList<>();
    List<String> BitmapBytes=new ArrayList<>();
    List<String> HotSearch_Baidu=new ArrayList<>();
    List<String> HotSearch_Weibo=new ArrayList<>();
    List<String> HotSearch_Baidu_Url=new ArrayList<>();
    List<String> HotSearch_Weibo_Url=new ArrayList<>();

    public int flag;
    String userID=null;
    private SharedPreferences sp ;
    public ProgressBar progressbar;
    public CheckBox c1;
    public CheckBox c2;
    public CheckBox c3;
    public static CheckBox c4;
    public CheckBox c5;
    public CheckBox c6;
    public CheckBox c7;
    public CheckBox c8;
    public CheckBox isActive;

    public RadioButton r1,r2,r3,r4;
    public String[] NewsSource={"listhot0","listhot1","listhot2"};
    public String NewsSourceS;

    public int PreProgress=0;
    public static int MaxProgress=0;
    public boolean isFinish=false;

    private static Bitmap mBitmap;
    private final static String TAG = "MainActivity";
    private static Context context;

    public int PrePosition=0;
    public int IndexPosition=1;
    public View view;
    public String id;

    public Dialog dialog_check;
    public TextView text_updateTime,text_updateId,text_updateCatelogue,text_Version;
    private long recordingTime = 0;
    public Chronometer ch;
    public String objectID;

    public String name,level;
    public int contribute,credit,achivement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


    }

    public void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        verifyStoragePermissions(MainActivity.this);
        AVOSCloud.initialize(this,"xHiee3l7EnDWTMU2nQXlPdoM-gzGzoHsz","TIYGEvp1RXu3H86O6j55ezx6");
        AVOSCloud.setDebugLogEnabled(true);

        actionButton1=findViewById(R.id.float_btn1);
        actionButton2=findViewById(R.id.float_btn2);
        actionButton3=findViewById(R.id.float_btn3);
        actionButton1.hide();
        actionButton2.hide();
        actionButton3.hide();

        progressbar=findViewById(R.id.progressBar);
        c1=findViewById(R.id.checkBox);
        c2=findViewById(R.id.checkBox2);
        c3=findViewById(R.id.checkBox3);
        c4=findViewById(R.id.checkBoxx);
        c5=findViewById(R.id.checkBox4);
        c6=findViewById(R.id.checkBox5);
        c7=findViewById(R.id.checkBox6);
        c8=findViewById(R.id.checkBox7);

        text_updateTime=findViewById(R.id.textView_updateTime_Hour);
        text_updateCatelogue=findViewById(R.id.textView_Update_Catelogue);
        text_updateId=findViewById(R.id.textView_updateId);
        text_Version=findViewById(R.id.textView_Version);

        view = getLayoutInflater().inflate(R.layout.dialog, null);

        c1.setOnCheckedChangeListener(new CheckBoxListener());
        c2.setOnCheckedChangeListener(new CheckBoxListener());
        c3.setOnCheckedChangeListener(new CheckBoxListener());
        c4.setOnCheckedChangeListener(new CheckBoxListener());
        c5.setOnCheckedChangeListener(new CheckBoxListener());
        c6.setOnCheckedChangeListener(new CheckBoxListener());
        c7.setOnCheckedChangeListener(new CheckBoxListener());
        c8.setOnCheckedChangeListener(new CheckBoxListener());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuOpen == false) {
                    actionButton1.show();
                    actionButton2.show();
                    actionButton3.show();
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                    menuOpen = true;
                    int x = (int) fab.getX();
                    int y = (int) fab.getY();
                    ValueAnimator v1 = ValueAnimator.ofInt(x, x - DISTANCE);
                    v1.setDuration(200);
                    v1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int l = (int) animation.getAnimatedValue();
                            int t = (int) actionButton1.getY();
                            int r = actionButton1.getWidth() + l;
                            int b = actionButton1.getHeight() + t;
                            actionButton1.layout(l, t, r, b);
                        }
                    });
                    ValueAnimator v2x = ValueAnimator.ofInt(x, x - DISTANCE2);
                    ValueAnimator v2y = ValueAnimator.ofInt(y, y - DISTANCE2);
                    v2x.setDuration(200).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int l = (int) animation.getAnimatedValue();
                            int t = (int) actionButton2.getY();
                            int r = actionButton2.getWidth() + l;
                            int b = actionButton2.getHeight() + t;
                            actionButton2.layout(l, t, r, b);
                        }
                    });
                    v2y.setDuration(200).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int t = (int) animation.getAnimatedValue();
                            int l = (int) actionButton2.getX();
                            int r = actionButton2.getWidth() + l;
                            int b = actionButton2.getHeight() + t;
                            actionButton2.layout(l, t, r, b);
                        }
                    });
                    ValueAnimator v3 = ValueAnimator.ofInt(y, y - DISTANCE);
                    v3.setDuration(200).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int t = (int) animation.getAnimatedValue();
                            int l = (int) actionButton3.getX();
                            int r = actionButton3.getWidth() + l;
                            int b = actionButton3.getHeight() + t;
                            actionButton3.layout(l, t, r, b);
                        }
                    });
                    v1.start();
                    v2x.start();
                    v2y.start();
                    v3.start();

                    fab.setImageResource(R.drawable.close);
                } else {
                    menuOpen = false;
                    int x = (int) actionButton1.getX();
                    ValueAnimator v1 = ValueAnimator.ofInt(x, (int) fab.getX());
                    v1.setDuration(200);
                    v1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int l = (int) animation.getAnimatedValue();
                            int t = (int) actionButton1.getY();
                            int r = actionButton1.getWidth() + l;
                            int b = actionButton1.getHeight() + t;
                            actionButton1.layout(l, t, r, b);
                        }
                    });
                    x = (int) actionButton2.getX();
                    int y = (int) actionButton2.getY();
                    ValueAnimator v2x = ValueAnimator.ofInt(x, (int) fab.getX());
                    ValueAnimator v2y = ValueAnimator.ofInt(y, (int) fab.getY());
                    v2x.setDuration(200).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int l = (int) animation.getAnimatedValue();
                            int t = (int) actionButton2.getY();
                            int r = actionButton2.getWidth() + l;
                            int b = actionButton2.getHeight() + t;
                            actionButton2.layout(l, t, r, b);
                        }
                    });
                    v2y.setDuration(200).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int t = (int) animation.getAnimatedValue();
                            int l = (int) actionButton2.getX();
                            int r = actionButton2.getWidth() + l;
                            int b = actionButton2.getHeight() + t;
                            actionButton2.layout(l, t, r, b);
                        }
                    });
                    y = (int) actionButton3.getY();
                    ValueAnimator v3 = ValueAnimator.ofInt(y, (int) fab.getY());
                    v3.setDuration(200).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int t = (int) animation.getAnimatedValue();
                            int l = (int) actionButton3.getX();
                            int r = actionButton3.getWidth() + l;
                            int b = actionButton3.getHeight() + t;
                            actionButton3.layout(l, t, r, b);
                        }
                    });
                    v1.start();
                    v2x.start();
                    v2y.start();
                    v3.start();
                    actionButton1.hide();
                    actionButton2.hide();
                    actionButton3.hide();
                    fab.setImageResource(R.drawable.more);
                }
            }
        });


        actionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ch=(Chronometer)findViewById(R.id.chronometer);
                ch.setBase(SystemClock.elapsedRealtime());
                ch.start();


                File dir = new File(Environment.getExternalStorageDirectory() + "/mdx");
                deleteDirWihtFile(dir);
                Thread mTimeRunnable = new TimeRunnable();
                mTimeRunnable.start();
                Thread mTimeRunnable2 = new TimeRunnable2();
                mTimeRunnable2.start();
                createFile();
                getData();
            }
        });
        actionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                am.restartPackage("hyd.modengxian.helper");
            }
        });

        actionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            View view2 = getLayoutInflater().inflate(R.layout.dialog_catelogue, null);
            final EditText editText2 = view2.findViewById(R.id.dialog_input);

                r1=view2.findViewById(R.id.radioButton);
                r2=view2.findViewById(R.id.radioButton2);
                r3=view2.findViewById(R.id.radioButton3);

                r1.setOnClickListener(new RadioButtonListener());
                r2.setOnClickListener(new RadioButtonListener());
                r3.setOnClickListener(new RadioButtonListener());

                sp=getSharedPreferences("Setting", MODE_PRIVATE);
                String newssource=sp.getString("newsSource","");
                if(newssource.equals("listhot0")){
                    r1.setChecked(true);
                }else if(newssource.equals("listhot1")){
                    r2.setChecked(true);
                }else{
                    r3.setChecked(true);
                }

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setIcon(R.drawable.catelogue2)//设置标题的图片
                .setTitle("选择新闻源")//设置对话框的标题
                .setView(view2)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sp=getSharedPreferences("Setting", MODE_PRIVATE);
                        SharedPreferences.Editor edit3 = sp.edit();
                        edit3.putString("newsSource", NewsSourceS);
                        edit3.apply();
                        String content = editText2.getText().toString();
                        if(!content.equals("") && content.length()!=0){
                            AVObject uploader = new AVObject("NewsSourceSuggestion");
                            uploader.put("userID", id);
                            uploader.put("Content", content);
                            uploader.saveInBackground();
                        }
                        dialog.dismiss();
                    }
                }).create();
            dialog.show();
            }
        });

        text_Version.setText((R.string.versioncode));
        AVQuery<AVObject> query = new AVQuery<>("LatestUpdate");
        query.whereEqualTo("valid", true);
        query.orderByDescending("createdAt");
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                text_updateId.setText(avObject.getString("id"));
                text_updateCatelogue.setText(avObject.getString("catelogue"));
                text_updateTime.setText(avObject.getString("time"));
            }
        });

        AVQuery<AVObject> query2 = new AVQuery<>("Versioncode");
        query2.whereEqualTo("valid", true);
        query2.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject2, AVException e) {
                int code=Integer.parseInt(avObject2.getString("versioncode"));
                if(code>CurrentVersionCode){
                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setIcon(R.drawable.update)//设置标题的图片
                            .setTitle("新版本")//设置对话框的标题
                            .setMessage("当前不允许应用内更新。\n请前至群下载最新版本。")//设置对话框的内容
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }
            }
        });


        sp=getSharedPreferences("Setting", MODE_PRIVATE);
        id=sp.getString("userID","");
        objectID=sp.getString("objectID","");
        if(id.equals("")|| id.length()==0) {

            final EditText inputServer = new EditText(this);
            inputServer.setHint("为自己取个名字吧~");
            inputServer.setTextSize(16);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("身份确认").setIcon(R.drawable.attention).setView(inputServer);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    userID = inputServer.getText().toString();
                    id=userID;
                    AVQuery<AVObject> query = new AVQuery<>("Person");
                    query.whereStartsWith("userID", userID);
                    query.getFirstInBackground(new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            // object 就是符合条件的第一个 AVObject
                            //(!avObject.equals("null")){

                            // }
                        }
                    });

                    AVObject uploader = new AVObject("Person");
                    uploader.put("userID", userID);
                    uploader.put("name", "");
                    uploader.put("authority", 1);
                    uploader.saveInBackground();

                    AVQuery<AVObject> query2 = new AVQuery<>("Person");
                    query2.whereEqualTo("userID", id);
                    query2.getFirstInBackground(new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            objectID=avObject.getObjectId();
                            sp=getSharedPreferences("Setting", MODE_PRIVATE);
                            SharedPreferences.Editor edit2 = sp.edit();
                            edit2.putString("userID", userID);
                            edit2.putString("objectID",objectID);
                            edit2.apply();
                        }
                    });
                }
            });
            builder.show();

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.info)//设置标题的图片
                    .setTitle("莫等闲助手·说明")//设置对话框的标题
                    .setMessage("这是一款莫等闲的辅助工具，目前实现的功能为<获取新闻-轻量编辑新闻-上传新闻到服务器>，这样在莫等闲中就可以阅读到每日最新动态啦。\n\n" +
                            "在编辑新闻界面，若图片为蓝色图像，则表明该则新闻没有图片。你所做的更改为最终上传的内容。\n\n" +
                            "当然，这样一款小程序一定有彩蛋啦~自己探索吧~\n\n" +
                            "希望各位使用愉快！")//设置对话框的内容
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.s_info:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setIcon(R.drawable.info)//设置标题的图片
                        .setTitle("莫等闲助手·说明")//设置对话框的标题
                        .setMessage("这是一款莫等闲的辅助工具，目前实现的功能为<获取新闻-轻量编辑新闻-上传新闻到服务器>，这样在莫等闲中就可以阅读到每日最新动态啦。\n\n" +
                                "在编辑新闻界面，若图片为蓝色图像，则表明该则新闻没有图片。你所做的更改为最终上传的内容。\n\n" +
                                "当然，这样一款小程序一定有彩蛋啦~自己探索吧~\n\n" +
                                "希望各位使用愉快！")//设置对话框的内容
                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                break;
            case R.id.s_me:
                Intent starter = new Intent(MainActivity.this, UserInfo.class);
                MainActivity.this.startActivity(starter);
                finish();
                break;
            case R.id.s_reset:
                sp=getSharedPreferences("Setting", MODE_PRIVATE);
                SharedPreferences.Editor edit3 = sp.edit();
                edit3.putString("userID", "");
                edit3.apply();
                File dir = new File(Environment.getExternalStorageDirectory() + "/mdx");
                deleteDirWihtFile(dir);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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

    public void createFile() {
        File file = new File(Environment.getExternalStorageDirectory() + "/mdx");
        if (!file.exists()) {
            file.mkdir();
            file = new File(Environment.getExternalStorageDirectory() + "/mdx/NewsTitle.txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = new File(Environment.getExternalStorageDirectory() + "/mdx/NewsSubTiTle.txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = new File(Environment.getExternalStorageDirectory() + "/mdx/NewsUrl.txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = new File(Environment.getExternalStorageDirectory() + "/mdx/PicUrl.txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = new File(Environment.getExternalStorageDirectory() + "/mdx/NewsContent.txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = new File(Environment.getExternalStorageDirectory() + "/mdx/CutContent.txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = new File(Environment.getExternalStorageDirectory() + "/mdx/pic");
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

    public void getData(){

            // 开启一个新线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MaxProgress=13;
                        c1.setChecked(true);
                        c1.setTextColor(Color.parseColor("#9E9E9E"));
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        sp=getSharedPreferences("Setting", MODE_PRIVATE);
                        NewsSourceS=sp.getString("newsSource","listhot0");

                        Document doc = Jsoup.connect("https://www.thepaper.cn/")
                                .timeout(3000) // 设置超时时间
                                .get(); // 使用GET方法访问URL
                        Element element = doc.getElementById(NewsSourceS);
                        NewsTitle=element.select("a").eachText();
                        Elements links = element.select("a");
                        links.eachText();
                        ConUrl=links.eachAttr("abs:href");

                        Calendar calendar = Calendar.getInstance();
                        int month = calendar.get(Calendar.MONTH)+1;
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        for(int j=0; j<NewsTitle.size(); j++) {
                            Document docx = Jsoup.connect(ConUrl.get(j))
                                    .timeout(3000) // 设置超时时间
                                    .get(); // 使用GET方法访问URL
                            String Subtitle="";
                            try{
                                Element elementx = docx.select("div.news_about").select("p").first();
                                Subtitle = elementx.text();
                                String[] split=null;
                                split = Subtitle.split(" ");
                                Subtitle=split[0];
                                Subtitle=Subtitle.replace("记者","");
                                Subtitle = Subtitle + " " + month + "月" + day + "日";
                            }catch (Exception e){
                                Subtitle = month + "月" + day + "日";
                            }
                            NewsSubTitle.add(Subtitle);

                            Element element2 = docx.select("div.news_txt").select("img").first();
                            String Picurl;
                            if (!(element2 ==null)) {
                                Picurl = docx.select("div.news_txt").select("img").first().attr("src");
                            } else {
                                Picurl = docx.select("div.ctread_img").select("a").select("img").attr("abs:src");
                            }
                            PicUrl.add(Picurl);
                            String content;
                            try {
                                content = docx.select("div.news_txt").first().text();
                            }catch (Exception e){
                                content="暂不支持播放视频 哦";
                            }
                            NewsContent.add(content);
                        }

                        Document doc2 = Jsoup.connect("https://s.weibo.com/top/summary?cate=total&key=person")
                                .timeout(3000) // 设置超时时间
                                .get(); // 使用GET方法访问URL
                        Elements element2 = doc2.getElementsByClass("td-02");
                        HotSearch_Weibo=element2.select("a").eachText();
                        Elements links2 = element2.select("a");
                        for(Element url:links2){
                            if(url.hasAttr("href_to"))
                                HotSearch_Weibo_Url.add(url.attr("href_to"));
                            else
                                HotSearch_Weibo_Url.add(url.attr("href"));
                        }
                        Document doc3 = Jsoup.connect("https://www.baidu.com/s?wd=%E8%83%A1%E4%BA%9A%E4%B8%9C&rsv_spt=1&rsv_iqid=0xe08521db00040b92&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&rqlang=cn&tn=baiduhome_pg&rsv_enter=1&oq=%25E5%25BE%2580%25E5%258F%25B3&inputT=1373&rsv_t=3f08T4mO377VVSy1YO3XFvvziV6Dulyx1cQhlYq1nV6n%2BVwbAb8Q9j0a%2F8X8NUs6vG5j&rsv_pq=dd4c973700044361&rsv_sug3=14&rsv_sug1=12&rsv_sug7=100&bs=%E5%BE%80%E5%8F%B3")
                                .timeout(3000) // 设置超时时间
                                .get(); // 使用GET方法访问URL
                        Elements element3 = doc3.getElementsByClass("c-table opr-toplist1-table");
                        HotSearch_Baidu=element3.select("a").eachText();
                        Elements links3 = element3.select("a");
                        HotSearch_Baidu_Url=links3.eachAttr("abs:href");

                        MaxProgress=25;
                        c2.setChecked(true);
                        c2.setTextColor(Color.parseColor("#9E9E9E"));

                        mHandler.sendEmptyMessageDelayed(1,1600);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();



        }

    public void cutContent(){
        String temp="",tempFirst="",tempLast="";
        String[] split=null;
        for(int k=0; k<NewsTitle.size(); k++) {
            temp="";
            tempFirst="";
            tempLast="";
            split=null;
            temp = NewsContent.get(k);
            temp=temp.replace("“","");
            temp=temp.replace("”","");
            temp=temp.replace("2018年","18年");
            temp=temp.replace("2019年","19年");
            temp=temp.replace("了","");
            temp=temp.replace("的","");
            temp=temp.replace("【编者按】","");
            temp=temp.replace("“","");
            temp=temp.replace("”","");
            temp=temp.replace(",","，");
            temp=temp.replace(".","。");
            temp=temp.replace("资料图","");
            temp=temp.replace("原题为","");
            temp=temp.replace("，","， ");
            temp=temp.replace("。","。 ");
            temp=temp.replaceAll("（[^（^）]*）", "");//正则剔除括号内内容
            temp=temp.replaceAll("\\([^(^)]*\\)", "");
            split = temp.split(" ");
            int m=0;
            for (m = 0; tempFirst.length() < 96; m++) {
                if(m==split.length && m!=0)
                    break;
                else {
                    tempFirst = tempFirst + split[m];
                    if (tempFirst.length()>96) {
                        tempFirst=tempFirst.replace(split[m],"");
                    }
                }
            }
            int n = 0;
            for (n = 0; tempLast.length() < 65; n++) {
                if(split.length - 1 - n<0)
                    break;
                else {
                    tempLast = split[split.length - 1 - n] + tempLast;
                    if (tempLast.length()>65){
                        n--;
                    }
                }
            }
            tempLast="";
            for (int p = 0; p <= n; p++) {
                if(split.length - 1 - n<0)
                    break;
                else
                    tempLast = tempLast+split[split.length - 1 - n + p];
            }
            if(tempFirst.endsWith("。") && tempLast.endsWith("。"))
                CutContent.add(tempFirst + "hyd" + tempLast);
            else if(tempFirst.endsWith("。"))
                CutContent.add(tempFirst + "hyd" + tempLast+"。");
            else if(tempLast.endsWith("。"))
                CutContent.add(tempFirst + "。hyd" + tempLast);
            else
                CutContent.add(tempFirst + "。hyd" + tempLast+"。");

            if(CutContent.get(k).equals("。hyd。"))
                CutContent.set(k,"暂不支持播放视频哦。hyd。");
            CutContent.set(k,CutContent.get(k).replace("。hyd。","。hyd此文就这么短咯~"));
            CutContent.set(k,CutContent.get(k).replace("……。","……"));
            CutContent.set(k,CutContent.get(k).replace("！。","！"));
            CutContent.set(k,CutContent.get(k).replace("？。","？"));
        }
        mHandler.sendEmptyMessage(2);
    }

    public void downloadPic(){
        new Thread(saveFileRunnable).start();
    }

    class TimeRunnable extends Thread{
        @Override
        public void run() {
            super.run();
            while(!isFinish) {
                try {
                    Thread.sleep(65);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            }
        }
    };

    class TimeRunnable2 extends Thread{
        @Override
        public void run() {
            super.run();
            while(!isFinish) {
                try {
                    Thread.sleep(1600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(3);
            }
        }
    };

    private class CheckBoxListener implements CheckBox.OnCheckedChangeListener{
        @SuppressLint("ResourceAsColor")
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch(buttonView.getId()) {
                case R.id.checkBox:
                    isActive=c2;
                    break;
                case R.id.checkBox2:
                    isActive=c3;
                    break;
                case R.id.checkBox3:
                    isActive=c4;
                    break;
                case R.id.checkBoxx:
                    isActive=c5;
                    break;
                case R.id.checkBox4:
                    isActive=c6;
                    break;
                case R.id.checkBox5:
                    isActive=c7;
                    break;
                case R.id.checkBox6:
                    isActive=c8;
                    break;
                case R.id.checkBox7:

                    break;

            }
            isActive.setTextColor(Color.parseColor("#D81B60"));
        }
    }

    private class RadioButtonListener implements RadioButton.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.radioButton:
                    NewsSourceS=NewsSource[0];
                    break;
                case R.id.radioButton2:
                    NewsSourceS=NewsSource[1];
                    break;
                case R.id.radioButton3:
                    NewsSourceS=NewsSource[2];
                    break;
            }
        }
    }

    public static InputStream getImageStream(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            return conn.getInputStream();
        }
        return null;
    }

    public static void saveFile(Bitmap bm, String fileName) throws IOException {
        File myCaptureFile = new File(Environment.getExternalStorageDirectory() + "/mdx/pic/"+ fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        bos.flush();
        bos.close();
    }

    public String bitmapToString(Bitmap bitmap) {
        if(bitmap!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imgBytes = baos.toByteArray();// 转为byte数组
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        }else{
            return "";
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

    public void cutPic(){
        Bitmap bitmap_temp;
        for(int r=0; r<NewsTitle.size(); r++) {
            bitmap_temp = null;
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/mdx/pic/t" + r + ".jpg");
            bitmap_temp = centerSquareScaleBitmap(bitmap);
            BitmapBytes.add(r,bitmapToString(bitmap_temp));
            File file = new File(Environment.getExternalStorageDirectory() + "/mdx/pic/a" + r + ".jpg");//将要保存图片的路径
            if (bitmap_temp != null) {
                try {
                    File file2 = new File(Environment.getExternalStorageDirectory() + "/mdx/pic/t" + r + ".jpg");//将要保存图片的路径
                    file2.delete();
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap_temp.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        MaxProgress=63;
        c5.setChecked(true);
        c5.setTextColor(Color.parseColor("#9E9E9E"));
        mHandler.sendEmptyMessageDelayed(7,1400);
    }

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap) {
        if (null == bitmap) {
            return null;
        }
        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        //以图片短的边为标准
        int length;
        length=widthOrg>=heightOrg?heightOrg:widthOrg;
        //以图片中心为中心裁剪
        int x=(widthOrg-length)/2;
        int y=(heightOrg-length)/2;
        result=Bitmap.createBitmap(bitmap,x,y,length,length);

        return result;

    }

    public void checkUpload(){
        AVObject todo = AVObject.createWithoutData("Person", "5c4edb9e303f394f826f68de");
        todo.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                String title = avObject.getString("name");// 读取 title
                if(title.equals("胡亚东")){
                    AVQuery<AVObject> query = new AVQuery<>("HotSearch_Baidu");
                    query.deleteAllInBackground(new DeleteCallback(){
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                AVQuery<AVObject> query2 = new AVQuery<>("HotSearch_Weibo");
                                query2.deleteAllInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if(e == null){
                                            MaxProgress = 75;
                                            mHandler.sendEmptyMessageDelayed(5, 1600);
                                            c6.setChecked(true);
                                            c6.setTextColor(Color.parseColor("#9E9E9E"));
                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });
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

    public void checkContent(){
        final RichEditText editText = view.findViewById(R.id.InputText01);
        final RichEditText editText2 = view.findViewById(R.id.InputText02);
        final RichEditText editText3 = view.findViewById(R.id.InputText03);
        final RichEditText editText4 = view.findViewById(R.id.InputText04);
        final ImageView imageView = view.findViewById(R.id.Text_Img);
        final TextView btnClose = view.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_check.dismiss();
                mHandler.sendEmptyMessage(6);
            }
        });

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        String[] temp2=null;
        editText.setText(NewsTitle.get(PrePosition));
        editText4.setText(NewsSubTitle.get(PrePosition));
        temp2 = CutContent.get(PrePosition).split("hyd");
        editText2.setText(temp2[0]);

        editText3.setText(temp2[1]);
        Bitmap bitmap = getLoacalBitmap(Environment.getExternalStorageDirectory() + "/mdx/pic/a" + PrePosition + ".jpg");
        if (bitmap == null)
            imageView.setImageResource(R.drawable.noimage);
        else
        imageView.setImageBitmap(bitmap);

            dialog_check = new AlertDialog.Builder(this)
                    .setIcon(R.drawable.check)//设置标题的图片
                    .setTitle("请仔细核对内容（"+IndexPosition+"/"+NewsTitle.size()+"）")//设置对话框的标题
                    .setView(view)
                    .setCancelable(false)
                    .setNegativeButton("取消上传", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "已取消上传", Toast.LENGTH_SHORT).show();
                            Runtime.getRuntime().exit(0);
                        }
                    })
                    .setNeutralButton("上一篇", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (PrePosition == 0) {
                                Toast.makeText(MainActivity.this, "到头啦！", Toast.LENGTH_SHORT).show();
                                checkContent();
                            }else {
                                PrePosition--;
                                IndexPosition--;
                                checkContent();
                            }
                        }
                    })
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NewsTitle.set(PrePosition, Objects.requireNonNull(editText.getText()).toString());
                            NewsSubTitle.set(PrePosition, Objects.requireNonNull(editText4.getText()).toString());
                            CutContent.set(PrePosition, Objects.requireNonNull(editText2.getText()).toString()+"hyd"+ Objects.requireNonNull(editText3.getText()).toString());
                            if (PrePosition ==NewsTitle.size()-1) {
                                dialog.dismiss();
                                mHandler.sendEmptyMessage(6);
                            } else {
                                IndexPosition++;
                                PrePosition++;
                                checkContent();
                            }

                        }
                    }).create();
            dialog_check.show();

    }

    public void uploadData() throws ParseException {
        sp=getSharedPreferences("Setting", MODE_PRIVATE);
        String newssource=sp.getString("newsSource","");
        if(newssource.equals("listhot0")){
            flag=0;
        }else if(newssource.equals("listhot1")){
            flag=10;
        }else{
            flag=20;
        }
        PrePosition=0;

        uploadCycle();
    }

    Date getDateWithDateString(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(dateString);
        return date;
    }

    public void uploadCycle() {
        AVObject uploader = new AVObject("PengpaiNews");
        uploader.put("index", (PrePosition + flag));
        uploader.put("title", NewsTitle.get(PrePosition));
        uploader.put("subtitle", NewsSubTitle.get(PrePosition));
        uploader.put("content", NewsContent.get(PrePosition));
        uploader.put("cutcontent", CutContent.get(PrePosition));
        uploader.put("conurl", ConUrl.get(PrePosition));
        uploader.put("author", id);
        uploader.put("bitmapbytes", BitmapBytes.get(PrePosition));
        uploader.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    AVObject uploader = new AVObject("HotSearch_Baidu");
                    uploader.put("title", HotSearch_Baidu.get(PrePosition));
                    uploader.put("url", HotSearch_Baidu_Url.get(PrePosition));
                    uploader.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                AVObject uploader = new AVObject("HotSearch_Weibo");
                                uploader.put("title", HotSearch_Weibo.get(PrePosition));
                                uploader.put("url", HotSearch_Weibo_Url.get(PrePosition));
                                uploader.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            if (PrePosition < NewsTitle.size() - 1) {
                                                PrePosition++;
                                                uploadCycle();
                                            } else {
                                                text_updateId.setText(id);
                                                String temp = "";
                                                if (NewsSourceS.equals("listhot0")) {
                                                    temp = "每日Top10";
                                                } else if (NewsSourceS.equals("listhot1")) {
                                                    temp = "三日Top10";
                                                } else {
                                                    temp = "一周Top10";
                                                }
                                                text_updateCatelogue.setText(temp);
                                                Calendar calendar = Calendar.getInstance();
                                                int month = calendar.get(Calendar.MONTH) + 1;
                                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                                                int min = calendar.get(Calendar.MINUTE);
                                                text_updateTime.setText(month + "月" + day + "日" + hour + ":" + min);


                                                ch.stop();
                                                recordingTime = (recordingTime + SystemClock.elapsedRealtime() - ch.getBase()) / 1000;// 保存这次记录了的时间
                                                DecimalFormat df = new DecimalFormat("#.000");
                                                df.format(recordingTime);
                                                AVObject uploader3 = new AVObject("UploadTime");
                                                uploader3.put("Id", id);
                                                uploader3.put("Time", recordingTime);
                                                uploader3.saveInBackground();

                                                AVObject uploader2 = new AVObject("LatestUpdate");
                                                uploader2.put("time", month + "月" + day + "日" + hour + ":" + min);
                                                uploader2.put("id", id);
                                                uploader2.put("catelogue", temp);
                                                uploader2.put("valid", true);
                                                uploader2.saveInBackground();

                                                AVQuery<AVObject> query = new AVQuery<>("Person");
                                                query.whereEqualTo("userID", id);
                                                query.getFirstInBackground(new GetCallback<AVObject>() {
                                                    @Override
                                                    public void done(AVObject avObject, AVException e) {
                                                        name = avObject.getString("name");
                                                        contribute = avObject.getNumber("contribute").intValue();
                                                        credit = avObject.getNumber("credit").intValue();
                                                        level = avObject.getString("level");
                                                        achivement = avObject.getNumber("achivement").intValue();
                                                        AVObject todo = AVObject.createWithoutData("Person", objectID);
                                                        todo.put("contribute", (contribute + 1));
                                                        todo.put("credit", (credit + 5 + IndexPosition));
                                                        todo.saveInBackground();
                                                    }
                                                });


                                                AVQuery<AVObject> query2 = new AVQuery<>("PengpaiNews");
                                                try {
                                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                                                    Date date = new Date(System.currentTimeMillis());
                                                    String time = simpleDateFormat.format(date);
                                                    query2.whereGreaterThanOrEqualTo("createdAt", getDateWithDateString(time));
                                                } catch (ParseException e1) {
                                                    e1.printStackTrace();
                                                }
                                                query2.countInBackground(new CountCallback() {
                                                    @Override
                                                    public void done(int i, AVException e) {
                                                        if (e == null) {
                                                            // 查询成功，输出计数
                                                            if (i > 10) {
                                                                AVQuery<AVObject> query3 = new AVQuery<>("PengpaiNews");
                                                                try {
                                                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                                                                    Date date = new Date(System.currentTimeMillis());
                                                                    String time = simpleDateFormat.format(date);
                                                                    query3.whereGreaterThanOrEqualTo("createdAt", getDateWithDateString(time));
                                                                } catch (ParseException e1) {
                                                                    e1.printStackTrace();
                                                                }
                                                                query3.limit(10);
                                                                query3.deleteAllInBackground(new DeleteCallback() {
                                                                    @Override
                                                                    public void done(AVException e) {
                                                                    }
                                                                });
                                                            }
                                                        } else {
                                                            // 查询失败
                                                        }
                                                    }
                                                });

                                                mHandler.sendEmptyMessageDelayed(8, 3000);
                                                MaxProgress = 100;
                                                c8.setChecked(true);
                                                c8.setTextColor(Color.parseColor("#9E9E9E"));

                                            }
                                        } else
                                            Toast.makeText(MainActivity.this, "位于" + PrePosition, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });
                }}});
    }

    private Runnable saveFileRunnable = new Runnable(){
        @Override
        public void run() {
            for (int q = 0; q < NewsTitle.size(); q++) {
                try {
                    mBitmap = BitmapFactory.decodeStream(getImageStream(PicUrl.get(q)));
                    saveFile(mBitmap, "t"+q+".jpg");
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            MaxProgress=50;
            c4.setChecked(true);
            c4.setTextColor(Color.parseColor("#9E9E9E"));
            mHandler.sendEmptyMessageDelayed(4,1600);

        }

    };


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(PreProgress<MaxProgress){
                        PreProgress++;
                        progressbar.setProgress(PreProgress);
                    }
                    break;
                case 1:

                    cutContent();

                    Writer r1,r2,r3,r4,r5,r6 = null;
                    try {
                        r1 = new FileWriter(Environment.getExternalStorageDirectory() + "/mdx/NewsTitle.txt", true);
                        r2 = new FileWriter(Environment.getExternalStorageDirectory() + "/mdx/NewsUrl.txt", true);
                        r3 = new FileWriter(Environment.getExternalStorageDirectory() + "/mdx/NewsSubTitle.txt", true);
                        r4 = new FileWriter(Environment.getExternalStorageDirectory() + "/mdx/NewsContent.txt", true);
                        r5 = new FileWriter(Environment.getExternalStorageDirectory() + "/mdx/PicUrl.txt", true);
                        for (String aNewsTitle : NewsTitle) {
                            r1.write(aNewsTitle + "\n");
                        }
                        r1.close();
                        for (String aConUrl : ConUrl) {
                            r2.write(aConUrl + "\n");
                        }
                        r2.close();
                        for (String aNewsSubTitle : NewsSubTitle) {
                            r3.write(aNewsSubTitle + "\n");
                        }
                        r3.close();
                        for (String aNewsContent : NewsContent) {
                            r4.write(aNewsContent + "\n");
                        }
                        r4.close();
                        for (String aPicUrl : PicUrl) {
                            r5.write(aPicUrl + "\n");
                        }
                        r5.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    downloadPic();
                    MaxProgress=38;
                    c3.setChecked(true);
                    c3.setTextColor(Color.parseColor("#9E9E9E"));

                    try {
                        r6 = new FileWriter(Environment.getExternalStorageDirectory() + "/mdx/CutContent.txt", true);
                        for (String aCutContent : CutContent) {
                            r6.write(aCutContent + "\n");
                        }
                        r6.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 3:
                    if (Looper.myLooper() == null)
                    {
                        Looper.prepare();
                    }
                    ObjectAnimator alpha = ObjectAnimator.ofFloat(isActive, "alpha", 1f, 0.1f);
                    alpha.setDuration(800);
                    ObjectAnimator alpha2 = ObjectAnimator.ofFloat(isActive, "alpha", 0.1f, 1f);
                    alpha2.setDuration(800);
                    AnimatorSet animSet = new AnimatorSet();
                    animSet.play(alpha).before(alpha2);
                    animSet.start();
                    Looper.loop();

                    break;

                case 4:
                    cutPic();
                    break;
                case 5:
                    ch.stop();
                    recordingTime = SystemClock.elapsedRealtime()- ch.getBase();// 保存这次记录了的时间
                    checkContent();
                    break;
                case 6:
                    ch.setBase(SystemClock.elapsedRealtime());// 跳过已经记录了的时间，起到继续计时的作用
                    ch.start();
                    MaxProgress=88;
                    c7.setChecked(true);
                    c7.setTextColor(Color.parseColor("#9E9E9E"));
                    try {
                        uploadData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    checkUpload();
                    break;
                case 8:
                    isFinish=true;
                    Intent starter = new Intent(MainActivity.this, UserInfo.class);
                    MainActivity.this.startActivity(starter);
                    finish();
                    break;
            }
        };
    };

}

