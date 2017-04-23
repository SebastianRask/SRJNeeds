package net.nrask.srjneeds.animation;

import android.graphics.Bitmap;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import net.nrask.srjneeds.util.ImageUtil;

/**
 * Created by Sebastian Rask on 24-06-2016.
 */
public class RoundImageAnimation extends Animation {
	int fromRounded, toRounded;
	ImageView view;
	Bitmap imageBitmap;

	public RoundImageAnimation(int fromRounded, int toRounded, ImageView view, Bitmap imageBitmap) {
		this.fromRounded = fromRounded;
		this.toRounded = toRounded;
		this.view = view;
		this.imageBitmap = imageBitmap;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		int rounded = (int) (fromRounded + (toRounded - fromRounded) * interpolatedTime);
		view.setImageBitmap(ImageUtil.getRoundedCornerBitmap(imageBitmap, rounded));
		view.requestLayout();
	}

	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
	}

	@Override
	public boolean willChangeBounds() {
		return false;
	}
}
