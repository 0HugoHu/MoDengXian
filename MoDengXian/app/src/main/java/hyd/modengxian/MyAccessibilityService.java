package hyd.modengxian;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MyAccessibilityService extends AccessibilityService {

    private static final String WECHAT_OPEN_EN = "Open";
    private static final String WECHAT_OPENED_EN = "You've opened";
    private boolean mLuckyMoneyReceived;
    private String lastFetchedHongbaoId = null;
    private AccessibilityNodeInfo rootNodeInfo;
    private List mReceiveNode;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        this.rootNodeInfo = event.getSource();
        if (rootNodeInfo == null) {
            return;
        }
        mReceiveNode = null;
        checkNodeInfo();
        /* 如果已经接收到红包并且还没有戳开 */
        if (mLuckyMoneyReceived && (mReceiveNode != null)) {
            int size = mReceiveNode.size();
            if (size > 0) {
                final String id = getHongbaoText((AccessibilityNodeInfo) mReceiveNode.get(size - 1));

                Handler handlerThree = new Handler(Looper.getMainLooper());
                handlerThree.post(new Runnable() {
                    public void run(){
                        Toast.makeText(getApplicationContext(), id+"", Toast.LENGTH_SHORT).show();
                    }
                });

                //disableSelf();
                mLuckyMoneyReceived = false;
            }
        }
    }

    private void checkNodeInfo() {
        if (rootNodeInfo == null) {
            return;
        }
        /* 聊天会话窗口，遍历节点匹配“点击拆开”，“口令红包”，“点击输入口令” */
        List nodes1 = this.findAccessibilityNodeInfosByTexts(this.rootNodeInfo, new String[]{"毛衣", "飞机", "习近平同朝鲜劳动党", "GO TO SETTINGS"});
        if (!nodes1.isEmpty()) {
            String nodeId = Integer.toHexString(System.identityHashCode(this.rootNodeInfo));
            if (!nodeId.equals(lastFetchedHongbaoId)) {
                mLuckyMoneyReceived = true;
                mReceiveNode = nodes1;
            }
        }
    }

    private String getHongbaoText(AccessibilityNodeInfo node) {
        /* 获取红包上的文本 */
        String content;
        try {
            AccessibilityNodeInfo i = node.getParent().getChild(0);
            content = i.getText().toString();
        } catch (NullPointerException npe) {
            return null;
        }
        return content;
    }

    private List findAccessibilityNodeInfosByTexts(AccessibilityNodeInfo nodeInfo, String[] texts) {
        for (String text : texts) {
            if (text == null) continue;
            List nodes = nodeInfo.findAccessibilityNodeInfosByText(text);
            if (!nodes.isEmpty()) {
                if (text.equals(WECHAT_OPEN_EN) && !nodeInfo.findAccessibilityNodeInfosByText(WECHAT_OPENED_EN).isEmpty()) {
                    continue;
                }
                return nodes;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void onInterrupt() {

    }



}
