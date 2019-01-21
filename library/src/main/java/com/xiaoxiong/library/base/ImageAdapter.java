package com.xiaoxiong.library.base;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoxiong.library.R;
import com.xiaoxiong.library.bean.PhotoInfo;
import com.xiaoxiong.library.utils.glide.GlideUtil;
import com.xiaoxiong.library.view.PinchImageView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class ImageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<PhotoInfo> mDatas;
    private LayoutInflater layoutInflater;
    private View mCurrentView;
    private PhotoCallback mCallback;

    public ImageAdapter(Activity activity, ArrayList<PhotoInfo> datas,PhotoCallback mCallback) {
        this.activity = activity;
        mDatas = datas;
        this.mCallback = mCallback;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentView = (View) object;
    }

    public View getPrimaryItem() {
        return mCurrentView;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_image, container, false);
        PinchImageView imageView = (PinchImageView) view.findViewById(R.id.img);
        GlideUtil.load(mDatas.get(position).getPhotoPath(),imageView);
        container.addView(view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onPhotoClick();
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public interface PhotoCallback{
        void onPhotoClick();
    }

}
