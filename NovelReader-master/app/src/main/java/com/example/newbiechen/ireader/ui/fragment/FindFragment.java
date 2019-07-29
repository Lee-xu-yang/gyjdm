package com.example.newbiechen.ireader.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.SectionBean;
import com.example.newbiechen.ireader.model.flag.FindType;
import com.example.newbiechen.ireader.ui.activity.BillboardActivity;
import com.example.newbiechen.ireader.ui.activity.BookListActivity;
import com.example.newbiechen.ireader.ui.activity.BookSortActivity;
import com.example.newbiechen.ireader.ui.adapter.SectionAdapter;
import com.example.newbiechen.ireader.ui.base.BaseFragment;
import com.example.newbiechen.ireader.widget.itemdecoration.DividerItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by newbiechen on 17-4-15.
 */

public class FindFragment extends BaseFragment {
    /******************view************************/
    @BindView(R.id.find_rv_content)
    RecyclerView mRvContent;
    /*******************Object***********************/
    SectionAdapter mAdapter;
    @BindView(R.id.tab_bangdan)
    TabLayout tabBangdan;
    Unbinder unbinder;

    @Override
    protected int getContentId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        setUpAdapter();
        initTabLayout();
    }

    private void initTabLayout() {
        tabBangdan.addTab(tabBangdan.newTab().setText("男生"));
        tabBangdan.addTab(tabBangdan.newTab().setText("女生"));
    }

    private void setUpAdapter() {
        ArrayList<SectionBean> sections = new ArrayList<>();
        for (FindType type : FindType.values()) {
            sections.add(new SectionBean(type.getTypeName(), type.getIconId()));
        }

        mAdapter = new SectionAdapter();
        mRvContent.setHasFixedSize(true);
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mRvContent.setAdapter(mAdapter);
        mAdapter.addItems(sections);
    }


    @Override
    protected void initClick() {
        mAdapter.setOnItemClickListener(
                (view, pos) -> {
                    FindType type = FindType.values()[pos];
                    Intent intent;
                    //跳转
                    switch (type) {
                        case TOP:
                            intent = new Intent(getContext(), BillboardActivity.class);
                            startActivity(intent);
                            break;
                        case SORT:
                            intent = new Intent(getContext(), BookSortActivity.class);
                            startActivity(intent);
                            break;
                        case TOPIC:
                            intent = new Intent(getContext(), BookListActivity.class);
                            startActivity(intent);
                            break;
                    }
                }
        );

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
