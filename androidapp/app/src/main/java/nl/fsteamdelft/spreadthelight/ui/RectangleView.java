package nl.fsteamdelft.spreadthelight.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.ColorInt;

import static nl.fsteamdelft.spreadthelight.morse.MorseImageAnalyzer.getAreaOfIntrest;

public class RectangleView extends View {

    private ShapeDrawable drawable;

    public RectangleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        drawable = new ShapeDrawable(new RectShape());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Rect aoi = getAreaOfIntrest(new Rect(0, 0, w, h));
        drawable.getPaint().setColor(Color.parseColor("white"));
        drawable.getPaint().setStrokeWidth(3);
        drawable.getPaint().setStyle(Paint.Style.STROKE);
        drawable.setBounds(aoi);
    }

    protected void onDraw(Canvas canvas) {
        drawable.draw(canvas);
    }
}