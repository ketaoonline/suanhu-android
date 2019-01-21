package com.bunnybear.suanhu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.Experience;
import com.xiaoxiong.library.base.BaseListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExperienceAdapter extends BaseListAdapter<Experience>{

    LayoutInflater inflater;
    public ExperienceAdapter(Context context, List<Experience> data) {
        super(context, data);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_experience,null);
            holder = new ViewHolder();
            ButterKnife.bind(holder,convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Experience experience = data.get(position);
        holder.tvDate.setText(experience.getTime());
        holder.tvContent.setText(experience.getInfo());
        holder.topLine.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);

        return convertView;
    }


    public class ViewHolder{
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.top_line)
        View topLine;
    }
}
