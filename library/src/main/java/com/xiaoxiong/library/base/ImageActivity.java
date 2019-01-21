package com.xiaoxiong.library.base;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import com.xiaoxiong.library.bean.PhotoInfo;
import com.xiaoxiong.library.bean.PhotoInfoPosition;
import com.xiaoxiong.library.R;
import com.xiaoxiong.library.utils.animation.JAnimationUtil;
import com.xiaoxiong.library.utils.animation.JBitmapUtils;
import com.xiaoxiong.library.utils.animation.JStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class ImageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, ImageAdapter.PhotoCallback {

    ViewPager viewPager;
    RelativeLayout mRlRoot;

    private boolean bFirstResume = true;
    private ArrayList<String> datas = new ArrayList();
    private int mCurrentIndex;
    private ImageAdapter mAdapter;
    private ArrayList<PhotoInfoPosition> mInfos; // 各个图片位置
    public static final String PARAMS_IMGS = "imgs";
    public static final String PARAMS_INDEX = "index";
    public static final String PARAMS_IMGS_INFO = "imgs_info";

    public static void open(Context context, ArrayList<String> imgs, int position, List<Rect> rects){
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra(PARAMS_IMGS, imgs);
        intent.putExtra(PARAMS_INDEX, position);
        ArrayList<PhotoInfoPosition> infos = new ArrayList<>();
        for (int i = 0; i < rects.size(); i++) {
            PhotoInfoPosition photosInfos = new PhotoInfoPosition();
            infos.add(photosInfos.build(rects.get(i)));
        }
        intent.putExtra(PARAMS_IMGS_INFO, infos);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(0, 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        setContentView(R.layout.activity_image);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        mRlRoot = (RelativeLayout) findViewById(R.id.rl_root);


        initData();
        initView();


    }

    public void initData() {
        Intent intent = getIntent();
        if(intent == null){
            finish();
            return;
        }
        datas = intent.getStringArrayListExtra(PARAMS_IMGS);
        mCurrentIndex = intent.getIntExtra(PARAMS_INDEX, 0);
        mInfos = (ArrayList<PhotoInfoPosition>) intent.getSerializableExtra(PARAMS_IMGS_INFO);
        ArrayList<PhotoInfo> mPhotoList = new ArrayList<>();
        if(datas != null){
            for (int i = 0; i < datas.size(); i++) {
                PhotoInfo info = new PhotoInfo();
                info.setPhotoPath(datas.get(i));
                mPhotoList.add(info);
            }
        }
        mAdapter = new ImageAdapter(this, mPhotoList ,this);
    }

    public void initView() {
        if(datas == null || datas.size() == 0){
            finish();
            return;
        }

        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(mCurrentIndex);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(bFirstResume){
            startImgAnim();
            bFirstResume = false;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void startImgAnim(){
        JAnimationUtil.startEnterViewScaleAnim(this,viewPager, JBitmapUtils.getCurrentPicOriginalScale(this, mInfos.get(mCurrentIndex)), mInfos.get(mCurrentIndex), new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                mIvTest.setVisibility(View.GONE);
//                mViewPager.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        JAnimationUtil.startEnterViewAlphaAnim(mRlRoot, JBitmapUtils.getCurrentPicOriginalScale(this, mInfos.get(mCurrentIndex)));
    }

    public void startExitAnim(){
        mRlRoot.setBackgroundColor(Color.parseColor(JStringUtils.getBlackAlphaBg(0)));
        JAnimationUtil.startExitViewScaleAnim(this,viewPager, JBitmapUtils.getCurrentPicOriginalScale(this, mInfos.get(mCurrentIndex)), mInfos.get(mCurrentIndex), new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onPhotoClick() {
        startExitAnim();
    }
}
