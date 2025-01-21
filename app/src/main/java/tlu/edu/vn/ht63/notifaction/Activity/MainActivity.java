package tlu.edu.vn.ht63.notifaction.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import tlu.edu.vn.ht63.notifaction.Helper.AppInfoHelper;
import tlu.edu.vn.ht63.notifaction.Helper.SharedPrefHelper;
import tlu.edu.vn.ht63.notifaction.Model.AppInfo;
import tlu.edu.vn.ht63.notifaction.PermissionEvent.PermissionUtils;
import tlu.edu.vn.ht63.notifaction.Recyclerview.AppAdapter;
import tlu.edu.vn.ht63.notifaction.Service.BackgroundService;
import tlu.edu.vn.ht63.notifaction.databinding.ActivityMainBinding;

import tlu.edu.vn.ht63.notifaction.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStartService = findViewById(R.id.btnStartService);
        Button btnStopService = findViewById(R.id.btnStopService);

        if(!PermissionUtils.hasUsageStatsPermission(this)){
            PermissionUtils.requestUsageStatsPermission(this);
        }
        if (SharedPrefHelper.isServiceRunning(this)) {
            btnStartService.setEnabled(false);
            btnStopService.setEnabled(true);
        } else {
            btnStartService.setEnabled(true);
            btnStopService.setEnabled(false);
        }
        if (!isNotificationListenerEnabled()) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }

        Intent serviceIntent = new Intent(MainActivity.this, BackgroundService.class);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(serviceIntent);
                SharedPrefHelper.setServiceRunning(MainActivity.this, true);  // Lưu trạng thái service đang chạy
                btnStartService.setEnabled(false);
                btnStopService.setEnabled(true);
            }
        });
        // Khi bấm nút Stop, dừng service và lưu trạng thái
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(serviceIntent);
                SharedPrefHelper.setServiceRunning(MainActivity.this, false);  // Lưu trạng thái service đã dừng
                btnStartService.setEnabled(true);
                btnStopService.setEnabled(false);
            }
        });
        AppInfoHelper appHelper = new AppInfoHelper(this);
        appHelper.displayAppInfo();
        RecyclerView recyclerViewApp = findViewById(R.id.recyclerViewApp);
        recyclerViewApp.setAdapter(new AppAdapter(appHelper.getAppInfos(),getPackageManager()));
        recyclerViewApp.setLayoutManager(new GridLayoutManager(this,1));
    }
    private boolean isNotificationListenerEnabled() {
        String enabledListeners = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        return enabledListeners != null && enabledListeners.contains(getPackageName());
    }
}