package hu.bme.aut.moviebase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Date;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.data.Movie;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("MovieItem");


        String name = movie.name;
        Movie.Category category = movie.category;
        String description = movie.description;
        float rating = movie.rating;

        TextView nameTextView = findViewById(R.id.nameTest);
        TextView categoryTextView = findViewById(R.id.categoryTest);
        TextView descTextView = findViewById(R.id.descTest);
        TextView ratingTextView = findViewById(R.id.ratingTest);

        nameTextView.setText(name);
        categoryTextView.setText(category.toString());
        descTextView.setText(description);
        ratingTextView.setText(Float.toString(rating));

    }
}
