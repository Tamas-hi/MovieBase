package hu.bme.aut.moviebase;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        img = findViewById(R.id.popcorn_3d);

        final EditText etEmailAddress = findViewById(R.id.email);
        final EditText etPassword = findViewById(R.id.password);
        final Button btnSignIn = findViewById(R.id.btnSignIn);

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
                }

                // TODO log in
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


            /*RotateAnimation rotate = new RotateAnimation(0.0f,360.f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            rotate.setDuration(1000);
            rotate.setInterpolator(new LinearInterpolator());
            img.startAnimation(rotate);*/

            /*Animation animation = AnimationUtils.loadAnimation(this, R.anim.popcorn_rotate);
            img.startAnimation(animation);*/
        }
    }
}
