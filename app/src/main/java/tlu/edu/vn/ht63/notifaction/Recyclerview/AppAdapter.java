package tlu.edu.vn.ht63.notifaction.Recyclerview;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;
import java.util.StringJoiner;

import tlu.edu.vn.ht63.notifaction.Interface.CheckButtonInterface;
import tlu.edu.vn.ht63.notifaction.Model.AppInfo;
import tlu.edu.vn.ht63.notifaction.R;
import tlu.edu.vn.ht63.notifaction.Repository.AppPermissionRepo;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    private List<AppInfo> appList;

    public AppAdapter(List<AppInfo> appList) {
        this.appList = appList;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        AppInfo appInfo = appList.get(position);
        String appName = appInfo.getName();
        if (AppPermissionRepo.getAppPermission().contains(appInfo)) {
            holder.switchButton.setChecked(true);
            holder.boxApp.setBackgroundResource(R.drawable.border);
        }
        else{
            holder.switchButton.setChecked(false);
            holder.boxApp.setBackgroundResource(0);
        }
        holder.appName.setText(appName);
        holder.appIcon.setImageDrawable(appInfo.getIcon());

        CheckButtonInterface checkButtonClick = new CheckButtonInterface() {
            @Override
            public void checked() {
                holder.boxApp.setBackgroundResource(R.drawable.border);
                AppPermissionRepo.addAppPermission(appInfo);
            }

            @Override
            public void unchecked() {
                holder.boxApp.setBackgroundResource(0);
                AppPermissionRepo.removeAppPermission(appInfo);
            }
        };
        holder.switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.switchButton.isChecked()) {
                    checkButtonClick.checked();
                } else {
                    checkButtonClick.unchecked();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder {
        TextView appName;
        ImageView appIcon;
        SwitchMaterial switchButton;
        ConstraintLayout boxApp;

        public AppViewHolder(View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.app_name);
            appIcon = itemView.findViewById(R.id.app_icon);
            switchButton = itemView.findViewById(R.id.checkButton);
            boxApp = itemView.findViewById(R.id.boxApp);
        }
    }
}
