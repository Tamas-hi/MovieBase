package hu.bme.aut.moviebase.adapter;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.UI_Helper.TouchHelperNotifier;
import hu.bme.aut.moviebase.activities.DetailsActivity;
import hu.bme.aut.moviebase.activities.MovieListActivity;
import hu.bme.aut.moviebase.data.MoneyInterface;
import hu.bme.aut.moviebase.data.MovieDatabase;
import hu.bme.aut.moviebase.data.Movie_;
import hu.bme.aut.moviebase.data.User;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> implements TouchHelperNotifier{

    private final List<Movie_> items;
    private MovieItemClickListener listener;
    private MoneyInterface m;
    //public int money = 30000;
    //User u;


    public MovieAdapter(MovieItemClickListener listener){ //MoneyInterface m, User u){
        this.listener = listener;
        items = new ArrayList<>();
        //this.m = m;
        //this.u = u;
    }

    public Movie_ getMovie(int position){
        return items.get(position);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        Movie_ movie = items.get(i);
        movieViewHolder.nameTextView.setText(movie.name);
        movieViewHolder.movieRating.setRating(movie.rating);
        movieViewHolder.priceTextView.setText(String.format("%s $", String.valueOf(movie.price)));
        movieViewHolder.movie = movie;
    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    @Override
    public void onItemDismissed(int position) {
        listener.onItemDeleted(items.get(position));
        items.remove(position);
        notifyItemRemoved(position);
    }


    public interface MovieItemClickListener{
        void onItemChanged(Movie_ item);
        void onItemDeleted(Movie_ item);
        void onAllItemDeleted();
    }

    public void addMovie(Movie_ movie){
        items.add(movie);
        notifyItemInserted(items.size() - 1);
    }

    public void deleteItem(Movie_ movie){
        items.remove(movie);
        notifyDataSetChanged();
    }

    public void deleteAllItem(){
        items.clear();
        notifyDataSetChanged();
    }

    public void update(List<Movie_> movies){
        items.clear();
        items.addAll(movies);
        notifyDataSetChanged();
    }

    public List<Movie_> getMovies(){
        return items;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        RatingBar movieRating;
        TextView priceTextView;
        Button btnBuy;

        Movie_ movie;

        MovieViewHolder(View itemView){
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvMovieName);
            movieRating = itemView.findViewById(R.id.ratingBar);
            priceTextView = itemView.findViewById(R.id.tvPrice);
            btnBuy = itemView.findViewById(R.id.btnBuy);

            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //u.money = u.money - movie.price;
                    //m.onBuyClick(String.valueOf(u.money));
                    //listener.onItemChanged(movie);
                    deleteItem(movie);
                    listener.onItemDeleted(movie);
                }
            });

            nameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("MovieItem",movie);
                    context.startActivity(intent);
                }
            });
        }
    }
}
