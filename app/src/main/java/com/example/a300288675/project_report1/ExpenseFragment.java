package com.example.a300288675.project_report1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.getbase.floatingactionbutton.FloatingActionButton;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ExpenseFragment extends Fragment {

    String itemDesc, expCategory, loginId;
    double expAmt;
    NumberFormat nf = NumberFormat.getCurrencyInstance();
    DatabaseHelper dbh;
    TextView txtTotalExpensesAmt,txtAvailBalance,txtRemainingBalanceAmt,txtClothingAmt,txtEntertainmentAmt,txtFoodAmt;
    TextView txtGasAmt,txtGroceryAmt,txtInsuranceAmt,txtTransportationAmt,txtUtilitiesAmt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_expense, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbh = new DatabaseHelper(getActivity());

        txtTotalExpensesAmt = getView().findViewById(R.id.txtTotalExpenseAmt);
        txtAvailBalance = getView().findViewById(R.id.txtTotalAvailBalanceAmt);
        txtRemainingBalanceAmt = getView().findViewById(R.id.txtRemainingBalanceAmt);
        txtClothingAmt = getView().findViewById(R.id.txtClothingAmt);
        txtEntertainmentAmt = getView().findViewById(R.id.txtEntertainmentAmt);
        txtFoodAmt = getView().findViewById(R.id.txtFoodAmt);
        txtGasAmt = getView().findViewById(R.id.txtGasAmt);
        txtGroceryAmt = getView().findViewById(R.id.txtGroceryAmt);
        txtInsuranceAmt = getView().findViewById(R.id.txtInsuranceAmt);
        txtTransportationAmt = getView().findViewById(R.id.txtTranspoAmt);
        txtUtilitiesAmt = getView().findViewById(R.id.txtUtilAmt);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String formattedDate = dateFormat.format(date);

        loginId = getActivity().getIntent().getExtras().get("loginid").toString();

        populateDataInFields(loginId,formattedDate);

        FloatingActionButton fab1 = getView().findViewById(R.id.fab_action1);

        //OnClickListener for the Homescreen Floating Action Button
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder expBuilder = new AlertDialog.Builder(getActivity());
                //Launches the Add Expense dialog box
                View expView = getLayoutInflater().inflate(R.layout.addexpense_dialog, null);
                expBuilder.setTitle("Add Expense");
                expBuilder.setView(v);

                final EditText txtExpDescription = (EditText)expView.findViewById(R.id.txtExpDescription);
                final EditText txtExpAmt = (EditText)expView.findViewById(R.id.txtExpAmt);
                final Spinner expCategoriesSpinner = (Spinner)expView.findViewById(R.id.expCategoriesSpinner);
                ArrayAdapter<String> expCategoriesArray = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.expCategoriesArray));
                expCategoriesArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                expCategoriesSpinner.setAdapter(expCategoriesArray);

                //Records the expense in the database after clicking the Save button.
                expBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        itemDesc = txtExpDescription.getText().toString();
                        expAmt = Double.parseDouble(txtExpAmt.getText().toString());
                        expCategory = expCategoriesSpinner.getSelectedItem().toString();

                        //ADDING EXPENSE

                        if(dbh.addTransaction(loginId,dbh.getExpenseCategoryID(expCategory), formattedDate, expAmt, itemDesc)){
                            Toast.makeText(getActivity(),"Expense added.",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(),"Expense not added. Please retry.",Toast.LENGTH_SHORT).show();
                        }
                        populateDataInFields(loginId,formattedDate);
                        dialog.dismiss();
                    }
                });

                //Cancels the Add Expense dialog screen.
                expBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                expBuilder.setView(expView);
                AlertDialog dialog = expBuilder.create();
                dialog.show();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void populateDataInFields(String loginId, String formattedDate){
        txtTotalExpensesAmt.setText(String.valueOf(nf.format(dbh.getSumOfTransactionsForDay(loginId,formattedDate))));
        txtAvailBalance.setText(String.valueOf(nf.format(dbh.getDailyAvailableBalance(loginId, formattedDate))));
        txtRemainingBalanceAmt.setText(String.valueOf(nf.format(dbh.getDailyAvailableBalance(loginId, formattedDate) - dbh.getSumOfTransactionsForDay(loginId,formattedDate))));
        txtClothingAmt.setText(String.valueOf(nf.format(dbh.getCategorywiseTotalForDay(loginId,formattedDate, dbh.getExpenseCategoryID("Clothing")))));
        txtEntertainmentAmt.setText(String.valueOf(nf.format(dbh.getCategorywiseTotalForDay(loginId,formattedDate, dbh.getExpenseCategoryID("Entertainment")))));
        txtFoodAmt.setText(String.valueOf(nf.format(dbh.getCategorywiseTotalForDay(loginId,formattedDate, dbh.getExpenseCategoryID("Food")))));
        txtGasAmt.setText(String.valueOf(nf.format(dbh.getCategorywiseTotalForDay(loginId,formattedDate, dbh.getExpenseCategoryID("Gas")))));
        txtGroceryAmt.setText(String.valueOf(nf.format(dbh.getCategorywiseTotalForDay(loginId,formattedDate, dbh.getExpenseCategoryID("Grocery")))));
        txtInsuranceAmt.setText(String.valueOf(nf.format(dbh.getCategorywiseTotalForDay(loginId,formattedDate, dbh.getExpenseCategoryID("Insurance")))));
        txtTransportationAmt.setText(String.valueOf(nf.format(dbh.getCategorywiseTotalForDay(loginId,formattedDate, dbh.getExpenseCategoryID("Transportation")))));
        txtUtilitiesAmt.setText(String.valueOf(nf.format(dbh.getCategorywiseTotalForDay(loginId,formattedDate, dbh.getExpenseCategoryID("Utilities")))));
    }
}
