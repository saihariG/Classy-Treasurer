package com.classyinc.classytreasurer.fragments;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.classyinc.classytreasurer.Model.Data;

import com.classyinc.classytreasurer.R;
import com.classyinc.classytreasurer.adapters.SearchexpenseAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {

    private DatabaseReference mExpenseDatabase;

    //recyclerview...
    private RecyclerView recyclerView;

    //Text view
    private TextView expenseTotalSum;

    //edt data item
    private EditText edtAmount;
    private EditText edtType;
    private EditText edtNote;

    //Data item Value
    private String type;
    private String note;
    private int amount;

    private String post_key;


    FirebaseUser firebaseUser;


    ArrayList<String> mamountList;
    ArrayList<String> mdateList;
    ArrayList<String> mnoteList;
    ArrayList<String> mtitleList;
    ArrayList<String> mtimeList;

    private SearchexpenseAdapter searchexpenseAdapter;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myview = inflater.inflate(R.layout.fragment_expense, container, false);

        EditText search_edt_txt = myview.findViewById(R.id.search_expense_edt_txt);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference("ExpenseData");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mamountList = new ArrayList<>();
        mtimeList = new ArrayList<>();
        mdateList = new ArrayList<>();
        mnoteList = new ArrayList<>();
        mtitleList = new ArrayList<>();

        search_edt_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()) {
                    setAdapter(s.toString());
                }
                /*else {
                    mdateList.clear();

                    mtimeList.clear();
                    mtitleList.clear();
                    mnoteList.clear();
                    mamountList.clear();
                    recyclerView.removeAllViews();
                }*/
            }
        });


        MobileAds.initialize(getActivity());
        AdLoader adLoader =new AdLoader.Builder(Objects.requireNonNull(getActivity()),"ca-app-pub-4710955483788759/9500767760")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        UnifiedNativeAdView unifiedNativeAdView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.native_layout,null);
                        mapUnifiedNativeAdToLayout(unifiedNativeAd,unifiedNativeAdView);

                        //FrameLayout nativeAdLayout = new FrameLayout(Objects.requireNonNull(getActivity()));

                        FrameLayout nativeAdLayout = myview.findViewById(R.id.frame_id);
                        nativeAdLayout.removeAllViews();
                        nativeAdLayout.addView(unifiedNativeAdView);
                    }

                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        super.onAdFailedToLoad(errorCode);
                     /*   if (errorCode == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
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
                })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());

        mExpenseDatabase =FirebaseDatabase.getInstance().getReference("ExpenseData");
        mExpenseDatabase.keepSynced(true);

        //firebase database
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        String uid = Objects.requireNonNull(mUser).getUid();

        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        expenseTotalSum = myview.findViewById(R.id.expense_txt_result);

        recyclerView = myview.findViewById(R.id.recycler_id_expense);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int totalvalue = 0;

                for(DataSnapshot mysnapshot: dataSnapshot.getChildren()) {

                    Data data = mysnapshot.getValue(Data.class);

                    assert data != null;
                    totalvalue += data.getAmount();

                    String stTotalvalue = String.valueOf(totalvalue);

                    expenseTotalSum.setText("$"+stTotalvalue+".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return myview;
    }

    private void setAdapter(final String searchedString) {

        mExpenseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mdateList.clear();

                mtimeList.clear();
                mtitleList.clear();
                mnoteList.clear();
                mamountList.clear();
                recyclerView.removeAllViews();
                int amount;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String uid = snapshot.getKey();
                    String title = snapshot.child("type").getValue(String.class);
                    String note = snapshot.child("note").getValue(String.class);
                    Data data = snapshot.getValue(Data.class);
                    amount = Objects.requireNonNull(data).getAmount();

                    String date = snapshot.child("date").getValue(String.class);
                    String time = snapshot.child("time").getValue(String.class);

                    String amt =  String.valueOf("$"+amount);

                    if(Objects.requireNonNull(title).toLowerCase().contains(searchedString.toLowerCase())) {

                        mdateList.add(date);
                        mtimeList.add(time);

                        mtitleList.add(title);
                        mnoteList.add(note);
                        mamountList.add(amt);

                    }else if(Objects.requireNonNull(note).toLowerCase().contains(searchedString.toLowerCase())) {
                        mdateList.add(date);
                        mtimeList.add(time);

                        mtitleList.add(title);
                        mnoteList.add(note);
                        mamountList.add(amt);
                    }else if (Objects.requireNonNull(amt).toLowerCase().contains(searchedString.toLowerCase())) {
                        mdateList.add(date);
                        mtimeList.add(time);

                        mtitleList.add(title);
                        mnoteList.add(note);
                        mamountList.add(amt);
                }else if(Objects.requireNonNull(date).toLowerCase().contains(searchedString.toLowerCase())) {
                        mdateList.add(date);
                        mtimeList.add(time);

                        mtitleList.add(title);
                        mnoteList.add(note);
                        mamountList.add(amt);
                    }else if(Objects.requireNonNull(time).toLowerCase().contains(searchedString.toLowerCase())) {
                        mdateList.add(date);
                        mtimeList.add(time);

                        mtitleList.add(title);
                        mnoteList.add(note);
                        mamountList.add(amt);

                    }


                }
                searchexpenseAdapter = new SearchexpenseAdapter(getActivity(),mdateList,mtimeList,mtitleList,mnoteList,mamountList);
                recyclerView.setAdapter(searchexpenseAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void mapUnifiedNativeAdToLayout(UnifiedNativeAd adFromGoogle, UnifiedNativeAdView myAdView) {

        MediaView mediaView = myAdView.findViewById(R.id.ad_media);
        myAdView.setMediaView(mediaView);

        myAdView.setHeadlineView(myAdView.findViewById(R.id.ad_headline));

        myAdView.setAdvertiserView(myAdView.findViewById(R.id.ad_advertiser));

        ((TextView) myAdView.getHeadlineView()).setText(adFromGoogle.getHeadline());

        if (adFromGoogle.getAdvertiser() == null) {
            myAdView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) myAdView.getAdvertiserView()).setText(adFromGoogle.getAdvertiser());
        }

        myAdView.setNativeAd(adFromGoogle);

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter=new FirebaseRecyclerAdapter<Data,MyViewHolder>
                (
                        Data.class,
                        R.layout.expense_recycler_data,
                        MyViewHolder.class,
                        mExpenseDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder, final Data model,final int position) {

                myViewHolder.setType(model.getType());
                myViewHolder.setNote(model.getNote());
                myViewHolder.setDate(model.getDate());
                myViewHolder.setTime(model.getTime());
                myViewHolder.setAmount(model.getAmount());


                myViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        post_key = getRef(position).getKey();

                        type = model.getType();
                        note = model.getNote();
                        amount = model.getAmount();

                        updateDataItem();
                    }
                });

            }
        };

        recyclerView.setAdapter(adapter);
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        private void setDate(String date) {
            TextView mDate = mView.findViewById(R.id.date_txt_expense_id);
            mDate.setText(date);

        }

        private void setType(String type) {
            TextView mType = mView.findViewById(R.id.type_txt_expense_id);
            mType.setText(type);
        }

        private void setNote(String note) {
            TextView mNote = mView.findViewById(R.id.note_txt_expense_id);
            mNote.setText(note);

        }

        private void setTime(String time) {
            TextView mTime = mView.findViewById(R.id.time_txt_expense_id);
            mTime.setText(time);

        }

        private void setAmount(int amnt) {
            TextView mAmount = mView.findViewById(R.id.amoount_txt_expense_id);
            String stamount = String.valueOf(amnt);
            mAmount.setText("$"+stamount);

        }

    }


    private void updateDataItem() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview = inflater.inflate(R.layout.update_expense_data_item,null);

        mydialog.setView(myview);


        edtAmount = myview.findViewById(R.id.edt_amount_update_expense_id);
        edtNote = myview.findViewById(R.id.edt_note_update_expense_id);
        edtType = myview.findViewById(R.id.edt_type_update_expense_id);

        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(String.valueOf(amount).length());

        Button btnUpdate = myview.findViewById(R.id.btn_UPDATE_update_expense_id);
        Button btnDelete = myview.findViewById(R.id.btn_DELETE_update_expense_id);

        final AlertDialog dialog = mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = edtType.getText().toString().trim();
                note = edtNote.getText().toString().trim();

                String mdamount = String.valueOf(amount);

                mdamount = edtAmount.getText().toString().trim();

                int myAmount = Integer.parseInt(mdamount);

                if(TextUtils.isEmpty(mdamount)) {
                    edtAmount.setError("Field Empty!");
                    return;
                }

                if(TextUtils.isEmpty(type)) {
                    edtType.setError("Field Empty!");
                    return;
                }

                if(TextUtils.isEmpty(note)) {
                    edtNote.setError("Field Empty!");
                }

                String mDate = DateFormat.getDateInstance().format(new Date());

                String time =DateFormat.getTimeInstance().format(new Date());

                Data data = new Data(myAmount,type,note,post_key,mDate,time);
                mExpenseDatabase.child(post_key).setValue(data);

                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mExpenseDatabase.child(post_key).removeValue();
                dialog.dismiss();

            }
        });

        dialog.show();
    }
}
