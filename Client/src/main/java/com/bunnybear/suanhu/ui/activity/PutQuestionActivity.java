//package com.bunnybear.suanhu.ui.activity;
//
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.EditText;
//
//import com.bunnybear.suanhu.R;
//import com.bunnybear.suanhu.api.CalcAPI;
//import com.bunnybear.suanhu.base.AppActivity;
//import com.bunnybear.suanhu.bean.Question;
//import com.bunnybear.suanhu.net.AppSubscriber;
//import com.bunnybear.suanhu.net.Http;
//import com.bunnybear.suanhu.ui.adapter.PutQuestionAdapter;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.xiaoxiong.library.http.RequestCallBack;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
//public class PutQuestionActivity extends AppActivity implements BaseQuickAdapter.OnItemClickListener, TextWatcher {
//
//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
//    @BindView(R.id.et)
//    EditText et;
//
//    public static void open(AppActivity activity, int type) {
//        Bundle bundle = new Bundle();
//        bundle.putInt("type", type);
//        activity.startActivity(bundle, PutQuestionActivity.class);
//    }
//
//    PutQuestionAdapter adapter;
//    List<Question> questionList = new ArrayList<>();
//
//    int type;
//    String question = "";
//
//    @Override
//    protected int initLayout() {
//        return R.layout.activity_put_question;
//    }
//
//    @Override
//    protected String setTitleStr() {
//        return "提问问题";
//    }
//
//    @Override
//    protected void init() {
//        type = getIntent().getIntExtra("type", -1);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new PutQuestionAdapter(questionList);
//        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(this);
//
//        getData();
//
//        et.addTextChangedListener(this);
//    }
//
//    @OnClick({R.id.tv_change, R.id.btn_submit})
//    public void onViewClicked(View view) {
//        if (antiShake.check(view.getId())) return;
//        switch (view.getId()) {
//            case R.id.tv_change:
//                changeQuestions();
//                break;
//            case R.id.btn_submit:
//                if (adapter.checkPosition == -1){
//                    question = et.getText().toString();
//                }
//                if (TextUtils.isEmpty(question)) {
//                    showMessage("问题不能为空");
//                    return;
//                }
//                MasterRecommendActivity.open(this, type, question);
//                break;
//        }
//    }
//
//    @Override
//    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
//        if (adapter.checkPosition != position){
//            adapter.setCheckPosition(position);
//            question = questionList.get(position).getContent();
//            et.setText("");
//        }else {
//            adapter.setCheckPosition(-1);
//            question = "";
//        }
//    }
//
//    private void getData() {
//        Http.http.createApi(CalcAPI.class)
//                .getQuestions(type)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(new AppSubscriber(new RequestCallBack<List<Question>>() {
//                    @Override
//                    public void success(List<Question> result) {
//                        questionList.clear();
//                        questionList.addAll(result);
//                        adapter.setNewData(questionList);
//                    }
//
//                    @Override
//                    public void fail(int errCode, String errStr) {
//                        showMessage(errStr);
//                    }
//                }));
//    }
//
//    private void changeQuestions() {
//        Http.http.createApi(CalcAPI.class)
//                .getOtherQuestions(type)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(new AppSubscriber(new RequestCallBack<List<Question>>() {
//                    @Override
//                    public void success(List<Question> result) {
//                        question = "";
//                        adapter.setCheckPosition(-1);
//                        questionList.clear();
//                        questionList.addAll(result);
//                        adapter.setNewData(questionList);
//                    }
//
//                    @Override
//                    public void fail(int errCode, String errStr) {
//                        showMessage(errStr);
//                    }
//                }));
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        if (s.toString().length() > 0){
//            adapter.setCheckPosition(-1);
//        }
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//
//    }
//}
