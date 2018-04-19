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
import android.widget.LinearLayout;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.content.Context;
import android.view.Menu;
import android.widget.LinearLayout;

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
        myView view=new myView(this);

        setTitle(year+"-"+month+"-"+day);


        buttonDate=findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(this);
        LinearLayout linearLayout=new LinearLayout(this);
        linearLayout=findViewById(R.id.a10001);
        linearLayout.addView(view);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    class myView extends View {
        Paint mPaint = new Paint();
        public myView(Context context) {
            super(context);
            mPaint.setColor(Color.YELLOW);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(3);
        }

        //实现View中的OnMeasure方法进行测量
        private double height;
        private double width;
        private double sectionHeight;

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            height = MeasureSpec.getSize(heightMeasureSpec);
            width = MeasureSpec.getSize(widthMeasureSpec);
            sectionHeight = height / 11;//10hours + 1 title
            setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);

            //setMeasuredDimension(width,height);
        }

        protected void onDraw(Canvas canvas) {

            //画横线
            for (int i = 0; i <= 11; i++) {
                canvas.drawLine(0, (float) (i * sectionHeight),
                        (float) width, (float) (i * sectionHeight), mPaint);
            }
        }
    }

}
