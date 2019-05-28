package com.optimus.eds.ui.cash_memo;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.optimus.eds.BaseActivity;
import com.optimus.eds.R;
import com.optimus.eds.db.entities.Order;
import com.optimus.eds.db.entities.OrderDetail;
import com.optimus.eds.db.entities.Outlet;
import com.optimus.eds.model.OrderModel;
import com.optimus.eds.ui.customer_input.CustomerInputActivity;
import com.optimus.eds.ui.order.OrderManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CashMemoActivity extends BaseActivity {


    private Long outletId;


    @BindView(R.id.rvCartItems)
    RecyclerView rvCartItems;

    @BindView(R.id.tvOutletName)
    TextView tvOutletName;

    @BindView(R.id.tvGrandTotal)
    TextView tvGrandTotal;

    @BindView(R.id.tvDiscountedAmount)
    TextView tvDiscountedAmount;

    @BindView(R.id.tvQty)
    TextView tvQty;
    private CashMemoAdapter cartAdapter;
    private CashMemoViewModel viewModel;
    public static void start(Context context, Long outletId) {
        Intent starter = new Intent(context, CashMemoActivity.class);
        starter.putExtra("OutletId",outletId);
        context.startActivity(starter);
    }


    @Override
    public int getID() {
        return R.layout.activity_cash_memo;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        outletId =  getIntent().getLongExtra("OutletId",0);
        ButterKnife.bind(this);
        setToolbar(getString(R.string.cash_memo));
        viewModel = ViewModelProviders.of(this).get(CashMemoViewModel.class);
        initAdapter();
        setObserver();

    }

    private void setObserver(){
        viewModel.loadOutlet(outletId).observe(this, this::onOutletLoaded);
       // viewModel.getOrder(outletId);
        viewModel.getOrder(outletId).observe(this, order -> {
            updateCart(order.getOrderDetails());
            updatePricesOnUi(order);

        });
    }

    private void onOutletLoaded(Outlet outlet) {
        tvOutletName.setText(outlet.getOutletName().concat(" - "+ outlet.getLocation()));
    }

    private void updatePricesOnUi(OrderModel order){
        tvGrandTotal.setText(String.valueOf(order.getOrder().getPayable()));

    }

    private void initAdapter(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCartItems.setLayoutManager(layoutManager);
        rvCartItems.setHasFixedSize(true);
        cartAdapter = new CashMemoAdapter(this);
        rvCartItems.setAdapter(cartAdapter);
        rvCartItems.setNestedScrollingEnabled(false);
    }


    private void updateCart(List<OrderDetail> products) {
        cartAdapter.populateCartItems(products);
    }

    @OnClick(R.id.btnNext)
    public void navigateToCustomerInput(){
        CustomerInputActivity.start(this);
    }
}
