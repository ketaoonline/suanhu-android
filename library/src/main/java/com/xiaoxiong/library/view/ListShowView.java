package com.xiaoxiong.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListShowView extends ListView {

	public ListShowView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListShowView(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightSpec;

		if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
			heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
					MeasureSpec.AT_MOST);
		} else {
			heightSpec = heightMeasureSpec;
		}

		super.onMeasure(widthMeasureSpec, heightSpec);
	}

};
