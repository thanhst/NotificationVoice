package tlu.edu.vn.ht63.notifaction.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.Locale;

import tlu.edu.vn.ht63.notifaction.Activity.MainActivity;
import tlu.edu.vn.ht63.notifaction.Helper.CheckContentMessageHelper;
import tlu.edu.vn.ht63.notifaction.Helper.SharedPrefHelper;
import tlu.edu.vn.ht63.notifaction.Listener.NotificationListener;
import tlu.edu.vn.ht63.notifaction.Model.Message;
import tlu.edu.vn.ht63.notifaction.Repository.MessageRepo;

public class BackgroundService extends Service {
    private static final String CHANNEL_ID = "BackgroundServiceChannel";
    private Observer<List<Message>> messageObserver;

    private static TextToSpeech textToSpeech;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // Đưa Activity đã tồn tại lên foreground
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Ứng dụng đang chạy")
                    .setContentText("Đang giám sát thông báo")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(android.R.drawable.ic_notification_overlay)
                    .build();
        }

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int langResult = textToSpeech.setLanguage(Locale.getDefault());
                if (langResult == TextToSpeech.LANG_MISSING_DATA
                        || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                }
            } else {
            }
        });

        // Bắt đầu Foreground service
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationListenerIntent = new Intent(this, NotificationListener.class);
        startService(notificationListenerIntent);
        NotificationListener.setListeningEnabled(true);
        readMessages();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        NotificationListener.setListeningEnabled(false);
        if (messageObserver != null) {
            MessageRepo.getMsgList().removeObserver(messageObserver);
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        SharedPrefHelper.setServiceRunning(this, false);  // Lưu trạng thái service đã dừng
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Background Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void readMessages() {
        messageObserver  = messages -> {
            if (messages != null && !messages.isEmpty()) {
                Message firstMessage = messages.get(0);
                String textToRead = "Có thông báo!";
                String content= CheckContentMessageHelper.checkMessageBanking(firstMessage.getTitle(),firstMessage.getContent());
                if(content!=null){
                    textToRead = "Thông báo: Biến động số dư , Giao dịch: "+ content;
                }
                else{
                    textToRead = "Thông báo: " + firstMessage.getTitle() + ", Nội dung : " + firstMessage.getContent();
                }
//                Log.d("Text", textToRead);
                textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                    }
                    @Override
                    public void onDone(String utteranceId) {
                        if ("message_utterance_id".equals(utteranceId)) {
                            MessageRepo.removeMessage();
                        }
                    }
                    @Override
                    public void onError(String utteranceId) {
                    }
                });
                if (textToSpeech != null && !textToSpeech.isSpeaking()) {
                    textToSpeech.speak(textToRead, TextToSpeech.QUEUE_ADD, null, "message_utterance_id");
                } else {
                }
            } else {
    //                Log.d("TextToSpeech", "No messages to read.");
            }
        };
        MessageRepo.getMsgList().observeForever(messageObserver);
    }
}
