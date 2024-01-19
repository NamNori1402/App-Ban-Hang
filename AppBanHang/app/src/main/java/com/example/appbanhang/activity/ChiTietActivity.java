package com.example.appbanhang.activity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.GioHang;
import com.example.appbanhang.model.SanPhamMoi;
import com.example.appbanhang.utils.utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp, giasp, mota;
    Button btnthem;
    Spinner spinner;
    ImageView imghinhanh;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        innitControl();
    }

    private void innitControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                themGioHang();
            }
        });
    }


    private void themGioHang() {
                if(utils.manggiohang.size()>0){
                    boolean flag = false;
                    int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                    for(int i= 0; i< utils.manggiohang.size(); i++) {
                        if (utils.manggiohang.get(i).getIdsp() == sanPhamMoi.getId()) {
                            utils.manggiohang.get(i).setSoluong(soluong + utils.manggiohang.get(i).getSoluong());
                            long gia = Long.parseLong(sanPhamMoi.getGiasp()) * utils.manggiohang.get(i).getSoluong();
                            utils.manggiohang.get(i).setGiasp(gia);
                            flag = true;
                        }
                    }
                    if(flag == false){

                        long gia = Long.parseLong(sanPhamMoi.getGiasp()) * soluong;
                        GioHang gioHang = new GioHang();
                        gioHang.setGiasp(gia);
                        gioHang.setSoluong(soluong);
                        gioHang.setIdsp(sanPhamMoi.getId());
                        gioHang.setTensp(sanPhamMoi.getTensp());
                        gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                        utils.manggiohang.add(gioHang);
                        }

                }else {
                    int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                    long gia = Long.parseLong(sanPhamMoi.getGiasp()) * soluong;
                    GioHang gioHang = new GioHang();
                    gioHang.setGiasp(gia);
                    gioHang.setSoluong(soluong);
                    gioHang.setIdsp(sanPhamMoi.getId());
                    gioHang.setTensp(sanPhamMoi.getTensp());
                    gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                    utils.manggiohang.add(gioHang);

                }
                int totalItem = 0;
                for(int i = 0; i<utils.manggiohang.size();i++){
                    totalItem = totalItem + utils.manggiohang.get(i).getSoluong();
                }
                badge.setText(String.valueOf(utils.manggiohang.size()));

            }

    private void initData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(imghinhanh);
        DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
        giasp.setText("giá: "+ decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+"Đ");
        Integer[] so= new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);
    }

    private void initView() {
        tensp= findViewById(R.id.txttensp);
        giasp= findViewById(R.id.txtgiasp);
        mota= findViewById(R.id.txtmotachitiet);
        btnthem= findViewById(R.id.btnthemvaogiohang);
        spinner= findViewById(R.id.spinner);
        imghinhanh=findViewById(R.id.imgchitiet);
        toolbar= findViewById(R.id.toobar);
        badge= findViewById(R.id.menu_sl);
        FrameLayout frameLayoutgiohang = findViewById(R.id.framegiohang);
        frameLayoutgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });
        if(utils.manggiohang !=null){
            int totalItem = 0;
            for(int i = 0; i<utils.manggiohang.size();i++){
                totalItem = totalItem + utils.manggiohang.get(i).getSoluong();
            }
        }
            badge.setText(String.valueOf(utils.manggiohang.size()));
    }
        private void ActionToolBar() {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

    @Override
    protected void onResume() {

        super.onResume();
        if(utils.manggiohang !=null){
            int totalItem = 0;
            for(int i = 0; i<utils.manggiohang.size();i++){
                totalItem = totalItem + utils.manggiohang.get(i).getSoluong();
            }
        }
        badge.setText(String.valueOf(utils.manggiohang.size()));

    }
}