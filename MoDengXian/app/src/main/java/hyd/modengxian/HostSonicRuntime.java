package hyd.modengxian;

import android.content.Context;
import android.os.Environment;

import com.tencent.sonic.sdk.SonicRuntime;
import com.tencent.sonic.sdk.SonicSessionClient;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Here is a sample subclass of SonicRuntime
 */
public class HostSonicRuntime extends SonicRuntime {
    public HostSonicRuntime(Context context) {
        super(context);
    }

    @Override
    public void log(String tag, int level, String message) {

    }

    @Override
    public String getCookie(String url) {
        return null;
    }

    @Override
    public boolean setCookie(String url, List<String> cookies) {
        return false;
    }

    /**
     * @return User's UA
     */
    @Override
    public String getUserAgent() {
        return "";
    }
    /**
     * @return the ID of user.
     */
    @Override
    public String getCurrentUserAccount() {
        return "";
    }

    @Override
    public boolean isSonicUrl(String url) {
        return false;
    }

    @Override
    public Object createWebResourceResponse(String mimeType, String encoding, InputStream data, Map<String, String> headers) {
        return null;
    }

    @Override
    public boolean isNetworkValid() {
        return false;
    }

    @Override
    public void showToast(CharSequence text, int duration) {

    }

    @Override
    public void postTaskToThread(Runnable task, long delayMillis) {

    }

    @Override
    public void notifyError(SonicSessionClient client, String url, int errorCode) {

    }

    /**
     * @return the file path which is used to save Sonic caches.
     */
    @Override
    public File getSonicCacheDir() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator         + "sonic/";
        File file = new File(path.trim());
        if(!file.exists()){
            file.mkdir();
        }
        return file;
    }
}