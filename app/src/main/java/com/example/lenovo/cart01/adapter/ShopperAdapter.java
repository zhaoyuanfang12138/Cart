package com.example.lenovo.cart01.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.lenovo.cart01.R;
import com.example.lenovo.cart01.bean.Product;
import com.example.lenovo.cart01.bean.Shopper;

import java.util.List;

/**
 * Created by lenovo on 2018/10/23.
 */

public class ShopperAdapter extends RecyclerView.Adapter<ShopperAdapter.Holder>{

    private Context context;
    private List<Shopper<List<Product>>> list;
    private ProductAdapter adapter;

    public ShopperAdapter(Context context, List<Shopper<List<Product>>> list) {
        this.context = context;
        this.list = list;
    }

    //一级列表(商家) 发生变化的接口
    public interface OnShopperClickListener{
        void onShopperClick(int position, boolean isCheck);
    }

    private OnShopperClickListener shopperClickListener;

    public void setOnShopperClickListener(OnShopperClickListener listener){
        this.shopperClickListener = listener;
    }


    // 二级列表的加减器监听
    private ProductAdapter.OnAddDecreaseProductListener productListener;

    public void setOnAddDecreaseProductListener(ProductAdapter.OnAddDecreaseProductListener listener){
           this.productListener = listener;
    }



    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = View.inflate(context, R.layout.item_shopper,null);

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        final Shopper<List<Product>> shopper = list.get(position);
        holder.txtShopperName.setText(shopper.getSellerName());

        //产品的列表
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(context);
        holder.rvProduct.setLayoutManager(pLayoutManager);
        adapter = new ProductAdapter(context,shopper.getList());


        // 给二级列表添加一个加减器的监听
         if (productListener != null){
             adapter.setOnAddDecreaseProductListener(productListener);
         }

        // 二级条目（商品）复选框点击事件
        adapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(int position, boolean isChecked) {
                   if (!isChecked){
                       shopper.setChecked(false);
                       shopperClickListener.onShopperClick(position,false);
                   }else {
                       boolean isAllProductSelected = true;
                       for (Product product : shopper.getList()) {
                           if (!product.isChecked()){
                               isAllProductSelected = false;
                               break;
                           }
                       }

                       shopper.setChecked(isAllProductSelected);
                       shopperClickListener.onShopperClick(position,true);

                   }

                   notifyDataSetChanged();
                  productListener.onChange(0,0);
            }
        });

        holder.rvProduct.setAdapter(adapter);

        // 先取消掉之前的点击变化监听
        holder.cbSHopper.setOnCheckedChangeListener(null);

        // 设置好初始化的状态
        holder.cbSHopper.setChecked(shopper.isChecked());

        holder.cbSHopper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // 1.商家被选中的时候，子类所有的商品应该被选中
                List<Product> productList = shopper.getList();

                for (Product product : productList) {
                    product.setChecked(isChecked);
                }
                // 子类商品的适配器刷新
                adapter.notifyDataSetChanged();

                // 当点击一级条目的时候，外部的全选按钮状态发生变化
                if (shopperClickListener != null){
                    shopperClickListener.onShopperClick(position,isChecked);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        private CheckBox cbSHopper;
        private TextView txtShopperName;
        private RecyclerView rvProduct;

        public Holder(View itemView) {
            super(itemView);

            cbSHopper =  itemView.findViewById(R.id.cb_shopper);
            txtShopperName = itemView.findViewById(R.id.txt_shopper_name);
            rvProduct = itemView.findViewById(R.id.rv_product);
        }
    }

}
