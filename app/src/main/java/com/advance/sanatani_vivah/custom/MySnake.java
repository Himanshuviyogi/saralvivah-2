package com.advance.sanatani_vivah.custom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.irozon.sneaker.interfaces.OnSneakerClickListener;
//import com.irozon.sneaker.interfaces.OnSneakerDismissListener;
//import com.irozon.sneaker.widget.RoundedImageView;

import java.lang.ref.WeakReference;

    public class MySnake implements View.OnClickListener {

        private static final int DEFAULT_VALUE = -100000;
        private static int mIcon = DEFAULT_VALUE;
        private static Drawable mIconDrawable = null;
        private static int mBackgroundColor = DEFAULT_VALUE;
        private static int mHeight = DEFAULT_VALUE;
        private static int mIconColorFilterColor = DEFAULT_VALUE;
        private static int mIconSize = 24;
        private static int mTitleSize = 14;
        private static int mMessageSize = 12;
        private static String mTitle = "";
        private static String mMessage = "";
        private static int mTitleColor = DEFAULT_VALUE;
        private static int mMessageColor = DEFAULT_VALUE;
        private static boolean mAutoHide = true;
        private static int mDuration = 3000;
        private static WeakReference<LinearLayout> layoutWeakReference;
        private static WeakReference<Activity> contextWeakReference;
        private static boolean mIsCircular = false;
//        private static OnSneakerClickListener mListener = null;
//        private static OnSneakerDismissListener mDismissListener = null;
        private static Typeface mTypeFace = null;
        private static int mCornerRadius = DEFAULT_VALUE;
        private static int mMargin = DEFAULT_VALUE;

        /**
         * Constructor
         *
         * @param activity
         */
        private MySnake(Activity activity) {
            contextWeakReference = new WeakReference<>(activity);
        }

        /**
         * Create Sneaker with activity reference
         *
         * @param activity
         * @return
         */
        public static MySnake with(Activity activity) {
            MySnake sneaker = new MySnake(activity);
            setDefault();
            return sneaker;
        }

        /**
         * Hides the sneaker
         */
        public static void hide() {
            if (getLayout() != null) {
               // getLayout().startAnimation(AnimationUtils.loadAnimation(getContext(), com.irozon.sneaker.R.anim.popup_hide));
//                if (mDismissListener != null) {
//                    mDismissListener.onDismiss();
//                }
                getActivityDecorView().removeView(getLayout());
            }
        }

        /**
         * Return activity parent view
         *
         * @return
         */
        private static ViewGroup getActivityDecorView() {
            ViewGroup decorView = null;

            decorView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();

            return decorView;
        }

        /**
         * Sets the default values to the sneaker
         */
        private static void setDefault() {
            mTitle = "";
            mIcon = DEFAULT_VALUE;
            mIconDrawable = null;
            mIconColorFilterColor = DEFAULT_VALUE;
            mIconSize = 24;
            mTitleSize = 14;
            mMessageSize = 12;
            mBackgroundColor = DEFAULT_VALUE;
            mAutoHide = true;
            mTitleColor = DEFAULT_VALUE;
            mMessageColor = DEFAULT_VALUE;
            mHeight = DEFAULT_VALUE;
            mIsCircular = false;
            //mListener = null;
            mTypeFace = null;
            mCornerRadius = DEFAULT_VALUE;
            mMargin = DEFAULT_VALUE;
        }

        /**
         * Return activity weak reference
         *
         * @return
         */
        private static Context getContext() {
            return contextWeakReference.get();
        }

        /**
         * Returns sneaker main layout weak reference
         *
         * @return
         */
        private static View getLayout() {
            return layoutWeakReference.get();
        }

        /**
         * Sets the title of the sneaker
         *
         * @param title string value of title
         * @return
         */
        public MySnake setTitle(String title) {
            mTitle = title;
            return this;
        }

        /**
         * Sets the title of the sneaker with color
         *
         * @param title string value of title
         * @param color Color resource for title text
         * @return
         */
        public MySnake setTitle(String title, int color) {
            mTitle = title;
            if (getContext() != null) {
                try {
                    mTitleColor = ContextCompat.getColor(getContext(), color);
                } catch (Exception e) {
                    mTitleColor = color;
                }
            }
            return this;
        }

        /**
         * Sets the message to sneaker
         *
         * @param message String value of message
         * @return
         */
        public MySnake setMessage(String message) {
            mMessage = message;
            return this;
        }

        public MySnake setTitleSize(int size) {
            mTitleSize = size;
            return this;
        }

        public MySnake setMessageSize(int size) {
            mMessageSize = size;
            return this;
        }

        /**
         * Sets the message to sneaker with color
         *
         * @param message String value of message
         * @param color   Color resource for message text
         * @return
         */
        public MySnake setMessage(String message, int color) {
            mMessage = message;
            if (getContext() != null) {
                try {
                    mMessageColor = ContextCompat.getColor(getContext(), color);
                } catch (Exception e) {
                    mMessageColor = color;
                }
            }
            return this;
        }

        /**
         * Sets the icon to sneaker
         *
         * @param icon Icon resource for sneaker
         * @return
         */
        public MySnake setIcon(int icon) {
            mIconDrawable = null;
            mIcon = icon;
            return this;
        }

        /**
         * Sets the icon to sneaker
         *
         * @param icon Icon drawable for sneaker
         * @return
         */
        public MySnake setIcon(Drawable icon) {
            mIcon = DEFAULT_VALUE;
            mIconDrawable = icon;
            return this;
        }

        /**
         * Sets the icon to sneaker with circular option
         *
         * @param icon
         * @param isCircular If icon is round or not
         * @return
         */
        public MySnake setIcon(int icon, boolean isCircular) {
            mIconDrawable = null;
            mIcon = icon;
            mIsCircular = isCircular;
            return this;
        }

        /**
         * Sets the icon to sneaker with circular option
         *
         * @param icon
         * @param isCircular If icon is round or not
         * @return
         */
        public MySnake setIcon(Drawable icon, boolean isCircular) {
            mIcon = DEFAULT_VALUE;
            mIconDrawable = icon;
            mIsCircular = isCircular;
            return this;
        }

        public MySnake setIcon(int icon, int tintColor) {
            mIconDrawable = null;
            mIcon = icon;
            if (getContext() != null) {
                try {
                    mIconColorFilterColor = ContextCompat.getColor(getContext(), tintColor);
                } catch (Exception e) {
                    mIconColorFilterColor = tintColor;
                }
            }
            return this;
        }

        public MySnake setIcon(Drawable icon, int tintColor) {
            mIcon = DEFAULT_VALUE;
            mIconDrawable = icon;
            if (getContext() != null) {
                try {
                    mIconColorFilterColor = ContextCompat.getColor(getContext(), tintColor);
                } catch (Exception e) {
                    mIconColorFilterColor = tintColor;
                }
            }
            return this;
        }

        /**
         * Sets the icon to sneaker with circular option and icon tint
         *
         * @param icon
         * @param tintColor  Icon tint color
         * @param isCircular If icon is round or not
         * @return
         */
        public MySnake setIcon(int icon, int tintColor, boolean isCircular) {
            mIconDrawable = null;
            mIcon = icon;
            mIsCircular = isCircular;
            if (getContext() != null) {
                try {
                    mIconColorFilterColor = ContextCompat.getColor(getContext(), tintColor);
                } catch (Exception e) {
                    mIconColorFilterColor = tintColor;
                }
            }
            return this;
        }

        /**
         * Sets the icon to sneaker with circular option and icon tint
         *
         * @param icon
         * @param tintColor  Icon tint color
         * @param isCircular If icon is round or not
         * @return
         */
        public MySnake setIcon(Drawable icon, int tintColor, boolean isCircular) {
            mIcon = DEFAULT_VALUE;
            mIconDrawable = icon;
            mIsCircular = isCircular;
            if (getContext() != null) {
                try {
                    mIconColorFilterColor = ContextCompat.getColor(getContext(), tintColor);
                } catch (Exception e) {
                    mIconColorFilterColor = tintColor;
                }
            }
            return this;
        }

        /**
         * Sets the size of the icon.
         *
         * @param size New icon size.
         */
        public MySnake setIconSize(int size) {
            mIconSize = size;
            return this;
        }

        /**
         * Sets the corner radius for round corner sneaker.
         *
         * @param radius Corner radius.
         */
        public MySnake setCornerRadius(int radius) {
            mCornerRadius = radius;
            return this;
        }

        /**
         * Sets the corner radius for round corner sneaker with margin.
         *
         * @param radius Corner radius.
         * @param margin margin.
         */
        public MySnake setCornerRadius(int radius, int margin) {
            mCornerRadius = radius;
            mMargin = margin;
            return this;
        }

        /**
         * Disable/Enable auto hiding sneaker
         *
         * @param autoHide
         * @return
         */
        public MySnake autoHide(boolean autoHide) {
            mAutoHide = autoHide;
            return this;
        }

        /**
         * Sets the height to sneaker
         *
         * @param height Height value for sneaker
         * @return
         */
        public MySnake setHeight(int height) {
            mHeight = height;
            return this;
        }

        /**
         * Sets the duration for sneaker.
         * After this duration sneaker will disappear
         *
         * @param duration
         * @return
         */
        public MySnake setDuration(int duration) {
            mDuration = duration;
            return this;
        }

        /**
         * Sets the click listener to sneaker
         *
         * @param listener
         * @return
         */
//        public MySnake setOnSneakerClickListener(OnSneakerClickListener listener) {
//            mListener = listener;
//            return this;
//        }

        /**
         * Sets the dismiss listener to sneaker
         */
//        public MySnake setOnSneakerDismissListener(OnSneakerDismissListener listener) {
//            mDismissListener = listener;
//            return this;
//        }

        /**
         * Set font for title and message
         *
         * @param typeface
         * @return
         */
        public MySnake setTypeface(Typeface typeface) {
            mTypeFace = typeface;
            return this;
        }

        /**
         * Shows sneaker with custom color
         *
         * @param backgroundColor Color resource for sneaker background color
         */
        public void sneak(int backgroundColor) {
            if (getContext() != null) {
                try {
                    mBackgroundColor = ContextCompat.getColor(getContext(), backgroundColor);
                } catch (Exception e) {
                    mBackgroundColor = backgroundColor;
                }
                sneakView();
            }
        }

        /**
         * Shows warning sneaker with fixed icon, background color and icon color.
         * Icons, background and text colors for this are not customizable
         */
//        public void sneakWarning() {
//            mBackgroundColor = Color.parseColor("#ffc100");
//            mTitleColor = Color.parseColor("#000000");
//            mMessageColor = Color.parseColor("#000000");
//            mIconColorFilterColor = Color.parseColor("#000000");
//            mIcon = com.irozon.sneaker.R.drawable.ic_warning;
//
//            if (getContext() != null)
//                sneakView();
//        }

        /**
         * Shows error sneaker with fixed icon, background color and icon color.
         * Icons, background and text colors for this are not customizable
         */
//        public MySnake sneakError() {
//            mBackgroundColor = Color.parseColor("#ff0000");
//            mTitleColor = Color.parseColor("#FFFFFF");
//            mMessageColor = Color.parseColor("#FFFFFF");
//            mIconColorFilterColor = Color.parseColor("#FFFFFF");
//            mIcon = com.irozon.sneaker.R.drawable.ic_error;
//
//            if (getContext() != null)
//                sneakView();
//            return null;
//        }

        /**
         * Shows success sneaker with fixed icon, background color and icon color.
         * Icons, background and text colors for this are not customizable
         */
//        public MySnake sneakSuccess() {
//            mBackgroundColor = Color.parseColor("#2bb600");
//            mTitleColor = Color.parseColor("#FFFFFF");
//            mMessageColor = Color.parseColor("#FFFFFF");
//            mIconColorFilterColor = Color.parseColor("#FFFFFF");
//            mIcon = com.irozon.sneaker.R.drawable.ic_success;
//
//            if (getContext() != null)
//                sneakView();
//            return null;
//        }

        /**
         * Creates the view and sneaks in
         */
        private void sneakView() {

            // Main layout
            LinearLayout layout = new LinearLayout(getContext());
            layoutWeakReference = new WeakReference<>(layout);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight == DEFAULT_VALUE ? (SnakUtils.getStatusBarHeight((Activity) getContext()) + SnakUtils.convertToDp(getContext(), 56)) : SnakUtils.convertToDp(getContext(), mHeight));
            if (mMargin != DEFAULT_VALUE) {
                layoutParams.setMargins(SnakUtils.convertToDp(getContext(), mMargin),
                        SnakUtils.convertToDp(getContext(), mMargin),
                        SnakUtils.convertToDp(getContext(), mMargin),
                        SnakUtils.convertToDp(getContext(), mMargin)
                );
            }

            layout.setLayoutParams(layoutParams);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setGravity(Gravity.CENTER_VERTICAL);
            layout.setPadding(46, 0, 46, 0);//top=SnakUtils.getStatusBarHeight((Activity) getContext())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                layout.setElevation(6);
            }
            // Background color
            if (mCornerRadius == DEFAULT_VALUE) {
                layout.setBackgroundColor(mBackgroundColor);
            } else {
                SnakUtils.customView(getContext(), layout, mBackgroundColor, mCornerRadius);
            }

            // Icon
            // If icon is set
            if (mIcon != DEFAULT_VALUE || mIconDrawable != null) {
                if (!mIsCircular) {
                    AppCompatImageView ivIcon = new AppCompatImageView(getContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(SnakUtils.convertToDp(getContext(), mIconSize), SnakUtils.convertToDp(getContext(), mIconSize));
                    ivIcon.setLayoutParams(lp);

                    if (mIcon == DEFAULT_VALUE) {
                        ivIcon.setImageDrawable(mIconDrawable);
                    } else {
                        ivIcon.setImageResource(mIcon);
                    }
                    ivIcon.setClickable(false);
                    if (mIconColorFilterColor != DEFAULT_VALUE) {
                        ivIcon.setColorFilter(mIconColorFilterColor);
                    }
                    layout.addView(ivIcon);
                } else {
//                    RoundedImageView ivIcon = new RoundedImageView(getContext());
//                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(SnakUtils.convertToDp(getContext(), mIconSize), SnakUtils.convertToDp(getContext(), mIconSize));
//                    ivIcon.setLayoutParams(lp);
//
//                    if (mIcon == DEFAULT_VALUE) {
//                        ivIcon.setImageDrawable(mIconDrawable);
//                    } else {
//                        ivIcon.setImageResource(mIcon);
//                    }
//                    ivIcon.setClickable(false);
//                    if (mIconColorFilterColor != DEFAULT_VALUE) {
//                        ivIcon.setColorFilter(mIconColorFilterColor);
//                    }
//                    layout.addView(ivIcon);
                }
            }

            // Title and description
            LinearLayout textLayout = new LinearLayout(getContext());
            LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textLayout.setLayoutParams(textLayoutParams);
            textLayout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams lpTv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (!mTitle.isEmpty()) {
                TextView tvTitle = new TextView(getContext());
                tvTitle.setLayoutParams(lpTv);
                tvTitle.setGravity(Gravity.CENTER_VERTICAL);
                if (!mMessage.isEmpty())
                    tvTitle.setPadding(46, 26, 26, 0); // Top padding if there is message
                else
                    tvTitle.setPadding(46, 0, 26, 0); // No top padding if there is no message
                if (mTitleColor != DEFAULT_VALUE)
                    tvTitle.setTextColor(mTitleColor);

                // typeface
                //if (mTypeFace != null)
                //    tvTitle.setTypeface(mTypeFace);
                tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
                tvTitle.setTextSize(mTitleSize);
                tvTitle.setText(mTitle);
                tvTitle.setClickable(false);
                textLayout.addView(tvTitle);
            }

            if (!mMessage.isEmpty()) {
                TextView tvMessage = new TextView(getContext());
                tvMessage.setLayoutParams(lpTv);
                tvMessage.setGravity(Gravity.CENTER_VERTICAL);
                if (!mTitle.isEmpty())
                    tvMessage.setPadding(46, 0, 26, 26); // Bottom padding if there is title
                else
                    tvMessage.setPadding(46, 0, 26, 0); // No bottom padding if there is no title
                if (mMessageColor != DEFAULT_VALUE)
                    tvMessage.setTextColor(mMessageColor);

                // typeface
                if (mTypeFace != null)
                    tvMessage.setTypeface(mTypeFace);

                tvMessage.setTextSize(mMessageSize);
                tvMessage.setText(mMessage);
                tvMessage.setClickable(false);
                textLayout.addView(tvMessage);
            }
            layout.addView(textLayout);
            //layout.setId(com.irozon.sneaker.R.id.mainLayout);

            final ViewGroup viewGroup = getActivityDecorView();
            getExistingOverlayInViewAndRemove(viewGroup);

            layout.setOnClickListener(this);
            viewGroup.addView(layout);

//            layout.startAnimation(AnimationUtils.loadAnimation(getContext(), com.irozon.sneaker.R.anim.popup_show));
//            if (mAutoHide) {
//                final Handler handler = new Handler();
//                handler.removeCallbacks(null);
//                Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        if (getLayout() != null && getContext() != null) {
//                            getLayout().startAnimation(AnimationUtils.loadAnimation(getContext(), com.irozon.sneaker.R.anim.popup_hide));
//                            viewGroup.removeView(getLayout());
//                        }
//                    }
//                };
//
//                handler.postDelayed(runnable, mDuration);
//            }
        }

        /**
         * Gets the existing sneaker and removes before adding new one
         *
         * @param parent
         */
        private void getExistingOverlayInViewAndRemove(ViewGroup parent) {

            for (int i = 0; i < parent.getChildCount(); i++) {

                View child = parent.getChildAt(i);
//                if (child.getId() == com.irozon.sneaker.R.id.mainLayout) {
//                    parent.removeView(child);
//                }
                if (child instanceof ViewGroup) {
                    getExistingOverlayInViewAndRemove((ViewGroup) child);
                }
            }
        }

        @Override
        public void onClick(View view) {

        }

        /**
         * Sneaker on click
         *
         * @param view
         */
//        @Override
//        public void onClick(View view) {
//            if (mListener != null) {
//                mListener.onSneakerClick(view);
//            }
//            if (getLayout() != null && getContext() != null) {
//                getLayout().startAnimation(AnimationUtils.loadAnimation(getContext(), com.irozon.sneaker.R.anim.popup_hide));
//                getActivityDecorView().removeView(getLayout());
//            }
//        }
    }


