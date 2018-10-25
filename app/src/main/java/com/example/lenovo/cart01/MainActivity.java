package com.example.lenovo.cart01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.cart01.adapter.ProductAdapter;
import com.example.lenovo.cart01.adapter.ShopperAdapter;
import com.example.lenovo.cart01.bean.MessageBean;
import com.example.lenovo.cart01.bean.Product;
import com.example.lenovo.cart01.bean.Shopper;
import com.example.lenovo.cart01.cart.presenter.CartPresenter;
import com.example.lenovo.cart01.cart.view.IView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IView{

    private TextView txtEdit;
    private CheckBox cbTotal;
    private TextView txtPrice;
    private Button btnCalu;
    private RecyclerView rvShopper;
    private CartPresenter presenter;

    private List<Shopper<List<Product>>> list;
    private ShopperAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        setlistener();


    }

    private void setlistener() {

        cbTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isChecked = cbTotal.isChecked();

              //遍历一级列表,和下方全选状态一致

                for (Shopper<List<Product>> listShopper : list) {
                    listShopper.setChecked(isChecked);
                    //遍历二级列表，和下方的全选状态一致
                    List<Product> products = listShopper.getList();
                    for (Product product : products) {
                        product.setChecked(isChecked);
                    }

                }
                calculatePrice();
                adapter.notifyDataSetChanged();


            }
        });
    }

    private void initData() {



        presenter = new CartPresenter();
        presenter.attach(this);
        presenter.getData();

    //Shopper适配器
        list = new ArrayList<>();
        adapter = new ShopperAdapter(this,list);


        // 添加一级条目（商家）状态发生变化时
        adapter.setOnShopperClickListener(new ShopperAdapter.OnShopperClickListener() {
            @Override
            public void onShopperClick(int position, boolean isCheck) {

                if (!isCheck){
                    cbTotal.setChecked(false);
                }else {
                    boolean isAllShopperCkecked = true;
                    for (Shopper<List<Product>> listShopper : list) {
                        if (!listShopper.isChecked()){
                            isAllShopperCkecked = false;
                            break;
                        }
                    }
                    cbTotal.setChecked(isAllShopperCkecked);
                }
                // 一级条目发生变化时，计算一下总价
                   calculatePrice();
            }
        });

        adapter.setOnAddDecreaseProductListener(new ProductAdapter.OnAddDecreaseProductListener() {
            @Override
            public void onChange(int position, int num) {
                calculatePrice();
            }
        });



        //线性布局管理器
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvShopper.setLayoutManager(layoutManager);
        rvShopper.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvShopper.setAdapter(adapter);




    }

    //计算商品总价
    private void calculatePrice(){
        //遍历商家
        float totalPrice = 0;
        for (Shopper<List<Product>> listShopper : list) {
            //遍历商家的商品
            List<Product> list = listShopper.getList();
            for (Product product : list) {
                //如果被选中
                if (product.isChecked()){
                    totalPrice += product.getNum()*product.getPrice();
                }
            }
        }

        txtPrice.setText("总价" +totalPrice);

    }



    //找控件
    private void initView() {
        txtEdit = findViewById(R.id.txt_edit);
        cbTotal = findViewById(R.id.cb_total_select);
        txtPrice = findViewById(R.id.txt_price);
        btnCalu = findViewById(R.id.btn_calu);
        rvShopper = findViewById(R.id.rv_shopper);
    }


    //成功
    @Override
    public void success(MessageBean<List<Shopper<List<Product>>>> data) {

        if (data!=null){
            //获取商家列表
            List<Shopper<List<Product>>> shoppers = data.getData();
            if (shoppers != null){
                list.clear();
                list.addAll(shoppers);
                adapter.notifyDataSetChanged();
            }

        }


    }

    //失败
    @Override
    public void failed(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detach();
        }
    }
}
