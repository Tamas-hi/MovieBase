package hu.bme.aut.moviebase.adapter;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.UI_Helper.TouchHelperNotifier;
import hu.bme.aut.moviebase.data.Movie;
import hu.bme.aut.moviebase.data.MovieDatabase;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> implements TouchHelperNotifier {

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

        movieViewHolder.movie = movie;
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    @Override
    public void onItemDismissed(final int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
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

    class MovieViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        RatingBar movieRating;
        TextView priceTextView;
        ImageButton removeButton;

        Movie movie;

        MovieViewHolder(View itemView){
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvMovieName);
            movieRating = itemView.findViewById(R.id.ratingBar);
            priceTextView = itemView.findViewById(R.id.tvPrice);
            removeButton = itemView.findViewById(R.id.MovieRemoveButton);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(movie);
                    listener.onItemDeleted(movie);
                }
            });

        }
    }
}
