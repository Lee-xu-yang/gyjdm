package com.example.newbiechen.ireader.presenter;

import com.example.newbiechen.ireader.model.bean.packages.BookSortPackage;
import com.example.newbiechen.ireader.model.bean.packages.BookSubSortPackage;
import com.example.newbiechen.ireader.model.remote.RemoteRepository;
import com.example.newbiechen.ireader.presenter.contract.SearchContract;
import com.example.newbiechen.ireader.ui.base.RxPresenter;
import com.example.newbiechen.ireader.utils.LogUtils;
import com.example.newbiechen.ireader.utils.RxUtils;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by newbiechen on 17-6-2.
 */

public class SearchPresenter extends RxPresenter<SearchContract.View>
        implements SearchContract.Presenter{

    @Override
    public void searchHotWord() {
        Disposable disp = RemoteRepository.getInstance()
                .getHotWords()
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        bean -> {
                            mView.finishHotWords(bean);
                        },
                        e -> {
                            LogUtils.e(e);
                        }
                );
        addDisposable(disp);
    }

    @Override
    public void searchKeyWord(String query) {
        Disposable disp = RemoteRepository.getInstance()
                .getKeyWords(query)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        bean -> {
                            mView.finishKeyWords(bean);
                        },
                        e -> {
                            LogUtils.e(e);
                        }
                );
        addDisposable(disp);
    }

    @Override
    public void searchBook(String query) {
        Disposable disp = RemoteRepository.getInstance()
                .getSearchBooks(query)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        bean -> {
                            mView.finishBooks(bean);
                        },
                        e -> {
                            LogUtils.e(e);
                            mView.errorBooks();
                        }
                );
        addDisposable(disp);
    }

    @Override
    public void refreshSortBean() {
        Single<BookSortPackage> sortSingle = RemoteRepository.getInstance()
                .getBookSortPackage();
        Single<BookSubSortPackage> subSortSingle = RemoteRepository.getInstance()
                .getBookSubSortPackage();

        Single<SortPackage> zipSingle =  Single.zip(sortSingle, subSortSingle,
                new BiFunction<BookSortPackage, BookSubSortPackage, SortPackage>() {
                    @Override
                    public SortPackage apply(BookSortPackage bookSortPackage, BookSubSortPackage subSortPackage) throws Exception {
                        return new SortPackage(bookSortPackage,subSortPackage);
                    }
                });

        Disposable disposable = zipSingle.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (bean) ->{
                            mView.finishRefresh(bean.sortPackage,bean.subSortPackage);
                            mView.complete();
                        }
                        ,
                        (e) -> {
                            mView.showError();
                            LogUtils.e(e);
                        }
                );
        addDisposable(disposable);
    }

    class SortPackage{
        BookSortPackage sortPackage;
        BookSubSortPackage subSortPackage;

        public SortPackage(BookSortPackage sortPackage, BookSubSortPackage subSortPackage){
            this.sortPackage = sortPackage;
            this.subSortPackage = subSortPackage;
        }
    }
}
