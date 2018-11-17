package hu.bme.aut.moviebase.activities;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.UI_Helper.MovieTouchHelperCallback;
import hu.bme.aut.moviebase.adapter.MovieAdapter;
import hu.bme.aut.moviebase.data.MoneyInterface;
import hu.bme.aut.moviebase.data.Movie_;
import hu.bme.aut.moviebase.data.MovieDatabase;
import hu.bme.aut.moviebase.data.User;
import hu.bme.aut.moviebase.fragments.NewMovieDialogFragment;

public class MovieListActivity extends AppCompatActivity implements NewMovieDialogFragment.NewMovieDialogListener, MovieAdapter.MovieItemClickListener{ //MoneyInterface{

    private MovieAdapter adapter;
    private MovieDatabase database;
    private boolean AdminLogOn = true;
    //TextView tvMoney;
    //User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Intent intent = getIntent();
        //u = intent.getParcelableExtra("userdata");
        //tvMoney = findViewById(R.id.tvMoney);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewMovieDialogFragment().show(getSupportFragmentManager(), NewMovieDialogFragment.TAG);
            }
        });

        database = Room.databaseBuilder(getApplicationContext(),MovieDatabase.class , "movie-list").allowMainThreadQueries().build();

        initRecyclerView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*public User getUser(){
        return u;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                adapter.deleteAllItem();
                onAllItemDeleted();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new MovieAdapter(this);//this,  u);
        loadItemsInBackground();
        //loadUsersInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback =
                new MovieTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        /*ItemTouchHelper.Callback callback2 = new RVHItemTouchHelperCallback(adapter,true,true,true);
        ItemTouchHelper touchHelper2 = new ItemTouchHelper(callback);
        touchHelper2.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new RVHItemClickListener(this, new RVHItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MovieListActivity.this, DetailsActivity.class);
                intent.putExtra("MovieItem",adapter.getMovie(position));
                startActivity(intent);
            }
        }));*/

    }

    /*private void loadUsersInBackground(){
        new AsyncTask<Void, Void, List<User>>(){
            @Override
            protected List<User> doInBackground(Void...voids){
                return database.userDao().getAll();
            }
        }.execute();
    }*/

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Movie_>>() {

            @Override
            protected List<Movie_> doInBackground(Void... voids) {
                return database.movieDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Movie_> movies) {
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
    public void onItemChanged(final Movie_ item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.movieDao().update(item);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                Log.d("MovieListActivity", "Movie update was successful");
            }
        }.execute();
    }

    @Override
    public void onItemDeleted(final Movie_ item) {
        new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(Void... voids){
                database.movieDao().deleteItem(item);
                return true;
            }
        }.execute();
    }

    @Override
    public void onMovieCreated(final Movie_ newMovie) {
        new AsyncTask<Void, Void, Movie_>() {

            @Override
            protected Movie_ doInBackground(Void... voids) {
                newMovie.id = database.movieDao().insert(newMovie);
                return newMovie;
            }

            @Override
            protected void onPostExecute(Movie_ movie) {
                adapter.addMovie(movie);
            }
        }.execute();
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to log off?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }

    /*@Override
    public void onBuyClick(String money) {
        tvMoney.setText(String.valueOf(u.money));
        database.userDao().update(u);
    }*/
}
