package uk.tomhomewood.whattowatch.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;

import java.util.concurrent.atomic.AtomicInteger;

public class ResourceUtils {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int getColourFromAttribute(Context context, int colourAttribute) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{colourAttribute});
        int color = a.getColor(0, context.getResources().getColor(android.R.color.darker_gray));
        a.recycle();
        return color;
    }

    public static int convertDpToPixels(float dps, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5F);
    }

    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}