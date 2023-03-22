package com.uni.cameraplugin.activity;

import MvCameraControlWrapper.CameraControlException;
import MvCameraControlWrapper.MvCameraControlDefines;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.displayimage.MultipleGLSurfaceView;
import com.example.displayimage.PixelFormat;
import com.uni.cameraplugin.R;

import java.io.File;
import java.util.ArrayList;

import static MvCameraControlWrapper.MvCameraControlDefines.MV_OK;

public class CameraActivity extends AppCompatActivity {

    private TextView tvLog;
    private RelativeLayout glViewGroup;
    private ImageView imgTest;
    private Button btnTakePhoto;

    private CameraManager cameraManager;
    private ArrayList<MvCameraControlDefines.MV_CC_DEVICE_INFO> deviceList = new ArrayList<>();
    private CameraProcess cameraProcess;
    private OpenDeviceThread openDeviceThread;
    private final int[] pixelFormatValue = {PixelFormat.MONO8, PixelFormat.YUV422, PixelFormat.RGB};
    boolean debug = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        tvLog = findViewById(R.id.tv_log);
        glViewGroup = findViewById(R.id.glViewGroup);
        imgTest = findViewById(R.id.img_test);
        btnTakePhoto = findViewById(R.id.tv_button);
        debug = getIntent().getIntExtra("debug", 0) == 1;
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
            if (!deviceList.isEmpty()) {
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


    /**
     * 日志输出信息
     *
     * @param errorMsg
     * @param msg
     */
    private void setLog(boolean errorMsg, String msg) {
        if (debug) {
            runOnUiThread(() -> {
                if (tvLog.getVisibility() == View.GONE) {
                    tvLog.setVisibility(View.VISIBLE);
                }
                tvLog.append("\n");
                tvLog.append(errorMsg ? "错误信息" : "" + msg);
            });
        }
    }

    /**
     * 拍照
     *
     * @param view
     */
    public void getImage(View view) {
        if (!takePhoto && canTakePhoto && !dealingPhoto) {
            takePhoto = true;
            runOnUiThread(() -> Toast.makeText(CameraActivity.this, "正在采集图像 请稍后...", Toast.LENGTH_LONG).show());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getOneFrameThread != null) {
            if (getOneFrameThread.isAlive()) {
                getOneFrameThread.closeflag = true;
                getOneFrameThread.flag = false;
            } else {
                cameraManager.stopDevice();
                cameraManager.closeDevice();
                cameraManager.destroyHandle();
            }
        } else {
            cameraManager.stopDevice();
            cameraManager.closeDevice();
            cameraManager.destroyHandle();
        }
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
                    if (deviceList.get(0).transportLayerType == MvCameraControlDefines.MV_GIGE_DEVICE) {
                        int nPacketSize = cameraManager.getOptimalPacketSize();
                        if (nPacketSize > 0) {
                            nRet = cameraManager.setIntValue("GevSCPSPacketSize", nPacketSize);
                            if (nRet != MV_OK) {
                                setLog(true, "Warning: Set Packet Size fail nRet" + Integer.toHexString(nRet));
                            } else {
                                setLog(false, "set GevSCPSPacketSize 1500");
                            }
                        } else {
                            setLog(true, "Warning: Get Packet Size fail nRet" + Integer.toHexString(nPacketSize));
                        }

                        int nRet1 = cameraManager.setFloatValue("ExposureTime", 700000f);
                        if (nRet1 == MV_OK) {
                            Log.e("Lyb", "设置曝光时间成功");
                        } else {
                            setLog(true, "设置曝光时间失败:" + Integer.toHexString(nRet1));
                        }
                        Integer GevSCPD = 0;
                        nRet = cameraManager.getIntValue("GevSCPD", GevSCPD);
                        if (nRet != MV_OK) {
                            setLog(true, "get GevSCPD fail nRet = " + Integer.toHexString(nRet));
                        } else {
                            setLog(false, "GevSCPD = " + GevSCPD);
                        }

                        nRet = cameraManager.setIntValue("GevSCPD", 2000);
                        if (nRet == MV_OK) {
                            setLog(false, "Set GevSCPD success");
                        } else {
                            setLog(true, "Set GevSCPD fail");
                        }
                        nRet = cameraManager.getIntValue("GevSCPD", GevSCPD);
                        if (nRet != MV_OK) {
                            setLog(true, "get GevSCPD fail nRet = " + Integer.toHexString(nRet));
                        } else {
                            setLog(false, "GevSCPD = " + GevSCPD);
                        }
                    }
                    final Integer width = 0;
                    nRet = cameraManager.getIntValue("Width", width);
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        setLog(false, "get Width fail nRet = " + Integer.toHexString(nRet));
                        return;
                    }
                    final Integer height = 0;
                    nRet = cameraManager.getIntValue("Height", height);
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        setLog(true, "get Height fail nRet = " + Integer.toHexString(nRet));
                        return;
                    }
                    nRet = cameraManager.setEnumValue("PixelFormat", pixelFormatValue[2]);
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        setLog(true, "set PixelFormat fail nRet = " + Integer.toHexString(nRet));
                    }
                    final Integer pixelFormat = 0;
                    nRet = cameraManager.getEnumValue("PixelFormat", pixelFormat);
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        setLog(true, "get PixelFormat fail nRet = " + Integer.toHexString(nRet));
                        return;
                    }
                    runOnUiThread(() -> updatePixelFormatView(pixelFormat, width, height));
                    nRet = cameraManager.startDevice();
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        setLog(false, "startDevice" + Integer.toHexString(nRet));
                    }
                    if (getOneFrameThread == null) {
                        getOneFrameThread = new GetBitMapThread();
                    }
                    getOneFrameThread.flag = true;
                    getOneFrameThread.runFlag = true;
                    getOneFrameThread.start();
                }
            } catch (CameraControlException e) {
                e.printStackTrace();
                setLog(true, e.errMsg + e.errCode);
            }
        }
    }

    //endregion
    int disPlayPixelFormat = 0;
    int width = 0;
    int height = 0;
    private MultipleGLSurfaceView multipleGLSurfaceView;

    private void updatePixelFormatView(final Integer pixelFormat, final Integer w, final Integer h) {
        if (disPlayPixelFormat != pixelFormat || w != width || h != height) {
            disPlayPixelFormat = pixelFormat;
            width = w;
            height = h;
            glViewGroup.removeAllViews();
            multipleGLSurfaceView = new MultipleGLSurfaceView(this, () -> {
                multipleGLSurfaceView.pixelFormat(pixelFormat);
                multipleGLSurfaceView.setSize(w, h);
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            multipleGLSurfaceView.setLayoutParams(params);
            glViewGroup.addView(multipleGLSurfaceView);

        }
    }

    /**
     * 将帧渲染到GLSurface 上
     *
     * @param data
     */
    public void updateImage(byte[] data) {
        multipleGLSurfaceView.updateImage(data);
    }

    GetBitMapThread getOneFrameThread;
    boolean takePhoto = false;//是否能拍照
    boolean canTakePhoto = false;//拍照
    boolean dealingPhoto = false;//正在处理图片

    class GetBitMapThread extends Thread {
        boolean runFlag = true;
        boolean flag = true;
        boolean stopflag = false;
        boolean closeflag = false;
        byte[] bytes = null;
        MvCameraControlDefines.MV_FRAME_OUT_INFO info = new MvCameraControlDefines.MV_FRAME_OUT_INFO();

        @Override
        public void run() {
            super.run();
            Integer width = 0;
            Integer height = 0;
            int nRetW = cameraManager.getIntValue("Width", width);
            int nRetH = cameraManager.getIntValue("Height", height);
            if (nRetW == MV_OK && nRetH == MV_OK) {
                if (bytes == null) {
                    bytes = new byte[width * height * 3];
                } else {
                    if (bytes.length < width * height * 3) {
                        bytes = new byte[width * height * 3];
                    }
                }
            } else {
                setLog(true, "获取图像宽高失败");
                return;
            }
            while (runFlag) {
                if (flag) {
                    int nRet = cameraManager.getBitMapTimeout(bytes, info, 1000);
                    if (nRet == 0) {
                        updateImage(bytes);
                        //                        String str = "width--------------" + info.width + "\n" + "height-------------" + info.height + "\n" + "pixelType----------" + info.pixelType.getnValue() + "\n" + "frameNum-----------" + info.frameNum + "\n" + "devTimeStampHigh---" + info.devTimeStampHigh + "\n" + "ddevTimeStampLow----" + info.devTimeStampLow + "\n" + "hostTimeStamp------" + info.hostTimeStamp + "\n" + "frameLen-----------" + info.frameLen + "\n" + "secondCount--------" + info.secondCount + "\n" + "cycleCount---------" + info.cycleCount + "\n" + "cycleOffset--------" + info.cycleOffset + "\n" + "gain---------------" + info.gain + "\n" + "exposureTime-------" + info.exposureTime + "\n" + "averageBrightness--" + info.averageBrightness + "\n" + "red----------------" + info.red + "\n" + "green--------------" + info.green + "\n" + "blue---------------" + info.blue + "\n" + "frameCounter-------" + info.frameCounter + "\n" + "triggerIndex-------" + info.triggerIndex + "\n" + "input--------------" + info.input + "\n" + "output-------------" + info.output + "\n" + "offsetX------------" + info.offsetX + "\n" + "offsetY------------" + info.offsetY + "\n" + "chunkWidth---------" + info.chunkWidth + "\n" + "chunkHeight--------" + info.chunkHeight + "\n" + "lostPacket---------" + info.lostPacket;
                        //                        Log.e("Lyb", "" + str);
                        canTakePhoto = true;
                        setBtnState(true);
                        if (takePhoto) {
                            takePhoto = false;
                            dealingPhoto = true;
                            File file = cameraProcess.dealFrameToPicture(bytes);
                            setSaveImage(file);
                            dealingPhoto = false;
                        }
                    }

                } else {
                    if (stopflag) {
                        stopflag = false;
                        Log.e("StopGrabThread", "StopGrabThread");
                        int nRet = cameraManager.stopDevice();
                        if (nRet != MvCameraControlDefines.MV_OK) {
                            setLog(false, "stopDevice" + Integer.toHexString(nRet));
                        }
                    } else if (closeflag) {
                        closeflag = false;
                        cameraManager.stopDevice();
                        cameraManager.closeDevice();
                        cameraManager.destroyHandle();
                        runFlag = false;
                    }
                }
            }
        }
    }

    private void setSaveImage(File file) {
        runOnUiThread(() -> {
            if (file != null) {
                Glide.with(CameraActivity.this).load(file).into(imgTest);
                Toast.makeText(CameraActivity.this, "图片保存成功路径：" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CameraActivity.this, "图片保存失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBtnState(boolean show) {
        runOnUiThread(() -> btnTakePhoto.setVisibility(show ? View.VISIBLE : View.GONE));
    }
}
