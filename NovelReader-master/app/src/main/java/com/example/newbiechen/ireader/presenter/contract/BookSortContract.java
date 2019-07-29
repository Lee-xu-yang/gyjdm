package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.RecommenBeanCkass;
import com.example.newbiechen.ireader.model.bean.packages.BookSortPackage;
import com.example.newbiechen.ireader.model.bean.packages.BookSubSortPackage;
import com.example.newbiechen.ireader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by newbiechen on 17-4-23.
 */

public interface BookSortContract {

    interface View extends BaseContract.BaseView{
        void finishRefresh(BookSortPackage sortPackage, BookSubSortPackage subSortPackage);
        void finish(List<RecommenBeanCkass.MaleBean> list);
        void load(List<RecommenBeanCkass.FemaleBean> beans);
        void showErrorTip(String s);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void refreshSortBean();
        void recommenBeanCkass();
    }
}
