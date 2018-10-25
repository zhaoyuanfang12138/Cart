package com.example.lenovo.cart01.cart.presenter;

import com.example.lenovo.cart01.bean.MessageBean;
import com.example.lenovo.cart01.bean.Product;
import com.example.lenovo.cart01.bean.Shopper;
import com.example.lenovo.cart01.cart.model.CartModel;
import com.example.lenovo.cart01.cart.view.IView;
import com.example.lenovo.cart01.inter.ICallBack;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by lenovo on 2018/10/23.
 */

public class CartPresenter {

    private IView iv;
    private CartModel model;


    public void attach(IView iv){
        this.iv=iv;
        model = new CartModel();
    }


    public void getData(){
        String url = "http://www.zhaoapi.cn/product/getCarts?uid=1538";
        Type type = new TypeToken<MessageBean<List<Shopper<List<Product>>>>>(){}.getType();

        model.getData(url, new ICallBack() {
            @Override
            public void onsuccess(Object obj) {

                MessageBean<List<Shopper<List<Product>>>> data = (MessageBean<List<Shopper<List<Product>>>>) obj;
                iv.success(data);

            }

            @Override
            public void onfailed(Exception e) {
              iv.failed(e);
            }
        },type);

    }



 public void detach(){
        if (iv!=null){
            iv=null;
        }
 }

}
