package com.example.movieinfo.view.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class EmptyDataObserver extends RecyclerView.AdapterDataObserver {
    private final RecyclerView recyclerView;
    private final View emptyView;

    public EmptyDataObserver(RecyclerView recyclerView, View emptyView) {
        this.recyclerView = recyclerView;
        this.emptyView = emptyView;
        checkIfEmpty(true);
    }

    private void checkIfEmpty(boolean isInitChange) {
        if (emptyView != null && recyclerView.getAdapter() != null) {
            recyclerView.setAlpha(1);
            emptyView.setAlpha(1);
            boolean isEmpty = recyclerView.getAdapter().getItemCount() == 0;
            if (isEmpty) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                if (isInitChange) {
                    showRecyclerByCrossfade();
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * Show RecyclerView in cross fade animation
     */
    private void showRecyclerByCrossfade() {

        // Set the recyclerView to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        recyclerView.setAlpha(0f);
        recyclerView.setVisibility(View.VISIBLE);

        // Animate the recyclerView to 100% opacity, and clear any animation
        // listener set on the view.
        recyclerView.animate()
                .alpha(1f)
                .setDuration(200)
                .setListener(null);

        // Animate the emptyView to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        emptyView.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        emptyView.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onChanged() {
        super.onChanged();
        checkIfEmpty(true);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        checkIfEmpty(positionStart == 0);
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        checkIfEmpty(positionStart == 0);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        checkIfEmpty(positionStart == 0);
    }
}
