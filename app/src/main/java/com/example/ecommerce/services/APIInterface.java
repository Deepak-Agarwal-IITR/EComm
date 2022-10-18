package com.example.ecommerce.services;

import com.example.ecommerce.models.Product;
import com.example.ecommerce.models.Store;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("/storeinfo")
    Observable<List<Store>> doGetStoreList();

    @GET("/productinfo")
    Observable<List<Product>> doGetProductList();
}
