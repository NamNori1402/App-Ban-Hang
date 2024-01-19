package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.appbanhang.R;
import com.example.appbanhang.utils.utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

public class ThanhtoanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien, txtsdt, txtemail;
    EditText edtdiachi;
    AppCompatButton btndathang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanhtoan);
        initView();
        initControl();
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
    });
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        long tongtien =getIntent().getLongExtra("tongtien",0);
        txttongtien.setText(decimalFormat.format(tongtien));
        txtemail.setText(utils.user_current.getEmail());
        txtsdt.setText(utils.user_current.getMobile());


        btndathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi= edtdiachi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(),"ban chua nhap dia chi",Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("test", new Gson().toJson(utils.manggiohang));

                }
            }
        });
}
    private void initView() {
        toolbar = findViewById(R.id.toobar);
        txttongtien = findViewById(R.id.txttongtien);
        txtemail = findViewById(R.id.txtemail);
        txtsdt = findViewById(R.id.txtsodienthoai);
        edtdiachi = findViewById(R.id.editdiachi);
        btndathang= findViewById(R.id.btndathang);
    }
}