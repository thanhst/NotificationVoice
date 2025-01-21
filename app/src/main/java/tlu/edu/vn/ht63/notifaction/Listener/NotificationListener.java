package tlu.edu.vn.ht63.notifaction.Listener;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tlu.edu.vn.ht63.notifaction.Helper.BinarySearch;
import tlu.edu.vn.ht63.notifaction.Model.AppInfo;
import tlu.edu.vn.ht63.notifaction.Model.Message;
import tlu.edu.vn.ht63.notifaction.Repository.AppPermissionRepo;
import tlu.edu.vn.ht63.notifaction.Repository.MessageRepo;

public class NotificationListener extends NotificationListenerService {

    private static Boolean isListeningEnabled = true;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if (isListeningEnabled) {
            try {
                if (BinarySearch.search(AppPermissionRepo.getAppPermission(),
                        new AppInfo(getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(sbn.getPackageName(), 0)).toString(), sbn.getPackageName(), null))) {
                    MessageRepo.addMessage(new Message(
                            sbn.getPackageName(),
                            sbn.getNotification().extras.getCharSequence("android.title").toString(),
                            sbn.getNotification().extras.getCharSequence("android.text").toString()
                    ));
                }
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static void setListeningEnabled(boolean enabled) {
        isListeningEnabled = enabled;
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
