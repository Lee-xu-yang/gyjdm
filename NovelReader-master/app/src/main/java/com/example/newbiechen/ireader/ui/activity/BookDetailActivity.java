package com.example.newbiechen.ireader.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookChapterBean;
import com.example.newbiechen.ireader.model.bean.BookDetailBean;
import com.example.newbiechen.ireader.model.bean.BookListBean;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.bean.HotCommentBean;
import com.example.newbiechen.ireader.model.bean.packages.BookChapterPackage;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.presenter.BookDetailPresenter;
import com.example.newbiechen.ireader.presenter.contract.BookDetailContract;
import com.example.newbiechen.ireader.ui.adapter.BookListAdapter;
import com.example.newbiechen.ireader.ui.adapter.CategoryAdapter;
import com.example.newbiechen.ireader.ui.adapter.HotCommentAdapter;
import com.example.newbiechen.ireader.ui.base.BaseMVPActivity;
import com.example.newbiechen.ireader.ui.base.adapter.BaseListAdapter;
import com.example.newbiechen.ireader.utils.Constant;
import com.example.newbiechen.ireader.utils.StatusBarManager;
import com.example.newbiechen.ireader.utils.StringUtils;
import com.example.newbiechen.ireader.utils.SystemBarUtils;
import com.example.newbiechen.ireader.utils.ToastUtils;
import com.example.newbiechen.ireader.widget.RefreshLayout;
import com.example.newbiechen.ireader.widget.itemdecoration.DividerItemDecoration;
import com.example.newbiechen.ireader.widget.page.PageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by newbiechen on 17-5-4.
 */

public class BookDetailActivity extends BaseMVPActivity<BookDetailContract.Presenter>
        implements BookDetailContract.View {
    public static final String RESULT_IS_COLLECTED = "result_is_collected";

    private static final String TAG = "BookDetailActivity";
    private static final String EXTRA_BOOK_ID = "extra_book_id";

    private static final int REQUEST_READ = 1;

    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.book_detail_iv_cover)
    ImageView mIvCover;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.book_detail_tv_title)
    TextView mTvTitle;
    @BindView(R.id.book_detail_tv_author)
    TextView mTvAuthor;
    @BindView(R.id.book_detail_tv_type)
    TextView mTvType;
    @BindView(R.id.book_detail_tv_word_count)
    TextView mTvWordCount;
    /*@BindView(R.id.book_detail_tv_lately_update)
    TextView mTvLatelyUpdate;*/
    @BindView(R.id.book_list_tv_chase)
    TextView mTvChase;
    @BindView(R.id.book_detail_tv_read)
    TextView mTvRead;
    @BindView(R.id.book_detail_tv_follower_count)
    TextView mTvFollowerCount;
    @BindView(R.id.book_detail_tv_retention)
    TextView mTvRetention;
    @BindView(R.id.book_detail_tv_day_word_count)
    TextView mTvDayWordCount;
    @BindView(R.id.book_detail_tv_brief)
    TextView mTvBrief;
    @BindView(R.id.detail)
    RelativeLayout detail;
    /*@BindView(R.id.book_detail_tv_more_comment)
    TextView mTvMoreComment;*/
    /*@BindView(R.id.book_detail_rv_hot_comment)
    RecyclerView mRvHotComment;*/
    @BindView(R.id.book_detail_rv_community)
    RelativeLayout mRvCommunity;
    @BindView(R.id.book_detail_tv_community)
    TextView mTvCommunity;
    //@BindView(R.id.book_detail_tv_posts_count)
    TextView mTvPostsCount;
    @BindView(R.id.book_list_tv_recommend_book_list)
    TextView mTvRecommendBookList;
    @BindView(R.id.book_detail_rv_recommend_book_list)
    RecyclerView mRvRecommendBookList;

    /************************************/
    private HotCommentAdapter mHotCommentAdapter;
    private BookListAdapter mBookListAdapter;
    private CollBookBean mCollBookBean;
    private ProgressDialog mProgressDialog;
    /*******************************************/
    private String mBookId;
    private boolean isBriefOpen = false;
    private boolean isCollected = false;
    private List<BookChapterBean> list=new ArrayList<>();
    private ListAdapter adapter;
    private ListView list_content;

    public static void startActivity(Context context, String bookId) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(EXTRA_BOOK_ID, bookId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected BookDetailContract.Presenter bindPresenter() {
        return new BookDetailPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null) {
            mBookId = savedInstanceState.getString(EXTRA_BOOK_ID);
        } else {
            mBookId = getIntent().getStringExtra(EXTRA_BOOK_ID);
        }
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        int color = ContextCompat.getColor(this, R.color.transparent1);
        StatusBarManager.setStatusBarColor(this, color);
        //getSupportActionBar().setTitle("书籍详情");
    }

    @SuppressLint("ResourceType")
    @Override
    protected void initClick() {
        super.initClick();
        mRvCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow();
            }
        });

        //可伸缩的TextView
        mTvBrief.setOnClickListener(
                (view) -> {
                    if (isBriefOpen) {
                        mTvBrief.setMaxLines(4);
                        isBriefOpen = false;
                    } else {
                        mTvBrief.setMaxLines(8);
                        isBriefOpen = true;
                    }
                }
        );

        back.setOnClickListener(
                (v) -> onBackPressed()
        );

        mTvChase.setOnClickListener(
                (V) -> {
                    //点击存储
                    if (isCollected) {
                        //放弃点击
                        BookRepository.getInstance()
                                .deleteCollBookInRx(mCollBookBean);

                        mTvChase.setText(getResources().getString(R.string.nb_book_detail_chase_update));

                        //修改背景
                        //Drawable drawable = getResources().getDrawable(R.drawable.selector_btn_book_list);
                        //mTvChase.setBackground(drawable);
                        //设置图片
                        //mTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.jiahao), null,
                                //null, null);
                        isCollected = false;
                    } else {
                        mPresenter.addToBookShelf(mCollBookBean);
                        mTvChase.setText(getResources().getString(R.string.nb_book_detail_give_up));
                        mTvChase.setTextColor(getResources().getColor(R.color.common_gray));

                        //修改背景
                        //Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
                        //mTvChase.setBackground(drawable);
                        //设置图片
                        //mTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete), null,
                                //null, null);
                        isCollected = true;
                    }
                }
        );

        mTvRead.setOnClickListener(
                (v) -> startActivityForResult(new Intent(this, ReadActivity.class)
                        .putExtra(ReadActivity.EXTRA_IS_COLLECTED, isCollected)
                        .putExtra(ReadActivity.EXTRA_COLL_BOOK, mCollBookBean), REQUEST_READ)
        );
    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    private void initPopWindow() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_pop, null);
        list_content = inflate.findViewById(R.id.list_content);
        PopupWindow popupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, 1200);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        //backgroundAlpha(1f);
        popupWindow.setBackgroundDrawable(new ColorDrawable(R.color.white));
        popupWindow.setAnimationStyle(R.style.popwidow_show);
        popupWindow.showAtLocation(detail, Gravity.BOTTOM,0,0);
        adapter=new ListAdapter(this,list);
        list_content.setAdapter(adapter);
        list_content.setFastScrollEnabled(true);
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha=0.7f;
        this.getWindow().setAttributes(lp);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        adapter=new ListAdapter(this,list);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mRefreshLayout.showLoading();
        mPresenter.refreshBookDetail(mBookId);
        mPresenter.getBookChapterPackage(mBookId);
    }

    @Override
    public void finishRefresh(BookDetailBean bean) {
        //封面
        Glide.with(this)
                .load(Constant.IMG_BASE_URL + bean.getCover())
                .placeholder(R.drawable.ic_book_loading)
                .error(R.drawable.ic_load_error)
                .centerCrop()
                .into(mIvCover);
        //书名
        mTvTitle.setText(bean.getTitle());
        //作者
        mTvAuthor.setText(bean.getAuthor());
        //类型
        mTvType.setText(bean.getMajorCate());

        //总字数
        mTvWordCount.setText(getResources().getString(R.string.nb_book_word, bean.getWordCount() / 10000));
        //更新时间
        //mTvLatelyUpdate.setText(StringUtils.dateConvert(bean.getUpdated(), Constant.FORMAT_BOOK_DATE));
        //追书人数
        mTvFollowerCount.setText(bean.getFollowerCount() + "");
        //存留率
        mTvRetention.setText(bean.getRetentionRatio() + "%");
        //日更字数
        mTvDayWordCount.setText(bean.getSerializeWordCount() + "");
        //简介
        mTvBrief.setText(bean.getLongIntro());
        //社区
        //mTvCommunity.setText(getResources().getString(R.string.nb_book_detail_community, bean.getTitle()));
        //帖子数
        //mTvPostsCount.setText(getResources().getString(R.string.nb_book_detail_posts_count, bean.getPostCount()));
        mCollBookBean = BookRepository.getInstance().getCollBook(bean.get_id());

        //判断是否收藏
        if (mCollBookBean != null) {
            isCollected = true;

            //mTvChase.setText(getResources().getString(R.string.nb_book_detail_give_up));
            //修改背景
            //Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
           //mTvChase.setBackground(drawable);
            //设置图片
            //mTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete), null,
                    //null, null);
            mTvRead.setText("继续阅读");
        } else {
            mCollBookBean = bean.getCollBookBean();
        }
    }

    @Override
    public void finishHotComment(List<HotCommentBean> beans) {
        if (beans.isEmpty()) {
            return;
        }
        mHotCommentAdapter = new HotCommentAdapter();
      /*  mRvHotComment.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //与外部ScrollView滑动冲突
                return false;
            }
        });
        mRvHotComment.addItemDecoration(new DividerItemDecoration(this));
        mRvHotComment.setAdapter(mHotCommentAdapter);*/
        mHotCommentAdapter.addItems(beans);
    }

    @Override
    public void finishRecommendBookList(List<BookListBean> beans) {
        if (beans.isEmpty()) {
            mTvRecommendBookList.setVisibility(View.GONE);
            return;
        }
        //推荐书单列表
        mBookListAdapter = new BookListAdapter();
        mRvRecommendBookList.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //与外部ScrollView滑动冲突
                return false;
            }
        });
        mRvRecommendBookList.addItemDecoration(new DividerItemDecoration(this));
        mRvRecommendBookList.setAdapter(mBookListAdapter);
        mBookListAdapter.addItems(beans);

        mBookListAdapter.setOnItemClickListener(
                (view,pos) -> {
                    BookListBean bean = mBookListAdapter.getItem(pos);
                    Log.e("ggg", "finishRecommendBookList: "+bean.get_id());
                    BookListDetailActivity.startActivity(this,bean.get_id());
                }
        );
    }

    @Override
    public void finishList(List<BookChapterBean> beans) {
        if (beans!=null){
            list.addAll(beans);
            if (adapter!=null){
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void waitToBookShelf() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("正在添加到书架中");
        }
        mProgressDialog.show();
    }

    @Override
    public void errorToBookShelf() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        ToastUtils.show("加入书架失败，请检查网络");
    }

    @Override
    public void succeedToBookShelf() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        ToastUtils.show("加入书架成功");
    }

    @Override
    public void showError() {
        mRefreshLayout.showError();
    }

    @Override
    public void complete() {
        mRefreshLayout.showFinish();
    }

    /*******************************************************/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_BOOK_ID, mBookId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果进入阅读页面收藏了，页面结束的时候，就需要返回改变收藏按钮
        if (requestCode == REQUEST_READ) {
            if (data == null) {
                return;
            }

            isCollected = data.getBooleanExtra(RESULT_IS_COLLECTED, false);

            if (isCollected) {
                mTvChase.setText(getResources().getString(R.string.nb_book_detail_give_up));
                //修改背景
                //Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
                //mTvChase.setBackground(drawable);
                //设置图片
                //mTvChase.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete), null,
                        //null, null);
                mTvRead.setText("继续阅读");
            }
        }
    }

    class ListAdapter extends BaseAdapter{

        private Context context;
        private List<BookChapterBean> list;

        public ListAdapter(Context context, List<BookChapterBean> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<BookChapterBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.layout_list, null);
            TextView title = inflate.findViewById(R.id.title);
            title.setText(list.get(i).getTitle());
            return inflate;
        }
    }
}
