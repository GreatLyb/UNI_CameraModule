package com.uni.cameraplugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.uni.cameraplugin.activity.CameraActivity;
import com.uni.cameraplugin.activity.CameraCore;
import com.uni.cameraplugin.activity.DemoMainActivity;
import com.uni.cameraplugin.imp.CameraInitCallBack;
import com.uni.cameraplugin.imp.TakePhotoCallBack;

import java.util.HashMap;
import java.util.Map;

import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class CameraModule extends UniModule {
    private static final String TAG = "CameraModule";
    public static int REQUEST_CODE = 1000;
    private CameraCore cameraCore;

    public CameraCore getCameraCore(Context context) {
        if (cameraCore == null) {
            cameraCore = new CameraCore(context);
        }
        return cameraCore;
    }


    //run ui thread
    @UniJSMethod(uiThread = true)
    public void openCamera(String jsonPara) {
        Log.e(TAG, "uniapp的jsonPara==" + jsonPara);
        if (mUniSDKInstance != null && mUniSDKInstance.getContext() instanceof Activity) {
            Intent intent = new Intent(mUniSDKInstance.getContext(), CameraActivity.class);
            intent.putExtra("caremaPara", jsonPara);
            ((Activity) mUniSDKInstance.getContext()).startActivityForResult(intent, REQUEST_CODE);
        }
    }

    /**
     * 初始化相机
     *
     * @param jsonPara
     * @param callback
     */
    @UniJSMethod(uiThread = true)
    public void initCamera(String jsonPara, UniJSCallback callback) {
        Log.e(TAG, "uniapp的jsonPara==" + jsonPara);
        if (mUniSDKInstance != null) {
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            if (CameraCore.initing) {
                //正在初始化中
                jsonMap.put("state", 0);
                jsonMap.put("msg", "正在初始化中,请稍后再试");
                callback.invoke(JSONObject.toJSONString(jsonMap));
            } else if (CameraCore.isInit) {
                //已经初始化过
                jsonMap.put("state", 1);
                jsonMap.put("msg", "初始化成功");
                callback.invoke(JSONObject.toJSONString(jsonMap));
            } else {
                getCameraCore(mUniSDKInstance.getContext()).initCamera(jsonPara, new CameraInitCallBack() {
                    @Override
                    public void initSuccess() {
                        //初始化成功
                        jsonMap.put("state", 1);
                        jsonMap.put("msg", "初始化成功");
                        callback.invoke(JSONObject.toJSONString(jsonMap));
                    }

                    @Override
                    public void initFail(String msg) {
                        jsonMap.put("state", 0);
                        jsonMap.put("msg", "初始化失败" + msg);
                        callback.invoke(JSONObject.toJSONString(jsonMap));
                    }
                });
            }

        }
    }

    /**
     * 拍照
     * @param callback
     */
    @UniJSMethod(uiThread = true)
    public void takePhoto(UniJSCallback callback) {
        if (mUniSDKInstance != null && mUniSDKInstance.getContext() instanceof Activity) {
            Map<String, Object> jsonMap = new HashMap<String, Object>();

            CameraCore cameraCore = getCameraCore(mUniSDKInstance.getContext());
            cameraCore.takePhoto(new TakePhotoCallBack() {
                @Override
                public void takeSuccess(String path) {
                    jsonMap.put("state", 1);
                    jsonMap.put("msg", "拍照成功");
                    jsonMap.put("path",path);
                    callback.invoke(JSONObject.toJSONString(jsonMap));
                    cameraCore.removeTakePhotoCallBack(this);
                }

                @Override
                public void takeFail(String errorMsg) {
                    jsonMap.put("state", 0);
                    jsonMap.put("msg", "拍照失败"+errorMsg);
                    callback.invoke(JSONObject.toJSONString(jsonMap));
                    cameraCore.removeTakePhotoCallBack(this);
                }
            });
        }
    }


}
