package hu.bme.aut.moviebase.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.data.MovieDatabase;
import hu.bme.aut.moviebase.data.Movie_;

public class DetailsActivity extends AppCompatActivity {

    private static float originalRating;
    private static MovieDatabase database;
    private static Movie_ movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        movie = intent.getParcelableExtra("MovieItem");

        final Button btnSaveRating = findViewById(R.id.btnSaveRating);
        final RatingBar ratingBar = findViewById(R.id.userRating);

        database = MovieDatabase.getDatabase(getApplicationContext());

        String name = movie.name;
        Movie_.Category category = movie.category;
        String description = movie.description;
        int length = movie.length;
        originalRating = movie.rating;

        final TextView nameTextView = findViewById(R.id.tvName);
        final TextView categoryTextView = findViewById(R.id.tvCategory);
        final TextView lengthTextView = findViewById(R.id.tvLength);
        final TextView descTextView = findViewById(R.id.tvDesc);
        final Button btnBack = findViewById(R.id.btnBackDetails);

        nameTextView.setText(name);
        categoryTextView.setText(category.toString());
        lengthTextView.setText(String.valueOf(length));
        descTextView.setText(description);

        btnSaveRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float newRating = ratingBar.getRating();
                originalRating = (originalRating + newRating) / 2;
                saveRatingInBackground();
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.new_rating) + originalRating, Snackbar.LENGTH_LONG).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private static void saveRatingInBackground(){
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids){
                Movie_ deleted = database.movieDao().findMovieById(movie.id);
                database.movieDao().deleteItem(deleted);
                movie.rating = originalRating;
                database.movieDao().insert(movie);
                return true;
            }
        }.execute();
    }

    @Override
    public void onBackPressed(){
      super.onBackPressed();
      MovieListActivity.loadItemsInBackground();
    }
}