package com.example.yuyan.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.Gravity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Main2Activity extends AppCompatActivity implements OnClickListener{
    private DatePicker datePicker;
    private Calendar cal;
    private Button buttonDate;
    private Handler handler=new Handler();
    private int year;
    private int month;
    private int day;
    private int weekday;
    private String sWeekday="";
    private int gridHeight,gridWidth;
    private LinearLayout layout;
    //private ScrollView scrollView;
    private static boolean isFirst = true;

    public Handler getHandler() {
        return handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH)+1;
        day=cal.get(Calendar.DAY_OF_MONTH);
        Date d1 = new Date(year-1900,month-1,day);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(d1);



        /*myView view=new myView(this);*/

        setTitle(day+"-"+month+"-"+year+" "+week);


        buttonDate=findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(this);
        layout=findViewById(R.id.layout);
        //scrollView=findViewById(R.id.scrollview);
        /*LinearLayout linearLayout=new LinearLayout(this);
        linearLayout=findViewById(R.id.a10001);
        linearLayout.addView(view);*/
    }

    @Override
    public void onClick(View V){

        new DatePickerDialog(this, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year1, int month1, int day1) {

                Handler mainHandler=Main2Activity.this.getHandler();
                year=year1;
                month=month1+1;
                day=day1;
                Date d1 = new Date(year1-1900,month1,day1);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                String week = sdf.format(d1);
                setTitle(day1+"-"+(month1+1)+"-"+year1+" "+week);
                Map[] events=null;
                String text,txtStart,txtEnd;
                int count=0;
                try {
                    events=readFileOnLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                layout.removeAllViews();
                int start,end,hour,minute=0;
                for(Map i:events){
                    start=Integer.parseInt((String)i.get("start"))+200;
                    end=Integer.parseInt((String)i.get("end"))+200;
                    hour=start/100;
                    minute=start%100;
                    txtStart=String.format(Locale.ENGLISH,"%02d",hour)+":"+String.format(Locale.ENGLISH,"%02d",minute);
                    hour=end/100;
                    minute=end%100;
                    txtEnd=String.format(Locale.ENGLISH,"%02d",hour)+":"+String.format(Locale.ENGLISH,"%02d",minute);
                    text=txtStart +"-"+txtEnd+"\n"+i.get("summary")+"\n"+"LOC : "+i.get("location")+"\n"+"PROF : "+i.get("prof");
                    text=text.substring(0,text.length()-2);

                    addView(count,text);
                    count++;
                }

            }
        }, year, month-1, day).show();/*设置按钮打开的日历*/
    }
    private void addView(int start,String text){
        TextView tv;
        tv= createTv(start,text);
        tv.setBackgroundColor(Color.argb(100,10*start,(start+3)*10,(start+6)*18));
        layout.addView(tv);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(isFirst) {
            isFirst = false;
            gridWidth = layout.getWidth();
            gridHeight = layout.getHeight()/2;
        }
    }

    private TextView createTv (int start, String text){
        TextView tv =new TextView(this);
        /*指定高度和宽度*/
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth,gridHeight);
        /*指定位置*/
        tv.setY(start*gridHeight/100);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        tv.setTypeface(null, Typeface.BOLD);
        return tv;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    //重写OptionsItemSelected(MenuItem item)来响应菜单项(MenuItem)的点击事件（根据id来区分是哪个item）
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case 2131165301:

                break;
            case R.id.over:
                try {
                    File file = new File("usrpsd.dat");
                    file.delete();
                }
                catch(Exception e){}
                Intent intent= new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
        //int id=item.getItemId();
        return super.onOptionsItemSelected(item);
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



    Map[] readFileOnLine() throws IOException {
        String res=null;
        String strFileName = "Timetable.ics";
        //File fis = new File(strFileName);
        //BufferedReader sBuffer = new BufferedReader(new FileReader(fis));
        FileInputStream fin = openFileInput(strFileName);
        int length = fin.available();
        byte [] buffer = new byte[length];
        fin.read(buffer);
        res=new String(buffer, "UTF-8");
        //res = EncodingUtils.getString(buffer, "UTF-8");
        BufferedReader sBuffer=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(res.getBytes(Charset.forName("UTF-8"))), Charset.forName("UTF-8")));
        fin.close();

        String strLine = null;
        int size=0;
        int len=0;
        while((strLine =  sBuffer.readLine()) != null) {
            size = strLine.indexOf("DTSTART:"+String.valueOf(this.year)+String.format(Locale.ENGLISH,"%02d",this.month)+String.format(Locale.ENGLISH,"%02d",this.day));
            if (size>-1) len++;
        }

        Map[] rslt=new HashMap[len];
        for (int i=0;i<len;i++){
            rslt[i]=new HashMap();
        }
        int i=0;
        boolean bool=false;
        sBuffer=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(res.getBytes(Charset.forName("UTF-8"))), Charset.forName("UTF-8")));
        Pattern p;
        Matcher m;
        String t="";
        String miao="";
        boolean profExists=false;
        boolean locaExists=false;
        while((strLine =  sBuffer.readLine()) != null) {
            size = strLine.indexOf("DTSTART:"+String.valueOf(this.year)+String.format(Locale.ENGLISH,"%02d",this.month)+String.format(Locale.ENGLISH,"%02d",this.day));
            if (size>-1) {
                String reg = "T(\\d*?)00Z";
                p = Pattern.compile(reg);
                m = p.matcher(strLine);
                if (m.find()) {
                    rslt[i].put("start", m.group(1));
                }
                bool = true;
                continue;
            }
            size = strLine.indexOf("DTEND:");
            if (size>-1 && bool){
                String reg="T(\\d*?)00Z";
                p = Pattern.compile(reg);
                m = p.matcher(strLine);
                if (m.find()) {
                    rslt[i].put("end",m.group(1));
                }
                continue;
            }
            size = strLine.indexOf("SUMMARY:");
            if (size>-1 && bool){
                String reg="SUMMARY:(.*) \\(";
                p = Pattern.compile(reg);
                m = p.matcher(strLine);
                if (m.find())
                    rslt[i].put("summary",m.group(1));
            }
            size = strLine.indexOf("LOCATION:");
            if (bool && locaExists && strLine.indexOf("DESCRIPTION:")==-1) {
                t=t+strLine.substring(1,strLine.length());
            }
            if (size>-1 && bool && !locaExists) {
                locaExists=true;
                t=strLine.substring(0,strLine.length());
            }
            if (size>-1 && bool && strLine.indexOf("DESCRIPTION:")==-1){
                String reg="LOCATION:(.*)";
                p = Pattern.compile(reg);
                m = p.matcher(strLine);
                if (m.find())
                    rslt[i].put("location",m.group(1));
            }

            size = strLine.indexOf("DESCRIPTION:");
            if (bool && profExists && strLine.indexOf("CATEGORIES:")==-1) {

                t=t+strLine.substring(1,strLine.length());
            }
            if (size>-1 && bool && !profExists && strLine.indexOf("CATEGORIES:")==-1) {
                locaExists = false;
                String reg="LOCATION:(.*)";
                p = Pattern.compile(reg);
                m = p.matcher(t);
                if (m.find())
                    rslt[i].put("location",m.group(1));
                t="";
                profExists=true;
                t=strLine.substring(0,strLine.length());
            }

            size = strLine.indexOf("CATEGORIES:");
            if (size>-1 && bool) {
                profExists = false;
                String reg = "Prof :(.*?)Groupe";
                p = Pattern.compile(reg);
                m = p.matcher(t);
                if (m.find()) {
                    miao = m.group(1);
                    rslt[i].put("prof", m.group(1));
                }
                t="";
            }
            size = strLine.indexOf("END:VEVENT");
            if (size>-1 && bool) {
                i++;
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
