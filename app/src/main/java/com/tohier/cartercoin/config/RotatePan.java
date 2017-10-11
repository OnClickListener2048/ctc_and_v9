package com.tohier.cartercoin.config;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.tohier.cartercoin.activity.NewSpanLayoutActivity2;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 作者：Nipuream
 * 时间: 2016-08-16 10:18
 * 邮箱：571829491@qq.com
 */
public class RotatePan extends View {

    private Context context;

    private Paint dPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int InitAngle = 30;
    private int radius = 0;

    private String[] strs = {" ", " ", " ", " ", " ", " ", " ", " "};
    private String[] descs = {" ", " ", " ", " ", " ", " ", " ", " "};
    private List<Bitmap> bitmaps = new ArrayList<>();
    private ScrollerCompat scroller;
    private int spanCount = 8;
    private int jiaoDu = 0;
    private int bitWidth = 0;
    private int[] mSpanColor = new int[]{0XFFe4f5d5, 0XFF88b8b7, 0XFFa9e7e6, 0XFFe4f5d5, 0XFF88b8b7, 0XFFa9e7e6, 0XFFe4f5d5, 0XFF88b8b7, 0XFFa9e7e6, 0XFFe4f5d5, 0XFF88b8b7, 0XFFa9e7e6};

    private Canvas canvas = null;

    public RotatePan(Context context) {
        this(context, null);
    }

    public RotatePan(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotatePan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;



        scroller = ScrollerCompat.create(context);

        dPaint.setColor(Color.rgb(255, 133, 132));
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(Util.dip2px(context, 12));
        setClickable(true);

        if (null != LoginUser.getInstantiation(getContext().getApplicationContext()).getLoginUser()) {
            if (null != LoginUser.getInstantiation(getContext().getApplicationContext()).getLoginUser().getSex()) {
                String sex = LoginUser.getInstantiation(getContext().getApplicationContext()).getLoginUser().getSex();
                //默认是男生的
                if (sex.equals("美女")) {
                    mSpanColor = new int[]{0Xfff986c1, 0XFFf668bd, 0XFFe90dc7, 0Xfff986c1, 0XFFf668bd, 0XFFe90dc7, 0Xfff986c1, 0XFFf668bd, 0XFFe90dc7, 0Xfff986c1, 0XFFf668bd, 0XFFe90dc7};
                } else if (sex.equals("帅哥")) {
                    textPaint.setColor(0xff4d4d4d);
                    mSpanColor = new int[]{0XFFe4f5d5, 0XFF88b8b7, 0XFFa9e7e6, 0XFFe4f5d5, 0XFF88b8b7, 0XFFa9e7e6, 0XFFe4f5d5, 0XFF88b8b7, 0XFFa9e7e6, 0XFFe4f5d5, 0XFF88b8b7, 0XFFa9e7e6};
                } else {
                    mSpanColor = new int[]{0XFF6d428d, 0XFFbc4e00, 0XFFeb6100, 0XFF6d428d, 0XFFbc4e00, 0XFFeb6100, 0XFF6d428d, 0XFFbc4e00, 0XFFeb6100, 0XFF6d428d, 0XFFbc4e00, 0XFFeb6100};
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //wrap_content value
        int mHeight = Util.dip2px(context, 300);
        int mWidth = Util.dip2px(context, 300);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.canvas = canvas;

        jiaoDu = 360 / spanCount;
        bitWidth = (int) (spanCount / 1.25);

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        int MinValue = Math.min(width, height);

        radius = MinValue / 2;

        RectF rectF = new RectF(getPaddingLeft(), getPaddingTop(), width, height);

        int angle = InitAngle - 30;

        for (int i = 0; i < spanCount; i++) {
            dPaint.setColor(mSpanColor[i]);
            canvas.drawArc(rectF, angle, jiaoDu, true, dPaint);
            drawText(angle, strs[i], descs[i], 2 * radius, textPaint, canvas, rectF);
            drawIcon(width / 2, height / 2, radius + 60, angle + jiaoDu / 2, i, canvas);
            angle += jiaoDu;
        }
        NewSpanLayoutActivity2.setClickTrue();
    }

    private void drawText(float startAngle, String string, String desc, int mRadius, Paint mTextPaint, Canvas mCanvas, RectF mRange) {
        Path path = new Path();
        path.addArc(mRange, startAngle, jiaoDu);
        float textWidth = mTextPaint.measureText(string);
        float descWidth = mTextPaint.measureText(desc);

        float hOffset = (float) (mRadius * Math.PI / spanCount / 2 - textWidth / 2);
        float descOffset = (float) (mRadius * Math.PI / spanCount / 2 - descWidth / 2);

        float vOffset = mRadius / 2 / 4;
        float vOffset2 = mRadius / 15;

        mCanvas.drawTextOnPath(desc, path, descOffset, vOffset, mTextPaint);
        mCanvas.drawTextOnPath(string, path, hOffset, vOffset2, mTextPaint);
    }

    private void drawIcon(int xx, int yy, int mRadius, float startAngle, int i, Canvas mCanvas) {
        int imgWidth = mRadius / 4;

        float angle = (float) Math.toRadians(startAngle);

        float x = (float) (xx + mRadius / 2 * Math.cos(angle));
        float y = (float) (yy + mRadius / 2 * Math.sin(angle));

        // 确定绘制图片的位置
        RectF rect = new RectF(x - imgWidth * 3 / bitWidth, y - imgWidth * 3 / bitWidth, x + imgWidth
                * 3 / bitWidth, y + imgWidth * 3 / bitWidth);

        if (bitmaps.size() > 0 && bitmaps.size() - 1 >= i) {
            Bitmap bitmap = bitmaps.get(i);
            mCanvas.drawBitmap(bitmap, null, rect, null);
        }
    }

    public void setData(int spanCount, String strs[], String[] descs, int[] mSpanColor, List<Bitmap> bitmaps) {
        this.bitmaps = new ArrayList<>();
        for (int i = 0; i < bitmaps.size(); i++) {
            this.bitmaps.add(bitmaps.get(i));
        }
        this.spanCount = spanCount;
        this.strs = strs;
        this.descs = descs;
        this.mSpanColor = mSpanColor;
    }

    public void refresh() {
        this.invalidate();
    }

    public void setStr(String... strs) {
        this.strs = strs;
    }

    //旋转一圈所需要的时间
    private static final long ONE_WHEEL_TIME = 500;

    /**
     * 开始转动
     *
     * @param pos 如果 pos = -1 则随机，如果指定某个值，则转到某个指定区域
     */
    public void startRotate(int pos) {

        int lap = 5;

        int angle = 0;
        if (pos < 0) {
            angle = (int) (Math.random() * 360);
        } else {   // >=0
            int initPos = queryPosition();
            if (pos > initPos) {
                angle = (pos - initPos) * jiaoDu;
                lap -= 1;
                angle = 360 - angle;
            } else if (pos < initPos) {
                angle = (initPos - pos) * jiaoDu;
            } else {
                //nothing to do.
            }
        }

        int increaseDegree = lap * 360 + angle;
        long time = (lap + angle / 360) * ONE_WHEEL_TIME;
        int DesRotate = increaseDegree + InitAngle;

        //TODO 为了每次都能旋转到转盘的中间位置
        int offRotate = DesRotate % 360 % jiaoDu;
        DesRotate -= offRotate;
        DesRotate += jiaoDu / 2;

        if (spanCount < 12) {
            DesRotate = (int) (DesRotate + (90 - (12 - spanCount) * 3));
        } else {
            DesRotate = (int) (DesRotate + 90);
        }

        ValueAnimator animtor = ValueAnimator.ofInt(InitAngle, DesRotate);
        animtor.setInterpolator(new AccelerateDecelerateInterpolator());
        animtor.setDuration(time);
        animtor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int updateValue = (int) animation.getAnimatedValue();
                InitAngle = (updateValue % 360 + 360) % 360;
                ViewCompat.postInvalidateOnAnimation(RotatePan.this);
            }
        });

        animtor.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

//                int pos = InitAngle / 60;
//                if(pos >= 0 && pos <= 3){
//                    pos = 3 - pos;
//                }else{
//                    pos = (6-pos) + 3;
//                }

                int position = 0;
                if (l != null)
                    position = queryPosition();
                l.endAnimation(position);
            }
        });
        animtor.start();
    }


    private int queryPosition() {
        InitAngle = (InitAngle % 360 + 360) % 360;
        int pos = InitAngle / jiaoDu;
        return calcumAngle(pos);
    }

    private int calcumAngle(int pos) {
        if (pos >= 0 && pos <= spanCount / 2) {
            pos = spanCount / 2 - pos;
        } else {
            pos = (spanCount - pos) + spanCount / 2;
        }
        return pos;
    }


    public interface AnimationEndListener {
        void endAnimation(int position);
    }

    private AnimationEndListener l;

    public void setAnimationEndListener(AnimationEndListener l) {
        this.l = l;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
    }

    public void setRotate(int rotation) {
        rotation = (rotation % 360 + 360) % 360;
        InitAngle = rotation;
        ViewCompat.postInvalidateOnAnimation(this);
    }


    @Override
    public void computeScroll() {

        if (scroller.computeScrollOffset()) {
            setRotate(scroller.getCurrY());
        }

        super.computeScroll();
    }
}
