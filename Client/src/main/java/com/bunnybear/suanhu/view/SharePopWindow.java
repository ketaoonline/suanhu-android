package com.bunnybear.suanhu.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bunnybear.suanhu.R;

/**
 * Created by Administrator on 2017/10/26 0026.
 */

public class SharePopWindow extends PopupWindow {

    private View view;

    public SharePopWindow(Activity activity, View.OnClickListener itemsOnClick) {
        super(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        view = inflater.inflate(R.layout.layout_share,null);

        view.findViewById(R.id.ll_weixin).setOnClickListener(itemsOnClick);
        view.findViewById(R.id.ll_friends_circle).setOnClickListener(itemsOnClick);
        view.findViewById(R.id.ll_qq).setOnClickListener(itemsOnClick);
        view.findViewById(R.id.ll_qzone).setOnClickListener(itemsOnClick);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(RelativeLayout.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}
