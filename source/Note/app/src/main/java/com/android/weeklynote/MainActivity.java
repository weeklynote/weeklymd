package com.android.weeklynote;

import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.android.weeklynote.recycler.TipsAdapter;
import com.android.weeklynote.recycler.XRecyclerViewAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements XRecyclerViewAdapter.OnItemClickListener {

    private ImageView mAnimatedView;
    private RecyclerView mAnimationList;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mAnimatedView = (ImageView) findViewById(R.id.animat_view);
        mAnimationList = (RecyclerView) findViewById(R.id.animation_list);
        mAnimationList.setLayoutManager(new LinearLayoutManager(this));
        TipsAdapter tipsAdapter = new TipsAdapter(getAnimations());
        tipsAdapter.setOnItemClickListener(this);
        mAnimationList.setAdapter(tipsAdapter);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private List<String> getAnimations() {
        List<String> animations = new ArrayList<>(5);
        animations.add("TranslateAnimation");
        animations.add("ScaleAnimation");
        animations.add("AlphaAnimation");
        animations.add("RotateAnimation");
        animations.add("AnimationSet");
        animations.add("FrameAnimation");
        return animations;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void applyTranslateAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 100, 0, 100);
        translateAnimation.setDuration(4000);
        mAnimatedView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translation));
    }

    private void applyScaleAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1, 0.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.8f, Animation.RELATIVE_TO_SELF, 0.9f);
        scaleAnimation.setDuration(4000);
        mAnimatedView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale));
    }

    private void applyAlphaAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(4000);
        mAnimatedView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.alpha));
    }

    private void applyRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(45, 270, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(4000);
        mAnimatedView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
    }

    private void applyAnimationSet() {
        AnimationSet animationSet = new AnimationSet(true);
        RotateAnimation rotateAnimation = new RotateAnimation(90, -90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(600);
        rotateAnimation.setRepeatMode(Animation.REVERSE);
        rotateAnimation.setRepeatCount(5);
        rotateAnimation.setStartTime(1200);
        animationSet.addAnimation(rotateAnimation);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.2f, 1.2f, 0.2f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(300);
        animationSet.addAnimation(scaleAnimation);
        mAnimatedView.startAnimation(animationSet);
    }

    private void applyFrameAnimation() {
//        AnimationDrawable animationDrawable = new AnimationDrawable();
//        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_media_route_on_holo_light);
//        animationDrawable.addFrame(drawable, 300);
//        drawable = ContextCompat.getDrawable(this, R.drawable.ic_media_route_on_0_holo_light);
//        animationDrawable.addFrame(drawable, 300);
//        drawable = ContextCompat.getDrawable(this, R.drawable.ic_media_route_on_1_holo_light);
//        animationDrawable.addFrame(drawable, 300);
//        drawable = ContextCompat.getDrawable(this, R.drawable.ic_media_route_on_2_holo_light);
//        animationDrawable.addFrame(drawable, 300);
//        animationDrawable.setOneShot(false);
//        mAnimatedView.setBackground(animationDrawable);
//        animationDrawable.start();
        mAnimatedView.setBackgroundResource(R.anim.frame_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) mAnimatedView.getBackground();
        animationDrawable.start();
    }

    @Override
    public void onItemClicked(int position) {
        switch (position) {
            case 0:
                applyTranslateAnimation();
                break;
            case 1:
                applyScaleAnimation();
                break;
            case 2:
                applyAlphaAnimation();
                break;
            case 3:
                applyRotateAnimation();
                break;
            case 4:
                applyAnimationSet();
                break;
            case 5:
                applyFrameAnimation();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.android.weeklynote/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.android.weeklynote/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
