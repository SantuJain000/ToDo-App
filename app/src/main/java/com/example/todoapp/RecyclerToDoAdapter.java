package com.example.todoapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecyclerToDoAdapter extends RecyclerView.Adapter<RecyclerToDoAdapter.ViewHolder> {


    Context context;
    ArrayList<ToDoModel> arrToDo;
    int lastposition=-1;

    RecyclerToDoAdapter adapter ;


    RecyclerToDoAdapter(Context context, ArrayList<ToDoModel> arrToDo) {
        this.context = context;
        this.arrToDo = arrToDo;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.todo_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.sno.setText(arrToDo.get(position).sno);
        holder.title.setText(arrToDo.get(position).title);
        holder.desc.setText(arrToDo.get(position).desc);
        holder.date.setText(arrToDo.get(position).date);
        holder.time.setText(arrToDo.get(position).time);

        setAnimation(holder.itemView,position);






        //To Update
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_update_lay);




                ImageView idBtnPickDate = dialog.findViewById(R.id.idBtnPickDate);
                ImageView idBtnPickTime = dialog.findViewById(R.id.idBtnPickTime);
                TextView txtDate = dialog.findViewById(R.id.txtDate);
                TextView txtTime = dialog.findViewById(R.id.txtTime);
                TextView txtTitle = dialog.findViewById(R.id.txtTitle);
                EditText edtTitle = dialog.findViewById(R.id.edtTitle);
                EditText edtDesc = dialog.findViewById(R.id.edtDesc);
                Button btnAdd = dialog.findViewById(R.id.btnAdd);

                btnAdd.setText("Update");
                txtTitle.setText("Update A ToDo");

                edtTitle.setText(arrToDo.get(position).title);
                edtDesc.setText(arrToDo.get(position).desc);
                txtDate.setText(arrToDo.get(position).date);
                txtTime.setText(arrToDo.get(position).time);



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
                                context,
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
                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
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


                //To update
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String title="",desc="",date="",time="";

                        if(!edtTitle.getText().toString().equals("")){

                            title = edtTitle.getText().toString();
                        }else{
                            Toast.makeText(context, "Please Enter Title.", Toast.LENGTH_SHORT).show();
                        }

                        if(!edtDesc.getText().toString().equals("")){

                            desc = edtDesc.getText().toString();
                        }else{
                            Toast.makeText(context, "Please Enter Description.", Toast.LENGTH_SHORT).show();
                        }

                        if(!txtDate.getText().toString().equals("")){

                            date = txtDate.getText().toString();
                        }else{
                            Toast.makeText(context, "Please Enter Date.", Toast.LENGTH_SHORT).show();
                        }

                        if(!txtTime.getText().toString().equals("")){

                            time = txtTime.getText().toString();
                        }else{
                            Toast.makeText(context, "Please Enter Time.", Toast.LENGTH_SHORT).show();
                        }

                        ToDoModel model = new ToDoModel(arrToDo.get(position).id,title,desc,date,time);


                        MyDBHelper dbHelper = new MyDBHelper(context);
                        dbHelper.updateToDos(model);
                        Toast.makeText(context, "ToDo Updated Successfully.", Toast.LENGTH_SHORT).show();
                        notifyItemChanged(position);
                        ((MainActivity)context).Reload();//To show data
                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });

        //To delete
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //OPenong A alert box
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete Contact")
                        .setMessage("Are you sure want to Delete?")
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                arrContacts.remove(position);
//                                notifyItemRemoved(position);

                                MyDBHelper dbHelper = new MyDBHelper(context);
                                dbHelper.deleteToDos((arrToDo.get(position).id));
                                Toast.makeText(context, "ToDo Deleted Successfully.", Toast.LENGTH_SHORT).show();
                                ((MainActivity)context).Reload();


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();

            }
        });





    }

    @Override
    public int getItemCount() {
        return arrToDo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sno,title,desc,date,time;
        LinearLayout llrow;
        Button btnUpdate,btnDelete;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sno = itemView.findViewById(R.id.sno);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            llrow = itemView.findViewById(R.id.llrow);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            recyclerView = itemView.findViewById(R.id.recViewToDO);

        }
    }

    private void setAnimation(View view,int position){
        if(position>lastposition) {
            Animation slidein = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            view.startAnimation(slidein);
            lastposition=position;
        }
    }


}
