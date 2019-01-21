package com.bunnybear.suanhu.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Lesson;
import com.xiaoxiong.library.base.BaseListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayGvAdapter extends BaseListAdapter<Lesson> {

    AppActivity activity;
    public int checkPosition = 0;

    public PlayGvAdapter(AppActivity activity, List<Lesson> data,int checkPosition) {
        super(activity, data);
        this.activity = activity;
        this.checkPosition = checkPosition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_lesson_item, null);
            holder = new ViewHolder();
            ButterKnife.bind(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.rl.setBackgroundResource(checkPosition == position ? R.drawable.shape_tag_checked : R.drawable.shape_tag_uncheck);
        holder.tvNameFront.setTextColor(checkPosition == position ? getColor(R.color.main_text_checked) : Color.parseColor("#666666"));
        holder.tvNameBack.setTextColor(checkPosition == position ? getColor(R.color.main_text_checked) : Color.parseColor("#666666"));

        holder.tvStatus.setVisibility(position < 2 ? View.VISIBLE : View.GONE);

        Lesson lesson = data.get(position);
        holder.tvNameFront.setText(lesson.getSension());
        holder.tvNameBack.setText(lesson.getName());

        switch (lesson.getType()){
            case 0://视频
                holder.tvType.setText("视频");
                break;
            case 1://音频
                holder.tvType.setText("音频");
                break;
            case 2://图文
                holder.tvType.setText("图文");
                break;
        }

        switch (lesson.getBuy()){
            case 0:
                holder.tvStatus.setVisibility(View.GONE);
                holder.ivLock.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.tvStatus.setVisibility(View.GONE);
                holder.ivLock.setVisibility(View.GONE);
                break;
            case 2:
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.ivLock.setVisibility(View.GONE);
                break;
        }

        return convertView;
    }


    public class ViewHolder {
        @BindView(R.id.rl)
        RelativeLayout rl;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_name_front)
        TextView tvNameFront;
        @BindView(R.id.tv_name_back)
        TextView tvNameBack;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.iv_lock)
        ImageView ivLock;

    }


    public void setCheckPosition(int position) {
        this.checkPosition = position;
        notifyDataSetChanged();
    }
}
