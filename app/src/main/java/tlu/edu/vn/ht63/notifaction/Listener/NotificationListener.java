package tlu.edu.vn.ht63.notifaction.Listener;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tlu.edu.vn.ht63.notifaction.Model.Message;
import tlu.edu.vn.ht63.notifaction.Repository.MessageRepo;

public class NotificationListener extends NotificationListenerService {

    private static Boolean isListeningEnabled = true;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if (isListeningEnabled) {
            String packageName = sbn.getPackageName();
            CharSequence notificationTitle = sbn.getNotification().extras.getCharSequence("android.title");
            CharSequence notificationText = sbn.getNotification().extras.getCharSequence("android.text");
            Log.d("Package", packageName);
            Message msg = new Message(packageName, notificationTitle.toString(), notificationText.toString());
            MessageRepo.addMessage(msg);
//            Log.d("Size msg", String.valueOf(MessageRepo.getMsgList().getValue().size()) + notificationText);
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
