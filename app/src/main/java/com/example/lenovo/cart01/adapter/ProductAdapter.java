package com.example.lenovo.cart01.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.cart01.R;
import com.example.lenovo.cart01.bean.Product;
import com.example.lenovo.cart01.utils.StringUtils;
import com.example.lenovo.cart01.widget.AddDecreaseView;

import java.util.List;

/**
 * Created by lenovo on 2018/10/23.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Holder>{

    private Context context;
    private List<Product> list;

    public ProductAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }


    //二级条目（商品）点击监听
    public interface OnProductClickListener{
        void onProductClick(int position,boolean isChecked);
    }

    private OnProductClickListener productClickListener;

    public void setOnProductClickListener(OnProductClickListener listener){
        this.productClickListener = listener;
    }


    // 加减器发生变化的监听
    public interface OnAddDecreaseProductListener{
        void onChange(int position,int num);
    }

    private OnAddDecreaseProductListener productListener;

     public void setOnAddDecreaseProductListener(OnAddDecreaseProductListener listener){
         this.productListener = listener;
     }



    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = View.inflate(context,R.layout.item_product,null);

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        final Product product = list.get(position);
        String images = product.getImages();
        //商品图片
         if (!TextUtils.isEmpty(images)){
             String[] strings = images.split("\\|");
             if (strings.length>0){
                 Glide.with(context).load(StringUtils.https2Http(strings[0]))
                         .into(holder.imgProduct);
             }
         }

         holder.txtProductName.setText(product.getTitle());
        holder.txtSinglePriice.setText(String.valueOf(product.getPrice()));

         holder.advProduct.setNum(product.getNum());

         //加减器添加点击事件
             holder.advProduct.setOnAddDecreaseClickListener(new AddDecreaseView.OnAddDecreaseClickListener() {
                 @Override
                 public void add(int num) {
                     product.setNum(num);

                     if (productListener != null){
                         productListener.onChange(position,num);
                     }

                 }

                 @Override
                 public void decrease(int num) {
                    product.setNum(num);
                    if (productListener != null){
                        productListener.onChange(position,num);
                    }

                 }
             });

             //商品的复选框
        holder.cbProduct.setOnCheckedChangeListener(null);
        holder.cbProduct.setChecked(product.isChecked());
        holder.cbProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                product.setChecked(isChecked);
                if (productClickListener != null){
                    productClickListener.onProductClick(position,isChecked);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        private CheckBox cbProduct;
        private ImageView imgProduct;
        private TextView txtProductName;
        private TextView txtSinglePriice;
        private AddDecreaseView advProduct;
        public Holder(View itemView) {
            super(itemView);

            cbProduct = itemView.findViewById(R.id.cb_product);
            imgProduct = itemView.findViewById(R.id.img_product);
            txtSinglePriice = itemView.findViewById(R.id.txt_single_price);
            advProduct = itemView.findViewById(R.id.adv_product);
            txtProductName = itemView.findViewById(R.id.txt_product_name);
        }
    }
}
