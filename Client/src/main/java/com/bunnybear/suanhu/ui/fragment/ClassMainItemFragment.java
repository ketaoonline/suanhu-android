package com.bunnybear.suanhu.ui.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.ClassAPI;
import com.bunnybear.suanhu.api.MainAPI;
import com.bunnybear.suanhu.base.ScrollContainerFragment;
import com.bunnybear.suanhu.bean.ArticleResponse;
import com.bunnybear.suanhu.bean.SClass;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.ui.activity.ClassDetailActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.willy.ratingbar.ScaleRatingBar;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClassMainItemFragment extends ScrollContainerFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static ClassMainItemFragment newInstance(String type){
        ClassMainItemFragment fragment = new ClassMainItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }
    SClassAdapter adapter;
    List<SClass> listDatas = new ArrayList<>();
    @Override
    protected int initLayout() {
        return R.layout.layout_class_gridview;
    }

    @Override
    protected void lazyLoad() {
//        recyclerView.setFocusableInTouchMode(false);
//        recyclerView.requestFocus();
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity,2));
        adapter = new SClassAdapter(listDatas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        getData();

    }

    @Override
    protected void normalLoad() {

    }

    @Override
    protected boolean useLazyLoad() {
        return true;
    }

    @Override
    public View getScrollableView() {
        return recyclerView;
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ClassDetailActivity.open(mActivity,listDatas.get(position).getId());
    }

    public class SClassAdapter extends BaseQuickAdapter<SClass,BaseViewHolder>{

        public SClassAdapter(@Nullable List<SClass> data) {
            super(R.layout.item_class, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SClass item) {
            GlideUtil.load(item.getHead_banner(), (ImageView) helper.getView(R.id.iv));
            helper.setText(R.id.tv_title,item.getCoursename());
            helper.setText(R.id.tv_price,item.getPrice()+"");
            ScaleRatingBar scaleRatingBar = helper.getView(R.id.scaleRatingBar);
            scaleRatingBar.setRating((float) item.getStars());
            helper.setText(R.id.tv_learn_count,item.getTotal_people()+"人学过");
            helper.setText(R.id.tv_star_count,item.getStars()+"分");
            TextView tvOldPrice = helper.getView(R.id.tv_old_price);
            tvOldPrice.setText("¥"+item.getOld_price());
            tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private void getData(){
        Http.http.createApi(ClassAPI.class)
                .getMainClasses(getArguments().getString("type"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<List<SClass>>() {
                    @Override
                    public void success(List<SClass> result) {
                        listDatas.clear();
                        listDatas.addAll(result);
                        adapter.setNewData(listDatas);
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        showMessage(errStr);
                    }
                }));
    }


}
