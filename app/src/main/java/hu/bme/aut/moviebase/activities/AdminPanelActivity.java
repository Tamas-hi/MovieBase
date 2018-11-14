package hu.bme.aut.moviebase.activities;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import java.util.Objects;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.adapter.MovieAdapter;
import hu.bme.aut.moviebase.data.Movie;
import hu.bme.aut.moviebase.data.MovieDatabase;
import hu.bme.aut.moviebase.fragments.NewMovieDialogFragment;

public class AdminPanelActivity extends AppCompatActivity implements NewMovieDialogFragment.NewMovieDialogListener, MovieAdapter.MovieItemClickListener {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private MovieDatabase database;
    private boolean AdminLogOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewMovieDialogFragment().show(getSupportFragmentManager(), NewMovieDialogFragment.TAG);
            }
        });

        database = Room.databaseBuilder(getApplicationContext(),MovieDatabase.class , "movie-list").build();

        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*// Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        switch(item.getItemId()){
            case R.id.action_settings:
                adapter.deleteAllItem();
                onAllItemDeleted();
        }



        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new MovieAdapter(this);
            loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Movie>>() {

            @Override
            protected List<Movie> doInBackground(Void... voids) {
                return database.movieDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                adapter.update(movies);
            }
        }.execute();
    }

    @Override
    public void onAllItemDeleted(){
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids){
                database.movieDao().deleteAll();
                return true;
            }

        }.execute();
    }

    @Override
    public void onItemChanged(final Movie item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.movieDao().update(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("AdminPanelActivity", "Movie update was successful");
            }
        }.execute();
    }

    @Override
    public void onItemDeleted(final Movie item) {
        new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(Void... voids){
                database.movieDao().deleteItem(item);
                return true;
            }
        }.execute();
    }

    @Override
    public void onMovieCreated(final Movie newMovie) {
        new AsyncTask<Void, Void, Movie>() {

            @Override
            protected Movie doInBackground(Void... voids) {
                newMovie.id = database.movieDao().insert(newMovie);
                return newMovie;
            }

            @Override
            protected void onPostExecute(Movie movie) {
                adapter.addMovie(movie);
            }
        }.execute();
    }
}
