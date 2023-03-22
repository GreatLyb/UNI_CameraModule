package com.uni.cameraplugin.bean;

import java.util.List;

/**
 * @author 李亚彪
 * @date 2023/3/22
 */
public class CameraBean {
    private int debug=1;
    private List<CameraParameterBean> parameterBeans;

    public int getDebug() {
        return debug;
    }

    public void setDebug(int debug) {
        this.debug = debug;
    }

    public List<CameraParameterBean> getParameterBeans() {
        return parameterBeans;
    }

    public void setParameterBeans(List<CameraParameterBean> parameterBeans) {
        this.parameterBeans = parameterBeans;
    }
}
