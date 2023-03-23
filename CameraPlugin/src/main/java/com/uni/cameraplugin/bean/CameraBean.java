package com.uni.cameraplugin.bean;

import java.util.List;

/**
 * @author 李亚彪
 * @date 2023/3/22
 */
public class CameraBean {
    private int debug=1;
    private String ip="";
    private String subNetMask="";
    private String defaultGateWay="";
    private List<CameraParameterBean> parameterBeans;

    public int getDebug() {
        return debug;
    }

    public void setDebug(int debug) {
        this.debug = debug;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSubNetMask() {
        return subNetMask;
    }

    public void setSubNetMask(String subNetMask) {
        this.subNetMask = subNetMask;
    }

    public String getDefaultGateWay() {
        return defaultGateWay;
    }

    public void setDefaultGateWay(String defaultGateWay) {
        this.defaultGateWay = defaultGateWay;
    }

    public List<CameraParameterBean> getParameterBeans() {
        return parameterBeans;
    }

    public void setParameterBeans(List<CameraParameterBean> parameterBeans) {
        this.parameterBeans = parameterBeans;
    }
}
