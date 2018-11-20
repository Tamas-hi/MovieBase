package hu.bme.aut.moviebase.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
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


public class MovieListActivity extends AppCompatActivity implements NewMovieDialogFragment.NewMovieDialogListener, MovieAdapter.MovieItemClickListener, MoneyInterface, MovieAdapter.BuyMovieClickListener{


    private static MovieAdapter adapter;
    private static MovieDatabase database;
    private boolean adminLogOn;
    private static User u;
    private TextView tvMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Intent intent = getIntent();
        u = intent.getParcelableExtra("userdata");
        adminLogOn = intent.getBooleanExtra("admin", false);

        tvMoney = findViewById(R.id.tvMoney);
        if(!adminLogOn) {
            tvMoney.setText(String.valueOf(u.money));
        }

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);

        final Button btnCollection = findViewById(R.id.btnCollection);
        final Button btnUsers = findViewById(R.id.btnUsers);
        final FloatingActionButton fab = findViewById(R.id.fab);

        database = MovieDatabase.getDatabase(getApplicationContext());

        if(adminLogOn) {
            btnCollection.setVisibility(View.INVISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new NewMovieDialogFragment().show(getSupportFragmentManager(), NewMovieDialogFragment.TAG);
                }
            });


            btnUsers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                    final ViewGroup nullView = null;
                    final View popupView = Objects.requireNonNull(inflater).inflate(R.layout.user_delete, nullView);
                    final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    final EditText etUserEmail = popupView.findViewById(R.id.etEmail);
                    final Button btnDelete = popupView.findViewById(R.id.btnDelete);
                    final Button btnDeleteAll = popupView.findViewById(R.id.btnDeleteAll);

                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0,0);
                    popupWindow.setFocusable(true);
                    popupWindow.update();

                    btnDeleteAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final List<User> allUsers = database.userDao().getAll();
                            if(allUsers.isEmpty()) {
                                Snackbar.make(findViewById(android.R.id.content), R.string.no_registered_user, Snackbar.LENGTH_LONG).show();
                                popupWindow.dismiss();
                                }
                            else {
                                deleteAllUsersInBackground();
                                Snackbar.make(findViewById(android.R.id.content), R.string.all_deleted, Snackbar.LENGTH_LONG).show();
                                popupWindow.dismiss();
                            }
                        }
                    });

                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String email = etUserEmail.getText().toString();
                            final List<User> allUsers = database.userDao().getAll();

                            if(allUsers.isEmpty()) {
                                Snackbar.make(findViewById(android.R.id.content), R.string.no_registered_user, Snackbar.LENGTH_LONG).show();
                                popupWindow.dismiss();
                            }

                            else {
                            for(User u: allUsers) {
                                if (u.email.equals(email)) {
                                    database.userDao().delete(u);
                                    popupWindow.dismiss();
                                    Snackbar.make(findViewById(android.R.id.content), R.string.user_deleted, Snackbar.LENGTH_LONG).show();
                                    break;
                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), R.string.user_not_found, Snackbar.LENGTH_LONG).show();
                                    popupWindow.dismiss();
                                }
                            }
                            }
                        }
                    });
                }
            });
        }
        else{
            fab.hide();
            btnUsers.setVisibility(View.INVISIBLE);
        }

        initRecyclerView();

        btnCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieListActivity.this, BoughtMoviesActivity.class);
                intent.putExtra("userdata", u);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if(!adminLogOn){
            menu.getItem(2).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.sortByTitle:
                Collections.sort(adapter.getMovies(), new Comparator<Movie_>(){
                    @Override
                    public int compare(Movie_ m1, Movie_ m2){
                        return m1.name.compareToIgnoreCase(m2.name);
                    }
                });
                adapter.notifyDataSetChanged();
                break;

            case R.id.sortByRating:
                Collections.sort(adapter.getMovies(), new Comparator<Movie_>(){
                    @Override
                    public int compare(Movie_ m1, Movie_ m2){
                        return (-Float.compare(m1.rating, m2.rating));
                    }
                });
                adapter.notifyDataSetChanged();
                break;

            case R.id.action_settings:
                adapter.deleteAllItem();
                onAllItemDeleted();
                break;

            case R.id.about:
                LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                final ViewGroup nullView = null;
                final View popupView = Objects.requireNonNull(inflater).inflate(R.layout.popup_window, nullView);
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
        adapter = new MovieAdapter(this,this,this, u, adminLogOn);
        loadItemsInBackground();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        if(adminLogOn) {
            ItemTouchHelper.Callback callback =
                    new MovieTouchHelperCallback(adapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
        }
    }

    static void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Movie_>>() {

            @Override
            protected List<Movie_> doInBackground(Void... voids) {
                return database.movieDao().getAllNoUser();
            }

            @Override
            protected void onPostExecute(List<Movie_> movies) {
                adapter.update(movies);
            }
        }.execute();
    }

    private static void onAllItemDeleted(){
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids){
                database.movieDao().deleteAll();
                return true;
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
                database.movieDao().insert(newMovie);
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
                .setTitle(R.string.exit)
                .setMessage(R.string.log_off)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.no,null)
                .show();
    }

    @Override
    public void onBuyClick(int money) {
        tvMoney.setText(String.valueOf(u.money));
        updateMoneyInBackground();
    }

    private static void updateMoneyInBackground(){
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids){
                User deleted = database.userDao().findUserById(u.id);
                database.userDao().deleteRow(deleted.id);
                database.userDao().insert(u);
                return true;
            }
        }.execute();
    }

    @Override
    public void onItemBought(final Movie_ item) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                Movie_ movieDeleted = database.movieDao().findMovieById(item.id);
                database.movieDao().deleteRow(movieDeleted.id);
                database.movieDao().insert(item);
                return true;
            }
        }.execute();
    }

    private static void deleteAllUsersInBackground(){
        new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(Void... voids){
                database.userDao().deleteAll();
                return true;
            }
        }.execute();
    }
}
