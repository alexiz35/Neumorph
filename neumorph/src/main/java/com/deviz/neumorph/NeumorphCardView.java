package com.deviz.neumorph;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


/**
 *
 */
public class NeumorphCardView extends FrameLayout {
    public static final String TAG = "NeuMorph";
    private Paint backgroundPaint;
    private Paint lightShadowPaint;
    private Paint darkShadowPaint;

    private float cornerRadius = 30f;
    private float shadowRadius = 20f;
    private float shadowOffset = 5f;
    private int backgroundColor = ContextCompat.getColor(getContext(),R.color.background_dark);
    private int lightShadowColor = ContextCompat.getColor(getContext(),R.color.dark_gray);
    private int darkShadowColor = ContextCompat.getColor(getContext(),R.color.black);

    protected boolean isPressed = false;
    private boolean switchStyle = false;
    private boolean pressable = false;

    private Bitmap shadowBitmap;

    public NeumorphCardView(Context context) {
        super(context);
        init(null);
    }

    public NeumorphCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NeumorphCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        setWillNotDraw(false);

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.NeumorphCardView);
            try {
                backgroundColor = ta.getColor(R.styleable.NeumorphCardView_neu_backgroundColor, backgroundColor);
                cornerRadius = ta.getDimension(R.styleable.NeumorphCardView_neu_cornerRadius, cornerRadius);
                shadowRadius = ta.getDimension(R.styleable.NeumorphCardView_neu_shadowRadius, shadowRadius);
                shadowOffset = ta.getDimension(R.styleable.NeumorphCardView_neu_shadowOffset, shadowOffset);
                isPressed = ta.getBoolean(R.styleable.NeumorphCardView_neu_pressed, false);
                switchStyle = ta.getBoolean(R.styleable.NeumorphCardView_neu_switchStyle, false);
                pressable = ta.getBoolean(R.styleable.NeumorphCardView_neu_pressable, false);
                lightShadowColor = ta.getColor(R.styleable.NeumorphCardView_neu_lightShadowColor, lightShadowColor);
                darkShadowColor = ta.getColor(R.styleable.NeumorphCardView_neu_darkShadowColor, darkShadowColor);
            } finally {
                ta.recycle();
            }
        }

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(backgroundColor);

        lightShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        darkShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();

        // Проверка перед рисованием
        if (width <= 0 || height <= 0) {
            // Размеров нет — пропускаем отрисовку
            return;
        }

        // ПРОВЕРКА: bitmap должен быть корректного размера
        if (shadowBitmap == null ||
                shadowBitmap.getWidth() != (int) width ||
                shadowBitmap.getHeight() != (int) height) {
            createShadowBitmap();
        }

        canvas.drawBitmap(shadowBitmap,0,0,null);
        canvas.drawRoundRect(shadowRadius, shadowRadius, width - shadowRadius, height - shadowRadius, cornerRadius, cornerRadius, backgroundPaint);
    }

    private void createShadowBitmap() {
        int width = getWidth();
        int height = getHeight();

        if (width <= 0 || height <= 0) return;

        if (shadowBitmap != null) {
            shadowBitmap.recycle();
        }

        shadowBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas shadowCanvas = new Canvas(shadowBitmap);

        if (isPressed) {
            drawInnerShadow(shadowCanvas, width, height);
        } else {
            drawOuterShadow(shadowCanvas, width, height);
        }
    }

    private void drawOuterShadow(Canvas canvas, float width, float height) {

        lightShadowPaint.setShadowLayer(shadowRadius, -shadowOffset, -shadowOffset, lightShadowColor);
        darkShadowPaint.setShadowLayer(shadowRadius, shadowOffset, shadowOffset, darkShadowColor);

        canvas.drawRoundRect(shadowRadius, shadowRadius, width - shadowRadius, height - shadowRadius, cornerRadius, cornerRadius, lightShadowPaint);
        canvas.drawRoundRect(shadowRadius, shadowRadius, width - shadowRadius, height - shadowRadius, cornerRadius, cornerRadius, darkShadowPaint);
    }



    /*
    // вариант отрисовки теней
    private void drawOuterShadow(Canvas canvas, float width, float height) {
        float shadowOffset = -5;

        // Настройка размытия
        BlurMaskFilter blurFilter = new BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.NORMAL);

        // Светлая тень — снизу/справа
        lightShadowPaint.setMaskFilter(blurFilter);
        lightShadowPaint.setColor(lightShadowColor);
        lightShadowPaint.setStyle(Paint.Style.FILL);

        // Тёмная тень — сверху/слева
        darkShadowPaint.setMaskFilter(blurFilter);
        darkShadowPaint.setColor(darkShadowColor);
        darkShadowPaint.setStyle(Paint.Style.FILL);

        setLayerType(LAYER_TYPE_SOFTWARE, lightShadowPaint);

        RectF rect = new RectF(
                shadowRadius, shadowRadius,
                width - shadowRadius, height - shadowRadius
        );

        // Тёмная тень (верх / влево)
        canvas.save();
        canvas.translate(-shadowOffset, -shadowOffset);
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, darkShadowPaint);
        canvas.restore();

        // Светлая тень (низ / вправо)
        canvas.save();
        canvas.translate(shadowOffset, shadowOffset);
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, lightShadowPaint);
        canvas.restore();
    }*/



    private void drawInnerShadow(Canvas canvas, float width, float height) {
        lightShadowPaint.setShadowLayer(shadowRadius, shadowOffset, shadowOffset, lightShadowColor);
        darkShadowPaint.setShadowLayer(shadowRadius, -shadowOffset, -shadowOffset, darkShadowColor);

        canvas.drawRoundRect(shadowRadius, shadowRadius, width - shadowRadius, height - shadowRadius, cornerRadius, cornerRadius, darkShadowPaint);
        canvas.drawRoundRect(shadowRadius, shadowRadius, width - shadowRadius, height - shadowRadius, cornerRadius, cornerRadius, lightShadowPaint);
    }



    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void toggleState(){
        isPressed = !isPressed;
        createShadowBitmap();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        if (!isEnabled() || !pressable) return false;

        switch (event.getAction()) {
            case android.view.MotionEvent.ACTION_DOWN:
                toggleState();
                setPressed(true);
                break;
            case android.view.MotionEvent.ACTION_UP:
            case android.view.MotionEvent.ACTION_CANCEL:
                if (!switchStyle) toggleState();
                setPressed(false);
                performClick();
                break;
        }
        return /*super.onTouchEvent(event);*/ true;

    }


    public void setPressedState(boolean pressed) {
        if (this.isPressed != pressed) {
            this.isPressed = pressed;
            createShadowBitmap(); // пересоздать тени при смене состояния
            invalidate();
        }
    }

    public boolean getPressedState(){
        return isPressed;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            createShadowBitmap(); // пересоздать тени при смене размера
            invalidate(); // Перерисовать с новыми размерами
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (shadowBitmap != null) {
            shadowBitmap.recycle();
            shadowBitmap = null;
        }
    }

}



