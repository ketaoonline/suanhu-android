package com.xiaoxiong.library.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoxiong.library.R;

/**
 * Created by zhaoya on 2018/2/1.
 */

public class EmptyViewUtil {


    public static View getEmptyView(Context context, int drawableResId, String des){
        View view = LayoutInflater.from(context).inflate(R.layout.no_data_layout, null);
        ((ImageView) view.findViewById(R.id.iv)).setImageResource(drawableResId);
        ((TextView) view.findViewById(R.id.tv)).setText(des);
        return view;
    }

}
