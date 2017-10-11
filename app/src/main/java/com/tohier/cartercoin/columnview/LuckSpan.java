package com.tohier.cartercoin.columnview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tohier.cartercoin.R;
import com.tohier.cartercoin.config.LoginUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wangchengmeng
 *         SurfaceView的一般写法
 */

public class LuckSpan extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final float CIRCLE_ANGLE      = 360;
    private static final float HALF_CIRCLE_ANGLE = 180;
    //Span的范围
    RectF            mRectRange;
    //圆环的范围
    RectF            mRectCircleRange;
    //绘制盘的画笔
    Paint            mSpanPaint;
    //绘制圆环
    Paint            mCirclePaint;
    //绘制文本的画笔
    Paint            mTextPaint;
    //转动状态监听
    SpanRollListener mSpanRollListener;
    //一般都会用到的成员变量
    private SurfaceHolder mSurfaceHolder;
    private Canvas        mCanvas;
    //开启线程绘制的线程
    private Timer mTimer;
    //控制线程的开关
    private boolean       isRunning;
    //奖项的名称
    private String[] mPrizeName = new String[]{"单反相机1", "IPad2", "恭喜发财3", "衣服一套4", "IPad5", "Iphone6", "衣服一套7", "恭喜发财8","单反相机9", "IPad10", "恭喜发财11", "衣服一套12"};
    //奖项的描述
    private String[] mPrizeDesc = new String[]{"单反相机1", "IPad2", "恭喜发财3", "衣服一套4", "IPad5", "Iphone6", "衣服一套7", "恭喜发财8","单反相机9", "IPad10", "恭喜发财11", "衣服一套12"};
    private String []    mPrizeIcon2 = new String[]{"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494842234035&di=1cd1138e69efdfd640f19acbfbb3d875&imgtype=0&src=http%3A%2F%2Fpic41.nipic.com%2F20140501%2F18539861_155959517170_2.jpg",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3224785883,1553235189&fm=11&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3009662169,1316632598&fm=11&gp=0.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494842352303&di=0b5bd44f5ac6166f4e82c53bcf707e24&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F5%2F5902b304badd1.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494842392981&di=9ab11515f76b6f4f99879921cc8a0e67&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Fskin%2F201312%2F25%2F19%2F20%2F52babf620e974066.jpg%2521600x600.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494842407070&di=6e5e2e52870202e9647832d0a10a0633&imgtype=0&src=http%3A%2F%2Fwww.qqjia.com%2Fz%2F06%2Ftu7991_12.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495437137&di=fe8a9a21cfaee1089cd2f36447476052&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3Dcba477b362d0f703e6b295d438fb5148%2F252dd42a2834349b428c4f21c9ea15ce37d3be8f.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495437148&di=8329eec55af28ff84e08afe17acc9e5a&imgtype=jpg&er=1&src=http%3A%2F%2Fp2.gexing.com%2Fshaitu%2F20130210%2F1859%2F51177da05d83f.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494842441684&di=26c079cd4237da3bf564cbcf86da8029&imgtype=0&src=http%3A%2F%2Fp3.gexing.com%2Fqqpifu%2F20120810%2F1542%2F5024bb6cc56c5.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494842460791&di=1764f2f84c05626ecdd6ba68dd000b69&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fpic%2Fitem%2Ffcfaaf51f3deb48f7f110b6af01f3a292df5780e.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495437195&di=9865debdd3d86e25bf8b1c8e354edceb&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fpic%2Fitem%2F7aec54e736d12f2e5f6707d94fc2d5628535682d.jpg",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3962878389,1899724775&fm=23&gp=0.jpg"};
    //与图标对应的Bitmap
    private Bitmap[] mImgIconBitmap;
    //盘区域的颜色  这里设置两种颜色交替
    private int[]  mSpanColor = new int[]{0XFFe4f5d5, 0XFF88b8b7, 0XFFa9e7e6,0XFFe4f5d5, 0XFF88b8b7, 0XFFa9e7e6,0XFFe4f5d5, 0XFF88b8b7, 0XFFa9e7e6, 0XFFe4f5d5, 0XFF88b8b7, 0XFFa9e7e6};
//    private int[]  mSpanColor = new int[]{0XFFF7F0DE, 0XFFFFFFFF, 0XFFF7F0DE, 0XFFFFFFFF, 0XFFF7F0DE, 0XFFFFFFFF, 0XFFF7F0DE, 0XFFFFFFFF,0XFFF7F0DE, 0XFFFFFFFF,0XFF0099ff};
    //盘的背景
    private Bitmap mSpanBackground = BitmapFactory.decodeResource(getResources(), R.mipmap.bg3);
    //转盘的直径
    private int mRadius;
    //设置的padding值，取一个padding值
    private int mPadding;
    //文字的大小  设置成可配置的属性 TODO
    private float mTextSize  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
    //盘的块数
    private int   mSpanCount = 12;
    //盘滚动的速度 默认为0
    private double mSpeed;
    //开始转动角度  可能会有多个线程访问  保证线程间的可见性
    private volatile float mStartSpanAngle = 0;
    //Span的中心
    private int     mCenter;
    //判断是否点击了停止旋转
    private boolean isSpanEnd;

    //开始按钮
    private Bitmap bmp;
    private TimerTask sPanTimerTask;
    private Timer bmpTimer;
    private TimerTask bmpTimerTask;

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void setmPrizeName(String[] mPrizeName) {
        this.mPrizeName = mPrizeName;
    }

    public void setmPrizeDesc(String[] mPrizeDesc) {
        this.mPrizeDesc = mPrizeDesc;
    }

    public void setmSpanColor(int[] mSpanColor) {
        this.mSpanColor = mSpanColor;
    }

    public void setmPrizeIcon2(String[] mPrizeIcon2) {
        this.mPrizeIcon2 = mPrizeIcon2;
    }

    public void setmSpanCount(int mSpanCount) {
        this.mSpanCount = mSpanCount;
        mImgIconBitmap = new Bitmap[mSpanCount];
    }

    public LuckSpan(Context context) {
        this(context, null);
    }

    public LuckSpan(Context context, AttributeSet attrs) {
        super(context, attrs);
        setZOrderOnTop(true);//使surfaceview放到最顶层

        getHolder().setFormat(PixelFormat.TRANSLUCENT);//使窗口支持透明度
        //初始化
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        //这是常亮
        setKeepScreenOn(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //直接控制Span为正方形
        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mPadding = getPaddingLeft();
        //直径
        mRadius = width - mPadding * 2;
        //设置中心点
        mCenter = width / 2;
        //设置成正方形
        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        //初始化绘制Span的画笔
        mSpanPaint = new Paint();
        mSpanPaint.setAntiAlias(true);
        mSpanPaint.setDither(true);
        //初始化绘制文本的画笔
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0Xff4d4d4d);
        //绘制圆环的画笔
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(0xff8dc9fb);

        if(null!= LoginUser.getInstantiation(getContext().getApplicationContext()).getLoginUser())
        {
            if(null!=LoginUser.getInstantiation(getContext().getApplicationContext()).getLoginUser().getSex())
            {
                String sex = LoginUser.getInstantiation(getContext().getApplicationContext()).getLoginUser().getSex();
                //默认是男生的
                if(sex.equals("美女"))
                {
                    bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.point);
                    mSpanBackground = BitmapFactory.decodeResource(getResources(), R.mipmap.iv_luckpan_girl_bg3);
                    mCirclePaint.setColor(0xffdf7acc);
                    mTextPaint.setColor(0xffffffff);
                }else if(sex.equals("帅哥"))
                {
                    bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.point);
                }else
                {
                    bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.iv_secrecy_point);
                    mSpanBackground = BitmapFactory.decodeResource(getResources(), R.mipmap.iv_luckpan_secrecy_bg3);
                    mCirclePaint.setColor(0xff7e1300);
                    mTextPaint.setColor(0xffffffff);
                }
            }
        }

        //初始化Span的范围
        mRectRange = new RectF(mPadding, mPadding, mPadding + mRadius, mPadding + mRadius);
        mRectCircleRange = new RectF(mPadding * 3 / 3, mPadding * 3 / 3, getMeasuredWidth() - mPadding * 3 / 3, getMeasuredWidth() - mPadding * 3 / 3);
        //初始化bitmap
        mImgIconBitmap = new Bitmap[mSpanCount];

        //将奖项的icon存储为Bitmap

        isRunning = true;
    }

    public void startCanvas()
    {
        mTimer = new Timer();
        sPanTimerTask = new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        };
        mTimer.scheduleAtFixedRate(sPanTimerTask,0,10);
    }

    public void startLoadBitmap()
    {
        bmpTimer = new Timer();
        bmpTimerTask = new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < mSpanCount; i++) {
                    if(null!=mPrizeIcon2[i])
                    {
                        if(!TextUtils.isEmpty(mPrizeIcon2[i]))
                        {
                            mImgIconBitmap[i] = returnBitmap(mPrizeIcon2[i]);
                        }
                    }
                }
            }
        };
        bmpTimer.scheduleAtFixedRate(bmpTimerTask,0,10);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //变化的时候
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //销毁的时候  关闭线程
        isRunning = false;
    }



    @Override
    public void run() {

            //在子线程中不断的绘制
    }

    public void stop()
    {
        isRunning = false;
        if(null!=bmpTimer)
        {
            bmpTimer.cancel();
        }

        if(null!=bmpTimerTask)
        {
            bmpTimerTask.cancel();
        }

        if(null!=mTimer)
        {
            mTimer.cancel();
        }

        if(null!=sPanTimerTask)
        {
            sPanTimerTask.cancel();
        }
    }

    private void draw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            if (null != mCanvas) {
                //避免执行到这里的时候程序已经退出 surfaceView已经销毁那么获取到canvas为null
                //绘制背景
                drawBg();

                if(isRunning)
                {
                    //绘制圆环
                    mCanvas.drawCircle(mCenter, mCenter, mRadius / 2 + mPadding / 20, mCirclePaint);
                }else
                {
                    Paint paint = new Paint();
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    mCanvas.drawPaint(paint);
                }

                drawSpan();

                if(isRunning)
                {
                    mCanvas.drawBitmap(bmp, null, new RectF(mCenter-86,mCenter-152, mCenter+86,mCenter+92), mSpanPaint);
                }else
                {
                    Paint paint = new Paint();
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    mCanvas.drawPaint(paint);
                }

            }
        } catch (Exception e) {
            //异常可以不必处理
        } finally {
            //一定要释放canvas避免泄露
            try {
                if(mSurfaceHolder!=null&&mCanvas!=null)
                {
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
            }catch (Exception e)
            {

            }
        }
    }

    //绘制盘块
    private void drawSpan() {
        float tempAngle = mStartSpanAngle;
        float sweepAngle = CIRCLE_ANGLE / mSpanCount;
        for (int i = 0; i < mSpanCount; i++) {

            if(isRunning)
            {
                mSpanPaint.setColor(mSpanColor[i]);
                mCanvas.drawArc(mRectCircleRange, tempAngle, sweepAngle, true, mSpanPaint);
                //绘制文字
                drawText(tempAngle, sweepAngle, mPrizeName[i], mPrizeDesc[i]);
                //绘制奖项Icon
                if(null!=mImgIconBitmap[i])
                {
                    drawPrizeIcon(tempAngle, mImgIconBitmap[i]);
                }
                //改变角度
                tempAngle += sweepAngle;
            }else
            {
                Paint paint = new Paint();
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                mCanvas.drawPaint(paint);
            }
        }
        //通过修改mSpeed的值让转盘有不同速度的转动
        mStartSpanAngle += mSpeed;
        if (isSpanEnd) {
            mSpeed -= 1;
        }
        if (mSpeed <= 0) {
            //停止旋转了
            mSpeed = 0;
            isSpanEnd = false;
            mSpanRollListener.onSpanRollListener(mSpeed);
        }
    }

    private void drawPrizeIcon(float tempAngle, Bitmap bitmap) {
        //图片的大小设置成直径的1/8
        int iconWidth = mRadius / 20;
        //根据角度计算icon中心点
        //角度计算
        double angle = (tempAngle + CIRCLE_ANGLE / mSpanCount / 2) * Math.PI / 180;
        //计算中心点
        int x = (int) (mCenter + mRadius / 3.5 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 3.5 * Math.sin(angle));

        //定义一个矩形 限制icon位置
        RectF rectF = new RectF(x - iconWidth, y - iconWidth, x + iconWidth, y + iconWidth);
        mCanvas.drawBitmap(bitmap, null, rectF, null);
    }

    //绘制文本
    private void drawText(float tempAngle, float sweepAngle, String text,String desc) {
        //绘制有弧度的文字 根据path绘制文字的路径
        Path path = new Path();
        path.addArc(mRectRange, tempAngle, sweepAngle);
        //让文字水平居中 那绘制文字的起点位子就是  弧度的一半 - 文字的一半
        float textWidth = mTextPaint.measureText(text);
        float hOval = (float) ((mRadius * Math.PI / mSpanCount / 2) - (textWidth / 2));

        float hOvalDesc = (float) ((mRadius * Math.PI / mSpanCount / 2) - (mTextPaint.measureText(desc) / 2));

        float vOval = mRadius / 15;//竖直偏移量可以自定义

        float vOvalDesc = mRadius / 8;//竖直偏移量可以自定义

        mCanvas.drawTextOnPath(desc, path, hOvalDesc, vOvalDesc, mTextPaint); //第三个四个参数是竖直和水平偏移量

        mCanvas.drawTextOnPath(text, path, hOval, vOval, mTextPaint); //第三个四个参数是竖直和水平偏移量
    }

    //绘制背景
    private void drawBg() {
        //背景设置为白色
        if (isRunning) {
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
            mCanvas.drawBitmap(mSpanBackground, null, new RectF(mPadding / 2, mPadding / 2, getMeasuredWidth() - mPadding / 2, getMeasuredHeight() - mPadding / 2), mSpanPaint);
        }else
        {
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mCanvas.drawPaint(paint);
        }
    }

    //网络请求成功后启动转盘
    public void luckyStart(int index) {
        //根据index控制停留的位置
        float angle = CIRCLE_ANGLE / mSpanCount;
        //计算指针停留在某个index下的角度范围
        float from = HALF_CIRCLE_ANGLE - (index - 1) * angle;
        float end = from + angle;

//        //设置需要停下来的时候转动的距离  保证每次不停留的某个index下的同一个位置
//        float targetFrom = 3 * CIRCLE_ANGLE + from;
//        float targetEnd = 3 * CIRCLE_ANGLE + end;//最终停下来的位置在from-end之间，4 * CIRCLE_ANGLE 自定义要多转几圈

        //计算要停留下来的时候速度的范围
        float vFrom = (float) ((Math.sqrt(1 + 8 * from) - 1) / 2);
        float vEnd = (float) ((Math.sqrt(1 + 8 * end) - 1) / 2);
        //在点击开始转动的时候 传递进来的index值就已经决定停留在那一项上面了
        mSpeed = vFrom + (vEnd - vFrom)/2;
        isSpanEnd = false;
    }

    //点击启动转盘
    public void defaultStart(int speed) {
        mSpeed = speed;
        isSpanEnd = false;
    }

    //停止转盘
    public void luckStop() {
        //在停止转盘的时候强制吧开始角度赋值为0  因为控制停留指定位置的角度计算是根据开始角度为0计算的
        mStartSpanAngle = 0;
        isSpanEnd = true;
    }

    //判断是否还在转动 true --还在转动 反之停止
    public boolean isStop() {
        return mSpeed == 0;
    }

    public void setOnSpanRollListener(SpanRollListener spanRollListener) {
        this.mSpanRollListener = spanRollListener;
    }

    public interface SpanRollListener {
        void onSpanRollListener(double speed);
    }


    /**
     * 根据图片的url路径获得Bitmap对象
     * @param url
     * @return
     */
    private Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(50000);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

}
