package com.bunnybear.suanhu.ui.adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bunnybear.suanhu.R;
import com.bunnybear.suanhu.base.AppActivity;
import com.bunnybear.suanhu.bean.Lesson;
import com.bunnybear.suanhu.bean.MainBaseBean;
import com.bunnybear.suanhu.bean.PlayDetail;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiaoxiong.library.event.BusFactory;
import com.xiaoxiong.library.event.IEvent;
import com.xiaoxiong.library.view.GridShowView;

import java.util.ArrayList;
import java.util.List;

public class PlayAdapter extends BaseMultiItemQuickAdapter<MainBaseBean,BaseViewHolder> {

    AppActivity activity;
    List<Lesson> lessonList;
    public int checkPosition = 0;
    boolean isVideo;
    public PlayAdapter(AppActivity activity,List<MainBaseBean> data,boolean isVideo) {
        super(data);
        this.activity = activity;
        this.isVideo = isVideo;
        addItemType(0, R.layout.item_lesson_info);
        addItemType(1, R.layout.item_lesson_gv);

        lessonList = new ArrayList<>();

    }

    @Override
    protected void convert(BaseViewHolder helper, MainBaseBean item) {
        switch (helper.getItemViewType()){
            case 0:
                if (item instanceof PlayDetail){
                    PlayDetail playDetail = (PlayDetail) item;
                    lessonList.clear();
                    lessonList.addAll(playDetail.getCatalog_info());
                    helper.setText(R.id.tv_name,playDetail.getName());
                    helper.setText(R.id.tv_introduce,playDetail.getIntroduce());
                    helper.setText(R.id.tv_total,"共"+playDetail.getCount()+"课时");
                    helper.setImageResource(R.id.iv_collect,playDetail.getCollection() == 0? R.mipmap.gray_star_big : R.mipmap.middle_yellow_star);
                    helper.addOnClickListener(R.id.iv_collect);
                    helper.addOnClickListener(R.id.iv_share);
                }
                break;
            case 1:
                GridShowView gv = helper.getView(R.id.gv);
                gv.setFocusableInTouchMode(false);
                gv.requestFocus();
                final PlayGvAdapter adapter = new PlayGvAdapter(activity,lessonList,checkPosition);
                gv.setAdapter(adapter);
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Lesson lesson = lessonList.get(i);
                        if (lesson.getBuy() == 0){
                            Toast.makeText(activity,"暂未购买该课程",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (adapter.checkPosition == i)return;
                        adapter.setCheckPosition(i);
                        setCheckPosition(i);
                        BusFactory.getBus().post(new IEvent(isVideo?"VideoUrl":"ImageUrl", lesson.getUrl()));
                    }
                });
                gv.smoothScrollToPositionFromTop(checkPosition,10,1500);
                break;
        }
    }

    public void setCheckPosition(int checkPosition) {
        this.checkPosition = checkPosition;
        notifyDataSetChanged();
    }
}
