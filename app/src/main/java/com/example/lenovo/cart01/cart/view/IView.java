package com.example.lenovo.cart01.cart.view;

import com.example.lenovo.cart01.bean.MessageBean;
import com.example.lenovo.cart01.bean.Product;
import com.example.lenovo.cart01.bean.Shopper;

import java.util.List;

/**
 * Created by lenovo on 2018/10/23.
 */

public interface IView {

    void success(MessageBean<List<Shopper<List<Product>>>> data);

    void failed(Exception e);

}
