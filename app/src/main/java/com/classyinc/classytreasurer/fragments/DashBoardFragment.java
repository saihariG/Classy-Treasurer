package com.classyinc.classytreasurer.fragments;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.classyinc.classytreasurer.Model.Data;
import com.classyinc.classytreasurer.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {

    //fab
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    //floating button textview
    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    //boolean
    private  boolean isopen = false;

    //animation
    private Animation FadOpen , FadClose;

    //dashboard income and expense result
    private TextView totalIncomeResult;
    private TextView totalExpenseResult;


    //Firebase...

    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;

    //recycler view...
    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;

    private boolean adLoaded = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_dash_board, container, false);


        MobileAds.initialize(getActivity(), "ca-app-pub-4710955483788759~6292166403");
        final AdView incometxtBannerAD = myview.findViewById(R.id.income_banner_id);
        final AdView expensetxtBannerAd = myview.findViewById(R.id.expense_banner_id);

        incometxtBannerAD.loadAd(new AdRequest.Builder().build());
        expensetxtBannerAd.loadAd(new AdRequest.Builder().build());

        incometxtBannerAD.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                adLoaded = true;
            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                adLoaded = false;
              /*  if (errorCode == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                    Toast.makeText(getActivity(), errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_INVALID_REQUEST) {
                    Toast.makeText(getActivity(), errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_NETWORK_ERROR){
                    Toast.makeText(getActivity(),errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_NO_FILL){
                    Toast.makeText(getActivity(),errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_APP_ID_MISSING){
                    Toast.makeText(getActivity(),errorCode, Toast.LENGTH_SHORT).show();
                } */

            }

        });

        expensetxtBannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded(){
                adLoaded = true;
            }
            public void onAdFailedToLoad(int errorCode){
                adLoaded = false;
            /*    if (errorCode == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                    Toast.makeText(getActivity(), errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_INVALID_REQUEST) {
                    Toast.makeText(getActivity(), errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_NETWORK_ERROR){
                    Toast.makeText(getActivity(), errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_NO_FILL){
                    Toast.makeText(getActivity(), errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_APP_ID_MISSING){
                    Toast.makeText(getActivity(), errorCode, Toast.LENGTH_SHORT).show();
                } */
            }

        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        String uid = Objects.requireNonNull(mUser).getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        mIncomeDatabase.keepSynced(true);
        mExpenseDatabase.keepSynced(true);

        //connect fab to layout
        FloatingActionButton fab_main_btn = myview.findViewById(R.id.fb_main_plus);
        fab_income_btn = myview.findViewById(R.id.income_ft_btn);
        fab_expense_btn = myview.findViewById(R.id.expense_ft_btn);

        //connect floating text
        fab_income_txt = myview.findViewById(R.id.income_ft_text);
        fab_expense_txt = myview.findViewById(R.id.expense_ft_text);

        //total income and expense result set...
        totalIncomeResult = myview.findViewById(R.id.income_set_result);
        totalExpenseResult = myview.findViewById(R.id.expense_set_result);

        //recycler...
        mRecyclerIncome = myview.findViewById(R.id.recycler_dashboard_income_id);
        mRecyclerExpense = myview.findViewById(R.id.recycler_dashboard_expense_id);

        //animation connect

        FadOpen = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadClose = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addData();

                if(isopen) {
                    fab_income_btn.startAnimation(FadClose);
                    fab_expense_btn.startAnimation(FadClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);

                    fab_income_txt.startAnimation(FadClose);
                    fab_expense_txt.startAnimation(FadClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isopen = false;

                }
                else {

                    fab_income_btn.startAnimation(FadOpen);
                    fab_expense_btn.startAnimation(FadOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadOpen);
                    fab_expense_txt.startAnimation(FadOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);
                    isopen=true;
                }
            }
        });

        //calculate total income...
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int totalsum = 0;

                for(DataSnapshot mysnap : dataSnapshot.getChildren()) {

                    Data data = mysnap.getValue(Data.class);


                    totalsum += Objects.requireNonNull(data).getAmount();

                    String stResult = String.valueOf(totalsum);

                    totalIncomeResult.setText("$"+stResult+".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 int totalsum = 0;

                for(DataSnapshot mysnap : dataSnapshot.getChildren()) {

                    Data data = mysnap.getValue(Data.class);

                    totalsum += Objects.requireNonNull(data).getAmount();

                    String stResult = String.valueOf(totalsum);

                    totalExpenseResult.setText("$"+stResult+".00");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Recycler...
        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);

        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerExpense.setReverseLayout(true);
        layoutManagerExpense.setStackFromEnd(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);


        return myview;

    }



    //floating btn animation
    private void ftAnimation() {

        if(isopen) {
            fab_income_btn.startAnimation(FadClose);
            fab_expense_btn.startAnimation(FadClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(FadClose);
            fab_expense_txt.startAnimation(FadClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isopen = false;
        }
        else {

            fab_income_btn.startAnimation(FadOpen);
            fab_expense_btn.startAnimation(FadOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(FadOpen);
            fab_expense_txt.startAnimation(FadOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);
            isopen=true;
        }

    }


    private void addData() {

        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                  incomeDataInsert();
            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                  expenseDataInsert();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void incomeDataInsert() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview  = inflater.inflate(R.layout.custom_layout_for_insertdata,null);

        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();
        //dialog.setCancelable(false);

        final EditText edtAmount = myview.findViewById(R.id.edt_amount_id);
        final EditText edttype = myview.findViewById(R.id.edt_type_id);
        final EditText edtNote = myview.findViewById(R.id.edt_note_id);

        Button btnSave = myview.findViewById(R.id.btn_save_id);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = edttype.getText().toString().trim();
                String amount = edtAmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if(TextUtils.isEmpty(type)) {
                    edttype.setError("Field Empty");
                    return;
                }

                if(TextUtils.isEmpty(amount)) {
                    edtAmount.setError("Field Empty");
                    return;
                }

                int ouramountint = Integer.parseInt(amount);

                if(TextUtils.isEmpty(note)) {
                    edtNote.setError("Field Empty");
                    //return;
                }

                String id = mIncomeDatabase.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());

                String time=DateFormat.getTimeInstance().format(new Date());

                Data data = new Data(ouramountint,type,note,id,mDate,time);

                mIncomeDatabase.child(Objects.requireNonNull(id)).setValue(data);
                Toast.makeText(getActivity(), "Income Data added", Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();

            }

        });

        dialog.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expenseDataInsert() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview  = inflater.inflate(R.layout.custom_layout_for_insert_expense_data,null);

        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();
        //dialog.setCancelable(false);

        final EditText amount = myview.findViewById(R.id.edt_amount_insert_expense_id);
        final EditText type = myview.findViewById(R.id.edt_type_insert_expense_id);
        final EditText note = myview.findViewById(R.id.edt_note_insert_expense_id);

        Button btnSave = myview.findViewById(R.id.btn_save_insert_expense_id);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tmtype = type.getText().toString().trim();
                String tmamount = amount.getText().toString().trim();
                String tmnote = note.getText().toString().trim();

                if(TextUtils.isEmpty(tmtype)) {
                    type.setError("Field Empty");
                    return;
                }

                if(TextUtils.isEmpty(tmamount)) {
                    amount.setError("Field Empty");
                    return;
                }

                int inamount = Integer.parseInt(tmamount);

                if(TextUtils.isEmpty(tmnote)) {
                    note.setError("Field Empty");
                   // return;
                }

                String id = mExpenseDatabase.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());


                String time = DateFormat.getTimeInstance().format(new Date());

                //String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(mDate);

                Data data = new Data(inamount,tmtype,tmnote,id,mDate,time);

                mExpenseDatabase.child(Objects.requireNonNull(id)).setValue(data);
                Toast.makeText(getActivity(), "Expense Data added", Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,IncomeViewHolder>incomeAdapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>
                (
                      Data.class,
                      R.layout.dashboard_income,
                      DashBoardFragment.IncomeViewHolder.class,
                      mIncomeDatabase
                ) {
            @Override
            protected void populateViewHolder(IncomeViewHolder incomeViewHolder, Data model, int position) {

                incomeViewHolder.setIncomeType(model.getType());
                incomeViewHolder.setIncomeAmount(model.getAmount());
                incomeViewHolder.setIncomeDate(model.getDate());
            }
        };

        mRecyclerIncome.setAdapter(incomeAdapter);

        FirebaseRecyclerAdapter<Data,ExpenseViewHolder> expenseAdapter = new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>
                (
                  Data.class,
                  R.layout.dashboard_expense,
                  DashBoardFragment.ExpenseViewHolder.class,
                  mExpenseDatabase
                ) {
            @Override
            protected void populateViewHolder(ExpenseViewHolder expenseViewHolder, Data model, int position) {
                expenseViewHolder.setExpenseType(model.getType());
                expenseViewHolder.setExpenseAmount(model.getAmount());
                expenseViewHolder.setExpenseDate(model.getDate());
            }
        };

        mRecyclerExpense.setAdapter(expenseAdapter);

    }

    //For income data
    public  static  class IncomeViewHolder extends RecyclerView.ViewHolder {

        View mIncomeView;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            mIncomeView = itemView;
        }

        private void setIncomeType(String type) {
            TextView mType = mIncomeView.findViewById(R.id.type_income_ds);
            mType.setText(type);
        }

        private void setIncomeAmount(int amount) {
            TextView mAmount = mIncomeView.findViewById(R.id.amount_income_ds);
            String stramount = String.valueOf(amount);
            mAmount.setText("$"+stramount);
        }

        private void setIncomeDate(String date) {
            TextView mDate = mIncomeView.findViewById(R.id.date_income_ds);
            mDate.setText(date);
        }

    }


    public  static  class ExpenseViewHolder extends RecyclerView.ViewHolder {

        View mExpenseView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            mExpenseView = itemView;
        }

        private void setExpenseType(String type) {
            TextView mType = mExpenseView.findViewById(R.id.type_expense_ds);
            mType.setText(type);
        }

        private void setExpenseAmount(int amount) {
            TextView mAmount = mExpenseView.findViewById(R.id.amount_expense_ds);
            String stramount = String.valueOf(amount);
            mAmount.setText("$"+stramount);
        }

        private void setExpenseDate(String date) {
            TextView mDate = mExpenseView.findViewById(R.id.date_expense_ds);
            mDate.setText(date);
        }

    }

}
