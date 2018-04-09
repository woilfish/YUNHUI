package com.yunhui.component.edittext;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.yunhui.R;


/**
 * Created by pengmin on 2018/1/10.
 */

public class ShowPasswordEditText extends EditText implements View.OnFocusChangeListener,TextWatcher {

    private Drawable showDrawable;

    /**判断焦点*/
    private boolean hasFocus;

    private boolean isShow = false;

    public void setHasFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }



    public ShowPasswordEditText(Context context) {
        this(context, null);
    }

    public ShowPasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ShowPasswordEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        showDrawable = getCompoundDrawables()[2];
        if (showDrawable == null)
            showDrawable = getResources().getDrawable(R.mipmap.btn_eye_close);//NullPointerException ,在XML中设置drawableRight

        showDrawable.setBounds(0, 0, showDrawable.getIntrinsicWidth(), showDrawable.getIntrinsicHeight());
        this.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {


                //增大点范围进行测试
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()-10)
                        && (event.getX() < ((getWidth() - getPaddingRight()+10)));

                if (touchable) {
                    if(isShow){
                        isShow = false;
                        showDrawable = getResources().getDrawable(R.mipmap.btn_eye_close);
                        showDrawable.setBounds(0, 0, showDrawable.getIntrinsicWidth(), showDrawable.getIntrinsicHeight());
                        setCompoundDrawables(getCompoundDrawables()[0],
                                getCompoundDrawables()[1], showDrawable, getCompoundDrawables()[3]);
                        this.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }else{
                        isShow = true;
                        showDrawable = getResources().getDrawable(R.mipmap.btn_eye_open);
                        showDrawable.setBounds(0, 0, showDrawable.getIntrinsicWidth(), showDrawable.getIntrinsicHeight());
                        setCompoundDrawables(getCompoundDrawables()[0],
                                getCompoundDrawables()[1],showDrawable , getCompoundDrawables()[3]);
                        this.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_NULL);
                    }

                    this.setSelection(this.getText().length());
                }

        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }


    /**
     * 设置显隐
     *
     * @param visible
     */
    public void setClearIconVisible(boolean visible) {
        Drawable right = visible ? showDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        if (hasFocus) {
            setClearIconVisible(s.length() > 0);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
