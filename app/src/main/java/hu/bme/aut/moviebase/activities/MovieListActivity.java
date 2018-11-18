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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.UI_Helper.MovieTouchHelperCallback;
import hu.bme.aut.moviebase.adapter.MovieAdapter;
import hu.bme.aut.moviebase.data.MoneyInterface;
import hu.bme.aut.moviebase.data.MovieDatabase;
import hu.bme.aut.moviebase.data.Movie_;
import hu.bme.aut.moviebase.data.User;
import hu.bme.aut.moviebase.fragments.NewMovieDialogFragment;

public class MovieListActivity extends AppCompatActivity implements NewMovieDialogFragment.NewMovieDialogListener, MovieAdapter.MovieItemClickListener, MoneyInterface{


    private MovieAdapter adapter;
    private MovieDatabase database;
    private boolean adminLogOn;
    private User u;
    private TextView tvMoney;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Intent intent = getIntent();
        u = intent.getParcelableExtra("userdata");
        users = intent.getParcelableArrayListExtra("users");
        adminLogOn = intent.getBooleanExtra("admin", false);
        tvMoney = findViewById(R.id.tvMoney);
        if(!adminLogOn) {
            tvMoney.setText(String.valueOf(u.money));
        }

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
                break;

            case R.id.about:
                LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                final View popupView = Objects.requireNonNull(inflater).inflate(R.layout.popup_window, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0,0);
                Button btnAboutOk = popupView.findViewById(R.id.btnAboutOk);
                btnAboutOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new MovieAdapter(this,this, u, adminLogOn);//this,  u);
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

    @Override
    public void onBuyClick(int money) {
        tvMoney.setText(String.valueOf(u.money));
        User deleted = database.userDao().findUserByEmail(u.email);
        database.userDao().deleteRow(deleted.email);
        database.userDao().insert(u);
        //database.userDao().deleteAll();
        //users.remove(u);
        //users.add(u);
        //database.userDao().insert(u);
        //database.userDao().deleteRow(u.id);
        //database.userDao().deleteAll();
        //database.userDao().insert(u);
        /*List<User> users = database.userDao().getAll();
        database.userDao().deleteAll();
        database.userDao().insertAll(users);
        database.userDao().insert(u);*/
        Toast.makeText(getBaseContext(), "test", Toast.LENGTH_LONG).show();
    }
}
