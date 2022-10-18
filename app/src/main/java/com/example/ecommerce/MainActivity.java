package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.models.Store;
import com.example.ecommerce.services.APIClient;
import com.example.ecommerce.services.APIInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

        callEndpoints();

        Order order = new Order();

    }

    private void callEndpoints() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Observable<List<Store>> storeObservable = apiInterface.doGetStoreList();
        Observable<List<Product>> productObservable = apiInterface.doGetProductList();

        Observable.merge(storeObservable, productObservable)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResults, this::handleError,this::handleCompletion);

    }

    private void handleCompletion() {
        responseText.setText(myStores.size()+": :"+myProducts.size());
        for (Map.Entry<Integer,Product> entry : myProducts.entrySet()){
            Toast.makeText(this, ""+entry.getValue().toString(), Toast.LENGTH_SHORT).show();
        }
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
    private void handleResults(List<? extends Object> objects) {
        if (objects != null && objects.size() != 0) {
            for(int x=  0;x<objects.size();x++){
                if(objects.get(x) instanceof Product){
                    myProducts.put(((Product)(objects.get(x))).getId(),(Product)(objects.get(x)));
                }else if(objects.get(x) instanceof Store){
                    myStores.put(((Store)objects.get(x)).getId(),(Store) objects.get(x));
                }
            }
        }else {
            Toast.makeText(this, "NO RESULTS FOUND", Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(Throwable t) {
        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again", Toast.LENGTH_LONG).show();
    }
}