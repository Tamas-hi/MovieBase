package hu.bme.aut.moviebase.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.adapter.MovieAdapter;
import hu.bme.aut.moviebase.data.MovieDatabase;
import hu.bme.aut.moviebase.data.Movie_;
import hu.bme.aut.moviebase.data.User;

public class BoughtMoviesActivity extends AppCompatActivity{

    private static MovieDatabase database;
    private static MovieAdapter adapter;
    private static User u;
    private boolean BoughtMovies = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought_movies);

        Intent intent = getIntent();
        u = intent.getParcelableExtra("userdata");

        final TextView tvMoney = findViewById(R.id.tvMoney);
        tvMoney.setText(String.valueOf(u.money));

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        database = MovieDatabase.getDatabase(getApplicationContext());

        initRecyclerView();

        final Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private static void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Movie_>>() {
            @Override
            protected List<Movie_> doInBackground(Void... voids) {
                return database.movieDao().getAllMoviesFromUser(u.id);
            }

            @Override
            protected void onPostExecute(List<Movie_> movies) {
               adapter.update(movies);
            }
        }.execute();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        BoughtMovies = false;
    }

    private void initRecyclerView(){
        final RecyclerView recyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new MovieAdapter(BoughtMovies);
        loadItemsInBackground();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }
}
