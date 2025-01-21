package tlu.edu.vn.ht63.notifaction.Helper;

import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tlu.edu.vn.ht63.notifaction.Model.AppInfo;

public class AppInfoHelper {

    private Context context;
    private List<AppInfo> appInfos = new ArrayList<>();

    public AppInfoHelper(Context context) {
        this.context = context;
    }

    // Lấy danh sách các ứng dụng đã cài đặt
    public List<ApplicationInfo> getInstalledApps() {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<ApplicationInfo> userApps = new ArrayList<>();

        for (ApplicationInfo appInfo : apps) {
            // Kiểm tra nếu đây là ứng dụng người dùng, không phải ứng dụng hệ thống
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                userApps.add(appInfo);
            }
        }
        return userApps;
    }

    // Lấy tên và icon của ứng dụng
    public void displayAppInfo() {
        List<ApplicationInfo> installedApps = getInstalledApps();
        for (ApplicationInfo appInfo : installedApps) {
            String appName = appInfo.loadLabel(context.getPackageManager()).toString();
            String packageName = appInfo.packageName;
            Drawable appIcon = appInfo.loadIcon(context.getPackageManager());
            AppInfo app = new AppInfo(appName,packageName,appIcon);
            appInfos.add(app);
        }
    }


    public List<AppInfo> getAppInfos(){
        displayAppInfo();
        Collections.sort(appInfos,new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo app1, AppInfo app2) {
                return app1.getName().compareTo(app2.getName());
            }
        });
        return appInfos;
    }
}
