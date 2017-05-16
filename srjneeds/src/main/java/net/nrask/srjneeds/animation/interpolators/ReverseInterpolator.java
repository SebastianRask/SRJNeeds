package net.nrask.srjneeds.animation.interpolators;

/**
 * Created by Sebastian Rask on 30-04-2017.
 */

import android.view.animation.Interpolator;

public class ReverseInterpolator implements Interpolator {
	@Override
	public float getInterpolation(float paramFloat) {
		return Math.abs(paramFloat -1f);
	}
}
