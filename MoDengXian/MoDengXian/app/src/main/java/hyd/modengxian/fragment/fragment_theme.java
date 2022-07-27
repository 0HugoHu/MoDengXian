package hyd.modengxian.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import hyd.modengxian.R;
import hyd.modengxian.theme.ShowTheme;

public class fragment_theme extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choosetheme, null);
        ImageView imageView=view.findViewById(R.id.theme_color_island);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ShowTheme.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}