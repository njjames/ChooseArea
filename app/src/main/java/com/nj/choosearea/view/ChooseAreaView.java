package com.nj.choosearea.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2018-03-03.
 */

public class ChooseAreaView extends View {
    private static final String TAG = "ChooseAreaView";
    private String[] letter = new String[]{"定位", "最近", "热门", "全部", "A", "B", "C", "D",
            "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q",
            "R", "S", "T", "W", "X", "Y", "Z"};
    private boolean isShowBG = false;
    private Paint mPaint;
    private OnSlidingListener mOnSlidingListener;
    private TextView mTextView;
    private int choose = -1;

    public ChooseAreaView(Context context) {
        this(context, null);
    }

    public ChooseAreaView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChooseAreaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始化一个画笔，用来写字符
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#8c8c8c"));
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(26);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowBG) {
            canvas.drawColor(Color.parseColor("#40000000"));
        }
        //获取每个字符的高度
        int singleHeight = getHeight() / letter.length;
        //循环所有行的字符
        for (int i = 0; i < letter.length; i++) {
            //得到每行的字符内容
            String text = letter[i];
            //获取绘制文字的x坐标
            float xPosition = getWidth() / 2 - mPaint.measureText(text) / 2;
            //获取绘制文字的y坐标（通过逐渐增加y的坐标来画出每行的文字）
            float yPosition = singleHeight * i + singleHeight;
            //把本行的文字绘制出来
            canvas.drawText(text, xPosition, yPosition, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        //获取当前事件的位置，根据当前点击的y坐标比上总像素值，再乘以数组的长度得到当前的位置
        int position = (int) (event.getY() / getHeight() * letter.length);
        int oldchoose = choose;
        switch (action) {
            case MotionEvent.ACTION_DOWN: //按下时
                //显示BG
                isShowBG = true;
                if (position >= 0 && position < letter.length) {
                    //当前点击位置的文字
                    String text = letter[position];
                    if (mTextView != null) {
                        mTextView.setVisibility(VISIBLE);
                        mTextView.setText(text);
                    }
                    if (mOnSlidingListener != null) {
                        mOnSlidingListener.sliding(text);
                    }
                    //重画（感觉不用重画啊，这里重画就是这一列的文字，不知道为什么要重画）
//                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                isShowBG = true;
                if (position != oldchoose) { //如果当前点击的位置获取到的位置不等于上一次旧的选择的位置，才重新设置
                    if (position >= 0 && position < letter.length) {
                        //把当前的位置设置为选择选择的位置
                        choose = position;
                        //当前点击位置的文字
                        String text = letter[position];
                        if (mTextView != null) {
                            mTextView.setVisibility(VISIBLE);
                            mTextView.setText(text);
                        }
                        if (mOnSlidingListener != null) {
                            mOnSlidingListener.sliding(text);
                        }
                        //                    invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isShowBG = false;
                if (mTextView != null) {
                    mTextView.setVisibility(GONE);
                }
//                invalidate();
                break;
        }
        //这里必须返回true，否则只能响应ACTION_DOWN的事件
        return true;
    }

    //定义一个回调接口（感觉这里没有必须设置这个回调接口，或者回调接口和设置TextView选择一个，但是如果需要把数据传出去，必须添加一个回调）
    public interface OnSlidingListener {
        //这个回调方法就是把点击位置的文件传出去
        public void sliding(String text);
    }

    //提供一个设置回调接口的公共方法
    public void setOnSlidingListener(OnSlidingListener onSlidingListener) {
        mOnSlidingListener = onSlidingListener;
    }

    //外面传递一个TextView，来显示文字
    public void setTextView(TextView textView) {
        mTextView = textView;
    }
}
