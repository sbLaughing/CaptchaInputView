package com.alien.captchainputview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.util.Stack;

/**
 * 描述:
 * <p>
 * Created by Alien on 2018/1/10.
 */

public class CaptchaInputView extends View {

    public static int[] ATTRS = new int[]{android.R.attr.textSize,android.R.attr.textColor};
    private static final String TAG = "CaptchaInputView";
    DisplayMetrics dm;

    @Mode
    private int mode = Mode.border;
    int contentLength = 6;
    int borderRadius = 0;
    int borderColor = Color.BLACK;
    int borderHighlightColor = borderColor;
    int borderWidth;
    int borderLength;
    int itemSpace;
    int textSize = 14;
    int textColor = Color.BLACK;
    boolean cipherEnable = true;
    boolean autoComplete = true;

    Paint borderPaint;
    Paint contentPaint;
    Stack<String> mStringStack = new Stack<>();
    RectF borderRect = new RectF();
    private InputMethodManager inputManager;
    private ICaptchaViewListener mICaptchaViewListener;




    public CaptchaInputView(Context context) {
        super(context);
        init(context, null);
    }

    public CaptchaInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CaptchaInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        resolveAttr(context, attrs);
        setFocusableInTouchMode(true);
        inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        setOnKeyListener(new KeyListener());

        borderPaint = new Paint();
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);

        contentPaint = new Paint();
        contentPaint.setTextAlign(Paint.Align.CENTER);
        contentPaint.setColor(textColor);
        contentPaint.setAntiAlias(true);
        contentPaint.setTextSize(textSize);

    }

    private void resolveAttr(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null)
            return;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CaptchaInputView, 0, 0);
        mode = typedArray.getInteger(R.styleable.CaptchaInputView_mode, mode);
        contentLength = typedArray.getInteger(R.styleable.CaptchaInputView_contentLength, contentLength);
        borderRadius = typedArray.getDimensionPixelOffset(R.styleable.CaptchaInputView_borderRadius, borderRadius);
        borderColor = typedArray.getColor(R.styleable.CaptchaInputView_borderColor, borderColor);
        borderHighlightColor = typedArray.getColor(R.styleable.CaptchaInputView_borderHighlightColor, borderColor);
        borderWidth = typedArray.getDimensionPixelOffset(R.styleable.CaptchaInputView_borderWidth, dp2px(1));
        borderLength = typedArray.getDimensionPixelOffset(R.styleable.CaptchaInputView_borderLength, dp2px(40));
        itemSpace = typedArray.getDimensionPixelOffset(R.styleable.CaptchaInputView_itemSpace, dp2px(12));
        cipherEnable = typedArray.getBoolean(R.styleable.CaptchaInputView_cipherEnable, cipherEnable);
        autoComplete = typedArray.getBoolean(R.styleable.CaptchaInputView_autoComplete, autoComplete);
        textSize = typedArray.getDimensionPixelSize(R.styleable.CaptchaInputView_textSize,textSize);
        textColor = typedArray.getColor(R.styleable.CaptchaInputView_textColor,borderColor);
        typedArray.recycle();


    }


    //borderLength：
    //在borderWidth较大的时候可以看出明显的线条边框，borderLength的值是指空白框的宽度+线条宽度*2=控件的高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = 0;
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                //没有指定大小，宽度 = 单个密码框大小 * 密码位数 + 密码框间距 *（密码位数 - 1）
                if (mode == Mode.border) width = borderLength*contentLength - (contentLength-1)*borderWidth;
                else width = borderLength * contentLength + itemSpace * (contentLength - 1);
                break;
            case MeasureSpec.EXACTLY:
                //指定大小，宽度 = 指定的大小
                width = MeasureSpec.getSize(widthMeasureSpec);
                //密码框大小 =  (宽度 - 密码框间距 *(密码位数 - 1)) / 密码位数
                if (mode == Mode.border) {
                    borderLength = (width - borderWidth * (contentLength + 1)) / contentLength + borderWidth * 2;
                } else if (mode == Mode.borderSparse) {
                    borderLength = (width - itemSpace * (contentLength - 1)) / contentLength;
                } else {
                    borderLength = (width - itemSpace * (contentLength - 1)) / contentLength;
                }
                break;
        }
        //以框的边长或者下划线长作为高度
        setMeasuredDimension(width, borderLength);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mode == Mode.underline) {
            drawUnderLine(canvas);
        } else if (mode == Mode.border) {
            drawBorder(canvas);
        } else {
            drawSparseRect(canvas);
        }

        Paint.FontMetricsInt fontMetrics = contentPaint.getFontMetricsInt();
        //中心线距baseline的距离，接下来我们求中心线就好了,注意top是小于0的所以用减号
        int baseline = (fontMetrics.bottom-fontMetrics.top )/2-fontMetrics.bottom;
        int centerY ;
        if (mode==Mode.underline){
            centerY = (getHeight()-borderWidth)/2;
        }else {
            centerY = getHeight()/2;
        }

        for (int i = 0; i < mStringStack.size(); i++) {
            if (cipherEnable) {
                canvas.drawCircle(getXCenterOffset(i),centerY,textSize/2,contentPaint);
            } else {
                String content = mStringStack.get(i);
                canvas.drawText(content,
                        getXCenterOffset(i),
                        centerY+baseline,
                        contentPaint);
            }
        }
    }

    private int getXCenterOffset(int index){
        if (mode==Mode.border){
            return borderLength/2 + index*(borderLength-borderWidth);
        }else{
            return borderLength / 2 + index * (borderLength + itemSpace);
        }
    }

    private void drawUnderLine(Canvas canvas) {
        int lengthUnit = itemSpace + borderLength;
        for (int i = 0; i < contentLength; i++) {
            if (i == mStringStack.size()) {
                borderPaint.setColor(borderHighlightColor);
            } else {
                borderPaint.setColor(borderColor);
            }
            borderPaint.setStrokeWidth(borderWidth);
            canvas.drawLine(i * lengthUnit, getHeight() - borderWidth / 2, i * lengthUnit + borderLength, getHeight() - borderWidth / 2, borderPaint);
        }


    }

    private void drawBorder(Canvas canvas) {
        borderRect.set(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth / 2, getHeight() - borderWidth / 2);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        if (borderRadius > 0)
            canvas.drawRoundRect(borderRect, borderRadius, borderRadius, borderPaint);
        else canvas.drawRect(borderRect, borderPaint);

        borderPaint.setStyle(Paint.Style.FILL);
        for (int i = 1; i < contentLength; i++) {
            int x = (int) (i * borderLength - borderWidth * (i - 0.5f));
            canvas.drawLine(x, 0, x, getHeight(), borderPaint);
        }

//        if (mStringStack.size()>0){
//            borderPaint.setStyle(Paint.Style.STROKE);
//            borderPaint.setColor(borderHighlightColor);
//            int index = mStringStack.size()-1;
//            int highlightLeft = index*(borderLength-borderWidth)+borderWidth/2;
//            int highlightRight = highlightLeft+borderLength-borderWidth;
//            if (borderRadius > 0 ) {
//                canvas.drawRoundRect(highlightLeft,borderWidth/2,highlightRight,getHeight()-borderWidth/2,borderRadius,borderRadius,borderPaint);
//                canvas.drawLine(highlightLeft,0,highlightLeft,getHeight(),borderPaint);
//            }else {
//                canvas.drawRect(highlightLeft,borderWidth/2,highlightRight,getHeight()-borderWidth/2,borderPaint);
//            }
//        }


    }

    private void drawSparseRect(Canvas canvas) {
        int lengthUnit = itemSpace + borderLength;
        for (int i = 0; i < contentLength; i++) {
            borderPaint.setStrokeWidth(borderWidth);
            if (i == mStringStack.size()) {
                borderPaint.setColor(borderHighlightColor);
            } else {
                borderPaint.setColor(borderColor);
            }
            int startX = i * lengthUnit + borderWidth / 2;
            int endX = startX + (borderLength - borderWidth);
            borderRect.set(startX, borderWidth / 2, endX, getHeight() - borderWidth / 2);
            if (borderRadius > 0) {
                canvas.drawRoundRect(borderRect, borderRadius, borderRadius, borderPaint);
            } else {
                canvas.drawRect(borderRect, borderPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            requestFocus();
            inputManager.showSoftInput(this, InputMethodManager.SHOW_FORCED);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            inputManager.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;
        return super.onCreateInputConnection(outAttrs);
    }

    public String getContent(){
        if (mStringStack.empty()) return "";
        else {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i=0;i<mStringStack.size();i++){
                stringBuffer.append(mStringStack.get(i));
            }
            return stringBuffer.toString();
        }
    }

    public boolean isComplete(){
        return mStringStack.size()==contentLength;
    }

    public void setICaptchaViewListener(ICaptchaViewListener ICaptchaViewListener) {
        mICaptchaViewListener = ICaptchaViewListener;
    }

    class KeyListener implements OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            int action = event.getAction();
            if (action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (mStringStack.empty()) {
                        return true;
                    }
                    mStringStack.pop();
                    if (mICaptchaViewListener != null) {
                        mICaptchaViewListener.onContentChanged(getContent());
                    }
                    postInvalidate();
                    return true;
                }
                if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
                    if (isComplete()) {
                        return true;
                    }
                    mStringStack.push(String.valueOf(keyCode - 7));
                    String result = getContent();
                    if (mICaptchaViewListener != null) {
                        mICaptchaViewListener.onContentChanged(result);
                        if (isComplete()&&autoComplete){
                            mICaptchaViewListener.onComplete(result);
                        }
                    }
                    postInvalidate();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (mICaptchaViewListener != null&&isComplete()) {
                        mICaptchaViewListener.onComplete(getContent());
                    }
                    return true;
                }
            }
            return false;
        }
    }

    protected int dp2px(float dpValue) {
        if (dm == null) {
            dm = new DisplayMetrics();
            ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealMetrics(dm);
        }

        return (int) (dpValue * dm.density + 0.5f);
    }

}
