package com.uni.cameraplugin.activity;

import MvCameraControlWrapper.CameraControlException;
import MvCameraControlWrapper.MvCameraControlDefines;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.displayimage.IGlobalLayout;
import com.example.displayimage.MultipleGLSurfaceView;
import com.example.displayimage.PixelFormat;
import com.uni.cameraplugin.R;
import com.uni.cameraplugin.ui.ButtonState;

import java.util.ArrayList;

import static MvCameraControlWrapper.MvCameraControlDefines.MV_OK;

public class CameraActivity extends AppCompatActivity {

    private TextView tvLog;
    private RelativeLayout glViewGroup;

    private CameraManager cameraManager;
    private ArrayList<MvCameraControlDefines.MV_CC_DEVICE_INFO> deviceList = new ArrayList<>();
    private CameraProcess cameraProcess;
    private OpenDeviceThread openDeviceThread;
    private int[] pixelFormatValue = {PixelFormat.MONO8, PixelFormat.YUV422, PixelFormat.RGB};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        tvLog = findViewById(R.id.tv_log);
        glViewGroup = findViewById(R.id.glViewGroup);

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
            tvLog.append("\n");
            tvLog.append(msg);
//            tvLog.setText(msg);
//            tvLog.setTextColor(errorMsg ? Color.parseColor("#eb4035") : Color.parseColor("#000000"));
        });
    }

    public void getImage(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getOneFrameThread != null) {
            if (getOneFrameThread.isAlive()) {
                getOneFrameThread.closeflag = true;
                getOneFrameThread.flag = false;
            } else {
                int nRet = cameraManager.stopDevice();
                nRet = cameraManager.closeDevice();
                cameraManager.destroyHandle();
            }
        } else {
            int nRet = cameraManager.stopDevice();
            nRet = cameraManager.closeDevice();
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

                        int nRet1 = cameraManager.setFloatValue("ExposureTime", 1400000f);
                        if (nRet1 == 0) {
                            Log.e("Lyb", "设置曝光时间成功");
                        } else {
                            setLog(true,"设置曝光时间失败:" + Integer.toHexString(nRet1));
                        }
                        Integer GevSCPD = new Integer(0);
                        nRet = cameraManager.getIntValue("GevSCPD", GevSCPD);
                        if (nRet != MV_OK) {
                            setLog(true, "get GevSCPD fail nRet = " + Integer.toHexString(nRet));
                        } else {
                            setLog(false, "GevSCPD = " + GevSCPD.intValue());
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
                            setLog(false, "GevSCPD = " + GevSCPD.intValue());
                        }
                    }


                    final Integer width = new Integer(0);
                    nRet = cameraManager.getIntValue("Width", width);
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        setLog(false, "get Width fail nRet = " + Integer.toHexString(nRet));
                        return;
                    }

                    final Integer height = new Integer(0);
                    nRet = cameraManager.getIntValue("Height", height);
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        setLog(true, "get Height fail nRet = " + Integer.toHexString(nRet));
                        return;
                    }


                    nRet = cameraManager.setEnumValue("PixelFormat", pixelFormatValue[0]);
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        setLog(true, "set PixelFormat fail nRet = " + Integer.toHexString(nRet));
                    }


                    final Integer pixelFormat = new Integer(0);
                    nRet = cameraManager.getEnumValue("PixelFormat", pixelFormat);
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        setLog(true, "get PixelFormat fail nRet = " + Integer.toHexString(nRet));
                        return;
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updatePixelFormatView(pixelFormat, width, height);
                        }
                    });


                    nRet = cameraManager.startDevice();
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        setLog(false, "startDevice" + Integer.toHexString(nRet));
                    }


                    if (getOneFrameThread == null) {
                        getOneFrameThread = new GetOneFrameThread();
                        getOneFrameThread.flag = true;
                        getOneFrameThread.runFlag = true;
                        getOneFrameThread.start();
                    } else {
                        getOneFrameThread.flag = true;
                        getOneFrameThread.runFlag = true;
                        getOneFrameThread.start();
                    }
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
            multipleGLSurfaceView = new MultipleGLSurfaceView(this, new IGlobalLayout() {
                @Override
                public void lodfinish() {
                    multipleGLSurfaceView.pixelFormat(pixelFormat);
                    multipleGLSurfaceView.setSize(w.intValue(), h.intValue());
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            multipleGLSurfaceView.setLayoutParams(params);
            glViewGroup.addView(multipleGLSurfaceView);

        }
    }

    public void updateImage(byte[] data) {
        multipleGLSurfaceView.updateImage(data);
    }

    GetOneFrameThread getOneFrameThread;

    class GetOneFrameThread extends Thread {
        boolean runFlag = true;
        boolean flag = true;
        boolean stopflag = false;
        boolean closeflag = false;
        byte[] bytes = null;
        MvCameraControlDefines.MV_FRAME_OUT_INFO info = new MvCameraControlDefines.MV_FRAME_OUT_INFO();

        @Override
        public void run() {
            super.run();
            Integer width = new Integer(0);
            Integer height = new Integer(0);

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
            info.exposureTime = 800000f;


            while (runFlag) {
                if (flag) {
                    int nRet = cameraManager.getOneFrameTimeout(bytes, info, 1000);
                    if (nRet == 0) {

                        updateImage(bytes);

                        String str =
                                "width--------------" + info.width + "\n" +
                                        "height-------------" + info.height + "\n" +
                                        "pixelType----------" + info.pixelType.getnValue() + "\n" +
                                        "frameNum-----------" + info.frameNum + "\n" +
                                        "devTimeStampHigh---" + info.devTimeStampHigh + "\n" +
                                        "ddevTimeStampLow----" + info.devTimeStampLow + "\n" +
                                        "hostTimeStamp------" + info.hostTimeStamp + "\n" +
                                        "frameLen-----------" + info.frameLen + "\n" +
                                        "secondCount--------" + info.secondCount + "\n" +
                                        "cycleCount---------" + info.cycleCount + "\n" +
                                        "cycleOffset--------" + info.cycleOffset + "\n" +
                                        "gain---------------" + info.gain + "\n" +
                                        "exposureTime-------" + info.exposureTime + "\n" +
                                        "averageBrightness--" + info.averageBrightness + "\n" +
                                        "red----------------" + info.red + "\n" +
                                        "green--------------" + info.green + "\n" +
                                        "blue---------------" + info.blue + "\n" +
                                        "frameCounter-------" + info.frameCounter + "\n" +
                                        "triggerIndex-------" + info.triggerIndex + "\n" +
                                        "input--------------" + info.input + "\n" +
                                        "output-------------" + info.output + "\n" +
                                        "offsetX------------" + info.offsetX + "\n" +
                                        "offsetY------------" + info.offsetY + "\n" +
                                        "chunkWidth---------" + info.chunkWidth + "\n" +
                                        "chunkHeight--------" + info.chunkHeight + "\n" +
                                        "lostPacket---------" + info.lostPacket;


                        Log.e("Lyb", "" + str);

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
                        int nRet = cameraManager.stopDevice();
                        nRet = cameraManager.closeDevice();
                        cameraManager.destroyHandle();

                        runFlag = false;
                    }
                }
            }
        }
    }

}