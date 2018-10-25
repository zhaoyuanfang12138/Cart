package com.example.lenovo.cart01.inter;

/**
 * Created by lenovo on 2018/10/22.
 */

public interface ICallBack {

    //创建成功与失败的接口
    void onsuccess(Object obj);

    void onfailed(Exception e);
}
