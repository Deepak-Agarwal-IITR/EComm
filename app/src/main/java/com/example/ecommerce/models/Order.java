package com.example.ecommerce.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Order implements Serializable {

    private Map<Product,Integer> products;

    public Order() {
        products = new HashMap<>();
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

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
    public String toString() {
        String s = "Order: \n";

        for (Map.Entry<Product,Integer> entry : products.entrySet()) {
            s+= entry.getKey().toString()+" -- "+entry.getValue()+"\n";
        }
        return s;
    }

    public int getQuantity(Product p) {
        return products.getOrDefault(p, 0);
    }
}
