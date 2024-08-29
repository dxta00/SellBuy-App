package com.example.sellbuy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sellbuy.databinding.ActivityBuyBinding;
import com.example.sellbuy.databinding.ActivityUserSignupBinding;

public class Buy extends AppCompatActivity {
    ActivityBuyBinding am;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        am = DataBindingUtil.setContentView(this, R.layout.activity_buy);
        am.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Buy.this,Thankyou.class);
                startActivity(intent);
            }
        });
    }

}