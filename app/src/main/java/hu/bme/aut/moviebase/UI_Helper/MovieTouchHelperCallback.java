package hu.bme.aut.moviebase.UI_Helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import hu.bme.aut.moviebase.adapter.MovieAdapter;

public class MovieTouchHelperCallback extends ItemTouchHelper.Callback {
    private TouchHelperNotifier touchHelperNotifier;
    MovieAdapter adapter;

    public MovieTouchHelperCallback(TouchHelperNotifier touchHelperNotifier){
        this.touchHelperNotifier = touchHelperNotifier;
    }

    @Override
    public boolean isLongPressDragEnabled(){
        return true;
    }

    public boolean isItemViewSwipeEnabled(){
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        touchHelperNotifier.onItemDismissed(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        touchHelperNotifier.onItemMoved(
                viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;
    }




}
