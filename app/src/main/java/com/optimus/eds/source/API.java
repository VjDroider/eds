package com.optimus.eds.source;

import com.google.gson.JsonObject;
import com.optimus.eds.db.entities.Merchandise;
import com.optimus.eds.db.entities.Outlet;
import com.optimus.eds.db.entities.Route;
import com.optimus.eds.model.BaseResponse;
import com.optimus.eds.model.MasterModel;
import com.optimus.eds.model.MerchandiseModel;
import com.optimus.eds.model.OrderResponseModel;
import com.optimus.eds.model.PackageProductResponseModel;
import com.optimus.eds.model.RouteOutletResponseModel;


import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    @FormUrlEncoded
    @POST("token")
    Single<TokenResponse> getToken(@Field("grant_type") String type , @Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("token")
    Call<TokenResponse> refreshToken(@Field("grant_type") String type , @Field("username") String username, @Field("password") String password);


    @GET("route/routes")
    Call<RouteOutletResponseModel> loadTodayRouteOutlets();

    @GET("route/products")
    Call<PackageProductResponseModel> loadTodayPackageProduct();

    @POST("api/order/calculateprice")
    Single<OrderResponseModel> calculatePricing(@Body OrderResponseModel order);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/order/PostOrder")
    Single<MasterModel> saveOrder(@Body MasterModel order,@Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/route/PostOutletVisit")
    Single<BaseResponse> postMerchandise(@Body MerchandiseModel merchandise,@Header("Authorization") String auth);

    @FormUrlEncoded
    @POST("api/AppOpertion/LogStartEnd")
    Single<BaseResponse> updateStartEndStatus(@FieldMap Map<String, Long> params);


    @GET("routes")
    Call<List<Route>> getRoutes(@Query("id") String userId);

    @GET("outlets")
    Call<List<Outlet>> getOutlets(@Query("id") String routeId);


}
