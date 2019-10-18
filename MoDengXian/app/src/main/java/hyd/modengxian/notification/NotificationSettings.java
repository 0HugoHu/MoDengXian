package hyd.modengxian.notification;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bilibili.magicasakura.utils.ThemeUtils;

import hyd.modengxian.MainActivity;
import hyd.modengxian.R;
import hyd.modengxian.fragment.fragment_notification;
import hyd.modengxian.fragment.fragment_theme;
import hyd.modengxian.theme.ChooseTheme;
import hyd.modengxian.utils.ThemeHelper;

import static android.view.View.GONE;

public class NotificationSettings extends AppCompatActivity {
    private Toolbar toolbar;
    private int theme_color;
    private BottomNavigationView bottomNavigationView;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar_title=findViewById(R.id.toolbar_title);
        toolbar =  findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bottomNavigationView=findViewById(R.id.navigation);
        bottomNavigationView.setVisibility(GONE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(
                ThemeUtils.getColorById(NotificationSettings.this, R.color.pink));

        toolbar_title.setText("常规");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(NotificationSettings.this, MainActivity.class);
                intent.putExtra("source", "settings");
                startActivity(intent);
            }
        });

        //步骤一：添加一个FragmentTransaction的实例
        FragmentManager fragmentManager =getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //步骤二：用add()方法加上Fragment的对象rightFragment
        fragment_notification fragment = new fragment_notification();
        transaction.add(R.id.content_frame, fragment);

        //步骤三：调用commit()方法使得FragmentTransaction实例的改变生效
        transaction.commit();

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            Intent intent = new Intent();
            intent.setClass(NotificationSettings.this, MainActivity.class);
            intent.putExtra("source", "settings");
            startActivity(intent);
        }
        return false;
    }
}

