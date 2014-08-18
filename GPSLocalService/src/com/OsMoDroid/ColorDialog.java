package com.OsMoDroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.SystemClock;
import android.util.Log;
import android.util.StateSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ColorDialog extends AlertDialog implements OnSeekBarChangeListener, OnClickListener {

    public interface OnClickListener {
        public void onClick(Object tag, int color);
    }

	private SeekBar mHue;
	private SeekBar mSaturation;
	private SeekBar mValue;
	private SeekBar mAlpha;
	private ColorDialog.OnClickListener mListener;
	private int mColor;
	private Object mTag;
	private GradientDrawable mPreviewDrawable;
	private boolean mUseAlpha;
	private IconPreviewDrawable mIcon;

	public ColorDialog(Context context, boolean useAlpha, Object tag, int color, OnClickListener listener, int iconId) {
		super(context);
		mUseAlpha = useAlpha;
		mTag = tag;
		mListener = listener;

		Resources res = context.getResources();
		setTitle(res.getText(R.string.color_dialog_title));
		setButton(BUTTON_POSITIVE, res.getText(android.R.string.yes), this);
		setButton(BUTTON_NEGATIVE, res.getText(android.R.string.cancel), this);
		View root = LayoutInflater.from(context).inflate(R.layout.color_picker, null);
		setView(root);
		
		View preview = root.findViewById(R.id.preview);
		mPreviewDrawable = new GradientDrawable();
		// 2 pix more than color_picker_frame's radius
		mPreviewDrawable.setCornerRadius(7);
		Drawable[] layers;
		if (useAlpha) {
			mIcon = new IconPreviewDrawable(getContext().getResources(), iconId);
			ClipDrawable topClip = new ClipDrawable(mPreviewDrawable, Gravity.TOP, ClipDrawable.VERTICAL);
			topClip.setLevel(5000);
			layers = new Drawable[] {
					topClip,
					mIcon,
					res.getDrawable(R.drawable.color_picker_frame),
			};
		} else {
			layers = new Drawable[] {
					mPreviewDrawable,
					res.getDrawable(R.drawable.color_picker_frame),
			};
		}
		preview.setBackgroundDrawable(new LayerDrawable(layers));
		
		mHue = (SeekBar) root.findViewById(R.id.hue);
		mSaturation = (SeekBar) root.findViewById(R.id.saturation);
		mValue = (SeekBar) root.findViewById(R.id.value);
		mAlpha = (SeekBar) root.findViewById(R.id.alpha);
		
		mColor = color;
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		int h = (int) (hsv[0] * mHue.getMax() / 360);
		int s = (int) (hsv[1] * mSaturation.getMax());
		int v = (int) (hsv[2] * mValue.getMax());
		setupSeekBar(mHue, R.string.color_h, h, res);
		setupSeekBar(mSaturation, R.string.color_s, s, res);
		setupSeekBar(mValue, R.string.color_v, v, res);
		if (useAlpha) {
			int a = Color.alpha(color) * mAlpha.getMax() / 255;
			setupSeekBar(mAlpha, R.string.color_a, a, res);
		} else {
			mAlpha.setVisibility(View.GONE);
		}
		
		updatePreview(color);
	}
	
	private void setupSeekBar(SeekBar seekBar, int id, int value, Resources res) {
		seekBar.setProgressDrawable(new TextSeekBarDrawable(res, id, value < seekBar.getMax() / 2));
		seekBar.setProgress(value);
		seekBar.setOnSeekBarChangeListener(this);
	}

	private void update() {
		float[] hsv = {
			360 * mHue.getProgress() / (float) mHue.getMax(),
			mSaturation.getProgress() / (float) mSaturation.getMax(),
			mValue.getProgress() / (float) mValue.getMax(),
		};
		if (mUseAlpha) {
			mColor = Color.HSVToColor(255 * mAlpha.getProgress() / mAlpha.getMax(), hsv);
		} else {
			mColor = Color.HSVToColor(hsv);
		}
		updatePreview(mColor);
	}
	
	private void updatePreview(int color) {
		if (mUseAlpha) {
			mIcon.setColorFilter(color, Mode.SRC_ATOP);
			// set color to be fully opaque
			color |= 0xff000000;
		}
		mPreviewDrawable.setColor(color);
		mPreviewDrawable.invalidateSelf();
	}

	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		update();
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
			mListener.onClick(mTag, mColor);
		}
		dismiss();
	}
	
	static class IconPreviewDrawable extends Drawable {
		private Bitmap mBitmap;
		private Bitmap mTmpBitmap;
		private Canvas mTmpCanvas;
		private int mTintColor;
		

		public IconPreviewDrawable(Resources res, int id) {
			Bitmap b;
			try {
				b = BitmapFactory.decodeResource(res, id);
				if (b == null) {
					b = BitmapFactory.decodeResource(res, R.drawable.icon);
				}
			} catch (NotFoundException e) {
				b = BitmapFactory.decodeResource(res, R.drawable.icon);
			}
			mBitmap = b;
			mTmpBitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Config.ARGB_8888);
			mTmpCanvas = new Canvas(mTmpBitmap);
		}
		
		@Override
		public void draw(Canvas canvas) {
			Rect b = getBounds();
			float x = (b.width() - mBitmap.getWidth()) / 2.0f;
			float y = 0.75f * b.height() - mBitmap.getHeight() / 2.0f;
			
			mTmpCanvas.drawColor(0, Mode.CLEAR);
			mTmpCanvas.drawBitmap(mBitmap, 0, 0, null);
			mTmpCanvas.drawColor(mTintColor, Mode.SRC_ATOP);
			canvas.drawBitmap(mTmpBitmap, x, y, null);
		}

		@Override
		public int getOpacity() {
			return PixelFormat.TRANSLUCENT;
		}

		@Override
		public void setAlpha(int alpha) {
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
		}
		
		@Override
		public void setColorFilter(int color, Mode mode) {
			mTintColor = color;
		}
	}
	
	static final int[] STATE_FOCUSED = {android.R.attr.state_focused};
	static final int[] STATE_PRESSED = {android.R.attr.state_pressed};
	
	static class TextSeekBarDrawable extends Drawable implements Runnable {
		
		private static final String TAG = "TextSeekBarDrawable";
		private static final long DELAY = 50;
		private String mText;
		private Drawable mProgress;
		private Paint mPaint;
		private Paint mOutlinePaint;
		private float mTextWidth;
		private boolean mActive;
		private float mTextXScale;
		private int mDelta;
		private ScrollAnimation mAnimation;

		public TextSeekBarDrawable(Resources res, int id, boolean labelOnRight) {
			mText = res.getString(id);
			mProgress = res.getDrawable(android.R.drawable.progress_horizontal);
			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaint.setTypeface(Typeface.DEFAULT_BOLD);
			mPaint.setTextSize(16);
			mPaint.setColor(0xff000000);
			mOutlinePaint = new Paint(mPaint);
			mOutlinePaint.setStyle(Style.STROKE);
			mOutlinePaint.setStrokeWidth(3);
			mOutlinePaint.setColor(0xbbffc300);
			mOutlinePaint.setMaskFilter(new BlurMaskFilter(1, Blur.NORMAL));
			mTextWidth = mOutlinePaint.measureText(mText);
			mTextXScale = labelOnRight? 1 : 0;
			mAnimation = new ScrollAnimation();
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			mProgress.setBounds(bounds);
		}
		
		@Override
		protected boolean onStateChange(int[] state) {
			mActive = StateSet.stateSetMatches(STATE_FOCUSED, state) | StateSet.stateSetMatches(STATE_PRESSED, state);
			invalidateSelf();
			return false;
		}
		
		@Override
		public boolean isStateful() {
			return true;
		}
		
		@Override
		protected boolean onLevelChange(int level) {
//			Log.d(TAG, "onLevelChange " + level);
			if (level < 4000 && mDelta <= 0) {
//				Log.d(TAG, "onLevelChange scheduleSelf ++");
				mDelta = 1;
				mAnimation.startScrolling(mTextXScale, 1);
				scheduleSelf(this, SystemClock.uptimeMillis() + DELAY);
			} else
			if (level > 6000 && mDelta >= 0) {
//				Log.d(TAG, "onLevelChange scheduleSelf --");
				mDelta = -1;
				mAnimation.startScrolling(mTextXScale, 0);
				scheduleSelf(this, SystemClock.uptimeMillis() + DELAY);
			}
			return mProgress.setLevel(level);
		}
		
		@Override
		public void draw(Canvas canvas) {
			mProgress.draw(canvas);

			if (mAnimation.hasStarted() && !mAnimation.hasEnded()) {
				// pending animation
				mAnimation.getTransformation(AnimationUtils.currentAnimationTimeMillis(), null);
				mTextXScale = mAnimation.getCurrent();
//				Log.d(TAG, "draw " + mTextX + " " + SystemClock.uptimeMillis());
			}
			
			Rect bounds = getBounds();
			float x = 6 + mTextXScale * (bounds.width() - mTextWidth - 6 - 6);
			float y = (bounds.height() + mPaint.getTextSize()) / 2;
			mOutlinePaint.setAlpha(mActive? 255 : 255 / 2);
			mPaint.setAlpha(mActive? 255 : 255 / 2);
			canvas.drawText(mText, x, y, mOutlinePaint);
			canvas.drawText(mText, x, y, mPaint);
		}

		@Override
		public int getOpacity() {
			return PixelFormat.TRANSLUCENT;
		}

		@Override
		public void setAlpha(int alpha) {
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
		}

		public void run() {
			mAnimation.getTransformation(AnimationUtils.currentAnimationTimeMillis(), null);
			// close interpolation of mTextX
			mTextXScale = mAnimation.getCurrent();
			if (!mAnimation.hasEnded()) {
				scheduleSelf(this, SystemClock.uptimeMillis() + DELAY);
			}
			invalidateSelf();
//			Log.d(TAG, "run " + mTextX + " " + SystemClock.uptimeMillis());
		}
	}
	
	static class ScrollAnimation extends Animation {
		private static final String TAG = "ScrollAnimation";
		private static final long DURATION = 750;
		private float mFrom;
		private float mTo;
		private float mCurrent;
		
		public ScrollAnimation() {
			setDuration(DURATION);
			setInterpolator(new DecelerateInterpolator());
		}
		
		public void startScrolling(float from, float to) {
			mFrom = from;
			mTo = to;
			startNow();
		}
		
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			mCurrent = mFrom + (mTo - mFrom) * interpolatedTime;
//			Log.d(TAG, "applyTransformation " + mCurrent);
		}
		
		public float getCurrent() {
			return mCurrent;
		}
	}
}
