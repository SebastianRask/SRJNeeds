package net.nrask.srjneeds.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sebastian Rask on 22-04-2017.
 */

public class ImageUtil {
	/**
	 * Creates a bitmap with rounded corners.
	 * @param bitmap The bitmap
	 * @param cornerRadius the corner radius in pixels
	 * @return The bitmap with rounded corners
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int cornerRadius) {
		if (bitmap == null) {
			return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
		}

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, (float) cornerRadius, (float) cornerRadius, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * Decodes a byte array to a bitmap and returns it.
	 */
	public static Bitmap getBitmapFromByteArray(byte[] bytes) {
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * Creates a byte-array for a drawable and returns it.
	 * This is useful for sending images with intents.
	 */
	public static byte[] getDrawableByteArray(Drawable aDrawable) {
		Bitmap bitmap = drawableToBitmap(aDrawable);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * Converts a drawable to a bitmap and returns it.
	 */
	public static Bitmap drawableToBitmap (Drawable drawable) {
		Bitmap bitmap = null;

		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if(bitmapDrawable.getBitmap() != null) {
				return bitmapDrawable.getBitmap();
			}
		}

		if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
			bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
		} else {
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Returns a resized bitmap with a spcified factor to change the width and height with.
	 */
	public static Bitmap getResizedBitmap(Bitmap bm, float factorchange) {
		return getResizedBitmap(bm, (int) (bm.getWidth() * factorchange), (int) (bm.getHeight() * factorchange));
	}


	/**
	 * Creates a new resized bitmap with a specified width and height.
	 */
	public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		//bm.recycle();
		return resizedBitmap;
	}

	public static Bitmap getResizedBitmap(Bitmap bm, int dpHeight, Context context) {
		try {
			Bitmap.Config mConfig = bm.getConfig() == null ? Bitmap.Config.ARGB_8888 : bm.getConfig();

			Bitmap resizedBitmap = bm.copy(mConfig, true);
			int heightPx = SRJUtil.dpToPixels(context, dpHeight);
			int widthPx = (int) ((1.0 * resizedBitmap.getWidth() / resizedBitmap.getHeight()) * (heightPx * 1.0));
			return getResizedBitmap(resizedBitmap, widthPx, heightPx);
		} catch( Exception e) {
			return null;
		}
	}

	public static void saveImageToStorage(Bitmap image, String key, Context context) {
		try {
			// Create an ByteArrayOutputStream and feed a compressed bitmap image in it
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.PNG, 100, byteStream); // PNG as only format with transparency

			// Create a FileOutputStream with out key and set the mode to private to ensure
			// Only this app and read the file. Write out ByteArrayOutput to the file and close it
			FileOutputStream fileOut = context.openFileOutput(key, Context.MODE_PRIVATE);
			fileOut.write(byteStream.toByteArray());
			byteStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getImageFromStorage(String key, Context context) throws IOException {
		InputStream fileIn = context.openFileInput(key);
		return BitmapFactory.decodeStream(fileIn);
	}

	public static void setImageRoundedTop(Bitmap workingBitmap, ImageView v, Context context, float cornerRadius) {
		int w = workingBitmap.getWidth();
		int h = workingBitmap.getHeight();
		Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmp);
		Shader shader = new BitmapShader(workingBitmap, Shader.TileMode.MIRROR,
				Shader.TileMode.MIRROR);

		Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		paint.setAntiAlias(true);
		paint.setShader(shader);
		RectF rec = new RectF(0, 0, w, h - (h/3));
		c.drawRect(new RectF(0, (h/3), w, h), paint);
		c.drawRoundRect(rec, cornerRadius, cornerRadius, paint);
		//v.setImageDrawable(new BitmapDrawable(context.getResources(), bmp));
		//v.setImageBitmap(new BitmapDrawable(context.getResources(), bmp).getBitmap());
	}

	public static Bitmap makeTransparent(Bitmap bit, int transparentColor) {
		int width =  bit.getWidth();
		int height = bit.getHeight();
		Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		int [] allpixels = new int [ myBitmap.getHeight()*myBitmap.getWidth()];
		bit.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
		myBitmap.setPixels(allpixels, 0, width, 0, 0, width, height);

		for(int i =0; i<myBitmap.getHeight()*myBitmap.getWidth();i++){
			if( allpixels[i] == transparentColor)
				allpixels[i] = Color.alpha(Color.TRANSPARENT);
		}

		myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
		return myBitmap;
	}

	/**
	 * Gets Bitmap from the specified URL
	 * Must not be called on Main UI Thread
	 */
	public static Bitmap getBitmapFromUrl(String url) {
		Bitmap bitmap = null;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.connect();
			InputStream input = connection.getInputStream();
			bitmap = BitmapFactory.decodeStream(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	}
}
