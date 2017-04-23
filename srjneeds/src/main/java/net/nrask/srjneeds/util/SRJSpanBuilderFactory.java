package net.nrask.srjneeds.util;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;

/**
 * Created by Sebastian Rask on 23-04-2017.
 */

public class SRJSpanBuilderFactory {
	private SpannableStringBuilder spanBuilder;
	private int currentStart = 0, currentEnd = 0;

	public SRJSpanBuilderFactory(String initialString) {
		spanBuilder = new SpannableStringBuilder();
		append(initialString);
	}

	public SRJSpanBuilderFactory append(String stringToAppend) {
		spanBuilder.append(stringToAppend);
		currentEnd = spanBuilder.length();
		currentStart = currentEnd - stringToAppend.length();
		return this;
	}

	public SRJSpanBuilderFactory withClickable(ClickableSpan span) {
		spanBuilder.setSpan(span, currentStart, currentEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return this;
	}

	public SRJSpanBuilderFactory withColor(@ColorRes int color, Context context) {
		return withColor(ContextCompat.getColor(context, color));
	}

	public SRJSpanBuilderFactory withColor(@ColorInt int color) {
		setSpan(new ForegroundColorSpan(color));
		return this;
	}

	public SRJSpanBuilderFactory withFontFamily(String fontFamily) {
		setSpan(new TypefaceSpan(fontFamily));
		return this;
	}

	public SRJSpanBuilderFactory withFontFamily(@StringRes int stringRes, Context context) {
		return withFontFamily(context.getString(stringRes));
	}

	public SRJSpanBuilderFactory withFontStyle(@StyleRes int styleRes) {
		setSpan(new StyleSpan(styleRes));
		return this;
	}

	public SRJSpanBuilderFactory withSize(int sizeInDp) {
		setSpan(new AbsoluteSizeSpan(sizeInDp, true));
		return this;
	}

	public void setSpan(ParcelableSpan span) {
		spanBuilder.setSpan(span, currentStart, currentEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	public SpannableStringBuilder build() {
		return spanBuilder;
	}
}
