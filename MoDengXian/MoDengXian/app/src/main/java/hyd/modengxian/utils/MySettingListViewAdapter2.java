package hyd.modengxian.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import hyd.modengxian.R;

import static android.content.Context.MODE_PRIVATE;

public class MySettingListViewAdapter2 extends ArrayAdapter {
    private Switch switch_ ;


    public MySettingListViewAdapter2(Context context, int resource, List<LinkMain2> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinkMain2 linkeMain2 = (LinkMain2) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.notification_setting_item, null);

        TextView news = (TextView) view.findViewById(R.id.setting_text_title);
        TextView subtitle = (TextView) view.findViewById(R.id.setting_text_subtitle);
        Switch switch_ = (Switch) view.findViewById(R.id.notification_setting_switch);

        news.setText(linkeMain2.getNews());
        subtitle.setText(linkeMain2.getSubtitle());
        if(linkeMain2.getShowSwitch()) {
            switch_.setVisibility(View.VISIBLE);
            if(linkeMain2.getIsOpen())
                switch_.setChecked(true);
            else
                switch_.setChecked(false);
        }else {
            switch_.setVisibility(View.GONE);
        }
        return view;
    }
}