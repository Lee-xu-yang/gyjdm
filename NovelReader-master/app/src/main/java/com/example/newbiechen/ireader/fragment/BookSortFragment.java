package com.example.newbiechen.ireader.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.RecommenBeanCkass;
import com.example.newbiechen.ireader.model.flag.BookSortListType;
import com.example.newbiechen.ireader.presenter.BookShelfPresenter;
import com.example.newbiechen.ireader.presenter.contract.BookShelfContract;
import com.example.newbiechen.ireader.ui.activity.BottomActivity;
import com.example.newbiechen.ireader.ui.activity.SearchActivity;
import com.example.newbiechen.ireader.ui.base.BaseMVPFragment;
import com.example.newbiechen.ireader.ui.fragment.BookSortListFragment;
import com.example.newbiechen.ireader.ui.fragment.SortBoyFragment;
import com.example.newbiechen.ireader.ui.fragment.SortGirlsFragment;
import com.example.newbiechen.ireader.utils.StatusBarManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookSortFragment extends BaseMVPFragment<BookShelfContract.Presenter> implements BookShelfContract.View {


    @BindView(R.id.tab_sort)
    TabLayout tabSort;
    Unbinder unbinder;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.search_img)
    ImageView search_img;

    @Override
    protected int getContentId() {
        return R.layout.fragment_sort;
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

    @Override
    public void finishRefresh(List<RecommenBeanCkass.MaleBean> maleBeans) {

    }

    @Override
    public void finishUpdate() {

    }

    @Override
    public void showErrorTip(String error) {

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
        tabSort.addTab(tabSort.newTab().setText("男生"));
        tabSort.addTab(tabSort.newTab().setText("女生"));

        List<Fragment> list = new ArrayList<>();
        list.add(new SortBoyFragment());
        list.add(new SortGirlsFragment());

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

        tabSort.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabSort));
    }

    @Override
    protected BookShelfContract.Presenter bindPresenter() {
        return new BookShelfPresenter();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

}
