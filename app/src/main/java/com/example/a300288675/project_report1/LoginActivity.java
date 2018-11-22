package com.example.a300288675.project_report1;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity{

    Button buttonLogin, buttonSignup;
    EditText etUserId, etPassword;
    String loginid="", password="";
    TextView tvWrongEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvWrongEntry = (TextView)findViewById(R.id.tvWrongCredentials);
        etUserId = (EditText)findViewById(R.id.etUser);
        etPassword = (EditText)findViewById(R.id.etPassword);
        buttonLogin = (Button)findViewById(R.id.btnLogin);
        buttonSignup = (Button)findViewById(R.id.btnSignUp);

        tvWrongEntry.setVisibility(View.INVISIBLE);

        final DatabaseHelper db = new DatabaseHelper(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginid = etUserId.getText().toString();
                password = etPassword.getText().toString();

                if (!loginid.isEmpty() && !password.isEmpty()){
                    if (db.validateUser(loginid,password)){
                        tvWrongEntry.setVisibility(View.INVISIBLE);
                        String name = db.getUserFullName(loginid);
                        String email = db.getEmail(loginid);

                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        i.putExtra("loginid",loginid);
                        i.putExtra("name",name);
                        i.putExtra("email",email);

                        Toast.makeText(LoginActivity.this,"Login successful.",Toast.LENGTH_LONG).show();
                        startActivity(i);
                    } else {
                        tvWrongEntry.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SignUp.editMode = false;
            startActivity(new Intent(LoginActivity.this, SignUp.class));
            }
        });


    }
}
