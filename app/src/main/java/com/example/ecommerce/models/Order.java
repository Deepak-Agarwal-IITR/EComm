package com.example.ecommerce.models;

import java.util.HashMap;
import java.util.Map;

public class Order {

    private Map<Product,Integer> products;

    public Order() {
        products = new HashMap<>();
    }

    void addProduct(Product p){
        if(products.containsKey(p)){
            int value = products.get(p);
            products.put(p,value+1);
        }else{
            products.put(p,1);
        }
    }

    boolean removeProduct(Product p){
        if(products.containsKey(p)){
            products.remove(p);
            return true;
        }else{
            return false;
        }
    }

    boolean decreaseProduct(Product p, int quantity){
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

    double getTotalPrice(){
        double sum = 0;

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            sum += (quantity * product.getPrice());
        }

        return sum;
    }

}
