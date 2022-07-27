package hyd.modengxian.utils;

public interface EventCallback {
    public void onProgressChanged(int progress, int maxProgress, boolean indeterminate);

    public void onProgressMessage(String fileName);

    public void onPageTitleAvailable(String pageTitle);

    public void onLogMessage(String message);

    public void onError(Throwable error);

    public void onError(String errorMessage);

    public void onFatalError(Throwable error, String pageUrl);
}
