package com.example.teacoffee.myDB;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public class SimpleBarChart extends View {

    private final List<BarData> data = new ArrayList<>();
    private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint valuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF barRect = new RectF();

    private float animatedFraction = 0f;
    private int maxValue = 100;

    // Màu gradient đẹp (8 màu lặp lại)
    private final int[] colorsStart = {
            Color.parseColor("#FF6B6B"), Color.parseColor("#4ECDC4"), Color.parseColor("#45B7D1"),
            Color.parseColor("#96CEB4"), Color.parseColor("#FECA57"), Color.parseColor("#DDA0DD"),
            Color.parseColor("#98D8C8"), Color.parseColor("#F7B731")
    };
    private final int[] colorsEnd = {
            Color.parseColor("#FF8E8E"), Color.parseColor("#72E0D7"), Color.parseColor("#6DC9E0"),
            Color.parseColor("#B8E0D0"), Color.parseColor("#FFD27D"), Color.parseColor("#E6B8E6"),
            Color.parseColor("#B8E6DC"), Color.parseColor("#F9CB5C")
    };

    public SimpleBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        shadowPaint.setColor(Color.parseColor("#2A000000"));
        shadowPaint.setStyle(Paint.Style.FILL);

        valuePaint.setColor(Color.WHITE);
        valuePaint.setTextSize(spToPx(14));
        valuePaint.setTextAlign(Paint.Align.CENTER);
        valuePaint.setFakeBoldText(true);

        labelPaint.setColor(Color.parseColor("#444444"));
        labelPaint.setTextSize(spToPx(11));
        labelPaint.setTextAlign(Paint.Align.CENTER);

        gridPaint.setColor(Color.parseColor("#EEEEEE"));
        gridPaint.setStrokeWidth(2f);
    }

    public void setData(List<BarData> list) {
        data.clear();
        data.addAll(list);
        if (!list.isEmpty()) {
            maxValue = list.stream().mapToInt(d -> d.value).max().orElse(100);
            maxValue = Math.max(maxValue, 10);
        }

        // Animation mượt 800ms
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 1f);
        anim.setDuration(800);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.addUpdateListener(a -> {
            animatedFraction = (float) a.getAnimatedValue();
            invalidate();
        });
        anim.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (data.isEmpty()) {
            valuePaint.setColor(Color.GRAY);
            valuePaint.setTextSize(spToPx(16));
            canvas.drawText("Không có dữ liệu", getWidth() / 2f, getHeight() / 2f, valuePaint);
            return;
        }

        float paddingLeft   = dpToPx(30);
        float paddingRight  = dpToPx(30);
        float paddingTop    = dpToPx(60);
        float paddingBottom = dpToPx(80);

        float availableWidth = getWidth() - paddingLeft - paddingRight;
        float availableHeight = getHeight() - paddingTop - paddingBottom;

        int itemCount = data.size();

        // Tự động tính độ rộng cột + khoảng cách sao cho vừa màn hình
        float totalSpace = dpToPx(30); // khoảng cách giữa các cột (cố định)
        float barWidth = (availableWidth - (itemCount - 1) * totalSpace) / itemCount;
        barWidth = Math.max(barWidth, dpToPx(20)); // không nhỏ hơn 20dp → vẫn đọc được

        // Vẽ lưới ngang nhẹ (5 dòng)
        for (int i = 0; i <= 5; i++) {
            float y = paddingTop + i * (availableHeight / 5f);
            canvas.drawLine(paddingLeft, y, getWidth() - paddingRight, y, gridPaint);
        }

        // Vẽ từng cột
        for (int i = 0; i < itemCount; i++) {
            BarData item = data.get(i);
            float left = paddingLeft + i * (barWidth + totalSpace);
            float centerX = left + barWidth / 2f;

            float barHeight = (item.value / (float) maxValue) * availableHeight * animatedFraction;
            float top = getHeight() - paddingBottom - barHeight;

            // Shadow
            barRect.set(left + 6, top + 6, left + barWidth + 6, getHeight() - paddingBottom + 6);
            canvas.drawRoundRect(barRect, dpToPx(12), dpToPx(12), shadowPaint);

            // Cột chính với gradient
            barRect.set(left, top, left + barWidth, getHeight() - paddingBottom);
            LinearGradient gradient = new LinearGradient(0, top, 0, getHeight() - paddingBottom,
                    colorsStart[i % colorsStart.length],
                    colorsEnd[i % colorsEnd.length],
                    Shader.TileMode.CLAMP);
            barPaint.setShader(gradient);
            canvas.drawRoundRect(barRect, dpToPx(12), dpToPx(12), barPaint);

            // === SỐ LƯỢNG - LUÔN HIỆN RÕ TRÊN ĐẦU MỖI CỘT ===
            String valueText = String.valueOf(item.value);
            valuePaint.setTextSize(spToPx(14));
            valuePaint.setFakeBoldText(true);
            valuePaint.setColor(Color.WHITE);

            // Đo kích thước chữ để vẽ nền
            float textWidth = valuePaint.measureText(valueText);
            float textHeight = valuePaint.getTextSize();

            // Nền đen mờ bo tròn (đẹp như Shopee, Grab)
            Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bgPaint.setColor(Color.parseColor("#80000000")); // đen mờ 50%
            RectF bgRect = new RectF(
                    centerX - textWidth / 2 - dpToPx(10),
                    top - dpToPx(40),
                    centerX + textWidth / 2 + dpToPx(10),
                    top - dpToPx(40) + textHeight + dpToPx(12)
            );
            canvas.drawRoundRect(bgRect, dpToPx(20), dpToPx(20), bgPaint);

            // Vẽ số ngay trên nền
            canvas.drawText(valueText, centerX, top - dpToPx(16), valuePaint);

            // === TÊN MÓN (giữ nguyên tự động thu gọn) ===
            String name = item.name;
            labelPaint.setTextSize(spToPx(11));
            if (labelPaint.measureText(name) > barWidth * 1.1f) {
                labelPaint.setTextSize(spToPx(9));
            }
            if (labelPaint.measureText(name) > barWidth * 1.1f) {
                name = name.length() > 10 ? name.substring(0, 8) + "..." : name;
            }
            canvas.drawText(name, centerX, getHeight() - dpToPx(26), labelPaint);
        }

        // Tiêu đề
        Paint titlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setColor(Color.parseColor("#2c3e50"));
        titlePaint.setTextSize(spToPx(18));
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("SỐ LƯỢNG MÓN BÁN RA", getWidth() / 2f, dpToPx(36), titlePaint);
    }

    public static class BarData {
        public String name;
        public int value;
        public BarData(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    private float dpToPx(float dp) {
        return dp * getContext().getResources().getDisplayMetrics().density;
    }

    private float spToPx(float sp) {
        return sp * getContext().getResources().getDisplayMetrics().scaledDensity;
    }
}