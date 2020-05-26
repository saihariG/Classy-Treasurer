package com.classyinc.classytreasurer;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.classyinc.classytreasurer.Model.Data;
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

import static com.classyinc.classytreasurer.R.*;
import static java.lang.Integer.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment {

    private DatabaseReference mIncomeDatabase;
    //recyclerview...
    private RecyclerView recyclerView;

    //Text view
    private TextView incomeTotalSum;

    //UPDATE edttext...
    private EditText edtAmount;
    private EditText edtType;
    private EditText edtNote;

    //Data item Value
    private String type;
    private String note;
    private int amount;

    private String post_key;


    ArrayList<String> mamountList;
    ArrayList<String> mdateList;
    ArrayList<String> mnoteList;
    ArrayList<String> mtitleList;
    ArrayList<String> mtimeList;

    private FirebaseUser firebaseUser;
    private SearchAdapter searchAdapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myview =  inflater.inflate(layout.fragment_income, container, false);

        EditText search_edt_txt = myview.findViewById(R.id.search_edt_txt);
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference("IncomeData");
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
                 } */
            }
        });


        MobileAds.initialize(getActivity());
        AdLoader adLoader =new AdLoader.Builder(Objects.requireNonNull(getActivity()),"ca-app-pub-6826247666109501/1489960584")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        UnifiedNativeAdView unifiedNativeAdView = (UnifiedNativeAdView) getLayoutInflater().inflate(layout.native_layout,null);
                        mapUnifiedNativeAdToLayout(unifiedNativeAd,unifiedNativeAdView);

                        //FrameLayout nativeAdLayout = new FrameLayout(Objects.requireNonNull(getActivity()));

                        FrameLayout nativeAdLayout = myview.findViewById(id.frame_income_id);
                        nativeAdLayout.removeAllViews();
                        nativeAdLayout.addView(unifiedNativeAdView);
                    }

                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        super.onAdFailedToLoad(errorCode);
                        if (errorCode == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                            Toast.makeText(getActivity(), "Internal error!", Toast.LENGTH_SHORT).show();
                        }
                        else if(errorCode == AdRequest.ERROR_CODE_INVALID_REQUEST) {
                            Toast.makeText(getActivity(), "Invalid Ad Request!", Toast.LENGTH_SHORT).show();
                        }
                        else if(errorCode == AdRequest.ERROR_CODE_NETWORK_ERROR){
                            Toast.makeText(getActivity(),"Please Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                        else if(errorCode == AdRequest.ERROR_CODE_NO_FILL){
                            Toast.makeText(getActivity(),"Lack of Ads in Inventory!", Toast.LENGTH_SHORT).show();
                        }
                        else if(errorCode == AdRequest.ERROR_CODE_APP_ID_MISSING){
                            Toast.makeText(getActivity(),"App Id Missing!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })

                .build();
        adLoader.loadAd(new AdRequest.Builder().build());

        recyclerView = myview.findViewById(id.recycler_id_income);

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));

        mIncomeDatabase=FirebaseDatabase.getInstance().getReference("IncomeData");
        mIncomeDatabase.keepSynced(true);

        //database...
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        String uid = Objects.requireNonNull(mUser).getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        incomeTotalSum = myview.findViewById(id.income_txt_result);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int totalvalue = 0;

                for(DataSnapshot mysnapshot: dataSnapshot.getChildren()) {

                    Data data = mysnapshot.getValue(Data.class);

                    totalvalue += Objects.requireNonNull(data).getAmount();

                    String stTotalvalue = String.valueOf(totalvalue);

                    incomeTotalSum.setText("$"+stTotalvalue+".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return myview;
    }



    private void setAdapter(final String searchedString) {

        mIncomeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mdateList.clear();
                mtimeList.clear();
                mtitleList.clear();
                mnoteList.clear();
                mamountList.clear();
                recyclerView.removeAllViews();

                int amount ;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String uid = snapshot.getKey();
                    String title = snapshot.child("type").getValue(String.class);
                    String note = snapshot.child("note").getValue(String.class);
                    Data data = snapshot.getValue(Data.class);
                    amount = Objects.requireNonNull(data).getAmount();
                    String date = Objects.requireNonNull(snapshot.child("date").getValue(String.class));
                    String time = Objects.requireNonNull(snapshot.child("time").getValue(String.class));

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
                    }else if(amt.toLowerCase().contains(searchedString.toLowerCase())) {
                        mdateList.add(date);
                        mtimeList.add(time);
                        mtitleList.add(title);
                        mnoteList.add(note);
                        mamountList.add(amt);


                    }else if(date.toLowerCase().contains(searchedString.toLowerCase())) {

                        mdateList.add(date);
                        mtimeList.add(time);
                        mtitleList.add(title);
                        mnoteList.add(note);
                        mamountList.add(amt);

                    }else if (time.toLowerCase().contains(searchedString.toLowerCase())) {

                        mdateList.add(date);
                        mtimeList.add(time);
                        mtitleList.add(title);
                        mnoteList.add(note);
                        mamountList.add(amt);

                    }

                }
                searchAdapter = new SearchAdapter(getActivity(),mdateList,mtimeList,mtitleList,mnoteList,mamountList);

                recyclerView.setAdapter(searchAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

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

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        layout.income_recycler_data,
                        MyViewHolder.class,
                        mIncomeDatabase
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

    public static class MyViewHolder extends RecyclerView.ViewHolder  {

        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;


        }


        void setType(String type) {

            TextView mType = mView.findViewById(id.type_txt_income_id);
            mType.setText(type);
        }

        public void setNote(String note) {
            TextView mNote = mView.findViewById(id.note_txt_income_id);
            mNote.setText(note);

        }

        void setDate(String date) {
            TextView mDate = mView.findViewById(id.date_txt_income_id);
            mDate.setText(date);

        }

        void setTime(String time) {
            TextView mTime = mView.findViewById(id.time_txt_income_id);
            mTime.setText(time);

        }

        void setAmount(int amnt) {
            TextView mAmount = mView.findViewById(id.amoount_txt_income_id);
            String stamount = String.valueOf(amnt);
            mAmount.setText("$"+stamount);

        }

    }


    private void updateDataItem() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View myview = inflater.inflate(layout.update_data_item,null);

        mydialog.setView(myview);


        edtAmount = myview.findViewById(id.edt_amount_id);
        edtNote = myview.findViewById(id.edt_note_id);
        edtType = myview.findViewById(id.edt_type_id);

        //set data to edittext
        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(String.valueOf(amount).length());

        //button for upd & del
        Button btnUpdate = myview.findViewById(id.btn_UPDATE_id);
        Button btnDelete = myview.findViewById(id.btn_DELETE_id);

        final AlertDialog dialog = mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = edtType.getText().toString().trim();
                note = edtNote.getText().toString().trim();

                String mdamount = String.valueOf(amount);

                mdamount = edtAmount.getText().toString().trim();

                int myAmount = parseInt(mdamount);

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

                //String stringDate = DateFormat.getDateTimeInstance().format(mDate);
                //DateFormat.getTimeInstance();

                String time =DateFormat.getTimeInstance().format(new Date());

                Data data = new Data(myAmount,type,note,post_key,mDate,time);
                mIncomeDatabase.child(post_key).setValue(data);

                dialog.dismiss();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIncomeDatabase.child(post_key).removeValue();
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
