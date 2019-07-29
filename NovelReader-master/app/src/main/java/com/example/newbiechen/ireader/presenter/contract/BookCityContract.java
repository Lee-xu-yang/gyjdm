package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.BookCitySearchBean;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.bean.RecommenBeanCkass;
import com.example.newbiechen.ireader.ui.base.BaseContract;

import java.util.List;

public interface BookCityContract {

    interface Presenter extends BaseContract.BasePresenter<View>{
       void finishRefresh();
        void finishUpdate();
        void showErrorTip(String error);
        void initBookSearch();
    }

    interface View extends BaseContract.BaseView{
       void createDownloadTask(CollBookBean collBookBean);
       void updateCollBooks();
       void onSuccess(List<BookCitySearchBean.SearchHotWordsBean> beans);
       void onFailure();
        //void loadRecommendBooks(String gender);
    }
}
