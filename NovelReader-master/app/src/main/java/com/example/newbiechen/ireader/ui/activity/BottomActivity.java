package com.example.newbiechen.ireader.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.RecommendBookEvent;
import com.example.newbiechen.ireader.fragment.BookCityFragment;
import com.example.newbiechen.ireader.fragment.BookShelfFragment;
import com.example.newbiechen.ireader.fragment.BookListFragment;
import com.example.newbiechen.ireader.fragment.BookShelfGrilFragment;
import com.example.newbiechen.ireader.fragment.BookSortFragment;
import com.example.newbiechen.ireader.ui.dialog.SexChooseDialog;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.SharedPreUtils;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class BottomActivity extends AppCompatActivity {

    private boolean isPrepareFinish = false;
    private static final int WAIT_INTERVAL = 2000;
    private static final int NOT_NOTICE = 2;

    private MenuItem mItem;

    private int mItemId;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            resetToDefaultIcon();
            mItem = item;

            mItemId = item.getItemId();
            switch (mItemId) {
                case R.id.navigation_home:
                    //registerFragment(new BookShelfFragment());
                    //item.setIcon(R.mipmap.shujia_click);
                    String sex1 = SharedPreUtils.getInstance()
                            .getString(Constant.SHARED_SEX);
                    if (!sex1.equals("")){
                        if (sex1.equals("boy")){
                            registerFragment(new BookShelfFragment());
                        }
                        if (sex1.equals("girl")){
                            registerFragment(new BookShelfGrilFragment());
                        }
                    }
                    return true;
                case R.id.navigation_dashboard:
                    registerFragment(new BookCityFragment());
                    //item.setIcon(R.mipmap.shucheng_click);
                    return true;
                case R.id.navigation_notifications:
                    registerFragment(new BookListFragment());
                    //item.setIcon(R.mipmap.bangdan_click);
                    return true;
                case R.id.navigation_sort:
                    registerFragment(new BookSortFragment());
                    //item.setIcon(R.mipmap.fenlei_click);
                    return true;
            }
            return false;
        }
    };

    private void resetToDefaultIcon() {
        MenuItem shujia =  navView.getMenu().findItem(R.id.navigation_home);
        shujia.setIcon(R.mipmap.shujia);
        MenuItem shucheng =  navView.getMenu().findItem(R.id.navigation_dashboard);
        shucheng.setIcon(R.mipmap.shucheng);
        MenuItem bangdan =  navView.getMenu().findItem(R.id.navigation_notifications);
        bangdan.setIcon(R.mipmap.bangdan1);
        MenuItem fenlei =  navView.getMenu().findItem(R.id.navigation_sort);
        fenlei.setIcon(R.mipmap.fenlei1);
    }

    private BottomNavigationView navView;
    private ViewPager mVp;

    public void registerFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (mItem != null && navView != null) {
            int itemId = R.id.navigation_home;
            int icon = R.mipmap.shujia_click;

            Class<? extends Fragment> fragmentClass = fragment.getClass();
            if (fragmentClass == BookShelfFragment.class) {
                icon = R.mipmap.shujia_click;
                itemId = R.id.navigation_home;
            } else if (fragmentClass == BookCityFragment.class) {
                icon = R.mipmap.shucheng_click;
                itemId = R.id.navigation_dashboard;
            } else if (fragmentClass == BookListFragment.class) {
                icon = R.mipmap.bangdan_click;
                itemId = R.id.navigation_notifications;
            } else if (fragmentClass == BookSortFragment.class) {
                icon = R.mipmap.fenlei_click;
                itemId = R.id.navigation_sort;
            }

            resetToDefaultIcon();
            MenuItem item = navView.getMenu().findItem(itemId);
            item.setIcon(icon);
            item.setChecked(true);
        }

        transaction.replace(R.id.ft, fragment);
        transaction.commit();
    }

    private void showSexChooseDialog(){
        String sex = SharedPreUtils.getInstance()
                .getString(Constant.SHARED_SEX);
        if (sex.equals("")){
            mVp.postDelayed(()-> {
                /*dialog = new SexChooseDialog(this);
                dialog.show();*/
                AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.CommonDialog)
                        .setView(R.layout.dialog_sex_choose).create();
                alertDialog.show();
                Button choose_btn_boy = alertDialog.findViewById(R.id.choose_btn_boy);
                Button choose_btn_girl = alertDialog.findViewById(R.id.choose_btn_girl);
                choose_btn_boy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreUtils.getInstance()
                                .putString(Constant.SHARED_SEX,Constant.SEX_BOY);
                        RxBus.getInstance().post(new RecommendBookEvent("male"));
                        alertDialog.dismiss();
                        registerFragment(new BookShelfFragment());
                    }
                });
                choose_btn_girl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreUtils.getInstance()
                                .putString(Constant.SHARED_SEX,Constant.SEX_GIRL);
                        RxBus.getInstance().post(new RecommendBookEvent("female"));
                        alertDialog.dismiss();
                        registerFragment(new BookShelfGrilFragment());
                    }
                });
            },500);
            String sex1 = SharedPreUtils.getInstance()
                    .getString(Constant.SHARED_SEX);
            if (!sex1.equals("")){
                if (sex1.equals(SharedPreUtils.getInstance().getString(Constant.SEX_BOY))){
                    registerFragment(new BookShelfFragment());
                }
                if (sex1.equals(SharedPreUtils.getInstance().getString(Constant.SEX_GIRL))){
                    registerFragment(new BookShelfGrilFragment());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==NOT_NOTICE){
            myRequetPermission();//由于不知道是否选择了允许所以需要再次判断
        }
    }

    private void myRequetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.CommonDialog)
                    .setView(R.layout.dialog_sex_choose).create();
            alertDialog.show();
            Button choose_btn_boy = alertDialog.findViewById(R.id.choose_btn_boy);
            Button choose_btn_girl = alertDialog.findViewById(R.id.choose_btn_girl);
            choose_btn_boy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreUtils.getInstance()
                            .putString(Constant.SHARED_SEX,Constant.SEX_BOY);
                    RxBus.getInstance().post(new RecommendBookEvent("male"));
                    alertDialog.dismiss();
                    registerFragment(new BookShelfFragment());
                }
            });
            choose_btn_girl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreUtils.getInstance()
                            .putString(Constant.SHARED_SEX,Constant.SEX_GIRL);
                    RxBus.getInstance().post(new RecommendBookEvent("female"));
                    alertDialog.dismiss();
                    registerFragment(new BookShelfGrilFragment());
                }
            });
        }else {
            //Toast.makeText(this,"您已经申请了权限!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sex1 = SharedPreUtils.getInstance()
                .getString(Constant.SHARED_SEX);
        if (!sex1.equals("")){
            if (sex1.equals("boy")){
                registerFragment(new BookShelfFragment());
            }
            if (sex1.equals("girl")){
                registerFragment(new BookShelfGrilFragment());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(!isPrepareFinish){
            mVp.postDelayed(
                    () -> isPrepareFinish = false,WAIT_INTERVAL
            );
            isPrepareFinish = true;
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
        navView = (BottomNavigationView) findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
       // BottomNavigationViewHelper.disableShiftMode(navView);
        //registerFragment(new BookShelfFragment());
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setUpSex();
        myRequetPermission();
    }

    private void setUpSex() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_vp, null);
        mVp = inflate.findViewById(R.id.vp);
        showSexChooseDialog();
    }
}
