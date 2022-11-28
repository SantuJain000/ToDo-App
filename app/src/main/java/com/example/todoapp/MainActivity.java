package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    ArrayList<ToDoModel> arrToDos = new ArrayList<>();
    MyDBHelper dbHelper = new MyDBHelper(MainActivity.this);
    FloatingActionButton btnOpenDialog;
    RecyclerToDoAdapter adapter ;
    RecyclerView recyclerView;
    FloatingActionButton btnReload;
    static final int ALARM_REQ_CODE = 100;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        registerReceiver(broadcastReceiver, new IntentFilter("INTERNET_LOST"));


        recyclerView = findViewById(R.id.recViewToDO);
        btnOpenDialog = findViewById(R.id.btnOpenDialog);
        btnReload = findViewById(R.id.btnReload);

        //To Define How RecyclerView will be shown either Linear or Grid or Staggered
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Alaram Manager Service
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent iBroadcast = new Intent(MainActivity.this,MyReceiver.class);

        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this,ALARM_REQ_CODE,iBroadcast,PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),pi);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//                System.currentTimeMillis(),
//                1000, pi);

        // Set the alarm to start at 7:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 07);
        calendar.set(Calendar.MINUTE, 00);

// setRepeating() lets you specify a precise custom interval--in this case,
// 20 minutes.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 1, pi);




        //To open Dialog For Adding Todos
        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_update_lay);


                // on below line we are initializing our variables.

                ImageView idBtnPickDate = dialog.findViewById(R.id.idBtnPickDate);
                ImageView idBtnPickTime = dialog.findViewById(R.id.idBtnPickTime);
                TextView txtDate = dialog.findViewById(R.id.txtDate);
                TextView txtTime = dialog.findViewById(R.id.txtTime);
                TextView txtTitle = dialog.findViewById(R.id.txtTitle);
                EditText edtTitle = dialog.findViewById(R.id.edtTitle);
                EditText edtDesc = dialog.findViewById(R.id.edtDesc);
                Button btnAdd = dialog.findViewById(R.id.btnAdd);


                // on below line we are adding click listener for our pick date button
                idBtnPickDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // on below line we are getting
                        // the instance of our calendar.
                        final Calendar c = Calendar.getInstance();

                        // on below line we are getting
                        // our day, month and year.
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);

                        // on below line we are creating a variable for date picker dialog.
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                // on below line we are passing context.
                                MainActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        // on below line we are setting date to our text view.
                                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    }
                                },
                                // on below line we are passing year,
                                // month and day for selected date in our date picker.
                                year, month, day);
                        // at last we are calling show to
                        // display our date picker dialog.
                        datePickerDialog.show();
                    }
                });


                // on below line we are adding click
                // listener for our pick date button
                idBtnPickTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // on below line we are getting the
                        // instance of our calendar.
                        final Calendar c = Calendar.getInstance();

                        // on below line we are getting our hour, minute.
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int minute = c.get(Calendar.MINUTE);

                        // on below line we are initializing our Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        // on below line we are setting selected time
                                        // in our text view.
                                        txtTime.setText(hourOfDay + ":" + minute);
                                    }
                                }, hour, minute, false);
                        // at last we are calling show to
                        // display our time picker dialog.
                        timePickerDialog.show();
                    }
                });






                //on below line we are adding click listener for our ADD Button
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String title="",desc="",date="",time="";

                        if(!edtTitle.getText().toString().equals("")){

                        title = edtTitle.getText().toString();
                        }else{
                            Toast.makeText(MainActivity.this, "Please Enter Title.", Toast.LENGTH_SHORT).show();
                        }

                        if(!edtDesc.getText().toString().equals("")){

                            desc = edtDesc.getText().toString();
                        }else{
                            Toast.makeText(MainActivity.this, "Please Enter Description.", Toast.LENGTH_SHORT).show();
                        }

                        if(!txtDate.getText().toString().equals("")){

                            date = txtDate.getText().toString();
                        }else{
                            Toast.makeText(MainActivity.this, "Please Enter Date.", Toast.LENGTH_SHORT).show();
                        }

                        if(!txtTime.getText().toString().equals("")){

                            time = txtTime.getText().toString();
                        }else{
                            Toast.makeText(MainActivity.this, "Please Enter Time.", Toast.LENGTH_SHORT).show();
                        }



                        dbHelper.addToDos(title,desc,date,time);
                        ArrayList<ToDoModel> arrToDos = dbHelper.fetchToDos();
                        adapter = new RecyclerToDoAdapter(MainActivity.this,arrToDos);
                        recyclerView.setAdapter(adapter);
                        recyclerView.scrollToPosition(arrToDos.size()-1);
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "ToDo Inserted Successfully.", Toast.LENGTH_SHORT).show();
                        Reload();
                        recyclerView.scrollToPosition(arrToDos.size()-1);
                    }
                });

                dialog.show();
            }
        });


        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reload();

            }
        });



Reload();


    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Reload();
        }
    };

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
//    }

    public void Reload() {
        ArrayList<ToDoModel> arrToDos = dbHelper.fetchToDos();
        // on below line we are creating and initializing
        // variable for simple date format.
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyyHH:mm");

        // on below line we are creating a variable
        // for current date and time and calling a simple date format in it.
        String currentDateAndTime = sdf.format(new Date());
        Log.d("sdf",currentDateAndTime);

        for (int i = 0;i<arrToDos.size();i++){

            if(currentDateAndTime.equals(arrToDos.get(i).date+arrToDos.get(i).time)){
                Log.d("sdf",currentDateAndTime);
                Log.d("dtm",arrToDos.get(i).date+arrToDos.get(i).time);

                Intent iNext;
                iNext = new Intent(MainActivity.this,NotificationService.class);

                //Code for bundle(data) passing

                //Sending Data with intent
                iNext.putExtra("title",arrToDos.get(i).title);
                iNext.putExtra("desc",arrToDos.get(i).desc);

                startService(iNext);
            }
        }
        RecyclerToDoAdapter adapter = new RecyclerToDoAdapter(this, arrToDos);
        recyclerView.setAdapter(adapter);
        //recyclerView.scrollToPosition(arrToDos.size()-1);


    }


}