package hyd.modengxian.fragment;

import com.bumptech.glide.Glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huxq17.handygridview.HandyGridView;
import com.huxq17.handygridview.listener.IDrawer;
import com.huxq17.handygridview.listener.OnItemCapturedListener;
import com.ocnyang.pagetransformerhelp.BannerItemBean;
import com.ocnyang.pagetransformerhelp.BannerViewPager;
import com.ocnyang.pagetransformerhelp.ImageLoaderInterface;
import com.ocnyang.pagetransformerhelp.transformer.ParallaxTransformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hyd.modengxian.MainActivity;
import hyd.modengxian.R;
import hyd.modengxian.service.FloatingService_Favourite;
import hyd.modengxian.utils.DensityUtil;
import hyd.modengxian.utils.GridViewAdapter;
import hyd.modengxian.utils.MyPagerAdapter;

public class fragment_home extends Fragment
        implements View.OnClickListener{

    View view;
    private HandyGridView mGridView;
    private List<String> strList;
    private TextView enableSelectorTv, changeModeTv, addTagTv;
    private Button recoveryTagTv;
    private GridViewAdapter adapter;
    private ViewGroup outLayout;
    private static SharedPreferences sp;
    int textWidth;
    int textHeight;
    StaticLayout tipsLayout;
    private TextPaint mTextPaint;
    private Button home_expand;
    private boolean isOpen=false;

    private ViewPager mViewPager;
    private CardView cardView;
    private static TextView normal1;
    private static TextView normal2;
    private static TextView normal3;
    private static TextView normal4;

    private TextView t1,t2,move_txt,sure;
    private CardView card3;
    private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,img10,img11,img12;
    private HorizontalScrollView scr1,scr2,scr3,scr4;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //避免同一activity下 多个fragment 切换时重复执行onCreateView方法
        // Fragment之间切换时每次都会调用onCreateView方法，导致每次Fragment的布局都重绘，无法保持Fragment原有状态。
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        }
        sp = getActivity().getSharedPreferences("Preference", Context.MODE_PRIVATE);
        cardView=view.findViewById(R.id.home_cardview3);
        normal1=view.findViewById(R.id.Normal_1);
        normal2=view.findViewById(R.id.Normal_2);
        normal3=view.findViewById(R.id.Normal_3);
        normal4=view.findViewById(R.id.Normal_4);
        sure = view.findViewById(R.id.home_sure);

        img1=view.findViewById(R.id.home_ti1);
        img2=view.findViewById(R.id.home_ti2);
        img3=view.findViewById(R.id.home_ti3);
        img4=view.findViewById(R.id.home_ti4);
        img5=view.findViewById(R.id.home_ti5);
        img6=view.findViewById(R.id.home_ti6);
        img7=view.findViewById(R.id.home_ti7);
        img8=view.findViewById(R.id.home_ti8);
        img9=view.findViewById(R.id.home_ti9);
        img10=view.findViewById(R.id.home_ti10);
        img11=view.findViewById(R.id.home_ti11);
        img12=view.findViewById(R.id.home_ti12);

        img1.setOnClickListener(new OnClickEvent());
        img2.setOnClickListener(new OnClickEvent());
        img3.setOnClickListener(new OnClickEvent());
        img4.setOnClickListener(new OnClickEvent());
        img5.setOnClickListener(new OnClickEvent());
        img6.setOnClickListener(new OnClickEvent());
        img7.setOnClickListener(new OnClickEvent());
        img8.setOnClickListener(new OnClickEvent());
        img9.setOnClickListener(new OnClickEvent());
        img10.setOnClickListener(new OnClickEvent());
        img11.setOnClickListener(new OnClickEvent());
        img12.setOnClickListener(new OnClickEvent());

        scr1 = view.findViewById(R.id.home_rela_ti_1);
        scr2 = view.findViewById(R.id.home_rela_ti_2);
        scr3 = view.findViewById(R.id.home_rela_ti_3);
        scr4 = view.findViewById(R.id.home_rela_ti_4);

        Handler handler = new Handler();
        handler.postDelayed(runnable, 2000);

        t1=view.findViewById(R.id.home_line1);
        t2=view.findViewById(R.id.home_line2);
        t1.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
        t2.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
        card3=view.findViewById(R.id.home_cardview3);
        move_txt=view.findViewById(R.id.home_move_txt);
        home_expand=view.findViewById(R.id.home_expand);
        home_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpen) {
                    ViewGroup.LayoutParams para1;
                    para1 = cardView.getLayoutParams();
                    para1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    cardView.setLayoutParams(para1);
                    home_expand.setRotation(180);
                    move_txt.setVisibility(View.VISIBLE);
                    sure.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                    sure.setVisibility(View.VISIBLE);
                }else{
                    ViewGroup.LayoutParams para1;
                    para1 = cardView.getLayoutParams();
                    para1.height = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));;
                    cardView.setLayoutParams(para1);
                    home_expand.setRotation(0);
                    move_txt.setVisibility(View.GONE);
                    sure.setVisibility(View.GONE);
                }
                isOpen=!isOpen;

                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent();
                        intent.setClass(getActivity(),MainActivity.class);
                        getActivity().startActivity(intent);
                    }
                });
            }
        });
        card3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(!isOpen) {
                        ViewGroup.LayoutParams para1;
                        para1 = cardView.getLayoutParams();
                        para1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        cardView.setLayoutParams(para1);
                        home_expand.setRotation(180);
                        move_txt.setVisibility(View.VISIBLE);
                    }else{
                        ViewGroup.LayoutParams para1;
                        para1 = cardView.getLayoutParams();
                        para1.height = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));;
                        cardView.setLayoutParams(para1);
                        home_expand.setRotation(0);
                        move_txt.setVisibility(View.GONE);
                    }
                    isOpen=!isOpen;
                }
                return false;
            }
        });
        setText();
        initData();
        initView();
        setVp();
        mViewPager =  view.findViewById(R.id.viewPager);
        mViewPager.setPageTransformer(true,new ParallaxTransformer());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {

            scr1.smoothScrollBy(50,0);
            scr2.smoothScrollBy(150,0);
            scr3.smoothScrollBy(0,0);
            scr4.smoothScrollBy(80,0);


        }
    };

    private void initView() {
        enableSelectorTv = view.findViewById(R.id.text_enable_selector);
        changeModeTv = view.findViewById(R.id.text_change_mode);
        addTagTv = view.findViewById(R.id.text_add_tag);
        recoveryTagTv = view.findViewById(R.id.text_recovery_tag);
        outLayout = view.findViewById(R.id.out_layout);

        mGridView = view.findViewById(R.id.grid_tips);
        adapter = new GridViewAdapter(getActivity(), strList);
        mGridView.setAdapter(adapter);

        setMode(HandyGridView.MODE.LONG_PRESS);

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mGridView.isTouchMode()&&!mGridView.isNoneMode() && !adapter.isFixed(position)) {//long press enter edit mode.
                    setMode(HandyGridView.MODE.TOUCH);
                    return true;
                }
                return false;
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // Toast.makeText(getActivity(), "click item at " + position, Toast.LENGTH_SHORT).show();
            }
        });
        mGridView.setOnItemCapturedListener(new OnItemCapturedListener() {
            @Override
            public void onItemCaptured(View v, int position) {
                v.setScaleX(1.5f);
                v.setScaleY(1.5f);
            }

            @Override
            public void onItemReleased(View v, int position) {
                v.setScaleX(1f);
                v.setScaleY(1f);
            }

        });
        mGridView.setDrawer(new IDrawer() {
            @Override
            public void onDraw(Canvas canvas, int width, int height) {
                if (!mGridView.isNoneMode()) {
                    int offsetX = -DensityUtil.dip2px(getActivity(), 10);
                    int offsetY = -DensityUtil.dip2px(getActivity(), 10);
                    //文字绘制于gridview的右下角，并向左，向上偏移10dp。
                    drawTips(canvas, width + offsetX, height + offsetY);
                }
            }
        },false);
        enableSelectorTv.setOnClickListener(this);
//        enableSelectorTv.performClick();
        changeModeTv.setOnClickListener(this);
        addTagTv.setOnClickListener(this);
        recoveryTagTv.setOnClickListener(this);
    }

    private void drawTips(Canvas canvas, int width, int height) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.text_enable_selector:
                boolean isSelectorEnabled = mGridView.isSelectorEnabled();
                //建议传入false,不要开启selector,不然item移动以后，在item原来的位置会有残影逐渐消失的效果。
                //默认是false。
                mGridView.setSelectorEnabled(!isSelectorEnabled);
                enableSelectorTv.setText(!isSelectorEnabled ? "selector已开启" : "selector已关闭");
                break;
            case R.id.text_change_mode:
                HandyGridView.MODE curMode = mGridView.getMode();
                int index = HandyGridView.MODE.indexOf(curMode);
                index = (index + 1) % HandyGridView.MODE.values().length;
                //有三种模式分别是点击拖动，长按拖动和不拖动，分别对应的是TOUCH,LONG_PRESS和NONE
                setMode(HandyGridView.MODE.get(index));
                break;
            case R.id.text_add_tag:
                addTag();
                break;
            case R.id.text_recovery_tag:
//                moveTaskToBack(true);
                recoveryTag();
                break;
        }
    }

    private void recoveryTag() {
        for (Iterator<String> iter = strList.iterator(); iter.hasNext(); ) {
            String str = iter.next();
            if (str.contains("更多")) {
                iter.remove();
            }
        }
        setMode(HandyGridView.MODE.LONG_PRESS);
        adapter.setData(strList);
        outLayout.setClipChildren(false);
    }

    private void addTag() {
        for (int i = 0; i < 4; i++) {
            strList.add("更多 "+(strList.size()-1));
        }
        adapter.setData(strList);
        outLayout.setClipChildren(true);
        mGridView.smoothScrollToPosition(adapter.getCount() - 1);
    }

    private void setMode(HandyGridView.MODE mode) {
        mGridView.setMode(mode);
        changeModeTv.setText(mode.toString());
        adapter.setInEditMode(mode == HandyGridView.MODE.TOUCH);
    }


    public void initData() {
        strList = new ArrayList<>();

        if(sp.getInt("Position1",0)==0) {
            strList.add("新闻");
            strList.add("历史");
            strList.add("收藏");
            strList.add("冥想");
            strList.add("游戏");
            strList.add("图片");
            strList.add("阅读");
            strList.add("热搜");
            strList.add("段子");
            strList.add("便条");
        }else{
            strList.add(MainActivity.PositionToString(sp.getInt("Position1",0)));
            strList.add(MainActivity.PositionToString(sp.getInt("Position2",0)));
            strList.add(MainActivity.PositionToString(sp.getInt("Position3",0)));
            strList.add(MainActivity.PositionToString(sp.getInt("Position4",0)));
            if(!strList.contains("新闻"))
                strList.add("新闻");
            if(!strList.contains("历史"))
                strList.add("历史");
            if(!strList.contains("收藏"))
                strList.add("收藏");
            if(!strList.contains("冥想"))
                strList.add("冥想");
            if(!strList.contains("游戏"))
                strList.add("游戏");
            if(!strList.contains("图片"))
                strList.add("图片");
            if(!strList.contains("阅读"))
                strList.add("阅读");
            if(!strList.contains("热搜"))
                strList.add("热搜");
            if(!strList.contains("段子"))
                strList.add("段子");
            if(!strList.contains("便条"))
                strList.add("便条");
        }
    }

    private void setVp() {
        List<String> list = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        list.add("这是莫等闲测试版Alpha0.1");
        list.add("UI界面已优化");
        list.add("部分功能还处于演示阶段");
        list.add("拓展支持的应用");
        list.add("现在还不能进行支付哦");

        list2.add(R.drawable.img0);
        list2.add(R.drawable.img1);
        list2.add(R.drawable.img2);
        list2.add(R.drawable.img3);
        list2.add(R.drawable.img4);

        ViewPager vp = (ViewPager) view.findViewById(R.id.viewPager);
        vp.setAdapter(new MyPagerAdapter(getActivity(),list,list2));
    }

    public static void setText(){

        normal1.setText(MainActivity.PositionToString(sp.getInt("Position1",0)));
        normal2.setText(MainActivity.PositionToString(sp.getInt("Position2",0)));
        normal3.setText(MainActivity.PositionToString(sp.getInt("Position3",0)));
        normal4.setText(MainActivity.PositionToString(sp.getInt("Position4",0)));
    }


    private class OnClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.home_ti1:
                    break;
                case R.id.home_ti2:
                    break;
                case R.id.home_ti3:
                    break;
                case R.id.home_ti4:
                    break;
                case R.id.home_ti5:
                    break;
                case R.id.home_ti6:
                    break;
                case R.id.home_ti7:
                    break;
                case R.id.home_ti8:
                    break;
                case R.id.home_ti9:
                    break;
                case R.id.home_ti10:
                    break;
                case R.id.home_ti11:
                    break;
                case R.id.home_ti12:
                    break;
            }

        }
    }
}
