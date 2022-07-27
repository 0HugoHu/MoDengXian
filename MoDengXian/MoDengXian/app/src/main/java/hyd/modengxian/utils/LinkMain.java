package hyd.modengxian.utils;

public class LinkMain {

    private int headPortrait;
    private int mark;
    private String news;
    private String time;

    public LinkMain(int headPortrait, int mark, String news, String time){

        this.headPortrait = headPortrait;
        this.mark = mark;
        this.news = news;
        this.time = time;
    }

    public int getHeadPortrait() {
        return headPortrait;
    }

    public int getMark() {
        return mark;
    }

    public String getNews() {
        return news;
    }

    public String getTime() {
        return time;
    }
}
