package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.models.Product;
import com.example.ecommerce.models.Store;
import com.example.ecommerce.services.APIClient;
import com.example.ecommerce.services.APIInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
//import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    TextView responseText;
    Map<Integer,Store> myStores = new HashMap<>();
    Map<Integer,Product> myProducts = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout main_layout = findViewById(R.id.main_layout);

        responseText = (TextView) findViewById(R.id.textView);

//        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

//        getStores();
//        getProducts();
        callEndpoints();

        GridLayout gridLayout = (GridLayout) findViewById(R.id.GridLayout);
        for (Map.Entry<Integer,Product> entry : myProducts.entrySet()){
            TextView t1 = new TextView(this);
            TextView t2 = new TextView(this);
            t1.setText(entry.getValue().getName());
            t2.setText(myStores.get(entry.getValue().getStoreId()).getName());

            gridLayout.addView(t1);
            gridLayout.addView(t2);
        }

    }

    private void callEndpoints() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Observable<List<Store>> storeObservable = apiInterface.doGetStoreList();

        Observable<List<Product>> productObservable = apiInterface.doGetProductList();

        Observable.merge(storeObservable, productObservable)
                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError);



    }

    private void handleResults(List<? extends Object> objects) {
        if (objects != null && objects.size() != 0) {
//            recyclerViewAdapter.setData(productList);
            for(int x=  0;x<objects.size();x++){
                if(objects.get(x) instanceof Product){
                    myProducts.put(((Product)(objects.get(x))).getId(),(Product)(objects.get(x)));
                }else if(objects.get(x) instanceof Store){
                    myStores.put(((Store)objects.get(x)).getId(),(Store) objects.get(x));
                }
            }
        } else {
            Toast.makeText(this, "NO RESULTS FOUND",
                    Toast.LENGTH_LONG).show();
        }
    }


//    private void handleResults(List<Product> productList) {
//        if (productList != null && productList.size() != 0) {
////            recyclerViewAdapter.setData(productList);
//        } else {
//            Toast.makeText(this, "NO RESULTS FOUND",
//                    Toast.LENGTH_LONG).show();
//        }
//    }

    private void handleError(Throwable t) {

        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again",
                Toast.LENGTH_LONG).show();
    }

//    private void getStores(){
//        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
//
//        Call<List<Store>> storesCall = apiInterface.doGetStoreList();
//        storesCall.enqueue(new Callback<List<Store>>() {
//            @Override
//            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
//                List<Store> stores = response.body();
//
//                for(Store s: stores){
//                    myStores.put(s.getId(),s);
//                }
//
//                Toast.makeText(MainActivity.this, "Store data fetched.", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onFailure(Call<List<Store>> call, Throwable t) {
//                responseText.setText("FAILED");
//                call.cancel();
//            }
//        });
//    }
//
//    private void getProducts(){
//        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
//
//        Call<List<Product>> productsCall = apiInterface.doGetProductList();
//        productsCall.enqueue(new Callback<List<Product>>() {
//            @Override
//            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                List<Product> products = response.body();
//
//                for(Product p: products){
//                    myProducts.put(p.getId(),p);
//                }
//
//                Toast.makeText(MainActivity.this, "Product data fetched.", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onFailure(Call<List<Product>> call, Throwable t) {
//                responseText.setText("FAILED");
//                call.cancel();
//            }
//        });
//    }
}