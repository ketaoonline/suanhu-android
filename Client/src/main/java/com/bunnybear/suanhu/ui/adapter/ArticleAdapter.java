package com.bunnybear.suanhu.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.bean.Article;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.utils.glide.GlideUtil;

import java.util.List;

public class ArticleAdapter extends BaseQuickAdapter<Article,BaseViewHolder>{
    public ArticleAdapter(@Nullable List<Article> data) {
        super(R.layout.item_article, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Article item) {
        helper.setText(R.id.tv_type, item.getCategory_name());
        helper.setText(R.id.tv_title, item.getPost_title());
        helper.setText(R.id.tv_author, "作者：" + item.getPost_source());
        helper.setText(R.id.tv_content, item.getPost_excerpt());
        GlideUtil.load(item.getImage(), (ImageView) helper.getView(R.id.iv));
    }
}
