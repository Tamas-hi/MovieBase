package hu.bme.aut.moviebase.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.nisrulz.recyclerviewhelper.RVHAdapter;
import github.nisrulz.recyclerviewhelper.RVHViewHolder;
import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.activities.DetailsActivity;
import hu.bme.aut.moviebase.activities.MovieListActivity;
import hu.bme.aut.moviebase.data.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> implements RVHAdapter {

    private final List<Movie> items;
    private MovieItemClickListener listener;


    public MovieAdapter(MovieItemClickListener listener){
        this.listener = listener;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        Movie movie = items.get(i);
        movieViewHolder.nameTextView.setText(movie.name);
        movieViewHolder.movieRating.setRating(movie.rating);
        movieViewHolder.priceTextView.setText(movie.price + " Ft");
        movieViewHolder.lengthTextView.setText(movie.length + " perc");

        movieViewHolder.movie = movie;
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    @Override
    public void onItemDismiss(int position, int direction) {
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }


    public interface MovieItemClickListener{
        void onItemChanged(Movie item);
        void onItemDeleted(Movie item);
        void onAllItemDeleted();
    }

    public void addMovie(Movie movie){
        items.add(movie);
        notifyItemInserted(items.size() - 1);
    }

    public void deleteItem(Movie movie){
        items.remove(movie);
        notifyDataSetChanged();
    }

    public void deleteAllItem(){
        items.clear();
        notifyDataSetChanged();
    }

    public void update(List<Movie> movies){
        items.clear();
        items.addAll(movies);
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements RVHViewHolder {

        TextView nameTextView;
        RatingBar movieRating;
        TextView priceTextView;
        //ImageButton removeButton;
        TextView lengthTextView;

        Movie movie;

        MovieViewHolder(View itemView){
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvMovieName);
            movieRating = itemView.findViewById(R.id.ratingBar);
            priceTextView = itemView.findViewById(R.id.tvPrice);
            //removeButton = itemView.findViewById(R.id.MovieRemoveButton);
            lengthTextView = itemView.findViewById(R.id.tvLength);

            /*removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(movie);
                    listener.onItemDeleted(movie);
                }
            });*/

        }

        @Override
        public void onItemClear() {

        }

        @Override
        public void onItemSelected(int actionstate) {

        }
    }
}
