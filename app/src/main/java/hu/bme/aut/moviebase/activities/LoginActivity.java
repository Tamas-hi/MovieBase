package hu.bme.aut.moviebase.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextClock;
import java.lang.Thread;
import java.util.Objects;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.UI_Helper.Rotate3dAnimation;

public class LoginActivity extends AppCompatActivity {

    private ImageView img;
    private static final String EMPTY_STRING = "";
    private static String USER_KEY = "";

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
        final Toolbar loginToolbar = findViewById(R.id.toolbarLogin);
        //setSupportActionBar(loginToolbar);
        //Objects.requireNonNull(getSupportActionBar()).setTitle(null);

        textClock.setFormat12Hour(null);
        textClock.setFormat24Hour("yyyy/MM/dd hh:mm:ss");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (etEmailAddress.getText().toString().isEmpty()) {
                    etEmailAddress.requestFocus();
                    etEmailAddress.setError("Please enter your email address");
                    return;
                }

                if (etPassword.getText().toString().isEmpty()) {
                    etPassword.requestFocus();
                    etPassword.setError("Please enter your password");
                    return;
                }

                USER_KEY = etEmailAddress.getText().toString();
                SharedPreferences preferences = getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
                String password = preferences.getString(USER_KEY, "");
                    if (password.equals(etPassword.getText().toString())) {
                        Intent intent = new Intent(LoginActivity.this, AdminPanelActivity.class);
                        startActivity(intent);
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
