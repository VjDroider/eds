package com.optimus.eds.ui.route.outlet.detail;

import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.location.Location;
import android.os.PersistableBundle;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.optimus.eds.Constant;
import com.optimus.eds.db.entities.Outlet;
import com.optimus.eds.source.JobIdManager;
import com.optimus.eds.source.MasterDataUploadService;

import java.util.Calendar;
import java.util.Objects;


public class OutletDetailViewModel extends AndroidViewModel {

    private final OutletDetailRepository repository;

    private final MutableLiveData<Integer> statusLiveData;

    public LiveData<Location> getOutletNearbyPos() {
        return outletNearbyPos;
    }

    private final MutableLiveData<Location> outletNearbyPos;
    private final MutableLiveData<Boolean> uploadStatus;


    private int outletStatus=1;
    private Outlet outlet;


    public OutletDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new OutletDetailRepository(application);
        statusLiveData = new MutableLiveData<>();
        uploadStatus = new MutableLiveData<>();
        outletNearbyPos = new MutableLiveData<>();

    }


    public LiveData<Outlet> findOutlet(Long outletId){
        return repository.getOutletById(outletId);
    }

    public void setOutlet(Outlet outlet){
        this.outlet = outlet;
    }


    // schedule
    public void scheduleMasterJob(Context context, Long outletId,Location location, Long visitDateTime,String reason,String token) {
        PersistableBundle extras = new PersistableBundle();
        extras.putLong(Constant.EXTRA_PARAM_OUTLET_ID,outletId);
        extras.putInt(Constant.EXTRA_PARAM_OUTLET_STATUS_ID,outletStatus);
        extras.putLong(Constant.EXTRA_PARAM_OUTLET_VISIT_TIME,visitDateTime);
        extras.putDouble(Constant.EXTRA_PARAM_PRESELLER_LAT,location.getLatitude());
        extras.putDouble(Constant.EXTRA_PARAM_PRESELLER_LNG,location.getLongitude());
        extras.putString(Constant.EXTRA_PARAM_OUTLET_REASON_N_ORDER,reason);
        extras.putString(Constant.TOKEN, "Bearer "+token);
        ComponentName serviceComponent = new ComponentName(context, MasterDataUploadService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JobIdManager.getJobId(JobIdManager.JOB_TYPE_MASTER_UPLOAD,outletId.intValue()), serviceComponent);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require any network
        builder.setExtras(extras);
        builder.setPersisted(true);
        JobScheduler jobScheduler = ContextCompat.getSystemService(context,JobScheduler.class);
        Objects.requireNonNull(jobScheduler).schedule(builder.build());
    }


    public void updateOutletStatusCode(int code){
        outletStatus = code;
        statusLiveData.postValue(code);
    }

    public LiveData<Integer> getStatusLiveData() {
        return statusLiveData;
    }

    public void onNextClick(Location currentLocation) {
        outlet.setVisitDateTime(Calendar.getInstance().getTimeInMillis());
        Location outletLocation = new Location("Outlet Location");
        outletLocation.setLatitude(outlet.getLatitude());
        outletLocation.setLongitude(outlet.getLongitude());
        double distance = currentLocation.distanceTo(outletLocation);
        // TODO enable this distance calculation check for live build
        /*if(distance>30 && outletStatus<=2)
            outletNearbyPos.postValue(outletLocation);
        else*/
            {
            outlet.setVisitTimeLat(currentLocation.getLatitude());
            outlet.setVisitTimeLng(currentLocation.getLongitude());
            outlet.setVisitStatus(outletStatus);
            if(statusLiveData.getValue()!=null)
            uploadStatus.postValue(statusLiveData.getValue() != 1);

        }
        repository.updateOutlet(outlet);
    }

    public void postOrderWithNoOrder(boolean noOrderFromBooking){
        if(noOrderFromBooking) {
            outletStatus = 6; // 6 means no order from booking view
            uploadStatus.postValue(true);
            outlet.setVisitStatus(outletStatus);
            repository.updateOutlet(outlet);

        }
    }

    public LiveData<Boolean> getUploadStatus() {
        return uploadStatus;
    }

    public LiveData<Boolean> loadProducts(){
        return repository.loadProductsFromServer();
    }

}
