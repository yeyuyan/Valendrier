package com.example.yuyan.myapplication;

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
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements OnClickListener{

    private String adress1="https://cas.univ-valenciennes.fr/cas/login?service=https://portail.univ-valenciennes.fr/Login";
    private String adress2="https://vtmob.univ-valenciennes.fr/esup-vtclient-up4/stylesheets/desktop/welcome.xhtml";
    private int isSuccessful=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button=findViewById(R.id.button);
        button.setOnClickListener(this);

    }

    public int sendPost(String adress,String postParams)  throws Exception {
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
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + postParams);
        //System.out.println("Response Code : " + responseCode);
        //setCookies(conn.getHeaderFields().get("Set-Cookie"));
        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        String response = new String();
        while ((inputLine = in.readLine()) != null) {
            response=response+"\r\n"+inputLine;
        }
        in.close();//�ر�������

        //System.out.println(response);

        //File f = new File("D:/KeChengBiao.ics");
        //FileWriter writer=new FileWriter(f);
        //writer.write(response);
        //writer.close();
        return 0;
    }

    public Map getFormParams(String adress, String username, String password) throws IOException {
        URL url=new URL(adress);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0+(Windows+NT+10.0;+Win64;+x64;+rv:59.0)+Gecko/20100101+Firefox/59.0");
        conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setDoInput(true);

        conn.setDoOutput(true);


        int responseCode = 200;//conn.getResponseCode();
        //System.out.println(responseCode);

        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        String response = new String();
        while ((inputLine = in.readLine()) != null) {
            response=response+"\n"+inputLine;
        }
        in.close();

        //System.out.println(response);

        //File f = new File("D:/helloWeb.html");
        //FileWriter writer=new FileWriter(f);
        //writer.write(response);
        //writer.close();


        String reg = "name=\"lt\" value=\"(.*?)\" />";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(response);
        String ltToken="";
        if(m.find()) {
            ltToken = m.group(1);
        }

        reg = "name=\"execution\" value=\"(.*?)\" />";
        p = Pattern.compile(reg);
        m = p.matcher(response);
        String executionToken="";
        if(m.find()) {
            executionToken = m.group(1);
        }
        //System.out.println(executionToken);

        reg = "name=\"_eventId\" value=\"(.*)\" />";
        p = Pattern.compile(reg);
        m = p.matcher(response);
        String eventIdToken="";
        if(m.find()) {
            eventIdToken = m.group(1);
        }
        //System.out.println(eventIdToken);

        reg = "name=\"ipAddress\" value=\"(.*)\"/>";
        p = Pattern.compile(reg);
        m = p.matcher(response);
        String ipAddressToken="";
        if(m.find()) {
            ipAddressToken= m.group(1);
        }
        //System.out.println(ipAddressToken);

        reg = "name=\"userAgent\" value=\"(.*)\" />";
        p = Pattern.compile(reg);
        m = p.matcher(response);
        String userAgentToken="";
        if(m.find()) {
            userAgentToken= m.group(1);
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
    public Map getFormParams2(String adress) throws IOException {
        URL url=new URL(adress);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        conn.setRequestProperty("User-Agent", "Mozilla/5.0+(Windows+NT+10.0;+Win64;+x64;+rv:59.0)+Gecko/20100101+Firefox/59.0");
        conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        conn.setRequestProperty("Connection", "Keep-Alive");// ά�ֳ�����

        conn.setDoInput(true);
        conn.setDoOutput(true);
        int responseCode = conn.getResponseCode();
        System.out.println(responseCode);
        BufferedReader in =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        String response = new String();
        while ((inputLine = in.readLine()) != null) {
            response=response+"\n"+inputLine;
        }
        in.close();

        String reg = "name=\"javax.faces.ViewState\" value=\"(.*?)\"";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(response);
        String viewStateToken="";
        if(m.find()) {
            viewStateToken = m.group(1);
        }
        System.out.println(viewStateToken);
        String params="_noJavaScript=false&j_id12:_idcl=j_id12:j_id15&javax.faces.ViewState="
                +viewStateToken+
                "&org.apache.myfaces.trinidad.faces.FORM=j_id12";

        Map m1=new HashMap();
        m1.put("params",params);
        m1.put("responseCode",responseCode);
        return m1;
    }

    //int isSuccessful(String username, String password) {
      //  if (username.equals("yyy") && password.equals("yyy"))
        //    return 1;
        //else return 0;
  //  }

    public void onClick(View v ){
        String username,password;

        AlertDialog.Builder builder = new Builder(this);
        EditText editText = findViewById(R.id.editText);
        EditText editText2 = findViewById(R.id.editText2);
        username = editText.getText().toString();
        password = editText2.getText().toString();

        int loginRight;
        CookieHandler.setDefault(new CookieManager());
        Map m;
        String postParams = null;
        try {
            m = this.getFormParams(adress1,username,password);
            if (m.get("responseCode").equals(200))  this.isSuccessful=0;
            else this.isSuccessful=1;
        } catch (IOException e) {;}
        try {
            this.sendPost(adress1,postParams);
        } catch (Exception e) {;}
        //try {}
        //catch (Exception ){;}

        try {
            m=this.getFormParams2(adress2);
        } catch (IOException e) {;}
        try {
            this.sendPost(adress2,postParams);
        } catch (Exception e) {;}

        loginRight=this.isSuccessful;

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

        //else if (loginRight == 2) {
          //  builder.setTitle("error connection");
          //  builder.setMessage("Please check your internet connection.");
          //  builder.setPositiveButton("OK", null);
          //  builder.show();
        //}

    }

}
