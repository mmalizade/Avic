package ir.moovic.entertainment.ui.pager;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerTransformers {

    public static class FlipVerticalTransformer extends BaseTransformer {
        @Override
        protected void onTransform(View view, float position) {
            final float rotation = -180f * position;

            view.setVisibility(rotation > 90f || rotation < -90f ? View.INVISIBLE : View.VISIBLE);
            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationX(rotation);
        }
    }

    public static class ForegroundToBackgroundTransformer extends BaseTransformer {

        @Override
        protected void onTransform(View view, float position) {
            final float height = view.getHeight();
            final float width = view.getWidth();
            final float scale = min(position > 0 ? 1f : Math.abs(1f + position), 0.5f);

            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setPivotX(width * 0.5f);
            view.setPivotY(height * 0.5f);
            view.setTranslationX(position > 0 ? (width * position) : ( -width * position * 0.25f) );
        }

        private static final float min(float val, float min) {
            return val < min ? min : val;
        }

    }

    public static class ParallaxPageTransformer extends BaseTransformer {

        private final int viewToParallax;

        public ParallaxPageTransformer(final int viewToParallax) {
            this.viewToParallax = viewToParallax;

        }

        @Override
        protected void onTransform(View view, float position) {
            int pageWidth = view.getWidth();


            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(1);

            } else if (position <= 1) { // [-1,1]

                view.findViewById(viewToParallax).setTranslationX(-position * (pageWidth / 2)); //Half the normal speed

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(1);
            }


        }

    }

    public static class RotateDownTransformer extends BaseTransformer {

        private static final float ROT_MOD = -15f;

        @Override
        protected void onTransform(View view, float position) {
            final float width = view.getWidth();
            final float height = view.getHeight();
            final float rotation = ROT_MOD * position * -1.25f;

            view.setPivotX(width * 0.5f);
            view.setPivotY(height);
            view.setRotation(rotation);
        }

        @Override
        protected boolean isPagingEnabled() {
            return true;
        }

    }

    public static class RotateUpTransformer extends BaseTransformer {

        private static final float ROT_MOD = -15f;

        @Override
        protected void onTransform(View view, float position) {
            final float width = view.getWidth();
            final float rotation = ROT_MOD * position;

            view.setPivotX(width * 0.5f);
            view.setPivotY(0f);
            view.setTranslationX(0f);
            view.setRotation(rotation);
        }

        @Override
        protected boolean isPagingEnabled() {
            return true;
        }

    }

    public static class TabletTransformer extends BaseTransformer {

        private static final Matrix OFFSET_MATRIX = new Matrix();
        private static final Camera OFFSET_CAMERA = new Camera();
        private static final float[] OFFSET_TEMP_FLOAT = new float[2];

        @Override
        protected void onTransform(View view, float position) {
            final float rotation = (position < 0 ? 30f : -30f) * Math.abs(position);

            view.setTranslationX(getOffsetXForRotation(rotation, view.getWidth(), view.getHeight()));
            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(0);
            view.setRotationY(rotation);
        }

        protected static final float getOffsetXForRotation(float degrees, int width, int height) {
            OFFSET_MATRIX.reset();
            OFFSET_CAMERA.save();
            OFFSET_CAMERA.rotateY(Math.abs(degrees));
            OFFSET_CAMERA.getMatrix(OFFSET_MATRIX);
            OFFSET_CAMERA.restore();

            OFFSET_MATRIX.preTranslate(-width * 0.5f, -height * 0.5f);
            OFFSET_MATRIX.postTranslate(width * 0.5f, height * 0.5f);
            OFFSET_TEMP_FLOAT[0] = width;
            OFFSET_TEMP_FLOAT[1] = height;
            OFFSET_MATRIX.mapPoints(OFFSET_TEMP_FLOAT);
            return (width - OFFSET_TEMP_FLOAT[0]) * (degrees > 0.0f ? 1.0f : -1.0f);
        }

    }

    public static class ZoomInTransformer extends BaseTransformer {

        @Override
        protected void onTransform(View view, float position) {
            final float scale = position < 0 ? position + 1f : Math.abs(1f - position);
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setAlpha(position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
        }

    }

    public static class ZoomOutSlideTransformer extends BaseTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        protected void onTransform(View view, float position) {
            if (position >= -1 || position <= 1) {
                // Modify the default slide transition to shrink the page as well
                final float height = view.getHeight();
                final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                final float vertMargin = height * (1 - scaleFactor) / 2;
                final float horzMargin = view.getWidth() * (1 - scaleFactor) / 2;

                // Center vertically
                view.setPivotY(0.5f * height);

                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            }
        }

    }

    public static class ZoomOutTranformer extends BaseTransformer {

        @Override
        protected void onTransform(View view, float position) {
            final float scale = 1f + Math.abs(position);
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setAlpha(position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
            if(position == -1){
                view.setTranslationX(view.getWidth() * -1);
            }
        }

    }

    public static class FlipHorizontalTransformer extends BaseTransformer {

        @Override
        protected void onTransform(View view, float position) {
            final float rotation = 180f * position;

            view.setVisibility(rotation > 90f || rotation < -90f ? View.INVISIBLE : View.VISIBLE);
            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationY(rotation);
        }

    }

    public static class DrawFromBackTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.75f;

        @Override
        public void transformPage(@NonNull View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1 || position > 1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);
                return;
            }

            if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                // Fade the page out.
                view.setAlpha(1 + position);
                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                return;

            }

            if (position > 0.5 && position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(0);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);
                return;
            }
            if (position > 0.3 && position <= 0.5) { // (0,1]
                // Fade the page out.
                view.setAlpha(1);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * position);

                float scaleFactor = MIN_SCALE;
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                return;
            }
            if (position <= 0.3) { // (0,1]
                // Fade the page out.
                view.setAlpha(1);
                // Counteract the default slide transition
                view.setTranslationX(pageWidth * position);

                // Scale the page down (between MIN_SCALE and 1)
                float v = (float) (0.3 - position);
                v = v >= 0.25f ? 0.25f : v;
                float scaleFactor = MIN_SCALE + v;
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            }
        }
    }

    public static class DepthPageTransformer extends BaseTransformer {

        private static final float MIN_SCALE = 0.75f;

        @Override
        protected void onTransform(View view, float position) {
            if (position <= 0f) {
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);
            } else if (position <= 1f) {
                final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setAlpha(1 - position);
                view.setPivotY(0.5f * view.getHeight());
                view.setTranslationX(view.getWidth() * -position);
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            }
        }

        @Override
        protected boolean isPagingEnabled() {
            return true;
        }

    }

    public static class DefaultTransformer extends BaseTransformer {

        @Override
        protected void onTransform(View view, float position) {
        }

        @Override
        public boolean isPagingEnabled() {
            return true;
        }

    }

    public static class CubeOutTransformer extends BaseTransformer {

        @Override
        protected void onTransform(View view, float position) {
            view.setPivotX(position < 0f ? view.getWidth() : 0f);
            view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationY(90f * position);
        }

        @Override
        public boolean isPagingEnabled() {
            return true;
        }

    }

    public static class CubeInTransformer extends BaseTransformer {

        @Override
        protected void onTransform(View view, float position) {
            // Rotate the fragment on the left or right edge
            view.setPivotX(position > 0 ? 0 : view.getWidth());
            view.setPivotY(0);
            view.setRotationY(-90f * position);
        }

        @Override
        public boolean isPagingEnabled() {
            return true;
        }

    }

    public static class BackgroundToForegroundTransformer extends BaseTransformer {

        @Override
        protected void onTransform(View view, float position) {
            final float height = view.getHeight();
            final float width = view.getWidth();
            final float scale = min(position < 0 ? 1f : Math.abs(1f - position), 0.5f);

            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setPivotX(width * 0.5f);
            view.setPivotY(height * 0.5f);
            view.setTranslationX(position < 0 ? width * position : -width * position * 0.25f);
        }

        private static final float min(float val, float min) {
            return val < min ? min : val;
        }

    }

    public static class AccordionTransformer extends BaseTransformer {

        @Override
        protected void onTransform(View view, float position) {
            view.setPivotX(position < 0 ? 0 : view.getWidth());
            view.setScaleX(position < 0 ? 1f + position : 1f - position);
        }

    }

    public abstract static class BaseTransformer implements ViewPager.PageTransformer {

        protected abstract void onTransform(View view, float position);

        @Override
        public void transformPage(View view, float position) {
            onPreTransform(view, position);
            onTransform(view, position);
            onPostTransform(view, position);
        }

        /**
         * If the position offset of a fragment is less than negative one or greater than one, returning true will set the
         * visibility of the fragment to {@link android.view.View#GONE}. Returning false will force the fragment to {@link android.view.View#VISIBLE}.
         *
         * @return
         */
        protected boolean hideOffscreenPages() {
            return true;
        }

        /**
         * Indicates if the default animations of the view pager should be used.
         *
         * @return
         */
        protected boolean isPagingEnabled() {
            return false;
        }

        /**
         * Called each {@link #transformPage(android.view.View, float)} before {{@link #onTransform(android.view.View, float)} is called.
         *
         * @param view
         * @param position
         */
        protected void onPreTransform(View view, float position) {
            final float width = view.getWidth();

            view.setRotationX(0);
            view.setRotationY(0);
            view.setRotation(0);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setPivotX(0);
            view.setPivotY(0);
            view.setTranslationY(0);
            view.setTranslationX(isPagingEnabled() ? 0f : -width * position);

            if (hideOffscreenPages()) {
                view.setAlpha(position <= -1f || position >= 1f ? 0f : 1f);
            } else {
                view.setAlpha(1f);
            }
        }

        /**
         * Called each {@link #transformPage(android.view.View, float)} callbacks after {@link #onTransform(android.view.View, float)} is finished.
         *
         * @param view
         * @param position
         */
        protected void onPostTransform(View view, float position) {
        }

    }

}
