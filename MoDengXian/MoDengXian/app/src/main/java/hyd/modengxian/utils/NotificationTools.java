package hyd.modengxian.utils;

import android.app.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import java.io.*;
import android.content.res.*;

import hyd.modengxian.R;
import hyd.modengxian.service.SaveService;

public class NotificationTools {

    private Notification.Builder builder;
    private NotificationManager notificationManager;

    private Service context;

    private final int NOTIFICATION_ID = 1;

    private boolean hasCancelAllAction = false;

    public NotificationTools(Service context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new Notification.Builder(context);
    }

    public void notifySaveStarted(int saveQueueSize) {
        builder = new Notification.Builder(context);
        builder.setTicker("Saving page...")
                .setContentTitle("Saving page...")
                .setContentText("Save in progress")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setProgress(0, 1, true)
                .setOnlyAlertOnce(true)
                .setOngoing(true);
        addCancelAction();

        if (saveQueueSize > 0) {
            addCancelAllAction();
        }
        context.startForeground(NOTIFICATION_ID, builder.build());
    }

    public void updateProgress(int progress, int maxProgress, boolean indeterminate, int saveQueueSize) {
        builder.setProgress(maxProgress, progress, indeterminate);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void updateText(String newTitle, String newContentText, int saveQueueSize) {
        if (newTitle != null) {
            builder.setContentTitle(newTitle);
        }

        if (newContentText != null) {
            builder.setContentText(newContentText);
        }

        if (saveQueueSize > 0 && !hasCancelAllAction) {
            hasCancelAllAction = true;
            addCancelAllAction();
        }

        builder.setNumber(saveQueueSize);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void notifyFinished(String pageTitle, String savedPageDirectoryLocation) {
        builder = new Notification.Builder(context);

        builder.setTicker("Save completed: " + pageTitle)
                .setContentTitle("Save completed")
                .setContentText(pageTitle)
                .setSmallIcon(R.drawable.ic_notify_save)
                .setProgress(0, 0, false)
                .setOnlyAlertOnce(false)
                .setOngoing(false);

        int maxLargeIconWidth = context.getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width);
        Bitmap largeIconBitmap = BitmapFactory.decodeFile(savedPageDirectoryLocation + File.separator + "saveForOffline_icon.png");

        builder.setLargeIcon(Bitmap.createScaledBitmap(largeIconBitmap, maxLargeIconWidth / 2, maxLargeIconWidth / 2, false));

        context.stopForeground(false);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void notifyFailure(String message, String pageUrl) {
        Log.w("NotificationTools", "notifyFailure called");
        builder = new Notification.Builder(context);

        builder.setTicker("Error, page not saved: " + message)
                .setContentTitle("Error, page not saved")
                .setContentText(message)
                .setProgress(0, 0, false)
                .setOngoing(false)
                .setOnlyAlertOnce(true)
                .setSmallIcon(android.R.drawable.stat_sys_warning);

        if (pageUrl != null) {
            addRetryAction(pageUrl);
        }

        context.stopForeground(false);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancelAll() {
        context.stopForeground(true);
    }

    private void addCancelAction() {
        Intent cancelIntent = new Intent(context, SaveService.class);
        cancelIntent.putExtra("USER_CANCELLED", true);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_notify_discard, "Cancel", pendingIntent);
    }

    private void addCancelAllAction() {
        Intent cancelIntent = new Intent(context, SaveService.class);
        cancelIntent.putExtra("USER_CANCELLED_ALL", true);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_notify_discard, "Cancel all", pendingIntent);
    }

    private void addRetryAction(String url) {
        Intent intent = new Intent(context, SaveService.class);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        PendingIntent pendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_notify_retry, "Retry", pendingIntent);
    }
}