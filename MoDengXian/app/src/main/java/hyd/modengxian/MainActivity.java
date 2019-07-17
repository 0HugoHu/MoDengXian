package hyd.modengxian;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
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
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bilibili.magicasakura.utils.ThemeUtils;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import hyd.modengxian.dialog.CardPickerDialog;
import hyd.modengxian.fragment.fragment_functions;
import hyd.modengxian.fragment.fragment_home;
import hyd.modengxian.fragment.fragment_settings;
import hyd.modengxian.menu.DrawerAdapter;
import hyd.modengxian.menu.DrawerItem;
import hyd.modengxian.menu.SimpleItem;
import hyd.modengxian.menu.SpaceItem;
import hyd.modengxian.notification.NotificationSettings;
import hyd.modengxian.service.FloatingService;
import hyd.modengxian.service.FloatingService_Favourite;
import hyd.modengxian.service.FloatingService_Sliding_Puzzle;
import hyd.modengxian.service.MyAccessibilityService;
import hyd.modengxian.service.SaveService;
import hyd.modengxian.theme.ChooseTheme;
import hyd.modengxian.utils.BottomNavigationViewHelper;
import hyd.modengxian.utils.OpenAccessibilitySettingHelper;
import hyd.modengxian.utils.ThemeHelper;
import ren.yale.android.cachewebviewlib.WebViewCacheInterceptor;
import ren.yale.android.cachewebviewlib.WebViewCacheInterceptorInst;


public class MainActivity extends AppCompatActivity
        implements ThemeUtils.switchColor,
                    CardPickerDialog.ClickListener,
                    DrawerAdapter.OnItemSelectedListener{

    private NotificationManager manger;

    private static final int Big_Image = 1000;
    private static final int Image_with_Text = 1001;
    private static final int Favourite = 1002;
    private static final int Game = 1003;
    private static final int Relax = 1004;
    private static final int MyAccessibilityService = 1005;
    private static final int Initial_Notification = 1006;
    private static final int Only_Text = 1007; //not show
    private static final int Novel_Reader = 1008;
    private static final int History_of_Today = 1009;
    private static final int Hot_Search = 1010;
    private static final int Jokes = 1011;
    private static final int Note = 1012;


    private BottomNavigationView bottomNavigationView;
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
                    toolbar_title.setText("莫等闲");
                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.icon_home_p);
                    bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.icon_collection);
                    bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.icon_me);
                    break;
                case R.id.navigation_functions:
                    tag[0] = TWO;
                    if ((fragment[0] = findFragment(tag[0])) == null) {
                        fragment[0] =new fragment_functions();
                        fragmentTransaction.add(R.id.content_frame, fragment[0], tag[0]);
                    }
                    toolbar_title.setText("收藏");

                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.icon_home);
                    bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.icon_collection_p);
                    bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.icon_me);
                    break;
                case R.id.navigation_settings:
                    tag[0] = THREE;
                    if ((fragment[0] = findFragment(tag[0])) == null) {
                        fragment[0] = new fragment_settings();
                        fragmentTransaction.add(R.id.content_frame, fragment[0], tag[0]);
                    }
                    bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.icon_home);
                    bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.icon_collection);
                    bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.icon_me_p);
                    toolbar_title.setText("我的");
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


//i：大图  j：图文  k：热搜  l:历史
    private int i=0,j=0,k=0,l=0;
    private Boolean Tag_like = false;
    private Boolean Tag_dislike = false;
    private Boolean Tag_Isbigimg = false;
    private Boolean Tag_onAccessibilityService=false;
    private Boolean Tag_isTextwithImg = false;

    private SharedPreferences sp ;
    private SharedPreferences sp2,sp3;
    private String url;
    private WebView webView;
    private final static String PARAM_URL = "param_url";
    private final static String PARAM_MODE = "param_mode";


    private List<String> NewsTitle= new ArrayList<>();
    private List<String> NewsSubTitle=new ArrayList<>();
    private List<String> CutContent=new ArrayList<>();
    private List<String> BitmapBytes=new ArrayList<>();
    private List<String> Content=new ArrayList<>();
    private List<String> ContentUrl=new ArrayList<>();

    private List<String> HotSearch_Weibo=new ArrayList<>();
    private List<String> HotSearch_Baidu=new ArrayList<>();

    private int saveData_i=0;
    private int saveData_Progress=0;
    private boolean isFinish=false;
    private Thread mTimeRunnable = new TimeRunnable();
    private Thread mTimeRunnable2 = new TimeRunnable2();
    private int updateText=0;
    private int now;
    private int NewsNum=10;

    private String NewsTitleUrl = "/MoDengXian/NewsTitle.txt";
    private String NewsContentUrl = "/MoDengXian/Content.txt";
    private String NewsContentUrlUrl = "/MoDengXian/ContentUrl.txt";
    private String NewsSubTitleUrl = "/MoDengXian/NewsSubTiTle.txt";
    private String HotSearch_BaiduUrl = "/MoDengXian/HotSearch_Baidu.txt";
    private String HotSearch_WeiboUrl = "/MoDengXian/HotSearch_Weibo.txt";
    private String Shared_PassagesUrl = "/MoDengXianData/Passages.txt";
    private String Shared_SelectedUrl = "/MoDengXianData/Selected.txt";
    private String Shared_OrderUrl = "/MoDengXianData/Order.txt";

    private String RootUrl = "/MoDengXian";
    private String PicUrl = "/MoDengXian/pic";
    private String SharedUrl = "/MoDengXianData";
    private String WebUrl = "/MoDengXian/WebCache";
    private File filePrefix = Environment.getExternalStorageDirectory();

    private int NewsReadLocation=0;
    private boolean alwaysUpdateData=false;//每次启动始终更新内容


    private FragmentManager fragmentManager;
    private final String ONE = "one";
    private final String TWO = "two";
    private final String THREE = "three";

    private TextView UpdateText;
    private TextView mTextView;
    private Toolbar toolbar;

    private static final int POS_DASHBOARD = 0;
    private static final int POS_SETTING = 4;
    private static final int POS_THEME = 1;
    private static final int POS_HELP = 2;
    private static final int POS_FEEDBACK = 3;
    private static final int POS_FORCESTOP = 6;
    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    private RecyclerView list;
    private DrawerAdapter adapter;
    private int theme_color=0x1;
    private String source;
    private RemoteViews NormalView,BigView,normalView;
    private int position1,position2,position3,position4;
    private int ButtonClickPositon;

    private int current_Category;
    private WebView nullWebView;
    private int webViewCacheNumber=0;
    Map<Integer, String> history_DataMap,history_FestivalMap;
    private String mHtml,mCharactersets;
    private TextView toolbar_title;
    private int state=0;
    private float width;
    private double cutwidth;

    private ImageView drawer_head;
    private TextView drawer_name,drawer_vip;
    private RecyclerView drawer_recy;
    private MaterialSearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar_title=findViewById(R.id.toolbar_title);
        Intent intent = getIntent();
        source=intent==null ? "":intent.getStringExtra("source");
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
        theme_color=ThemeHelper.getTheme(this);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .withDragDistance(150)
                .withRootViewScale(0.65f)
                .withRootViewElevation(4)
                .withRootViewYTranslation(4)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_THEME),
                createItemFor(POS_HELP),
                createItemFor(POS_FEEDBACK),
                createItemFor(POS_SETTING),
                new SpaceItem(48),
                createItemFor(POS_FORCESTOP)));
        adapter.setListener(this);

        list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setSelected(POS_DASHBOARD);

        setNavigationBarColor();

        DealwithIntent();


    }

    @Override
    public void onItemSelected(int position) {
        fragmentManager = getSupportFragmentManager();
        final Fragment[] fragment = {null};
        final String[] tag = new String[1];
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (position){
            case POS_DASHBOARD:
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
                toolbar_title.setText("莫等闲");
                bottomNavigationView.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                break;
            case POS_SETTING:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, NotificationSettings.class);
                startActivity(intent);
                //前往设置
                break;
            case POS_THEME:
                intent = new Intent();
                intent.setClass(MainActivity.this, ChooseTheme.class);
                startActivity(intent);
                break;
            case POS_HELP:
                toolbar_title.setText("帮助");
                bottomNavigationView.setVisibility(View.GONE);
                break;
            case POS_FEEDBACK:
                toolbar_title.setText("意见反馈");
                bottomNavigationView.setVisibility(View.GONE);
                break;
            case POS_FORCESTOP:

                finish();
        }
        slidingRootNav.closeMenu();
    }

    private DrawerItem createItemFor(int position) {
        switch (theme_color){
            case 0x1:
                return new SimpleItem(screenIcons[position], screenTitles[position])
                        .withIconTint(color(R.color.gray))
                        .withSelectedIconTint(color(R.color.blue_doder))
                        .withSelectedTextTint(color(R.color.blue_doder))
                        .withTextTint(color(R.color.default_text_color));
            case 0x2:
                return new SimpleItem(screenIcons[position], screenTitles[position])
                        .withIconTint(color(R.color.default_text_color))
                        .withSelectedIconTint(color(R.color.purple))
                        .withSelectedTextTint(color(R.color.purple))
                        .withTextTint(color(R.color.text_primary_color));
            case 0x3:
                return new SimpleItem(screenIcons[position], screenTitles[position])
                        .withIconTint(color(R.color.default_text_color))
                        .withSelectedIconTint(color(R.color.blue))
                        .withSelectedTextTint(color(R.color.blue))
                        .withTextTint(color(R.color.text_primary_color));
            case 0x4:
                return new SimpleItem(screenIcons[position], screenTitles[position])
                        .withIconTint(color(R.color.default_text_color))
                        .withSelectedIconTint(color(R.color.green))
                        .withSelectedTextTint(color(R.color.green))
                        .withTextTint(color(R.color.text_primary_color));
            case 0x5:
                return new SimpleItem(screenIcons[position], screenTitles[position])
                        .withIconTint(color(R.color.default_text_color))
                        .withSelectedIconTint(color(R.color.green_light))
                        .withSelectedTextTint(color(R.color.green_light))
                        .withTextTint(color(R.color.text_primary_color));
            case 0x6:
                return new SimpleItem(screenIcons[position], screenTitles[position])
                        .withIconTint(color(R.color.default_text_color))
                        .withSelectedIconTint(color(R.color.yellow))
                        .withSelectedTextTint(color(R.color.yellow))
                        .withTextTint(color(R.color.text_primary_color));
            case 0x7:
                return new SimpleItem(screenIcons[position], screenTitles[position])
                        .withIconTint(color(R.color.default_text_color))
                        .withSelectedIconTint(color(R.color.orange))
                        .withSelectedTextTint(color(R.color.orange))
                        .withTextTint(color(R.color.text_primary_color));
            case 0x8:
                return new SimpleItem(screenIcons[position], screenTitles[position])
                        .withIconTint(color(R.color.default_text_color))
                        .withSelectedIconTint(color(R.color.red))
                        .withSelectedTextTint(color(R.color.red))
                        .withTextTint(color(R.color.text_primary_color));
            default:
                return new SimpleItem(screenIcons[position], screenTitles[position])
                        .withIconTint(color(R.color.selector_tink_pink))
                        .withTextTint(color(R.color.selector_tink_pink));
        }
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (toolbar != null){
            toolbar_title.setText("莫等闲");
        }
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ThemeUtils.getColorById(this, R.color.theme_color_primary_dark));
        ActivityManager.TaskDescription description = new ActivityManager.TaskDescription(null, null,
                ThemeUtils.getThemeAttrColor(this, android.R.attr.colorPrimary));
        setTaskDescription(description);

        refreshUI();
        mTimeRunnable2.start();

        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        width = outMetrics.widthPixels;

        cutwidth=px2dip(MainActivity.this,width)*0.35/2+150;

        drawer_head = findViewById(R.id.setting_image_header);
        drawer_name = findViewById(R.id.drawer_id);
        drawer_vip = findViewById(R.id.drawer_ic_type);
        drawer_recy = findViewById(R.id.list);

        //Toast.makeText(this, ""+cutwidth, Toast.LENGTH_SHORT).show();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) drawer_head.getLayoutParams();
        layoutParams.leftMargin = dip2px(MainActivity.this, (float) (cutwidth/2-35));
        //Toast.makeText(this, ""+(Double.valueOf(cutwidth/2-35)).intValue(), Toast.LENGTH_SHORT).show();
        drawer_head.setLayoutParams(layoutParams);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                toolbar_title.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                toolbar_title.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
            }
        });
        searchView.setMenuItem(item);
        searchView.setVoiceSearch(false);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:

                break;
            default:
                break;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int replaceColorById(Context context, @ColorRes int colorId) {
        if (ThemeHelper.isDefaultTheme(context)) {
            return context.getResources().getColor(colorId,null);
        }
        String theme = getTheme(context);
        if (theme != null) {
            colorId = getThemeColorId(context, colorId, theme);
        }
        return context.getResources().getColor(colorId,null);
    }

    @Override
    public int replaceColor(Context context, @ColorInt int originColor) {
        if (ThemeHelper.isDefaultTheme(context)) {
            return originColor;
        }
        String theme = getTheme(context);
        int colorId = -1;

        if (theme != null) {
            colorId = getThemeColor(context, originColor, theme);
        }
        return colorId != -1 ? getResources().getColor(colorId,null) : originColor;
    }

    @Override
    public void onConfirm(int currentTheme) {
        if (ThemeHelper.getTheme(MainActivity.this) != currentTheme) {
            ThemeHelper.setTheme(MainActivity.this, currentTheme);
            setNavigationBarColor();
        }
    }

    public void init(){
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView=findViewById(R.id.navigation);

        if(source==null || source.equals("")) {
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
            bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.icon_home_p);
        }
        /*ThemeUtils.refreshUI(MainActivity.this, new ThemeUtils.ExtraRefreshable() {
                    @Override
                    public void refreshGlobal(Activity activity) {
                        //for global setting, just do once
                        if (Build.VERSION.SDK_INT >= 21) {
                            final MainActivity context = MainActivity.this;
                            ActivityManager.TaskDescription taskDescription =
                                    new ActivityManager.TaskDescription(null, null,
                                            ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary));
                            setTaskDescription(taskDescription);
                            getWindow().setStatusBarColor(
                                    ThemeUtils.getColorById(context, R.color.theme_color_primary_dark));

                        }
                    }

                    @Override
                    public void refreshSpecificView(View view) {
                        //TODO: will do this for each traversal
                    }
                });


*/
        manger = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Resources resources =getResources();

        sp=getSharedPreferences("Setting", MODE_PRIVATE);
        Tag_onAccessibilityService=sp.getBoolean("onAccessibilityService",false);

        sp2=getSharedPreferences("Preference", MODE_PRIVATE);
        position1=sp2.getInt("Position1",0);
        if(position1==0){
            SharedPreferences.Editor edit = sp2.edit();
            edit.putInt("Position1",1001);
            edit.putInt("Position2",1009);
            edit.putInt("Position3",1002);
            edit.putInt("Position4",1004);
            edit.apply();
        }
        position1=sp2.getInt("Position1",0);
        position2=sp2.getInt("Position2",0);
        position3=sp2.getInt("Position3",0);
        position4=sp2.getInt("Position4",0);

        if(sp.getBoolean("No_Sound_isOpen",false)) {
            Toast.makeText(this, ""+sp, Toast.LENGTH_SHORT).show();
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (Objects.requireNonNull(audioManager).getRingerMode() > 1) {
                verifyDoNotDisturbPermissions();
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
        }

        ImgTitleList=resources.getStringArray(R.array.imgtitle);
        ImgSubtitleList=resources.getStringArray(R.array.imgsubtitle);

        VocTitleList=resources.getStringArray(R.array.voctitle);
        VocYinbiaoList=resources.getStringArray(R.array.vocyinbiao);
        VocContentList=resources.getStringArray(R.array.voccontent);
        VocMeanList=resources.getStringArray(R.array.vocmean);

        nullWebView=findViewById(R.id.nullWebView);




        WebViewCacheInterceptorInst.getInstance().
                init(new WebViewCacheInterceptor.Builder(this));

        AVOSCloud.initialize(this,"xHiee3l7EnDWTMU2nQXlPdoM-gzGzoHsz","TIYGEvp1RXu3H86O6j55ezx6");
        AVOSCloud.setDebugLogEnabled(true);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体


//获取应用接收分享


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

        MyBroadCast8 hot_search_Next = new MyBroadCast8();
        IntentFilter filter13=new IntentFilter("hot_search_Next");
        registerReceiver(hot_search_Next, filter13);

        MyBroadCast9 hot_search_Previous = new MyBroadCast9();
        IntentFilter filter14=new IntentFilter("hot_search_Previous");
        registerReceiver(hot_search_Previous, filter14);

        MyBroadCast10 history_Next = new MyBroadCast10();
        IntentFilter filter21=new IntentFilter("history_Next");
        registerReceiver(history_Next, filter21);

        MyBroadCast11 history_Previous = new MyBroadCast11();
        IntentFilter filter22=new IntentFilter("history_Previous");
        registerReceiver(history_Previous, filter22);

//初始界面按钮
        MyBroadCast_Main_Action1 action1 = new MyBroadCast_Main_Action1();
        IntentFilter filter7=new IntentFilter("action1");
        registerReceiver(action1, filter7);

        MyBroadCast_Main_Action2 action2 = new MyBroadCast_Main_Action2();
        IntentFilter filter8=new IntentFilter("action2");
        registerReceiver(action2, filter8);

        MyBroadCast_Main_Action3 action3 = new MyBroadCast_Main_Action3();
        IntentFilter filter9=new IntentFilter("action3");
        registerReceiver(action3, filter9);

        MyBroadCast_Main_Action4 action4 = new MyBroadCast_Main_Action4();
        IntentFilter filter10=new IntentFilter("action4");
        registerReceiver(action4, filter10);

        MyBroadCast_Main_AccessibilityService MyAccibilityService = new MyBroadCast_Main_AccessibilityService();
        IntentFilter filter11=new IntentFilter("Main_AccessibilityService");
        registerReceiver(MyAccibilityService, filter11);


//关闭按钮
        MyBroadCast7 Close = new MyBroadCast7();
        IntentFilter filter12=new IntentFilter("Close");
        registerReceiver(Close, filter12);

        MyBroadCast_HotSearch_Text1 action_Text1 = new MyBroadCast_HotSearch_Text1();
        IntentFilter filter15=new IntentFilter("HotSearch_Text1");
        registerReceiver(action_Text1, filter15);

        MyBroadCast_HotSearch_Text2 action_Text2 = new MyBroadCast_HotSearch_Text2();
        IntentFilter filter16=new IntentFilter("HotSearch_Text2");
        registerReceiver(action_Text2, filter16);

        MyBroadCast_HotSearch_Text3 action_Text3 = new MyBroadCast_HotSearch_Text3();
        IntentFilter filter17=new IntentFilter("HotSearch_Text3");
        registerReceiver(action_Text3, filter17);

        MyBroadCast_HotSearch_Text4 action_Text4 = new MyBroadCast_HotSearch_Text4();
        IntentFilter filter18=new IntentFilter("HotSearch_Text4");
        registerReceiver(action_Text4, filter18);

        MyBroadCast_HotSearch_Text5 action_Text5 = new MyBroadCast_HotSearch_Text5();
        IntentFilter filter19=new IntentFilter("HotSearch_Text5");
        registerReceiver(action_Text5, filter19);

        MyBroadCast_Load_More load_more = new MyBroadCast_Load_More();
        IntentFilter filter20=new IntentFilter("Load_More");
        registerReceiver(load_more, filter20);

    }

    //notification helper
    public void sendNotification(int id) {
        switch (id) {
            case Initial_Notification:
                manger.cancelAll();
                sendNotificationBundle(1006,true);
                break;
            case Big_Image:
                sendNotificationBundle(1000,false);
                break;
            case Image_with_Text:
                sendNotificationBundle(1001,false);
                break;
            case Only_Text:
                sendNotificationBundle(1007,false);
                break;
            case Favourite:
                sendNotificationBundle(1002,true);
                break;
            case Game:
                sendNotificationBundle(1003,true);
                break;
            case Relax:
                sendNotificationBundle(1004,true);
                break;
            case History_of_Today:
                sendNotificationBundle(1009,false);
                break;
            case Jokes:
                sendNotificationBundle(1011,true);
                break;
            case Hot_Search:
                sendNotificationBundle(1010,false);
                break;
            case Novel_Reader:
                sendNotificationBundle(1008,true);
                break;
            case Note:
                sendNotificationBundle(1012,true);
                break;
        }
    }
    public void sendNotificationBundle(int category,boolean ongoing){

        NotificationChannel notificationChannel = new NotificationChannel("1",
                "请点击并设置重要性为“低”", NotificationManager.IMPORTANCE_HIGH);
        manger.createNotificationChannel(notificationChannel);
        NormalView = new RemoteViews(getPackageName(), R.layout.normal_down);
        current_Category=category;
        switch(category){
            case 1000:
                BigView = new RemoteViews(getPackageName(), R.layout.activity_image);
                BigView.setTextViewText(R.id.Image_Title, ImgTitleList[i]);
                BigView.setTextViewText(R.id.Image_SubTitle, ImgSubtitleList[i]);
                Intent Next_Img=new Intent("Big_Image_Next");
                BigView.setOnClickPendingIntent(R.id.btnNext,PendingIntent.getBroadcast(MainActivity.this, 11, Next_Img,PendingIntent.FLAG_UPDATE_CURRENT));
                Intent Previous_Img=new Intent("Big_Image_Previous");
                BigView.setOnClickPendingIntent(R.id.btnPrevious,PendingIntent.getBroadcast(MainActivity.this, 12, Previous_Img,PendingIntent.FLAG_UPDATE_CURRENT));
                Intent close=new Intent("Close");
                BigView.setOnClickPendingIntent(R.id.btnClose,PendingIntent.getBroadcast(MainActivity.this, 0, close,PendingIntent.FLAG_UPDATE_CURRENT));
                break;
            case 1001:
                BigView = new RemoteViews(getPackageName(), R.layout.activity_text);
                LikeOrDislike();
                SetText();
                Bitmap bitmap = getLoacalBitmap(filePrefix +PicUrl+"/a" + j + ".jpg");
                BigView.setImageViewBitmap(R.id.Text_Img,bitmap);
                break;
            case 1002:
                break;
            case 1003:
                BigView = new RemoteViews(getPackageName(), R.layout.activity_game);
                break;
            case 1004:
                BigView = new RemoteViews(getPackageName(), R.layout.activity_relax);
                close=new Intent("Close");
                BigView.setOnClickPendingIntent(R.id.btnClose,PendingIntent.getBroadcast(MainActivity.this, 0, close,PendingIntent.FLAG_UPDATE_CURRENT));
                break;
            case 1005:
                break;
            case 1006:
                break;
            case 1007:
                BigView = new RemoteViews(getPackageName(), R.layout.activity_onlytext);
                LikeOrDislike();
                SetText();
                break;
            case 1008:
                break;
            case 1009:
                BigView = new RemoteViews(getPackageName(), R.layout.activity_history);
                Intent Next_History=new Intent("history_Next");
                BigView.setOnClickPendingIntent(R.id.btnNext,PendingIntent.getBroadcast(MainActivity.this, 13, Next_History,PendingIntent.FLAG_UPDATE_CURRENT));
                Intent Previous_History=new Intent("history_Previous");
                BigView.setOnClickPendingIntent(R.id.btnPrevious,PendingIntent.getBroadcast(MainActivity.this, 14, Previous_History,PendingIntent.FLAG_UPDATE_CURRENT));
                close=new Intent("Close");
                BigView.setOnClickPendingIntent(R.id.btnClose,PendingIntent.getBroadcast(MainActivity.this, 0, close,PendingIntent.FLAG_UPDATE_CURRENT));

                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH)+1;
                int date = calendar.get(Calendar.DAY_OF_MONTH);
                switch (month){
                    case 1:
                        history_DataMap = Txt("history_01");
                        break;
                    case 2:
                        history_DataMap = Txt("history_02");
                        break;
                    case 3:
                        history_DataMap = Txt("history_03");
                        break;
                    case 4:
                        history_DataMap = Txt("history_04");
                        break;
                    case 5:
                        history_DataMap = Txt("history_05");
                        break;
                }
                String position=searchForPosition_DataMap(month,date);
                Toast.makeText(this, ""+history_DataMap.get(214), Toast.LENGTH_SHORT).show();
                String[] temp=position.split("&");
                BigView.setTextViewText(R.id.History_Title,"历史上的今天（"+history_DataMap.get(Integer.valueOf(temp[0])).split("：")[0]+"）");
                StringBuilder text= new StringBuilder();
                switch (l){
                    case 0:
                        history_FestivalMap = Txt("festival");
                        int y=searchForPosition_Festival(month,date);
                        if(y==0){

                        }else{
                            text.append(history_FestivalMap.get(1+ y)+"\n");
                            text.append(history_FestivalMap.get(2+ y));
                            BigView.setTextViewText(R.id.History_Content, text.toString());
                        }
                        break;
                    case 1:
                        for(int i=0;i<Integer.valueOf(temp[1])-1;i++){
                            text.append(history_DataMap.get(1+Integer.valueOf(temp[0]) + i));
                        }
                        break;
                    case 2:
                        for(int i=0;i<Integer.valueOf(temp[2]);i++){
                            text.append(history_DataMap.get(Integer.valueOf(temp[0]) +Integer.valueOf(temp[1])+ i));
                        }
                        break;
                    case 3:
                        for(int i=0;i<Integer.valueOf(temp[3]);i++){
                            text.append(history_DataMap.get(Integer.valueOf(temp[0]) +Integer.valueOf(temp[1])+Integer.valueOf(temp[2])+ i));
                        }
                        break;
                }

                if(l!=0) {
                    String showtext = "";
                    showtext = text.toString().replace("：", "：\n");
                    showtext = showtext.replace("。", "。\n");
                    BigView.setTextViewText(R.id.History_Content, showtext);
                }
                break;
            case 1010:
                BigView = new RemoteViews(getPackageName(), R.layout.activity_hotsearch);
                SetHotSearchText();
                Intent Next_hot_Search=new Intent("hot_search_Next");
                BigView.setOnClickPendingIntent(R.id.btnNext,PendingIntent.getBroadcast(MainActivity.this, 11, Next_hot_Search,PendingIntent.FLAG_UPDATE_CURRENT));
                Intent Previous_hot_Search=new Intent("hot_search_Previous");
                BigView.setOnClickPendingIntent(R.id.btnPrevious,PendingIntent.getBroadcast(MainActivity.this, 12, Previous_hot_Search,PendingIntent.FLAG_UPDATE_CURRENT));
                Intent Next_hot_Search1=new Intent("HotSearch_Text1");
                BigView.setOnClickPendingIntent(R.id.hotsearch_text1,PendingIntent.getBroadcast(MainActivity.this, 13, Next_hot_Search1,PendingIntent.FLAG_UPDATE_CURRENT));
                Intent Next_hot_Search2=new Intent("HotSearch_Text2");
                BigView.setOnClickPendingIntent(R.id.hotsearch_text2,PendingIntent.getBroadcast(MainActivity.this, 14, Next_hot_Search2,PendingIntent.FLAG_UPDATE_CURRENT));
                Intent Next_hot_Search3=new Intent("HotSearch_Text3");
                BigView.setOnClickPendingIntent(R.id.hotsearch_text3,PendingIntent.getBroadcast(MainActivity.this, 15, Next_hot_Search3,PendingIntent.FLAG_UPDATE_CURRENT));
                Intent Next_hot_Search4=new Intent("HotSearch_Text4");
                BigView.setOnClickPendingIntent(R.id.hotsearch_text4,PendingIntent.getBroadcast(MainActivity.this, 16, Next_hot_Search4,PendingIntent.FLAG_UPDATE_CURRENT));
                Intent Next_hot_Search5=new Intent("HotSearch_Text5");
                BigView.setOnClickPendingIntent(R.id.hotsearch_text5,PendingIntent.getBroadcast(MainActivity.this, 17, Next_hot_Search5,PendingIntent.FLAG_UPDATE_CURRENT));
                Intent close_=new Intent("Close");
                BigView.setOnClickPendingIntent(R.id.btnClose,PendingIntent.getBroadcast(MainActivity.this, 0, close_,PendingIntent.FLAG_UPDATE_CURRENT));

                break;
            case 1011:
                break;
            case 1012:
                break;
        }

        switch (ButtonClickPositon){
            case 1:
                normalView = new RemoteViews(getPackageName(), R.layout.normal_1);
                break;
            case 2:
                normalView = new RemoteViews(getPackageName(), R.layout.normal_2);
                break;
            case 3:
                normalView = new RemoteViews(getPackageName(), R.layout.normal_3);
                break;
            case 4:
                normalView = new RemoteViews(getPackageName(), R.layout.normal_4);
                break;
        }
        Notification notification_content = new NotificationCompat.Builder(this,"1")
                .setSmallIcon(R.drawable.icon)
                .setTicker("")
                .setOngoing(ongoing)
                .setGroupSummary(false)
                .setGroup("")
                .setContent(NormalView)
                .setCustomBigContentView(BigView)
                .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                .build();

        if(category==1000||category==1001||category==1007) {
            Intent action_Like = new Intent("Like");
            Intent action_Dislike = new Intent("Dislike");
            BigView.setOnClickPendingIntent(R.id.btnLike, PendingIntent.getBroadcast(MainActivity.this, 10, action_Like, PendingIntent.FLAG_UPDATE_CURRENT));
            BigView.setOnClickPendingIntent(R.id.btnDislike, PendingIntent.getBroadcast(MainActivity.this, 11, action_Dislike, PendingIntent.FLAG_UPDATE_CURRENT));
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null && !(category==1006||category==1003||category==1002)) {
            notificationManager.notify(1, notification_content);
        }
        if(category==1003){
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            MainActivity.this.sendBroadcast(it);
            Intent intent = new Intent(MainActivity.this, FloatingService_Sliding_Puzzle.class);
            startService(intent);
        }
        if(category==1002){
            Intent intent= new Intent().setClass(MainActivity.this, FloatingService_Favourite.class);
            startService(intent);
        }

        NotificationChannel notificationChannel2 = new NotificationChannel("0",
                "请点击并设置重要性为“低”", NotificationManager.IMPORTANCE_HIGH);
        manger.createNotificationChannel(notificationChannel2);
        if(category==1006) {
            normalView = new RemoteViews(getPackageName(), R.layout.normal);
        }
        normalView.setTextViewText(R.id.Normal_1,PositionToString(position1));
        normalView.setTextViewText(R.id.Normal_2,PositionToString(position2));
        normalView.setTextViewText(R.id.Normal_3,PositionToString(position3));
        normalView.setTextViewText(R.id.Normal_4,PositionToString(position4));
        SetAccessibilityIcon();

        Notification notification_fix = new NotificationCompat.Builder(this,"0")
                .setSmallIcon(R.drawable.icon)
                .setTicker("")
                .setGroupSummary(false)
                .setGroup("")
                .setOngoing(true)
                .setContent(normalView)
                .setPriority(NotificationCompat.PRIORITY_MAX)//设置最大优先级
                .build();

        Intent action1=new Intent("action1");
        normalView.setOnClickPendingIntent(R.id.Normal_1,PendingIntent.getBroadcast(MainActivity.this, 101, action1,PendingIntent.FLAG_UPDATE_CURRENT));

        Intent action2=new Intent("action2");
        normalView.setOnClickPendingIntent(R.id.Normal_2,PendingIntent.getBroadcast(MainActivity.this, 102, action2,PendingIntent.FLAG_UPDATE_CURRENT));

        Intent action3=new Intent("action3");
        normalView.setOnClickPendingIntent(R.id.Normal_3,PendingIntent.getBroadcast(MainActivity.this, 103, action3,PendingIntent.FLAG_UPDATE_CURRENT));

        Intent action4=new Intent("action4");
        normalView.setOnClickPendingIntent(R.id.Normal_4,PendingIntent.getBroadcast(MainActivity.this, 104, action4,PendingIntent.FLAG_UPDATE_CURRENT));

        Intent action0=new Intent("Main_AccessibilityService");
        normalView.setOnClickPendingIntent(R.id.Normal_Icon,PendingIntent.getBroadcast(MainActivity.this, 100, action0,PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationManager notificationManager2 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager2 != null) {
            notificationManager2.notify(0, notification_fix);
        }

    }
    public void LikeOrDislike(){
        if(Tag_like && !Tag_Isbigimg) {
            BigView.setImageViewResource(R.id.btnLike, R.drawable.like_press);
        }else{
            BigView.setImageViewResource(R.id.btnLike, R.drawable.like);
        }

        if(Tag_dislike && !Tag_Isbigimg) {
            BigView.setImageViewResource(R.id.btnDislike, R.drawable.dislike_press);
        }else{
            BigView.setImageViewResource(R.id.btnDislike, R.drawable.dislike);
        }
    }
    public void SetText(){
        BigView.setTextViewText(R.id.Text_Title, NewsTitle.get(j));
        BigView.setTextViewText(R.id.Text_SubTitle, NewsSubTitle.get(j));
/*
        if(Content.get(j).length()>103) {
            BigView.setTextViewText(R.id.Text_Content, Content.get(j).substring(0, 103));
            if(Content.get(j).length()>145)
                BigView.setTextViewText(R.id.Text_Content2, Content.get(j).substring(103, 145) + "...<查看更多>");
            else
                BigView.setTextViewText(R.id.Text_Content2, Content.get(j).substring(103));
        }else{
            BigView.setTextViewText(R.id.Text_Content, Content.get(j));
        }
*/
        if(Content.get(j).length()<96 && current_Category==1001)
            BigView.setTextViewText(R.id.load_more,"");
        else if(Content.get(j).length()<150 && current_Category==1007)
            BigView.setTextViewText(R.id.load_more,"");
        else
            BigView.setTextViewText(R.id.load_more,"查看更多...");

        BigView.setTextViewText(R.id.Text_Content, Content.get(j));

        Intent load_more=new Intent("Load_More");
        BigView.setOnClickPendingIntent(R.id.load_more,PendingIntent.getBroadcast(MainActivity.this, 0, load_more,PendingIntent.FLAG_UPDATE_CURRENT));

        Intent close=new Intent("Close");
        BigView.setOnClickPendingIntent(R.id.btnClose,PendingIntent.getBroadcast(MainActivity.this, 0, close,PendingIntent.FLAG_UPDATE_CURRENT));

        Intent next_IwT=new Intent("Image_with_Text_Next");
        BigView.setOnClickPendingIntent(R.id.btnNext,PendingIntent.getBroadcast(MainActivity.this, 1, next_IwT,PendingIntent.FLAG_UPDATE_CURRENT));

        Intent previous_IwT=new Intent("Image_with_Text_Previous");
        BigView.setOnClickPendingIntent(R.id.btnPrevious,PendingIntent.getBroadcast(MainActivity.this, 2, previous_IwT,PendingIntent.FLAG_UPDATE_CURRENT));

    }
    public void SetHotSearchText(){
        String[] search_temp,search_temp2,search_temp3,search_temp4,search_temp5=null;
        switch (k){
            case 0:
                BigView.setTextViewText(R.id.hotsearch_title, "微博热搜：");
                BigView.setTextViewText(R.id.hotsearch_number1, "1");
                BigView.setTextViewText(R.id.hotsearch_number2, "2");
                BigView.setTextViewText(R.id.hotsearch_number3, "3");
                BigView.setTextViewText(R.id.hotsearch_number4, "4");
                BigView.setTextViewText(R.id.hotsearch_number5, "5");
                BigView.setImageViewResource(R.id.hotsearch_numbercolor1,R.color.number_1);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor2,R.color.number_2);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor3,R.color.number_3);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor4,R.color.number_4);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor5,R.color.number_4);
                search_temp = HotSearch_Weibo.get(0).split("hyd");
                search_temp2 = HotSearch_Weibo.get(1).split("hyd");
                search_temp3 = HotSearch_Weibo.get(2).split("hyd");
                search_temp4 = HotSearch_Weibo.get(3).split("hyd");
                search_temp5 = HotSearch_Weibo.get(4).split("hyd");
                BigView.setTextViewText(R.id.hotsearch_text1, search_temp[0]);
                BigView.setTextViewText(R.id.hotsearch_text2, search_temp2[0]);
                BigView.setTextViewText(R.id.hotsearch_text3, search_temp3[0]);
                BigView.setTextViewText(R.id.hotsearch_text4, search_temp4[0]);
                BigView.setTextViewText(R.id.hotsearch_text5, search_temp5[0]);
                break;
            case 1:
                BigView.setTextViewText(R.id.hotsearch_title, "微博热搜：");
                search_temp = HotSearch_Weibo.get(5).split("hyd");
                search_temp2 = HotSearch_Weibo.get(6).split("hyd");
                search_temp3 = HotSearch_Weibo.get(7).split("hyd");
                search_temp4 = HotSearch_Weibo.get(8).split("hyd");
                search_temp5 = HotSearch_Weibo.get(9).split("hyd");
                BigView.setTextViewText(R.id.hotsearch_text1, search_temp[0]);
                BigView.setTextViewText(R.id.hotsearch_text2, search_temp2[0]);
                BigView.setTextViewText(R.id.hotsearch_text3, search_temp3[0]);
                BigView.setTextViewText(R.id.hotsearch_text4, search_temp4[0]);
                BigView.setTextViewText(R.id.hotsearch_text5, search_temp5[0]);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor1,R.color.number_4);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor2,R.color.number_4);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor3,R.color.number_4);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor4,R.color.number_4);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor5,R.color.number_4);
                BigView.setTextViewText(R.id.hotsearch_number1, "6");
                BigView.setTextViewText(R.id.hotsearch_number2, "7");
                BigView.setTextViewText(R.id.hotsearch_number3, "8");
                BigView.setTextViewText(R.id.hotsearch_number4, "9");
                BigView.setTextViewText(R.id.hotsearch_number5, "X");
                break;
            case 2:
                search_temp = HotSearch_Baidu.get(0).split("hyd");
                search_temp2 = HotSearch_Baidu.get(1).split("hyd");
                search_temp3 = HotSearch_Baidu.get(2).split("hyd");
                search_temp4 = HotSearch_Baidu.get(3).split("hyd");
                search_temp5 = HotSearch_Baidu.get(4).split("hyd");
                BigView.setImageViewResource(R.id.hotsearch_numbercolor1,R.color.number_1);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor2,R.color.number_2);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor3,R.color.number_3);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor4,R.color.number_4);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor5,R.color.number_4);
                BigView.setTextViewText(R.id.hotsearch_title, "百度热搜：");
                BigView.setTextViewText(R.id.hotsearch_text1, search_temp[0]);
                BigView.setTextViewText(R.id.hotsearch_text2, search_temp2[0]);
                BigView.setTextViewText(R.id.hotsearch_text3, search_temp3[0]);
                BigView.setTextViewText(R.id.hotsearch_text4, search_temp4[0]);
                BigView.setTextViewText(R.id.hotsearch_text5, search_temp5[0]);
                BigView.setTextViewText(R.id.hotsearch_number1, "1");
                BigView.setTextViewText(R.id.hotsearch_number2, "2");
                BigView.setTextViewText(R.id.hotsearch_number3, "3");
                BigView.setTextViewText(R.id.hotsearch_number4, "4");
                BigView.setTextViewText(R.id.hotsearch_number5, "5");
                break;

            case 3:
                search_temp = HotSearch_Baidu.get(5).split("hyd");
                search_temp2 = HotSearch_Baidu.get(6).split("hyd");
                search_temp3 = HotSearch_Baidu.get(7).split("hyd");
                search_temp4 = HotSearch_Baidu.get(8).split("hyd");
                search_temp5 = HotSearch_Baidu.get(9).split("hyd");
                BigView.setImageViewResource(R.id.hotsearch_numbercolor1,R.color.number_4);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor2,R.color.number_4);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor3,R.color.number_4);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor4,R.color.number_4);
                BigView.setImageViewResource(R.id.hotsearch_numbercolor5,R.color.number_4);
                BigView.setTextViewText(R.id.hotsearch_title, "百度热搜：");
                BigView.setTextViewText(R.id.hotsearch_text1, search_temp[0]);
                BigView.setTextViewText(R.id.hotsearch_text2, search_temp2[0]);
                BigView.setTextViewText(R.id.hotsearch_text3, search_temp3[0]);
                BigView.setTextViewText(R.id.hotsearch_text4, search_temp4[0]);
                BigView.setTextViewText(R.id.hotsearch_text5, search_temp5[0]);
                BigView.setTextViewText(R.id.hotsearch_number1, "6");
                BigView.setTextViewText(R.id.hotsearch_number2, "7");
                BigView.setTextViewText(R.id.hotsearch_number3, "8");
                BigView.setTextViewText(R.id.hotsearch_number4, "9");
                BigView.setTextViewText(R.id.hotsearch_number5, "X");
                break;
        }


    }
    public void SetAccessibilityIcon(){
        if(Tag_onAccessibilityService) {
            normalView.setImageViewResource(R.id.Normal_Icon, R.drawable.icon_click);
        }else{
            normalView.setImageViewResource(R.id.Normal_Icon, R.drawable.icon);
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
    //HOT SEARCH next
    class MyBroadCast8 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            k++;
            if(k==4)
                k=0;
            sendNotification(Hot_Search);
        }
    }
    //HOT SEARCH previous
    class MyBroadCast9 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            k--;
            if(k==-1)
                k=3;
            sendNotification(Hot_Search);
        }
    }
    //history next
    class MyBroadCast10 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            l++;
            if(l>3)
                l=0;
            sendNotification(History_of_Today);
        }
    }
    //history previous
    class MyBroadCast11 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            l--;
            if(l<0)
                l=3;
            sendNotification(History_of_Today);
        }
    }

    //广播-主界面
    class MyBroadCast_Main_Action1 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            ButtonClickPositon=1;
            if(!CheckIsImageAndText(position1))
            sendNotificationBundle(position1,true);
        }
    }
    class MyBroadCast_Main_Action2 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            ButtonClickPositon=2;
            if(!CheckIsImageAndText(position2))
            sendNotificationBundle(position2,true);
        }
    }
    class MyBroadCast_Main_Action3 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            ButtonClickPositon=3;
            if(!CheckIsImageAndText(position3))
            sendNotificationBundle(position3,true);
        }
    }
    class MyBroadCast_Main_Action4 extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent)
        {
            ButtonClickPositon=4;
            if(!CheckIsImageAndText(position4))
            sendNotificationBundle(position4,true);
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
    //清除通知按钮
    class MyBroadCast7 extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent)
        {
            manger.cancelAll();
            sendNotification(1006);

        }
    }

    class MyBroadCast_HotSearch_Text1 extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent)
        {
            startFloatingWindow(1);
        }
    }
    class MyBroadCast_HotSearch_Text2 extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent)
        {
            startFloatingWindow(2);
        }
    }
    class MyBroadCast_HotSearch_Text3 extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent)
        {
            startFloatingWindow(3);
        }
    }
    class MyBroadCast_HotSearch_Text4 extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent)
        {
            startFloatingWindow(4);
        }
    }
    class MyBroadCast_HotSearch_Text5 extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent)
        {
            startFloatingWindow(5);
        }
    }

    class MyBroadCast_Load_More extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent)
        {
            startFloatingWindow(0);
        }
    }

    //方法类
    private void verifyDoNotDisturbPermissions(){
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
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
    private String getTheme(Context context) {
        if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_STORM) {
            return "blue";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_HOPE) {
            return "purple";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_WOOD) {
            return "green";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_LIGHT) {
            return "green_light";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_THUNDER) {
            return "yellow";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_SAND) {
            return "orange";
        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_FIREY) {
            return "red";
        }
        return null;
    }
    private @ColorRes int getThemeColorId(Context context, int colorId, String theme) {
        switch (colorId) {
            case R.color.theme_color_primary:
                return context.getResources().getIdentifier(theme, "color", getPackageName());
            case R.color.theme_color_primary_dark:
                return context.getResources().getIdentifier(theme + "_dark", "color", getPackageName());
            case R.color.theme_color_primary_trans:
                return context.getResources().getIdentifier(theme + "_trans", "color", getPackageName());
        }
        return colorId;
    }
    private @ColorRes int getThemeColor(Context context, int color, String theme) {
        switch (color) {
            case 0xfffb7299:
                return context.getResources().getIdentifier(theme, "color", getPackageName());
            case 0xffb85671:
                return context.getResources().getIdentifier(theme + "_dark", "color", getPackageName());
            case 0x99f0486c:
                return context.getResources().getIdentifier(theme + "_trans", "color", getPackageName());
        }
        return -1;
    }
    public void setNavigationBarColor(){
        BottomNavigationViewHelper.setImageSize(bottomNavigationView,60,60);
        theme_color=ThemeHelper.getTheme(this);
        list.removeAllViews();
        adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_THEME),
                createItemFor(POS_HELP),
                createItemFor(POS_FEEDBACK),
                createItemFor(POS_SETTING),
                new SpaceItem(48),
                createItemFor(POS_FORCESTOP)));
        adapter.setListener(this);

        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        adapter.setSelected(POS_DASHBOARD);

        switch (theme_color){
            case 0x1:
                bottomNavigationView.setItemIconTintList(null);
                bottomNavigationView.setItemTextColor(this.getColorStateList(R.color.selector_tink_blue));
                break;
            case 0x2:
                bottomNavigationView.setItemIconTintList(this.getColorStateList(R.color.selector_tink_purple));
                bottomNavigationView.setItemTextColor(this.getColorStateList(R.color.selector_tink_purple));
                break;
            case 0x3:
                bottomNavigationView.setItemIconTintList(this.getColorStateList(R.color.selector_tink_blue));
                bottomNavigationView.setItemTextColor(this.getColorStateList(R.color.selector_tink_blue));
                break;
            case 0x4:
                bottomNavigationView.setItemIconTintList(this.getColorStateList(R.color.selector_tink_green));
                bottomNavigationView.setItemTextColor(this.getColorStateList(R.color.selector_tink_green));
                break;
            case 0x5:
                bottomNavigationView.setItemIconTintList(this.getColorStateList(R.color.selector_tink_green_light));
                bottomNavigationView.setItemTextColor(this.getColorStateList(R.color.selector_tink_green_light));
                break;
            case 0x6:
                bottomNavigationView.setItemIconTintList(this.getColorStateList(R.color.selector_tink_yellow));
                bottomNavigationView.setItemTextColor(this.getColorStateList(R.color.selector_tink_yellow));
                break;
            case 0x7:
                bottomNavigationView.setItemIconTintList(this.getColorStateList(R.color.selector_tink_orange));
                bottomNavigationView.setItemTextColor(this.getColorStateList(R.color.selector_tink_orange));
                break;
            case 0x8:
                bottomNavigationView.setItemIconTintList(this.getColorStateList(R.color.selector_tink_red));
                bottomNavigationView.setItemTextColor(this.getColorStateList(R.color.selector_tink_red));
                break;
        }
        refreshUI();
    }
    public void DealwithIntent(){

        if(source==null)
            source="";
        if(source.equals("settings")){
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
            fragmentManager = getSupportFragmentManager();
            final Fragment[] fragment = {null};
            final String[] tag = new String[1];
            //新建一个fragment事务来进行处理
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            tag[0] = THREE;
            if ((fragment[0] = findFragment(tag[0])) == null) {
                fragment[0] = new fragment_settings();
                fragmentTransaction.add(R.id.content_frame, fragment[0], tag[0]);
            }
            for (Fragment fragment1 : fragmentManager.getFragments()) {
                if (fragment[0] == fragment1) {
                    fragmentTransaction.show(fragment1);
                } else {
                    fragmentTransaction.hide(fragment1);
                }
            }
            fragmentTransaction.commitAllowingStateLoss();
        }else if(source.equals("tile")){
            moveTaskToBack(true);
        }
    }
    public Boolean CheckIsImageAndText(int position){
        if(position==1001) {
            sp = getSharedPreferences("Setting", MODE_PRIVATE);
            int temp = sp.getInt("NewsReadLocation", 0);
            j = temp;
            File file = new File(filePrefix + PicUrl + "/a" + temp + ".jpg");
            if (file.exists()) {
                sendNotification(Image_with_Text);
            } else {
                sendNotification(Only_Text);
            }
            return true;
        }
        return false;
    }
    public static String PositionToString(int position){
        switch (position){
            case 1000:
                return "图片";
            case 1001:
                return "新闻";
            case 1002:
                return "收藏";
            case 1003:
                return "游戏";
            case 1004:
                return "冥想";
            case 1005:
                return "";
            case 1006:
                return "";
            case 1007:
                return "";
            case 1008:
                return "阅读";
            case 1009:
                return "历史";
            case 1010:
                return "热搜";
            case 1011:
                return "段子";
            case 1012:
                return "便条";
            default:
                return "";
        }
    }
    public static int StringToPosition(String name){
        if(name.equals("新闻"))
            return 1001;
        else if(name.equals("历史"))
            return 1009;
        else if(name.equals("收藏"))
            return 1002;
        else if(name.equals("冥想"))
            return 1004;
        else if(name.equals("游戏"))
            return 1003;
        else if(name.equals("图片"))
            return 1000;
        else if(name.equals("阅读"))
            return 1008;
        else if(name.equals("热搜"))
            return 1010;
        else if(name.equals("段子"))
            return 1011;
        else if(name.equals("便条"))
            return 1012;
        else return 0;
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
    public String searchForPosition_DataMap(int month,int date){
        for(int i=0; i<history_DataMap.size();i++){
            if(Objects.equals(history_DataMap.get(i), month + "月" + date + "日：")){
                for(int j=0; j<20;j++){
                    if(Objects.equals(history_DataMap.get(i+j), "出生人物：")){
                        for(int k=0; k<20;k++){
                            if(Objects.equals(history_DataMap.get(i+j+k), "逝世人物：")){
                                for(int l=0; l<20;l++){
                                    if(Objects.equals(history_DataMap.get(i+j+k+l), month + "月" + (date+1) + "日：")){
                                        return i+"&"+j+"&"+k+"&"+l;
                                }
                            }

                        }
                    }

                    }
                }
            }
        }

        return "0&0";
    }
    public int searchForPosition_Festival(int month,int date) {
        for (int i = 0; i < history_FestivalMap.size(); i++) {
            if (Objects.equals(history_FestivalMap.get(i), "*"+month + "月" + date + "日")) {
                return i;
            }
        }
        return 0;
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static int dip2px(Context context, float dpValue) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
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
            file = new File(filePrefix + NewsContentUrlUrl);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = new File(filePrefix + PicUrl);
            file.mkdir();
            file = new File(filePrefix + WebUrl);
            file.mkdir();
            file = new File(filePrefix + SharedUrl);
            file.mkdir();
            file = new File(filePrefix + Shared_PassagesUrl);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = new File(filePrefix + Shared_SelectedUrl);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file = new File(filePrefix + Shared_OrderUrl);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                    Thread.sleep(200);
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
            while(true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(1);
            }
        }
    };
    public void refreshUI(){
        ThemeUtils.refreshUI(MainActivity.this, new ThemeUtils.ExtraRefreshable() {
                    @Override
                    public void refreshGlobal(Activity activity) {
                        //for global setting, just do once
                        final MainActivity context = MainActivity.this;
                        ActivityManager.TaskDescription taskDescription =
                                new ActivityManager.TaskDescription(null, null,
                                        ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary));
                        setTaskDescription(taskDescription);
                        switch (theme_color){
                            case 0x1:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.pink));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.pink));
                                break;
                            case 0x2:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.purple));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.purple));
                                break;
                            case 0x3:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.blue));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
                                break;
                            case 0x4:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.green));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.green));
                                break;
                            case 0x5:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.green_light));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.green_light));
                                break;
                            case 0x6:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.yellow));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.yellow));
                                break;
                            case 0x7:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.orange));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.orange));
                                break;
                            case 0x8:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.red));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.red));
                                break;
                        }

                    }

                    @Override
                    public void refreshSpecificView(View view) {
                        //TODO: will do this for each traversal
                    }
                }
        );
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if (searchView.isSearchOpen()) {
                searchView.closeSearch();
            } else {

            }
        }
        return false;
    }

    public void checkUpdateData() throws ParseException {
        sp=getSharedPreferences("Time", MODE_PRIVATE);
        int time=sp.getInt("UpdateTime",1);
        Calendar calendar = Calendar.getInstance();
        now = calendar.get(Calendar.DAY_OF_YEAR);
        SharedPreferences sp;
        sp=getSharedPreferences("Setting", MODE_PRIVATE);
        alwaysUpdateData=sp.getBoolean("Always_update",false);
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
                "请点击并设置提示音为“无”", NotificationManager.IMPORTANCE_HIGH);
        manger.createNotificationChannel(noti_id_saveData);
        RemoteViews view_id_0_saveData = new RemoteViews(getPackageName(), R.layout.notification_progressbar);
        view_id_0_saveData.setProgressBar(R.id.notification_progressbar_progressbar,100,saveData_Progress,false);
        view_id_0_saveData.setTextViewText(R.id.notification_progressbar_text,"正在更新每日内容，请稍后...(" + updateText+"%）");
        Notification notification_id_0_saveData = new NotificationCompat.Builder(this,"0")
                .setSmallIcon(R.drawable.icon)
                .setTicker("下载数据")
                .setOngoing(true)
                .setGroupSummary(false)
                .setSound(Uri.fromFile(new File(String.valueOf(getResources().openRawResourceFd(R.raw.no_sound)))))
                .setGroup("其它")
                .setOnlyAlertOnce(true)
                .setContent(view_id_0_saveData)//设置普通notification视图
                .setPriority(NotificationCompat.PRIORITY_HIGH)//设置最大优先级
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

        AVQuery<AVObject> query2 = new AVQuery<>("HotSearch_Baidu");
        query2.selectKeys(Arrays.asList("title", "url"));
        query2.limit(10);
        query2.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject avObject : list) {
                    HotSearch_Baidu.add(avObject.getString("title")+"hyd"+avObject.getString("url"));
                }
            }
        });

        AVQuery<AVObject> query3 = new AVQuery<>("HotSearch_Weibo");
        query3.selectKeys(Arrays.asList("title", "url"));
        query3.limit(10);
        query3.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject avObject : list) {
                    HotSearch_Weibo.add(avObject.getString("title")+"hyd"+"https://s.weibo.com"+avObject.getString("url"));
                }
            }
        });

        AVQuery<AVObject> query = new AVQuery<>("PengpaiNews");
        query.whereGreaterThanOrEqualTo("createdAt", getDateWithDateString(time));
        query.addAscendingOrder("priority");
        query.selectKeys(Arrays.asList("title", "subtitle","content","bitmapbytes","conurl"));
        query.limit(10);
        query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    for (AVObject avObject : list) {

                        ContentUrl.add(saveData_i , avObject.getString("conurl"));
                        NewsTitle.add(saveData_i , avObject.getString("title"));
                        NewsSubTitle.add(saveData_i, avObject.getString("subtitle"));
                        Content.add(saveData_i, avObject.getString("content"));
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
/*
    public void saveDataWebCache(){

        WebSettings webSettings = nullWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setTextZoom(100);
        nullWebView.setWebChromeClient(new WebChromeClient());
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setUserAgentString(webSettings.getUserAgentString());
        webSettings.setDatabaseEnabled(true);
        nullWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                    try {
                        Thread.sleep(1000);
                        webViewCacheNumber++;
                        saveDataWebCache();
                        if(webViewCacheNumber==9)
                            saveData2();
                    } catch (InterruptedException ignored) {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }
        });

        String temp[];
        temp=ContentUrl.get(webViewCacheNumber).split("www");
        url=temp[0]+"//m"+temp[1];
        nullWebView.loadUrl(url);


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            nullWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        nullWebView.getSettings().setBlockNetworkImage(false);



    }
*/
    public void saveData2(){
        Writer r1,r3,r4,r5,r6,r7 = null;
        try {
            r1 = new FileWriter(filePrefix + NewsTitleUrl, true);
            r3 = new FileWriter(filePrefix + NewsSubTitleUrl, true);
            r4 = new FileWriter(filePrefix + NewsContentUrl, true);
            r5 = new FileWriter(filePrefix + HotSearch_BaiduUrl, true);
            r6 = new FileWriter(filePrefix + HotSearch_WeiboUrl, true);
            r7 = new FileWriter(filePrefix + NewsContentUrlUrl, true);
            for (String aNewsTitle : NewsTitle) {
                r1.write(aNewsTitle + "\n");
            }
            r1.close();
            for (String aNewsSubTitle : NewsSubTitle) {
                r3.write(aNewsSubTitle + "\n");
            }
            r3.close();
            for (String aNewsContent : Content) {
                r4.write(aNewsContent + "\n");
            }
            r4.close();
            for (String aHotSearch_Baidu : HotSearch_Baidu) {
                r5.write(aHotSearch_Baidu + "\n");
            }
            r5.close();
            for (String aHotSearch_Weibo : HotSearch_Weibo) {
                r6.write(aHotSearch_Weibo + "\n");
            }
            r6.close();
            for (String aContentUrl : ContentUrl) {
                r7.write(aContentUrl + "\n");
            }
            r7.close();

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
        Content.clear();
        HotSearch_Baidu.clear();
        HotSearch_Weibo.clear();
        ContentUrl.clear();
        for(int temp=0; temp<NewsNum; temp++){
            NewsTitle.addAll(ReadTxtFile(filePrefix+NewsTitleUrl));
            NewsSubTitle.addAll(ReadTxtFile(filePrefix+NewsSubTitleUrl));
            Content.addAll(ReadTxtFile(filePrefix+NewsContentUrl));
            HotSearch_Weibo.addAll(ReadTxtFile(filePrefix+HotSearch_WeiboUrl));
            HotSearch_Baidu.addAll(ReadTxtFile(filePrefix+HotSearch_BaiduUrl));
            ContentUrl.addAll(ReadTxtFile(filePrefix+NewsContentUrlUrl));
        }
    }

    public void startFloatingWindow(int number){
    Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    MainActivity.this.sendBroadcast(it);
       String url="";
        switch (number){
            case 0://加载更多
                String temp[];
                temp=ContentUrl.get(j).split("www");
                url=temp[0]+"//m"+temp[1];
                break;
            case 1:
                switch (k){
                    case 0:
                        url=HotSearch_Weibo.get(0).split("hyd")[1];
                        break;
                    case 1:
                        url=HotSearch_Weibo.get(5).split("hyd")[1];
                        break;
                    case 2:
                        url=HotSearch_Baidu.get(0).split("hyd")[1];
                        break;
                    case 3:
                        url=HotSearch_Baidu.get(5).split("hyd")[1];
                        break;
                }
                break;
            case 2:
                switch (k){
                    case 0:
                        url=HotSearch_Weibo.get(1).split("hyd")[1];
                        break;
                    case 1:
                        url=HotSearch_Weibo.get(6).split("hyd")[1];
                        break;
                    case 2:
                        url=HotSearch_Baidu.get(1).split("hyd")[1];
                        break;
                    case 3:
                        url=HotSearch_Baidu.get(6).split("hyd")[1];
                        break;
                }
                break;
            case 3:
                switch (k){
                    case 0:
                        url=HotSearch_Weibo.get(2).split("hyd")[1];
                        break;
                    case 1:
                        url=HotSearch_Weibo.get(7).split("hyd")[1];
                        break;
                    case 2:
                        url=HotSearch_Baidu.get(2).split("hyd")[1];
                        break;
                    case 3:
                        url=HotSearch_Baidu.get(7).split("hyd")[1];
                        break;
                }
                break;
            case 4:
                switch (k){
                    case 0:
                        url=HotSearch_Weibo.get(3).split("hyd")[1];
                        break;
                    case 1:
                        url=HotSearch_Weibo.get(8).split("hyd")[1];
                        break;
                    case 2:
                        url=HotSearch_Baidu.get(3).split("hyd")[1];
                        break;
                    case 3:
                        url=HotSearch_Baidu.get(8).split("hyd")[1];
                        break;
                }
                break;
            case 5:
                switch (k){
                    case 0:
                        url=HotSearch_Weibo.get(4).split("hyd")[1];
                        break;
                    case 1:
                        url=HotSearch_Weibo.get(9).split("hyd")[1];
                        break;
                    case 2:
                        url=HotSearch_Baidu.get(4).split("hyd")[1];
                        break;
                    case 3:
                        url=HotSearch_Baidu.get(9).split("hyd")[1];
                        break;
                }
                break;
        }
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "请授权显示悬浮窗", Toast.LENGTH_SHORT);
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
        } else {
            if(k==3||k==2){
                String temp[];
                temp=url.split("www");
                url=temp[0]+"//m"+temp[1];
            }
            if(current_Category==1001 || current_Category==1007){
                sp = getSharedPreferences("Temp", MODE_PRIVATE);
                SharedPreferences.Editor edit3 = sp.edit();
                edit3.putString("HotSearch_Url", url);
                edit3.apply();
                Intent intent = new Intent(MainActivity.this, FloatingService.class);
                startService(intent);
            }else if(current_Category==1003) {
                sp = getSharedPreferences("Temp", MODE_PRIVATE);
                SharedPreferences.Editor edit3 = sp.edit();
                edit3.putString("HotSearch_Url", url);
                edit3.apply();
                Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FloatingService.class);
                startService(intent);
            }
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
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE );
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
                case 1:
                    if(slidingRootNav.isMenuOpened()) {
                        if(state!=1)
                        getWindow().setStatusBarColor(
                                ThemeUtils.getColorById(MainActivity.this, R.color.ivory_white));
                        state = 1;
                    }else {
                        if(state!=0)
                        getWindow().setStatusBarColor(
                                ThemeUtils.getColorById(MainActivity.this, R.color.pink));
                        state = 0;
                    }
                    break;
            }
        }
    };
}
