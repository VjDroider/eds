package com.optimus.eds.source;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.optimus.eds.Constant;
import com.optimus.eds.db.entities.CustomerInput;
import com.optimus.eds.db.entities.Order;
import com.optimus.eds.db.entities.OrderDetail;
import com.optimus.eds.model.BaseResponse;
import com.optimus.eds.model.MasterModel;
import com.optimus.eds.model.OrderDetailAndPriceBreakdown;
import com.optimus.eds.model.OrderModel;
import com.optimus.eds.model.OrderResponseModel;
import com.optimus.eds.ui.customer_input.CustomerInputRepository;
import com.optimus.eds.ui.order.OrderBookingRepository;
import com.optimus.eds.ui.order.OrderManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static com.optimus.eds.Constant.EXTRA_PARAM_OUTLET_ID;
import static com.optimus.eds.Constant.EXTRA_PARAM_OUTLET_REASON_N_ORDER;
import static com.optimus.eds.Constant.EXTRA_PARAM_OUTLET_STATUS_ID;
import static com.optimus.eds.Constant.EXTRA_PARAM_OUTLET_VISIT_TIME;
import static com.optimus.eds.Constant.EXTRA_PARAM_PRESELLER_LAT;
import static com.optimus.eds.Constant.EXTRA_PARAM_PRESELLER_LNG;
import static com.optimus.eds.Constant.TOKEN;

public class UploadOrdersService extends JobService {

    private final String iTAG = UploadOrdersService.class.getSimpleName();
    String token;
    private int jobId;

    private OrderBookingRepository orderRepository;
    private CustomerInputRepository customerInputRepository;
    MasterModel masterModel;
    @Override
    public void onCreate() {
        super.onCreate();

        customerInputRepository = new CustomerInputRepository(getApplication());
        orderRepository = OrderBookingRepository.singleInstance(getApplication());
        masterModel = new MasterModel();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        if (params != null) {
            PersistableBundle bundle = params.getExtras();
            jobId = params.getJobId();
            final Long outletId = bundle.getLong(EXTRA_PARAM_OUTLET_ID);
            final Integer statusId = bundle.getInt(EXTRA_PARAM_OUTLET_STATUS_ID);
            final String reason = bundle.getString(EXTRA_PARAM_OUTLET_REASON_N_ORDER,"");
            final Double latitude = bundle.getDouble(EXTRA_PARAM_PRESELLER_LAT,0);
            final Double longitude = bundle.getDouble(EXTRA_PARAM_PRESELLER_LNG,0);
            final Long visitTime = bundle.getLong(EXTRA_PARAM_OUTLET_VISIT_TIME);
            token = bundle.getString(TOKEN);

            masterModel.setOutletId(outletId);
            masterModel.setOutletStatus(statusId);
            masterModel.setReason(reason);
            masterModel.setOutletVisitTime(visitTime>0?visitTime:null);
            masterModel.setLocation(latitude,longitude);
            findOrder(outletId);
           // uploadMasterData(masterModel);
        }
        return true;
    }

    public void findOrder(Long outletId){

        Maybe<OrderModel> orderSingle = orderRepository.findOrder(outletId)
                .map(orderModel -> {
                    List<OrderDetail> orderDetails = new ArrayList<>();
                    for(OrderDetailAndPriceBreakdown orderDetail:orderModel.getOrderDetailAndCPriceBreakdowns()){
                        orderDetail.getOrderDetail().setCartonPriceBreakDown(orderDetail.getCartonPriceBreakDownList());
                        orderDetail.getOrderDetail().setUnitPriceBreakDown(orderDetail.getUnitPriceBreakDownList());
                        orderDetails.add(orderDetail.getOrderDetail());
                    }
                    orderModel.setOrderDetails(orderDetails);

                    return orderModel;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());

        Maybe<CustomerInput> customerInputSingle = customerInputRepository.getCustomerInput(outletId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());

        Maybe<MasterModel> masterModelSingle = Maybe.zip(orderSingle,customerInputSingle,
                (orderModel, customerInput) -> {

            Order order = orderModel.getOrder();
            Gson gson  = new Gson();
            String json = gson.toJson(order);
            OrderResponseModel responseModel = gson.fromJson(json,OrderResponseModel.class);
            responseModel.setOrderDetails(orderModel.getOrderDetails());


            masterModel.setCustomerInput(customerInput);
            masterModel.setOrderModel(responseModel);
           /* model.setLocation(orderModel.getOutlet().getVisitTimeLat(),orderModel.getOutlet().getVisitTimeLng());
            model.setOutletId(order.getOutletId());
            model.setOutletVisitTime(orderModel.getOutlet().getVisitDateTime());*/
            return masterModel;
        }) ;

        masterModelSingle.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(this::uploadMasterData,this::error);




    }

    private void uploadMasterData(MasterModel masterModel) {
        Log.i(iTAG,"JobId: "+jobId);

        RetrofitHelper.getInstance().getApi().saveOrder(masterModel,token)
                .observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe(this::onUpload,this::error);
    }

    private void onUpload(MasterModel orderResponseModel) {
        if(orderResponseModel!=null) {
            orderResponseModel.setCustomerInput(null);
            orderResponseModel.getOrderModel().setOrderDetails(null);
        }


        if(orderResponseModel !=null && orderResponseModel.getOrderModel()!=null)
        OrderBookingRepository.singleInstance(getApplication())
                .findOrderById(orderResponseModel.getOrderModel().getMobileOrderId()).map(order -> {
                    order.setOrderStatus(orderResponseModel.getOrderModel().getOrderStatusId());
                    return order;
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(order -> {
            OrderBookingRepository.singleInstance(getApplication()).updateOrder(order);
        });
        Intent intent = new Intent();
        intent.setAction(Constant.ACTION_ORDER_UPLOAD);
        intent.putExtra("Response", orderResponseModel);
        LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(intent);

        this.stopSelf();

    }

    private void error(Throwable throwable) throws IOException {

        throwable.printStackTrace();
        String errorBody = throwable.getMessage();
        if (throwable instanceof HttpException){
            HttpException error = (HttpException)throwable;
            errorBody = error.response().errorBody().string();
        }
        MasterModel baseResponse = new MasterModel();
        baseResponse.setResponseMsg(errorBody);
        baseResponse.setSuccess(false);
        Intent intent = new Intent();
        intent.setAction(Constant.ACTION_ORDER_UPLOAD);
        intent.putExtra("Response", baseResponse);
        LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(intent);
        this.stopSelf();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
