package com.uni.cameraplugin;

import android.util.Log;


import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class CameraModule extends UniModule {
    private static final String TAG = "CameraModule";

    //run ui thread
    @UniJSMethod(uiThread = true)
    public void openCamera(UniJSCallback callback) {
        Log.e(TAG, "openCamera--");
        if (callback != null) {
//            callback.invoke(data);
        }
    }


}
