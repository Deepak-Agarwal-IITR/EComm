package com.example.ecommerce.services;

import com.example.ecommerce.models.Product;
import com.example.ecommerce.models.Store;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

//    @GET("/storeinfo")
//    Call<List<Store>> doGetStoreList();
//
//    @GET("/productinfo")
//    Call<List<Product>> doGetProductList();

    @GET("/storeinfo")
    Observable<List<Store>> doGetStoreList();

    @GET("/productinfo")
    Observable<List<Product>> doGetProductList();
}
