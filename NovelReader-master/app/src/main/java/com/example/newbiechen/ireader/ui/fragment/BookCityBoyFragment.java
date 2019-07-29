package com.example.newbiechen.ireader.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.fragment.BookListFragment;
import com.example.newbiechen.ireader.fragment.BookSortFragment;
import com.example.newbiechen.ireader.model.bean.BillBookBean;
import com.example.newbiechen.ireader.model.bean.BookListBean;
import com.example.newbiechen.ireader.model.bean.BookListBean1;
import com.example.newbiechen.ireader.presenter.BillBookPresenter;
import com.example.newbiechen.ireader.presenter.contract.BillBookContract;
import com.example.newbiechen.ireader.ui.activity.BillBookActivity;
import com.example.newbiechen.ireader.ui.activity.BookDetailActivity;
import com.example.newbiechen.ireader.ui.activity.BookListActivity;
import com.example.newbiechen.ireader.ui.activity.BottomActivity;
import com.example.newbiechen.ireader.ui.activity.SearchActivity;
import com.example.newbiechen.ireader.ui.base.BaseMVPFragment;
import com.example.newbiechen.ireader.utils.SharedPreUtils;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookCityBoyFragment extends BaseMVPFragment<BillBookContract.Presenter> implements BillBookContract.View {


    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.hot)
    TextView hot;
    @BindView(R.id.change1)
    TextView change1;
    @BindView(R.id.hot_list)
    RecyclerView hotList;
    @BindView(R.id.bangdan)
    TextView bangdan;
    @BindView(R.id.change2)
    TextView change2;
    @BindView(R.id.bangdan_list)
    RecyclerView bangdanList;
    @BindView(R.id.good)
    TextView good;
    @BindView(R.id.change)
    TextView change;
    @BindView(R.id.good_list)
    RecyclerView goodList;
    Unbinder unbinder;
    private ArrayList<BookListBean1.MaleBean> list = new ArrayList<>();
    private ArrayList<BillBookBean> hotBeans = new ArrayList<>();
    private ArrayList<BillBookBean> billBeans = new ArrayList<>();
    private ArrayList<BillBookBean> goodBeans = new ArrayList<>();
    private HotAdapter hotAdapter;
    private BillAdapter billAdapter;
    private GoodAdapter goodAdapter;
    private String id;
    private String billId;
    private String goodId;

    @Override
    public void finishRefresh(List<BillBookBean> beans) {
        if (beans != null) {
            hotBeans.add(beans.get(1));
            hotAdapter.setList(hotBeans);
            hotAdapter.notifyDataSetChanged();
            billBeans.add(beans.get(2));
            billAdapter.setList(billBeans);
            billAdapter.notifyDataSetChanged();
            goodBeans.add(beans.get(3));
            goodAdapter.setList(goodBeans);
            goodAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setTabLayout();
        setUpAdapter();
        setBanner();
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.RecommandList();
        mPresenter.refreshBookBrief(id);
    }

    @Override
    protected void initClick() {
        super.initClick();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        hotAdapter.setOnItemClcik(new HotAdapter.OnItemClcik() {
            @Override
            public void onClcik(int position, View view) {
                String id = hotBeans.get(position).get_id();
                BookDetailActivity.startActivity(getContext(),id);
            }
        });

        hotAdapter.setOnclcik(new HotAdapter.Onclcik() {
            @Override
            public void onClick(int position, View view) {
                TextView add_shelf = view.findViewById(R.id.add_shelf);
                //add_shelf.setText("开始阅读");
                //if (add_shelf.getText().equals("开始阅读")){
                    String id = hotBeans.get(position).get_id();
                    BookDetailActivity.startActivity(getContext(),id);
                //}
            }
        });

        billAdapter.setOnItemClcik(new BillAdapter.OnItemClcik() {
            @Override
            public void onClcik(int position) {
                String id = billBeans.get(position).get_id();
                BookDetailActivity.startActivity(getContext(),id);
            }
        });

        billAdapter.setOnclcik(new BillAdapter.Onclcik() {
            @Override
            public void onClick(int position, View view) {
                TextView add_shelf = view.findViewById(R.id.add_shelf);
                ///add_shelf.setText("开始阅读");
                //if (add_shelf.getText().equals("开始阅读")){
                    String id = billBeans.get(position).get_id();
                    BookDetailActivity.startActivity(getContext(),id);
                //}
            }
        });

        goodAdapter.setOnItemClcik(new GoodAdapter.OnItemClcik() {
            @Override
            public void onClcik(int position) {
                String id = goodBeans.get(position).get_id();
                BookDetailActivity.startActivity(getContext(),id);
            }
        });

        goodAdapter.setOnclcik(new GoodAdapter.Onclcik() {
            @Override
            public void onClick(int position, View view) {
                TextView add_shelf = view.findViewById(R.id.add_shelf);
                //add_shelf.setText("开始阅读");
                //if (add_shelf.getText().equals("开始阅读")){
                    String id = billBeans.get(position).get_id();
                    BookDetailActivity.startActivity(getContext(),id);
                //}
            }
        });
    }

    private void setTabLayout() {

        tab.addTab(tab.newTab().setIcon(R.mipmap.bangdan).setText("榜单"));
        tab.addTab(tab.newTab().setIcon(R.mipmap.fenlei).setText("分类"));
        tab.addTab(tab.newTab().setIcon(R.mipmap.wanben).setText("完本"));
        tab.addTab(tab.newTab().setIcon(R.mipmap.zhuti).setText("书单"));

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                a(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                a(tab);
            }
        });
    }

    private void setBanner() {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.banner3);
        list.add(R.drawable.banner1);
        list.add(R.drawable.banner2);
        banner.setImages(list);
        banner.setImageLoader( new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        }).start();
    }

    private void setUpAdapter() {
        hotAdapter = new HotAdapter(getContext(), hotBeans);
        hotList.setAdapter(hotAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        hotList.setLayoutManager(manager);

        billAdapter = new BillAdapter(getContext(), billBeans);
        bangdanList.setAdapter(billAdapter);
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        bangdanList.setLayoutManager(manager1);

        goodAdapter = new GoodAdapter(getContext(), goodBeans);
        goodList.setAdapter(goodAdapter);
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext());
        goodList.setLayoutManager(manager2);
    }


    private void a(TabLayout.Tab tab) {
        FragmentActivity activity = getActivity();

        if (!(activity instanceof BottomActivity)) {
            return;
        }

        BottomActivity bottomActivity = (BottomActivity) activity;

        int position = tab.getPosition();
        switch (position) {
            case 0:
                bottomActivity.registerFragment(new BookListFragment());
                break;
            case 1:
                bottomActivity.registerFragment(new BookSortFragment());
                break;
            case 2:
                BillBookActivity.startActivity(getContext(),"54d42d92321052167dfb75e3"
                ,"564d820bc319238a644fb408","564d8494fe996c25652644d2");
                break;
            case 3:
                Intent intent1 = new Intent(getContext(), BookListActivity.class);
                startActivity(intent1);
                break;
            default:
                break;

        }
    }

    @Override
    public void finishLoading(BookListBean1 beans) {
        if (beans != null && beans.getMale() != null) {
            list.addAll(beans.getMale());
            if (hot.getText().equals("本周最热")) {
                id = list.get(0).get_id();
                mPresenter.refreshBookBrief(id);
            }
            if (bangdan.getText().equals("实时榜单")) {
                billId = list.get(1).get_id();
                mPresenter.refreshBookBrief(billId);
            }
            if (good.getText().equals("口碑佳作")) {
                goodId = list.get(2).get_id();
                mPresenter.refreshBookBrief(goodId);
            }
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

    @Override
    protected int getContentId() {
        return R.layout.fragment_book_city_boy;
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


    static class HotAdapter extends RecyclerView.Adapter<HotAdapter.ViewHolder> {

        private Context context;
        private ArrayList<BillBookBean> list;

        public HotAdapter(Context context, ArrayList<BillBookBean> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(ArrayList<BillBookBean> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.layout_shucheng, null);
            ViewHolder viewHolder = new ViewHolder(inflate);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.book_name.setText(list.get(position).getTitle());
            holder.book_content.setText(list.get(position).getShortIntro());
            holder.book_author.setText(list.get(position).getAuthor());
            Glide.with(context).load("http://statics.zhuishushenqi.com" + list.get(position).getCover()).into(holder.book_img);

            holder.book_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClcik.onClcik(position, v);
                }
            });

            holder.book_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClcik.onClcik(position, v);
                }
            });

            holder.book_author.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClcik.onClcik(position, v);
                }
            });

            holder.book_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClcik.onClcik(position, v);
                }
            });

            holder.add_shelf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclcik.onClick(position, v);
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
            private TextView book_content;
            private TextView book_author;
            private TextView add_shelf;

            public ViewHolder(View itemView) {
                super(itemView);
                book_img = (ImageView) itemView.findViewById(R.id.book_img);
                book_name = (TextView) itemView.findViewById(R.id.book_name);
                book_content = (TextView) itemView.findViewById(R.id.book_content);
                book_author = (TextView) itemView.findViewById(R.id.book_author);
                add_shelf = (TextView) itemView.findViewById(R.id.add_shelf);
            }
        }

        private OnItemClcik onItemClcik;

        public void setOnItemClcik(OnItemClcik onItemClcik) {
            this.onItemClcik = onItemClcik;
        }

        interface OnItemClcik {
            void onClcik(int position, View view);
        }

        private Onclcik onclcik;

        public void setOnclcik(Onclcik onclcik) {
            this.onclcik = onclcik;
        }

        interface Onclcik {
            void onClick(int position, View view);
        }
    }

    static class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

        private Context context;
        private ArrayList<BillBookBean> list;

        public BillAdapter(Context context, ArrayList<BillBookBean> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(ArrayList<BillBookBean> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.layout_shucheng, null);
            ViewHolder viewHolder = new ViewHolder(inflate);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.book_name.setText(list.get(position).getTitle());
            holder.book_content.setText(list.get(position).getShortIntro());
            holder.book_author.setText(list.get(position).getAuthor());
            Glide.with(context).load("http://statics.zhuishushenqi.com" + list.get(position).getCover()).into(holder.book_img);

            holder.book_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClcik.onClcik(position);
                }
            });

            if (holder.book_content.getText()!=null){
                holder.book_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClcik.onClcik(position);
                    }
                });
            }



            holder.book_author.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClcik.onClcik(position);
                }
            });

            holder.book_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClcik.onClcik(position);
                }
            });

            holder.add_shelf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclcik.onClick(position, v);
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
            private TextView book_content;
            private TextView book_author;
            private TextView add_shelf;

            public ViewHolder(View itemView) {
                super(itemView);
                book_img = (ImageView) itemView.findViewById(R.id.book_img);
                book_name = (TextView) itemView.findViewById(R.id.book_name);
                book_content = (TextView) itemView.findViewById(R.id.book_content);
                book_author = (TextView) itemView.findViewById(R.id.book_author);
                add_shelf = (TextView) itemView.findViewById(R.id.add_shelf);
            }
        }

        private OnItemClcik onItemClcik;

        public void setOnItemClcik(OnItemClcik onItemClcik) {
            this.onItemClcik = onItemClcik;
        }

        interface OnItemClcik {
            void onClcik(int position);
        }

        private Onclcik onclcik;

        public void setOnclcik(Onclcik onclcik) {
            this.onclcik = onclcik;
        }

        interface Onclcik {
            void onClick(int position, View view);
        }
    }

    static class GoodAdapter extends RecyclerView.Adapter<GoodAdapter.ViewHolder> {

        private Context context;
        private ArrayList<BillBookBean> list;

        public GoodAdapter(Context context, ArrayList<BillBookBean> list) {
            this.context = context;
            this.list = list;
        }

        public void setList(ArrayList<BillBookBean> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.layout_shucheng, null);
            ViewHolder viewHolder = new ViewHolder(inflate);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.book_name.setText(list.get(position).getTitle());
            holder.book_content.setText(list.get(position).getShortIntro());
            holder.book_author.setText(list.get(position).getAuthor());
            Glide.with(context).load("http://statics.zhuishushenqi.com" + list.get(position).getCover()).into(holder.book_img);

            holder.book_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClcik.onClcik(position);
                }
            });

            holder.add_shelf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclcik.onClick(position, v);
                }
            });

            holder.book_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClcik.onClcik(position);
                }
            });

            holder.book_author.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClcik.onClcik(position);
                }
            });

            holder.book_img.setOnClickListener(new View.OnClickListener() {
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
            private ImageView book_img;
            private TextView book_name;
            private TextView book_content;
            private TextView book_author;
            private TextView add_shelf;

            public ViewHolder(View itemView) {
                super(itemView);
                book_img = (ImageView) itemView.findViewById(R.id.book_img);
                book_name = (TextView) itemView.findViewById(R.id.book_name);
                book_content = (TextView) itemView.findViewById(R.id.book_content);
                book_author = (TextView) itemView.findViewById(R.id.book_author);
                add_shelf = (TextView) itemView.findViewById(R.id.add_shelf);
            }
        }

        private OnItemClcik onItemClcik;

        public void setOnItemClcik(OnItemClcik onItemClcik) {
            this.onItemClcik = onItemClcik;
        }

        interface OnItemClcik {
            void onClcik(int position);
        }

        private Onclcik onclcik;

        public void setOnclcik(Onclcik onclcik) {
            this.onclcik = onclcik;
        }

        interface Onclcik {
            void onClick(int position, View view);
        }
    }
}
