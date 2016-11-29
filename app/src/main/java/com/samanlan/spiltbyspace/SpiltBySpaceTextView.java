package com.samanlan.spiltbyspace;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 作者(Author)：蓝深铭(LanSaman)
 * 邮箱(E-Mail)：lansaman@163.com
 * 时间(Time)：on 2016/11/29 20:01
 */

/**
 * 可使用你自己定制的View代替
 */
public class SpiltBySpaceTextView extends LinearLayout {

    Context mContext;

    public SpiltBySpaceTextView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SpiltBySpaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public SpiltBySpaceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    TextView textView;
    ImageView imageView;
    SpiltBySpaceClickListener mEditBySpaceClickListener;

    private void init() {
        textView = new TextView(mContext);
        textView.setBackgroundResource(R.drawable.edit_by_space_textview_bg);
        textView.setTextSize(14);
        textView.setTextColor(Color.BLACK);
        textView.setPadding(6, 4, 6 + dip2px(mContext, 8), 4);
//        textView.setSingleLine();
        textView.setGravity(Gravity.CENTER);
        LayoutParams text_lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        text_lp.topMargin = 8;
        text_lp.weight = 1;

        imageView = new ImageView(mContext);
        /** 在此我是使用一个imageView作为删除按钮，调用layout的delete回调 */
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditBySpaceClickListener != null) {
                    mEditBySpaceClickListener.delete(SpiltBySpaceTextView.this);
                } else {
                    throw new NullPointerException("没有监听器");
                }
            }
        });
        imageView.setImageResource(R.drawable.delete);
        LayoutParams img_lp = new LayoutParams(dip2px(mContext, 16), dip2px(mContext, 16));
        img_lp.leftMargin = -dip2px(mContext, 8);

        addView(textView, text_lp);
        addView(imageView, img_lp);
    }

    public void setText(String s) {
        textView.setText(s);
    }

    /**
     * 如果改成定制的View，需要提供一个getString方法给layout获取View里面字符串信息
     */
    public String getText() {
        return textView.getText().toString();
    }

    public void setOnClickListener(SpiltBySpaceClickListener editBySpaceClickListener) {
        mEditBySpaceClickListener = editBySpaceClickListener;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
