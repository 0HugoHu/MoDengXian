package hyd.modengxian.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class fragment_advance_settings extends Fragment {
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
        linkMains2.add(new LinkMain2("允许推送","推送你可能感兴趣的内容",false,true));
    }

}