package com.example.newbiechen.ireader.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BillBookBean;
import com.example.newbiechen.ireader.model.bean.BookListBean;
import com.example.newbiechen.ireader.model.bean.BookListBean1;
import com.example.newbiechen.ireader.presenter.BillBookPresenter;
import com.example.newbiechen.ireader.presenter.contract.BillBookContract;
import com.example.newbiechen.ireader.ui.base.BaseMVPFragment;
import com.example.newbiechen.ireader.ui.fragment.BookCityBoyFragment;
import com.example.newbiechen.ireader.ui.fragment.BookCityGrilFragment;
import com.example.newbiechen.ireader.ui.fragment.BookCitySeletorFragment;
import com.example.newbiechen.ireader.utils.StatusBarManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookCityFragment extends BaseMVPFragment<BillBookContract.Presenter> implements BillBookContract.View {

    Unbinder unbinder;
    @BindView(R.id.tab_shelf)
    TabLayout tabShelf;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.book_shelf_rv_content)
    LinearLayout bookShelfRvContent;

    @Override
    protected int getContentId() {
        return R.layout.fragment_book_city;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpTabLayout();
        int color = ContextCompat.getColor(getActivity(), R.color.transparent1);
        StatusBarManager.setStatusBarColor(getActivity(), color);
    }

    private void setUpTabLayout() {
        tabShelf.addTab(tabShelf.newTab().setText("精选"));
        tabShelf.addTab(tabShelf.newTab().setText("男生"));
        tabShelf.addTab(tabShelf.newTab().setText("女生"));

        List<Fragment> list=new ArrayList<>();
        list.add(new BookCitySeletorFragment());
        list.add(new BookCityBoyFragment());
        list.add(new BookCityGrilFragment());

        vp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return list.get(i);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });

        tabShelf.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabShelf));
    }

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
