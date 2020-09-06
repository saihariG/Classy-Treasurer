package com.classyinc.classytreasurer.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.classyinc.classytreasurer.Model.reqFeature;
import com.classyinc.classytreasurer.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RequestFeature extends AppCompatActivity {

    private EditText edtreq;

    private DatabaseReference mReqFeatureDatabseRefer;

    Context context;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_feature);

        Toolbar toolbar = findViewById(R.id.tool_req_id);
        toolbar.setTitle("Request a Feature");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        MobileAds.initialize(context,"ca-app-pub-4710955483788759~6292166403");

        final InterstitialAd interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId("ca-app-pub-4710955483788759/7244664731");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded(){
                if(interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
         /*  @Override
            public void onAdFailedToLoad(int errorCode){
                if (errorCode == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
                    Toast.makeText(RequestFeature.this, errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_INVALID_REQUEST) {
                    Toast.makeText(RequestFeature.this, errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_NETWORK_ERROR){
                    Toast.makeText(RequestFeature.this, errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_NO_FILL){
                    Toast.makeText(RequestFeature.this, errorCode, Toast.LENGTH_SHORT).show();
                }
                else if(errorCode == AdRequest.ERROR_CODE_APP_ID_MISSING){
                    Toast.makeText(RequestFeature.this, errorCode, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onAdOpened() {

            }
            @Override
            public void onAdClosed() {


            } */

        });


        edtreq = findViewById(R.id.edt_reqFeature_id);
        Button btnreq = findViewById(R.id.btn_reqFeature_id);

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        mReqFeatureDatabseRefer = FirebaseDatabase.getInstance().getReference().child("FeatureRequests").child(uid);
        mReqFeatureDatabseRefer.keepSynced(true);

        btnreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mReq = edtreq.getText().toString();

                if (TextUtils.isEmpty(mReq)) {
                    edtreq.setError("Field Empty");
                }

                if (!TextUtils.isEmpty(mReq)) {
                    requestFeature();
                }
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
    }


    private void requestFeature() {

        String mReq = edtreq.getText().toString();


        String id = mReqFeatureDatabseRefer.push().getKey();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String email = null;
        if (firebaseUser != null) {

            email = firebaseUser.getEmail();

        }


        reqFeature reqFeature = new reqFeature(id,email,mReq);

        assert id != null;
        mReqFeatureDatabseRefer.child(id).setValue(reqFeature);

        startActivity(new Intent(context ,HomeActivity.class));

        Toast.makeText(context , "Your Request Have Been Saved.Our Team Will Contact you Shortly", Toast.LENGTH_SHORT).show();

    }


}
