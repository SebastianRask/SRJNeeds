package net.nrask.srjneeds.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

/**
 * Created by Sebastian Rask on 22-04-2017.
 */

public class SRJUtil {
	/**
	 * Checks if the device is connected to a valid network
	 * Can be called on the UI thread
	 */
	public static boolean isNetWorkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnectedOrConnecting();
	}

	/**
	 * Checks if the device is connected to a valid network
	 * Can only be called on a thread
	 */
	public static boolean isNetworkConnectedThreadOnly(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
			try {
				HttpURLConnection urlc = (HttpURLConnection)
						(new URL("http://clients3.google.com/generate_204")
								.openConnection());
				urlc.setRequestProperty("User-Agent", "Android");
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(1500);
				urlc.connect();
				return (urlc.getResponseCode() == 204 &&
						urlc.getContentLength() == 0);
			} catch (IOException e) {
				Log.e("SERVICE", "Error checking internet connection", e);
			}
		} else {
			Log.d("SERVICE", "No network available!");
		}

		return false;
	}

	/**
	 * Hides the onscreen keyboard if it is visisble
	 */
	public static void hideKeyboard(Activity activity) {
		// Check if no view has focus:
		View view = activity.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * Shows the soft keyboard
	 */
	public static void showKeyboard(Activity activity) {
		// Check if no view has focus:
		View view = activity.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputMethodManager=(InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
		}
	}

	/**
	 * Creates and returns an intent that navigates the user to the Google Play landing page
	 * @return The intent
	 */
	public static Intent getPlayStoreIntent(String appBundleName) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=" + appBundleName));
		return intent;
	}

	/**
	 * Does the opposite of the View.bringToFront() method
	 * @param v the view you want to send to the back
	 */
	public static void bringToBack(final View v) {
		final ViewGroup parent = (ViewGroup)v.getParent();
		if (null != parent) {
			parent.removeView(v);
			parent.addView(v, 0);
		}
	}

	public static boolean doesStorageFileExist(String key, Context context){
		File file = context.getFileStreamPath(key);
		return file.exists();
	}

	/**
	 * Gets the navigation drawer toggle view from a toolbar
	 * @param toolbar The toolbar containing the navigation button
	 * @return The ImageButton
	 */
	public static ImageButton getNavButtonView(Toolbar toolbar) {
		try {
			Class<?> toolbarClass = Toolbar.class;
			Field navButtonField = toolbarClass.getDeclaredField("mNavButtonView");
			navButtonField.setAccessible(true);

			return (ImageButton) navButtonField.get(toolbar);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns the height of the device screen
	 */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.heightPixels;
	}

	/**
	 * Returns the width of the device screen
	 */
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}

	public static boolean isDbSafe(SQLiteDatabase db) {
		return db.isOpen() && !db.isReadOnly() && !db.isDbLockedByCurrentThread();
	}

	public static boolean isVertical(Context aContext) {
		int orientation = aContext.getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			return true;
		} else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return false;
		}
		return true;
	}

	public static double getDataReceived() {
		return (double) TrafficStats.getUidRxBytes(android.os.Process
				.myUid()) / (1024 * 1024);
	}

	public static int dpToPixels(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	/**
	 * Method for increasing a Navigation Drawer's edge size.
	 */
	public static void increaseNavigationDrawerEdge(DrawerLayout aDrawerLayout, Context context) {
		// Increase the area from which you can open the navigation drawer.
		try {
			Field mDragger = aDrawerLayout.getClass().getDeclaredField("mLeftDragger");
			mDragger.setAccessible(true);
			ViewDragHelper draggerObj = (ViewDragHelper) mDragger.get(aDrawerLayout);

			Field mEdgeSize = draggerObj.getClass().getDeclaredField("mEdgeSize");
			mEdgeSize.setAccessible(true);
			int edgeSize = mEdgeSize.getInt(draggerObj) * 3;

			mEdgeSize.setInt(draggerObj, edgeSize); //optimal value as for me, you may set any constant in dp
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if two calendar objects have the same day of the year
	 * @return True if the day is the same, otherwise false
	 */
	public static boolean isCalendarSameDay(Calendar one, Calendar two) {
		return one.get(Calendar.YEAR) == two.get(Calendar.YEAR) && one.get(Calendar.DAY_OF_YEAR) == two.get(Calendar.DAY_OF_YEAR);
	}
}
