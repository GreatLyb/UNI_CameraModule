package com.uni.cameraplugin.imp;

/**
 * @author 李亚彪
 * @date 2023/4/9
 */
public interface TakePhotoCallBack {

    void takeSuccess(String path);

    void takeFail(String errorMsg);


}
