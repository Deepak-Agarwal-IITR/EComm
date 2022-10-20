package com.example.ecommerce;

import static com.example.ecommerce.utils.LayoutUtils.df;
import static com.example.ecommerce.utils.LayoutUtils.getLayoutParams;
import static com.example.ecommerce.utils.LayoutUtils.getLinearLayout;
import static com.example.ecommerce.utils.LayoutUtils.getTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.models.Order;
import com.example.ecommerce.models.Product;
import com.example.ecommerce.models.Store;
import com.example.ecommerce.services.APIClient;
import com.example.ecommerce.services.APIInterface;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    TextView responseTextView, priceTextView;
    Map<Integer,Store> myStores = new HashMap<>();
    Map<Integer,Product> myProducts = new HashMap<>();

    LinearLayout productsLinearLayout;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout main_layout = findViewById(R.id.main_layout);
        responseTextView = (TextView) findViewById(R.id.textView);
        productsLinearLayout = (LinearLayout) findViewById(R.id.productsLinearLayout);
        priceTextView = (TextView) findViewById(R.id.priceText);

        callEndpoints();
        order = new Order();
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

        for (Map.Entry<Integer, Product> entry : myProducts.entrySet()) {
            LinearLayout horizontalLinearLayout = getLinearLayout(this, LinearLayout.HORIZONTAL,
                    getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,30,5,30,5)) ;

            LinearLayout textVerticalLinearLayout = getLinearLayout(this, LinearLayout.VERTICAL,
                    getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,0.35f,15,5,15,5));

            LinearLayout plusHorizontalLinearLayout = getLinearLayout(this,LinearLayout.HORIZONTAL,
                    getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0.65f,15,5,15,5));

            LinearLayout.LayoutParams layoutParams = getLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT,(1.0f/3f),5,0,5,0);

            TextView minus = getTextView(this,"-",30,Gravity.CENTER);
            minus.setLayoutParams(layoutParams);
            minus.setTag("p"+entry.getValue().getId());
            minus.setOnClickListener(this::removeProductInOrder);

            TextView quantity = getTextView(this,"0",30,Gravity.CENTER);
            quantity.setTag("pq"+entry.getValue().getId());
            quantity.setLayoutParams(layoutParams);

            TextView plus = getTextView(this,"+",30,Gravity.CENTER);
            plus.setTag("p"+entry.getValue().getId());
            plus.setLayoutParams(layoutParams);
            plus.setOnClickListener(this::addProductInOrder);

            textVerticalLinearLayout.addView(getTextView(this,entry.getValue().getName()+": "+entry.getValue().getPrice(),LinearLayout.LayoutParams.MATCH_PARENT,20,Gravity.CENTER_HORIZONTAL));
            textVerticalLinearLayout.addView(getTextView(this,myStores.get(entry.getValue().getStoreId()).getName(),LinearLayout.LayoutParams.MATCH_PARENT,20,Gravity.CENTER_HORIZONTAL));

            plusHorizontalLinearLayout.addView(minus);
            plusHorizontalLinearLayout.addView(quantity);
            plusHorizontalLinearLayout.addView(plus);

            horizontalLinearLayout.addView(textVerticalLinearLayout);
            horizontalLinearLayout.addView(plusHorizontalLinearLayout);
            productsLinearLayout.addView(horizontalLinearLayout);
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
        Toast.makeText(this, t.getLocalizedMessage()+" ERROR IN FETCHING API RESPONSE. Try again. ", Toast.LENGTH_LONG).show();
    }


    public void addProductInOrder(View view){
        String pId = view.getTag().toString();
        int productId = Integer.parseInt(pId.substring(1));
        order.addProduct(myProducts.get(productId));

        LinearLayout linearLayout = (LinearLayout) view.getParent();
        TextView quantityTextView = (TextView) linearLayout.findViewWithTag("pq" + productId);
        quantityTextView.setText(""+order.getQuantity(myProducts.get(productId)));
        priceTextView.setText(df.format(order.getTotalPrice()));
    }

    public void removeProductInOrder(View view) {
        String pId = view.getTag().toString();
        int productId = Integer.parseInt(pId.substring(1));

        if(order.decreaseProduct(myProducts.get(productId),1)) {
            LinearLayout linearLayout = (LinearLayout) view.getParent();
            TextView quantityTextView = (TextView) linearLayout.findViewWithTag("pq"+productId);
            quantityTextView.setText(""+order.getQuantity(myProducts.get(productId)));
            priceTextView.setText(df.format(order.getTotalPrice()));
        }
    }

    public void goToCart(View view){

        Intent intent = new Intent(this, CartPage.class);
        intent.putExtra("orderDetails", (Serializable) order);
        intent.putExtra("storeDetails", (Serializable) myStores);
        startActivity(intent);
        finish();

    }
}