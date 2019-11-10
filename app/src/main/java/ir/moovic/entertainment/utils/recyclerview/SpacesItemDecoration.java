package ir.moovic.entertainment.utils.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    public static final int TYPE_MANUAL = 0;
    public static final int RTL = 1;
    public static final int GRID = 2;
    public static final int VERTICAL = 4;
    public static final int HORIZONTAL = 8;
    public static final int CHIPS = 16;

    public static final int RTL_GRID = RTL | GRID | VERTICAL;
    public static final int RTL_GRID_HORIZONTAL = RTL | GRID | HORIZONTAL;
    public static final int RTL_HORIZONTAL = RTL | HORIZONTAL;
    public static final int HORIZONTAL_CHIPS = HORIZONTAL | CHIPS;
    public static final int VERTICAL_CHIPS = VERTICAL | CHIPS;

    private int spaceRight, spaceLeft, spaceTop, spaceBottom;
    private int layoutType = TYPE_MANUAL;
    private int gridSpan = 3;
    private SparseArrayCompat<Factor> scaleFactors;

    public SpacesItemDecoration(int space, int type) {
        this(space, space, space, space, type);
    }

    public SpacesItemDecoration(int space, int type, int gridSpan) {
        this(space, type);
        this.gridSpan = gridSpan;
    }

    public SpacesItemDecoration(int left, int top, int right, int bottom, int type) {
        this.layoutType = type;
        this.spaceLeft = left;
        this.spaceTop = top;
        this.spaceRight = right;
        this.spaceBottom = bottom;
    }

    public SpacesItemDecoration(int left, int top, int right, int bottom) {
        this(left, top, right, bottom, TYPE_MANUAL);
    }

    public SpacesItemDecoration withGridSpanCount(int span){
        if(span > 0){
            this.gridSpan = span;
        }
        return this;
    }


    private Factor getScaleFactor(int position){
        if(scaleFactors == null) return null;
        return scaleFactors.get(position, null);
    }

    public void setScaleFactor(int position, float factor, int side){
        if(scaleFactors == null) {
            scaleFactors = new SparseArrayCompat<>();
        }
        scaleFactors.delete(position);
        scaleFactors.put(position, new Factor(factor, side));
    }

    private boolean isFirstItem(int pos){
        return isGrid() ? (pos < gridSpan) : (pos == 0);
    }

    private boolean isAtRightSideGrid(int pos) {
        return isGrid() && (isRtl() ? (pos % gridSpan == 0) : (pos % gridSpan == (gridSpan - 1)));
    }

    private boolean isAtLeftSideGrid(int pos) {
        return isGrid() && (!isRtl() ? (pos % gridSpan == 0) : (pos % gridSpan == (gridSpan - 1)));
    }

    private boolean isAtMiddleGrid(int pos){
        if(!isGrid()) return false;
        if(gridSpan <=2) return true;
        int r = pos % gridSpan;
        return r > 0 && r < (gridSpan-1);
    }

    public static int getTypeFromLayoutManager(RecyclerView.LayoutManager layoutManager){
        int value = VERTICAL;
        if(layoutManager instanceof GridLayoutManager){
            value = GRID;
            if(layoutManager instanceof RtlGridLayoutManager){
                value |= RTL;
            }
            if( ((GridLayoutManager) layoutManager).getOrientation() == RecyclerView.HORIZONTAL){
                value |= HORIZONTAL;
            } else {
                value |= VERTICAL;
            }
        } else if(layoutManager instanceof LinearLayoutManager){

            if(layoutManager instanceof RtlHorizontalLayoutManager){
                value = RTL_HORIZONTAL;
            } else if(((LinearLayoutManager) layoutManager).getOrientation() == RecyclerView.VERTICAL){
                value = VERTICAL;
            } else {
                value = HORIZONTAL;
            }
        }
        return value;
    }

    private boolean isGrid(){
        return (layoutType & GRID) != 0;
    }

    private boolean isRtl(){
        return (layoutType & RTL) != 0;
    }

    private boolean isVertical(){
        return (layoutType & VERTICAL) != 0;
    }

    private boolean isHorizontal(){
        return (layoutType & HORIZONTAL) != 0;
    }

    private boolean isChips(){
        return (layoutType & CHIPS) != 0;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        int left = spaceLeft;
        int right = spaceRight;
        int top = spaceTop;
        int bottom = spaceBottom;
        int position = -1;
        if(scaleFactors != null && scaleFactors.size() > 0){
            position = parent.getChildLayoutPosition(view);
            Factor factor = getScaleFactor(position);
            if(factor != null) {
                left = (int) (factor.leftFactor() * left);
                top = (int) (factor.topFactor() * top);
                right = (int) (factor.rightFactor() * right);
                bottom = (int) (factor.bottomFactor() * bottom);
            }
        }

        if(layoutType == TYPE_MANUAL){
            outRect.left = left/2;
            outRect.right = right/2;
            outRect.top = top/2;
            outRect.bottom = bottom/2;
            return;
        }

        if(position == -1){
            position = parent.getChildLayoutPosition(view);
        }
        boolean isFirst = isFirstItem(position);
        boolean isRightCol = isAtRightSideGrid(position);
        boolean isLeftCol = isAtLeftSideGrid(position);
        boolean isMidCol = isAtMiddleGrid(position);
        if(isChips()){
            outRect.left = left / 2;
            outRect.right = right / 2;
            outRect.bottom = bottom / 2;
            outRect.top = top / 2;
        } else if(isVertical()){
            if(isGrid()){
                int q = (right + left) / 4;
                if(isMidCol){
                    outRect.left = q;
                    outRect.right = q;
                } else if(isRightCol){
                    outRect.right = 2 * q;
                    outRect.left = 0;
                } else if(isLeftCol){
                    outRect.left = 2 * q;
                    outRect.right = 0;
                } else {
                    outRect.left = q;
                    outRect.right = q;
                }
            } else {
                outRect.left = left;
                outRect.right = right;
            }
            outRect.bottom = bottom;
            outRect.top = isFirst ? top : 0;
        } else if(isHorizontal()){
            outRect.bottom = bottom;
            outRect.top = top;
            if(isRtl()){
                outRect.left = left;
                outRect.right = isFirst ? right : 0;
            } else {
                outRect.right = right;
                outRect.left = isFirst ? left : 0;
            }
        }
    }


    public static class Factor {
        public static final int TOP = 1;
        public static final int LEFT = 2;
        public static final int RIGHT = 4;
        public static final int BOTTOM = 8;

        public int side = TOP|LEFT|RIGHT|BOTTOM;
        public float factor = 1f;

        public Factor(float factor, int side) {
            this.factor = Math.abs(factor);
            this.side = side;
        }

        public float topFactor(){
            if((side & TOP) != 0){
                return factor;
            }
            else return 1f;
        }
        public float rightFactor(){
            if((side & RIGHT) != 0){
                return factor;
            }
            else return 1f;
        }
        public float leftFactor(){
            if((side & LEFT) != 0){
                return factor;
            }
            else return 1f;
        }
        public float bottomFactor(){
            if((side & BOTTOM) != 0){
                return factor;
            }
            else return 1f;
        }
    }
}
