package com.example.administrator.mymessager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.mymessager.MessageAPI.ConnectionDB;
import com.example.administrator.mymessager.MessageAPI.Validate;

import java.sql.Connection;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    EditText SendersName;
    Button ValidateButton;
    Connection con=null;
    Validate val;
    Statement st=null;
    ConnectionDB conDB;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        conDB=new ConnectionDB();
        con=conDB.getConnection();
        val=new Validate();

        SendersName=(EditText) findViewById(R.id.sendersName);

        ValidateButton=(Button) findViewById(R.id.validate_btn);

        if(!getSnderName().equals("logout")) {
            User.setUserName(getSnderName());
            Intent myIntent = new Intent(MainActivity.this,ContactList.class);
            startActivity(myIntent);
        }else {
            ValidateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //validation code
                    if (val.validateUser(SendersName.getText().toString()) == "True") {
                        User.setUserName(SendersName.getText().toString());
                        setSnderName(SendersName.getText().toString());

                        // Start NewActivity.class
                        Intent myIntent = new Intent(MainActivity.this, ContactList.class);
                        startActivity(myIntent);

                    } else {

                        //MessageBox("New User","You are New User Thank You Dor Giving MyMessager A Chance It Contiously Updating");
                        Toast.makeText(getApplicationContext(), "Awesome  New User", Toast.LENGTH_LONG).show();
                        try {
                            User.setUserName(SendersName.getText().toString());
                            setSnderName(SendersName.getText().toString());
                            st = con.createStatement();
                            st.executeUpdate("insert into account Values('"+SendersName.getText().toString()+"','0','I')");

                        }catch (Exception e)
                        {

                        }
                        // Start NewActivity.class
                        Intent myIntent = new Intent(MainActivity.this, ContactList.class);
                        startActivity(myIntent);
                    }
                }
            });
        }
    }

    public void setSnderName(String Sender)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("SenderName",Sender);
        editor.commit();
    }

    public String getSnderName() {
        String Sender=sharedpreferences.getString("SenderName", "logout");
        return  Sender;
    }

}
