package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class Feed extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;
    String[] Colors={"23 grams(appx.)","30 grams(appx.)","39 grams(appx.)"};
    String colorSelected;
    TextView textDateandTime;
    Boolean connected = false;
    private DatabaseReference mdatabase;
    private Button btnDateandTime;
    private TextView amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //For TextView and Button
        amount = (TextView) findViewById(R.id.txtAmount);
        textDateandTime = findViewById(R.id.textDateandTime);
        btnDateandTime = (Button) findViewById(R.id.btnDateandTime);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if(!connected){
            Toast.makeText(Feed.this, "No Internet", Toast.LENGTH_LONG).show();
        } else {
            //Write a message to the database
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference intensityDbStatus = database.getReference("Feed");
            mdatabase = FirebaseDatabase.getInstance().getReference();



            btnDateandTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }

                private void showDialog() {
                    colorSelected = Colors[0];
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Feed.this);
                    builder.setTitle("Choose Amount");
                    builder.setSingleChoiceItems(Colors, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            colorSelected = Colors[i];
                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(Feed.this, "selected: " + colorSelected, Toast.LENGTH_LONG).show();
                            amount.setText(colorSelected);
                            String showAmount = amount.getText().toString().trim();
                            //mdatabase.child("Food Amount").setValue(showAmount);
                            intensityDbStatus.setValue(showAmount);
                            showDateandTime();
                        }
                    });
                    builder.setNegativeButton("Cancel",null);
                    builder.show();
                }

                private void showDateandTime() {
                    TextView tdate = (TextView) findViewById(R.id.textDateandTime);
                    long date = System.currentTimeMillis();
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss a");
                    String dateString = sdf.format(date);
                    tdate.setText(dateString);
                }
            });
        }
        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
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