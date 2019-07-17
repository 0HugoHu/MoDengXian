package hyd.modengxian.utils;

public class LinkMain2 {

    private String news;
    private String subtitle;
    private Boolean isOpen;
    private Boolean showSwitch;

    public LinkMain2( String news, String subtitle, Boolean isOpen, Boolean showSwitch){

        this.news = news;
        this.subtitle = subtitle;
        this.isOpen = isOpen;
        this.showSwitch = showSwitch;
    }


    public String getNews() {
        return news;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public Boolean getShowSwitch() {
        return showSwitch;
    }
}
