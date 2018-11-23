package hu.bme.aut.moviebase.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.data.MovieDatabase;
import hu.bme.aut.moviebase.data.User;

public class RegisterActivity extends AppCompatActivity {

    private static List<User> users;
    private static final int START_MONEY = 30000;
    private static MovieDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText emailRegister = findViewById(R.id.emailRegister);
        final EditText passwordRegister = findViewById(R.id.passwordRegister);

        final Button btnCancel = findViewById(R.id.btnCancel);
        final Button btnOk = findViewById(R.id.btnOk);
        final CheckBox cbAgree = findViewById(R.id.cbAgree);

        database = MovieDatabase.getDatabase(getApplicationContext());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (emailRegister.getText().toString().isEmpty()) {
                    emailRegister.requestFocus();
                    emailRegister.setError(getString(R.string.enter_email));
                    return;
                }

                if (passwordRegister.getText().toString().isEmpty()) {
                    passwordRegister.requestFocus();
                    passwordRegister.setError(getString(R.string.register_password));
                    return;
                }

                if (!(cbAgree.isChecked())) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.cb_checked, Snackbar.LENGTH_LONG).show();
                    return;
                }

                loadItemsInBackground();

                User u = new User(emailRegister.getText().toString(), passwordRegister.getText().toString(), START_MONEY);

                for (User user : users) {
                    if (emailRegister.getText().toString().equals(user.email)) {
                        Toast.makeText(getBaseContext(), R.string.user_exists, Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                insertUserInBackground(u);
                Toast.makeText(getBaseContext(), R.string.reg_success, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private static void loadItemsInBackground() {
        AsyncTask<Void, Void, List<User>> execute = new AsyncTask<Void, Void, List<User>>() {

            @Override
            protected List<User> doInBackground(Void... voids) {
                users = database.userDao().getAll();
                return users;
            }
        }.execute();
        try {
            users = execute.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertUserInBackground(final User u) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                database.userDao().insert(u);
                return true;
            }
        }.execute();
    }
}
