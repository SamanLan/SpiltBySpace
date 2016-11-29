package com.samanlan.spiltbyspace;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者(Author)：蓝深铭(LanSaman)
 * 邮箱(E-Mail)：lansaman@163.com
 * 时间(Time)：on 2016/11/29 19:59
 */

public class SpiltBySpaceLayout extends ViewGroup {

    Context mContext;
    /** 是通过list来保存所有的子View顺序，也使得editText位于最后一位 */
    List<View> mList;
    EditText mEditText;

    public SpiltBySpaceLayout(Context context) {
        super(context);
        init(context);
    }

    public SpiltBySpaceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SpiltBySpaceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /** 当子view需要删除功能，就可以使用这个接口 */
    SpiltBySpaceClickListener spiltBySpaceClickListener;

    private void init(Context context) {
        mContext = context;
        mList = new ArrayList<View>();
        mEditText = new EditText(context);
        spiltBySpaceClickListener = new SpiltBySpaceClickListener() {
            @Override
            public void delete(View view) {
                /** list和viewGroup同时删除该子View */
                SpiltBySpaceLayout.this.removeView(view);
                mList.remove(view);
            }
        };
        mEditText = new EditText(mContext);
        mEditText.setTextSize(12);
        mEditText.setGravity(Gravity.CENTER);
        mEditText.setTextColor(0xff000000);
        mEditText.setSingleLine(true);
        mEditText.setHint("空格截断");
//        InputFilter[] inputFilter={new InputFilter.LengthFilter(10)};
//        mEditText.setFilters(inputFilter);
//            mEditText.setBackgroundResource(R.drawable.input_back);
        MarginLayoutParams layoutParams = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
        mList.add(mEditText);
        addView(mEditText, layoutParams);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println(editable.toString());
                String s = editable.toString();
                if (s.length() > 0 && s.charAt(s.length() - 1) == ' ') {
                    /** 监听editText，当输入空格，为ViewGroup增加一个子view */
                    System.out.println("输入空格" + "    宽度：" + getMeasuredWidth() + "  高度：" + getMeasuredHeight());
                    add(s.substring(0, s.length() - 1));
                    editable.clear();
                }
            }
        });
    }

    public void add(String s) {
        SpiltBySpaceTextView textView = new SpiltBySpaceTextView(mContext);
        textView.setText(s);
        textView.setOnClickListener(spiltBySpaceClickListener);
        MarginLayoutParams layoutParams = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = 4;
        layoutParams.topMargin = 6;
        mList.add(mList.size() - 1, textView);
        addView(textView, layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightResult = 0;
        int widthResult = 0;

        int widthUsed = 0;
        int heightUsed = 0;

        /** 测量所有子View */
        for (int i = 0; i < mList.size(); i++) {
            View v = mList.get(i);
            measureChild(v, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams layoutParams = (MarginLayoutParams) v.getLayoutParams();

            int v_width = v.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            int v_height = v.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

            if (width - getPaddingLeft() - getPaddingRight() < widthUsed + v_width) {
                widthResult = Math.max(widthResult, widthUsed);
                widthUsed = v_width;
                heightResult += heightUsed;
                heightUsed = v_height;
            } else {
                widthUsed += v_width;
                heightUsed = Math.max(heightUsed, v_height);
            }
            if (i == mList.size() - 1) {
                widthResult = Math.max(widthUsed, widthResult);
                heightResult += heightUsed;
            }
        }
        setMeasuredDimension(
                widthMode == MeasureSpec.EXACTLY ? width : widthResult + getPaddingLeft() + getPaddingRight(),
                heightMode == MeasureSpec.EXACTLY ? height : heightResult + getPaddingTop() + getPaddingBottom()//
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //实际宽度
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        //已用宽度
        int widthUsed = 0;
        //已用高度
        int heightUsed = 0;
        //用于保存同一行内各个view中最高的那个
        int tempHeight = 0;
        int lastViewHeight = 0;

        for (int i = 0; i < mList.size(); i++) {
            View v = mList.get(i);
            MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
            int tv_width = v.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
            int tv_height = v.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (tv_width + widthUsed > width) {
                if (tempHeight == 0)
                    tempHeight = lastViewHeight;
                widthUsed = 0;
                heightUsed += tempHeight;
                tempHeight = 0;
                v.layout(widthUsed + lp.leftMargin + getPaddingLeft()
                        , heightUsed + lp.topMargin + getPaddingTop()
                        , widthUsed + lp.leftMargin + v.getMeasuredWidth() + getPaddingLeft()
                        , heightUsed + lp.topMargin + v.getMeasuredHeight() + getPaddingTop());
                widthUsed += tv_width;
            } else {
                v.layout(widthUsed + lp.leftMargin + getPaddingLeft()
                        , heightUsed + lp.topMargin + getPaddingTop()
                        , widthUsed + lp.leftMargin + v.getMeasuredWidth() + getPaddingLeft()
                        , heightUsed + lp.topMargin + v.getMeasuredHeight() + getPaddingTop());
                widthUsed += tv_width;
                if (tv_height > tempHeight) {
                    tempHeight = tv_height;
                }
            }
            lastViewHeight = tv_height;
        }
    }

    // 取得里面所有text，包括edittext里面还没按空格显示的的text
    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < mList.size() - 1; i++) {
            SpiltBySpaceTextView textView = (SpiltBySpaceTextView) mList.get(i);
            str += textView.getText() + " ";
        }
        str += mEditText.getText();
        return str;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

}
