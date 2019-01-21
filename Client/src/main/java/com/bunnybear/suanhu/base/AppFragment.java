package com.bunnybear.suanhu.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.xiaoxiong.library.base.BaseFragment;
import com.xiaoxiong.library.utils.AntiShake;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xiaoxiong on 2017/2/6.
 */

public abstract class AppFragment extends BaseFragment {

    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    protected boolean isLoad = false;

    public AppActivity mActivity;
    private ProgressDialog dialog;
    private Unbinder unbinder;
    public AntiShake antiShake;

    private View view;
    private boolean isNeedLazyLoad = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (AppActivity) getActivity();
        antiShake = mActivity.antiShake;
        view = inflater.inflate(initLayout(), container, false);
        unbinder = ButterKnife.bind(this, view);
        isInit = true;
        /**初始化的时候去加载数据**/
        isCanLoadData();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public boolean useEventBus() {
        return false;
    }



    public void showMessage(final Object message) {
        Toast.makeText(mActivity.getApplicationContext(), message.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
        isInit = false;
        isLoad = false;
        EventBus.getDefault().unregister(this);
    }

    protected abstract int initLayout();
    protected abstract void lazyLoad();
    protected abstract void normalLoad();
    protected abstract boolean useLazyLoad();
    protected void stopLoad() {}

    protected View getContentView() {
        return view;
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (useLazyLoad()) {
            isCanLoadData();
        }
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (useLazyLoad()) {
            if (!isInit) {
                return;
            }
            if (getUserVisibleHint()) {
                if (!isLoad) {
                    lazyLoad();
                    isLoad = true;
                }
            } else {
                if (isLoad) {
                    stopLoad();
                }
            }
        }else {
            normalLoad();
        }

    }

}
