package com.example.gurvindersingh.worker_part;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashScreen extends AppCompatActivity {

    ImageView SplashImage;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String shareprefrence_name = "Login_Details";
     String PAss_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intialize();
    }
    public void Intialize(){
        SplashImage=(ImageView)findViewById(R.id.splashbg);
        Glide.with(getApplicationContext()).load(R.drawable.splash).asBitmap().into(SplashImage);

        sharedpreferences = getSharedPreferences(shareprefrence_name, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();



        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                if(sharedpreferences.getString("userid", "").length()>0){
                    PAss_user_id = sharedpreferences.getString("userid", "");
                    Intent i=new Intent(SplashScreen.this,Enter_Worker_Detail.class);
                    i.putExtra("userID",PAss_user_id);
                    startActivity(i);
                }
                else{
                    Intent i=new Intent(SplashScreen.this,LoginPage.class);
                    i.putExtra("task","worker");
                    startActivity(i);
                }
            }
        }, 1600);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
}
