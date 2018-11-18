package hu.bme.aut.moviebase.activities;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.adapter.MovieAdapter;
import hu.bme.aut.moviebase.data.MovieDatabase;
import hu.bme.aut.moviebase.data.Movie_;
import hu.bme.aut.moviebase.data.User;

public class DetailsActivity extends AppCompatActivity {

    private float originalRating;
    //private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        final Movie_ movie = intent.getParcelableExtra("MovieItem");
        final Button btnSaveRating = findViewById(R.id.btnSaveRating);
        final RatingBar ratingBar = findViewById(R.id.userRating);
        final MovieDatabase database = MovieDatabase.getDatabase(getApplicationContext());
        String name = movie.name;
        Movie_.Category category = movie.category;
        String description = movie.description;
        int length = movie.length;
        originalRating = movie.rating;

        //adapter = new MovieAdapter();

        TextView nameTextView = findViewById(R.id.tvName);
        TextView categoryTextView = findViewById(R.id.tvCategory);
        TextView lengthTextView = findViewById(R.id.tvLength);
        TextView descTextView = findViewById(R.id.tvDesc);

        nameTextView.setText(name);
        categoryTextView.setText(category.toString());
        lengthTextView.setText(String.valueOf(length));
        descTextView.setText(description);

        btnSaveRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float newRating = ratingBar.getRating();
                originalRating = (originalRating + newRating) / 2;
                Movie_ deleted = database.movieDao().findMovieByName(movie.name);
                database.movieDao().deleteRow(deleted.name);
                movie.rating = originalRating;
                database.movieDao().insert(movie);
                Toast.makeText(getBaseContext(), String.valueOf(movie.rating), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent i = new Intent(DetailsActivity.this, MovieListActivity.class);
        //i.putExtra("newRating", originalRating);
    }
}