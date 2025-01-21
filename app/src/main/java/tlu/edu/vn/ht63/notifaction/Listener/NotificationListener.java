package tlu.edu.vn.ht63.notifaction.Listener;

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
            AppInfo app = new AppInfo(null,sbn.getPackageName(),null);
            if(BinarySearch.search(AppPermissionRepo.getAppPermission(),app)){
//                Log.d("AddMessage","Add");
                Message msg = new Message(
                        sbn.getPackageName(),
                        sbn.getNotification().extras.getCharSequence("android.title").toString(),
                        sbn.getNotification().extras.getCharSequence("android.text").toString()
                );
                MessageRepo.addMessage(msg);
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
