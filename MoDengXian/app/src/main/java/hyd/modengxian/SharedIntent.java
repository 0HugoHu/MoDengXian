package hyd.modengxian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import hyd.modengxian.service.FloatingService;
import hyd.modengxian.service.FloatingService_Dialog;

public class SharedIntent extends AppCompatActivity {
    private String Shared_PassagesUrl = "/MoDengXianData/Passages.txt";
    private String Shared_OrderUrl = "/MoDengXianData/Order.txt";
    private File filePrefix = Environment.getExternalStorageDirectory();
    CharSequence text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_intent);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        text= getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);

        if (Intent.ACTION_SEND.equals(action)&&type!=null){
            if ("text/plain".equals(type)){
                dealTextMessage(intent);
            }else if(type.startsWith("image/")){
                dealPicStream(intent);
            }
        }else if (Intent.ACTION_SEND_MULTIPLE.equals(action)&&type!=null){
            if (type.startsWith("image/")){
                dealMultiplePicStream(intent);
            }
        }else{
            if(text!=null||!Objects.equals(text, "")){
                Calendar cal= Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH)+1;
                int day = cal.get(Calendar.DATE);

                SharedPreferences sp;
                sp = getSharedPreferences("Temp", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("Content", "将下列内容添加至收藏？（点击修改）");
                edit.putString("Decision_type", "Selected");
                edit.putString("Data", String.valueOf(text));
                edit.putString("Time", year+"-"+month+"-"+day);
                String packages="未知应用";
                edit.putString("Package", packages);
                edit.commit();

                Intent intent2= new Intent();
                intent2.setClass(SharedIntent.this, FloatingService_Dialog.class);
                startService(intent2);
                finish();
            }
        }
    }

    //接收应用分享
    public void dealTextMessage(Intent intent) {
        String share = intent.getStringExtra(Intent.EXTRA_TEXT);
        String title = intent.getStringExtra(Intent.EXTRA_TITLE);
        Writer r1 = null;
        share = share.replace("\n","@hyd&");
        Toast.makeText(this, share, Toast.LENGTH_LONG).show();
        try {
            r1 = new FileWriter(filePrefix + Shared_PassagesUrl, true);
            r1.write(share + "\n");
            r1.close();
            r1 = new FileWriter(filePrefix + Shared_OrderUrl, true);
            r1.write(1 + "\n");
            r1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }
    public void dealPicStream(Intent intent){
        Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        finish();
    }
    public void dealMultiplePicStream(Intent intent){
        ArrayList<Uri> arrayList = intent.getParcelableArrayListExtra(intent.EXTRA_STREAM);
        finish();
    }
}
