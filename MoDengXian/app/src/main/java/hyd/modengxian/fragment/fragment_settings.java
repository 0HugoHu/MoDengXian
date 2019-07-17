package hyd.modengxian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;



import java.util.ArrayList;
import java.util.List;

import hyd.modengxian.advance.AdvanceSettings;
import hyd.modengxian.normal.NormalSettings;
import hyd.modengxian.notification.NotificationSettings;
import hyd.modengxian.theme.ChooseTheme;
import hyd.modengxian.R;
import hyd.modengxian.utils.LinkMain;
import hyd.modengxian.utils.MySettingListViewAdapter;

public class fragment_settings extends Fragment {

    View view;
    private List<LinkMain> linkMains = new ArrayList<>();
    private ListView listView;
    private List<LinkMain> linkMains2 = new ArrayList<>();
    private ListView listView2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //避免同一activity下 多个fragment 切换时重复执行onCreateView方法
        // Fragment之间切换时每次都会调用onCreateView方法，导致每次Fragment的布局都重绘，无法保持Fragment原有状态。
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_settings, container, false);
            this.init();
            this.init2();
            MySettingListViewAdapter myAdapter = new MySettingListViewAdapter(this.getActivity(),R.layout.setting_item,linkMains);
            listView = view.findViewById(R.id.Setting_ListView);
            listView.setAdapter(myAdapter);
            MySettingListViewAdapter myAdapter2 = new MySettingListViewAdapter(this.getActivity(),R.layout.setting_item,linkMains2);
            listView2 = view.findViewById(R.id.Setting_ListView2);
            listView2.setAdapter(myAdapter2);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i){
                        case 0:

                            break;
                        case 1:

                            break;
                        case 2:
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), ChooseTheme.class);
                            startActivity(intent);
                            break;
                        case 3:
                            Intent intent0 = new Intent();
                            intent0.setClass(getActivity(), NotificationSettings.class);
                            startActivity(intent0);
                            break;
                    }
                }
            });
            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i){
                        case 0:

                            break;
                        case 1:

                            break;
                        case 2:
                            break;
                        case 3:

                            break;
                        case 4:

                            break;
                    }
                }
            });
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private void init(){

        linkMains.add(new LinkMain(R.drawable.ic_setting_notification,R.drawable.ic_notify_expand,"消息","暂不可用"));
        linkMains.add(new LinkMain(R.drawable.ic_setting_lab,R.drawable.ic_notify_expand,"应用","暂不可用"));
        linkMains.add(new LinkMain(R.drawable.ic_setting_theme,R.drawable.ic_notify_expand,"主题","通知栏风格和壁纸"));
        linkMains.add(new LinkMain(R.drawable.ic_setting_normalsettings,R.drawable.ic_notify_expand,"常规",""));

    }

    private void init2(){

        linkMains2.add(new LinkMain(R.drawable.ic_setting_donate,R.drawable.ic_notify_expand,"捐赠","奉献一点爱心吧"));
        linkMains2.add(new LinkMain(R.drawable.ic_setting_help,R.drawable.ic_notify_expand,"帮助",""));
        linkMains2.add(new LinkMain(R.drawable.ic_setting_advice,R.drawable.ic_notify_expand,"反馈",""));
        linkMains2.add(new LinkMain(R.drawable.ic_setting_recommend,R.drawable.ic_notify_expand,"推荐莫等闲",""));
        linkMains2.add(new LinkMain(R.drawable.ic_setting_about,R.drawable.ic_notify_expand,"关于",""));
    }

}