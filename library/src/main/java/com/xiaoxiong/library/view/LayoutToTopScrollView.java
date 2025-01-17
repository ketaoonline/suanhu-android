package com.xiaoxiong.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class LayoutToTopScrollView extends ScrollView {

	private int _scrollY;
	private int _scrollX;
	private GestureDetector mGestureDetector;
	public LayoutToTopScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	} 

	public LayoutToTopScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

	public LayoutToTopScrollView(Context context) {
		super(context);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		_scrollY = getScrollY();
		_scrollX = getScrollX();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		LayoutToTopScrollView.this.scrollTo(_scrollX, 0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
	}

	class YScrollDetector extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			/**
			 * if we're scrolling more closer to x direction, return false, let subview to process it
			 */
			return (Math.abs(distanceY) > Math.abs(distanceX));
		}
	}

} 