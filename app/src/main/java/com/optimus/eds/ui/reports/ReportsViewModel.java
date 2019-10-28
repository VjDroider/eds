package com.optimus.eds.ui.reports;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.optimus.eds.db.entities.OrderDetail;
import com.optimus.eds.model.OrderDetailAndPriceBreakdown;
import com.optimus.eds.model.OrderModel;
import com.optimus.eds.model.ReportModel;

import com.optimus.eds.ui.order.OrderManager;
import com.optimus.eds.ui.route.outlet.OutletListRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class ReportsViewModel extends AndroidViewModel {


    private OutletListRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<ReportModel> summaryMutable;
    private ReportModel reportModel;

    private List<OrderDetail> orderDetailList; // this list is sku based
    private Double total=0.0;
    private Long carton=0l;
    private Long unit = 0l;
    private int totalOrder = 0;
    private int pjpCount = 0;
    private int completedCount = 0;
    private int productiveCount = 0;
    public ReportsViewModel(@NonNull Application application) {
        super(application);
        repository = OutletListRepository.getInstance(application);
        orderDetailList = new ArrayList<>();
        reportModel = new ReportModel();
        summaryMutable = new MutableLiveData<>();
    }

    public void getPjpCount(){

        AsyncTask.execute(() -> {
            pjpCount = repository.getPjpCount();
            completedCount = repository.getCompletedCount();
            productiveCount = repository.getProductiveCount();
            reportModel.setCounts(pjpCount,completedCount,productiveCount);
            summaryMutable.postValue(reportModel);

        });

    }

    public void getReport(){
        getPjpCount();
        Observable<List<OrderModel>> orderListObservable = repository.getOrders().toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
        disposable.add(orderListObservable
                .flatMap((Function<List<OrderModel>, ObservableSource<OrderModel>>) orderModelList -> {
                    totalOrder = orderModelList.size();
                    return Observable.fromIterable(orderModelList);
                })
                .subscribeWith(new DisposableObserver<OrderModel>() {
                    @Override
                    public void onNext(OrderModel orderDetail) {

                        Double price = orderDetail.getOrder().getPayable();
                        total+=price;
                        for(OrderDetailAndPriceBreakdown detailAndPriceBreakdown:orderDetail.getOrderDetailAndCPriceBreakdowns())
                        {
                            OrderDetail orderItem = detailAndPriceBreakdown.getOrderDetail();
                            Integer cQty = orderItem.getCartonQuantity();
                            Integer uQty = orderItem.getUnitQuantity();
                            cQty= cQty!=null?cQty:0;
                            uQty = uQty!=null?uQty:0;

                            if(orderDetailList.contains(orderItem)){
                                int pos = orderDetailList.indexOf(orderItem);
                                OrderDetail savedItem = orderDetailList.get(pos);
                                int cartons = savedItem.getCartonQuantity()+cQty;
                                int units = savedItem.getUnitQuantity()+uQty;

                              OrderManager.OrderQuantity quantity =   OrderManager.instance().calculateOrderQty(orderItem.getCartonSize(),units,cartons);
                                orderDetailList.get(pos).setCartonQuantity(quantity.getCarton());
                                orderDetailList.get(pos).setUnitQuantity(quantity.getUnits());
                            }else{
                                orderDetailList.add(orderItem);
                            }

                           // carton+= cQty;
                           // unit+=uQty;
                         /*   Long sku= detailAndPriceBreakdown.getOrderDetail().getProductId();
                            if(!skuList.contains(sku)){
                                skuList.add(sku);
                            }*/


                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ReportsViewModel",e.getMessage());
                        summaryMutable.postValue(setSummary(total,carton,unit,totalOrder,orderDetailList.size()));
                    }

                    @Override
                    public void onComplete() {
                        for (OrderDetail item: orderDetailList) {
                            carton+=item.getCartonQuantity()!=null?item.getCartonQuantity():0;
                            unit+=item.getUnitQuantity()!=null?item.getUnitQuantity():0;
                        }
                        summaryMutable.postValue(setSummary(total,carton,unit,totalOrder,orderDetailList.size()));
                    }
                }));


    }

    private ReportModel setSummary(Double total,Long carton,Long unit,int totalOrder,int skuSize){

        reportModel.setTotalSale(total);
        reportModel.setCarton(carton);
        reportModel.setUnit(unit);
        reportModel.setSkuSize(skuSize);
        reportModel.setTotalOrders(totalOrder);
        return reportModel;
    }



    public LiveData<ReportModel> getSummary(){
        return summaryMutable;
    }
}
