package hu.bme.aut.moviebase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.data.Movie;

public class DetailsActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        final Movie movie = intent.getParcelableExtra("MovieItem");

        String name = movie.name;
        Movie.Category category = movie.category;
        String description = movie.description;
        int length = movie.length;

        TextView nameTextView = findViewById(R.id.tvName);
        TextView categoryTextView = findViewById(R.id.tvCategory);
        TextView lengthTextView = findViewById(R.id.tvLength);
        TextView descTextView = findViewById(R.id.tvDesc);

        nameTextView.setText(name);
        categoryTextView.setText(category.toString());
        lengthTextView.setText(String.valueOf(length));
        descTextView.setText(description);
    }
}