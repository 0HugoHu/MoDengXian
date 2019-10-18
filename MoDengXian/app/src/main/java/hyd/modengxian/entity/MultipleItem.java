package hyd.modengxian.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MultipleItem implements MultiItemEntity {
    public static final int TEXT = 1;
    public static final int IMG = 2;
    public static final int IMG_TEXT = 3;
    public static final int TEXT_SPAN_SIZE = 4;
    public static final int IMG_SPAN_SIZE = 1;
    public static final int IMG_TEXT_SPAN_SIZE = 4;
    public static final int IMG_TEXT_SPAN_SIZE_MIN = 2;
    private int itemType;
    private int spanSize;
    private int imageSource;
    private String title;
    private String content;
    private String Time;

    //图片和标题
    public MultipleItem(int itemType, int spanSize, String title,int imageSource) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.title = title;
        this.imageSource = imageSource;
    }

    //纯图片
    public MultipleItem(int itemType, int spanSize ,int imageSource) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.imageSource = imageSource;
    }

    //文字
    public MultipleItem(int itemType, int spanSize ,String title, String content, int imageSource, String Time) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.title = title;
        this.content = content;
        this.imageSource = imageSource;
        this.Time = Time;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

 

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public int getImageSource() {
        return imageSource;
    }

    public void setImageSouce(int ImageSource) {
        this.imageSource = imageSource;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}