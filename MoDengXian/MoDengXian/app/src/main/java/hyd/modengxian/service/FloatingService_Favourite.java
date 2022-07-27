package hyd.modengxian.service;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import hyd.modengxian.R;
import hyd.modengxian.entity.MultipleItem;
import hyd.modengxian.utils.MultipleItemQuickAdapter;

public class FloatingService_Favourite extends Service  {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Button button;
    private String url;
    private WebView webView;
    private ImageView quit,move,small,hide,img;

    private View mDesktopLayout;
    private int height,width;
    View view;
    private TextView t1,t2,t3,t4,t5;
    private static final int ALL = 0, PASSAGE = 1, SELECTED = 2, PICTURES = 3, FILE = 4;
    RecyclerView mRecyclerView;
    private File filePrefix = Environment.getExternalStorageDirectory();
    private String Shared_PassagesUrl = "/MoDengXianData/Passages.txt";
    private String Shared_SelectedUrl = "/MoDengXianData/Selected.txt";
    private String Shared_OrderUrl = "/MoDengXianData/Order.txt";
    static Map<Integer, String> Passages;
    static Map<Integer, String> Selected;
    static Map<Integer, String> Pictures;
    static Map<Integer, String> Files;
    static Map<Integer, String> Order;
    static Map<Integer, String> Urls = new HashMap<Integer, String>();;

    private List<MultipleItem> data;

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
            layoutParams.height = height*95/100;
            layoutParams.x = 0;
            layoutParams.y = 0;

            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mDesktopLayout = inflater.inflate(R.layout.floating_window_favourite, null);



          

            ReadData();

            quit = mDesktopLayout.findViewById(R.id.floating_window_quit);/*
            img = mDesktopLayout.findViewById(R.id.plain_img);
            content = mDesktopLayout.findViewById(R.id.plain_text1);
*/

            final SharedPreferences sp=getSharedPreferences("Temp", MODE_PRIVATE);


            quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    windowManager.removeView(mDesktopLayout);
                    onDestroy();
                    stopSelf();
                }
            });

            t1 = mDesktopLayout.findViewById(R.id.f_tab_1);
            t2 = mDesktopLayout.findViewById(R.id.f_tab_2);
            t3 = mDesktopLayout.findViewById(R.id.f_tab_3);
            t4 = mDesktopLayout.findViewById(R.id.f_tab_4);
            t5 = mDesktopLayout.findViewById(R.id.f_tab_5);
            t1.setTextColor(getResources().getColor(R.color.blue_doder,null));

            windowManager.addView(mDesktopLayout, layoutParams);
            showFragment(ALL);

            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragment(ALL);
                    t1.setTextColor(getResources().getColor(R.color.blue_doder,null));
                    t2.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t3.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t4.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t5.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                }
            });
            t2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragment(PASSAGE);
                    t2.setTextColor(getResources().getColor(R.color.blue_doder,null));
                    t1.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t3.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t4.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t5.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                }
            });
            t3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragment(SELECTED);
                    t3.setTextColor(getResources().getColor(R.color.blue_doder,null));
                    t2.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t1.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t4.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t5.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                }
            });
            t4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragment(PICTURES);
                    t4.setTextColor(getResources().getColor(R.color.blue_doder,null));
                    t2.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t3.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t1.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t5.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                }
            });
            t5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragment(FILE);
                    t5.setTextColor(getResources().getColor(R.color.blue_doder,null));
                    t2.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t3.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t4.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                    t1.setTextColor(getResources().getColor(R.color.text_primary_color,null));
                }
            });


        }
    }

    public void showFragment(int category){
        ReadData();
        mRecyclerView = (RecyclerView) mDesktopLayout.findViewById(R.id.rv);
        switch (category){
            case ALL:
                data = getMultipleItemData_All();
                break;
            case PASSAGE:
                data = getMultipleItemData_Shared();
                break;
            case SELECTED:
                data = getMultipleItemData_Selected();
                break;
            case PICTURES:
                data = getMultipleItemData_Pictures();
                break;
            case FILE:
                data = getMultipleItemData_Files();
                break;
            default:
                //data = getMultipleItemData();
                break;
        }

        final MultipleItemQuickAdapter multipleItemAdapter = new MultipleItemQuickAdapter(this, data);
        final GridLayoutManager manager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(manager);
        multipleItemAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
        multipleItemAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM );
        mRecyclerView.setAdapter(multipleItemAdapter);

        multipleItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Patterns.WEB_URL.matcher(Objects.requireNonNull(Urls.get(position))).matches() || URLUtil.isValidUrl(Urls.get(position))) {
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("Temp", MODE_PRIVATE);
                    SharedPreferences.Editor edit3 = sp.edit();
                    edit3.putString("HotSearch_Url", Urls.get(position));
                    edit3.apply();
                    Intent intent = new Intent(FloatingService_Favourite.this, FloatingService.class);
                    getApplicationContext().startService(intent);
                }else{
                    final SharedPreferences sp=getApplicationContext().getSharedPreferences("Temp", MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("Content",Urls.get(position));
                    edit.commit();
                    Intent intent = new Intent(FloatingService_Favourite.this, FloatingService_Plain.class);
                    getApplicationContext().startService(intent);
                }
            }
        });

        multipleItemAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                return false;
            }
        });
    }

    public void ReadData(){
        Passages = Txt(filePrefix+Shared_PassagesUrl);
        Selected = Txt(filePrefix+Shared_SelectedUrl);
        Order = Txt(filePrefix+Shared_OrderUrl);
    }

    public static List<MultipleItem> getMultipleItemData_Shared() {
        List<MultipleItem> list = new ArrayList<>();

        int Passages_size = Passages.size();
        //list.add(new MultipleItem(MultipleItem.IMG, MultipleItem.IMG_SPAN_SIZE, R.drawable.icon_home));
        for(int i=0;i<Passages_size;i++) {
            if(Passages.get(Passages_size-i-1).contains("分享自知乎网")&&Passages.get(Passages_size-i-1).contains("】")&&Passages.get(Passages_size-i-1).contains("…")) {
                Urls.put(i,("http"+Passages.get(Passages_size-i-1).split(" http")[1]).split(" （分享")[0]);
                list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Passages.get(Passages_size-i-1).split(" http")[0].split("【")[1].split("】")[0], "来自"+Passages.get(Passages_size-i-1).split("】")[1].split("：…")[0]+"的回答", R.drawable.icon_zhihu,"知乎"));
            }else if(Passages.get(Passages_size-i-1).contains("分享自知乎网")) {
                Urls.put(i,("http"+Passages.get(Passages_size-i-1).split(" http")[1]).split(" （分享")[0]);
                list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Passages.get(Passages_size-i-1).split(" http")[0], "话题", R.drawable.icon_zhihu,"知乎"));
            } else if(Passages.get(Passages_size-i-1).contains("UP主: ")) {
                Urls.put(i,"http"+Passages.get(Passages_size-i-1).split(" http")[1]);
                list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Passages.get(Passages_size-i-1).split("UP主: ")[0], ("UP主:"+Passages.get(Passages_size-i-1).split(" UP主: ")[1]).split(" http")[0] , R.drawable.icon_bilibili,"哔哩哔哩"));
            }else if(Passages.get(Passages_size-i-1).contains("www.bilibili.com")) {
                Urls.put(i,"http"+Passages.get(Passages_size-i-1).split(" http")[1]);
                list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Passages.get(Passages_size-i-1).split(" https:")[0], "文章", R.drawable.icon_bilibili,"哔哩哔哩"));
            } else if(Passages.get(Passages_size-i-1).contains("★")||Passages.get(Passages_size-i-1).contains("☆")) {
            Urls.put(i,"http"+Passages.get(Passages_size-i-1).split("http")[1]);
            list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Passages.get(Passages_size-i-1).split("@hyd&")[0], Passages.get(Passages_size-i-1).split("@hyd&")[1].split(",")[0], R.drawable.icon_dazhongdianping,"大众点评"));
        }

        }
        //list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE,"这是标题",R.drawable.icon_home));
        //list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN,"这是标题",R.drawable.icon_home));
        //list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN,"这是标题",R.drawable.icon_home));

        if(Passages.size()==0){
            list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE,"这里是 分享 界面",R.mipmap.choose2));
            list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE,"在任意应用分享内容到莫等闲","您可以随时在通知栏找到他们",R.mipmap.head_img1,""));
            list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE,"下面是分享的几个示例",R.mipmap.choose));
            list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE,"精灵宝可梦剑与盾分析：从标题透露出的全新武装宝可梦？","UP主：你好啊我是谁",R.drawable.icon_bilibili,"哔哩哔哩"));
            list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE,"如何看待焦越清华考研笔试第一复试被刷后称被[性别歧视]？","来自菜鸡的回答",R.drawable.icon_zhihu,"知乎"));

        }
        return list;
    }

    public static List<MultipleItem> getMultipleItemData_Selected() {
        List<MultipleItem> list = new ArrayList<>();
        String packagename;
        int Selected_size = Selected.size();
        //list.add(new MultipleItem(MultipleItem.IMG, MultipleItem.IMG_SPAN_SIZE, R.drawable.icon_home));
        for(int i=0;i<Selected_size;i++) {
            Urls.put(i, Selected.get(Selected_size - i - 1).split("？hyd？")[0].replace("@&hyd","\n"));
            if(Selected.get(Selected_size - i - 1).contains("Tim"))
                list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Selected.get(Selected_size - i - 1).split("？hyd？")[0].replace("@&hyd","\n"), "来自" + Selected.get(Selected_size - i - 1).split("？hyd？")[1], R.drawable.icon_tim, Selected.get(Selected_size - i - 1).split("？hyd？")[2]));
            else if(Selected.get(Selected_size - i - 1).contains("微信"))
                list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Selected.get(Selected_size - i - 1).split("？hyd？")[0].replace("@&hyd","\n"), "来自" + Selected.get(Selected_size - i - 1).split("？hyd？")[1], R.drawable.icon_wechat, Selected.get(Selected_size - i - 1).split("？hyd？")[2]));
            else if(Selected.get(Selected_size - i - 1).contains("谷歌"))
                list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Selected.get(Selected_size - i - 1).split("？hyd？")[0].replace("@&hyd","\n"), "来自" + Selected.get(Selected_size - i - 1).split("？hyd？")[1], R.drawable.icon_chrome, Selected.get(Selected_size - i - 1).split("？hyd？")[2]));
            else
                list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Selected.get(Selected_size - i - 1).split("？hyd？")[0].replace("@&hyd","\n"), "来自" + Selected.get(Selected_size - i - 1).split("？hyd？")[1], R.drawable.icon_text, Selected.get(Selected_size - i - 1).split("？hyd？")[2]));

        }

        if(Selected_size==0){
            list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE,"这里是 选择 界面",R.mipmap.choose3));
            list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE,"将任何应用的一段话、一则消息、一句美文记录在这里","您可以方便的在通知栏随时查阅、回忆",R.mipmap.head_img0,""));
            list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE,"步骤1","点我开启无障碍功能",R.drawable.number1,""));
            list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE,"步骤2","点我查看使用教程",R.drawable.number2,""));
            list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE,"步骤3","仅需点击一段文字，即可添加至本收藏",R.drawable.number3,""));
            list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE,"下面是选择的两个示例",R.mipmap.choose4));
            list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE,"卑微的皮卡丘：别忘了今天打太极！！","来自Tim",R.drawable.icon_text,"2019-3-19"));
            list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE,"G9-班长-zrm：@全体成员 麻烦各班宣传委员将班会课新闻稿发给我，谢谢。","来自微信",R.drawable.icon_text,"2019-3-17"));

        }
        //list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE,"这是标题",R.drawable.icon_home));
        //list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN,"这是标题",R.drawable.icon_home));
        //list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN,"这是标题",R.drawable.icon_home));


        return list;
    }

    public static List<MultipleItem> getMultipleItemData_Files() {
        List<MultipleItem> list = new ArrayList<>();
        list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE,"该项还在开发中哦...敬请期待",R.mipmap.header_background));
        list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE,"最近的文件","在这里您可以查阅最近接收的文件",R.mipmap.click_head_img_0,"2019-5-20"));
        list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN,"莫等闲.pdf",R.mipmap.animation_img1));
        list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE_MIN,"惠普创业沙拉.doc",R.mipmap.animation_img2));
        return list;
    }

    public static List<MultipleItem> getMultipleItemData_Pictures() {
        List<MultipleItem> list = new ArrayList<>();
        list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE,"该项还在开发中哦...敬请期待",R.mipmap.header_background));
        list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE,"最近的图片","您可以在这里收藏表情、记录美好瞬间~",R.mipmap.head_img2,""));
        list.add(new MultipleItem(MultipleItem.IMG, MultipleItem.IMG_SPAN_SIZE,R.mipmap.click_head_img_0));
        list.add(new MultipleItem(MultipleItem.IMG, MultipleItem.IMG_SPAN_SIZE,R.mipmap.click_head_img_1));
        list.add(new MultipleItem(MultipleItem.IMG, MultipleItem.IMG_SPAN_SIZE,R.mipmap.click_head_img_0));
        return list;
    }

    public static List<MultipleItem> getMultipleItemData_All() {
        List<MultipleItem> list = new ArrayList<>();

        int Passages_size = Passages.size();
        int Selected_size = Selected.size();
        int passage_times=0,selected_times=0;

        for(int i=0;i<(Passages_size+Selected_size);i++) {
            if(Order.get(Passages_size+Selected_size-i-1).equals("1")) {
                if (Passages.get(Passages_size - passage_times - 1).contains("分享自知乎网") && Passages.get(Passages_size - passage_times - 1).contains("】") && Passages.get(Passages_size - passage_times - 1).contains("…")) {
                    Urls.put(passage_times+selected_times, ("http" + Passages.get(Passages_size - passage_times - 1).split(" http")[1]).split(" （分享")[0]);
                    list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Passages.get(Passages_size - passage_times - 1).split(" http")[0].split("【")[1].split("】")[0], "来自" + Passages.get(Passages_size - passage_times - 1).split("】")[1].split("：…")[0] + "的回答", R.drawable.icon_zhihu, "知乎"));
                } else if (Passages.get(Passages_size - passage_times - 1).contains("分享自知乎网")) {
                    Urls.put(passage_times+selected_times, ("http" + Passages.get(Passages_size - passage_times - 1).split("http")[1]).split(" （分享")[0]);
                    list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Passages.get(Passages_size - passage_times - 1).split(" http")[0], "话题", R.drawable.icon_zhihu, "知乎"));
                } else if (Passages.get(Passages_size - passage_times - 1).contains("UP主: ")) {
                    Urls.put(passage_times+selected_times, "http" + Passages.get(Passages_size - passage_times - 1).split("http")[1]);
                    list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Passages.get(Passages_size - passage_times - 1).split("UP主: ")[0], ("UP主:" + Passages.get(Passages_size - passage_times - 1).split(" UP主: ")[1]).split(" http")[0], R.drawable.icon_bilibili, "哔哩哔哩"));
                } else if (Passages.get(Passages_size - passage_times - 1).contains("www.bilibili.com")) {
                    Urls.put(passage_times+selected_times, "http" + Passages.get(Passages_size - passage_times - 1).split(" http")[1]);
                    list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Passages.get(Passages_size - passage_times - 1).split(" https:")[0], "文章", R.drawable.icon_bilibili, "哔哩哔哩"));
                }else if(Passages.get(Passages_size-passage_times-1).contains("★")||Passages.get(Passages_size-passage_times-1).contains("☆")) {
                    Urls.put(passage_times+selected_times,"http"+Passages.get(Passages_size-passage_times-1).split("http")[1]);
                    list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Passages.get(Passages_size-passage_times-1).split("@hyd&")[0], Passages.get(Passages_size-passage_times-1).split("@hyd&")[1].split(",")[0], R.drawable.icon_dazhongdianping,"大众点评"));
                }
                passage_times++;
            }else if(Order.get(Passages_size+Selected_size-i-1).equals("2")) {
                Urls.put(selected_times+passage_times, Selected.get(Selected_size - selected_times - 1).split("？hyd？")[0].replace("@&hyd","\n"));
                if(Selected.get(Selected_size - selected_times - 1).contains("Tim"))
                    list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Selected.get(Selected_size - selected_times - 1).split("？hyd？")[0].replace("@&hyd","\n"), "来自" + Selected.get(Selected_size - selected_times - 1).split("？hyd？")[1], R.drawable.icon_tim, Selected.get(Selected_size - selected_times - 1).split("？hyd？")[2]));
                else if(Selected.get(Selected_size - selected_times - 1).contains("微信"))
                    list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Selected.get(Selected_size - selected_times - 1).split("？hyd？")[0].replace("@&hyd","\n"), "来自" + Selected.get(Selected_size - selected_times - 1).split("？hyd？")[1], R.drawable.icon_wechat, Selected.get(Selected_size - selected_times - 1).split("？hyd？")[2]));
                else if(Selected.get(Selected_size - selected_times - 1).contains("谷歌"))
                    list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Selected.get(Selected_size - selected_times - 1).split("？hyd？")[0].replace("@&hyd","\n"), "来自" + Selected.get(Selected_size - selected_times - 1).split("？hyd？")[1], R.drawable.icon_chrome, Selected.get(Selected_size - selected_times - 1).split("？hyd？")[2]));
                else
                    list.add(new MultipleItem(MultipleItem.TEXT, MultipleItem.TEXT_SPAN_SIZE, Selected.get(Selected_size - selected_times - 1).split("？hyd？")[0].replace("@&hyd","\n"), "来自" + Selected.get(Selected_size - selected_times - 1).split("？hyd？")[1], R.drawable.icon_text, Selected.get(Selected_size - selected_times - 1).split("？hyd？")[2]));
                selected_times++;
            }

        }



        if(Selected.size()==0&&Passages.size()==0){
            list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE,"这里还没有内容哦",R.mipmap.animal));
            list.add(new MultipleItem(MultipleItem.IMG_TEXT, MultipleItem.IMG_TEXT_SPAN_SIZE,"您可以先看看“分享”选项卡",R.mipmap.animal2));
        }
        return list;
    }



    public Map<Integer, String> Txt(String filePath) {
        //将读出来的一行行数据使用Map存储
        Map<Integer, String> map = new HashMap<Integer, String>();
        try {
            int count = 0;//初始化 key值
            FileInputStream file = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(file);
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