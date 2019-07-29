package com.example.newbiechen.ireader.presenter;

import android.util.Log;

import com.example.newbiechen.ireader.model.bean.BookListBean;
import com.example.newbiechen.ireader.model.bean.BookListBean1;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.BillBookContract;
import com.example.newbiechen.ireader.ui.base.RxPresenter;
import com.example.newbiechen.ireader.utils.LogUtils;
import com.example.newbiechen.ireader.utils.RxUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-5-3.
 */

public class BillBookPresenter extends RxPresenter<BillBookContract.View>
        implements BillBookContract.Presenter {
    private static final String TAG = "BillBookPresenter";
    @Override
    public void refreshBookBrief(String billId) {
        Disposable remoteDisp = RemoteRepository.getInstance()
                .getBillBooks(billId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (beans)-> {
                            if (mView!=null&&beans!=null){
                                mView.finishRefresh(beans);
                                mView.complete();
                            }

                        }
                        ,
                        (e) ->{
                            if (mView!=null){
                                mView.showError();
                            }
                            LogUtils.e(e);
                        }
                );
        addDisposable(remoteDisp);
    }

    @Override
    public void RecommandList() {
        RemoteRepository.getInstance().getBookList()
                .doOnSuccess(new Consumer<BookListBean1>() {
                    @Override
                    public void accept(BookListBean1 maleBeans) throws Exception {

                    }
                }).compose(RxUtils::toSimpleSingle)
                .subscribe(maleBeans -> {
                    if (mView!=null){
                        mView.finishLoading(maleBeans);
                        mView.complete();
                    }

                        },
                        (e) -> {
                            //提示没有网络
                            LogUtils.e(e);
                            mView.showLoadError();
                            mView.complete();
                        }
                );
        addDisposable(mDisposable);
    }
}
