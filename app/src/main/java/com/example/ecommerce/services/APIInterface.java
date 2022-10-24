package com.example.ecommerce.services;

import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.models.Store;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {

    @GET("/storeinfo")
    Observable<List<Store>> doGetStoreList();

    @GET("/productinfo")
    Observable<List<Product>> doGetProductList();

    @FormUrlEncoded
    @POST("/orderDone")
    Call<Order> placeOrder(@FieldMap Map<String, String> params, @Field("address") String address);
}
