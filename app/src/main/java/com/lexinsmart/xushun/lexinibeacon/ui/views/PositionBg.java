package com.lexinsmart.xushun.lexinibeacon.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lexinsmart.xushun.lexinibeacon.model.BasesBean;
import com.lexinsmart.xushun.lexinibeacon.model.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xushun on 2017/6/16.
 */

public class PositionBg extends View {
    Paint basePoint, LBaseCirclePaint, RBaseCircleRPaint, MBaseCirclePaint, LocationPoint, textPaint;
    //view 的宽高
    private float mWidth, mHeight;
    BasesBean basesBean = new BasesBean();
    List<BasesBean.BaseBean> base = new ArrayList<>();
    Coordinate mylocationCoor = new Coordinate();

    public PositionBg(Context context) {
        this(context, null);
    }

    public PositionBg(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PositionBg(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        BasesBean.BaseBean baseBean = new BasesBean.BaseBean();
//        baseBean.setBasemac("1");
//        baseBean.setBasex(0);
//        baseBean.setBasey(0);
//        baseBean.setR(1.2);
//
//        BasesBean.BaseBean baseBean2 = new BasesBean.BaseBean();
//        baseBean2.setBasemac("2");
//        baseBean2.setBasex(4.2);
//        baseBean2.setBasey(0);
//        baseBean2.setR(1.5);
//
//        BasesBean.BaseBean baseBean3 = new BasesBean.BaseBean();
//        baseBean3.setBasemac("3");
//        baseBean3.setBasex(2.1);
//        baseBean3.setBasey(2.1);
//        baseBean3.setR(2);
//
//        base.add(baseBean);
//        base.add(baseBean2);
//        base.add(baseBean3);
//        basesBean.setBase(base);


        basePoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        basePoint.setColor(Color.WHITE);
        basePoint.setAntiAlias(true);
        basePoint.setStyle(Paint.Style.FILL_AND_STROKE);
        basePoint.setStrokeWidth(10);

        LBaseCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        LBaseCirclePaint.setColor(Color.RED);
        LBaseCirclePaint.setAntiAlias(true);
        LBaseCirclePaint.setStyle(Paint.Style.STROKE);
        LBaseCirclePaint.setStrokeWidth(10);


        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(20);

        LocationPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        LocationPoint.setColor(Color.YELLOW);
        LocationPoint.setAntiAlias(true);
        LocationPoint.setStyle(Paint.Style.FILL_AND_STROKE);
        LocationPoint.setStrokeWidth(10);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBases(canvas);
        drawDistances(canvas);
        drawLocation(canvas);
    }

    private void drawLocation(Canvas canvas) {

        canvas.drawCircle(setX(mylocationCoor.getX()),setY(mylocationCoor.getY()),20,LocationPoint);
        handler.sendEmptyMessageDelayed(0,1000);
    }

    private void drawDistances(Canvas canvas) {
        for (int i = 0; i < basesBean.getBase().size(); i++) {
            BasesBean.BaseBean baseContent = basesBean.getBase().get(i);
            canvas.drawCircle(setX(baseContent.getBasex()), setY(baseContent.getBasey()), setR(baseContent.getR()), LBaseCirclePaint);
            handler.sendEmptyMessageDelayed(0,1000);

        }
    }

    private void drawBases(Canvas canvas) {
        for (int i = 0; i < basesBean.getBase().size(); i++) {
            BasesBean.BaseBean baseContent = basesBean.getBase().get(i);
            canvas.drawCircle(setX(baseContent.getBasex()), setY(baseContent.getBasey()), 20, basePoint);
            canvas.drawText(baseContent.getBasemac(), setX(baseContent.getBasex()) + 40, setY(baseContent.getBasey()) + 40, textPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(measuredDimension(widthMeasureSpec), measuredDimension(heightMeasureSpec));
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        System.out.println("w:" + mWidth + "\t" + mHeight);

    }

    private int measuredDimension(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 500;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }
    public void  setData( BasesBean base ){

        this.basesBean = base;
    }
    public void setLocationPoint(Coordinate coordinate){
        this.mylocationCoor = coordinate;

    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                invalidate();
            }
        }
    };

    private float setX(double x) {
        x = x * 200 + 100;
        return (float) x;
    }

    private float setY(double y) {
        y = y * 200 + 500;
        return (float) y;
    }

    private float setR(double r) {
        r = r * 200;
        return (float) r;
    }
}
