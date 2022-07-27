package hyd.modengxian.utils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hyd.modengxian.R;

public class MyPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mData;
    private List<Integer> mPic;

    public MyPagerAdapter(Context context ,List<String> list, List<Integer> list2) {
        mContext = context;
        mData = list;
        mPic = list2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.banner_viewpager,null);
        TextView tv = (TextView) view.findViewById(R.id.banner_viewpager_text);
        ImageView iv = (ImageView) view.findViewById(R.id.banner_viewpager_pic);
        ImageView dot1 = (ImageView) view.findViewById(R.id.banner_viewpager_dot1);
        ImageView dot2 = (ImageView) view.findViewById(R.id.banner_viewpager_dot2);
        ImageView dot3 = (ImageView) view.findViewById(R.id.banner_viewpager_dot3);
        ImageView dot4 = (ImageView) view.findViewById(R.id.banner_viewpager_dot4);
        ImageView dot5 = (ImageView) view.findViewById(R.id.banner_viewpager_dot5);
        switch (position){
            case 0:
                dot1.setImageResource(R.drawable.ic_dot_current);
                break;
            case 1:
                dot2.setImageResource(R.drawable.ic_dot_current);
                break;
            case 2:
                dot3.setImageResource(R.drawable.ic_dot_current);
                break;
            case 3:
                dot4.setImageResource(R.drawable.ic_dot_current);
                break;
            case 4:
                dot5.setImageResource(R.drawable.ic_dot_current);
                break;
        }
        tv.setText(mData.get(position));
        iv.setImageResource(mPic.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container,position,object); 这一句要删除，否则报错
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
