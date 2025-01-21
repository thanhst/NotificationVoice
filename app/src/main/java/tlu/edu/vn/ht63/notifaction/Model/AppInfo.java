package tlu.edu.vn.ht63.notifaction.Model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppInfo implements Serializable,Comparable<AppInfo> {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    private String name;
    private String packageName;
    private Drawable icon;

    public AppInfo(String name, String packageName, Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AppInfo appInfo = (AppInfo) obj;
        return packageName.equals(appInfo.packageName) && name.equals(appInfo.name);
    }

    @Override
    public int hashCode() {
        int result = packageName.hashCode();
        result = 31 * result + name.hashCode(); // Kết hợp hash của packageName và name
        return result;
    }
    @Override
    public int compareTo(AppInfo other) {
        return this.packageName.compareTo(other.packageName);
    }
}
