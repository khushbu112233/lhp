package com.aip.targascan.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.aip.targascan.R;
import com.aip.targascan.common.util.Pref;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aipxperts-ubuntu-01 on 31/7/17.
 */

public class OrderListDisplayActivity extends Activity {
    ListView list_order;
    EditText search_cartun;

    // Listview Adapter
    Button btnSearchNow;
    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
   public static  ArrayList<String> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list_display_layout);
        // Listview Data
       products= new ArrayList<>();
        products.add("Dell Inspiron");
        products.add("HTC One X");
        products.add("HTC Wildfire S");
        products.add("HTC Sense");
        products.add("HTC Sensation XE");
        products.add("iPhone 4S");
        products.add("Samsung Galaxy Note 800");
        products.add("Samsung Galaxy S3");
        products.add("MacBook Air");
        products.add("Mac Mini");
        products.add("MacBook Pro");

        list_order = (ListView) findViewById(R.id.list_order);
        search_cartun = (EditText) findViewById(R.id.search_cartun);
        btnSearchNow = (Button) findViewById(R.id.btnSearchNow);



        list_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent =new Intent(OrderListDisplayActivity.this,ProductDetailActivity.class);
                startActivity(intent);
                Pref.setValue(OrderListDisplayActivity.this,"position",i);
            }
        });

    }
}
