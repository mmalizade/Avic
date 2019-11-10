package ir.moovic.entertainment.ui.helper.spannedgridlman;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class SpannedGridLayoutManager extends RecyclerView.LayoutManager{

    private static final boolean DEBUG = true;
    private static final String LOGTAG = "SpannedGrid";

    public enum Orientation {
        VERTICAL, HORIZONTAL
    }

    public enum Direction {
        START, END
    }

    public Orientation orientation;
    public int spans;

    protected int scroll = 0;

    public int firstVisiblePosition  = 0;
    public int lastVisiblePosition = 0;
    protected int layoutStart = 0;
    protected int layoutEnd = 0;
    public int size;
    protected SparseArray<Rect> childFrames;
    protected Integer pendingScrollToPosition = null;
    public boolean itemOrderIsStable = false;
    protected SpanSizeLookup spanSizeLookup = null;
    protected RectsHelper rectsHelper;

    public SpannedGridLayoutManager(Orientation orientation, int spans) {
        this.orientation = orientation;
        this.spans = spans;
        if(spans < 1) {
            throw new InvalidMaxSpansException(spans);
        }
        childFrames = new SparseArray<>();
        spanSizeLookup = new DefaultSpanSizeLookup();
    }

    public int getSpans() {
        return spans;
    }

    public int getFirstVisiblePosition() {
        if(getChildCount() != 0) {
            View child = getChildAt(0);
            if(child != null) {
                return getPosition(child);
            }
        }
        return 0;
    }

    public int getLastVisiblePosition() {
        if(getChildCount() == 0) return 0;
        View child = getChildAt(getChildCount() - 1);
        if(child != null) return getPosition(child);
        return 0;
    }

    public int getSize() {
        if (orientation == Orientation.VERTICAL)
            return getHeight();
        else
            return getWidth();

    }


    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.spanSizeLookup = spanSizeLookup;

    }

    //==============================================================================================
    //  ~ Override parent
    //==============================================================================================

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        );
    }

    //==============================================================================================
    //  ~ View layouting methods
    //==============================================================================================


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        rectsHelper = new RectsHelper(this, orientation);

        layoutStart = getPaddingStartForOrientation();

        if (scroll != 0) {
            int currentRow = (scroll - layoutStart) / rectsHelper.getItemSize();
            layoutEnd = currentRow * rectsHelper.getItemSize();
        } else {
            layoutEnd = getPaddingEndForOrientation();
        }

        // Clear cache, since layout may change
        childFrames.clear();

        // If there were any views, detach them so they can be recycled
        detachAndScrapAttachedViews(recycler);

        long start = System.currentTimeMillis();

        int itemCount = state.getItemCount();
        for (int i = 0; i <  itemCount; i++) {
            SpanSize spanSize = spanSizeLookup.getSpanSize(i);
            Rect childRect = rectsHelper.findRect(i, spanSize);
            rectsHelper.pushRect(i, childRect);
        }

        if (DEBUG) {
            long elapsed = System.currentTimeMillis() - start;
            debugLog("Elapsed time: " + elapsed + "ms");
        }

        // Restore scroll position based on first visible view

        if (itemCount != 0 && pendingScrollToPosition != null && pendingScrollToPosition >= spans) {

            int currentRow = -1;
            for(int i = 0; i < rectsHelper.rows.size(); i++) {
                int key = rectsHelper.rows.keyAt(i);
                Set<Integer> rowPositions = rectsHelper.rows.get(key);
                if(rowPositions.contains(pendingScrollToPosition)) {
                    currentRow = key;
                }
            }
            if (currentRow >= 0) {
                scroll = getPaddingStartForOrientation() + (currentRow * rectsHelper.getItemSize());
            }

            this.pendingScrollToPosition = null;
        }

        // Fill from start to visible end
        fillGap(Direction.END, recycler, state);

        recycleChildrenOutOfBounds(Direction.END, recycler);

        // Check if after changes in layout we aren't out of its bounds
        int overScroll = scroll + size - layoutEnd - getPaddingEndForOrientation();

        boolean isLastItemInScreen = false;
        int childCount = getChildCount();
        for(int i = 0; i < childCount ; i++) {
            View child = getChildAt(i);
            if(child != null) {
                int pos = getPosition(child);
                if(pos == itemCount-1) {
                    isLastItemInScreen = true;
                }
            }
        }

        boolean allItemsInScreen = itemCount == 0 || (firstVisiblePosition == 0 && isLastItemInScreen);
        if (!allItemsInScreen && overScroll > 0) {
            // If we are, fix it
            scrollBy(overScroll, state);

            if (overScroll > 0) {
                fillBefore(recycler);
            } else {
                fillAfter(recycler);
            }
        }
    }

    /**
     * Measure child view using [RectsHelper]
     */
    protected void measureChild(int position, View view) {

        RectsHelper freeRectsHelper = this.rectsHelper;

        int itemWidth = freeRectsHelper.getItemSize();
        int itemHeight = freeRectsHelper.getItemSize();

        SpanSize spanSize = spanSizeLookup.getSpanSize(position);

        int usedSpan = (orientation == Orientation.HORIZONTAL) ? spanSize.height : spanSize.width;

        if (usedSpan > this.spans || usedSpan < 1) {
            throw new InvalidSpanSizeException(usedSpan, spans);
        }

        // This rect contains just the row and column number - i.e.: [0, 0, 1, 1]
        Rect rect = freeRectsHelper.findRect(position, spanSize);

        // Multiply the rect for item width and height to get positions
        int left = rect.left * itemWidth;
        int right = rect.right * itemWidth;
        int top = rect.top * itemHeight;
        int bottom = rect.bottom * itemHeight;

        Rect insetsRect = new Rect();
        calculateItemDecorationsForChild(view, insetsRect);

        // Measure child
        int width = right - left - insetsRect.left - insetsRect.right;
        int height = bottom - top - insetsRect.top - insetsRect.bottom;
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        measureChildWithMargins(view, width, height);

        // Cache rect
        childFrames.put(position, new Rect(left, top, right, bottom));
    }


    /**
     * Layout child once it's measured and its position cached
     */
    protected void layoutChild(int position, View view) {
        Rect frame = childFrames.get(position);

        if (frame != null) {
            int scroll = this.scroll;
            int startPadding = getPaddingStartForOrientation();

            if (orientation == Orientation.VERTICAL) {
                layoutDecorated(view,
                        frame.left + getPaddingLeft(),
                        frame.top - scroll + startPadding,
                        frame.right + getPaddingLeft(),
                        frame.bottom - scroll + startPadding);
            } else {
                layoutDecorated(view,
                        frame.left - scroll + startPadding,
                        frame.top + getPaddingTop(),
                        frame.right - scroll + startPadding,
                        frame.bottom + getPaddingTop());
            }
        }

        // A new child was layouted, layout edges change
        updateEdgesWithNewChild(view);
    }

    /**
     * Ask the recycler for a view, measure and layout it and add it to the layout
     */
    protected View makeAndAddView(int position, Direction direction, RecyclerView.Recycler recycler) {
        View view = makeView(position, direction, recycler);
        if (direction == Direction.END) {
            addView(view);
        } else {
            addView(view, 0);
        }
        return view;
    }

    protected View makeView(int position, Direction direction, RecyclerView.Recycler recycler) {
        View view = recycler.getViewForPosition(position);
        measureChild(position, view);
        layoutChild(position, view);
        return view;
    }

    /**
     * A new view was added, update layout edges if needed
     */
    protected void updateEdgesWithNewChild(View view) {
        int childStart = getChildStart(view) + scroll + getPaddingStartForOrientation();

        if (childStart < layoutStart) {
            layoutStart = childStart;
        }

        int newLayoutEnd = childStart + rectsHelper.getItemSize();

        if (newLayoutEnd > layoutEnd) {
            layoutEnd = newLayoutEnd;
        }
    }

    //==============================================================================================
    //  ~ Scroll methods
    //==============================================================================================


    @Override
    public int computeVerticalScrollOffset(@NonNull RecyclerView.State state) {
        return computeScrollOffset();
    }


    @Override
    public int computeHorizontalScrollOffset(RecyclerView.State state) {
        return computeScrollOffset();
    }

    private int computeScrollOffset() {
        if (getChildCount() == 0) return 0;
        else return getFirstVisiblePosition();
    }

    @Override
    public int computeVerticalScrollExtent(@NonNull RecyclerView.State state) {
        return getChildCount();
    }

    @Override
    public int computeHorizontalScrollExtent(@NonNull RecyclerView.State state) {
        return getChildCount();
    }

    @Override
    public int computeVerticalScrollRange(@NonNull RecyclerView.State state) {
        return state.getItemCount();
    }

    @Override
    public int computeHorizontalScrollRange(@NonNull RecyclerView.State state) {
        return state.getItemCount();
    }

    @Override
    public boolean canScrollVertically() {
        return orientation == Orientation.VERTICAL;
    }

    @Override
    public boolean canScrollHorizontally() {
        return orientation == Orientation.HORIZONTAL;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return scrollBy(dy, recycler, state);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return scrollBy(dx, recycler, state);
    }

    protected int scrollBy(int delta, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // If there are no view or no movement, return
        if (delta == 0) {
            return 0;
        }

        boolean canScrollBackwards = (firstVisiblePosition) >= 0 &&
                0 < scroll &&
                delta < 0;

        int childCount = getChildCount();
        boolean canScrollForward = (firstVisiblePosition + childCount) <= state.getItemCount()&&
                (scroll + size) < (layoutEnd + rectsHelper.getItemSize() + getPaddingEndForOrientation()) && delta > 0;

        // If can't scroll forward or backwards, return
        if (!(canScrollBackwards || canScrollForward)) {
            return 0;
        }

        int correctedDistance = scrollBy(-delta, state);

        Direction direction = (delta > 0) ? Direction.END : Direction.START;

        recycleChildrenOutOfBounds(direction, recycler);

        fillGap(direction, recycler, state);

        return -correctedDistance;
    }

    /**
     * Scrolls distance based on orientation. Corrects distance if out of bounds.
     */
    protected int scrollBy(int distance, RecyclerView.State state) {
        int paddingEndLayout = getPaddingEndForOrientation();
        int start = 0;
        int end = layoutEnd + rectsHelper.getItemSize() + paddingEndLayout;

        scroll -= distance;

        int correctedDistance = distance;

        // Correct scroll if was out of bounds at start
        if (scroll < start) {
            correctedDistance += scroll;
            scroll = start;
        }

        // Correct scroll if it would make the layout scroll out of bounds at the end
        int childCount = getChildCount();
        if (scroll + size > end && (firstVisiblePosition + childCount + spans) >= state.getItemCount()) {
            correctedDistance -= (end - scroll - size);
            scroll = end - size;
        }

        if (orientation == Orientation.VERTICAL) {
            offsetChildrenVertical(correctedDistance);
        } else{
            offsetChildrenHorizontal(correctedDistance);
        }

        return correctedDistance;
    }


    @Override
    public void scrollToPosition(int position) {
        pendingScrollToPosition = position;
        requestLayout();
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        SmoothScroller smoothScroller = new SmoothScroller(recyclerView.getContext(), this);
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }


    /**
     * Fills gaps on the layout, on directions [Direction.START] or [Direction.END]
     */
    protected void fillGap(Direction direction, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (direction == Direction.END) {
            fillAfter(recycler);
        } else {
            fillBefore(recycler);
        }
    }

    /**
     * Fill gaps before the current visible scroll position
     * @param recycler Recycler
     */
    protected void fillBefore(RecyclerView.Recycler recycler) {
        int currentRow = (scroll - getPaddingStartForOrientation()) / rectsHelper.getItemSize();
        int lastRow = (scroll + size - getPaddingStartForOrientation()) / rectsHelper.getItemSize();

        for (int row = lastRow-1; row >= currentRow; row--) {
            List<Integer> list = new ArrayList<>(rectsHelper.findPositionsForRow(row));
            Collections.sort(list, Collections.reverseOrder());
            Set<Integer> positionsForRow = new LinkedHashSet<>(list);


            for (int position : positionsForRow) {
                if (findViewByPosition(position) != null)
                    continue;
                makeAndAddView(position, Direction.START, recycler);
            }
        }
    }

    /**
     * Fill gaps after the current layouted views
     * @param recycler Recycler
     */
    protected void fillAfter(RecyclerView.Recycler recycler) {
        int visibleEnd = scroll + size;

        int lastAddedRow = layoutEnd / rectsHelper.getItemSize();
        int lastVisibleRow =  visibleEnd / rectsHelper.getItemSize();

        for (int rowIndex = lastAddedRow; rowIndex <= lastVisibleRow; rowIndex++) {
            Set<Integer> row = rectsHelper.rows.get(rowIndex);
            if(row == null || row.isEmpty()) continue;

            for (int itemIndex : row) {

                if (findViewByPosition(itemIndex) != null)
                    continue;

                    makeAndAddView(itemIndex, Direction.END, recycler);
            }
        }
    }

    //==============================================================================================
    //  ~ Decorated position and sizes
    //==============================================================================================

    @Override
    public int getDecoratedMeasuredWidth(@NonNull View child) {
        int position = getPosition(child);
        Rect rect = childFrames.get(position);
        if(rect != null){
            return rect.width();
        }
        return 0;
    }


    @Override
    public int getDecoratedMeasuredHeight(@NonNull View child) {
        int position = getPosition(child);
        Rect rect = childFrames.get(position);
        if(rect != null){
            return rect.height();
        }
        return 0;
    }

    @Override
    public int getDecoratedTop(@NonNull View child) {
        int position = getPosition(child);
        int decoration = getTopDecorationHeight(child);
        Rect rect = childFrames.get(position);
        int top = (rect != null ? rect.top : 0) + decoration;
        if (orientation == Orientation.VERTICAL) {
            top -= scroll;
        }

        return top;
    }

    @Override
    public int getDecoratedRight(@NonNull View child) {
        int position = getPosition(child);
        int decoration = getLeftDecorationWidth(child) + getRightDecorationWidth(child);
        Rect rect = childFrames.get(position);
        int right = (rect == null ? 0 : rect.right) + decoration;

        if (orientation == Orientation.HORIZONTAL) {
            right -= scroll - getPaddingStartForOrientation();
        }

        return right;
    }

    @Override
    public int getDecoratedLeft(@NonNull View child) {
        int position = getPosition(child);
        int decoration = getLeftDecorationWidth(child);
        Rect rect = childFrames.get(position);
        int left = (rect == null ? 0 : rect.left) + decoration;

        if (orientation == Orientation.HORIZONTAL) {
            left -= scroll;
        }

        return left;
    }

    @Override
    public int getDecoratedBottom(@NonNull View child) {
        int position = getPosition(child);
        int decoration = getTopDecorationHeight(child) + getBottomDecorationHeight(child);
        Rect rect = childFrames.get(position);
        int bottom = (rect != null ? rect.bottom : 0) + decoration;

        if (orientation == Orientation.VERTICAL) {
            bottom -= scroll - getPaddingStartForOrientation();
        }
        return bottom;
    }


    //==============================================================================================
    //  ~ Orientation Utils
    //==============================================================================================

    protected int getPaddingStartForOrientation() {
        if (orientation == Orientation.VERTICAL) {
            return getPaddingTop();
        } else {
            return getPaddingLeft();
        }
    }

    protected int getPaddingEndForOrientation() {
        if (orientation == Orientation.VERTICAL) {
            return getPaddingBottom();
        } else {
            return getPaddingRight();
        }
    }

    protected int getChildStart(View child)  {
        if (orientation == Orientation.VERTICAL) {
            return getDecoratedTop(child);
        } else {
            return getDecoratedLeft(child);
        }
    }

    protected int getChildEnd(View child){
        if (orientation == Orientation.VERTICAL) {
            return getDecoratedBottom(child);
        } else {
            return getDecoratedRight(child);
        }
    }

    //==============================================================================================
    //  ~ Recycling methods
    //==============================================================================================

    /**
     * Recycle any views that are out of bounds
     */
    protected void recycleChildrenOutOfBounds(Direction direction, RecyclerView.Recycler recycler) {
        if (direction == Direction.END) {
            recycleChildrenFromStart(direction, recycler);
        } else {
            recycleChildrenFromEnd(direction, recycler);
        }
    }

    /**
     * Recycle views from start to first visible item
     */
    protected void recycleChildrenFromStart(Direction direction, RecyclerView.Recycler recycler) {
        int childCount = getChildCount();
        int start = getPaddingStartForOrientation();

        List<View> toDetach = new ArrayList<>();

        for (int i =0;i <childCount; i++) {
            View child = getChildAt(i);
            if(child != null) {
                int childEnd = getChildEnd(child);
                if (childEnd < start) {
                    toDetach.add(child);
                }
            }

        }

        for (View child : toDetach) {
            removeAndRecycleView(child, recycler);
            updateEdgesWithRemovedChild(child, direction);
        }
    }

    /**
     * Recycle views from end to last visible item
     */
    protected void recycleChildrenFromEnd(Direction direction, RecyclerView.Recycler recycler) {
        int childCount = getChildCount();
        int end = size + getPaddingEndForOrientation();

        List<View> toDetach = new ArrayList<>();

        for (int i = childCount-1; i >= 0; i--) {
            View child = getChildAt(i);
            if(child != null) {
                int childEnd = getChildEnd(child);
                if (childEnd > end) {
                    toDetach.add(child);
                }
            }
        }

        for (View child : toDetach) {
            removeAndRecycleView(child, recycler);
            updateEdgesWithRemovedChild(child, direction);
        }

    }

    /**
     * Update layout edges when views are recycled
     */
    protected void updateEdgesWithRemovedChild(View view, Direction direction) {
        int childStart = getChildStart(view) + scroll;
        int childEnd = getChildEnd(view) + scroll;

        if (direction == Direction.END) { // Removed from start
            layoutStart = getPaddingStartForOrientation() + childEnd;
        } else if (direction == Direction.START) { // Removed from end
            layoutEnd = getPaddingStartForOrientation() + childStart;
        }
    }

    //==============================================================================================
    //  ~ Save & Restore State
    //==============================================================================================


    @Nullable
    @Override
    public Parcelable onSaveInstanceState() {
        if (itemOrderIsStable && getChildCount() > 0) {
            debugLog("Saving first visible position: $firstVisiblePosition");
            return new SavedState(firstVisiblePosition);
        } else {
            return null;
        }
    }


    @Override
    public void onRestoreInstanceState(Parcelable state) {
        debugLog("Restoring state");
        SavedState savedState = (SavedState) state ;
        if (savedState != null) {
            int firstVisibleItem = savedState.firstVisibleItem;
            scrollToPosition(firstVisibleItem);
        }
    }

    //==============================================================================================
    //  ~ Helper Class
    //==============================================================================================

    public static class SavedState implements Parcelable {
        public int firstVisibleItem;


        public SavedState(int firstVisibleItem) {
            this.firstVisibleItem = firstVisibleItem;
        }


        protected SavedState(Parcel in) {
            firstVisibleItem = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(firstVisibleItem);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


    private void debugLog(String s) {
        Log.d(LOGTAG, s);
    }


    public abstract static class SpanSizeLookup {
        public final SparseArray<SpanSize> cache;
        public boolean usesCache = false;
        public int spans = 1;

        public SpanSizeLookup() {
            this(1);
        }
        public SpanSizeLookup(int fullSpans) {
            cache = new SparseArray<>();
            this.spans = fullSpans < 1 ? 1 : fullSpans;
        }

        public void setUsesCache(boolean usesCache) {
            this.usesCache = usesCache;
            if(! usesCache) {
                invalidateCache();
            }
        }

        public SpanSize getSpanSize(int position) {
            if (usesCache) {
                SpanSize cachedValue = cache.get(position);
                if (cachedValue != null) return cachedValue;

                SpanSize value = getSpanSizeFromFunction(position);
                cache.put(position, value);
                return value;
            } else {
                return getSpanSizeFromFunction(position);
            }
        }

        private SpanSize getSpanSizeFromFunction(int position) {
            SpanSize value = spanSize(position);
            if(value != null) return value;
            else return getDefaultSpanSize();
        }

        protected SpanSize getDefaultSpanSize() {
            return new SpanSize(spans, 1);
        }

        public void invalidateCache() {
            cache.clear();
        }

        protected abstract SpanSize spanSize(int position);

    }

    public static class SpanSize {
        public int width;
        public int height;

        public SpanSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        @NonNull
        @Override
        public String toString() {
            return "SpanSize(" + width + "," + height + ")";
        }
    }

    public static class SmoothScroller extends LinearSmoothScroller {
        private final WeakReference<SpannedGridLayoutManager> lmanRef;

        public SmoothScroller(Context context, SpannedGridLayoutManager layoutManager) {
            super(context);
            lmanRef = new WeakReference<>(layoutManager);
        }

        @Nullable
        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            SpannedGridLayoutManager layoutManager = lmanRef.get();
            int childCount = (layoutManager == null) ? 0 : layoutManager.getChildCount();
            if (childCount == 0) {
                return null;
            }

            float direction = (targetPosition < layoutManager.getFirstVisiblePosition()) ? -1f : 1f;
            return new PointF(0f, direction);
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }

    private static class RectComparator implements Comparator<Rect> {
        private final WeakReference<RectsHelper> ref;
        public RectComparator(RectsHelper rectsHelper) {
            this.ref = new WeakReference<>(rectsHelper);
        }

        @Override
        public int compare(Rect rect1, Rect rect2) {
            RectsHelper rectsHelper = ref.get();
            if(rectsHelper == null) return 0;
            if(rectsHelper.getOrientation() == SpannedGridLayoutManager.Orientation.VERTICAL) {
                if (rect1.top == rect2.top) {
                    return (rect1.left < rect2.left) ? -1 : 1 ;
                } else {
                    return (rect1.top < rect2.top) ? -1 : 1 ;
                }
            } else if(rectsHelper.getOrientation() == SpannedGridLayoutManager.Orientation.HORIZONTAL) {
                if (rect1.left == rect2.left) {
                    return (rect1.top < rect2.top) ? -1:  1;
                } else {
                    return (rect1.left < rect2.left) ? -1 : 1;
                }
            }
            return 0;
        }

        public boolean areEqual(Rect rect1, Rect rect2) {
            return compare(rect1, rect2) == 0;
        }
    }

    public static class RectsHelper {
        private SpannedGridLayoutManager.Orientation orientation;
        private SpannedGridLayoutManager layoutManager;
        private final SparseArray<LinkedHashSet<Integer>> rows;
        private final SparseArray<Rect> rectsCache;
        private final List<Rect> freeRects;
        private RectComparator comparator;

        public RectsHelper(SpannedGridLayoutManager layoutManager, SpannedGridLayoutManager.Orientation orientation) {
            this.orientation = orientation;
            this.layoutManager = layoutManager;
            rows = new SparseArray<>();
            rectsCache = new SparseArray<>();
            freeRects = new ArrayList<>();
            comparator = new RectComparator(this);
            Rect initialFreeRect =
                    (orientation == SpannedGridLayoutManager.Orientation.VERTICAL) ?
                        new Rect(0, 0, layoutManager.spans, Integer.MAX_VALUE) :
                    new Rect(0, 0, Integer.MAX_VALUE, layoutManager.spans);
            freeRects.add(initialFreeRect);

        }

        public int getSize(){
            if (orientation == SpannedGridLayoutManager.Orientation.VERTICAL) {
                return layoutManager.getWidth() - layoutManager.getPaddingLeft() - layoutManager.getPaddingRight();
            } else {
                return layoutManager.getHeight()- layoutManager.getPaddingTop()- layoutManager.getPaddingBottom();
            }
        }

        public int getItemSize(){
            return getSize() / layoutManager.spans;
        }

        public int getStart() {
            Rect rect = freeRects.get(0);
            if(rect == null) {
                return 0;
            }
            int itemSize = getItemSize();
            if (orientation == SpannedGridLayoutManager.Orientation.VERTICAL) {
                return rect.top * itemSize;
            } else {
                return rect.left * itemSize;
            }
        }

        public int getEnd() {
            int count = freeRects.size();
            Rect rect = count > 0 ? freeRects.get(count-1) : null;
            if(rect == null) {
                return 0;
            }
            int itemSize = getItemSize();
            if (orientation == SpannedGridLayoutManager.Orientation.VERTICAL) {
                return (rect.top + 1) * itemSize;
            } else {
                return (rect.left + 1) * itemSize;
            }
        }

        /**
         * Get a free rect for the given span and item position
         */
        protected Rect findRect(int position, SpanSize spanSize) {
            Rect rect = rectsCache.get(position);
            return rect != null ? rect : findRectForSpanSize(spanSize);
        }

        /**
         * Find a valid free rect for the given span size
         */
        protected Rect findRectForSpanSize(SpanSize spanSize) {
            Log.e("DEBUGLOG", spanSize.toString() + " | free rects size = " + freeRects.size());
            if(!freeRects.isEmpty()) {
                for(Rect rect : freeRects) {
                    Rect itemRect = new Rect(rect.left, rect.top, rect.left + spanSize.getWidth(), rect.top + spanSize.getHeight());
                    if(rect.contains(itemRect)) {
                        return new Rect(rect.left, rect.top, rect.left+spanSize.width, rect.top + spanSize.height);
                    }
                }
            }
            return null;
        }

        public void pushRect(int position, Rect rect) {
            int start = (orientation == SpannedGridLayoutManager.Orientation.VERTICAL) ? rect.top : rect.left;
            LinkedHashSet<Integer> startRow = rows.get(start);
            if(startRow == null) {
                startRow = new LinkedHashSet<>();
            }

            startRow.add(position);
            rows.put(start, startRow);

            int end = (orientation == SpannedGridLayoutManager.Orientation.VERTICAL) ? rect.bottom : rect.right;
            LinkedHashSet<Integer> endRow = rows.get(end-1);
            if(endRow == null) {
                endRow = new LinkedHashSet<>();
            }
            endRow.add(position);
            rows.put(end - 1, endRow);
            rectsCache.put(position, rect);
            subtract(rect);
        }

        public Set<Integer> findPositionsForRow(int rowPosition) {
            Set<Integer> result = rows.get(rowPosition);
            return result != null ? result : new LinkedHashSet<>();
        }

        protected void subtract(Rect subtractedRect) {

            List<Rect> interestingRects = new ArrayList<>();
            Iterator<Rect> iterator = this.freeRects.iterator();

            while(iterator.hasNext()) {
                Rect item = iterator.next();
                if (RectExtenstion.isAdjacentTo(item, subtractedRect) || RectExtenstion.intersects(item, subtractedRect)) {
                    interestingRects.add(item);
                }
            }

            List<Rect> possibleNewRects = new ArrayList<>();
            List<Rect> adjacentRects = new ArrayList<>();


            for (Rect free: interestingRects) {
                if (RectExtenstion.isAdjacentTo(free, subtractedRect) && !subtractedRect.contains(free)) {
                    adjacentRects.add(free);
                } else {
                    freeRects.remove(free);
                    if (free.left < subtractedRect.left) { // Left
                        possibleNewRects.add(new Rect(free.left, free.top, subtractedRect.left, free.bottom));
                    }

                    if (free.right > subtractedRect.right) { // Right
                        possibleNewRects.add(new Rect(subtractedRect.right, free.top, free.right, free.bottom));
                    }

                    if (free.top < subtractedRect.top) { // Top
                        possibleNewRects.add(new Rect(free.left, free.top, free.right, subtractedRect.top));
                    }

                    if (free.bottom > subtractedRect.bottom) { // Bottom
                        possibleNewRects.add(new Rect(free.left, subtractedRect.bottom, free.right, free.bottom));
                    }
                }
            }

            for (Rect rect : possibleNewRects) {
                Iterator<Rect> iter = adjacentRects.iterator();
                Rect result = null;
                while(iter.hasNext()) {
                    Rect item = iter.next();
                    if(!comparator.areEqual(item, rect) && item.contains(rect)) {
                        result = item;
                        break;
                    }
                }
                if (result != null) continue;

                iter = possibleNewRects.iterator();
                while(iter.hasNext()) {
                    Rect item = iter.next();
                    if(!comparator.areEqual(item, rect) && item.contains(rect)) {
                        result = item;
                        break;
                    }
                }
                if (result != null) continue;
                freeRects.add(rect);

            }

            Collections.sort(freeRects, comparator);
        }

        public Orientation getOrientation() {
            return this.orientation;
        }
    }

    public class DefaultSpanSizeLookup extends SpanSizeLookup {

        @Override
        protected SpanSize spanSize(int position) {
            return null;
        }

    }

}
