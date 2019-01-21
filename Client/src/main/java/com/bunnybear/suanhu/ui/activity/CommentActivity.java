package com.bunnybear.suanhu.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.ClassAPI;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.ClassDetailIntroduce;
import com.bunnybear.suanhu.bean.ImageBean;
import com.bunnybear.suanhu.bean.Star;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.EmptyViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CommentActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_star_count)
    TextView tvStarCount;
    @BindView(R.id.et_content)
    EditText etContent;

    public static void open(AppActivity activity,int id,String tableName) {
        Bundle bundle = new Bundle();
        bundle.putString("tableName",tableName);
        bundle.putInt("object_id",id);
        activity.startActivity(bundle, CommentActivity.class);
    }
    StarsAdapter adapter;
    List<Star> starList = new ArrayList<>();
    @Override
    protected int initLayout() {
        return R.layout.activity_comment;
    }

    @Override
    protected String setTitleStr() {
        return "评价";
    }

    @Override
    protected void init() {
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        starList.add(new Star(true));
        starList.add(new Star(false));
        starList.add(new Star(false));
        starList.add(new Star(false));
        starList.add(new Star(false));
        adapter = new StarsAdapter(starList);
        adapter.setEmptyView(EmptyViewUtil.getEmptyView(mActivity, R.mipmap.no_data, "暂无数据"));
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }


    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        if (antiShake.check(view.getId()))return;
        switch (view.getId()){
            case R.id.btn_submit:
                submitComment();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        for (int i = 0; i < starList.size(); i++) {
            starList.get(i).setLight(false);
        }
        for (int i = 0; i < starList.size(); i++) {
            if (i<=position){
                starList.get(i).setLight(true);
            }
        }
        int count = position + 1;
        tvStarCount.setText(count+"分");
        adapter.notifyDataSetChanged();
    }


    public class StarsAdapter extends BaseQuickAdapter<Star,BaseViewHolder> {

        public StarsAdapter(@Nullable List<Star> data) {
            super(R.layout.item_star, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Star item) {
            helper.setImageResource(R.id.iv,item.isLight() ? R.mipmap.big_yellow_star : R.mipmap.big_gray_star);
        }
    }


    private void submitComment(){
        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            showMessage("评论内容不能为空");
            return;
        }
        showProgressDialog("提交中...");
        Http.http.createApi(MineAPI.class)
                .comment(content,getIntent().getIntExtra("object_id",-1),getIntent().getStringExtra("tableName"),getStar()*2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        hideProgressDialog();
                        showMessage("评论成功");
                        finish();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        hideProgressDialog();
                        showMessage(errStr);
                    }
                }));
    }

    private int getStar(){
        for (int i = 0; i < starList.size(); i++) {
            if (!starList.get(i).isLight()){
                return i;
            }
        }
        return 5;
    }
}
