package com.example.a300288675.project_report1;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SignUp extends AppCompatActivity {

    TextView tvInvalidLogin, tvInvalidEmail, tvInvalidPassword, tvInvalidCnfrmPassword, tvMonthlyIncomeNote;
    EditText eLogin, eFname, eLname, eEmail, ePhone, ePassword, eCnfrmPassword, eMonthlyIncome;
    Button btnSave;
    boolean hasErrors;
    static boolean editMode = false;
    String login="", fname="", lname="", phone="", email="", pass="";
    double monthlyIncome=0.00;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final DatabaseHelper dbh = new DatabaseHelper(this);

        tvInvalidLogin = (TextView)findViewById(R.id.tvLoginExists);
        tvInvalidEmail = (TextView)findViewById(R.id.tvInvalidEmail);
        tvInvalidPassword = (TextView)findViewById(R.id.tvInvalidPassword);
        tvInvalidCnfrmPassword = (TextView)findViewById(R.id.tvPasswordsMismatch);
        tvMonthlyIncomeNote = (TextView)findViewById(R.id.tvMonthlyIncomeNote);

        tvMonthlyIncomeNote.setVisibility(View.INVISIBLE);

        eLogin = (EditText)findViewById(R.id.etLogin);
        eFname = (EditText)findViewById(R.id.etFName);
        eLname = (EditText)findViewById(R.id.etLName);
        eEmail = (EditText)findViewById(R.id.etEmail);
        ePhone = (EditText)findViewById(R.id.etPhone);
        ePassword = (EditText)findViewById(R.id.etPassword);
        eCnfrmPassword = (EditText)findViewById(R.id.etCnfrmPassword);
        eMonthlyIncome = (EditText)findViewById(R.id.etMonthlyIncome);

        if (editMode){
            eLogin.setText(getIntent().getExtras().get("loginID").toString());
            eFname.setText(getIntent().getExtras().get("fname").toString());
            eLname.setText(getIntent().getExtras().get("lname").toString());
            eEmail.setText(getIntent().getExtras().get("email").toString());
            ePhone.setText(getIntent().getExtras().get("phone").toString());
            ePassword.setText(getIntent().getExtras().get("pass").toString());
            eCnfrmPassword.setText(getIntent().getExtras().get("pass").toString());
            eMonthlyIncome.setText(getIntent().getExtras().get("monthlyincome").toString());

            eLogin.setFocusable(false);
            tvMonthlyIncomeNote.setVisibility(View.VISIBLE);
        }

        eLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(dbh.checkUserExists(eLogin.getText().toString())){
                        tvInvalidLogin.setVisibility(View.VISIBLE);
                        hasErrors = true;
                    } else {
                        tvInvalidLogin.setVisibility(View.INVISIBLE);
                        hasErrors = false;
                    }
                }
            }
        });

        eEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!eEmail.getText().toString().contains("@") || !eEmail.getText().toString().contains(".")){
                        tvInvalidEmail.setVisibility(View.VISIBLE);
                        hasErrors = true;
                    } else {
                        tvInvalidEmail.setVisibility(View.INVISIBLE);
                        hasErrors = false;
                    }
                }
            }
        });

        ePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(ePassword.length()<8){
                        tvInvalidPassword.setVisibility(View.VISIBLE);
                        hasErrors = true;
                    } else {
                        tvInvalidPassword.setVisibility(View.INVISIBLE);
                        hasErrors = false;
                    }
                }
            }
        });

        eCnfrmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!eCnfrmPassword.getText().toString().equals(ePassword.getText().toString())){
                        tvInvalidCnfrmPassword.setVisibility(View.VISIBLE);
                        hasErrors = true;
                    } else {
                        tvInvalidCnfrmPassword.setVisibility(View.INVISIBLE);
                        hasErrors = false;
                    }
                }
            }
        });

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                login = eLogin.getText().toString();
                fname = eFname.getText().toString();
                lname = eLname.getText().toString();
                email = eEmail.getText().toString();
                phone = ePhone.getText().toString();
                pass = ePassword.getText().toString();
                monthlyIncome = Double.valueOf(eMonthlyIncome.getText().toString());

                Date date = new Date();
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                final int currentMonth = localDate.getMonthValue();
                final int currentYear = localDate.getYear();

                if(!login.trim().isEmpty() && !pass.trim().isEmpty() && !email.trim().isEmpty() && monthlyIncome>0){
                    if(!hasErrors){
                        if (editMode){
                            //in edit mode. so the monthly income will be applied for the next month
                            if (dbh.updateUser(login, fname, lname, email, phone, pass) && dbh.addMonthlyIncomeTracking(login, currentMonth+1, currentYear, monthlyIncome)) {
                                Toast.makeText(SignUp.this, "Profile updated successfully.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignUp.this, LoginActivity.class));
                            }
                        } else {
                            //first time saving mode
                            if (dbh.addUser(login, fname, lname, email, phone, pass) && dbh.addMonthlyIncomeTracking(login, currentMonth, currentYear, monthlyIncome)) {
                                Toast.makeText(SignUp.this, "Profile created successfully.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignUp.this, LoginActivity.class));
                            }
                        }
                    } else {
                        Toast.makeText(SignUp.this,"Data not saved. Recheck the data you have entered.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SignUp.this,"'LoginId', 'Email' and 'Password' are mandatory fields.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
