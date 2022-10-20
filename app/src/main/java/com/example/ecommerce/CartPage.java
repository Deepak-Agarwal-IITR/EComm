package com.example.ecommerce;

import static com.example.ecommerce.utils.LayoutUtils.df;
import static com.example.ecommerce.utils.LayoutUtils.getLayoutParams;
import static com.example.ecommerce.utils.LayoutUtils.getLinearLayout;
import static com.example.ecommerce.utils.LayoutUtils.getTextView;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.Serializable;
import java.util.Map;

public class CartPage extends AppCompatActivity {

    Order order;
    LinearLayout productsLinearLayout;
    Map<Integer, Store> myStores;
    TextView priceTextView, errorTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);
        productsLinearLayout = (LinearLayout) findViewById(R.id.productsLinearLayout);
        priceTextView = (TextView) findViewById(R.id.priceTextView);
        errorTextView = (TextView) findViewById(R.id.errorTextView);

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("orderDetails");

        priceTextView.setText(df.format(order.getTotalPrice()));
        myStores = (Map<Integer, Store>) intent.getSerializableExtra("storeDetails");

        for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
            LinearLayout horizontalLinearLayout = getLinearLayout(this, LinearLayout.HORIZONTAL,
                    getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,30,5,30,5)) ;

            LinearLayout textVerticalLinearLayout = getLinearLayout(this, LinearLayout.VERTICAL,
                    getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,0.35f,15,5,15,5));

            LinearLayout plusHorizontalLinearLayout = getLinearLayout(this,LinearLayout.HORIZONTAL,
                    getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,0.65f,15,5,15,5));

            LinearLayout.LayoutParams layoutParams = getLayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT,(1.0f/3f),5,0,5,0);

            TextView minus = getTextView(this,"-",30,Gravity.CENTER);
            minus.setLayoutParams(layoutParams);
            minus.setTag(entry.getKey());
            minus.setOnClickListener(this::removeProductInOrder);

            TextView quantity = getTextView(this,""+order.getQuantity(entry.getKey()),30,Gravity.CENTER);
            quantity.setTag("pq"+entry.getKey().getId());
            quantity.setLayoutParams(layoutParams);

            TextView plus = getTextView(this,"+",30,Gravity.CENTER);
            plus.setTag(entry.getKey());
            plus.setLayoutParams(layoutParams);
            plus.setOnClickListener(this::addProductInOrder);

            textVerticalLinearLayout.addView(getTextView(this,entry.getKey().getName()+": "+entry.getKey().getPrice(),LinearLayout.LayoutParams.MATCH_PARENT,20,Gravity.CENTER_HORIZONTAL));
            textVerticalLinearLayout.addView(getTextView(this,myStores.get(entry.getKey().getStoreId()).getName(),LinearLayout.LayoutParams.MATCH_PARENT,20,Gravity.CENTER_HORIZONTAL));

            plusHorizontalLinearLayout.addView(minus);
            plusHorizontalLinearLayout.addView(quantity);
            plusHorizontalLinearLayout.addView(plus);

            horizontalLinearLayout.addView(textVerticalLinearLayout);
            horizontalLinearLayout.addView(plusHorizontalLinearLayout);
            productsLinearLayout.addView(horizontalLinearLayout);
        }
    }

    public void backToProducts(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("orderDetails", (Serializable) order);
        intent.putExtra("storeDetails", (Serializable) myStores);
        startActivity(intent);
        finish();
    }

    public void addProductInOrder(View view){
        Product product = (Product) view.getTag();
        order.addProduct(product);

        LinearLayout linearLayout = (LinearLayout) view.getParent();
        TextView quantityTextView = (TextView) linearLayout.findViewWithTag("pq" + product.getId());
        quantityTextView.setText(""+order.getQuantity(product));
        priceTextView.setText(df.format(order.getTotalPrice()));
    }

    public void removeProductInOrder(View view) {
        Product product = (Product) view.getTag();

        if(order.decreaseProduct(product,1)) {
            LinearLayout linearLayout = (LinearLayout) view.getParent();
            TextView quantityTextView = (TextView) linearLayout.findViewWithTag("pq"+product.getId());
            quantityTextView.setText(""+order.getQuantity(product));
            priceTextView.setText(df.format(order.getTotalPrice()));
        }
    }
}