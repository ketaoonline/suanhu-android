package com.bunnybear.suanhu.ui.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.api.MineAPI;
import com.bunnybear.suanhu.bean.MasterTag;
import com.bunnybear.suanhu.bean.Star;
import com.bunnybear.suanhu.net.AppSubscriber;
import com.bunnybear.suanhu.net.Http;
import com.bunnybear.suanhu.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.http.RequestCallBack;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FinishChatActivity extends AppCompatActivity {
    @BindView(R.id.iv_master_head)
    CircleImageView ivMasterHead;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.rv_tag)
    RecyclerView rvTag;
    @BindView(R.id.tv_star_des)
    TextView tvStarDes;
    @BindView(R.id.et_content)
    EditText etContent;

    public static void open(Activity activity, int masterId, String headUrl,int orderId) {
        Intent intent = new Intent(activity, FinishChatActivity.class);
        intent.putExtra("object_id", masterId);
        intent.putExtra("tableName", "master");
        intent.putExtra("headUrl", headUrl);
        intent.putExtra("orderId", orderId);
        activity.startActivityForResult(intent, 1000, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
    }

    StarsAdapter starAdapter;
    List<Star> starList = new ArrayList<>();
    TagAdapter tagAdapter;
    List<MasterTag> tagList = new ArrayList<>();

    String[] des = {"糟糕","差","一般","好","非常好"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setEnterTransition(new Slide(Gravity.BOTTOM).setDuration(300));
        getWindow().setExitTransition(new Slide(Gravity.TOP).setDuration(300));

        setContentView(R.layout.pop_finish_chat);
        ButterKnife.bind(this);

        GlideUtil.load(getIntent().getStringExtra("headUrl"),ivMasterHead);
        tvStarDes.setText(des[0]);

        LinearLayoutManager rvStarManager = new LinearLayoutManager(this);
        rvStarManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(rvStarManager);
        starList.add(new Star(true));
        starList.add(new Star(false));
        starList.add(new Star(false));
        starList.add(new Star(false));
        starList.add(new Star(false));

        starAdapter = new StarsAdapter(starList);
        rv.setAdapter(starAdapter);
        starAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < starList.size(); i++) {
                    starList.get(i).setLight(false);
                }
                for (int i = 0; i < starList.size(); i++) {
                    if (i <= position) {
                        starList.get(i).setLight(true);
                    }
                }
                tvStarDes.setText(des[position]);
                starAdapter.notifyDataSetChanged();
            }
        });

        rvTag.setLayoutManager(new GridLayoutManager(this, 3));
//        tagList.add(new MasterTag(true, "神机妙算"));
//        tagList.add(new MasterTag(false, "神机妙算"));
//        tagList.add(new MasterTag(false, "神机妙算"));
//        tagList.add(new MasterTag(false, "神机妙算"));
//        tagList.add(new MasterTag(false, "神机妙算"));
//        tagList.add(new MasterTag(false, "神机妙算"));
        tagAdapter = new TagAdapter(tagList);
        rvTag.setAdapter(tagAdapter);
        tagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                tagList.get(position).setChecked(!tagList.get(position).isChecked());
                tagAdapter.setNewData(tagList);
                tagAdapter.notifyDataSetChanged();
            }
        });

    }

    @OnClick({R.id.iv_close, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finishAfterTransition();
                break;
            case R.id.btn_submit:
                comment();
                break;
        }
    }

    public class StarsAdapter extends BaseQuickAdapter<Star, BaseViewHolder> {

        public StarsAdapter(@Nullable List<Star> data) {
            super(R.layout.item_star, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Star item) {
            helper.setImageResource(R.id.iv, item.isLight() ? R.mipmap.finish_chat_yellow_star : R.mipmap.finish_chat_gray_star);
        }
    }

    public class TagAdapter extends BaseQuickAdapter<MasterTag, BaseViewHolder> {

        public TagAdapter(@Nullable List<MasterTag> data) {
            super(R.layout.item_finish_chat_tag, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, MasterTag item) {
            helper.setBackgroundRes(R.id.tv, item.isChecked() ? R.drawable.shape_tag_checked : R.drawable.shape_tag_uncheck);
            helper.setTextColor(R.id.tv, item.isChecked() ? Color.parseColor("#A096DE") : Color.parseColor("#666666"));
        }
    }

    private void comment() {
        String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show(this, "评价内容不能为空");
            return;
        }
        Http.http.createApi(MineAPI.class)
                .comment(content, getIntent().getIntExtra("object_id", -1), getIntent().getStringExtra("tableName"), getStar() * 2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        finishChat();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        ToastUtil.show(FinishChatActivity.this,errStr);
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

    private void finishChat(){
        Http.http.createApi(MineAPI.class)
                .finishChat(getIntent().getIntExtra("orderId",-1))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new AppSubscriber(new RequestCallBack<String>() {
                    @Override
                    public void success(String result) {
                        ToastUtil.show(FinishChatActivity.this,"详测结束,评论成功");
                        setResult(RESULT_OK);
                        finishAfterTransition();
                    }

                    @Override
                    public void fail(int errCode, String errStr) {
                        ToastUtil.show(FinishChatActivity.this,errStr);
                    }
                }));
    }

}
