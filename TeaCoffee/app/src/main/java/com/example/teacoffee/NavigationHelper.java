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
        MaterialButton tabCategory   = activity.findViewById(R.id.btnTabCategory);

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

        if (tabProduct != null) {
            tabProduct.setOnClickListener(v -> {
                if (currentTabId != R.id.btnTabProduct) {
                    Intent i = new Intent(activity, AdminFoodManagement.class);
                    activity.startActivity(i);
                    activity.finish();
                }
            });
        }

        if (tabStats != null) {
            tabStats.setOnClickListener(v -> {
                if (currentTabId != R.id.btnTabStats) {
                    Intent i = new Intent(activity, ReportActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                }
            });
        }
        if (tabCategory != null) {
            tabCategory.setOnClickListener(v -> {
                if (currentTabId != R.id.btnTabCategory) {
                    Intent i = new Intent(activity, AdminCategoryActivity.class);
                    activity.startActivity(i);
                    activity.finish();
                }
            });
        }
    }
}
