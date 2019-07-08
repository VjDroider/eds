package com.optimus.eds.ui.order;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.optimus.eds.R;
import com.optimus.eds.db.entities.Product;
import com.optimus.eds.model.PackageModel;
import com.optimus.eds.utils.Util;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class PackageSection extends StatelessSection {


    String title;
    List<Product> list;


    PackageSection(PackageModel pkg) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.order_booking_item_view)
                .headerResourceId(R.layout.section_header)
                .build());
        this.title = pkg.getPackageName();
        this.list = pkg.getProducts();



    }

    public List<Product> getList() {
        return list;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        Product product = list.get(position);

        itemHolder.tvItemName.setText(product.getName());

        itemHolder.whStock.setText(String.valueOf(Util.convertToNullableDecimalQuantity(product.getCartonStockInHand(),product.getUnitStockInHand())));

        itemHolder.etAvlStock.setText(String.valueOf(Util.convertToNullableDecimalQuantity(product.getAvlStockCarton(),product.getAvlStockUnit())));

        itemHolder.etOrderQty.setText(String.valueOf(Util.convertToNullableDecimalQuantity(product.getQtyCarton(),product.getQtyUnit())));

        itemHolder.etAvlStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<1 || (s.length()==1 && s.toString().equals("."))) {
                    product.setAvlStock(null,null);
                    return;
                }
                double qty = Double.parseDouble(s.toString());
                if(qty>0){
                    Integer[] cu = Util.convertToLongQuantity(s.toString());
                    product.setAvlStock(cu[0],cu[1]);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        itemHolder.etOrderQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<1 || (s.length()==1 && s.toString().equals("."))) {
                    product.setQty(null,null);
                    return;
                }
                double qty = Double.parseDouble(s.toString());
                if(qty>0){
                    Integer[] cu = Util.convertToLongQuantity(s.toString());
                    product.setQty(cu[0],cu[1]);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(title);
    }




    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
        }
    }



    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;

        private final TextView tvItemName;
        private final TextView whStock;
        private final  EditText etAvlStock;
        private final EditText etOrderQty;

        ItemViewHolder(View view) {
            super(view);

            rootView = view;
            tvItemName = view.findViewById(R.id.item_name);
            whStock = view.findViewById(R.id.wh_stock);
            etAvlStock = view.findViewById(R.id.avl_stock);
            etOrderQty = view.findViewById(R.id.order_unit);

        }
    }


}