package com.uni.cameraplugin.activity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.displayimage.PixelFormat;
import com.uni.cameraplugin.bean.CameraBean;
import com.uni.cameraplugin.imp.CameraInitCallBack;
import com.uni.cameraplugin.imp.TakePhotoCallBack;
import com.uni.cameraplugin.imp.VideoStreamCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import MvCameraControlWrapper.CameraControlException;
import MvCameraControlWrapper.MvCameraControlDefines;

import static MvCameraControlWrapper.MvCameraControlDefines.MV_OK;

/**
 * @author 李亚彪
 * @date 2023/4/8
 */
public class CameraCore {

    public static final String TAG = "CameraCore";
    public static boolean initing = false;//初始化中

    public static boolean isInit = false;//是否已经初始化过

    private Context mContext;
    private CameraManager cameraManager;
    private ArrayList<MvCameraControlDefines.MV_CC_DEVICE_INFO> deviceList = new ArrayList<>();
    private CameraProcess cameraProcess;

    private OpenDeviceThread openDeviceThread;

    private final int[] pixelFormatValue = {PixelFormat.MONO8, PixelFormat.YUV422, PixelFormat.RGB};
    private CameraBean cameraBean;
    private VideoStreamCallBack videoStreamCallBack;

    public CameraCore(Context mContext) {
        this.mContext = mContext;
    }

    public void setVideoStreamCallBack(VideoStreamCallBack videoStreamCallBack) {
        this.videoStreamCallBack = videoStreamCallBack;
    }

    /**
     * 初始化相机
     *
     * @param caremaPara   相机参数
     * @param initCallBack 初始化回调
     */
    public void initCamera(String caremaPara, CameraInitCallBack initCallBack) {
        if (initing) {
            //初始化中
            initCallBack.initFail("正在初始化中请稍后....");
            return;
        }
        initing = true;
        if (cameraManager == null) {
            cameraManager = new CameraManager();
        }
        if (cameraProcess == null) {
            cameraProcess = new CameraProcess(mContext);
        }
        cameraBean = cameraProcess.getCameraBean(caremaPara);
        if (openDeviceThread == null) {
            openDeviceThread = new OpenDeviceThread();
            openDeviceThread.start(initCallBack);
        } else {
            if (!openDeviceThread.isAlive()) {
                openDeviceThread.start(initCallBack);
            }
        }

    }


    class OpenDeviceThread extends Thread {
        CameraInitCallBack initCallBack;

        public void start(CameraInitCallBack initCallBack) {
            this.initCallBack = initCallBack;
            super.start();
        }

        @Override
        public void run() {
            try {
                cameraManager.createHandle(deviceList.get(0));
                int nRet = cameraManager.openDevice();
                if (nRet != MvCameraControlDefines.MV_OK) {
                    Log.e(TAG, "设备打开失败 nRet = " + Integer.toHexString(nRet));
                    initing = false;
                    isInit = false;
                    initCallBack.initFail("设备打开失败 nRet = " + Integer.toHexString(nRet));
                } else {
                    if (deviceList.get(0).transportLayerType == MvCameraControlDefines.MV_GIGE_DEVICE) {
                        //设置IP
                        if (!TextUtils.isEmpty(cameraBean.getIp())) {
                            nRet = cameraManager.ForceIp(cameraBean.getIp(), cameraBean.getSubNetMask(), cameraBean.getDefaultGateWay());
                            if (nRet == MvCameraControlDefines.MV_OK) {
                                Log.e(TAG, "修改相机ip 成功" + "Ip==" + cameraBean.getIp() + "  subNetMask==" + cameraBean.getSubNetMask() + "  defaultGateWay==" + cameraBean.getDefaultGateWay());
                            } else {
                                Log.e(TAG, "MV_GIGE_ForceIpEx fail! nRet " + Integer.toHexString(nRet));
                                initing = false;
                                isInit = false;
                                initCallBack.initFail("MV_GIGE_ForceIpEx fail! nRet " + Integer.toHexString(nRet));
                                return;
                            }
                        } else {
                            Log.e(TAG, "IP为空 跳过设置IP地址");
                        }
                        //
                        int nPacketSize = cameraManager.getOptimalPacketSize();
                        if (nPacketSize > 0) {
                            nRet = cameraManager.setIntValue("GevSCPSPacketSize", nPacketSize);
                            if (nRet != MV_OK) {
                                Log.e(TAG, "Warning: Set Packet Size fail nRet" + Integer.toHexString(nRet));
                            } else {
                                Log.e(TAG, "set GevSCPSPacketSize 1500");
                            }
                        } else {
                            Log.e(TAG, "Warning: Get Packet Size fail nRet" + Integer.toHexString(nPacketSize));
                        }
                        Integer GevSCPD = new Integer(0);
                        nRet = cameraManager.getIntValue("GevSCPD", GevSCPD);
                        if (nRet != MV_OK) {
                            Log.e(TAG, "get GevSCPD fail nRet = " + Integer.toHexString(nRet));
                        } else {
                            Log.e(TAG, "GevSCPD = " + GevSCPD);
                        }

                        nRet = cameraManager.setIntValue("GevSCPD", 2000);
                        if (nRet == MV_OK) {
                            Log.e(TAG, "Set GevSCPD success");
                        } else {
                            Log.e(TAG, "Set GevSCPD fail");
                        }
                        nRet = cameraManager.getIntValue("GevSCPD", GevSCPD);
                        if (nRet != MV_OK) {
                            Log.e(TAG, "get GevSCPD fail nRet = " + Integer.toHexString(nRet));
                        } else {
                            Log.e(TAG, "GevSCPD = " + GevSCPD);
                        }
                    }
                    final Integer width = new Integer(0);
                    nRet = cameraManager.getIntValue("Width", width);
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        Log.e(TAG, "get Width fail nRet = " + Integer.toHexString(nRet));
                    }
                    final Integer height = new Integer(0);
                    nRet = cameraManager.getIntValue("Height", height);
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        Log.e(TAG, "get Height fail nRet = " + Integer.toHexString(nRet));
                    }
                    nRet = cameraManager.setEnumValue("PixelFormat", pixelFormatValue[2]);
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        Log.e(TAG, "set PixelFormat fail nRet = " + Integer.toHexString(nRet));
                    }
                    final Integer pixelFormat = new Integer(0);
                    nRet = cameraManager.getEnumValue("PixelFormat", pixelFormat);
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        Log.e(TAG, "get PixelFormat fail nRet = " + Integer.toHexString(nRet));
                    }
                    cameraProcess.initCameraPara(cameraManager, cameraBean);
                    nRet = cameraManager.startDevice();
                    if (nRet != MvCameraControlDefines.MV_OK) {
                        initing = false;
                        isInit = false;
                        initCallBack.initFail("设备启动失败==" + Integer.toHexString(nRet));
                    } else {
                        if (getOneFrameThread == null) {
                            getOneFrameThread = new GetBitMapThread();
                        }
                        getOneFrameThread.flag = true;
                        getOneFrameThread.runFlag = true;
                        getOneFrameThread.start();
                        isInit = true;
                        initing = false;
                        initCallBack.initSuccess();
                        Log.e(TAG, "开启设备成功");
                    }

                }
            } catch (CameraControlException e) {
                isInit = false;
                initing = false;
                initCallBack.initFail("catch error==" + e.errMsg + e.errCode);
                e.printStackTrace();
                Log.e(TAG, e.errMsg + e.errCode);
            }
        }


    }

    boolean takePhoto = false;//是否拍照
    boolean dealingPhoto = false;//正在处理图片
    List<TakePhotoCallBack> takePhotoCallBacks = new ArrayList<>();

    /**
     * 拍照 记得移除监听
     */
    public void takePhoto(TakePhotoCallBack takePhotoCallBack) {
        takePhotoCallBacks.add(takePhotoCallBack);
        if (!dealingPhoto) {
            takePhoto = true;
        }
    }

    /**
     * 移除拍照监听
     * @param takePhotoCallBack
     */
    public void removeTakePhotoCallBack(TakePhotoCallBack takePhotoCallBack){
        takePhotoCallBacks.remove(takePhotoCallBack);
    }


    private GetBitMapThread getOneFrameThread;

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
                Log.e(TAG, "获取图像宽高成功 准备取流");
            } else {
                Log.e(TAG, "获取图像宽高失败");
                return;
            }
            while (runFlag) {
                if (flag) {
                    int nRet = cameraManager.getBitMapTimeout(bytes, info, 1000);
                    if (nRet == 0) {
                        Log.e(TAG, "获取到一帧有效的bitmap");
                        if (videoStreamCallBack != null) {
                            videoStreamCallBack.frameData(bytes);
                        }
                        if (takePhoto) {
                            takePhoto = false;
                            dealingPhoto = true;
                            File file = cameraProcess.dealFrameToPicture(bytes);
                            if (file != null) {
                                for (TakePhotoCallBack takePhotoCallBack : takePhotoCallBacks) {
                                    takePhotoCallBack.takeSuccess(file.getAbsolutePath());
                                }
                                //                                Toast.makeText(mContext, "图片保存成功路径：" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                            } else {
                                //                                Toast.makeText(mContext, "图片保存失败，请重试", Toast.LENGTH_SHORT).show();
                                for (TakePhotoCallBack takePhotoCallBack : takePhotoCallBacks) {
                                    takePhotoCallBack.takeFail("图片保存失败");
                                }
                                dealingPhoto = false;
                            }
                        }

                    } else {
                        if (stopflag) {
                            stopflag = false;
                            Log.e("StopGrabThread", "StopGrabThread");
                             nRet = cameraManager.stopDevice();
                            if (nRet != MvCameraControlDefines.MV_OK) {
                                Log.e(TAG, "stopDevice" + Integer.toHexString(nRet));
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


        /**
         * 资源回收
         */
        public void onDestroy() {
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
    }
}
