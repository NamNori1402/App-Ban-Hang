package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.adapter.LoaiSPAdapter;
import com.example.appbanhang.adapter.SanPhamMoiAdapter;
import com.example.appbanhang.model.LoaiSp;
import com.example.appbanhang.model.SanPhamMoi;
import com.example.appbanhang.model.SanPhamMoiModel;
import com.example.appbanhang.retrofit.ApiBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.utils.utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/** @noinspection deprecation*/
public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSPAdapter loaiSPAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable =new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(utils.BASE_URL).create(ApiBanHang.class);
        Anhxa();
        ActionBar();
        ActionViewFlipper();
        if(isConnected(this)){

            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEventclick();
        }
        else {
            Toast.makeText(getApplicationContext(),"khong co internet",Toast.LENGTH_LONG).show();
        }
    }
    private void getEventclick(){
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                }
            }
        });
    }
    @SuppressLint("SuspiciousIndentation")
    private void getSpMoi(){
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess())
                                mangSpMoi= sanPhamMoiModel.getResult();
                                spAdapter= new SanPhamMoiAdapter(getApplicationContext(), mangSpMoi);
                                recyclerViewManHinhChinh.setAdapter(spAdapter);

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "khong ket noi" +throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                loaiSpmodel -> {
                    if(loaiSpmodel.isSuccess()){
//                        Toast.makeText(getApplicationContext(),loaiSpmodel.getResult().get(0).getTensanpham(),Toast.LENGTH_LONG).show();
                        mangloaisp=loaiSpmodel.getResult();

                        loaiSPAdapter = new LoaiSPAdapter(getApplicationContext(),mangloaisp);

                        listViewManHinhChinh.setAdapter(loaiSPAdapter);
                    }
                }
        ));
    }

    private void ActionViewFlipper(){
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://scontent.fhan14-3.fna.fbcdn.net/v/t1.6435-9/195048033_4142811425784630_386029521867339958_n.png?stp=dst-png_p843x403&_nc_cat=104&ccb=1-7&_nc_sid=730e14&_nc_ohc=UxJZeIn1uIwAX_iTrjj&_nc_ht=scontent.fhan14-3.fna&oh=00_AfAMR2erGPlrHxFRtdRmvnFvO6Ery1e7Jc4TtaovD-NgIw&oe=6546D8B7");
        mangquangcao.add("https://cdn.tgdd.vn/Files/2021/06/02/1356835/ho-tro-mua-dich-giao-1h-re-bat-ngo-giam-soc-den-4.jpg");
        //mangquangcao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for(int i=0;i<mangquangcao.size();i++){
            ImageView imageView= new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);

        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setInAnimation(slide_out);
    }
    private void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    private void Anhxa(){
        toolbar= findViewById(R.id.toobarmanhinhchinh);
        viewFlipper= findViewById(R.id.viewlipper);
        recyclerViewManHinhChinh=findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager= new GridLayoutManager(this, 2);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
        recyclerViewManHinhChinh.setHasFixedSize(true);
        listViewManHinhChinh=findViewById(R.id.listviewmanhinhchinh);
        navigationView=findViewById(R.id.navigationview);
        drawerLayout=findViewById(R.id.drawerLayout);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegiohang);
//khoi tao list
        mangloaisp=new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        if(utils.manggiohang == null){
            utils.manggiohang = new ArrayList<>();
        }else {
            int totalItem = 0;
            for(int i = 0; i<utils.manggiohang.size();i++){
                totalItem = totalItem + utils.manggiohang.get(i).getSoluong();
            }
            badge.setText(String.valueOf(utils.manggiohang.size()));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for(int i = 0; i<utils.manggiohang.size();i++){
            totalItem = totalItem + utils.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(utils.manggiohang.size()));
    }

    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi;
        wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile;
        mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi!= null && wifi.isConnected()) || (mobile.isConnected())){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    protected void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }
}