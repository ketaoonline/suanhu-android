package com.xiaoxiong.library.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class MyTextView extends TextView {

    private final String namespace = "http://hdt.hdt/hdt";
    private String text;
    private float textSize;
    private Paint paint1 = new Paint();
    private float paddingLeft;
    private float paddingRight;
    private float textShowWidth;
    private int textColor;
    private float lineSpace;
    private int maxLines = Integer.MAX_VALUE;
    private boolean ellipsize = false;
    private float ellipsizeLength = 0;
    private String ellipsizeString = "（未完待续...）";
    // 初始化是否需要设置高度，不加判断则会无限的递归onDraw，自己看
    private boolean needHieght = true;
    private int lineCount = 0;

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        text = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text");
        textSize = attrs.getAttributeIntValue(namespace, "textSize", 10);
        textColor = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "textColor", Color.WHITE);
        maxLines = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "maxLines", Integer.MAX_VALUE);
        String ell = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "ellipsize");
        ellipsize = "3".equals(ell);//android:ellipsize="end"对应的值是3，请看安卓源码
        paddingLeft = attrs.getAttributeIntValue(namespace, "paddingLeft", 0);
        paddingRight = attrs.getAttributeIntValue(namespace, "paddingRight", 0);
        lineSpace = attrs.getAttributeIntValue(namespace, "lineSpacingExtra", 3);
        float d = context.getResources().getDisplayMetrics().density;
        textSize = d * textSize + 0.5f;
        lineSpace = d * lineSpace + 0.5f;
        if(maxLines <= 0){
            maxLines = Integer.MAX_VALUE;
        }
        paint1.setTextSize(textSize);
        paint1.setColor(textColor);
        paint1.setAntiAlias(true);
        textShowWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth() - paddingLeft - paddingRight;
        ellipsizeLength = paint1.measureText(ellipsizeString);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int lineCount = 0;
        char[] textCharArray = text.toCharArray();
        float drawedWidth = 0;
        float charWidth;
        for (int i = 0; i < textCharArray.length; i++) {
            charWidth = paint1.measureText(textCharArray, i, 1);
            // 这里是用于，设置了最大行数和末尾省略的情况下进行的判断,16完全是我凭感觉给出的数字，没有为什么
            if (ellipsize && textShowWidth - drawedWidth - ellipsizeLength < 16) {
                if (lineCount == maxLines - 1) {
                    canvas.drawText(ellipsizeString, paddingLeft + drawedWidth, (lineCount + 1) * textSize + lineSpace
                            * lineCount, paint1);
                    break;
                }
            }
            // 跳入下一行判断
            if (textShowWidth - drawedWidth < charWidth || textCharArray[i] == '\n') {
                lineCount++;
                if (lineCount > maxLines - 1) {
                    lineCount--;
                    break;
                }
                drawedWidth = 0;
            }

            canvas.drawText(textCharArray, i, 1, paddingLeft + drawedWidth, (lineCount + 1) * textSize + lineSpace * lineCount,
                    paint1);
            drawedWidth += charWidth;
        }
        if (needHieght) {
            setHeight((lineCount + 1) * (int) (textSize + lineSpace));
            needHieght = false;
        }
        this.lineCount = lineCount;
    }

    @Override
    public void invalidate() {
        needHieght = true;
        super.invalidate();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        // TODO Auto-generated method stub
        super.setText(text, type);
        this.text = String.valueOf(text);
        invalidate();
    }

    /**
     * 设置省略显示内容，默认"。。。"
     *
     * @param ellString
     */
    public final void setEllipsizeString(String ellString) {
        this.ellipsizeString = ellString;
    }

    /**
     * 设置结尾是否显示省略内容
     *
     * @param isEnd
     */
    public final void setEllipsizeEnd(boolean isEnd) {
        this.ellipsize = isEnd;
    }

    @Override
    public void setMaxLines(int maxlines) {
        this.maxLines = maxlines;
        if(this.maxLines <= 0){
            this.maxLines = Integer.MAX_VALUE;
        }
        super.setMaxLines(maxlines);
    }

    @Override
    public CharSequence getText() {
        return this.text;
    }

    /**
     * 设置行间距
     *
     * @param spa
     */
    public void setLineSpacingExtra(float spa) {
        this.lineSpace = spa;
    }

    @Override
    public int getLineCount() {
        return lineCount;
    }

    @Override
    public int getLineHeight() {
        return (int) (textSize+lineSpace);
    }

    @Override
    public float getTextSize() {
        return textSize;
    }

    @Override
    public void setSingleLine() {
        setSingleLine(true);
    }

    @Override
    public void setSingleLine(boolean singleLine) {
        if(singleLine){
            setMaxLines(1);
        }else{
            setMaxLines(Integer.MAX_VALUE);
        }
    }

    @Override
    public void setTextColor(int color) {
        this.textColor = color;
        paint1.setColor(color);
        super.setTextColor(color);
    }
}
