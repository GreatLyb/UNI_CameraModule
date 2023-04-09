package com.uni.cameraplugin.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.uni.cameraplugin.bean.CameraBean;
import com.uni.cameraplugin.bean.CameraParameterBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import MvCameraControlWrapper.MvCameraControlDefines;

/**
 * @author 李亚彪
 * @date 2023/3/20
 */
class CameraProcess {

    private static final String TAG="CameraProcess";

    private Context activity;

    public CameraProcess(Context activity) {
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
            str = "[" + 0 + "]" + entity.gigEInfo.manufacturerName + "--" + entity.gigEInfo.serialNumber + "--" + entity.gigEInfo.deviceVersion + "--" + entity.gigEInfo.userDefinedName;
        } else {
            str = "[" + 0 + "]" + entity.usb3VInfo.manufacturerName + "--" + entity.usb3VInfo.serialNumber + "--" + entity.usb3VInfo.deviceVersion + "--" + entity.usb3VInfo.userDefinedName;
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
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

    public CameraBean getCameraBean(String caremaPara) {
        String body = caremaPara;
        if (body.isEmpty()) {
            body = obtainJson();
        }
        return JSON.parseObject(body, CameraBean.class);
    }

    private String obtainJson() {
        CameraBean cameraBean = new CameraBean();
        cameraBean.setDebug(1);
        List<CameraParameterBean> beans = new ArrayList<>();
        CameraParameterBean bean1 = new CameraParameterBean();
        bean1.setType("IFloat");
        bean1.setKey("ExposureTime");//曝光时间
        bean1.setValue("1400000");
        beans.add(bean1);
        cameraBean.setParameterBeans(beans);
        return JSON.toJSONString(cameraBean);
    }

    /**
     * 初始化相机参数
     *
     * @param cameraManager
     * @param cameraBean
     */
    public void initCameraPara(CameraManager cameraManager, CameraBean cameraBean) {
        if (cameraBean.getParameterBeans() != null && cameraBean.getParameterBeans().size() > 0) {
            int state = -1;
            for (CameraParameterBean parameterBean : cameraBean.getParameterBeans()) {
                switch (parameterBean.getType()) {
                    case "IEnumeration":
                        state = cameraManager.setEnumValue(parameterBean.getKey(), Integer.parseInt(parameterBean.getValue()));
                       Log.e(TAG,"设置" + parameterBean.getKey() + "=" + parameterBean.getValue() + "---结果=" + state);
                        break;
                    case "IString":
                        state = cameraManager.setStrValu(parameterBean.getKey(), parameterBean.getValue());
                        Log.e(TAG,"设置" + parameterBean.getKey() + "=" + parameterBean.getValue() + "---结果=" + state);
                        break;
                    case "IInteger":
                        long i = Long.parseLong(parameterBean.getValue());
                        state = cameraManager.setIntValue(parameterBean.getKey(), i);
                         Log.e(TAG, "设置" + parameterBean.getKey() + "=" + i + "---结果=" + state);
                        break;
                    case "IBoolean":
                        boolean b = false;
                        boolean legitimate = false;
                        if (parameterBean.getValue().equals("true")) {
                            b = true;
                            legitimate = true;
                        } else if (parameterBean.getValue().equals("false")) {
                            b = false;
                            legitimate = true;
                        }
                        if (legitimate) {
                            state = cameraManager.setBoolValue(parameterBean.getKey(), b);
                             Log.e(TAG, "设置" + parameterBean.getKey() + "=" + b + "---结果=" + state);
                        } else {
                             Log.e(TAG, "设置" + parameterBean.getKey() + "=参数不合法只能是true或者false");
                        }
                        break;
                    case "IFloat":
                        float v = Float.parseFloat(parameterBean.getValue());
                        state = cameraManager.setFloatValue(parameterBean.getKey(), v);
                         Log.e(TAG, "设置" + parameterBean.getKey() + "=" + v + "---结果=" + state);
                        break;
                    case "EnumValueByString":
                        state = cameraManager.setEnumValueByString(parameterBean.getKey(), parameterBean.getValue());
                         Log.e(TAG, "设置" + parameterBean.getKey() + "=" + parameterBean.getValue() + "---结果=" + state);
                        break;
                    default:
                        break;
                }
            }
        } else {
            String s = "相机参数类型为空  不设置任何参数";
             Log.e(TAG, s);
            Log.e("Lyb", s);
        }

    }
}
