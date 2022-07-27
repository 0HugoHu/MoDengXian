package hyd.modengxian.helper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;

import java.util.List;

public class UserInfo extends AppCompatActivity {

    public TextView Name,Id,Contribute,Credit,Level,Achivement,Time1,Time2,Time3,Contri1,Contri2,Contri3;
    private SharedPreferences sp ;
    public String name,id,contribute,credit,level,achivement,time1,time2,time3,contri1,contri2,contri3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent starter = new Intent(UserInfo.this, MainActivity.class);
                UserInfo.this.startActivity(starter);
                finish();
            }
        });

        AVOSCloud.initialize(this,"xHiee3l7EnDWTMU2nQXlPdoM-gzGzoHsz","TIYGEvp1RXu3H86O6j55ezx6");
        AVOSCloud.setDebugLogEnabled(true);

        Name=findViewById(R.id.userActivityUsernameTv);
        Id=findViewById(R.id.userActivityUseridTv);
        Contribute=findViewById(R.id.userActivityScoreTextView);
        Credit=findViewById(R.id.UserActivityUserlevelTextView1);
        Level=findViewById(R.id.UserActivityUserScorelevelTextView11);
        Achivement=findViewById(R.id.UserActivityUserScorelevelTextView1);
        Time1=findViewById(R.id.textView5);
        Time2=findViewById(R.id.textView6);
        Time3=findViewById(R.id.textView7);
        Contri1=findViewById(R.id.textView25);
        Contri2=findViewById(R.id.textView26);
        Contri3=findViewById(R.id.textView27);

        sp=getSharedPreferences("Setting", MODE_PRIVATE);
        id=sp.getString("userID","");

        AVQuery<AVObject> query = new AVQuery<>("Person");
        query.whereEqualTo("userID", id);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                name=avObject.getString("name");
                contribute=avObject.getNumber("contribute").toString();
                credit=avObject.getNumber("credit").toString();
                level=avObject.getString("level");
                achivement=avObject.getNumber("achivement").toString();

                Name.setText(name);
                Contribute.setText(contribute);
                Credit.setText(credit);
                Level.setText(level);
                Achivement.setText(achivement);
            }
        });

        AVQuery<AVObject> query2 = new AVQuery<>("UploadTime");
        query2.whereExists("Time");
        query2.orderByAscending("Time");
        query2.limit(3);
        query2.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                time1=(list.get(0).getString("Id"))+"    "+ list.get(0).getNumber("Time")+"秒";
                time2=(list.get(1).getString("Id"))+"    "+ list.get(1).getNumber("Time")+"秒";
                time3=(list.get(2).getString("Id"))+"    "+ list.get(2).getNumber("Time")+"秒";

                Time1.setText(time1);
                Time2.setText(time2);
                Time3.setText(time3);
            }
        });

        AVQuery<AVObject> query3 = new AVQuery<>("Person");
        query3.whereExists("contribute");
        query3.orderByDescending("contribute");
        query3.limit(3);
        query3.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                contri1=(list.get(0).getString("userID"))+"    "+ list.get(0).getNumber("contribute")+"次";
                contri2=(list.get(1).getString("userID"))+"    "+ list.get(1).getNumber("contribute")+"次";
                contri3=(list.get(2).getString("userID"))+"    "+ list.get(2).getNumber("contribute")+"次";

                Contri1.setText(contri1);
                Contri2.setText(contri2);
                Contri3.setText(contri3);
            }
        });

    }
}
