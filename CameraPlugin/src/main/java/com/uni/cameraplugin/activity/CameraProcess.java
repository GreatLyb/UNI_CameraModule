package com.uni.cameraplugin.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    /**
     * 获取设备信息
     *
     * @param entity
     * @return
     */
    public String getDeviceInfo(MvCameraControlDefines.MV_CC_DEVICE_INFO entity) {
        String str = "";
        if (entity.transportLayerType == MvCameraControlDefines.MV_GIGE_DEVICE) {
            str = "[" + String.valueOf(0) + "]" + entity.gigEInfo.manufacturerName + "--" + entity.gigEInfo.serialNumber + "--" + entity.gigEInfo.deviceVersion + "--" + entity.gigEInfo.userDefinedName;
        } else {
            str = "[" + String.valueOf(0) + "]" + entity.usb3VInfo.manufacturerName + "--" + entity.usb3VInfo.serialNumber + "--" + entity.usb3VInfo.deviceVersion + "--" + entity.usb3VInfo.userDefinedName;
        }
        return str;
    }

    /**
     * 将数据帧转成jpeg
     *
     * @param bytes
     * @return
     */
    public File dealFrameToPicture(byte[] bytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        //Bitmap转换成byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datas = baos.toByteArray();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String format = formatter.format(date);
        //存储路径
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + format + ".jpeg";
        File file = bytesToImageFile(datas, path);
        bitmap.recycle();
        return file;
    }

    /**
     * bute 转file
     *
     * @param bytes
     * @param path
     * @return
     */
    private File bytesToImageFile(byte[] bytes, String path) {
        File file = new File(path);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes, 0, bytes.length);
            fos.flush();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        file.delete();
        return null;
    }
}
