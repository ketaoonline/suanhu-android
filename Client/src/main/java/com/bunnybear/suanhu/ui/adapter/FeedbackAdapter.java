package com.bunnybear.suanhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.luck.picture.lib.entity.LocalMedia;
import com.xiaoxiong.library.base.BaseListAdapter;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedbackAdapter extends BaseListAdapter<LocalMedia>{
    public FeedbackAdapter(AppActivity activity, List<LocalMedia> data) {
        super(activity, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_feedback,null);
            holder = new ViewHolder();
            ButterKnife.bind(holder,convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        LocalMedia media = data.get(position);
        if ("default".equals(media.getPath())){
            holder.ivAdd.setVisibility(View.VISIBLE);
            holder.ivBig.setVisibility(View.GONE);
        }else {
            holder.ivAdd.setVisibility(View.GONE);
            holder.ivBig.setVisibility(View.VISIBLE);
        }
        GlideUtil.load(media.getPath(),holder.ivBig);
        return convertView;
    }

    public class ViewHolder{
        @BindView(R.id.iv_big)
        ImageView ivBig;
        @BindView(R.id.iv_add)
        ImageView ivAdd;
    }


}
