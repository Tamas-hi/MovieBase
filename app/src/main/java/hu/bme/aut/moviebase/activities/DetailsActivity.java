package hu.bme.aut.moviebase.activities;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.adapter.MovieAdapter;
import hu.bme.aut.moviebase.data.Movie;
import hu.bme.aut.moviebase.data.MovieDatabase;

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