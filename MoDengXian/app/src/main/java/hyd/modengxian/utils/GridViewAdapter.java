package hyd.modengxian.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import hyd.modengxian.MainActivity;
import hyd.modengxian.R;
import hyd.modengxian.fragment.fragment_home;
import hyd.modengxian.service.FloatingService_Sliding_Puzzle;
import hyd.modengxian.widget.TagView;
import com.huxq17.handygridview.scrollrunner.OnItemMovedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static java.lang.Thread.sleep;

public class GridViewAdapter extends BaseAdapter implements OnItemMovedListener, TagView.OnTagDeleteListener {
    private Context context;
    private List<String> mDatas = new ArrayList<>();
    private TextView sure;

    public GridViewAdapter(Context context, List<String> dataList) {
        this.context = context;
        this.mDatas.addAll(dataList);
    }

    private GridView mGridView;
    private boolean inEditMode = false;

    public void setData(List<String> dataList) {
        this.mDatas.clear();
        this.mDatas.addAll(dataList);
        notifyDataSetChanged();
        saveItemData();
    }

    public void setInEditMode(boolean inEditMode) {
        this.inEditMode = inEditMode;
        notifyDataSetChanged();
        saveItemData();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public String getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mGridView == null) {
            mGridView = (GridView) parent;
        }
        TagView textView;
        if (convertView == null) {
            textView = new TagView(context);
            convertView = textView;
            textView.setMaxLines(1);
            textView.setHeight(DensityUtil.dip2px(context, 30));
            int id = context.getResources().getIdentifier("s_grid_item", "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);
            textView.setBackgroundDrawable(drawable);
            textView.setTextSize(15);
            textView.setGravity(Gravity.CENTER);
        } else {
            textView = (TagView) convertView;
        }
        if (!isFixed(position)) {
            textView.showDeleteIcon(inEditMode);
        } else {
            textView.showDeleteIcon(false);
        }
        textView.setText(getItem(position));
        textView.setOnTagDeleteListener(this);

        if(position<4){
            int id = context.getResources().getIdentifier("tag_first4_circle", "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);
            textView.setBackground(drawable);
            textView.setTextColor(context.getResources().getColor(R.color.text_primary_color,null));
        }else{
            int id = context.getResources().getIdentifier("tag_others_circle", "drawable", context.getPackageName());
            Drawable drawable = context.getResources().getDrawable(id);
            textView.setBackground(drawable);
            textView.setTextColor(context.getResources().getColor(R.color.gray,null));
        }
        return convertView;
    }

    @Override
    public void onItemMoved(int from, int to) {
        String s = mDatas.remove(from);
        mDatas.add(to, s);

        NotificationManager manger;
        manger = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manger.cancelAll();
        notifyDataSetChanged();
        saveItemData();

    }

    @Override
    public boolean isFixed(int position) {
        //When postion==0,the item can not be dragged.
        /*if (position == 0) {
            return true;
        }
        */
        return false;
    }
/*
    @Override
    public void onDelete(View deleteView) {
        int index = mGridView.indexOfChild(deleteView);
        if (index <= 0) return;
        int position = index + mGridView.getFirstVisiblePosition();
        mDatas.remove(position);
        notifyDataSetChanged();
    }
    */


    public void saveItemData(){
        SharedPreferences sp = context.getSharedPreferences("Preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("Position1",MainActivity.StringToPosition(mDatas.get(0)));
        edit.putInt("Position2",MainActivity.StringToPosition(mDatas.get(1)));
        edit.putInt("Position3",MainActivity.StringToPosition(mDatas.get(2)));
        edit.putInt("Position4",MainActivity.StringToPosition(mDatas.get(3)));
        edit.apply();

        fragment_home.setText();

    }


}