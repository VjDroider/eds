package com.optimus.eds.ui.cash_memo;

import android.content.Context;
import android.support.design.card.MaterialCardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.optimus.eds.R;
import com.optimus.eds.db.entities.OrderDetail;
import com.optimus.eds.model.OrderPriceBreakDownModel;
import com.optimus.eds.model.PriceBreakDownModel;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashMemoItemView extends MaterialCardView {

    @BindView(R.id.tvProductName)
    TextView productName;
    @BindView(R.id.tvProductQty)
    TextView productQty;
    /*@BindView(R.id.tvProductRate)
    TextView productRate;*/
  /*  @BindView(R.id.tvProductDiscount)
    TextView productDiscount;*/

    @BindView(R.id.tvProductTotal)
    TextView total;

    @BindView(R.id.rate_container)
    FrameLayout rateContainer;

    private OrderDetail order;

    public CashMemoItemView(Context context) {
        super(context);
    }

    public CashMemoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CashMemoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);

    }

    public void setCartItem(OrderDetail item) {

        DecimalFormat df = new DecimalFormat("#.##");
        this.order = item;
        if (item != null) {

            productName.setText(item.getProductName());
            productQty.setText(order.getQuantity());
            total.setText(df.format((order.getCartonTotalPrice()+order.getUnitTotalPrice())));

            rateContainer.addView(addPricingView(item));
        }
    }


    private View addPricingView(OrderDetail item){
        LayoutInflater inflater =  LayoutInflater.from(getContext());
        CashMemoRateView rateView = (CashMemoRateView) inflater.inflate(R.layout.memo_rate_child_view,null);

        rateView.setRates(item);
        return rateView;
    }


}
