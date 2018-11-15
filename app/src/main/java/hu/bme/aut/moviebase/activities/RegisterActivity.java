package hu.bme.aut.moviebase.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Objects;

import hu.bme.aut.moviebase.R;

public class RegisterActivity extends AppCompatActivity {

    private static String USER_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText emailRegister = findViewById(R.id.emailRegister);
        final EditText passwordRegister = findViewById(R.id.passwordRegister);

        final Button btnCancel = findViewById(R.id.btnCancel);
        final Button btnOk = findViewById(R.id.btnOk);
        final CheckBox cbAgree = findViewById(R.id.cbAgree);

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
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (emailRegister.getText().toString().isEmpty()) {
                    emailRegister.requestFocus();
                    emailRegister.setError("Please enter your email address");
                    return;
                }

                if (passwordRegister.getText().toString().isEmpty()) {
                    passwordRegister.requestFocus();
                    passwordRegister.setError("Please enter your password");
                    return;
                }

                if(!(cbAgree.isChecked())) {
                    Snackbar.make(findViewById(android.R.id.content), "You can not continue with the checkbox unchecked.", Snackbar.LENGTH_LONG).show();
                    return;
                }

                USER_KEY = emailRegister.getText().toString();
                SharedPreferences preferences = getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);

                File f = new File("/data/data/hu.bme.aut.moviebase/shared_prefs/"+USER_KEY+".xml");
                if(f.exists()){
                    Toast.makeText(getBaseContext(), "User already exists!", Toast.LENGTH_LONG).show();
                }

                else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(emailRegister.getText().toString(), passwordRegister.getText().toString());
                    editor.apply();

                    Toast.makeText(getBaseContext(), "Registration was successful!", Toast.LENGTH_LONG).show();

                    finish();
                }
            }
        });
    }
}
