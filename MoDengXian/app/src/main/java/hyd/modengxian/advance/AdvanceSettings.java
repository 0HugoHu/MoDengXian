package hyd.modengxian.advance;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import com.bilibili.magicasakura.utils.ThemeUtils;

import hyd.modengxian.MainActivity;
import hyd.modengxian.R;
import hyd.modengxian.fragment.fragment_advance_settings;
import hyd.modengxian.fragment.fragment_normal_settings;
import hyd.modengxian.normal.NormalSettings;
import hyd.modengxian.utils.ThemeHelper;

import static android.view.View.GONE;

public class AdvanceSettings extends AppCompatActivity {
    private Toolbar toolbar;
    private int theme_color;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        theme_color = ThemeHelper.getTheme(this);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bottomNavigationView=findViewById(R.id.navigation);
        bottomNavigationView.setVisibility(GONE);

        refreshUI();
        getSupportActionBar().setTitle("实验室");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AdvanceSettings.this, MainActivity.class);
                intent.putExtra("source", "settings");
                startActivity(intent);
            }
        });

        //步骤一：添加一个FragmentTransaction的实例
        FragmentManager fragmentManager =getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //步骤二：用add()方法加上Fragment的对象rightFragment
        fragment_advance_settings fragment = new fragment_advance_settings();
        transaction.add(R.id.content_frame, fragment);

        //步骤三：调用commit()方法使得FragmentTransaction实例的改变生效
        transaction.commit();

    }

    public void refreshUI(){
        ThemeUtils.refreshUI(AdvanceSettings.this, new ThemeUtils.ExtraRefreshable() {
                    @Override
                    public void refreshGlobal(Activity activity) {
                        //for global setting, just do once
                        final AdvanceSettings context = AdvanceSettings.this;
                        ActivityManager.TaskDescription taskDescription =
                                new ActivityManager.TaskDescription(null, null,
                                        ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary));
                        setTaskDescription(taskDescription);
                        switch (theme_color){
                            case 0x1:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.pink));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.pink));
                                break;
                            case 0x2:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.purple));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.purple));
                                break;
                            case 0x3:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.blue));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
                                break;
                            case 0x4:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.green));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.green));
                                break;
                            case 0x5:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.green_light));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.green_light));
                                break;
                            case 0x6:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.yellow));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.yellow));
                                break;
                            case 0x7:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.orange));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.orange));
                                break;
                            case 0x8:
                                getWindow().setStatusBarColor(
                                        ThemeUtils.getColorById(context, R.color.red));
                                toolbar.setBackgroundColor(getResources().getColor(R.color.red));
                                break;
                        }

                    }

                    @Override
                    public void refreshSpecificView(View view) {
                        //TODO: will do this for each traversal
                    }
                }
        );
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            Intent intent = new Intent();
            intent.setClass(AdvanceSettings.this, MainActivity.class);
            intent.putExtra("source", "settings");
            startActivity(intent);
        }
        return false;
    }
}