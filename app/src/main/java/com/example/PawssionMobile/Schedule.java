package com.example.PawssionMobile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Schedule extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;

    private AlarmManager alarmManager;
    private Calendar calendar;
    private PendingIntent pendingIntent;
    TimePicker timePicker;

    String[] Colors={"23 grams(appx.)","30 grams(appx.)","39 grams(appx.)"};
    String colorSelected;
    TextView textDateandTime;
    ImageButton imagebuttonOne;
    ImageButton imagebuttonTwo;
    ImageButton imagebuttonThree;
    TextView txtOne;
    TextView txtTwo;
    TextView txtThree;
    TextView txtTimeOne;
    TextView txtTimeTwo;
    TextView txtTimeThree;
    TextClock txtTime;
    CheckBox checkSchedule;
    String dateString;
    Button cancelOne;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        textDateandTime = findViewById(R.id.textDateandTime);
        imagebuttonThree = findViewById(R.id.imagebuttonThree);
        imagebuttonTwo = findViewById(R.id.imagebuttonTwo);
        imagebuttonOne = findViewById(R.id.imagebuttonOne);
        txtOne = findViewById(R.id.txtOne);
        txtTwo = findViewById(R.id.txtTwo);
        txtThree = findViewById(R.id.txtThree);
        txtTimeOne = findViewById(R.id.txtTimeOne);
        txtTimeTwo = findViewById(R.id.txtTimeTwo);
        txtTimeThree = findViewById(R.id.txtTimeThree);
        checkSchedule = findViewById(R.id.checkSchedule);
        txtTime = findViewById(R.id.txtTime);
        cancelOne = findViewById(R.id.cancelOne);



        timePicker = findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourofDay, int minute) {

            }
        });
        showImageButtonOne();
        showImageButtonTwo();
        showImageButtonThree();

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    private void showImageButtonOne() {
        imagebuttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
            private void showDialog() {
                colorSelected = Colors[0];
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Schedule.this);
                builder.setTitle("Choose Amount");
                builder.setSingleChoiceItems(Colors, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        colorSelected = Colors[i];
                    }
                });
                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                    showTimerOne();
                    showDateandTime();
                    setAlarm();

                });
                builder.setNegativeButton("Cancel",null);
                builder.show();
            }

            private void setAlarm() {

                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Intent intent = new Intent(Schedule.this,ReminderBroadcast.class);

                pendingIntent = PendingIntent.getBroadcast(Schedule.this, 0, intent, 0);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY,pendingIntent);

                Toast.makeText(Schedule.this, "Schedule Set!", Toast.LENGTH_SHORT).show();

            }

            private void showTimerOne() {

                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                }
                else{
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }
                if(hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                }
                else
                {
                    am_pm="AM";
                }
                calendar = Calendar.getInstance();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    calendar.set(Calendar.MINUTE,timePicker.getMinute());
                }
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                TextView tdate = (TextView) findViewById(R.id.txtOne);
                long date = System.currentTimeMillis();
                String alarmTimer = hour +":"+ minute+" "+am_pm;
                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy");
                dateString = sdf.format(date);
                tdate.setText(dateString);

                String show = "";
                if(checkSchedule.isChecked()){
                    show += "Schedule: Repeat";
                }
                txtTimeOne.setText("\n  "+hour +":"+ minute+" "+am_pm);
                txtOne.setText(dateString+" , "+"\n  "+colorSelected+"\n  "+show);
            }
        });
    }

    private void showDateandTime() {
        TextView tdate = (TextView) findViewById(R.id.textDateandTime);
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss a");
        String dateString = sdf.format(date);
        tdate.setText(dateString);
    }

    private void showImageButtonTwo() {
        imagebuttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
            private void showDialog() {
                colorSelected = Colors[0];
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Schedule.this);
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
                        Toast.makeText(Schedule.this, "selected: " + colorSelected, Toast.LENGTH_SHORT).show();
                        showTimerTwo();
                        showDateandTime();
                        setAlarm();
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.show();
            }

            private void setAlarm() {
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Intent intent = new Intent(Schedule.this,ReminderBroadcast.class);

                pendingIntent = PendingIntent.getBroadcast(Schedule.this, 0, intent, 0);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY,pendingIntent);

                Toast.makeText(Schedule.this, "Schedule Set!", Toast.LENGTH_SHORT).show();
            }

            private void showTimerTwo() {
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                }
                else{
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }
                if(hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                }
                else
                {
                    am_pm="AM";
                }
                TextView tdate = (TextView) findViewById(R.id.txtTwo);
                long date = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy");
                dateString = sdf.format(date);
                tdate.setText(dateString);
                String show = "";
                if(checkSchedule.isChecked()){
                    show += "Schedule: Repeat";
                }
                txtTimeTwo.setText("\n  "+hour +":"+ minute+" "+am_pm);
                txtTwo.setText(dateString+" , "+"\n  "+colorSelected+"\n  "+show);
            }
        });
    }


    private void showImageButtonThree() {
        imagebuttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
            private void showDialog() {
                colorSelected = Colors[0];
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Schedule.this);
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
                        Toast.makeText(Schedule.this, "selected: " + colorSelected, Toast.LENGTH_SHORT).show();
                        showTimerThree();
                        showDateandTime();
                        setAlarm();
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.show();
            }

            private void setAlarm() {
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Intent intent = new Intent(Schedule.this,ReminderBroadcast.class);

                pendingIntent = PendingIntent.getBroadcast(Schedule.this, 0, intent, 0);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY,pendingIntent);

                Toast.makeText(Schedule.this, "Schedule Set!", Toast.LENGTH_SHORT).show();

            }


            private void showTimerThree() {
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                }
                else{
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }
                if(hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                }
                else
                {
                    am_pm="AM";
                }
                TextView tdate = (TextView) findViewById(R.id.txtThree);
                long date = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy");
                dateString = sdf.format(date);
                tdate.setText(dateString);
                String show = "";
                if(checkSchedule.isChecked()){
                    show += "Schedule: Repeat";
                }
                txtTimeThree.setText("\n  "+hour +":"+ minute+" "+am_pm);
                txtThree.setText(dateString+" , "+"\n  "+colorSelected+"\n  "+show);

            }
        });
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
        //Redirect activity to View Camera
        MainActivity.redirectActivity(this, ViewCamera.class);
    }

    public void ClickSchedule(View view){
        //Recreate activity
        recreate();
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