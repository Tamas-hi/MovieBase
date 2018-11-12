package hu.bme.aut.moviebase.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.data.User;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText emailRegister = findViewById(R.id.emailRegister);
        final EditText passwordRegister = findViewById(R.id.passwordRegister);

        final Button btnCancel = findViewById(R.id.btnCancel);
        final Button btnOk = findViewById(R.id.btnOk);
        final CheckBox cbAgree = findViewById(R.id.cbAgree);

        final SharedPreferences preferences = getSharedPreferences("Data", Context.MODE_PRIVATE);

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
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
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
                if(preferences.contains(emailRegister.getText().toString())) {
                    Toast.makeText(getBaseContext(), "User already exists!", Toast.LENGTH_LONG).show();
                    return;
                }

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", emailRegister.getText().toString());
                editor.putString("password", passwordRegister.getText().toString());
                editor.apply();

                Toast.makeText(getBaseContext(), "Registration was successful!", Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }
}
