package com.uni.cameraplugin.activity;

import MvCameraControlWrapper.CameraControlException;
import MvCameraControlWrapper.MvCameraControlDefines;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.uni.cameraplugin.R;
import com.uni.cameraplugin.ui.ButtonState;

import java.util.ArrayList;

import static MvCameraControlWrapper.MvCameraControlDefines.MV_OK;

public class CameraActivity extends AppCompatActivity {

    private TextView tvLog;
    private CameraManager cameraManager;
    private ArrayList<MvCameraControlDefines.MV_CC_DEVICE_INFO> deviceList = new ArrayList<>();
    private CameraProcess cameraProcess;
    private OpenDeviceThread openDeviceThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        tvLog = findViewById(R.id.tv_log);
        initDevice();
    }

    /**
     * 初始化设备
     */
    private void initDevice() {
        cameraProcess = new CameraProcess(this);
        cameraManager = new CameraManager();
        Log.e("Lyb", "SDK版本" + cameraManager.GetSDKVersion());
        setLog(false, "SDK版本" + cameraManager.GetSDKVersion());
        try {
            deviceList.clear();
            deviceList = cameraManager.enumDevice();
            if (deviceList.size() > 0) {
                cameraProcess.getDeviceInfo(deviceList.get(0));
                setLog(false, "发现设备默认取第一个设备：" + cameraManager.GetSDKVersion());
                openDevice();
            } else {
                setLog(true, "未发现设备");
            }
        } catch (CameraControlException e) {
            e.printStackTrace();
            setLog(true, "枚举设备" + e);

        }

    }



    private void setLog(boolean errorMsg, String msg) {
        runOnUiThread(() -> {
            tvLog.setText(msg);
            tvLog.setTextColor(errorMsg ? Color.parseColor("#eb4035") : Color.parseColor("#000000"));
        });
    }

    public void getImage(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int nRet = cameraManager.stopDevice();
        nRet = cameraManager.closeDevice();
        cameraManager.destroyHandle();
    }

    //region 打开设备
    private void openDevice() {
        if (openDeviceThread == null) {
            openDeviceThread = new OpenDeviceThread();
            openDeviceThread.start();
        } else {
            if (!openDeviceThread.isAlive()) {
                openDeviceThread.start();
            }
        }
    }
    class OpenDeviceThread extends Thread {
        @Override
        public void run() {
            try {
                cameraManager.createHandle(deviceList.get(0));
                int nRet = cameraManager.openDevice();
                if (nRet != MvCameraControlDefines.MV_OK) {
                    setLog(true, "设备打开失败 nRet = " + Integer.toHexString(nRet));
                } else {
                    setLog(false, "设备打开成功");
                }
            } catch (CameraControlException e) {
                e.printStackTrace();
                setLog(true, e.errMsg + e.errCode);
            }
        }
    }
    //endregion


}