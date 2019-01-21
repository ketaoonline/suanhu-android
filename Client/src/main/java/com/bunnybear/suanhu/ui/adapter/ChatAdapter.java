package com.bunnybear.suanhu.ui.adapter;

import android.widget.TextView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.ChatMsg;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class ChatAdapter extends BaseMultiItemQuickAdapter<ChatMsg,BaseViewHolder> {

    public ChatAdapter(List<ChatMsg> data) {
        super(data);

        addItemType(1, R.layout.item_self_send_text);
        addItemType(2, R.layout.item_self_send_image);
        addItemType(3, R.layout.item_receive_text);
        addItemType(4, R.layout.item_receive_image);

    }

    @Override
    protected void convert(BaseViewHolder helper, ChatMsg item) {
        switch (helper.getItemViewType()){
            case 1:
                helper.setText(R.id.tv_send_text,item.getContent());
                GlideUtil.load(item.getSend_avatar(),helper.getView(R.id.iv_send_head));
                break;
            case 2:
                GlideUtil.load(item.getContent(),helper.getView(R.id.iv_send_image));
                helper.addOnClickListener(R.id.iv_send_image);
                GlideUtil.load(item.getSend_avatar(),helper.getView(R.id.iv_send_head));
                break;
            case 3:
                GlideUtil.load(item.getSend_avatar(),helper.getView(R.id.iv_receive_head));
                helper.setText(R.id.tv_receive_text,item.getContent());
                break;
            case 4:
                GlideUtil.load(item.getSend_avatar(),helper.getView(R.id.iv_receive_head));
                GlideUtil.load(item.getContent(),helper.getView(R.id.iv_receive_image));
                helper.addOnClickListener(R.id.iv_receive_image);
                break;

        }


    }
}
