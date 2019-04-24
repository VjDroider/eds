package com.optimus.eds.ui.order;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.optimus.eds.R;
import com.optimus.eds.db.entities.Package;
import com.optimus.eds.db.entities.Product;
import com.optimus.eds.model.PackageModel;

import java.util.ArrayList;
import java.util.Arrays;
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
        itemHolder.tvAvlStock.setText("0.0");
        itemHolder.etOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
           // if(Double.parseDouble(s.toString())>0)

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(getContext(),
                        String.format("Clicked on position #%s of Section %s",
                                sectionAdapter.getPositionInSection(itemHolder.getAdapterPosition()),
                                title),
                        Toast.LENGTH_SHORT).show();*/
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
        private final TextView tvAvlStock;
        private final  TextView tvWhStock;
        private final EditText etOrder;

        ItemViewHolder(View view) {
            super(view);

            rootView = view;
            tvItemName = view.findViewById(R.id.item_name);
            tvAvlStock = view.findViewById(R.id.avl_stock);
            tvWhStock = view.findViewById(R.id.wh_stock);
            etOrder = view.findViewById(R.id.orderQty);

        }
    }
}