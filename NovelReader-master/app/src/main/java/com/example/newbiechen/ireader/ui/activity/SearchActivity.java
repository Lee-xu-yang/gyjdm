package com.example.newbiechen.ireader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookSubSortBean;
import com.example.newbiechen.ireader.model.bean.packages.BookSortPackage;
import com.example.newbiechen.ireader.model.bean.packages.BookSubSortPackage;
import com.example.newbiechen.ireader.model.bean.packages.SearchBookPackage;
import com.example.newbiechen.ireader.presenter.SearchPresenter;
import com.example.newbiechen.ireader.presenter.contract.SearchContract;
import com.example.newbiechen.ireader.ui.adapter.KeyWordAdapter;
import com.example.newbiechen.ireader.ui.adapter.SearchBookAdapter;
import com.example.newbiechen.ireader.ui.base.BaseMVPActivity;
import com.example.newbiechen.ireader.widget.RefreshLayout;
import com.example.newbiechen.ireader.widget.itemdecoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gujun.android.taggroup.TagGroup;

/**
 * Created by newbiechen on 17-4-24.
 */

public class SearchActivity extends BaseMVPActivity<SearchContract.Presenter>
        implements SearchContract.View {
    private static final String TAG = "SearchActivity";
    private static final int TAG_LIMIT = 8;

    @BindView(R.id.search_iv_back)
    ImageView mIvBack;
    @BindView(R.id.search_et_input)
    EditText mEtInput;
    @BindView(R.id.search_iv_delete)
    ImageView mIvDelete;
    @BindView(R.id.search_iv_search)
    ImageView mIvSearch;
    @BindView(R.id.search_book_tv_refresh_hot)
    TextView mTvRefreshHot;
    @BindView(R.id.search_tg_hot)
    TagGroup mTgHot;
    /*    @BindView(R.id.search_rv_history)
        RecyclerView mRvHistory;*/
    @BindView(R.id.refresh_layout)
    RefreshLayout mRlRefresh;
    @BindView(R.id.refresh_rv_content)
    RecyclerView mRvSearch;
    @BindView(R.id.hot_bang)
    RelativeLayout hotBang;
    @BindView(R.id.qianli)
    RelativeLayout qianli;
    @BindView(R.id.up)
    RelativeLayout up;
    @BindView(R.id.wanjie)
    RelativeLayout wanjie;
    @BindView(R.id.good)
    RelativeLayout good;

    private KeyWordAdapter mKeyWordAdapter;
    private SearchBookAdapter mSearchAdapter;
    private ArrayList<BookSubSortBean> list = new ArrayList<>();
    private ArrayList<BookSubSortBean> females = new ArrayList<>();
    private String title;

    private boolean isTag;
    private List<String> mHotTagList;
    private int mTagStart = 0;
    private BookSubSortBean bookSubSortBean;
    private BookSubSortBean bookSubSortBean1;

    @Override
    protected int getContentId() {
        return R.layout.activity_search;
    }

    @Override
    protected SearchContract.Presenter bindPresenter() {
        return new SearchPresenter();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mRlRefresh.setBackground(ContextCompat.getDrawable(this, R.color.white));
        setUpAdapter();
    }

    private void setUpAdapter() {
        mKeyWordAdapter = new KeyWordAdapter();
        mSearchAdapter = new SearchBookAdapter();

        mRvSearch.setLayoutManager(new LinearLayoutManager(this));
        mRvSearch.addItemDecoration(new DividerItemDecoration(this));
    }

    public void get(String title) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMajor().equals(title)) {
                bookSubSortBean = list.get(i);
            }
        }
        for (int i = 0; i < females.size(); i++) {
            if (females.get(i).getMajor().equals(title)){
                bookSubSortBean1 = females.get(i);
            }
        }
    }

    @Override
    protected void initClick() {
        super.initClick();

        //退出
        mIvBack.setOnClickListener(
                (v) -> onBackPressed()
        );

        hotBang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = "玄幻";
                get(title);
                BookSortListActivity.startActivity(SearchActivity.this, "male", bookSubSortBean);
            }
        });

        qianli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = "武侠";
                get(title);
                BookSortListActivity.startActivity(SearchActivity.this, "male", bookSubSortBean);
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = "都市";
                get(title);
                BookSortListActivity.startActivity(SearchActivity.this, "male", bookSubSortBean);
            }
        });

        wanjie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = "科幻";
                get(title);
                BookSortListActivity.startActivity(SearchActivity.this, "female", bookSubSortBean1);
            }
        });

        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = "女尊";
                get(title);
                BookSortListActivity.startActivity(SearchActivity.this, "female", bookSubSortBean1);
            }
        });

        //输入框
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().equals("")) {
                    //隐藏delete按钮和关键字显示内容
                    if (mIvDelete.getVisibility() == View.VISIBLE) {
                        mIvDelete.setVisibility(View.INVISIBLE);
                        mRlRefresh.setVisibility(View.INVISIBLE);
                        //删除全部视图
                        mKeyWordAdapter.clear();
                        mSearchAdapter.clear();
                        mRvSearch.removeAllViews();
                    }
                    return;
                }
                //显示delete按钮
                if (mIvDelete.getVisibility() == View.INVISIBLE) {
                    mIvDelete.setVisibility(View.VISIBLE);
                    mRlRefresh.setVisibility(View.VISIBLE);
                    //默认是显示完成状态
                    mRlRefresh.showFinish();
                }

                //搜索
                String query = s.toString().trim();
                if (isTag) {
                    mRlRefresh.showLoading();
                    mPresenter.searchBook(query);
                    isTag = false;
                } else {
                    //传递
                    mPresenter.searchKeyWord(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //键盘的搜索
        mEtInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchBook();
                    return true;
                }
                return false;
            }
        });

        //进行搜索
        mIvSearch.setOnClickListener(
                (v) -> searchBook()
        );

        //删除字
        mIvDelete.setOnClickListener(
                (v) -> {
                    mEtInput.setText("");
                    toggleKeyboard();
                }
        );

        //点击查书
        mKeyWordAdapter.setOnItemClickListener(
                (view, pos) -> {
                    //显示正在加载
                    mRlRefresh.showLoading();
                    String book = mKeyWordAdapter.getItem(pos);
                    mPresenter.searchBook(book);
                    toggleKeyboard();
                }
        );

        //Tag的点击事件
        mTgHot.setOnTagClickListener(
                tag -> {
                    isTag = true;
                    mEtInput.setText(tag);
                }
        );

        //Tag的刷新事件
        mTvRefreshHot.setOnClickListener(
                (v) -> refreshTag()
        );

        //书本的点击事件
        mSearchAdapter.setOnItemClickListener(
                (view, pos) -> {
                    String bookId = mSearchAdapter.getItem(pos).get_id();
                    BookDetailActivity.startActivity(this, bookId);
                }
        );
    }

    private void searchBook() {
        String query = mEtInput.getText().toString().trim();
        if (!query.equals("")) {
            mRlRefresh.setVisibility(View.VISIBLE);
            mRlRefresh.showLoading();
            mPresenter.searchBook(query);
            //显示正在加载
            mRlRefresh.showLoading();
            toggleKeyboard();
        }
    }

    private void toggleKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        //默认隐藏
        mRlRefresh.setVisibility(View.GONE);
        //获取热词
        mPresenter.searchHotWord();
        mPresenter.refreshSortBean();
    }

    @Override
    public void showError() {
    }

    @Override
    public void complete() {

    }

    @Override
    public void finishHotWords(List<String> hotWords) {
        mHotTagList = hotWords;
        refreshTag();
    }

    private void refreshTag() {
        int last = mTagStart + TAG_LIMIT;
        if (mHotTagList.size() <= last) {
            mTagStart = 0;
            last = TAG_LIMIT;
        }
        List<String> tags = mHotTagList.subList(mTagStart, last);
        mTgHot.setTags(tags);
        mTagStart += TAG_LIMIT;
    }

    @Override
    public void finishKeyWords(List<String> keyWords) {
        if (keyWords.size() == 0) mRlRefresh.setVisibility(View.INVISIBLE);
        mKeyWordAdapter.refreshItems(keyWords);
        if (!(mRvSearch.getAdapter() instanceof KeyWordAdapter)) {
            mRvSearch.setAdapter(mKeyWordAdapter);
        }
    }

    @Override
    public void finishBooks(List<SearchBookPackage.BooksBean> books) {
        mSearchAdapter.refreshItems(books);
        if (books.size() == 0) {
            mRlRefresh.showEmpty();
        } else {
            //显示完成
            mRlRefresh.showFinish();
        }
        //加载
        if (!(mRvSearch.getAdapter() instanceof SearchBookAdapter)) {
            mRvSearch.setAdapter(mSearchAdapter);
        }
    }

    @Override
    public void finishRefresh(BookSortPackage sortPackage, BookSubSortPackage subSortPackage) {
        if (subSortPackage != null) {
            list.addAll(subSortPackage.getMale());
            females.addAll(subSortPackage.getFemale());

        }
    }

    @Override
    public void errorBooks() {
        mRlRefresh.showEmpty();
    }

    @Override
    public void onBackPressed() {
        if (mRlRefresh.getVisibility() == View.VISIBLE) {
            mEtInput.setText("");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
