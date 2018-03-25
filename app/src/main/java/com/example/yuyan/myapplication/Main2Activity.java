package com.example.yuyan.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.Toast;

import java.util.Calendar;


public class Main2Activity extends AppCompatActivity implements OnClickListener{
    private DatePicker datePicker;
    private Calendar cal;
    private Button buttonDate;

    private int year;
    private int month;
    private int day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH)+1;
        day=cal.get(Calendar.DAY_OF_MONTH);


        setTitle(year+"-"+month+"-"+day);

       // datePicker=findViewById(R.id.datePicker);


        buttonDate=findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View V){

        new DatePickerDialog(this, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                setTitle(year+"-"+month+"-"+day);
            }
        }, year, cal.get(Calendar.MONTH), day).show();
    }


//        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                setTitle(year+"-"+month+"-"+day);
//            }
//        },year,cal.get(Calendar.MONTH),day).show();

}
