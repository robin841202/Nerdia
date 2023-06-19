package com.robinhsueh.nerdia.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.robinhsueh.nerdia.R;

import java.util.Locale;

public class InAppUpdate implements InstallStateUpdatedListener {
    private final String LOG_TAG = "InAppUpdate";

    private final AppUpdateManager appUpdateManager;
    private final int MY_REQUEST_CODE = 500;
    private final Activity parentActivity;
    private int currentType = AppUpdateType.FLEXIBLE;


    public InAppUpdate(Activity activity){
        parentActivity = activity;
        appUpdateManager = AppUpdateManagerFactory.create(parentActivity);

        checkUpdate();

        appUpdateManager.registerListener(this);
    }

    /**
     * Checking update availability and update type
     */
    private void checkUpdate(){
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(info -> {
            if(info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                startUpdate(info, AppUpdateType.IMMEDIATE);
            }
        });
    }

    /**
     * Start update flow
     * @param info AppUpdateInfo object
     * @param type update type
     */
    private void startUpdate(AppUpdateInfo info, int type) {
        try {
            appUpdateManager.startUpdateFlowForResult(info, parentActivity, AppUpdateOptions.defaultOptions(type), MY_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            throw new RuntimeException(e);
        }
        currentType = type;
    }

    /**
     * Add listener back when activity onDestroy
     */
    public void onResume() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(info -> {
            if (currentType == AppUpdateType.FLEXIBLE) {
                // If the update is downloaded but not installed, notify the user to complete the update.
                if (info.installStatus() == InstallStatus.DOWNLOADED)
                    flexibleUpdateDownloadCompleted();
            } else if (currentType == AppUpdateType.IMMEDIATE) {
                // for AppUpdateType.IMMEDIATE only, already executing updater
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    startUpdate(info, AppUpdateType.IMMEDIATE);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != AppCompatActivity.RESULT_OK) {
                Log.e(LOG_TAG, String.format(Locale.TAIWAN, "Update flow failed! Result code: %s", resultCode));
                // If the update is cancelled or fails, you can request to start the update again.
                checkUpdate();
            }
        }
    }

    /**
     * Show snackbar msg when flexible update downloaded
     */
    private void flexibleUpdateDownloadCompleted() {
        Snackbar.make(parentActivity.findViewById(R.id.bottomNavView), R.string.label_ready_to_update, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.label_install, view -> appUpdateManager.completeUpdate())
                .setActionTextColor(parentActivity.getColor(R.color.white))
                .show();
    }

    /**
     * Unregister listener when activity onDestroy
     */
    public void onDestroy() {
        appUpdateManager.unregisterListener(this);
    }

    /**
     * Triggered when update state change
     * @param installState install state
     */
    @Override
    public void onStateUpdate(@NonNull InstallState installState) {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            // show ready to update message
            flexibleUpdateDownloadCompleted();
        }
    }
}
