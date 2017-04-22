package net.nrask.srjneeds.util;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Sebastian Rask on 22-04-2017.
 */

public class ColorUtil {
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
}
