package com.example.newbiechen.ireader.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BookSubSortBean;
import com.example.newbiechen.ireader.model.bean.RecommenBeanCkass;
import com.example.newbiechen.ireader.model.bean.packages.BookSortPackage;
import com.example.newbiechen.ireader.model.bean.packages.BookSubSortPackage;
import com.example.newbiechen.ireader.presenter.BookShelfPresenter;
import com.example.newbiechen.ireader.presenter.BookSortPresenter;
import com.example.newbiechen.ireader.presenter.contract.BookShelfContract;
import com.example.newbiechen.ireader.presenter.contract.BookSortContract;
import com.example.newbiechen.ireader.ui.activity.BookSortActivity;
import com.example.newbiechen.ireader.ui.activity.BookSortListActivity;
import com.example.newbiechen.ireader.ui.base.BaseMVPFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SortBoyFragment extends BaseMVPFragment<BookSortContract.Presenter> implements BookSortContract.View{

    @BindView(R.id.sort_list)
    RecyclerView sortList;

    private ArrayList<BookSubSortBean> list = new ArrayList<>();
    private BookShelfAdapter adapter;
    private ArrayList<RecommenBeanCkass.MaleBean> maleBeans=new ArrayList<>();

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        adapter = new BookShelfAdapter(getContext(), list,maleBeans);
        sortList.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        sortList.setLayoutManager(manager);
    }

    @Override
    protected void initClick() {
        super.initClick();
        adapter.setOnItemClick(new BookShelfAdapter.OnItemClick() {
            @Override
            public void onClcik(int position) {
                BookSubSortBean bookSubSortBean = list.get(position);
                //上传
                BookSortListActivity.startActivity(getContext(),"male",bookSubSortBean);
            }
        });
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.refreshSortBean();
        mPresenter.recommenBeanCkass();
    }

    @Override
    protected BookSortContract.Presenter bindPresenter() {
        return new BookSortPresenter();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_sort_boy;
    }

    @Override
    public void finishRefresh(BookSortPackage sortPackage, BookSubSortPackage subSortPackage) {
        if (subSortPackage!=null){
            list.addAll(subSortPackage.getMale());
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void finish(List<RecommenBeanCkass.MaleBean> list) {
        if (list!=null){
            maleBeans.addAll(list);
            adapter.setMaleBeans(maleBeans);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void load(List<RecommenBeanCkass.FemaleBean> beans) {

    }

    @Override
    public void showErrorTip(String s) {

    }

    static class BookShelfAdapter extends RecyclerView.Adapter<BookShelfAdapter.ViewHolder> {

        private Context context;
        private ArrayList<BookSubSortBean> list;
        ArrayList<RecommenBeanCkass.MaleBean> maleBeans;

        public BookShelfAdapter(Context context, ArrayList<BookSubSortBean> list, ArrayList<RecommenBeanCkass.MaleBean> maleBeans) {
            this.context = context;
            this.list = list;
            this.maleBeans = maleBeans;
        }

        public void setList(ArrayList<BookSubSortBean> list) {
            this.list = list;
        }

        public void setMaleBeans(ArrayList<RecommenBeanCkass.MaleBean> maleBeans) {
            this.maleBeans = maleBeans;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.sort_boy, null);
            ViewHolder viewHolder = new ViewHolder(inflate);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.book_name.setText(list.get(position).getMajor());
            if (maleBeans.size()>0){
                holder.book_subname.setText(maleBeans.get(position).getBookCount()+"本");
                if (maleBeans.get(position).getBookCover().get(0)!=null){
                    Glide.with(context).load("http://statics.zhuishushenqi.com" + maleBeans.get(position).getBookCover().get(0)).into(holder.book_img);
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onClcik(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView book_img;
            private TextView book_name;
            private TextView book_subname;

            public ViewHolder(View itemView) {
                super(itemView);
                book_img = (ImageView) itemView.findViewById(R.id.book_img);
                book_name = (TextView) itemView.findViewById(R.id.book_name);
                book_subname = (TextView) itemView.findViewById(R.id.book_subname);
            }
        }

        private OnItemClick onItemClick;

        public void setOnItemClick(OnItemClick onItemClick) {
            this.onItemClick = onItemClick;
        }

        interface OnItemClick{
            void onClcik(int position);
        }
    }
}
