package hu.bme.aut.moviebase.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.UI_Helper.Rotate3dAnimation;
import hu.bme.aut.moviebase.data.MovieDatabase;
import hu.bme.aut.moviebase.data.User;

public class LoginActivity extends AppCompatActivity {

    private ImageView img;
    private static final String EMPTY_STRING = "";
    private static String USER_KEY = "";
    List<User> users;
    boolean adminLogOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        img = findViewById(R.id.popcorn_3d);

        final EditText etEmailAddress = findViewById(R.id.email);
        final EditText etPassword = findViewById(R.id.password);
        final Button btnSignIn = findViewById(R.id.btnSignIn);
        final ImageButton btnX1 = findViewById(R.id.firstX);
        final ImageButton btnX2 = findViewById(R.id.secondX);
        final Button btnRegister = findViewById(R.id.RegisterButton);
        final TextClock textClock = findViewById(R.id.textClock);

        final MovieDatabase database = MovieDatabase.getDatabase(getApplicationContext());
        users = database.userDao().getAll();


        textClock.setFormat12Hour(null);
        textClock.setFormat24Hour(getString(R.string.dateFormat));

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // admin LOGON
                if(etEmailAddress.getText().toString().equals("admin") && etPassword.getText().toString().equals("admin")){
                    Intent intent = new Intent(LoginActivity.this, MovieListActivity.class);
                    adminLogOn = true;
                    intent.putExtra("admin", adminLogOn);
                    startActivity(intent);
                }

                if (etEmailAddress.getText().toString().isEmpty()) {
                    etEmailAddress.requestFocus();
                    etEmailAddress.setError(getString(R.string.enterEmail));
                    return;
                }

                if (etPassword.getText().toString().isEmpty()) {
                    etPassword.requestFocus();
                    etPassword.setError(getString(R.string.enterPass));
                    return;
                }

                // user LOGON
                /*USER_KEY = etEmailAddress.getText().toString();
                SharedPreferences preferences = getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
                String password = preferences.getString(USER_KEY, "");
                    if (password.equals(etPassword.getText().toString())) {
                        Intent intent = new Intent(LoginActivity.this, MovieListActivity.class);
                        startActivity(intent);
                    }*/

               users = database.userDao().getAll();

               for(User u : users) {
                   if (etEmailAddress.getText().toString().equals(u.email) && etPassword.getText().toString().equals(u.password)) {
                       Intent intent = new Intent(LoginActivity.this, MovieListActivity.class);
                       intent.putExtra("userdata", u);
                       intent.putParcelableArrayListExtra("users", (ArrayList<? extends Parcelable>) users);
                       startActivity(intent);
                   }else{
                       Snackbar.make(findViewById(android.R.id.content), "Wrong password!", Snackbar.LENGTH_LONG).show();
                   }
               }
            }
        });

        btnX1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(etEmailAddress.getText().toString().isEmpty())){
                    etEmailAddress.setText(EMPTY_STRING);
                }
            }
        });

        btnX2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(etPassword.getText().toString().isEmpty())){
                    etPassword.setText(EMPTY_STRING);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            Rotate3dAnimation anim = new Rotate3dAnimation(0,0,360,0,0,0);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(2250);
            anim.setRepeatCount(Animation.INFINITE);
            img.startAnimation(anim);
        }
    }
}
