package net.nrask.srjneeds.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by Sebastian Rask JEpsen on 28/07/16.
 */
public class BackEventEditText extends android.support.v7.widget.AppCompatEditText {

	private EditTextImeBackListener mOnImeBack;

	public BackEventEditText(Context context) {
		super(context);
	}

	public BackEventEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BackEventEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK &&
					event.getAction() == KeyEvent.ACTION_UP) {
			if (mOnImeBack != null)
				mOnImeBack.onImeBack(this, this.getText().toString());
		}
		return super.dispatchKeyEvent(event);
	}

	public void setOnEditTextImeBackListener(EditTextImeBackListener listener) {
		mOnImeBack = listener;
	}

	public interface EditTextImeBackListener {
		void onImeBack(BackEventEditText ctrl, String text);
	}
}


