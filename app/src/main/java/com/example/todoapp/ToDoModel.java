
//Structure Class
package com.example.todoapp;

public class ToDoModel {

    String sno;
    int id;
    String title,desc,date,time;

    //Constructor
    public ToDoModel(String sno,String title,String desc,String date,String time){

        this.sno = sno;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.time = time;

    }

    public ToDoModel(int id,String title,String desc,String date,String time){

        this.id = id;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.time = time;

    }

    public ToDoModel(String title,String desc,String date,String time){


        this.title = title;
        this.desc = desc;
        this.date = date;
        this.time = time;

    }

    public ToDoModel(){



    }
}
