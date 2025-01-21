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
//        Log.d("BackgroundService", "Service đã được tạo.");

        // Tạo thông báo cho Foreground Service
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Ứng dụng đang chạy")
                    .setContentText("Đang giám sát thông báo")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(android.R.drawable.ic_notification_overlay)
                    .build();
        }

        // Khởi tạo TextToSpeech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int langResult = textToSpeech.setLanguage(Locale.getDefault());
                if (langResult == TextToSpeech.LANG_MISSING_DATA
                        || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
//                    Log.e("TTS", "Language is not supported or missing data.");
                }
            } else {
//                Log.e("TTS", "Initialization failed.");
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
//        Log.d("BackgroundService", "BG is being destroyed.");
        NotificationListener.setListeningEnabled(false);
        if (messageObserver != null) {
            MessageRepo.getMsgList().removeObserver(messageObserver);
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

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
//        Log.e("TextToSpeech", "TextToSpeech is ready or already speaking.");
        messageObserver  = messages -> {
            if (messages != null && !messages.isEmpty()) {
                // Lấy thông báo đầu tiên trong danh sách
                Message firstMessage = messages.get(0);
                String textToRead = "Thông báo: " + firstMessage.getTitle() + ", Nội dung thông báo : " + firstMessage.getContent();
                Log.d("Text", textToRead);
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
//                        Log.e("TextToSpeech", "Error in speech synthesis.");
                    }
                });
                if (textToSpeech != null && !textToSpeech.isSpeaking()) {
                    textToSpeech.speak(textToRead, TextToSpeech.QUEUE_ADD, null, "message_utterance_id");
                } else {
//                    Log.e("TextToSpeech", "TextToSpeech is not ready or already speaking.");
                }
            } else {
                Log.d("TextToSpeech", "No messages to read.");
            }
        };
        MessageRepo.getMsgList().observeForever(messageObserver);
    }
}
