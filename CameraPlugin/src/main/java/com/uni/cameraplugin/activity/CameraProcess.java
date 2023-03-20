package com.uni.cameraplugin.activity;

import MvCameraControlWrapper.MvCameraControlDefines;

/**
 * @author 李亚彪
 * @date 2023/3/20
 */
class CameraProcess {
    private CameraActivity activity;

    public CameraProcess(CameraActivity activity) {
        this.activity = activity;
    }

    public String getDeviceInfo(MvCameraControlDefines.MV_CC_DEVICE_INFO entity){
        String str="";
        if (entity.transportLayerType == MvCameraControlDefines.MV_GIGE_DEVICE) {
             str = "[" + String.valueOf(0) + "]"
                    + entity.gigEInfo.manufacturerName + "--"
                    + entity.gigEInfo.serialNumber + "--"
                    + entity.gigEInfo.deviceVersion + "--"
                    + entity.gigEInfo.userDefinedName;
        } else {
             str = "[" + String.valueOf(0) + "]"
                    + entity.usb3VInfo.manufacturerName + "--"
                    + entity.usb3VInfo.serialNumber + "--"
                    + entity.usb3VInfo.deviceVersion + "--"
                    + entity.usb3VInfo.userDefinedName;
        }
        return str;
    }
}
