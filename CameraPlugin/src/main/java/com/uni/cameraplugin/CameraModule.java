package com.uni.cameraplugin;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;


import com.uni.cameraplugin.activity.DemoMainActivity;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class CameraModule extends UniModule {
    private static final String TAG = "CameraModule";
    public static int REQUEST_CODE = 1000;

    //run ui thread
    @UniJSMethod(uiThread = true)
    public void openCamera(UniJSCallback callback) {
        Log.e(TAG, "openCamera--");
        if (callback != null) {
//            callback.invoke(data);
        }
        if(mUniSDKInstance != null && mUniSDKInstance.getContext() instanceof Activity) {
            Intent intent = new Intent(mUniSDKInstance.getContext(), DemoMainActivity.class);
            ((Activity)mUniSDKInstance.getContext()).startActivityForResult(intent, REQUEST_CODE);
        }
    }


}
