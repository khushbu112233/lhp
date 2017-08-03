package com.aip.targascan.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aip.targascan.R;
import com.aip.targascan.databinding.ListItemLayoutBinding;

import java.util.ArrayList;

/**
 * Created by aipxperts-ubuntu-01 on 1/8/17.
 */

public class ListCotypeAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> products;

    LayoutInflater inflater ;

    public ListCotypeAdapter(Context context, ArrayList<String> products){
        this.context=context;
        this.products=products;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View rowView;


        final ListItemLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_layout, viewGroup, false);
        rowView = binding.getRoot();
        binding.productName.setText(products.get(i));

        return rowView;
    }
}
