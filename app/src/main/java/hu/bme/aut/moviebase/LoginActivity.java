package hu.bme.aut.moviebase;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        img = findViewById(R.id.popcorn_3d);

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
