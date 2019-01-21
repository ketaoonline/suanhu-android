package com.bunnybear.suanhu.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Chapter;
import com.bunnybear.suanhu.bean.Lesson;
import com.bunnybear.suanhu.ui.activity.AudioActivity;
import com.bunnybear.suanhu.ui.activity.PlayActivity;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;
import com.xiaoxiong.library.utils.LogUtil;
import com.xiaoxiong.library.utils.ToolsUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassDetailCatalogueAdater extends ExpandableRecyclerViewAdapter<ClassDetailCatalogueAdater.ChapterViewHolder, ClassDetailCatalogueAdater.LessonViewHolder> {

    LayoutInflater inflater;
    AppActivity activity;
    public ClassDetailCatalogueAdater(AppActivity activity, List<Chapter> groups) {
        super(groups);
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public ChapterViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_chapter,null);
        return new ChapterViewHolder(view);
    }

    @Override
    public LessonViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_lesson,null);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(LessonViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        holder.onBind((Lesson) group.getItems().get(childIndex));
    }

    @Override
    public void onBindGroupViewHolder(ChapterViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTvTitle(group);
    }


    public class ChapterViewHolder extends GroupViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.iv)
        ImageView iv;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTvTitle(ExpandableGroup group) {
            tvTitle.setText(group.getTitle());
        }

        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            RotateAnimation rotate = new RotateAnimation(360, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            iv.startAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            iv.startAnimation(rotate);
        }
    }

    public class LessonViewHolder extends ChildViewHolder {

        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.rl_lesson)
        RelativeLayout rlLesson;
        @BindView(R.id.tv)
        TextView tv;
        @BindView(R.id.iv_lock)
        ImageView ivLock;

        public LessonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(final Lesson lesson) {
            switch (lesson.getBuy()){
                case 0:
                    tv.setVisibility(View.INVISIBLE);
                    ivLock.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    tv.setVisibility(View.INVISIBLE);
                    ivLock.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    tv.setVisibility(View.VISIBLE);
                    ivLock.setVisibility(View.INVISIBLE);
                    break;
            }
            tvName.setText(lesson.getSension()+"  "+lesson.getName());
            switch (lesson.getType()){
                case 0://视频
                    tvType.setText("视频");
                    break;
                case 1://音频
                    tvType.setText("音频");
                    break;
                case 2://图文
                    tvType.setText("图文");
                    break;
            }
            rlLesson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lesson.getBuy() == 0){
                        Toast.makeText(activity,"暂未购买该课程",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    switch (lesson.getType()){
                        case 0://视频
                            PlayActivity.open(true,lesson.getId(),activity,-1);
                            break;
                        case 1://音频
                            AudioActivity.open(lesson.getId(),activity,-1);
                            break;
                        case 2://图文
                            PlayActivity.open(false,lesson.getId(),activity,-1);
                            break;
                    }

                }
            });
        }
    }



}
