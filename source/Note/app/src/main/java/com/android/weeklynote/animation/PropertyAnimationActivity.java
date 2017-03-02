package com.android.weeklynote.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.weeklynote.R;

public class PropertyAnimationActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = PropertyAnimationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);
        findViewById(R.id.object_animation).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.object_animation:
                Log.d(TAG, "PropertyAnimationActivity object_animation....");
//                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, "translationX", 200);
//                objectAnimator.setDuration(4000);
//                objectAnimator.start();
                Animator objectAnimator = AnimatorInflater.loadAnimator(this, R.anim.object_animator);
                objectAnimator.setTarget(v);
                objectAnimator.start();
                break;
        }
    }
}
