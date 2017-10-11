package com.tohier.cartercoin.config;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.tohier.cartercoin.R;

import java.io.InputStream;

/**
 * 描述：
 * 作者：Nipuream
 * 时间: 2016-08-15 17:34
 * 邮箱：571829491@qq.com
 */
public class LuckPanLayout extends View {

    private Context context;
    private int radius;
    private int CircleX,CircleY;
    private Canvas canvas;
    private boolean isYellow = false;
    private int delayTime = 500;

    private Paint mSpanPaint;

    //盘的背景
    private Bitmap mSpanBackground = null;

    public LuckPanLayout(Context context) {
        this(context,null);
    }

    public LuckPanLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LuckPanLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        mSpanPaint = new Paint();
        mSpanPaint.setAntiAlias(true);
        mSpanPaint.setDither(true);

        if(null!= LoginUser.getInstantiation(getContext().getApplicationContext()).getLoginUser())
        {
            if(null!=LoginUser.getInstantiation(getContext().getApplicationContext()).getLoginUser().getSex())
            {
                String sex = LoginUser.getInstantiation(getContext().getApplicationContext()).getLoginUser().getSex();
                if(null!=mSpanBackground&&!mSpanBackground.isRecycled())
                {
                    mSpanBackground.recycle();
                    mSpanBackground = null;
                }
                System.gc();

                //默认是男生的
                if(sex.equals("美女"))
                {
                    mSpanBackground = readBitMap(context,R.mipmap.iv_luckpan_girl_bg3);
//                    mSpanBackground = BitmapFactory.decodeResource(getResources(), R.mipmap.iv_luckpan_girl_bg3);
                }else if(sex.equals("帅哥"))
                {
                    mSpanBackground = readBitMap(context,R.mipmap.bg3);
//                    mSpanBackground = BitmapFactory.decodeResource(getResources(), R.mipmap.bg3);
                }else
                {
                    mSpanBackground = readBitMap(context,R.mipmap.iv_luckpan_secrecy_bg3);
//                    mSpanBackground = BitmapFactory.decodeResource(getResources(), R.mipmap.iv_luckpan_secrecy_bg3);
                }
            }
        }
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthSpecMode == MeasureSpec.AT_MOST  && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(180, 180);
        }else if(widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(180, heightSpecSize);
        }else if(heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize, 180);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        int MinValue = Math.min(width,height);

        radius = MinValue /2;
        CircleX = getWidth() /2;
        CircleY = getHeight() /2;

        canvas.drawBitmap(mSpanBackground, null, new RectF(getPaddingLeft()/2, getPaddingTop() / 2, getMeasuredWidth() - getPaddingRight() / 2, getMeasuredHeight() - getPaddingBottom() / 2), mSpanPaint);


//        drawSmallCircle(isYellow);
    }

//    private void drawSmallCircle(boolean FirstYellow){
//        int pointDistance = radius - Util.dip2px(context,10);
//        for(int i=0;i<=360;i+=20){
//            int x = (int) (pointDistance * Math.sin(Util.change(i))) + CircleX;
//            int y = (int) (pointDistance * Math.cos(Util.change(i))) + CircleY;
//
//            if(FirstYellow)
//                canvas.drawCircle(x,y,Util.dip2px(context,4),yellowPaint);
//            else
//                canvas.drawCircle(x,y,Util.dip2px(context,4),whitePaint);
//            FirstYellow = !FirstYellow;
//        }
//    }

    public void startLuckLight(){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                isYellow = !isYellow;
                invalidate();
                postDelayed(this,delayTime);
            }
        },delayTime);
    }

    public void setDelayTime(int delayTime){
        this.delayTime = delayTime;
    }

}
