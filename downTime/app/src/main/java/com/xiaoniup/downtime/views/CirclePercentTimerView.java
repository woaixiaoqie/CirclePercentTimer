package com.xiaoniup.downtime.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

import com.xiaoniup.downtime.R;


/**
 * Created by Administrator on 2017/11/8.
 */
public class CirclePercentTimerView extends View {

    //圆的半径
    private float mRadius;

    //色带的宽度
    private float mStripeWidth;

    private float mMoveWidth;
    private float mMoveWidth2;
    private float mMoveWidth3;

    //总体大小
    private int mHeight;
    private int mWidth;

    //动画位置百分比进度
    private float mCurPercent;

    //圆心坐标
    private float x;
    private float y;

    //要画的弧度(角度)
    private float mEndAngle;
    private double mEndAnglePai;

    //饼状图的颜色
    private int mTorusColor;
    //小圆的颜色
    private int mSmallColor;
    //大圆颜色
    private int mBigColor;

    //图片宽高
    private int mImgWidthHeight;

    //中心百分比文字大小
    private float mCenterTextSize;
    private float mCenterTextSize2;

    //中心百分比文字颜色
    private int mCenterTextColor;
    private int mCenterTextColor2;

    public CirclePercentTimerView(Context context) {
        this(context, null);
    }

    public CirclePercentTimerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView, defStyleAttr, 0);
        mStripeWidth = a.getDimension(R.styleable.CirclePercentView_stripeWidth, PxUtils.dpToPx(8, context));
        mCurPercent = a.getFloat(R.styleable.CirclePercentView_percent, 0);
        mTorusColor = a.getColor(R.styleable.CirclePercentView_torusColor, 0xffdaf1ac);
        mSmallColor = a.getColor(R.styleable.CirclePercentView_smallColor, 0xffffffff);
        mBigColor = a.getColor(R.styleable.CirclePercentView_bigColor, 0xff7eaf1b);

        mCenterTextColor = a.getColor(R.styleable.CirclePercentView_textColor, 0xffff0000);
        mCenterTextColor2 = a.getColor(R.styleable.CirclePercentView_textColor2, 0xff333333);

        mCenterTextSize = a.getDimensionPixelSize(R.styleable.CirclePercentView_centerTextSize, PxUtils.spToPx(19,context));
        mCenterTextSize2 = a.getDimensionPixelSize(R.styleable.CirclePercentView_centerTextSize2, PxUtils.spToPx(9,context));
        mRadius = a.getDimensionPixelSize(R.styleable.CirclePercentView_radius, PxUtils.dpToPx(86,context));

        mImgWidthHeight = a.getDimensionPixelSize(R.styleable.CirclePercentView_widthheight, PxUtils.dpToPx(19,context));
        mMoveWidth = a.getDimensionPixelSize(R.styleable.CirclePercentView_movewidth, PxUtils.dpToPx(5,context));

        mMoveWidth2 = a.getDimensionPixelSize(R.styleable.CirclePercentView_movewidth2, PxUtils.dpToPx(1,context));
        mMoveWidth3 = a.getDimensionPixelSize(R.styleable.CirclePercentView_movewidth3, PxUtils.dpToPx(13,context));


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取测量大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            mRadius = widthSize / 2;
            x = widthSize / 2;
            y = heightSize / 2;
            mWidth = widthSize;
            mHeight = heightSize;
        }

        if(widthMode == MeasureSpec.AT_MOST&&heightMode == MeasureSpec.AT_MOST){
            mWidth = (int) (mRadius*2);
            mHeight = (int) (mRadius*2);
            x = mRadius;
            y = mRadius;
        }
        mRadius = mRadius - mMoveWidth;

        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mEndAngle = (float) (mCurPercent * 3.6);//计算度数（旋转角度需要）
        mEndAnglePai = mCurPercent * 0.02 * Math.PI;//计算弧度数（三角函数需要）

        //绘制大圆
        Paint bigCirclePaint = new Paint();
        bigCirclePaint.setAntiAlias(true);
        bigCirclePaint.setColor(mBigColor);
        canvas.drawCircle(x, y, mRadius, bigCirclePaint);

        //饼状图
        Paint sectorPaint = new Paint();
        sectorPaint.setColor(mTorusColor);
        sectorPaint.setAntiAlias(true);
        RectF rect = new RectF(x-mRadius, y-mRadius, x+mRadius, y+mRadius);
        canvas.drawArc(rect, 270, mEndAngle, true, sectorPaint);


        //绘制小圆,颜色透明
        Paint smallCirclePaint = new Paint();
        smallCirclePaint.setAntiAlias(true);
        smallCirclePaint.setColor(mSmallColor);
        canvas.drawCircle(x, y, mRadius - mStripeWidth, smallCirclePaint);


        if (mCurPercent < 100) {
            //绘制文本
            Paint textPaint = new Paint();
            String text = toFormatTime(mTime);
//            String text = mEstimate_delivery_time;//设置显示的时间为预计的送达时间
            textPaint.setTextSize(mCenterTextSize);
            float textLength = textPaint.measureText(text);
            textPaint.setColor(mCenterTextColor);
            canvas.drawText(text, x-textLength/2, y-mMoveWidth2, textPaint);

            //绘制文本2
            Paint textPaint2 = new Paint();
//            String text2 = "送达倒计时";
            String text2 = "预计送达时间";
            textPaint2.setTextSize(mCenterTextSize2);
            float textLength2 = textPaint2.measureText(text2);
            textPaint2.setColor(mCenterTextColor2);
            canvas.drawText(text2, x - textLength2/2, y+mMoveWidth3, textPaint2);
        } else {
            //绘制文本3
            Paint textPaint3 = new Paint();
            String text3 = "请刷新";
            textPaint3.setTextSize(mCenterTextSize);
            float textLength3 = textPaint3.measureText(text3);
            textPaint3.setColor(mCenterTextColor);
            canvas.drawText(text3, x - textLength3/2, y + mMoveWidth, textPaint3);
        }

        //绘制图片
        Paint imgPaint = new Paint();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.deadling_time_circle_img);
        bitmap = zoomImg(bitmap, mImgWidthHeight);
        canvas.drawBitmap(bitmap, (float)((mRadius-mStripeWidth/2) * Math.sin(mEndAnglePai)) + x-mImgWidthHeight/2, (float)((mRadius-mStripeWidth/2) * Math.cos(mEndAnglePai)*(-1.0)) + y-mImgWidthHeight/2, imgPaint);

    }

    /**
     *  处理图片
     * @param bm 所要转换的bitmap
     * @param newWidthHeight 新的宽高
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidthHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidthHeight) / width;
        float scaleHeight = ((float) newWidthHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public String toFormatTime(long time) {
        if (time >= 0) {
            long minute = time % 3600 / 60;// 分钟
            long second = time % 60;// 秒
            return format(minute) + ":" + format(second);
        } else {//超时
            return "";
        }
    }

    public String format(long aa) {
        if (aa < 10) {
            return "0" + aa;
        } else {
            return "" + aa;
        }
    }


    //剩余时间
    private long mTime = 0;

    //设置剩余时间
    public void setPercent(long time, String estimate_delivery_time) {
        mTime = time;
        mEstimate_delivery_time = estimate_delivery_time;
        mCurPercent = (float) (100 - (time * 100.0 /mTotal_Time));
        if (mCurPercent > 100) {
            throw new IllegalArgumentException("percent must less than 100!");
        }

        //内部设置百分比 用于动画效果
        CirclePercentTimerView.this.postInvalidate();
    }

    private LimitTimer mTimer;
    private double mTotal_Time = 0;
    private String mEstimate_delivery_time = "";
    private LimitTimeListener listener=null;


    /**
     * @param endTime  外部时间数秒数,倒计时的时间戳(s)
     * @param total_time  总时间戳(s)
     * @param estimate_delivery_time  预计送达时间
     */
    public void initLeftTime(long endTime, double total_time, String estimate_delivery_time) {
        mTotal_Time = total_time;
        mEstimate_delivery_time = estimate_delivery_time;

        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
        mTimer = new LimitTimer(endTime * 1000, 1000);
        mTimer.start();
    }

    /**
     * 如果该控件使用在碎片中，返回时，则最好还是要stop
     */
    public void stopTimeCount() {
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private class LimitTimer extends CountDownTimer {

        public LimitTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long totalSecond = millisUntilFinished / 1000;
            setPercent(totalSecond, mEstimate_delivery_time);
            if (listener != null) {
                listener.onLeftTime(totalSecond);
            }
        }
        @Override
        public void onFinish() {
            setPercent(0, mEstimate_delivery_time);
            if (listener!=null){
                listener.onTimeOver(true);
            }
        }
    }

    //倒计时结束监听
    public void setOnLimitTimeListener(LimitTimeListener listener) {
        this.listener = listener;
    }
    public interface LimitTimeListener {
        //是否倒计时完毕
        void onTimeOver(boolean flag);
        //剩余时间
        void onLeftTime(long leftTime);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

}
