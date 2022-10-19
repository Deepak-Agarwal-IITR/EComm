package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    TextView responseText, priceText;
    Map<Integer,Store> myStores = new HashMap<>();
    Map<Integer,Product> myProducts = new HashMap<>();

    private static final DecimalFormat df = new DecimalFormat("0.00");
    LinearLayout productsLinearLayout;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout main_layout = findViewById(R.id.main_layout);
        responseText = (TextView) findViewById(R.id.textView);
        productsLinearLayout = (LinearLayout) findViewById(R.id.productsLinearLayout);
        priceText = (TextView) findViewById(R.id.priceText);

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

    public TextView getTextView(Context context, String text,int width,int textSize,int gravity){
        TextView t = new TextView(context);
        t.setText(text);
        t.setWidth(width);
        t.setTextSize(textSize);
        t.setGravity(gravity);

        return t;
    }

    public LinearLayout.LayoutParams getLayoutParams(int width,int height,int ml, int mt, int mr,int mb){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        layoutParams.setMargins(ml,mt,mr,mb);

        return layoutParams;
    }
    public LinearLayout.LayoutParams getLayoutParams(int width,int height,float weight,int ml, int mt, int mr,int mb){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height,weight);
        layoutParams.setMargins(ml,mt,mr,mb);

        return layoutParams;
    }

    public LinearLayout getLinearLayout(Context context, int orientation, LinearLayout.LayoutParams layoutParams){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(orientation);
        linearLayout.setLayoutParams(layoutParams);

        return linearLayout;
    }
    private void handleCompletion() {

        for (Map.Entry<Integer, Product> entry : myProducts.entrySet()) {
            LinearLayout horizontalLinearLayout = getLinearLayout(this, LinearLayout.HORIZONTAL,
                    getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,30,5,30,5)) ;

            LinearLayout textVerticalLinearLayout = getLinearLayout(this, LinearLayout.VERTICAL,
                    getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,0.15f,15,5,15,5));

            LinearLayout plusVerticalLinearLayout = getLinearLayout(this, LinearLayout.VERTICAL,
                    getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.85f,15,5,15,5));
            plusVerticalLinearLayout.setGravity(Gravity.CENTER);

            TextView plus = getTextView(this,"+",LinearLayout.LayoutParams.MATCH_PARENT,30,Gravity.CENTER);
            plus.setTag("p"+entry.getValue().getId());
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addProductInOrder(view);
                }
            });

            textVerticalLinearLayout.addView(getTextView(this,entry.getValue().getName()+": "+entry.getValue().getPrice(),LinearLayout.LayoutParams.MATCH_PARENT,20,Gravity.CENTER_HORIZONTAL));
            textVerticalLinearLayout.addView(getTextView(this,myStores.get(entry.getValue().getStoreId()).getName(),LinearLayout.LayoutParams.MATCH_PARENT,20,Gravity.CENTER_HORIZONTAL));

            plusVerticalLinearLayout.addView(plus);

            horizontalLinearLayout.addView(textVerticalLinearLayout);
            horizontalLinearLayout.addView(plusVerticalLinearLayout);

            productsLinearLayout.addView(horizontalLinearLayout);
        }
    }

    public void addProductInOrder(View v){
        String pId = v.getTag().toString();
        int productId = Integer.parseInt(pId.substring(1));

        order.addProduct(myProducts.get(productId));
        priceText.setText(df.format(order.getTotalPrice()));
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
    
    public void goToCart(View view){
        Toast.makeText(this, "going to cart", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
    }
}