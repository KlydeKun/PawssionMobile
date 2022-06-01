package com.example.PawssionMobile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Schedule extends AppCompatActivity {
    //Initialize variable
    DrawerLayout drawerLayout;

    private AlarmManager alarmManager;
    private Calendar calendar;
    private PendingIntent pendingIntent;
    TimePicker timePicker;

    String[] Colors = {"1/4 cup(appx.)","1/2 cup(appx.)","3/4 cup(appx.)"};
    String colorSelected;
    TextView textDateandTime;
    ImageButton imagebuttonOne, imagebuttonTwo, imagebuttonThree;
    TextView txtOne, txtTwo, txtThree;
    TextView txtTimeOne, txtTimeTwo, txtTimeThree;
    TextClock txtTime;
    CheckBox checkSchedule;
    String dateString;
    Button cancelOne;
    Button cancelTwo;
    Button cancelThree;

    Boolean connected = false;
    private DatabaseReference mdatabase;



    private long scheduleToMillis(String schedule){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String scheduleDate = dtf.format(LocalDateTime.now()) + " " + schedule + ":00";

        long millis = -1;
        try {
            millis = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(scheduleDate).getTime();
        }
        catch(Exception ex) {}
        return millis;
    }

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
        cancelTwo = findViewById(R.id.cancelTwo);
        cancelThree = findViewById(R.id.cancelThree);

        timePicker = findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourofDay, int minute) {

            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (!connected) {
            Toast.makeText(Schedule.this, "No Internet", Toast.LENGTH_LONG).show();
        } else {
            //Write a message to the database
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference firstScheduleDbStatus = database.getReference("1st Schedule");
            final DatabaseReference firstAmountDbStatus = database.getReference("1st amount");
            final DatabaseReference secondScheduleDbStatus = database.getReference("2nd Schedule");
            final DatabaseReference secondAmountDbStatus = database.getReference("2nd amount");
            final DatabaseReference thirdScheduleDbStatus = database.getReference("3rd Schedule");
            final DatabaseReference thirdAmountDbStatus = database.getReference("3rd amount");
            mdatabase = FirebaseDatabase.getInstance().getReference();


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
                    builder.setPositiveButton("OK", (DialogInterface dialogInterface, int i) -> {
                        Toast.makeText(Schedule.this, "selected: " + colorSelected, Toast.LENGTH_LONG).show();
                        txtOne.setText(colorSelected);
                        String showAmount = txtOne.getText().toString().trim();
                        firstAmountDbStatus.setValue((showAmount));
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

                    String minString = Integer.toString(minute);
                    minString = minString.length() == 1 ? "0" + minString : minString;
                    SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy");
                    dateString = sdf.format(date);
                    tdate.setText(dateString);

                    String show = "";

                    txtTimeOne.setText("\n  "+ hour + ":" + minString + " " + am_pm);
                    String showTime = txtTimeOne.getText().toString().trim();
                    firstScheduleDbStatus.setValue(showTime);
                    txtOne.setText(dateString+" , "+"\n  "+colorSelected+"\n  "+show);
                }
            });

            cancelOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCancelAlarm();
                    txtTimeOne.setText("\n    Time");
                    txtOne.setText("\n     Schedule Feed 1");

                }

                private void setCancelAlarm() {
                    Intent intent = new Intent(Schedule.this, ReminderBroadcast.class);

                    pendingIntent = PendingIntent.getBroadcast(Schedule.this,0,intent,0);

                    if (alarmManager == null){

                        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    }

                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(Schedule.this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();

                }
            });

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
                            txtTwo.setText(colorSelected);
                            String showAmount = txtTwo.getText().toString().trim();
                            secondAmountDbStatus.setValue(showAmount);
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
                    calendar = Calendar.getInstance();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        calendar.set(Calendar.MINUTE,timePicker.getMinute());
                    }
                    calendar.set(Calendar.SECOND,0);
                    calendar.set(Calendar.MILLISECOND,0);
                    TextView tdate = (TextView) findViewById(R.id.txtTwo);
                    long date = System.currentTimeMillis();
                    String minString = Integer.toString(minute);
                    minString = minString.length() == 1 ? "0" + minString : minString;
                    SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy");
                    dateString = sdf.format(date);
                    tdate.setText(dateString);
                    String show = "";

                    txtTimeTwo.setText("\n  "+ hour +":"+ minString + " " + am_pm);
                    String showTime = txtTimeTwo.getText().toString().trim();
                    secondScheduleDbStatus.setValue(scheduleToMillis(hour + ":" + minString));
                    txtTwo.setText(dateString+" , "+"\n  "+colorSelected+"\n  "+show);
                }
            });

            cancelTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCancelAlarm();
                    txtTimeTwo.setText("\n    Time");
                    txtTwo.setText("\n     Schedule Feed 2");

                }

                private void setCancelAlarm() {
                    Intent intent = new Intent(Schedule.this, ReminderBroadcast.class);

                    pendingIntent = PendingIntent.getBroadcast(Schedule.this,0,intent,0);

                    if (alarmManager == null){

                        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    }

                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(Schedule.this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();

                }
            });
            
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
                            txtThree.setText(colorSelected);
                            String showAmount = txtThree.getText().toString().trim();
                            thirdAmountDbStatus.setValue(showAmount);
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
                    calendar = Calendar.getInstance();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        calendar.set(Calendar.MINUTE,timePicker.getMinute());
                    }
                    calendar.set(Calendar.SECOND,0);
                    calendar.set(Calendar.MILLISECOND,0);
                    TextView tdate = (TextView) findViewById(R.id.txtThree);
                    long date = System.currentTimeMillis();
                    String minString = Integer.toString(minute);
                    minString = minString.length() == 1 ? "0" + minString : minString;
                    SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy");
                    dateString = sdf.format(date);
                    tdate.setText(dateString);
                    String show = "";
                    if(checkSchedule.isChecked()){
                        show += "Schedule: Repeat";
                    }
                    txtTimeThree.setText("\n  " + hour + ":" + minString + " " + am_pm);
                    String showTime = txtTimeThree.getText().toString().trim();
                    thirdScheduleDbStatus.setValue(scheduleToMillis(hour + ":" + minString));
                    txtThree.setText(dateString+" , "+"\n  "+colorSelected+"\n  "+show);

                }
            });
            
            cancelThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCancelAlarm();
                    txtTimeThree.setText("\n    Time");
                    txtThree.setText("\n     Schedule Feed 3");
                }

                private void setCancelAlarm() {
                    Intent intent = new Intent(Schedule.this, ReminderBroadcast.class);

                    pendingIntent = PendingIntent.getBroadcast(Schedule.this,0,intent,0);

                    if (alarmManager == null){

                        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    }

                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(Schedule.this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
                }
            });

        }

        //Assign variable
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    private void showDateandTime() {
        TextView tdate = (TextView) findViewById(R.id.textDateandTime);
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy, hh:mm:ss a");
        String dateString = sdf.format(date);
        tdate.setText(dateString);
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