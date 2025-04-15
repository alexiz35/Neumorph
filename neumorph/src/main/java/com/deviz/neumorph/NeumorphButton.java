package com.deviz.neumorph;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


public class NeumorphButton extends NeumorphCardView {

    private ImageView imageView;
    private TextView textView;
    private Drawable icon,secondaryIcon;
    private int primaryIconColor, secondaryIconColor, primaryTextColor, secondaryTextColor;


    public NeumorphButton(Context context) {
        super(context);
        init(context,null);
    }

    public NeumorphButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public NeumorphButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        setClickable(true);
        setFocusable(true);

        // Layout для содержимого кнопки
        LinearLayout layout = new LinearLayout(context);
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
        ));

        imageView = new ImageView(context);
        textView = new TextView(context);

        // Стартовые параметры (можешь подогнать стили под свой вкус)
        imageView.setVisibility(GONE);
        textView.setVisibility(GONE);
        textView.setGravity(Gravity.CENTER_VERTICAL);

        layout.addView(imageView);
        layout.addView(textView);

        addView(layout);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NeumorphButton);
            String text = ta.getString(R.styleable.NeumorphButton_neu_ButtonText);
            primaryTextColor = ta.getColor(R.styleable.NeumorphButton_neu_primaryColorText, Color.WHITE);
            secondaryTextColor = ta.getColor(R.styleable.NeumorphButton_neu_secondaryColorText, primaryTextColor);
            float textSize = ta.getDimension(R.styleable.NeumorphButton_neu_sizeText,spToPx(16));

            icon = ta.getDrawable(R.styleable.NeumorphButton_neu_ButtonIcon);
            secondaryIcon = ta.getDrawable(R.styleable.NeumorphButton_neu_SecondaryButtonIcon);
            primaryIconColor = ta.getColor(R.styleable.NeumorphButton_neu_primaryColorIcon, Color.WHITE);
            secondaryIconColor = ta.getColor(R.styleable.NeumorphButton_neu_secondaryColorIcon, primaryIconColor);
            float sizeIcon = ta.getDimension(R.styleable.NeumorphButton_neu_sizeIcon,dpToPx(24));

            int innerPadding = ta.getDimensionPixelSize(R.styleable.NeumorphButton_neu_innerPadding, 0);

            ta.recycle();

            if (text != null) {
                setText(text);
                setSizeText(textSize);
                setColorText(primaryTextColor);
            }
            if (icon != null) {
                setIcon(icon);
                setIconTint(primaryIconColor);
                setIconSize(sizeIcon);
            }

            layout.setPadding(innerPadding,innerPadding,innerPadding,innerPadding);

        }
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
                                        );
    }

    private float spToPx(float sp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                getResources().getDisplayMetrics()
                                        );
    }



    public void setText(String text) {
        textView.setText(text);
        textView.setVisibility(VISIBLE);
    }
    public void setSizeText(float sizeText) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,sizeText);
    }
    public void setColorText(int colorText) {
        textView.setTextColor(colorText);
    }



    public void setIcon(Drawable drawable) {
        imageView.setImageDrawable(drawable);
        imageView.setVisibility(VISIBLE);
    }

    private void setIconTint(int color) {
        // И установка цвета, если нужно
        imageView.setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    public void setIconSize(float sizePx) {
        int size = Math.round(sizePx);

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        if (params == null) {
            params = new LinearLayout.LayoutParams(size, size);
        } else {
            params.width = size;
            params.height = size;
        }

        imageView.setLayoutParams(params);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);  // сохранить результат
        updateIconState();
        return result;  // вернуть реальный результат родителя
    }

    private void updateIconState() {
        if (icon==null) return;
        if (secondaryIcon==null) secondaryIcon = icon;
        if (isPressed) {
            // Если кнопка нажата — установить вторичную иконку и цвет
            setIcon(secondaryIcon);
            setIconTint(secondaryIconColor);
            setColorText(secondaryTextColor);
        } else {
            // Если кнопка отпущена — установить основную иконку и цвет
            setIcon(icon);
            setIconTint(primaryIconColor);
            setColorText(primaryTextColor);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

}
