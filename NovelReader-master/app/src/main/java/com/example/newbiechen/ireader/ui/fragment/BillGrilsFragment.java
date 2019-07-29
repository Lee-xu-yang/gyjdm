package com.example.newbiechen.ireader.ui.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BillBookBean;
import com.example.newbiechen.ireader.model.bean.BookListBean;
import com.example.newbiechen.ireader.model.bean.BookListBean1;
import com.example.newbiechen.ireader.presenter.BillBookPresenter;
import com.example.newbiechen.ireader.presenter.contract.BillBookContract;
import com.example.newbiechen.ireader.ui.activity.BookDetailActivity;
import com.example.newbiechen.ireader.ui.base.BaseMVPFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillGrilsFragment extends BaseMVPFragment<BillBookContract.Presenter> implements BillBookContract.View {


    @BindView(R.id.tab_vertical_girl)
    VerticalTabLayout tabVertical;
    @BindView(R.id.book_list)
    RecyclerView bookList;
    Unbinder unbinder;
    private List<BookListBean1.FemaleBean> list = new ArrayList<>();
    private List<BillBookBean> booksBeans = new ArrayList<>();
    private BookListAdapter adapter;
    private String mBillId;
    private LinearLayoutManager manager;

    @Override
    public void finishRefresh(List<BillBookBean> beans) {

        if (beans!=null){
                booksBeans.clear();
                booksBeans.addAll(beans);
                adapter.setList(booksBeans);
                adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void finishLoading(BookListBean1 beans) {
        if (beans != null) {
            list.addAll(beans.getFemale());
            mBillId=list.get(0).get_id();
            mPresenter.refreshBookBrief(mBillId);
            tabVertical.setTabAdapter(new TabAdapter() {

                @Override
                public int getCount() {
                    return list.size();
                }

                @Override
                public ITabView.TabBadge getBadge(int position) {
                    return null;
                }

                @Override
                public ITabView.TabIcon getIcon(int position) {
                    return null;
                }

                @Override
                public ITabView.TabTitle getTitle(int position) {
                    ITabView.TabTitle title = new ITabView.TabTitle.Builder()
                            .setContent(list.get(position).getShortTitle())
                            .setTextColor(Color.RED, Color.BLACK)
                            .build();
                    return title;
                }

                @Override
                public int getBackground(int position) {
                    return 0;
                }
            });
        }
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        adapter=new BookListAdapter(getContext(),booksBeans);
        bookList.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext());
        bookList.setLayoutManager(manager);
    }

    @Override
    public void showLoadError() {

    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.RecommandList();
        mPresenter.refreshBookBrief(mBillId);
    }

    @Override
    protected BillBookContract.Presenter bindPresenter() {
        return new BillBookPresenter();
    }

    @Override
    protected void initClick() {
        super.initClick();
        adapter.setOnItemClcik(new BookListAdapter.OnItemClcik() {
            @Override
            public void onClcik(int position) {
                String bookId = booksBeans.get(position).get_id();
                BookDetailActivity.startActivity(getContext(), bookId);
            }
        });

        tabVertical.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                String id = list.get(position).get_id();
                mPresenter.refreshBookBrief(id);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_bill_grils;
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

    static class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

        private Context context;
        private List<BillBookBean> list;

        public BookListAdapter(Context context, List<BillBookBean> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(List<BillBookBean> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.list_boy, null);
            ViewHolder viewHolder = new ViewHolder(inflate);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.title.setText(list.get(position).getTitle());
            holder.content.setText(list.get(position).getShortIntro());
            Log.d("ggggggg", "onBindViewHolder: "+"======"+list.get(position).getAuthor());
            holder.author.setText(list.get(position).getAuthor());
            Glide.with(context).load("http://statics.zhuishushenqi.com"+list.get(position).getCover()).into(holder.list_img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClcik.onClcik(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView list_img;
            private TextView title;
            private TextView content;
            private TextView author;

            public ViewHolder(View itemView) {
                super(itemView);
                list_img = (ImageView) itemView.findViewById(R.id.list_img);
                title = (TextView) itemView.findViewById(R.id.title);
                content = (TextView) itemView.findViewById(R.id.content);
                author = (TextView) itemView.findViewById(R.id.author);
            }
        }

        private OnItemClcik onItemClcik;

        public void setOnItemClcik(OnItemClcik onItemClcik) {
            this.onItemClcik = onItemClcik;
        }

        interface OnItemClcik {
            void onClcik(int position);
        }
    }
}
