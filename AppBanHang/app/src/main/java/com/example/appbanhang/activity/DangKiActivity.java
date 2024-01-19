package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbanhang.R;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKiActivity extends AppCompatActivity {
    EditText email, pass,repass,mobile,username;
    AppCompatButton button;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        initView();
        initControl();
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangki();
            }

    });
}
            private void dangki() {
                String str_email = email.getText().toString().trim();
                String str_pass = pass.getText().toString().trim();
                String str_repass = repass.getText().toString().trim();
                String str_mobile = mobile.getText().toString().trim();
                String str_username = username.getText().toString().trim();
                if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(getApplicationContext(), "chua nhap email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(getApplicationContext(), "chua nhap pass", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(str_repass)) {
                    Toast.makeText(getApplicationContext(), "chua nhap repass", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(str_mobile)) {
                    Toast.makeText(getApplicationContext(), "chua nhap mobile", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(str_username)) {
                        Toast.makeText(getApplicationContext(), "chua nhap username", Toast.LENGTH_SHORT).show();
                }else {
                    if(str_pass.equals(str_repass)) {
                        compositeDisposable.add(apiBanHang.dangki(str_email,str_pass,str_username,str_mobile)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        userModel -> {
                                            if (userModel.isSuccess()){
                                                utils.user_current.setEmail(str_email);
                                                utils.user_current.setPass(str_pass);
                                                Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        },
                                        throwable -> {
                                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                ));
                    }else {
                        Toast.makeText(getApplicationContext(),"pass chua khop", Toast.LENGTH_LONG).show();
                    }
                }
            }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        mobile = findViewById(R.id.mobile);
        username = findViewById(R.id.username);
        button = findViewById(R.id.btndangki);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}