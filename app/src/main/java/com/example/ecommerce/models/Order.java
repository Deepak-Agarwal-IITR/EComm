package com.example.ecommerce.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Order implements Parcelable {

    private Map<Product,Integer> products;

    public Order() {
        products = new HashMap<>();
    }

    protected Order(Parcel in) {
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public void addProduct(Product p){
        if(products.containsKey(p)){
            int value = products.get(p);
            products.put(p,value+1);
        }else{
            products.put(p,1);
        }
    }

    public boolean removeProduct(Product p){
        if(products.containsKey(p)){
            products.remove(p);
            return true;
        }else{
            return false;
        }
    }

    public boolean decreaseProduct(Product p, int quantity){
        if(products.containsKey(p)){
            int value = products.get(p);
            if(value==1){
                products.remove(p);
                return true;
            }

            products.put(p,value-1);
            return true;
        }else{
            return false;
        }
    }

    public double getTotalPrice(){
        double sum = 0;

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            sum += (quantity * product.getPrice());
        }

        return sum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
