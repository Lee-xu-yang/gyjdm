package com.example.newbiechen.ireader.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BillBookBean;
import com.example.newbiechen.ireader.model.bean.BookListBean;
import com.example.newbiechen.ireader.model.bean.BookListBean1;
import com.example.newbiechen.ireader.presenter.BillBookPresenter;
import com.example.newbiechen.ireader.presenter.contract.BillBookContract;
import com.example.newbiechen.ireader.ui.activity.BookDetailActivity;
import com.example.newbiechen.ireader.ui.activity.BottomActivity;
import com.example.newbiechen.ireader.ui.activity.SearchActivity;
import com.example.newbiechen.ireader.ui.adapter.BookListAdapter;
import com.example.newbiechen.ireader.ui.base.BaseMVPFragment;
import com.example.newbiechen.ireader.ui.fragment.BillBookFragment;
import com.example.newbiechen.ireader.ui.fragment.BillBoyFragment;
import com.example.newbiechen.ireader.ui.fragment.BillGrilsFragment;
import com.example.newbiechen.ireader.utils.StatusBarManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends BaseMVPFragment<BillBookContract.Presenter> implements BillBookContract.View {


    @BindView(R.id.tab)
    TabLayout tab;
    Unbinder unbinder;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.search_img)
    ImageView search_img;
    private List<Fragment> list;

    @Override
    public void finishRefresh(List<BillBookBean> beans) {

    }

    @Override
    public void finishLoading(BookListBean1 beans) {

    }



    @Override
    public void showLoadError() {

    }

    @Override
    protected void initClick() {
        super.initClick();
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setTabLayout();
        int color = ContextCompat.getColor(getActivity(), R.color.transparent1);
        StatusBarManager.setStatusBarColor(getActivity(), color);
    }

    private void setTabLayout() {
        list = new ArrayList<>();
        list.add(new BillBoyFragment());
        list.add(new BillGrilsFragment());

        tab.addTab(tab.newTab().setText("男生"));
        tab.addTab(tab.newTab().setText("女生"));

        vp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vp.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
    }

    @Override
    protected BillBookContract.Presenter bindPresenter() {
        return new BillBookPresenter();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
