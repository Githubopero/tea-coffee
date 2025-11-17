package com.example.teacoffee;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class NavigationHelper {
    public static void setupAdminTabs(Activity activity, int currentTabId) {
        MaterialButtonToggleGroup tabs = activity.findViewById(R.id.tabsGroup);
        MaterialButton tabStaff   = activity.findViewById(R.id.btnTabStaff);
        MaterialButton tabProduct = activity.findViewById(R.id.btnTabProduct);
        MaterialButton tabStats   = activity.findViewById(R.id.btnTabStats);
        MaterialButton tabDanhMuc   = activity.findViewById(R.id.btnTabDanhMuc);

        if (tabs == null) return;
        tabs.check(currentTabId);

        if (tabStaff != null) {
            tabStaff.setOnClickListener(v -> {
                if (currentTabId != R.id.btnTabStaff) {
                    Intent i = new Intent(activity, AdminHomeActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                }
            });
        }

//        if (tabProduct != null) {
//            tabProduct.setOnClickListener(v -> {
//                if (currentTabId != R.id.btnTabProduct) {
//                    Intent i = new Intent(activity, ProductActivity.class);
//                    activity.startActivity(i);
//                    activity.finish();
//                }
//            });
//        }

        if (tabStats != null) {
            tabStats.setOnClickListener(v -> {
                if (currentTabId != R.id.btnTabStats) {
                    Intent i = new Intent(activity, ReportActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                }
            });
        }
//        if (tabStats != null) {
//            tabStats.setOnClickListener(v -> {
//                if (currentTabId != R.id.btnTabDanhMuc) {
//                    Intent i = new Intent(activity, DanhMucActivity.class);
//                    activity.startActivity(i);
//                    activity.finish();
//                }
//            });
//        }
    }
}
