package com.xiaoxiong.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


/**
 * Created by renlei on 2016/5/23.
 */
public abstract class BaseFragment extends Fragment {

    protected abstract boolean useEventBus();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
