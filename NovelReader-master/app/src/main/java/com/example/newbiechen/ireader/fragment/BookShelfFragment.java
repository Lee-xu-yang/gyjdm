package com.example.newbiechen.ireader.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.BillBookBean;
import com.example.newbiechen.ireader.model.bean.BookListBean1;
import com.example.newbiechen.ireader.presenter.BillBookPresenter;
import com.example.newbiechen.ireader.presenter.contract.BillBookContract;
import com.example.newbiechen.ireader.ui.activity.BookDetailActivity;
import com.example.newbiechen.ireader.ui.activity.BottomActivity;
import com.example.newbiechen.ireader.ui.activity.SearchActivity;
import com.example.newbiechen.ireader.ui.base.BaseMVPFragment;
import com.example.newbiechen.ireader.utils.StatusBarManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookShelfFragment extends BaseMVPFragment<BillBookContract.Presenter> implements BillBookContract.View {


    @BindView(R.id.book_shelf_list)
    RecyclerView bookShelfList;
    Unbinder unbinder;
    @BindView(R.id.shelf_search)
    ImageView shelfSearch;
    private BookShelfAdapter adapter;
    private ArrayList<BillBookBean> list = new ArrayList<>();
    private ArrayList<BookListBean1.MaleBean> books = new ArrayList<>();
    private String id;

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapeter();
        int color = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        StatusBarManager.setStatusBarColor(getActivity(), color);
    }

    private void setUpAdapeter() {
        adapter = new BookShelfAdapter(getContext(), list);
        bookShelfList.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        bookShelfList.setLayoutManager(manager);

        adapter.setOnClcik(new BookShelfAdapter.OnClcik() {
            @Override
            public void onClcik(int position) {
                FragmentActivity activity = getActivity();
                if (!(activity instanceof BottomActivity)) {
                    return;
                }
                BottomActivity bottomActivity = (BottomActivity) activity;
                bottomActivity.registerFragment(new BookCityFragment());
            }
        });
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_book_shelf;
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

    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.RecommandList();
    }

    @Override
    protected void initClick() {
        super.initClick();
        adapter.setOnItemClcik(new BookShelfAdapter.OnItemClcik() {
            @Override
            public void onClcik(int position) {
                String bookId = list.get(position).get_id();
                BookDetailActivity.startActivity(getContext(), bookId);
            }
        });

        shelfSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void finishRefresh(List<BillBookBean> beans) {
        if (beans != null) {
            for (int i = 0; i < 9; i++) {
                list.add(beans.get(i));
            }
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void finishLoading(BookListBean1 beans) {
        if (beans != null && beans.getMale() != null) {
            books.addAll(beans.getMale());
            id = books.get(0).get_id();
            mPresenter.refreshBookBrief(id);
        }
    }

    @Override
    public void showLoadError() {

    }

    @Override
    protected BillBookContract.Presenter bindPresenter() {
        return new BillBookPresenter();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    static class BookShelfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private ArrayList<BillBookBean> list;

        public BookShelfAdapter(Context context, ArrayList<BillBookBean> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(ArrayList<BillBookBean> list) {
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            if (viewType == 0) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.bookshelf, null);
                viewHolder = new InfoViewHolder(inflate);
            } else if (viewType == 1) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.bookshelf_add, null);
                viewHolder = new AddViewHolder(inflate);
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof InfoViewHolder) {
                InfoViewHolder infoViewHolder = (InfoViewHolder) holder;
                infoViewHolder.book_name.setText(list.get(position).getTitle());
                infoViewHolder.book_subname.setText(list.get(position).getAuthor());
                Glide.with(context).load("http://statics.zhuishushenqi.com" + list.get(position).getCover()).into(infoViewHolder.book_img);
                infoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClcik.onClcik(position);
                    }
                });

            } else if (holder instanceof AddViewHolder) {
                AddViewHolder addViewHolder = (AddViewHolder) holder;
                addViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClcik.onClcik(position);
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 8) {
                return 1;
            } else {
                return 0;
            }
        }

        public class InfoViewHolder extends RecyclerView.ViewHolder {
            private ImageView book_img;
            private TextView book_name;
            private TextView book_subname;

            public InfoViewHolder(View itemView) {
                super(itemView);
                book_img = (ImageView) itemView.findViewById(R.id.book_img);
                book_name = (TextView) itemView.findViewById(R.id.book_name);
                book_subname = (TextView) itemView.findViewById(R.id.book_subname);
            }
        }

        public class AddViewHolder extends RecyclerView.ViewHolder {
            private ImageView book_img;
            private LinearLayout add;

            public AddViewHolder(View itemView) {
                super(itemView);
                book_img = (ImageView) itemView.findViewById(R.id.book_img);
                add = (LinearLayout) itemView.findViewById(R.id.add);
            }
        }

        private OnItemClcik onItemClcik;

        public void setOnItemClcik(OnItemClcik onItemClcik) {
            this.onItemClcik = onItemClcik;
        }

        interface OnItemClcik {
            void onClcik(int position);
        }

        private OnClcik onClcik;

        public void setOnClcik(OnClcik onClcik) {
            this.onClcik = onClcik;
        }

        interface OnClcik {
            void onClcik(int position);
        }
    }
}