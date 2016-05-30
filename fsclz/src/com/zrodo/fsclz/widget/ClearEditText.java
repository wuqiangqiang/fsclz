package com.zrodo.fsclz.widget;

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by grandry.xu on 15-10-27.
 * 具有删除图标的edittext
 */
public class ClearEditText extends EditText implements TextWatcher, View.OnFocusChangeListener {
    /**
     * 左右图片资源
     */
    private Drawable left, right;

    /**
     * 是否获取焦点
     */
    private boolean hasFocus = false;

    /**
     * 手指抬起时x坐标
     */
    private int xUp = 0;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initParam(attrs);
    }

    /**
     * 获取edittext的左右图片
     *
     * @param set
     */
    private void initParam(AttributeSet set) {
        try {
            left = getCompoundDrawables()[0];
            right = getCompoundDrawables()[2];
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        try {
            if (left != null) {
                setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
            }else{
                setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            addListener();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 添加事件监听
     */
    private void addListener() {
        try {
            setOnFocusChangeListener(this);
            addTextChangedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus=hasFocus;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int after) {

        if (hasFocus) {
            if (TextUtils.isEmpty(s)) {
                //如果为空，则不显示右侧删除图标
                setCompoundDrawablesWithIntrinsicBounds(left,null,null,null);
            }else{
                //not empty show right icon
                if(right!=null){
                    setCompoundDrawablesWithIntrinsicBounds(left,null,right,null);
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try{
            switch (event.getAction()){
                case MotionEvent.ACTION_UP:
                	if (getCompoundDrawables()[2] != null) {

        				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
        						&& (event.getX() < ((getWidth() - getPaddingRight())));
        				
        				if (touchable) {
        					this.setText("");
        				}
        			}
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置动画
     */
    public void shakeAimation(int count){
        Animation shake=new TranslateAnimation(0,10,0,0);
        shake.setInterpolator(new CycleInterpolator(count));
        shake.setDuration(1000);
        startAnimation(shake);
    }
}
