package com.example.yuyan.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





public class MainActivity extends AppCompatActivity implements OnClickListener{
    private int isSuccessful=-1;
    private Handler handler=new Handler();
    Button button=null;
    final int codeWrite=30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean autoLog=true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextHolder.initial(this);
        /*if (autoLog)
            try {
                String res = null;
                String strFileName = "usrpsd.dat";
                FileInputStream fin = openFileInput(strFileName);
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                res = new String(buffer, "UTF-8");
                BufferedReader sBuffer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(res.getBytes(Charset.forName("UTF-8"))), Charset.forName("UTF-8")));
                fin.close();
            }
            catch(Exception){} catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        //StrictCode Mode
        //if (android.os.Build.VERSION.SDK_INT > 9) {
        //    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //    StrictMode.setThreadPolicy(policy);
        //}

        Button butt=findViewById(R.id.button);
        button=butt;
        button.setOnClickListener(this);


    }

    public Handler getHandler() {
        return handler;
    }

    //int isSuccessful(String username, String password) {
      //  if (username.equals("yyy") && password.equals("yyy"))
        //    return 1;
        //else return 0;
  //  }
    public void setIsSuccessful(int param){
        this.isSuccessful=param;
    }
    public void onClick(View v ){
        button.setEnabled(false);
        new Thread(new Runnable() {
            private int successful=1;
            private String adress1="https://cas.univ-valenciennes.fr/cas/login?service=https://portail.univ-valenciennes.fr/Login";
            private String adress2="https://vtmob.univ-valenciennes.fr/esup-vtclient-up4/stylesheets/desktop/welcome.xhtml";

            public Map sendPostLogin(String adress, String postParams)  throws Exception {
                URL url=new URL(adress);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                conn.setRequestProperty("User-Agent", "Mozilla/5.0+(Windows+NT+10.0;+Win64;+x64;+rv:59.0)+Gecko/20100101+Firefox/59.0");
                conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = conn.getResponseCode();
                Map m=new HashMap();
                String cookieValue= conn.getHeaderField("set-cookie");
                m.put("responseCode",responseCode);
                m.put("cookie",cookieValue);
                return m;
            }

            public int sendPostDownloading(String adress,String postParams,String cookie)  throws Exception {
                URL url=new URL(adress);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                conn.setRequestProperty("Cookie",cookie);
                conn.setRequestProperty("User-Agent", "Mozilla/5.0+(Windows+NT+10.0;+Win64;+x64;+rv:59.0)+Gecko/20100101+Firefox/59.0");
                conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();

                int responseCode = conn.getResponseCode();
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                String response = new String();
                while ((inputLine = in.readLine()) != null) {
                    response=response+"\n"+inputLine;
                }
                in.close();
                if (response.contains("Besoin d'aide")) throw(new IOException());
                writeFile("Timetable.ics",response);
                return responseCode;
            }

            public Map getFormParamsLogin(String adress, String username, String password) throws IOException {
                URL url=new URL(adress);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestProperty("User-Agent", "Mozilla/5.0+(Windows+NT+10.0;+Win64;+x64;+rv:59.0)+Gecko/20100101+Firefox/59.0");
                conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setDoInput(true);

                conn.setDoOutput(true);

                int responseCode = conn.getResponseCode();
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                String response = new String();
                while ((inputLine = in.readLine()) != null) {
                    response=response+"\n"+inputLine;
                }
                in.close();

                String reg = "name=\"lt\" value=\"(.*?)\" />";
                Pattern p = Pattern.compile(reg);
                Matcher m = p.matcher(response);
                String ltToken = "";
                if (m.find()) {
                    ltToken = m.group(1);
                }

                reg = "name=\"execution\" value=\"(.*?)\" />";
                p = Pattern.compile(reg);
                m = p.matcher(response);
                String executionToken = "";
                if (m.find()) {
                    executionToken = m.group(1);
                }
                //System.out.println(executionToken);

                reg = "name=\"_eventId\" value=\"(.*)\" />";
                p = Pattern.compile(reg);
                m = p.matcher(response);
                String eventIdToken = "";
                if (m.find()) {
                    eventIdToken = m.group(1);
                }
                //System.out.println(eventIdToken);

                reg = "name=\"ipAddress\" value=\"(.*)\"/>";
                p = Pattern.compile(reg);
                m = p.matcher(response);
                String ipAddressToken = "";
                if (m.find()) {
                    ipAddressToken = m.group(1);
                }
                //System.out.println(ipAddressToken);

                reg = "name=\"userAgent\" value=\"(.*)\" />";
                p = Pattern.compile(reg);
                m = p.matcher(response);
                String userAgentToken = "";
                if (m.find()) {
                    userAgentToken = m.group(1);
                }

                String params="_eventId="+eventIdToken
                        +"&execution="+executionToken
                        +"&ipAddress="+ipAddressToken
                        +"&lt="+ltToken
                        +"&userAgent="+userAgentToken
                        +"&username="+username
                        +"&password="+password
                        +"&submit=Connexion";
                //System.out.printf(params);
                Map m1=new HashMap();
                m1.put("params",params);
                m1.put("responseCode",responseCode);

                return m1;
            }
            public Map getFormParamsDownloading(String adress,String cookie) throws IOException {
                URL url=new URL(adress);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestProperty("Cookie",cookie);
                conn.setRequestProperty("User-Agent", "Mozilla/5.0+(Windows+NT+10.0;+Win64;+x64;+rv:59.0)+Gecko/20100101+Firefox/59.0");
                conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                conn.setRequestProperty("Connection", "Keep-Alive");// ά�ֳ�����

                conn.setDoInput(true);
                conn.setDoOutput(true);
                int responseCode = conn.getResponseCode();
                //System.out.println(responseCode);
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                String response = new String();
                while ((inputLine = in.readLine()) != null) {
                    response=response+"\n"+inputLine;
                }
                in.close();
                if (response.contains("Besoin d'aide")) throw(new IOException());
                String reg = "name=\"javax.faces.ViewState\" value=\"(.*?)\"";
                Pattern p = Pattern.compile(reg);
                Matcher m = p.matcher(response);
                String viewStateToken="";
                if(m.find()) {
                    viewStateToken = m.group(1);
                }
                String params="_noJavaScript=false&j_id12:_idcl=j_id12:j_id15&javax.faces.ViewState="
                        +viewStateToken+
                        "&org.apache.myfaces.trinidad.faces.FORM=j_id12";

                Map m1=new HashMap();
                m1.put("params",params);
                m1.put("responseCode",responseCode);
                return m1;
            }
            public void writeFile(String fileName,String writestr) throws IOException{

                try{
                    //File file=new File(fileName);
                    //if (!file.exists()){
                    //     file.createNewFile();
                    //}
                    //FileOutputStream fout =Context.();
                    FileOutputStream fout =openFileOutput(fileName,MODE_PRIVATE);

                    byte [] bytes = writestr.getBytes();

                    fout.write(bytes);

                    fout.close();
                }

                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void run() {

                Handler mainHandler=MainActivity.this.getHandler();

                String username,password;
                String sessionID=null;
                EditText editText = findViewById(R.id.editText);
                EditText editText2 = findViewById(R.id.editText2);
                username = editText.getText().toString();
                password = editText2.getText().toString();
                CookieHandler.setDefault(new CookieManager());
                try {
                    this.writeFile("usrpsd.dat",username+"\n"+password);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Map m=null;
                String postParams = null;
                String cookie=null;
                PasswordHelp passwordSaver= new PasswordHelp();
                passwordSaver.savePassword(ContextHolder.getContext(),username,password);
                try {
                    m = this.getFormParamsLogin(adress1,username,password);
                    postParams=(String)m.get("params");
                } catch (IOException e) {}
                try {
                    m=this.sendPostLogin(adress1,postParams);
                    cookie=(String)m.get("cookie");
                    if (cookie!=null) sessionID=cookie.substring(0,cookie.indexOf(";"));
                } catch (Exception e) {}

                try {
                    m=this.getFormParamsDownloading(adress2,cookie);
                    postParams=(String)m.get("params");
                }

                catch (IOException e)
                    {successful=1;}

                try {
                    if (this.sendPostDownloading(adress2,postParams,cookie)==200) this.successful=0;
                    else this.successful=1;

                } catch (Exception e) {successful=1;}
                MainActivity.this.setIsSuccessful(successful);
                mainHandler.post(new Runnable(){
                    @Override
                    public void run() {
                        button.setEnabled(true);
                        newPage();
                    }
                });



                /*MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.setIsSuccessful(successful);

                    }
                });*/
            }

        }).start();




        //else if (loginRight == 2) {
          //  builder.setTitle("error connection");
          //  builder.setMessage("Please check your internet connection.");
          //  builder.setPositiveButton("OK", null);
          //  builder.show();
        //}

    }
    public void getPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},codeWrite ); }
    }

    public void newPage(){
        AlertDialog.Builder builder = new Builder(this);
        int loginRight=this.isSuccessful;
        if (loginRight==0) {
            Intent intent= new Intent(this, Main2Activity.class);
            startActivity(intent);
        }

        if (loginRight == 1) {
            builder.setTitle("error password or error connection");
            builder.setMessage("Please check your password or connection again.");
            builder.setPositiveButton("OK", null);
            builder.show();
        }

    }

}
