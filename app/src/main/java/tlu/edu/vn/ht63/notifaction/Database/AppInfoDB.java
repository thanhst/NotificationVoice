package tlu.edu.vn.ht63.notifaction.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

import tlu.edu.vn.ht63.notifaction.Model.AppInfo;

public class AppInfoDB extends android.database.sqlite.SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notificationVoice";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "app_permission";
    private static final String COLUMN_PACKAGE_NAME = "package_name";
    private static final String COLUMN_APP_NAME = "app_name";
    private static final String COLUMN_IS_ENABLED = "is_enabled";

    // Câu lệnh tạo bảng
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_PACKAGE_NAME + " TEXT, "
            + COLUMN_APP_NAME + " TEXT, "
            + "PRIMARY KEY (" + COLUMN_PACKAGE_NAME + ", " + COLUMN_APP_NAME + "))";

    // Constructor
    public AppInfoDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE); // Tạo bảng khi cơ sở dữ liệu được tạo
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addAppInfo(AppInfo appInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PACKAGE_NAME, appInfo.getPackageName());
        values.put(COLUMN_APP_NAME, appInfo.getName());

        db.insert(TABLE_NAME, null, values);
    }

    public List<AppInfo> getAllAppInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        List<AppInfo> appInfoList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String packageName = cursor.getString(0);
                String appName = cursor.getString(1);
                appInfoList.add(new AppInfo(appName,packageName,null));
            }
            cursor.close();
        }

        return appInfoList;
    }

    // Lấy AppInfo theo package name
    public Cursor getAppInfoByPackage(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PACKAGE_NAME + " = ?", new String[]{packageName});
    }

    public int updateAppInfo(AppInfo appInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PACKAGE_NAME, appInfo.getPackageName());
        values.put(COLUMN_APP_NAME, appInfo.getName());

        // Cập nhật theo id
        return db.update(TABLE_NAME, values, COLUMN_PACKAGE_NAME + " = ? and " +COLUMN_APP_NAME + " = ?" , new String[]{appInfo.getPackageName(),appInfo.getName()});
    }

    // Xóa AppInfo
    public void deleteAppInfo(AppInfo appInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_PACKAGE_NAME + " = ? and " +COLUMN_APP_NAME + " = ?", new String[]{appInfo.getPackageName(),appInfo.getName()});
    }
}
