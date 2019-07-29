package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.model.bean.BookCitySearchBean;
import com.example.newbiechen.ireader.model.bean.BookListBean;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.BookCityContract;
import com.example.newbiechen.ireader.ui.base.RxPresenter;
import com.example.newbiechen.ireader.utils.LogUtils;
import com.example.newbiechen.ireader.utils.RxUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

public class BookCityPresenter extends RxPresenter<BookCityContract.View> implements BookCityContract.Presenter{


    @Override
    public void finishRefresh() {

    }

    @Override
    public void finishUpdate() {

    }

    @Override
    public void showErrorTip(String error) {

    }

    @Override
    public void initBookSearch() {
        RemoteRepository.getInstance().getBookCitySearch()
                .doOnSuccess(new Consumer<List<BookCitySearchBean.SearchHotWordsBean>>() {
                    @Override
                    public void accept(List<BookCitySearchBean.SearchHotWordsBean> hotWordsBeans) throws Exception {

                    }
                }).compose(RxUtils::toSimpleSingle)
                .subscribe(maleBeans -> {
                            mView.onSuccess(maleBeans);
                            mView.complete();
                        },
                        (e) -> {
                            //提示没有网络
                            LogUtils.e(e);
                            mView.onFailure();
                            mView.complete();
                        }
                );
        addDisposable(mDisposable);
    }
}
