package com.example.newbiechen.ireader.ui.fragment;


import android.content.Context;
import android.content.Intent;
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
import com.example.newbiechen.ireader.ui.activity.BookSortListActivity;
import com.example.newbiechen.ireader.ui.base.BaseMVPFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SortGirlsFragment extends BaseMVPFragment<BookSortContract.Presenter> implements BookSortContract.View{


    @BindView(R.id.sort_list)
    RecyclerView sortList;

    private ArrayList<BookSubSortBean> list = new ArrayList<>();
    private ArrayList<RecommenBeanCkass.FemaleBean> femaleBeans=new ArrayList<>();
    private BookShelfAdapter adapter;

   /* @Override
    public void finishRefresh(List<RecommenBeanCkass.MaleBean> maleBeans) {
        if (maleBeans != null) {
            list.addAll(maleBeans);
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
    }*/

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        adapter = new BookShelfAdapter(getContext(), list,femaleBeans);
        sortList.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        sortList.setLayoutManager(manager);
    }

    @Override
    protected void initClick() {
        super.initClick();
        adapter.setOnItemClcik(new BookShelfAdapter.OnItemClcik() {
            @Override
            public void onClick(int position) {
                BookSubSortBean bookSubSortBean = list.get(position);
                //上传
                BookSortListActivity.startActivity(getContext(),"female",bookSubSortBean);
            }
        });
    }

   /* @Override
    public void finishUpdate() {

    }

    @Override
    public void showErrorTip(String error) {

    }*/

    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.recommenBeanCkass();
        mPresenter.refreshSortBean();
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
        return R.layout.fragment_sort_girls;
    }

    @Override
    public void finishRefresh(BookSortPackage sortPackage, BookSubSortPackage subSortPackage) {
        if (subSortPackage!=null){
            list.addAll(subSortPackage.getFemale());
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void finish(List<RecommenBeanCkass.MaleBean> list) {

    }

    @Override
    public void load(List<RecommenBeanCkass.FemaleBean> beans) {
        if (list!=null){
            femaleBeans.addAll(beans);
            adapter.setBeanCkasses(femaleBeans);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showErrorTip(String s) {

    }

    static class BookShelfAdapter extends RecyclerView.Adapter<BookShelfAdapter.ViewHolder> {

        private Context context;
        private ArrayList<BookSubSortBean> list;
        private ArrayList<RecommenBeanCkass.FemaleBean> beanCkasses;

        public BookShelfAdapter(Context context, ArrayList<BookSubSortBean> list, ArrayList<RecommenBeanCkass.FemaleBean> beanCkasses) {
            this.context = context;
            this.list = list;
            this.beanCkasses = beanCkasses;
        }

        public void setList(ArrayList<BookSubSortBean> list) {
            this.list = list;
        }

        public void setBeanCkasses(ArrayList<RecommenBeanCkass.FemaleBean> beanCkasses) {
            this.beanCkasses = beanCkasses;
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
            if (beanCkasses.size()>0){
                holder.book_subname.setText(beanCkasses.get(position).getBookCount()+"本");
                Glide.with(context).load("http://statics.zhuishushenqi.com" + beanCkasses.get(position).getBookCover().get(0)).into(holder.book_img);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClcik.onClick(position);
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

        private OnItemClcik onItemClcik;

        public void setOnItemClcik(OnItemClcik onItemClcik) {
            this.onItemClcik = onItemClcik;
        }

        interface OnItemClcik{
            void onClick(int position);
        }
    }
}
