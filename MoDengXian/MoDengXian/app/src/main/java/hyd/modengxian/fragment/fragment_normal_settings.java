package hyd.modengxian.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hyd.modengxian.R;
import hyd.modengxian.utils.LinkMain2;
import hyd.modengxian.utils.MySettingListViewAdapter2;

import static android.content.Context.MODE_PRIVATE;

public class fragment_normal_settings extends Fragment {
    private List<LinkMain2> linkMains2 = new ArrayList<>();
    private ListView listView;
    private SharedPreferences sp;
    private boolean switch_;
    private Switch switch_sound;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, null);
        sp=getActivity().getSharedPreferences("Preference", MODE_PRIVATE);

        this.init();
        MySettingListViewAdapter2 myAdapter = new MySettingListViewAdapter2(this.getActivity(),R.layout.notification_setting_item,linkMains2);
        listView = view.findViewById(R.id.Setting_ListView);
        listView.setAdapter(myAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("Position1",1001);
                        edit.putInt("Position2",1009);
                        edit.putInt("Position3",1002);
                        edit.putInt("Position4",1004);
                        edit.apply();
                        Toast.makeText(getActivity(), "已重置布局", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:

                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void init(){
        linkMains2.add(new LinkMain2("重置通知栏布局","初始化应用布局",false,false));
        linkMains2.add(new LinkMain2("清除缓存数据","下次启动时可能会重新加载部分数据",false,false));
        linkMains2.add(new LinkMain2("初始化应用","清除所有本地数据",false,false));
    }

}