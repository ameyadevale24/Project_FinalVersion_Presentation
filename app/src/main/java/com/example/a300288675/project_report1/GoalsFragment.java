package com.example.a300288675.project_report1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GoalsFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    NumberFormat nf = NumberFormat.getCurrencyInstance();
    TextView tvGoalsName1, tvGoalsName2, tvGoalsName3, tvGoalsName4, tvGoalsAmount1, tvGoalsAmount2, tvGoalsAmount3, tvGoalsAmount4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final DatabaseHelper db = new DatabaseHelper(getContext());

        final String loginId = getActivity().getIntent().getExtras().get("loginid").toString();

        updateData(loginId);

        FloatingActionButton fab2 = getView().findViewById(R.id.fab_action2);

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder goalBuilder = new AlertDialog.Builder(getActivity());
                final View goalsView = getLayoutInflater().inflate(R.layout.addgoal_dialog, null);
                goalBuilder.setTitle("Add Goal");
                goalBuilder.setView(v);

                //ADDING SAVINGS
                goalBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText etGoalName = (EditText)goalsView.findViewById(R.id.txtGoalName);
                        String goalName = etGoalName.getText().toString();
                        EditText etGoalAmt = (EditText)goalsView.findViewById(R.id.txtGoalAmt);
                        double goalAmt = Double.parseDouble(etGoalAmt.getText().toString());

                        if(db.addEventwiseSaving(loginId, goalName, goalAmt)){
                            Toast.makeText(getActivity(),"Goal added.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(),"Goal not added. Please retry.",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        updateData(loginId);
                    }
                });

                goalBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                goalBuilder.setView(goalsView);
                AlertDialog dialog = goalBuilder.create();
                dialog.show();
            }
        });
    }

    public void updateData(String loginId){
        tvGoalsName1 = getView().findViewById(R.id.tvGoalName1);
        tvGoalsName2 = getView().findViewById(R.id.tvGoalName2);
        tvGoalsName3 = getView().findViewById(R.id.tvGoalName3);
        tvGoalsName4 = getView().findViewById(R.id.tvGoalName4);
        tvGoalsAmount1 = getView().findViewById(R.id.tvGoalAmount1);
        tvGoalsAmount2 = getView().findViewById(R.id.tvGoalAmount2);
        tvGoalsAmount3 = getView().findViewById(R.id.tvGoalAmount3);
        tvGoalsAmount4 = getView().findViewById(R.id.tvGoalAmount4);

        DatabaseHelper db = new DatabaseHelper(getContext());

        Cursor goals = db.getEventsAndAmount(loginId);
        String goalsText="";
        if (goals!=null){
            if(!goals.isAfterLast()){
                tvGoalsName1.setText(goals.getString(0));
                tvGoalsAmount1.setText(String.valueOf(goals.getDouble(1)));
                goals.moveToNext();
            }
            if(!goals.isAfterLast()){
                tvGoalsName2.setText(goals.getString(0));
                tvGoalsAmount2.setText(String.valueOf(goals.getDouble(1)));
                goals.moveToNext();
            }
            if(!goals.isAfterLast()){
                tvGoalsName3.setText(goals.getString(0));
                tvGoalsAmount3.setText(String.valueOf(goals.getDouble(1)));
                goals.moveToNext();
            }
            if(!goals.isAfterLast()){
                tvGoalsName4.setText(goals.getString(0));
                tvGoalsAmount4.setText(String.valueOf(goals.getDouble(1)));
            }

        }
    }
}
