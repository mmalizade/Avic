package ir.moovic.entertainment.ui.helper;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private boolean enabled = true;
    private int mPreviousTotal = 0;
    private boolean mLoading = true;
    private int mVisibleThreshold = -1;
    private boolean mIsOrientationHelperVertical = false;
    private OrientationHelper mOrientationHelper = null;
    private int currentPage = 0;
    private RecyclerView.LayoutManager layoutManager = null;

    public EndlessRecyclerOnScrollListener() {

    }

    public EndlessRecyclerOnScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public EndlessRecyclerOnScrollListener(int visibleThreshold) {
        this.mVisibleThreshold = visibleThreshold;
    }

    public EndlessRecyclerOnScrollListener(RecyclerView.LayoutManager layoutManager, int visibleThreshold) {
        this.layoutManager = layoutManager;
        this.mVisibleThreshold = visibleThreshold;
    }


    private int findFirstVisibleItemPosition(RecyclerView recyclerView) {
        View child = findOneVisibleChild(0, layoutManager.getChildCount(), false, true);
        if (child == null)
            return RecyclerView.NO_POSITION;
        else
            return recyclerView.getChildAdapterPosition(child);
    }

    private int findLastVisibleItemPosition(RecyclerView recyclerView) {
        View child = findOneVisibleChild(recyclerView.getChildCount() - 1, -1, false, true);
        if (child == null)
            return RecyclerView.NO_POSITION;
        else
            return recyclerView.getChildAdapterPosition(child);
    }

    private View findOneVisibleChild(int fromIndex, int toIndex, boolean completelyVisible, boolean acceptPartiallyVisible){
        if (layoutManager.canScrollVertically() != mIsOrientationHelperVertical || mOrientationHelper == null) {
            mIsOrientationHelperVertical = layoutManager.canScrollVertically();
            mOrientationHelper = (mIsOrientationHelperVertical) ? OrientationHelper.createVerticalHelper(layoutManager) : OrientationHelper.createHorizontalHelper(layoutManager);
        }

        if(mOrientationHelper == null) return null;


        int start = mOrientationHelper.getStartAfterPadding();
        int end = mOrientationHelper.getEndAfterPadding();
        int next = (toIndex > fromIndex) ? 1 : -1;
        View partiallyVisible = null;
        int i = fromIndex;
        while (i != toIndex) {
            View child = layoutManager.getChildAt(i);
            if (child != null) {
                int childStart = mOrientationHelper.getDecoratedStart(child);
                int childEnd = mOrientationHelper.getDecoratedEnd(child);
                if (childStart < end && childEnd > start) {
                    if (completelyVisible) {
                        if (childStart >= start && childEnd <= end) {
                            return child;
                        } else if (acceptPartiallyVisible && partiallyVisible == null) {
                            partiallyVisible = child;
                        }
                    } else {
                        return child;
                    }
                }
            }
            i += next;
        }
        return partiallyVisible;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (enabled) {
            if (layoutManager == null) {
                layoutManager = recyclerView.getLayoutManager();
                if(layoutManager == null) {
//                    throw new RuntimeException("A LayoutManager is required")
                    return;
                }
            }

            int footerItemCount = 0; // reserved for future

            if (mVisibleThreshold == -1)
                mVisibleThreshold = findLastVisibleItemPosition(recyclerView) - findFirstVisibleItemPosition(recyclerView) - footerItemCount;

            int visibleItemCount = recyclerView.getChildCount() - footerItemCount;
            int totalItemCount = layoutManager.getItemCount() - footerItemCount;
            int firstVisibleItem = findFirstVisibleItemPosition(recyclerView);

            if (mLoading) {
                if (totalItemCount > mPreviousTotal) {
                    mLoading = false;
                    mPreviousTotal = totalItemCount;
                }
            }
            if (!mLoading && totalItemCount - visibleItemCount <= firstVisibleItem + mVisibleThreshold) {

                currentPage++;

                onLoadMore(currentPage);

                mLoading = true;
            }
        }
    }

    public EndlessRecyclerOnScrollListener enable() {
        this.enabled = true;
        return this;
    }

    public EndlessRecyclerOnScrollListener disable() {
        this.enabled = true;
        return this;
    }

    public void resetPageCount(){
        this.resetPageCount(0);
    }

    public void resetPageCount(int page) {
        mPreviousTotal = 0;
        mLoading = true;
        currentPage = page;
        onLoadMore(currentPage);
    }

    public abstract void onLoadMore(int currentPage);
}
