package com.optimus.eds.source;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.optimus.eds.AnnotationExclusionStrategy;
import com.optimus.eds.Constant;
import com.optimus.eds.EdsApplication;
import com.optimus.eds.utils.PreferenceUtil;
import com.optimus.eds.utils.Util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by sidhu on 4/11/2018.
 */

public class RetrofitHelper implements Constant {

    public static final String BASE_URL_DEV = "http://192.168.8.100:51185/";
    public static final String BASE_URL = "http://optimuseds.com/API/";

    private static RetrofitHelper instance;
    public Retrofit retrofit;
    API service;


    private static final String TAG = "RetrofitHelper";
    private RetrofitHelper () {

        retrofit = getRetrofit();
        service = retrofit.create(API.class);
    }

    public static RetrofitHelper getInstance() {
        if (instance==null) {
            instance = new RetrofitHelper();
        }
        return instance;
    }



    public API getApi() {
        return service;
    }


    public class AuthorizationInterceptor implements Interceptor {
        Request request;
        Response response;
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            String token = Util.getAuthorizationHeader(EdsApplication.getInstance());

            if (token!=null) {
                if(originalRequest.header("Authorization")==null || originalRequest.header("Authorization").isEmpty())
                request = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + token).build();
                else
                    request = originalRequest;
                response = chain.proceed(request);

                if (response.code()== 401) {
                    API tokenApi =  getRetrofit().create(API.class);
                    retrofit2.Response<TokenResponse> tokenResponse= tokenApi.refreshToken("password","imran","imranshabrati").execute();
                    if(tokenResponse.isSuccessful()){
                        TokenResponse tokenResponseObj=tokenResponse.body();

                        try {
                            request= response.request().newBuilder()
                                    .header("Authorization", "Bearer " + tokenResponseObj.getAccessToken()).build();
                            response = chain.proceed(request);
                            PreferenceUtil.getInstance(EdsApplication.getInstance()).saveToken(tokenResponseObj.getAccessToken());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        // goto Login as we cannot refresh token
                    }

                }

                return response;
            } else {
                return chain.proceed(originalRequest);
            }



        }
    }

    private Retrofit getRetrofit(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new AuthorizationInterceptor());
        Gson builder = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(builder))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClientBuilder.build())
                .build();
    }


}
