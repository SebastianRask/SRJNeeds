package net.nrask.srjneeds.util;

/**
 * Created by Sebastian Rask on 23-04-2017.
 */

/**
 * Borrowed from github.com/romannurik/muzei
 */

public class MathUtil {

	private MathUtil() { }

	public static float constrain(float min, float max, float v) {
		return Math.max(min, Math.min(max, v));
	}
}
