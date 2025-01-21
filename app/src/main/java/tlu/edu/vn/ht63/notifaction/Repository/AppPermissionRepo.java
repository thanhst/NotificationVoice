package tlu.edu.vn.ht63.notifaction.Repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tlu.edu.vn.ht63.notifaction.Database.AppInfoDB;
import tlu.edu.vn.ht63.notifaction.Model.AppInfo;

public class AppPermissionRepo {
    private static final List<AppInfo> appPermission = new ArrayList<>();
    private static AppInfoDB db;

    public static void init(Context context) {
        db = new AppInfoDB(context);
        loadFromDatabase();
    }
    public static void loadFromDatabase() {
        appPermission.clear();
        appPermission.addAll(db.getAllAppInfo());
        shortListApp();
    }
    public static List<AppInfo> getAppPermission(){
        shortListApp();
        return appPermission;
    }
    public static void setAppPermission(List<AppInfo> appList){
        appPermission.clear();
        appPermission.addAll(appList);
    }
    public static void addAppPermission(AppInfo app){
        appPermission.add(app);
        db.addAppInfo(app);
    }
    public static void removeAppPermission(AppInfo app){
        appPermission.remove(app);
        db.deleteAppInfo(app);
    }
    public static void shortListApp(){
        appPermission.sort(new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo app1, AppInfo app2) {
                return app1.getName().compareTo(app2.getName());
            }
        });
    }
}
