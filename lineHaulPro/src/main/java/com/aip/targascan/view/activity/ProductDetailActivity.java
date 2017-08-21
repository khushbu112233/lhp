package com.aip.targascan.view.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.aip.targascan.R;
import com.aip.targascan.common.util.Pref;
import com.aip.targascan.databinding.ProductDetailLayoutBinding;
import com.aip.targascan.databinding.ProductDetailNewLayoutBinding;
import com.aip.targascan.model.ProductDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.aip.targascan.view.activity.OrderListDisplayActivity.products;

/**
 * Created by aipxperts-ubuntu-01 on 31/7/17.
 */

public class ProductDetailActivity extends Activity{
    ProductDetailLayoutBinding mBinding;
    ProductDetailNewLayoutBinding binding;
    public ArrayList<ProductDetail> productDetailArrayList=new ArrayList<>();
    String warning="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.product_detail_new_layout);


        Bundle data = getIntent().getExtras();
        if(data!=null) {

            productDetailArrayList = data.getParcelableArrayList("productDetailArrayList");
            warning = data.getString("warning");
            Log.e("filelist",""+productDetailArrayList);
        }
        binding.webview.loadUrl(Pref.getValue(ProductDetailActivity.this,"Detail_url",""));
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.txtCartoonNum.setText(productDetailArrayList.get(0).getCarton_num1());
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /**
         * old code without webview
         */
       /* Bundle data = getIntent().getExtras();
        if(data!=null) {

            productDetailArrayList = data.getParcelableArrayList("productDetailArrayList");
            warning = data.getString("warning");
            Log.e("filelist",""+productDetailArrayList);
        }
           if(warning.equalsIgnoreCase("1"))
        {
            mBinding.txtWarning.setVisibility(View.VISIBLE);
            mBinding.txtWarning.setText("More than one carton number "+productDetailArrayList.get(0).getCarton_num1()+" found for co type "+productDetailArrayList.get(0).getCo_type1()+" .Please contact manager regarding this problem.");
        }else
        {
            mBinding.txtWarning.setVisibility(View.GONE);
        }
        String url = "https://yourcargoonline.com/"+productDetailArrayList.get(0).getCo_type1()+".jpg";
        Picasso.with(this).load(url).into(mBinding.imgproduct);
        if(warning.equalsIgnoreCase("1"))
        {
            mBinding.txtWarning.setVisibility(View.VISIBLE);
            mBinding.txtWarning.setText("More than one carton number "+productDetailArrayList.get(0).getCarton_num1()+" found for co type "+productDetailArrayList.get(0).getCo_type1()+" .Please contact manager regarding this problem.");
        }else
        {
            mBinding.txtWarning.setVisibility(View.GONE);
        }
        if(productDetailArrayList.get(0).getPupname().equalsIgnoreCase(""))
        {
            mBinding.llPup.setVisibility(View.GONE);
        }else
        {
            mBinding.llPup.setVisibility(View.VISIBLE);
        }
        mBinding.txtClientId.setText(productDetailArrayList.get(0).getCo_type1());
        mBinding.txtOrder.setText("#"+productDetailArrayList.get(0).getWork_order_num());
        mBinding.txtCarton.setText("#"+productDetailArrayList.get(0).getCarton_num1());
        mBinding.txtDeliveredOn.setText(productDetailArrayList.get(0).getDate());
        mBinding.txtDeliverByDriverId.setText(productDetailArrayList.get(0).getDriver_id1());
        mBinding.txtPickUpName.setText(productDetailArrayList.get(0).getPupname());
        mBinding.txtPickUpAddress.setText(productDetailArrayList.get(0).getPupaddress());
        mBinding.txtPickupCity.setText(productDetailArrayList.get(0).getPupcity());
        mBinding.txtPickZip.setText(productDetailArrayList.get(0).getPupzip());
        mBinding.txtDeliverToName.setText(productDetailArrayList.get(0).getDelivery_name());
        mBinding.txtDeliverToAddress.setText(productDetailArrayList.get(0).getDelivery_address());
        mBinding.txtDeliverToCity.setText(productDetailArrayList.get(0).getCity());
        mBinding.txtDeliverToZipCode.setText(productDetailArrayList.get(0).getZip());
        mBinding.txtQuantity.setText(productDetailArrayList.get(0).getQuantity());
        mBinding.txtWeight.setText(productDetailArrayList.get(0).getWeight());
        mBinding.txtStatusOfDeliver.setText(productDetailArrayList.get(0).getRedeliver1());
        mBinding.txtPickupBetween.setText(productDetailArrayList.get(0).getComment());
        if(!productDetailArrayList.get(0).getSig_string().equalsIgnoreCase("")) {
            byte[] decodedString = Base64.decode(productDetailArrayList.get(0).getSig_string(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            mBinding.imgSign.setImageBitmap(decodedByte);
        }else
        {
            mBinding.imgSign.setVisibility(View.GONE);
        }*/
    }
}
