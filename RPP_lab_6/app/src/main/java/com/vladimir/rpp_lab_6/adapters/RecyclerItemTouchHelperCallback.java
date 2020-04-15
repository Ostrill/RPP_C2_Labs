package com.vladimir.rpp_lab_6.adapters;

import com.vladimir.rpp_lab_6.database.ShopEntity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

    @Nullable
    private OnSwipeListener onSwipeListener;

    public RecyclerItemTouchHelperCallback(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    @Override
    public boolean onMove(
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            @NonNull RecyclerView.ViewHolder target
    ) {
        return false;
    }

    @Override
    public void onSwiped(
            @NonNull RecyclerView.ViewHolder viewHolder,
            int direction
    ) {
        if (onSwipeListener != null) {
            onSwipeListener.onSwipe(((BackRecyclerAdapter.ViewHolder)viewHolder).getProduct());
        }
    }

    public void setOnSwipeListener(@Nullable OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    public interface OnSwipeListener {
        void onSwipe(ShopEntity product);
    }

}
