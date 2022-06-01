package com.example.PawssionMobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.marcoscg.ipcamview.IPCamView;

public class ViewCamera extends AppCompatActivity {

    DrawerLayout drawerLayout;
    private IPCamView ipCamView;
    Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_camera);

        buttonStart = (Button) findViewById(R.id.buttonStart);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipCamView = findViewById(R.id.ip_cam_view);
                ipCamView.setUrl("http://192.168.0.62/cam-hi.jpg");
                ipCamView.setInterval(100);
                ipCamView.start();
            }
        });



        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    public void stop(View view){
        ipCamView.stop();
    }

    public void ClickMenu(View view){
        //Open drawer
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view){
        //Redirect activity to home
        MainActivity.redirectActivity(this, MainActivity.class);
    }

    public void ClickFeed(View view){
        //Redirect activity to Feed
        MainActivity.redirectActivity(this, Feed.class);
    }

    public void ClickView(View view){
        //Recreate activity
        recreate();
    }

    public void ClickSchedule(View view){
        //Redirect activity to Schedule
        MainActivity.redirectActivity(this, Schedule.class);
    }

    public void ClickExit(View view){
        //Close app
        MainActivity.exit(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
}