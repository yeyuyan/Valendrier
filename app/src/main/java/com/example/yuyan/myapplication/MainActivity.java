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



public class MainActivity extends AppCompatActivity implements OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button);
        button.setOnClickListener(this);
    }


    int isSuccessful(String username, String password) {
        if (username.equals("yyy") && password.equals("yyy"))
            return 1;
        else return 0;
    }

    public void onClick(View v ){
        String username,password;

        AlertDialog.Builder builder = new Builder(this);
        EditText editText = findViewById(R.id.editText);
        EditText editText2 = findViewById(R.id.editText2);
        username = editText.getText().toString();
        password = editText2.getText().toString();

        int loginRight;
        loginRight=isSuccessful(username,password);

        if (loginRight==1) {
            Intent intent= new Intent(this, Main2Activity.class);
            startActivity(intent);
        }

        if (loginRight == 0) {
            builder.setTitle("error password");
            builder.setMessage("Yours password is wrong, please enter again.");
            builder.setPositiveButton("OK", null);
            builder.show();
        }

        else if (loginRight == 2) {
            builder.setTitle("error connection");
            builder.setMessage("Please check your internet connection.");
            builder.setPositiveButton("OK", null);
            builder.show();
        }

    }

}
