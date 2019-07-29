package com.example.newbiechen.ireader.presenter.contract;

import com.example.newbiechen.ireader.model.bean.packages.BookSortPackage;
import com.example.newbiechen.ireader.model.bean.packages.BookSubSortPackage;
import com.example.newbiechen.ireader.model.bean.packages.SearchBookPackage;
import com.example.newbiechen.ireader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by newbiechen on 17-6-2.
 */

public interface SearchContract extends BaseContract {

    interface View extends BaseView{
        void finishHotWords(List<String> hotWords);
        void finishKeyWords(List<String> keyWords);
        void finishBooks(List<SearchBookPackage.BooksBean> books);
        void finishRefresh(BookSortPackage sortPackage, BookSubSortPackage subSortPackage);

        void errorBooks();
    }

    interface Presenter extends BasePresenter<View>{
        void searchHotWord();
        //搜索提示
        void searchKeyWord(String query);
        //搜索书籍
        void searchBook(String query);

        void refreshSortBean();
    }
}
