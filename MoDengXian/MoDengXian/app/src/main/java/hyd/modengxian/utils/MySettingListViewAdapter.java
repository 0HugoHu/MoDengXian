package hyd.modengxian.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hyd.modengxian.R;

public class MySettingListViewAdapter extends ArrayAdapter {


    public MySettingListViewAdapter(Context context, int resource, List<LinkMain> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinkMain linkeMain = (LinkMain) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.setting_item, null);

        ImageView headPortrait = (ImageView) view.findViewById(R.id.setting_image_view);
        ImageView mark = (ImageView) view.findViewById(R.id.setting_image_mark);
        TextView news = (TextView) view.findViewById(R.id.setting_text_title);
        TextView time = (TextView) view.findViewById(R.id.setting_mark_title);

        headPortrait.setImageResource(linkeMain.getHeadPortrait());
        mark.setImageResource(linkeMain.getMark());
        news.setText(linkeMain.getNews());
        time.setText(linkeMain.getTime());


        return view;
    }
}