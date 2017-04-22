package net.nrask.srjneeds.util;

/**
 * Created by Sebastian Rask on 22-04-2017.
 */

public class FormatUtil {
	/**
	 * Makes a timestamp from a length in seconds.
	 * @param videoLengthInSeconds Length in seconds
	 * @return
	 */
	public static String prettifyVideoLength(int videoLengthInSeconds) {
		String result = "";
		double  hours = videoLengthInSeconds/60.0/60.0,
				minutes,
				seconds;

		double minutesAsDecimalHours = hours - Math.floor(hours);
		minutes = 60.0 * minutesAsDecimalHours;
		double secondsAsDecimalMinutes = minutes - Math.floor(minutes);
		seconds = 60.0 * secondsAsDecimalMinutes;

		if (hours >= 1) {
			result = ((int) Math.floor(hours)) + ":";
		}
		if (minutes >= 1 || hours >= 1) {
			result += numberToTime(minutes) + ":";
		}
		result += numberToTime(Math.round(seconds));

		return result;
	}

	/**
	 * Converts Double to time. f.eks. 4.5 becomes "04"
	 */
	public static String numberToTime(double time) {
		int timeInt = ((int) Math.floor(time));

		if (timeInt < 10) {
			return "0" + timeInt;
		} else {
			return "" + timeInt;
		}
	}

	/**
	 * Creates a string with a unicode emoticon.
	 * @param unicode
	 * @return
	 */
	public static String getEmijoByUnicode(int unicode){
		return new String(Character.toChars(unicode));
	}
}
