package hyd.modengxian.utils;

import android.content.Context;


import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import hyd.modengxian.R;
import hyd.modengxian.entity.MultipleItem;

public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    public MultipleItemQuickAdapter(Context context, List data) {
        super(data);
        addItemType(MultipleItem.TEXT, R.layout.item_text_view);
        addItemType(MultipleItem.IMG, R.layout.item_image_view);
        addItemType(MultipleItem.IMG_TEXT, R.layout.item_img_text_view);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.TEXT:
                helper.setText(R.id.tv, item.getTitle());
                helper.setText(R.id.tv1, item.getContent());
                helper.setText(R.id.tv2, item.getTime());
                helper.setImageResource(R.id.iv, item.getImageSource());
                break;
            case MultipleItem.IMG_TEXT:
                helper.setImageResource(R.id.iv, item.getImageSource());
                helper.setText(R.id.tv,item.getTitle());
                break;
            case MultipleItem.IMG:
                helper.setImageResource(R.id.iv, item.getImageSource());
            default:
                break;
        }
    }

}