package hyd.modengxian.utils;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.widget.ImageView;

public class BottomNavigationViewHelper { /**
 * 设置图片尺寸
 * @param view
 * @param width
 * @param height
 */
public static void setImageSize(BottomNavigationView view, int width, int height) {
    BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
    try {
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
            ImageView imageView = item.findViewById(android.support.design.R.id.icon);
            imageView.getLayoutParams().width = width;
            imageView.getLayoutParams().height = height;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
