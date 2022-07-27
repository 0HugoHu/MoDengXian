package hyd.modengxian.service;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Person;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hyd.modengxian.MainActivity;
import hyd.modengxian.R;
import me.codeboy.android.aligntextview.AlignTextView;

public class FloatingService_Sliding_Puzzle extends Service {
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Button button;
    private String url;
    private ImageView quit,move,small,hide;
    private View mDesktopLayout;
    private int height,width;
    private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,img0,img00;
    private List<Integer> number=new ArrayList<>();
    private int zeroNum;
    private View touchView;
    float mPosX=0,mPosY=0,mCurPosX=0,mCurPosY=0;
    private boolean animation_isFinish=false;
    private boolean isFinish=false;
    private boolean canStartAnimation=true;
    private int[] realPosition={0,1,2,3,4,5,6,7,8,9};
    private float distance;
    private int footsteps=0,time;
    private TextView txt_footstep,txt_time;
    private Button btn_reset;
    private Handler mhandle = new Handler();
    private boolean isPause = false;//是否暂停
    private long currentSecond = 0;//当前毫秒数
    private boolean isReady=false;
    private Thread mTimeRunnable = new Thread() {
        @Override
        public void run() {
            if(isFinish)
                mTimeRunnable.interrupt();
            else
            currentSecond = currentSecond + 1000;
            if(isReady) {
                if (currentSecond / 60000 == 0)
                    txt_time.setText("用时：" + (currentSecond % 60000) / 1000 + "秒");
                else
                    txt_time.setText("用时：" + currentSecond / 60000 + "分" + (currentSecond % 60000) / 1000 + "秒");
            }
                if (!isPause) {
                    //递归调用本runable对象，实现每隔一秒一次执行任务
                    mhandle.postDelayed(this, 1000);
                }

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sp=getSharedPreferences("Temp", MODE_PRIVATE);
        url=sp.getString("Full_Passage","");

        mTimeRunnable.start();
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
            mDesktopLayout = inflater.inflate(R.layout.floating_window_sliding_puzzle, null);

            quit = mDesktopLayout.findViewById(R.id.floating_window_quit);
            move = mDesktopLayout.findViewById(R.id.floating_window_move);
            small = mDesktopLayout.findViewById(R.id.floating_window_small);
            img1 = mDesktopLayout.findViewById(R.id.sliding_puzzle_img1);
            img2 = mDesktopLayout.findViewById(R.id.sliding_puzzle_img2);
            img3 = mDesktopLayout.findViewById(R.id.sliding_puzzle_img3);
            img4 = mDesktopLayout.findViewById(R.id.sliding_puzzle_img4);
            img5 = mDesktopLayout.findViewById(R.id.sliding_puzzle_img5);
            img6 = mDesktopLayout.findViewById(R.id.sliding_puzzle_img6);
            img7 = mDesktopLayout.findViewById(R.id.sliding_puzzle_img7);
            img8 = mDesktopLayout.findViewById(R.id.sliding_puzzle_img8);
            img9 = mDesktopLayout.findViewById(R.id.sliding_puzzle_img9);
            img0 = mDesktopLayout.findViewById(R.id.sliding_puzzle_img0);
            img00 = mDesktopLayout.findViewById(R.id.sliding_puzzle_img00);
            touchView = mDesktopLayout.findViewById(R.id.sliding_puzzle_touchView);
            txt_footstep = mDesktopLayout.findViewById(R.id.puzzle_footstep);
            txt_time = mDesktopLayout.findViewById(R.id.puzzle_time);
            btn_reset = mDesktopLayout.findViewById(R.id.puzzle_reset);

            txt_footstep.setText("步数："+footsteps);

            img00.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    distance=img00.getY()-img0.getY();
                }
            });

            number.add(-1);
            number.add(-1);
            number.add(-1);
            number.add(-1);
            number.add(-1);
            number.add(-1);
            number.add(-1);
            number.add(-1);
            number.add(-1);

            Random random=new Random();

            do {
                for (int i = 0; i < 9; i++) {
                    do {
                        number.set(i, random.nextInt(9) + 1);
                    } while (ListContains(number, number.get(i)));
                    if (number.get(i) == 9)
                        zeroNum = i + 1;
                }
            }while(!checkValid());

            RefreshUI();
            isReady=true;
            currentSecond=0;
            btn_reset = (Button)mDesktopLayout.findViewById(R.id.puzzle_reset);

            btn_reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    number.set(0,-1);
                    number.set(1,-1);
                    number.set(2,-1);
                    number.set(3,-1);
                    number.set(4,-1);
                    number.set(5,-1);
                    number.set(6,-1);
                    number.set(7,-1);
                    number.set(8,-1);
                    Random random=new Random();

                    do {
                        for (int i = 0; i < 9; i++) {
                            do {
                                number.set(i, random.nextInt(9) + 1);
                            } while (ListContains(number, number.get(i)));
                            if (number.get(i) == 9)
                                zeroNum = i + 1;
                        }
                    }while(!checkValid());

                    RefreshUI();
                    currentSecond=0;
                    footsteps=0;
                }
            });

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
                    windowManager.removeView(mDesktopLayout);	// 移除悬浮窗
                    onDestroy();
                    stopSelf();
                }
            });


            touchView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(canStartAnimation&&!isFinish) {
                        switch (event.getAction()) {

                            case MotionEvent.ACTION_DOWN:
                                mPosX = event.getX();
                                mPosY = event.getY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                mCurPosX = event.getX();
                                mCurPosY = event.getY();

                                break;
                            case MotionEvent.ACTION_UP:

                                if (mCurPosY - mPosY > 0
                                        && (Math.abs(mCurPosY - mPosY) > 25) && (Math.abs(mCurPosX - mPosX) < Math.abs(mCurPosY - mPosY))) {
                                    //向下滑动
                                    if (zeroNum >= 4 && zeroNum <= 9) {

                                        footsteps++;
                                        float curTranslationY = getId(zeroNum - 3).getTranslationY();
                                        float curTranslationY2 = getId(zeroNum).getTranslationY();

                                        number.set(zeroNum-1,number.get(zeroNum-4));
                                        number.set(zeroNum-4,9);
                                        ObjectAnimator animator = ObjectAnimator.ofFloat(getId(zeroNum-3), "translationY", curTranslationY, curTranslationY + distance);
                                        animator.setDuration(300);
                                        animator.start();
                                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(getId(zeroNum), "translationY", curTranslationY2, curTranslationY2 - distance);
                                        animator2.setDuration(300);
                                        animator2.start();
                                        int temp=realPosition[zeroNum];
                                        realPosition[zeroNum]=realPosition[zeroNum-3];
                                        realPosition[zeroNum-3]=temp;
                                        zeroNum-=3;
                                        canStartAnimation=false;
                                        txt_footstep.setText("步数："+footsteps);
                                        if(ifFinish())
                                            isFinish=true;
                                        animator.addListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                canStartAnimation=true;
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {

                                            }
                                        });


                                    }

                                } else if (mCurPosY - mPosY < 0
                                        && (Math.abs(mCurPosY - mPosY) > 25) && (Math.abs(mCurPosX - mPosX) < Math.abs(mCurPosY - mPosY))) {
                                    //向上滑动
                                    if (zeroNum >= 1 && zeroNum <= 6) {
                                        footsteps++;
                                        float curTranslationY = getId(zeroNum + 3).getTranslationY();
                                        float curTranslationY2 = getId(zeroNum).getTranslationY();
                                        number.set(zeroNum-1,number.get(zeroNum+2));
                                        number.set(zeroNum+2,9);
                                        ObjectAnimator animator = ObjectAnimator.ofFloat(getId(zeroNum+3), "translationY", curTranslationY, curTranslationY - distance);
                                        animator.setDuration(300);
                                        animator.start();
                                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(getId(zeroNum), "translationY", curTranslationY2, curTranslationY2 + distance);
                                        animator2.setDuration(300);
                                        animator2.start();
                                        int temp=realPosition[zeroNum];
                                        realPosition[zeroNum]=realPosition[zeroNum+3];
                                        realPosition[zeroNum+3]=temp;
                                        zeroNum+=3;
                                        canStartAnimation=false;
                                        txt_footstep.setText("步数："+footsteps);
                                        if(ifFinish())
                                            isFinish=true;
                                        animator.addListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                canStartAnimation=true;
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {

                                            }
                                        });
                                    }

                                } else if (mCurPosX - mPosX < 0
                                        && (Math.abs(mCurPosX - mPosX) > 25) && (Math.abs(mCurPosX - mPosX) > Math.abs(mCurPosY - mPosY))) {
                                    //向左滑动
                                    if ((zeroNum >= 1 && zeroNum <= 2) || (zeroNum >= 4 && zeroNum <= 5) || (zeroNum >= 7 && zeroNum <= 8)) {
                                        footsteps++;
                                        float curTranslationX = getId(zeroNum).getTranslationX();
                                        float curTranslationX2 = getId(zeroNum + 1).getTranslationX();
                                        number.set(zeroNum-1,number.get(zeroNum));
                                        number.set(zeroNum,9);
                                        ObjectAnimator animator = ObjectAnimator.ofFloat(getId(zeroNum), "translationX", curTranslationX, curTranslationX + distance);
                                        animator.setDuration(300);
                                        animator.start();
                                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(getId(zeroNum+1), "translationX", curTranslationX2, curTranslationX2 - distance);
                                        animator2.setDuration(300);
                                        animator2.start();
                                        int temp=realPosition[zeroNum];
                                        realPosition[zeroNum]=realPosition[zeroNum+1];
                                        realPosition[zeroNum+1]=temp;
                                        zeroNum+=1;
                                        canStartAnimation=false;
                                        txt_footstep.setText("步数："+footsteps);
                                        if(ifFinish())
                                            isFinish=true;
                                        animator.addListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                canStartAnimation=true;
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {

                                            }
                                        });
                                    }

                                } else if (mCurPosX - mPosX > 0
                                        && (Math.abs(mCurPosX - mPosX) > 25) && (Math.abs(mCurPosX - mPosX) > Math.abs(mCurPosY - mPosY))) {
                                    //向右滑动
                                    if ((zeroNum >= 2 && zeroNum <= 3) || (zeroNum >= 5 && zeroNum <= 6) || (zeroNum >= 8 && zeroNum <= 9)) {
                                        footsteps++;
                                        float curTranslationX = getId(zeroNum - 1).getTranslationX();
                                        float curTranslationX2 = getId(zeroNum).getTranslationX();
                                        number.set(zeroNum-1,number.get(zeroNum-2));
                                        number.set(zeroNum-2,9);
                                        ObjectAnimator animator = ObjectAnimator.ofFloat(getId(zeroNum-1), "translationX", curTranslationX, curTranslationX + distance);
                                        animator.setDuration(300);
                                        animator.start();
                                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(getId(zeroNum), "translationX", curTranslationX2, curTranslationX2 - distance);
                                        animator2.setDuration(300);
                                        animator2.start();
                                        int temp=realPosition[zeroNum];
                                        realPosition[zeroNum]=realPosition[zeroNum-1];
                                        realPosition[zeroNum-1]=temp;
                                        zeroNum-=1;
                                        canStartAnimation=false;
                                        txt_footstep.setText("步数："+footsteps);
                                        if(ifFinish())
                                            isFinish=true;
                                        animator.addListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                canStartAnimation=true;
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {

                                            }
                                        });
                                    }

                                }

                                break;
                        }
                    }
                    return true;
                }
            });

            }
        }

    public boolean ifFinish(){
        if(number.get(0)==1&&number.get(1)==2&&number.get(2)==3&&number.get(3)==4&&number.get(4)==5&&number.get(5)==6&&number.get(6)==7&&number.get(7)==8&&number.get(8)==9){
            isFinish=true;
            return true;
        }
        else
            return false;
    }
    public boolean checkValid(){
        int time=0;
        for(int i=0;i<8;i++){
            if(number.get(i+1)<number.get(i))
                time++;
        }
        if(!(time % 2 ==0)){
                number.set(0,-1);
                number.set(1,-1);
                number.set(2,-1);
                number.set(3,-1);
                number.set(4,-1);
                number.set(5,-1);
                number.set(6,-1);
                number.set(7,-1);
                number.set(8,-1);
            }
        return time % 2 == 0;
        }

    public ImageView getId(int num){
        num=realPosition[num];
        switch (num){
            case 1:
                return img1;
            case 2:
                return img2;
            case 3:
                return img3;
            case 4:
                return img4;
            case 5:
                return img5;
            case 6:
                return img6;
            case 7:
                return img7;
            case 8:
                return img8;
            case 9:
                return img9;
                default:return img1;
        }
    }

    public boolean ListContains(List<Integer> list,int num){
        int time=0;
        for(int i=0;i<list.size();i++){
            if(list.get(i)==num)
                time++;
        }
        if(time>1)
            return true;
        else
            return false;
    }

    public void RefreshUI(){
            setDrawable(number.get(0), img1);
            setDrawable(number.get(1), img2);
            setDrawable(number.get(2), img3);
            setDrawable(number.get(3), img4);
            setDrawable(number.get(4), img5);
            setDrawable(number.get(5), img6);
            setDrawable(number.get(6), img7);
            setDrawable(number.get(7), img8);
            setDrawable(number.get(8), img9);

    }

    public void setDrawable(int number,ImageView view){
        int id;
        switch (number){
            case 1:
                id=R.drawable.number1;
                break;
            case 2:
                id=R.drawable.number2;
                break;
            case 3:
                id=R.drawable.number3;
                break;
            case 4:
                id=R.drawable.number4;
                break;
            case 5:
                id=R.drawable.number5;
                break;
            case 6:
                id=R.drawable.number6;
                break;
            case 7:
                id=R.drawable.number7;
                break;
            case 8:
                id=R.drawable.number8;
                break;
            case 9:
                id=0;
                break;
                default:id=0;
        }

        if(id==0){
            view.setImageDrawable(null);
        }else {
            view.setImageDrawable(getResources().getDrawable(id));
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