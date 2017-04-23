package net.nrask.srjneeds.util;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.util.TypedValue;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Sebastian Rask on 22-04-2017.
 * Based on Plaid ColorUtils https://github.com/nickbutcher/plaid
 */

public class ColorUtil {
	public static final int IS_LIGHT = 0;
	public static final int IS_DARK = 1;
	public static final int LIGHTNESS_UNKNOWN = 2;

	/**
	 * Animates the background color of a view from one color to another color.
	 * @param v The view to animate
	 * @param toColor The To Color
	 * @param fromColor The From Color
	 * @param duration The Duration of the animation
	 * @return the animator
	 */
	public static Animator animateBackgroundColorChange(View v, int toColor, int fromColor, int duration) {
		ObjectAnimator colorFade = ObjectAnimator.ofObject(v, "backgroundColor", new ArgbEvaluator(), fromColor, toColor);
		colorFade.setDuration(duration);
		colorFade.start();
		return colorFade;
	}

	/**
	 * Finds and returns an attribute color. If it was not found the method returns the default color
	 */
	public static int getColorAttribute(@AttrRes int attribute, @ColorRes int defaultColor, Context context) {
		TypedValue a = new TypedValue();
		context.getTheme().resolveAttribute(attribute, a, true);
		if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
			return a.data;
		} else {
			return ContextCompat.getColor(context, defaultColor);
		}
	}

	/**
	 *
	 * @param view The view to get the color from
	 * @param defaultColor The color to return if the view's background isn't a ColorDrawable
	 * @return The color
	 */
	public static int getBackgroundColorFromView(View view, int defaultColor) {
		int color = defaultColor;
		Drawable background = view.getBackground();
		if (background instanceof ColorDrawable) {
			color = ((ColorDrawable) background).getColor();
		}

		return color;
	}

	/**
	 * Set the alpha component of {@code color} to be {@code alpha}.
	 */
	public static @CheckResult
	@ColorInt
	int modifyAlpha(@ColorInt int color,
					@IntRange(from = 0, to = 255) int alpha) {
		return (color & 0x00ffffff) | (alpha << 24);
	}

	/**
	 * Set the alpha component of {@code color} to be {@code alpha}.
	 */
	public static @CheckResult @ColorInt int modifyAlpha(@ColorInt int color,
														 @FloatRange(from = 0f, to = 1f) float alpha) {
		return modifyAlpha(color, (int) (255f * alpha));
	}

	/**
	 * Checks if the most populous color in the given palette is dark
	 * <p/>
	 * Annoyingly we have to return this Lightness 'enum' rather than a boolean as palette isn't
	 * guaranteed to find the most populous color.
	 */
	public static @Lightness int isDark(Palette palette) {
		Palette.Swatch mostPopulous = getMostPopulousSwatch(palette);
		if (mostPopulous == null) return LIGHTNESS_UNKNOWN;
		return isDark(mostPopulous.getHsl()) ? IS_DARK : IS_LIGHT;
	}

	public static @Nullable
	Palette.Swatch getMostPopulousSwatch(Palette palette) {
		Palette.Swatch mostPopulous = null;
		if (palette != null) {
			for (Palette.Swatch swatch : palette.getSwatches()) {
				if (mostPopulous == null || swatch.getPopulation() > mostPopulous.getPopulation()) {
					mostPopulous = swatch;
				}
			}
		}
		return mostPopulous;
	}

	/**
	 * Determines if a given bitmap is dark. This extracts a palette inline so should not be called
	 * with a large image!!
	 * <p/>
	 * Note: If palette fails then check the color of the central pixel
	 */
	public static boolean isDark(@NonNull Bitmap bitmap) {
		return isDark(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
	}

	/**
	 * Determines if a given bitmap is dark. This extracts a palette inline so should not be called
	 * with a large image!! If palette fails then check the color of the specified pixel
	 */
	public static boolean isDark(@NonNull Bitmap bitmap, int backupPixelX, int backupPixelY) {
		// first try palette with a small color quant size
		Palette palette = Palette.from(bitmap).maximumColorCount(3).generate();
		if (palette != null && palette.getSwatches().size() > 0) {
			return isDark(palette) == IS_DARK;
		} else {
			// if palette failed, then check the color of the specified pixel
			return isDark(bitmap.getPixel(backupPixelX, backupPixelY));
		}
	}

	/**
	 * Check that the lightness value (0–1)
	 */
	public static boolean isDark(float[] hsl) { // @Size(3)
		return hsl[2] < 0.5f;
	}

	/**
	 * Convert to HSL & check that the lightness value
	 */
	public static boolean isDark(@ColorInt int color) {
		float[] hsl = new float[3];
		android.support.v4.graphics.ColorUtils.colorToHSL(color, hsl);
		return isDark(hsl);
	}

	/**
	 * Calculate a variant of the color to make it more suitable for overlaying information. Light
	 * colors will be lightened and dark colors will be darkened
	 *
	 * @param color the color to adjust
	 * @param isDark whether {@code color} is light or dark
	 * @param lightnessMultiplier the amount to modify the color e.g. 0.1f will alter it by 10%
	 * @return the adjusted color
	 */
	public static @ColorInt int scrimify(@ColorInt int color,
										 boolean isDark,
										 @FloatRange(from = 0f, to = 1f) float lightnessMultiplier) {
		float[] hsl = new float[3];
		android.support.v4.graphics.ColorUtils.colorToHSL(color, hsl);

		if (!isDark) {
			lightnessMultiplier += 1f;
		} else {
			lightnessMultiplier = 1f - lightnessMultiplier;
		}

		hsl[2] = MathUtil.constrain(0f, 1f, hsl[2] * lightnessMultiplier);
		return android.support.v4.graphics.ColorUtils.HSLToColor(hsl);
	}

	public static @ColorInt int scrimify(@ColorInt int color,
										 @FloatRange(from = 0f, to = 1f) float lightnessMultiplier) {
		return scrimify(color, isDark(color), lightnessMultiplier);
	}

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({IS_LIGHT, IS_DARK, LIGHTNESS_UNKNOWN})
	public @interface Lightness {
	}
}
