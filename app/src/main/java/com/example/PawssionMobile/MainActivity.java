 package com.example.PawssionMobile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

 public class MainActivity extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Assign Variable
        drawerLayout = findViewById(R.id.drawer_layout);
    }
     public void ClickMenu(View view){
         //Open drawer
         openDrawer(drawerLayout);
     }

     public static void openDrawer(DrawerLayout drawerLayout) {
         //Open drawer layout
         drawerLayout.openDrawer(GravityCompat.START);
     }

     public void ClickLogo(View view){
         //Closer drawer
         closeDrawer(drawerLayout);
     }

     public static void closeDrawer(DrawerLayout drawerLayout) {
         //close drawer layout
         //check condition
         if(drawerLayout.isDrawerOpen(GravityCompat.START)){
             //When drawer is open
             //Close drawer
             drawerLayout.closeDrawer(GravityCompat.START);
         }
     }

     public void ClickHome(View view){
         //Recreate activity
         recreate();
     }

     public void ClickView(View view){
         //Redirect activity to dashboard
         redirectActivity(this,ViewCamera.class);
     }

     public void ClickFeed(View view){
         //Redirect activity to dashboard
         redirectActivity(this,Feed.class);
     }

     public void ClickSchedule(View view){
         //Redirect activity to dashboard
         redirectActivity(this,Schedule.class);
     }
     public void ClickExit(View view){
         //close app
         exit(this);
     }

     public static void exit(Activity activity){
         //Initialize alert dialog
         AlertDialog.Builder builder = new AlertDialog.Builder(activity);
         //Set title
         builder.setTitle("Exit");
         //Set message
         builder.setMessage("Are you sure you want to exit?");
         //Positive yes button
         builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 //Finish activity
                 activity.finishAffinity();
                 //Exit app
                 System.exit(0);
             }
         });
         //Negative no button
         builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 //Dismiss dialog
                 dialog.dismiss();
             }
         });
         //Show dialog
         builder.show();
     }

     public static void redirectActivity(Activity activity, Class aClass) {
         //Initialize intent
         Intent intent = new Intent(activity, aClass);
         //Set flag
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         //Start activity
         activity.startActivity(intent);
     }

     @Override
     protected void onPause(){
         super.onPause();
         //Close drawer
         closeDrawer(drawerLayout);
     }
}