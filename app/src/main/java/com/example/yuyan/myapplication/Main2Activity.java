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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.Gravity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main2Activity extends AppCompatActivity implements OnClickListener{
    private DatePicker datePicker;
    private Calendar cal;
    private Button buttonDate;

    private int year;
    private int month;
    private int day;

    private int gridHeight,gridWidth;
    private RelativeLayout layout;
    private static boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH)+1;
        day=cal.get(Calendar.DAY_OF_MONTH);
        /*myView view=new myView(this);*/

        setTitle(year+"-"+month+"-"+day);


        buttonDate=findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(this);
        layout=findViewById(R.id.layout);
        /*LinearLayout linearLayout=new LinearLayout(this);
        linearLayout=findViewById(R.id.a10001);
        linearLayout.addView(view);*/
    }

    @Override
    public void onClick(View V){

        new DatePickerDialog(this, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year1, int month1, int day1) {

                setTitle(year1+"-"+(month1+1)+"-"+day1);
                year=year1;
                month=month1+1;
                day=day1;
                Event[] events=new Event[1];
                String text;
                int count=0;
                try {
                    events=readFileOnLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                layout.removeAllViews();
                for(Event i:events){
                    text=i.start+"-"+i.end+"\n"+i.summary+"\n"+i.location+"\n"+i.prof;
                    addView(count+1,count+2,text);
                }

            }
        }, year, month-1, day).show();/*设置按钮打开的日历*/
    }
    private void addView(int start,int end,String text){
        TextView tv;
        tv= createTv(start,end,text);
        tv.setBackgroundColor(Color.argb(100,start*5,(start+end)*20,0));
        layout.addView(tv);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(isFirst) {
            isFirst = false;
            gridWidth = layout.getWidth();
            gridHeight = layout.getHeight()/6;
        }
    }

    private TextView createTv (int start, int end, String text){
        TextView tv =new TextView(this);
        /*指定高度和宽度*/
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth,gridHeight*30);
        /*指定位置*/
        tv.setY(gridHeight*(start-1));
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        return tv;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

/*
    public String readFile(String fileName) throws IOException {
        String res="";
        try{
            FileInputStream fin;
            fin = openFileInput(fileName);
            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            res = new String (buffer, "UTF-8");
            fin.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }*/
    private class Event{
        public String start;
        public String end;
        public String summary;
        public String location;
        public String prof;


}
    Event[] readFileOnLine() throws IOException {
        String strFileName = "Timetable.ics";
        File fis = new File(strFileName);
        BufferedReader sBuffer = new BufferedReader(new FileReader(fis));
        String strLine = null;
        int size=0;
        int len=0;
        while((strLine =  sBuffer.readLine()) != null) {
            size = strLine.indexOf("DTSTART:"+String.valueOf(this.year)+String.valueOf(this.month)+String.valueOf(this.day));
            if (size>-1) len++;
        }

        Event[] rslt=new Event[len];
        int i=0;
        boolean bool=false;
        while((strLine =  sBuffer.readLine()) != null) {
            size = strLine.indexOf("DTSTART:"+String.valueOf(this.year)+String.valueOf(this.month)+String.valueOf(this.day));
            if (size>-1) {
                String reg = "t(.*?)00z";
                Pattern p = Pattern.compile(reg);
                Matcher m = p.matcher(strLine);
                rslt[i].start = m.group(1);
                bool = true;
                i++;
                continue;
            }
            size = strLine.indexOf("DTEND:");
            if (size>-1 && bool){
                String reg="t(.*?)00z";
                Pattern p = Pattern.compile(reg);
                Matcher m = p.matcher(strLine);
                rslt[i].end=m.group(1);
                continue;
            }
            size = strLine.indexOf("SUMMARY:");
            if (size>-1 && bool){
                String reg="SUMMARY:(.*?)";
                Pattern p = Pattern.compile(reg);
                Matcher m = p.matcher(strLine);
                rslt[i].summary=m.group(1);
            }
            size = strLine.indexOf("LOCATION:");
            if (size>-1 && bool){
                String reg="LOCATION:(.*?)";
                Pattern p = Pattern.compile(reg);
                Matcher m = p.matcher(strLine);
                rslt[i].location=m.group(1);
            }

            size = strLine.indexOf("Prof :");
            if (size>-1 && bool) {
                String reg = "Prof :(.*?)\ngroupe";
                Pattern p = Pattern.compile(reg);
                Matcher m = p.matcher(strLine);
                rslt[i].prof = m.group(1);
            }

            size = strLine.indexOf("END:VEVENT");
            if (size>-1 && bool) {
                bool=false;
            }

        }
        sBuffer.close();
        return(rslt);
    }



/*
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
    }*/



}
