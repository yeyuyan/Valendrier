package com.example.yuyan.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup;
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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.util.TimeZone;



// TODO: 5/17/18  xialacaidan
public class Main2Activity extends AppCompatActivity implements OnClickListener,View.OnTouchListener {
    private DatePicker datePicker;
    private Calendar cal;
    private Button buttonDate;
    private Handler handler=new Handler();
    private int year;
    private int month;
    private int day;
    private int weekday;
    private String sWeekday="";

    private ScrollView scrollView;
    private LinearLayout layout;
    //private ScrollView scrollView;
    private static boolean isFirst;
    private GestureDetector mGestureDetector;
    PullScrollView pullScrollView;
    float x1=0;
    float y1=0;
    float x2=0;
    float y2=0;
    int gridHeight;
    int gridWidth;
    private TextView textView;
    TimeZone tz;

    int offsetTime,offsetTime2;
    String allText;

    public Handler getHandler() {
        return handler;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isFirst=true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH)+1;
        day=cal.get(Calendar.DAY_OF_MONTH);
        Date d1 = new Date(year-1900,month-1,day);
        tz=cal.getTimeZone();
        offsetTime=tz.getOffset(System.currentTimeMillis())/(3600*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(d1);
        mGestureDetector = new GestureDetector(new simpleGestureListener());

        /*myView view=new myView(this);*/

        setTitle(day+"-"+month+"-"+year+" "+week);

        pullScrollView=(PullScrollView) findViewById(R.id.test);
        scrollView=findViewById(R.id.scrollview);
        buttonDate=findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(this);
        layout=(LinearLayout)findViewById(R.id.layout);
        layout.setOnTouchListener(this);
        layout.setLongClickable(true);
        try {
            allText = readFile("Timetable.ics");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View V){
        new DatePickerDialog(this, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year1, int month1, int day1) {
                char color_='w';
                //Handler mainHandler=Main2Activity.this.getHandler();
                setView(day1, month1+1, year1);
                /*Date d1 = new Date(year1-1900,month1,day1);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                String week = sdf.format(d1);
                setTitle(day1+"-"+(month1+1)+"-"+year1+" "+week);
                long mseconds=d1.getTime();
                offsetTime2=tz.getOffset(mseconds)/(3600*1000);
                Map[] events=null;
                String text,txtStart,txtEnd;
                int count=0;
                try {
                    events=readFileOnLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String summary;
                layout.removeAllViews();
                int start,end,hour,minute=0;
                for(Map i:events){
                    start=Integer.parseInt((String)i.get("start"))+offsetTime2*100;
                    end=Integer.parseInt((String)i.get("end"))+offsetTime2*100;
                    hour=start/100;
                    minute=start%100;
                    txtStart=String.format(Locale.ENGLISH,"%02d",hour)+":"+String.format(Locale.ENGLISH,"%02d",minute);
                    hour=end/100;
                    minute=end%100;
                    summary=i.get("summary")+"\n";
                    txtEnd=String.format(Locale.ENGLISH,"%02d",hour)+":"+String.format(Locale.ENGLISH,"%02d",minute);
                    text=txtStart +"-"+txtEnd+"\n"+summary+"LOC : "+i.get("location")+"\n"+"PROF : "+i.get("prof");
                    text=text.substring(0,text.length()-2);
                    if (summary.contains("CM")) color_='p';
                    if (summary.contains("TD")) color_='b';
                    if (summary.contains("TP")) color_='g';
                    if (summary.contains("RES")) color_='G';


                    addView(count,text,color_);
                    count++;
                }
                addView(count,"",'w');
                */
                //layout.addView(tve);

            }
        }, year, month-1, day).show();/*设置按钮打开的日历*/
    }
    private void addView(int start,String text,char color_){
        TextView tv;
        tv= createTv(start,text);
        switch (color_) {
            case 'w':
                tv.setBackgroundColor(Color.argb(100,255,255,255));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth,160);
                tv.setLayoutParams(params);
                break;
            case 'g':
                tv.setBackgroundColor(Color.argb(100,150+3*start,230+(start) * 2,70+(start + 6)*2));
                break;
            case 'b':
                tv.setBackgroundColor(Color.argb(100,150+3*start,200+(start + 3) * 2,240+start*2));
                break;
            case 'p':
                tv.setBackgroundColor(Color.argb(100,200+3*start,200+(start) * 3,255));
                break;
            case 'G':
                tv.setBackgroundColor(Color.argb(100,140,140,140));
                break;
                //Color.argb(100, 10 * start, (start + 3) * 10, (start + 6) * 18));

        }
        layout.addView(tv);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setView(day, month, year);

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
    public void writeFile(String fileName,String writestr) throws IOException{
        FileOutputStream fout=null;
        try{
            //File file=new File(fileName);
            //if (!file.exists()){
            //     file.createNewFile();
            //}
            //FileOutputStream fout =Context.();
            fout=openFileOutput(fileName,MODE_PRIVATE);

            byte [] bytes = writestr.getBytes();

            fout.write(bytes);

        }

        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            fout.close();
        }
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
        boolean bool=false;
        switch (item.getItemId()) {

            case 2131165301:

                break;
            case R.id.over:
                try {
                    //File file = new File("usrpsd.dat");

                    writeFile("usrpsd.dat","");

                }

                catch(Exception e){}
                //if (bool){};
                Intent intent= new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
        //int id=item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    String readFile(String strFileName) throws IOException {
        //String strFileName = "Timetable.ics";
        FileInputStream fin = openFileInput(strFileName);
        int length = fin.available();
        byte [] buffer = new byte[length];
        fin.read(buffer);
        return new String(buffer, "UTF-8");
    }

    Map[] getLessons(String strFile,String date_){
        String reg = "DTSTART:"+date_+"T(.*?)END:VEVENT";
        //date_="20180604";
        Pattern p = Pattern.compile(reg, Pattern.DOTALL);
        Matcher m = p.matcher(strFile);
        Matcher m1;
        String[] content=new String[20];
        int i;
        i=0;
        String regr;
        while(m.find()) {
            regr="(\r*)";
            content[i]=m.group(0);
            p = Pattern.compile(regr);
            m1 = p.matcher(content[i]);
            if (m1.find()) content[i]=m1.replaceAll("");
            regr="(\n *)";
            p = Pattern.compile(regr);
            m1 = p.matcher(content[i]);
            if (m1.find()) content[i]=m1.replaceAll("");
            regr="(\n*)";
            p = Pattern.compile(regr);
            m1 = p.matcher(content[i]);
            regr="(\\\\n*)";
            p = Pattern.compile(regr);
            m1 = p.matcher(content[i]);
            if (m1.find()) content[i]=m1.replaceAll("");
            regr="(\\\\)";
            p = Pattern.compile(regr);
            m1 = p.matcher(content[i]);
            if (m1.find()) content[i]=m1.replaceAll("");
            i = i + 1;
        }
        int len=i;
        Map[] rslt=new HashMap[len];
        for (i=0;i<len;i++){
            rslt[i]=new HashMap();
        }
        if (len==0) return rslt;
        boolean bln=false;
        for (i=0;i<len;i++){
            reg = "DTSTART:" + date_ + "T(.*?)00Z";
            p = Pattern.compile(reg, Pattern.DOTALL);
            m1 = p.matcher(content[i]);
            if (m1.find()){
                rslt[i].put("start",m1.group(1));}
            else rslt[i].put("start","None");
            reg="DTEND:"+date_+"T(.*?)00Z";
            p = Pattern.compile(reg, Pattern.DOTALL);
            m1=p.matcher(content[i]);
            if (m1.find()){
                rslt[i].put("end",m1.group(1));}
            else {rslt[i].put("end", "None");}
            reg="SUMMARY:(.*?)\\(";
            p = Pattern.compile(reg, Pattern.DOTALL);
            m1=p.matcher(content[i]);
            if (m1.find()){ rslt[i].put("summary", m1.group(1));}
            else{rslt[i].put("summary", "None");}
            reg="LOCATION:(.*?)DESCRIPTION";
            p = Pattern.compile(reg, Pattern.DOTALL);
            m1 = p.matcher(content[i]);
            if (m1.find()){ rslt[i].put("location", m1.group(1));}
            else{rslt[i].put("location", "None");}
            reg="Prof : (.*?)Groupe";
            p = Pattern.compile(reg, Pattern.DOTALL);
            m1 = p.matcher(content[i]);
            if (m1.find()){ rslt[i].put("prof", m1.group(1));}
            else{rslt[i].put("prof", "None");}

        }

        return rslt;

    }
    void setView(int day1 , int month1 , int year1){
        if(isFirst) {
            isFirst = false;
            gridWidth = layout.getWidth();
            gridHeight = layout.getHeight() / 2;
        }
        char color_='w';
        Date d1 = new Date(year1-1900,month1-1,day1);
        SimpleDateFormat sdfW = new SimpleDateFormat("EEEE");
        SimpleDateFormat sdfD = new SimpleDateFormat("dd");
        SimpleDateFormat sdfM = new SimpleDateFormat("MM");
        SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
        String week = sdfW.format(d1);
        String day_=sdfD.format(d1);
        String month_=sdfM.format(d1);
        String year_=sdfY.format(d1);
        day=Integer.valueOf(day_);
        year=Integer.valueOf(year_);
        month=Integer.valueOf(month_);
        setTitle(day_+"-"+month_+"-"+year_+" "+week);
        long mseconds=d1.getTime();
        offsetTime=tz.getOffset(mseconds)/(3600*1000);
        Map[] events=null;

        try {
            events=readFileOnLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        events = getLessons(allText,year_+month_+day_);
        String text,txtStart,txtEnd;
        int count=0;
        String summary;
        layout.removeAllViews();
        int start,end,hour,minute=0;
        for(Map i:events){
            start=Integer.parseInt((String)i.get("start"))+offsetTime*100;
            end=Integer.parseInt((String)i.get("end"))+offsetTime*100;
            hour=start/100;
            minute=start%100;
            txtStart=String.format(Locale.ENGLISH,"%02d",hour)+":"+String.format(Locale.ENGLISH,"%02d",minute);
            hour=end/100;
            minute=end%100;
            summary=i.get("summary")+"\n";
            txtEnd=String.format(Locale.ENGLISH,"%02d",hour)+":"+String.format(Locale.ENGLISH,"%02d",minute);
            text=txtStart +"-"+txtEnd+"\n"+summary+"LOC : "+i.get("location")+"\n"+"PROF : "+i.get("prof");
            //text=text.substring(0,text.length()-1);
            if (summary.contains("CM :")) color_='p';
            else if (summary.contains("TD :")) color_='b';
            else if (summary.contains("TP :")) color_='g';
            else if (summary.contains("RES")) {color_='G';text=txtStart +"-"+txtEnd+"\n"+"Reservation\n"+"LOC : "+i.get("location")+"\n";}
            addView(count,text,color_);
            count++;
        }
        addView(count,"",'w');
        //layout.addView(tve);
    }
    Map[] readFileOnLine() throws IOException {
        String res=null;
        String strFileName = "Timetable.ics";
        //File fis = new File(strFileName);
        //BufferedReader sBuffera = new BufferedReader(new FileReader(fis));
        FileInputStream fin = openFileInput(strFileName);
        int length = fin.available();
        byte [] buffer = new byte[length];
        fin.read(buffer);
        res=new String(buffer, "UTF-8");
        //res = EncodingUtils.getString(buffer, "UTF-8");
        BufferedReader sBuffer=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(res.getBytes(Charset.forName("UTF-8"))), Charset.forName("UTF-8")));
        fin.close();

        String strLine =
                null;
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
                String reg="SUMMARY:(d.*) \\(";
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

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
    private class simpleGestureListener extends
            GestureDetector.SimpleOnGestureListener {

        /*****OnGestureListener的函数*****/

        final int FLING_MIN_DISTANCE_X = 100, FLING_MIN_VELOCITY_X = 200,FLING_MAX_DISTANCE_Y = 300;


        // 触发条件 ：
        // X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒

        // 参数解释：
        // e1：第1个ACTION_DOWN MotionEvent
        // e2：最后一个ACTION_MOVE MotionEvent
        // velocityX：X轴上的移动速度，像素/秒
        // velocityY：Y轴上的移动速度，像素/秒
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {


            if (e1.getRawX() - e2.getRawX() > FLING_MIN_DISTANCE_X
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY_X && Math.abs(e2.getRawY()-e1.getRawY())<FLING_MAX_DISTANCE_Y) {
                // Fling left
                //Log.i("MyGesture", "Fling left");
                day+=1;
                setView(day,month,year);
                //Toast.makeText(Main2Activity.this, "Fling Left", Toast.LENGTH_SHORT).show();
            } else if (e2.getRawX() - e1.getRawX() > FLING_MIN_DISTANCE_X
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY_X && Math.abs(e2.getRawY()-e1.getRawY())<FLING_MAX_DISTANCE_Y) {
                // Fling right
                ///Log.i("MyGesture", "Fling right");
                day-=1;
                setView(day,month,year);
                //Toast.makeText(Main2Activity.this, "Fling Right", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        //TODOAuto-generatedmethodstub
        mGestureDetector.onTouchEvent(ev); //让GestureDetector响应触碰事件
        super.dispatchTouchEvent(ev); //让Activity响应触碰事件
        return false;
    }
}
