package ir.moovic.entertainment.ui.helper;

import android.os.Handler;

import java.lang.ref.WeakReference;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewScrollToPosition implements Runnable {
        private long delay = 0;
        private WeakReference<RecyclerView> rvRef;
        private int position = 0;
        private boolean smooth = false;
        private Handler handler;

        public RecyclerViewScrollToPosition(RecyclerView rv ) {
            this.rvRef = new WeakReference<>(rv);
        }

        public RecyclerViewScrollToPosition withHandler(Handler handler) {
            this.handler = handler;
            return this;
        }

        public RecyclerViewScrollToPosition withDelay(long delay) {
            if(delay >= 0) {
                this.delay = delay;
            }
            return this;
        }

        @Override
        public void run() {
            RecyclerView rv = rvRef != null ? rvRef.get() : null;
            if(rv != null) {
                try {
                    if(rv.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager lman = (LinearLayoutManager) rv.getLayoutManager();
                        lman.scrollToPositionWithOffset(position, 0);
                    } else if(smooth) {
                        rv.smoothScrollToPosition(position);
                    } else {
                        rv.scrollToPosition(position);
                    }
                } catch (Exception e){}
            }
            this.position = 0;
            this.smooth = false;
        }

        public void scrollToPosition(int position) {
            scrollToPosition(position, false);
        }

        public void scrollToPosition(int position, boolean smooth) {
            this.position = position;
            this.smooth = smooth;
            if(this.delay > 0) {
                if(handler == null) {
                    handler = new Handler();
                }
                handler.postDelayed(this, delay);
            } else {
                run();
            }
        }
    }