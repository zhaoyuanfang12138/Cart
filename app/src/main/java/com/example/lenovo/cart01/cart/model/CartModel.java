package com.example.lenovo.cart01.cart.model;

import com.example.lenovo.cart01.inter.ICallBack;
import com.example.lenovo.cart01.utils.HttpUtils;

import java.lang.reflect.Type;

/**
 * Created by lenovo on 2018/10/23.
 */

public class CartModel {

    public void getData(String url, ICallBack callBack, Type type){
        HttpUtils.getInstance().get(url,callBack,type);
    }

}
