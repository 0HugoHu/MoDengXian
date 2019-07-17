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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hyd.modengxian.R;
import hyd.modengxian.theme.ChooseTheme;
import hyd.modengxian.utils.LinkMain2;
import hyd.modengxian.utils.MySettingListViewAdapter2;
import hyd.modengxian.utils.OpenAccessibilitySettingHelper;

import static android.content.Context.MODE_PRIVATE;

public class fragment_notification extends Fragment {
    private List<LinkMain2> linkMains2 = new ArrayList<>();
    private ListView listView;
    private SharedPreferences sp;
    private boolean switch_,switch2,switch3;
    private Switch switch_sound,switch_accesibility;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, null);
        sp=getActivity().getSharedPreferences("Setting", MODE_PRIVATE);
        switch_=sp.getBoolean("No_Sound_isOpen",false);
        switch2=OpenAccessibilitySettingHelper.isAccessibilitySettingsOn(getActivity(),
                hyd.modengxian.service.MyAccessibilityService.class.getName());

        switch3=sp.getBoolean("Always_update",false);
        this.init();
        MySettingListViewAdapter2 myAdapter = new MySettingListViewAdapter2(this.getActivity(),R.layout.notification_setting_item,linkMains2);
        listView = view.findViewById(R.id.Setting_ListView);
        listView.setAdapter(myAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        ApplicationInfo appInfo = getActivity().getApplicationInfo();
                        String pkg = getActivity().getApplicationContext().getPackageName();
                        int uid = appInfo.uid;
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                            intent.putExtra(Settings.EXTRA_APP_PACKAGE, pkg);
                            intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                            intent.putExtra("app_package", pkg);
                            intent.putExtra("app_uid", uid);
                            getActivity().startActivity(intent);

                        } catch (Exception e) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            getActivity().startActivity(intent);
                        }
                        break;
                    case 1:
                        SharedPreferences.Editor edit = sp.edit();
                        if(switch_)
                            edit.putBoolean("No_Sound_isOpen",false);
                        else
                            edit.putBoolean("No_Sound_isOpen",true);
                        edit.apply();
                        switch_sound=view.findViewById(R.id.notification_setting_switch);
                        switch_=!switch_;
                        switch_sound.setChecked(switch_);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        SharedPreferences.Editor edit2 = sp.edit();
                        if(switch2)
                            edit2.putBoolean("Accesibility_isOpen",false);
                        else {
                            edit2.putBoolean("Accesibility_isOpen", true);
                            if (!OpenAccessibilitySettingHelper.isAccessibilitySettingsOn(getActivity(),
                                    hyd.modengxian.service.MyAccessibilityService.class.getName())){// 判断服务是否开启
                                Toast.makeText(getActivity(), "请找到“莫等闲”并开启辅助功能", Toast.LENGTH_LONG).show();
                                OpenAccessibilitySettingHelper.jumpToSettingPage(getActivity());// 跳转到开启页面
                            }else {
                                //Toast.makeText(this, "服务已开启", Toast.LENGTH_SHORT).show();
                            }

                        }
                        edit2.apply();
                        switch_accesibility=view.findViewById(R.id.notification_setting_switch);
                        switch2=!switch2;
                        switch_accesibility.setChecked(switch2);
                        break;
                    case 5:
                        SharedPreferences.Editor edit3 = sp.edit();
                        if(switch3)
                            edit3.putBoolean("Always_update",false);
                        else {
                            edit3.putBoolean("Always_update", true);
                            }
                        edit3.apply();
                        switch_accesibility=view.findViewById(R.id.notification_setting_switch);
                        switch3=!switch3;
                        switch_accesibility.setChecked(switch3);
                        break;
                    case 6:
                        SharedPreferences.Editor edit4 = sp.edit();
                        edit4.putInt("Position1",1001);
                        edit4.putInt("Position2",1009);
                        edit4.putInt("Position3",1002);
                        edit4.putInt("Position4",1004);
                        edit4.apply();
                        Toast.makeText(getActivity(), "已重置布局", Toast.LENGTH_SHORT).show();
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
        linkMains2.add(new LinkMain2("设置通知静音","将前往系统设置界面\n部分品牌手机设置十分繁琐，默认开启下面功能",false,false));
        if(switch_)
            linkMains2.add(new LinkMain2("应用使用时手机静音","避免通知频繁发出声音",true,true));
        else
            linkMains2.add(new LinkMain2("应用使用时手机静音","避免通知频繁发出声音",false,true));
        linkMains2.add(new LinkMain2("通知背景颜色","透明（暂不可用）",false,false));
        linkMains2.add(new LinkMain2("通知字体颜色","黑（暂不可用）",false,false));
        if(switch2)
            linkMains2.add(new LinkMain2("开启无障碍功能","便于快捷选择文字",true,true));
        else
            linkMains2.add(new LinkMain2("开启无障碍功能","便于快捷选择文字",false,true));
        if(switch3)
            linkMains2.add(new LinkMain2("保持最新数据","每次启动自动缓存最新数据",true,true));
        else
            linkMains2.add(new LinkMain2("保持最新数据","每次启动自动缓存最新数据",false,true));
        linkMains2.add(new LinkMain2("重置通知栏布局","初始化应用布局",false,false));
        linkMains2.add(new LinkMain2("清除缓存数据","下次启动时可能会重新加载部分数据",false,false));
        linkMains2.add(new LinkMain2("初始化应用","清除所有本地数据",false,false));
    }

}