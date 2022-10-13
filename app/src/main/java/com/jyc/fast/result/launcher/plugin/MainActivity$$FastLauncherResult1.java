package com.jyc.fast.result.launcher.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

/// @author jyc
/// 创建日期：2022/10/11
/// 描述：MainActivity$$FastLauncherResult
public class MainActivity$$FastLauncherResult1<Host extends MainActivity> {

    public void initStartActivityResult(final Host host) {
        ActivityResultLauncher<Intent> initStartActivityResult = host.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new StartActivityResult$$CallBack(host));

        ActivityResultLauncher<Intent> initStartActivityResult1 = host.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();
                        int resultCode = result.getResultCode();
                    }
                });

        host.startActivityResult = initStartActivityResult;
    }

    class StartActivityResult$$CallBack implements ActivityResultCallback<ActivityResult>{

        private final Host host;
        public StartActivityResult$$CallBack(final Host host) {
            this.host = host;
        }

        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            int resultCode = result.getResultCode();


            if (resultCode == Activity.RESULT_OK) {
                host.onStartActivityResult(data);
            }
        }
    }
}
