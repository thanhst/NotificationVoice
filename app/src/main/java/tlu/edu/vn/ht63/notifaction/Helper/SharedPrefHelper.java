package tlu.edu.vn.ht63.notifaction.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {

    private static final String PREF_NAME = "ServiceStatePrefs";
    private static final String KEY_SERVICE_RUNNING = "service_running";

    // Lưu trạng thái service (đang chạy hay không)
    public static void setServiceRunning(Context context, boolean isRunning) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_SERVICE_RUNNING, isRunning);
        editor.apply();
    }

    // Lấy trạng thái service (đang chạy hay không)
    public static boolean isServiceRunning(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_SERVICE_RUNNING, false);
    }

}
