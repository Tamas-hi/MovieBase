package hu.bme.aut.moviebase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.data.Movie;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("MovieItem");

        //long id = movie.id;
        String name = movie.name;
        //Movie.Category category = movie.category;
        //int length = movie.length;
        //String description = movie.description;
        //float rating = movie.rating;
        //int price = movie.price;

        TextView nameTextView = findViewById(R.id.nameTest);
        //TextView categoryTextView = findViewById(R.id.categoryTest);
        //TextView lengthTextView = findViewById(R.id.lengthTest);
       // TextView descTextView = findViewById(R.id.descTest);
       // TextView priceTextView = findViewById(R.id.priceTest);

        nameTextView.setText(name);
        //lengthTextView.setText(length);
        //descTextView.setText(description);
        //priceTextView.setText(price);

    }
}
