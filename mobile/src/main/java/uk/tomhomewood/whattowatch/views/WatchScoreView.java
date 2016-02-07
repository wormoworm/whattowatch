package uk.tomhomewood.whattowatch.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import uk.tomhomewood.whattowatch.R;
import uk.tomhomewood.whattowatch.utils.ResourceUtils;

/**
 *
 */
public class WatchScoreView extends TextView {

    private static final float PADDING_DP = 8;
    private static final float ICON_MARGIN_RIGHT_DP = 8;

    private static final String SCORE_FORMAT = "%d";

    private static final int WATCH_SCORE_DEMO = 88;

    public WatchScoreView(Context context) {
        super(context);
        initialise(context);
    }

    public WatchScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise(context);
    }

    private void initialise(Context context) {
        int paddingPx = ResourceUtils.convertDpToPixels(PADDING_DP, context);
        setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_watch_title_white, 0, 0, 0);
        setCompoundDrawablePadding(ResourceUtils.convertDpToPixels(ICON_MARGIN_RIGHT_DP, context));
        setBackgroundResource(R.drawable.watch_score_view_background);
        setTextAppearance(context, android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Title);
        setTextColor(context.getResources().getColor(R.color.white));
        if(isInEditMode()) setWatchScore(WATCH_SCORE_DEMO);
    }

    public void setWatchScore(int watchScore) {
        setText(String.format(SCORE_FORMAT, watchScore));
    }
}